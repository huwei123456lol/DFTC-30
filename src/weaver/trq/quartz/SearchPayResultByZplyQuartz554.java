package weaver.trq.quartz;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Base64;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.trq.webservice.jhx.TxServiceGatewayProxy;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询支票领用流程九恒星的支付状态的定时任务
 *
 * @author Alex.Du
 */
public class SearchPayResultByZplyQuartz554 extends BaseCronJob {
    public void execute() {
        BaseBean baseBean = new BaseBean();
        baseBean.writeLog("开始查询支票领用流程九恒星的支付状态的定时任务");

        //查询所有已归档，未获得支付成功的“支票领用流程”。
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        rs.execute("select fm.* from formtable_main_554 fm,workflow_requestbase wr where wr.currentnodetype=3 and (fm.fkzt is null or fm.fkzt='') and fm.requestid=wr.requestid and fm.sqsj>='2019-11-20'");


        String code = "MPBS-Q002";//服务代码
        String batchNo = "";//批次号
        String nodeId = "client.001";//接收节点编号ID
        String channelId = "ERP";//渠道编号ID
        String clientId = "client.002";//客户编号ID
        String clientName = "网上银行";//客户名称
        String txDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期时间	默认 当前时间
        String erpInsId = "";//ERP端付款唯一标识

        int i = 0;
        while (rs.next()) {
            batchNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+(++i);
            //获取批次号
            //batchNo = rs.getString("lsh");
            //获取ERP端付款唯一标识
            erpInsId = rs.getString("requestid");

            //将数据装入Map,用于转换成Json传递给九恒星接口
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", code);
            jsonMap.put("batchNo", batchNo);
            jsonMap.put("nodeId", nodeId);
            jsonMap.put("channelId", channelId);
            jsonMap.put("clientId", clientId);
            jsonMap.put("clientName", clientName);
            jsonMap.put("txDateTime", txDateTime);

            Map<String, String> dataJsonMap = new HashMap<String, String>();
            dataJsonMap.put("ERP_INS_ID", erpInsId);

            jsonMap.put("data", dataJsonMap);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(jsonMap);

            baseBean.writeLog("jsonObject:" + jsonObject.toJSONString());

            //将传递的JSON数据进行加密
            String encodeJson = null;
            try {
                encodeJson = encryption(jsonObject.toJSONString(), "MTIzNDU2Nzg5MDEyMzQ1Ng==", "AES");
            } catch (Exception e) {
                e.printStackTrace();
                baseBean.writeLog("传递数据加密出现异常：" + e.getMessage());
                continue;
            }

            baseBean.writeLog("encodeJson:" + encodeJson);

            //调用九恒星接口，进行数据查询
            TxServiceGatewayProxy proxy = new TxServiceGatewayProxy();
            try {
                String result = proxy.send(encodeJson);
                baseBean.writeLog("result:" + result);
                //对返回内容进行解密
                String decodeResult = decryption(result, "MTIzNDU2Nzg5MDEyMzQ1Ng==", "AES");
                baseBean.writeLog("decodeResult:" + decodeResult);

                JSONObject resultJson = JSONObject.parseObject(decodeResult);
                if (!resultJson.getString("resultCode").equals("000000")) {
                    baseBean.writeLog("调用九恒星接口出现错误信息返回：" + resultJson.get("resultMsg"));
                    continue;
                }
                //如果返回查询成功，则判断当前付款是否已有结果（0为付款成功、-1为付款失败、-2为流程中）
                JSONObject dataResult = resultJson.getJSONObject("data");

                //获取付款方式
                String fkfs = "";
                if(null!=dataResult.getString("PAY_TYPE")&&!dataResult.getString("PAY_TYPE").trim().equals("")) {
                    if (dataResult.getString("PAY_TYPE").trim().equals("1")) {
                        fkfs = "银企直联";
                    } else if (dataResult.getString("PAY_TYPE").trim().equals("2")) {
                        fkfs = "线下付款";
                    } else if (dataResult.getString("PAY_TYPE").trim().equals("3")) {
                        fkfs = "付票";
                    }
                }

                //将付款结果和付款方式更新到流程表单中
                if(dataResult.getString("RET_CODE").equals("0")){
                    rs2.execute("update formtable_main_554 set fkzt='付款成功',fkfs='"+fkfs+"' where requestid='"+rs.getString("requestid")+"'");
                }else if(dataResult.getString("RET_CODE").equals("-1")){
                    rs2.execute("update formtable_main_554 set fkzt='付款失败',fkfs='"+fkfs+"' where requestid='"+rs.getString("requestid")+"'");
                }else if(dataResult.getString("RET_CODE").equals("-3")){
                    rs2.execute("update formtable_main_554 set fkzt='付款被退回（"+dataResult.getString("ERROR_MSG")+"）',fkfs='"+fkfs+"' where requestid='"+rs.getString("requestid")+"'");
                }else if(dataResult.getString("RET_CODE").equals("-4")){
                    rs2.execute("update formtable_main_554 set fkzt='票据已开',fkfs='"+fkfs+"' where requestid='"+rs.getString("requestid")+"'");
                }
            } catch (Throwable e) {
                e.printStackTrace();
                baseBean.writeLog("调用九恒星接口出现异常：" + e.getMessage());
                continue;
            }
        }

        baseBean.writeLog("查询支票领用流程九恒星的支付状态的定时任务完毕");
    }

    /**
     * 用于对数据进行AES加密
     *
     * @param plainData
     * @param keyStr
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static String encryption(String plainData, String keyStr, String algorithm) throws Exception {
        byte[] key = Base64.decode(keyStr.getBytes());
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(1, secretKey);
        byte[] cipherByte = cipher.doFinal(plainData.getBytes());
        return new String(Base64.encode(cipherByte));
    }

    /**
     * 用于对数据进行AES解密
     *
     * @param secretData
     * @param keyStr
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static String decryption(String secretData, String keyStr, String algorithm) throws Exception {
        byte[] key = Base64.decode(keyStr.getBytes());
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Cipher cipher = null;
        cipher = Cipher.getInstance(algorithm);
        cipher.init(2, secretKey);
        byte[] cipherByte = cipher.doFinal(Base64.decode(secretData.getBytes()));
        return new String(cipherByte);
    }
}
