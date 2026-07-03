package weaver.dfqcgsjszx.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.general.TimeUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.dfqcgsjszx.util.EIP.service_client.ISendMessageServiceProxy;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 获取EIP付款信息
 * @author AlexDu
 */
public class GetEIPPayInfoQuartz extends BaseCronJob {
    @Override
    public void execute() {
        new BaseBean().writeLog("[GetEIPPayInfoQuartz]开始执行获取EIP付款信息");
        JSONObject paramHead = null;
        String result = null;

        RecordSet rs = new RecordSet();
        RecordSetTrans rst = new RecordSetTrans();
        rst.setAutoCommit(false);

        String gxrq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String gxsj = new SimpleDateFormat("HH:mm:ss").format(new Date());
        try {
            //调用EIP接口，获取付款信息
            //构建参数，调用接口
            //构建接口头部参数
            paramHead = new JSONObject();
            paramHead.put("clientCode", "DFTC_COS");
            paramHead.put("reqSerialNo", UUID.randomUUID().toString());
            paramHead.put("tradeCode", "DFG_EIP_012");
            paramHead.put("tradeDescription", "COS获取EIP付款信息");
            paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
            paramHead.put("version", "1.0");


            try {
                result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), "");
            } catch (Exception e) {
                e.printStackTrace();
                new BaseBean().writeLog("[GetEIPPayInfoQuartz]调用获取EIP付款信息接口时出现异常：" + e.getMessage());
                return;
            }

            new BaseBean().writeLog("[GetEIPPayInfoQuartz]result=" + result);

            JSONArray resultJson = JSONObject.parseObject(result).getJSONArray("DATA");

            //构建EIP标识已处理成功的付款信息接口头部参数
            paramHead.clear();
            paramHead.put("clientCode", "DFTC_COS");
            paramHead.put("reqSerialNo", UUID.randomUUID().toString());
            paramHead.put("tradeCode", "DFG_EIP_013");
            paramHead.put("tradeDescription", "COS标识已处理成功的付款信息");
            paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
            paramHead.put("version", "1.0");

            JSONArray bsyclParamBodyArray = new JSONArray();//用于记录标识已处理的body参数

            new BaseBean().writeLog("开始循环处理返回的付款信息数据（resultJson.size()="+resultJson.size()+"）");

            //查询在插入付款数据之前，付款记录表中最大的id值，用于后面的建模数据重构权限
            rs.execute("select max(id) as id from uf_cghtfkjl");
            int beforeMaxId = 0;
            if (rs.next()) {
                beforeMaxId = rs.getInt("id");
            }

            for (int i = 0; i < resultJson.size(); i++) {
                try {
                    JSONObject jsonObject = resultJson.getJSONObject(i);

                    //通过合同编号获取合同id
                    rs.execute("select id from uf_cghtjbxx where htbh = '" + jsonObject.getString("HTBH") + "'");
                    String htid = "";
                    if (rs.next()) {
                        htid = rs.getString("id");
                    }

                    //通过付款申请人（编号）查询付款申请人id
                    rs.execute("select id from hrmresource where workcode='" + jsonObject.getString("FKSQR") + "'");
                    String fksqrid = "";
                    if (rs.next()) {
                        fksqrid = rs.getString("id");
                    }

                    //插入付款数据
                    rst.execute("insert into uf_cghtfkjl(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,htbh,htmc,fksqr,fkrq,fkje,fksy,lcmc,lcdh) values('48001','1','0','"
                            + gxrq + "','"
                            + gxsj + "','"
                            + UUID.randomUUID() + "','"
                            + jsonObject.getString("HTBH") + "','"
                            + htid + "','"
                            + fksqrid + "','"
                            + jsonObject.getString("FKRQ") + "','"
                            + jsonObject.getString("FKJE") + "','"
                            + jsonObject.getString("FKSY") + "','"
                            + jsonObject.getString("LCMC") + "','"
                            + jsonObject.getString("LCBH") + "')");

                    //调用EIP标识已处理成功的付款信息
                    //构建接口内容参数
                    JSONObject paramBody = new JSONObject();
                    paramBody.put("HTBH", jsonObject.getString("HTBH"));
                    paramBody.put("LCBH", jsonObject.getString("LCBH"));
                    paramBody.put("isSUCCESS", 0);

                    bsyclParamBodyArray.add(paramBody);

                    paramBody = null;

                    //更新付款总额到合同台账
                    rst.execute("UPDATE uf_cghtjbxx SET yzfje = (SELECT SUM(FKJE) FROM uf_cghtfkjl WHERE HTBH = '"+jsonObject.getString("HTBH")+"')  WHERE HTBH = '"+jsonObject.getString("HTBH")+"'");

                    //更新付款总额到进度管控表
                    rst.execute("UPDATE uf_cggl_cgxmjdgkb SET yfkje = (SELECT SUM(FKJE) FROM uf_cghtfkjl WHERE HTBH = '"+jsonObject.getString("HTBH")+"')  WHERE htbhwb = '"+jsonObject.getString("HTBH")+"'");

                } catch (Exception e) {
                    e.printStackTrace();
                    new BaseBean().writeLog("[GetEIPPayInfoQuartz]解析获取EIP付款信息接口的调用结果出现异常：" + e.getMessage());
                    rst.rollback();
                    return;
                }
            }

            //对要传递存储标识接口的参数进行封装
            JSONObject dataParamBody = new JSONObject();
            dataParamBody.put("DATA",bsyclParamBodyArray);

            result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), dataParamBody.toJSONString());

            dataParamBody= null;

            new BaseBean().writeLog("[GetEIPPayInfoQuartz]向EIP传递付款数据存储标识result:"+result);

            if (JSONObject.parseObject(result).getIntValue("STATUS") == 0) {
                new BaseBean().writeLog("[GetEIPPayInfoQuartz]向EIP传递付款数据存储标识成功");
                //告知EIP获取付款数据标识成功
                rst.commit();
            } else {
                new BaseBean().writeLog("[GetEIPPayInfoQuartz]向EIP传递付款数据存储标识失败");
                rst.rollback();
                return;
            }

            //进行权限重构
            //查询需要进行权限重构的id
            rs.execute("select id from uf_cghtfkjl where id >"+beforeMaxId);
            while (rs.next()) {
                new ModeRightInfo().editModeDataShare(1, 48001, rs.getInt("id"));
            }
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("[GetEIPPayInfoQuartz]获取EIP付款信息出现异常：" + e.getMessage());
        }finally{
            rs = null;
            rst = null;
            gxrq = null;
            gxsj = null;
            result = null;
            paramHead = null;
        }


        new BaseBean().writeLog("[GetEIPPayInfoQuartz]执行获取EIP付款信息结束");
    }

}
