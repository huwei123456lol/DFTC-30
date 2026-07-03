package weaver.trq.action;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Base64;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.trq.webservice.jhx.TxServiceGatewayProxy;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支票领用调用九恒星接口的Action（车都公司）
 *
 * @author GodWei
 */
public class CdgsZplySendJhxAction implements Action {

	public String execute(RequestInfo requestInfo) {
        BaseBean baseBean = new BaseBean();
        baseBean.writeLog("开始执行支票领用调用九恒星接口的Action");

        String code = "MPBS-T001";//服务代码
        String batchNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//批次号
        String lsh = "";
        String nodeId = "client.001";
        String channelId = "ERP";
        String clientId = "client.002";//客户编号ID(单位编号)	默认 client.002
        String clientName = "网上银行";//客户名称	默认 网上银行
        String txDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期时间	默认 当前时间
        String erpInsId = requestInfo.getRequestid();//ERP端付款唯一标识 默认 requestid
        String payCltNo = "";//付款单位	申请人所属分部中文名称
        String payDept = "";//部门信息	申请人部门中文名称
        String payDeptID = "";//部门信息ID
        String payAcntNo = "";//付款方账号
        String payAcntName = "";//付款方户名
        String payBank = "";//付款银行
        String payDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ;//付款日期
        String amount = "";//付款金额
        String currency = "CNY";//币种
        String propNo = "";//款项类别
        String feeType = "";//费用类型
        String flowNo = "";//现金流编号
        String bdgNo = "";//预算编号
        String receAccNo = "";//收款方账号
        String receAccName = "";//收款方户名
        String receBankNo = "";//银行大行编号
        String receOpbankName = "";//收款方开户行名称
        String receCnaps = "";//收款方开户行CNAPS号
        String regNo = "";//收款方标准地名编码
        String isPerson = "";//对公\对私
        String urgentFlag = "0";//加急标识，加急1,不加急0
        String purpose = "";//用途
        String remark = "";//附言
        String creator = "";//录入人
        String createTime = "";//录入时间
        String textValue1 = "";//供应商PK
        String textValue2 = "";//分管会计

        Property[] properties = requestInfo.getMainTableInfo().getProperty();

        for (int i = 0; i < properties.length; i++) {
            String name = properties[i].getName();
            String value = properties[i].getValue();

            //批次号 流水号
            if(name.trim().equals("lsh")){
                lsh = value.trim();
                continue;
            }

            //所属公司
            if (name.trim().equals("ssgs")) {
                payCltNo = value.trim();
                continue;
            }

            //所属部门
            if (name.trim().equals("ssbm")) {
                payDeptID = value.trim();
                continue;
            }

            //付款金额
            if (name.trim().equals("sqje")) {
                amount = value.trim();
                continue;
            }

            //费用类型
            if (name.trim().equals("lksy")) {
                feeType = value.trim();
                continue;
            }

            //收款方账号
            if (name.trim().equals("skdwzh")) {
                receAccNo = value.trim();
                continue;
            }

            //收款方户名
            if (name.trim().equals("swdw")) {
                receAccName = value.trim();
                continue;
            }

            //收款方开户行名称
            if (name.trim().equals("skdwyh")) {
                receOpbankName = value.trim();
                continue;
            }

            //对公/对私
            if (name.trim().equals("zhlx")) {
                isPerson = value.trim();
                continue;
            }

            //用途
            if (name.trim().equals("jybt")) {
                purpose = value.trim();
                continue;
            }

            //录入人
            if (name.trim().equals("sqr")) {
                creator = value.trim();
                continue;
            }

            //录入时间
            if (name.trim().equals("sqsj")) {
                createTime = value.trim();
                continue;
            }

            //分管会计
            if (name.trim().equals("xzcwfgkj")) {
                textValue2 = value.trim();
                continue;
            }
        }

        //数据处理
        RecordSet rs = new RecordSet();
        //付款单位数据
        rs.execute("select subcompanyname from hrmsubcompany where id='" + payCltNo + "'");
        if (rs.next()) {
            payCltNo = rs.getString("subcompanyname");
        }
        //部门信息数据
        rs.execute("select departmentname from hrmdepartment where id='" + payDeptID + "'");
        if (rs.next()) {
            payDept = rs.getString("departmentname");
        }
        //费用类型数据
        rs.execute("select fyxm from uf_cdfygk where id='" + feeType + "'");
        if (rs.next()) {
            feeType = rs.getString("fyxm");
        }
        //收款方户名、供应商PK数据
        rs.execute("select gysmc,ncpk from uf_gysk where id='" + receAccName + "'");
        if (rs.next()) {
            receAccName = rs.getString("gysmc");
            textValue1 = rs.getString("ncpk");
        }
        //录入人数据
        rs.execute("select lastname from hrmresource where id='" + creator + "'");
        if (rs.next()) {
            creator = rs.getString("lastname");
        }

        //分管会计数据
        rs.execute("select lastname from hrmresource where id='" + textValue2 + "'");
        if (rs.next()) {
            textValue2 = rs.getString("lastname");
        }

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
        dataJsonMap.put("PAY_CLT_NO", payCltNo);
        dataJsonMap.put("PAY_DEPT", payDept);
        dataJsonMap.put("PAY_ACNT_NO", payAcntNo);
        dataJsonMap.put("PAY_ACNT_NAME", payAcntName);
        dataJsonMap.put("PAY_BANK", payBank);
        dataJsonMap.put("PAY_DATE", payDate);
        dataJsonMap.put("AMOUNT", amount);
        dataJsonMap.put("CURRENCY", currency);
        dataJsonMap.put("PROP_NO", propNo);
        dataJsonMap.put("FEE_TYPE", feeType);
        dataJsonMap.put("FLOW_NO", flowNo);
        dataJsonMap.put("BDG_NO", bdgNo);
        dataJsonMap.put("RECE_ACC_NO", receAccNo);
        dataJsonMap.put("RECE_ACC_NAME", receAccName);
        dataJsonMap.put("RECE_BANK_NO", receBankNo);
        dataJsonMap.put("RECE_OPBANK_NAME", receOpbankName);
        dataJsonMap.put("RECE_CNAPS", receCnaps);
        dataJsonMap.put("REG_NO", regNo);
        dataJsonMap.put("IS_PERSON", isPerson);
        dataJsonMap.put("URGENT_FLAG", urgentFlag);
        dataJsonMap.put("PURPOSE", purpose);
        dataJsonMap.put("REMARK", remark);
        dataJsonMap.put("CREATOR", creator);
        dataJsonMap.put("CREATE_TIME", createTime);
        dataJsonMap.put("TEXTVALUE1", textValue1);
        dataJsonMap.put("TEXTVALUE2", textValue2);

        jsonMap.put("data", dataJsonMap);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(jsonMap);

        baseBean.writeLog("jsonObject:" + jsonObject.toJSONString());

        String encodeJson = null;
        try {
            encodeJson = encryption(jsonObject.toJSONString(), "MTIzNDU2Nzg5MDEyMzQ1Ng==", "AES");
        } catch (Exception e) {
            e.printStackTrace();
            baseBean.writeLog("传递数据加密出现异常：" + e.getMessage());
            // 阻止流程提交
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "传递数据加密发生异常,错误信息为:" + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }

        baseBean.writeLog("encodeJson:" + encodeJson);

        TxServiceGatewayProxy proxy = new TxServiceGatewayProxy();
        try {
            String result = proxy.send(encodeJson);
            baseBean.writeLog("result:" + result);
            //对返回内容进行解密
            String decodeResult = decryption(result, "MTIzNDU2Nzg5MDEyMzQ1Ng==", "AES");
            baseBean.writeLog("decodeResult:" + decodeResult);

            JSONObject resultJson = JSONObject.parseObject(decodeResult);

            //将九恒星的接口调用结果存入建模表中，用于记录调用结果
            rs.execute("insert into uf_fkcdrz(lcid,rz,ztm,ms,lsh,bm,je,cdsj,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('" + requestInfo.getRequestid() + "','" + decodeResult + "','" + resultJson.getString("resultCode") + "','" + resultJson.getString("resultMsg") + "','"+lsh+"','"+payDeptID+"','"+amount+"','"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"',228,1,0,'"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"','"+new SimpleDateFormat("HH:mm:ss").format(new Date())+"')");

            int billid=0;
            String sql="select max(id) as maxid from uf_fkcdrz";
            rs.execute(sql);
            if(rs.next()){
                billid=rs.getInt("maxid");
            }

            new ModeRightInfo().editModeDataShare(1,228,billid);

            if (!resultJson.getString("resultCode").equals("000000")) {
                baseBean.writeLog("调用九恒星接口出现错误信息返回：" + resultJson.get("resultMsg"));
                // 阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用九恒星接口出现错误信息返回:" + resultJson.get("resultMsg"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            baseBean.writeLog("调用九恒星接口出现异常：" + e.getMessage());
            // 阻止流程提交
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "流程数据提交到九恆星发生异常,错误信息为:" + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }

        baseBean.writeLog("执行支票领用调用九恒星接口的Action完毕");
        return Action.SUCCESS;
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
