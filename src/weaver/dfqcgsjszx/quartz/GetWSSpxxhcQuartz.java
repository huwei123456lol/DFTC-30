package weaver.dfqcgsjszx.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.general.TimeUtil;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.ws.service_client.ISendMessageServiceProxy;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.workflow.action.Action;

import java.util.UUID;

/**
 * 获取外事系统的审批信息回传数据
 *
 * @author Alex.Du
 */
public class GetWSSpxxhcQuartz extends BaseCronJob {
    @Override
    public void execute() {
        new BaseBean().writeLog("[GetWSSpxxhcQuartz]开始执行获取外事系统的审批信息回传数据的定时任务");
        final String tableName = "formtable_main_176";

        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        //查询因公出国季度调整流程中已做了因公出国申报且没有获取到回传的批件号的流程（ygcgsb=1）
        rs.execute("select requestid,zbwsxtcgrwsbid from " + tableName + " where ygcgsb=1 and (jlhcpjh is null or jlhcpjh='')");

        //构建接口头部参数
        JSONObject paramHead = new JSONObject();
        paramHead.put("clientCode", "DFTC_COS");
        paramHead.put("reqSerialNo", UUID.randomUUID().toString());
        paramHead.put("tradeCode", "DFG_FAM_006");
        paramHead.put("tradeDescription", "获取外事系统的审批信息回传数据(" + rs.getString("requestid") + ")");
        paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
        paramHead.put("version", "1.0");

        JSONObject paramBody = new JSONObject();
        String result = null;
        //循环处理每一条，调用接口获取回传的批件号
        while (rs.next()) {
            new BaseBean().writeLog("[GetWSSpxxhcQuartz]当前要处理的requestid为：" + rs.getString("requestid"));
            //构建接口内容参数
            paramBody = new JSONObject();
            paramBody.put("GUID", rs.getString("zbwsxtcgrwsbid"));
            paramBody.put("TemplateType", "2");

            new BaseBean().writeLog("[GetWSSpxxhcQuartz]paramHead=" + paramHead);
            new BaseBean().writeLog("[GetWSSpxxhcQuartz]paramBody=" + paramBody);

            result = null;
            JSONArray resultJson = null;
            try {
                result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());

                new BaseBean().writeLog("[GetWSSpxxhcQuartz]result=" + result);
                new BaseBean().writeLog("[GetWSSpxxhcQuartz]外事系统的审批信息回传接口调用完毕");

                new BaseBean().writeLog("[GetWSSpxxhcQuartz]开始解析外事系统的审批信息回传接口的调用结果");
                resultJson = JSONArray.parseArray(result);
                new BaseBean().writeLog("[GetWSSpxxhcQuartz]解析外事系统的审批信息回传接口的调用结果完成");

                //记录返回的因公出国批件号
                String jlhcpjh = resultJson.getJSONObject(0).getJSONObject("data").getString("MissionNo").trim();
                if(jlhcpjh!=null&&!jlhcpjh.trim().equals("")&&!jlhcpjh.trim().toLowerCase().equals("null")) {
                    new BaseBean().writeLog("[GetWSSpxxhcQuartz]记录返回的因公出国批件号的SQL语句为：update " + tableName + " set jlhcpjh='" + jlhcpjh + "' where requestid=" + rs.getString("requestid"));
                    rs2.execute("update " + tableName + " set jlhcpjh='" + jlhcpjh + "' where requestid=" + rs.getString("requestid"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                new BaseBean().writeLog("[GetWSSpxxhcQuartz]（requestid:"+rs.getString("requestid")+"）调用外事系统的审批信息回传接口时出现异常：" + e.getMessage());
            } finally {
                paramBody = null;
                resultJson = null;
            }
        }

        new BaseBean().writeLog("[GetWSSpxxhcQuartz]获取外事系统的审批信息回传数据的定时任务执行完毕");
    }
}
