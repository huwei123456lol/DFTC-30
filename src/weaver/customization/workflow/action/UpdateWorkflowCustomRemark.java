package weaver.customization.workflow.action;

import com.api.workflow.util.ServiceUtil;
import com.engine.workflow.biz.RequestLogBiz;
import com.engine.workflow.biz.freeNode.FreeNodeBiz;
import com.engine.workflow.biz.requestForm.RequestFormBiz;
import com.engine.workflow.util.CollectionUtil;
import com.engine.workflow.web.WorkflowPAAction;
import weaver.conn.RecordSet;
import weaver.crm.Maint.CustomerInfoComInfo;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.request.RequestLogOperateName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateWorkflowCustomRemark implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {

        //获取当前流程requestid，workflowid
        String requestid = requestInfo.getRequestid();
        String workflowid = requestInfo.getWorkflowid();

        //根据workflowid 获取建模相关配置
        RecordSet rs = new RecordSet();
        rs.executeQuery("select * from uf_sfwbbzqpz where lcmc = ?",workflowid);
        while (rs.next()){
            //获取写入表名
            String remarkTable = rs.getString("bbmc");
            //获取写入字段名称
            String reamrkCol = rs.getString("qzyjlm");
            //获取节点 对应节点nodeid
            String remarkNodeId = rs.getString("qzyjjd");


            //提取对应节点签字意见
            String remarkStr = getCustomRemark(remarkNodeId, Integer.parseInt(requestid), Integer.parseInt(workflowid));
            //更新数据到对应的字段
            RecordSet rss = new RecordSet();
            rss.executeQuery("update " + remarkTable + " set " + reamrkCol + "  = ?  where requestid = ?",remarkStr,requestid);

        }





        return Action.SUCCESS;
    }


    private String getCustomRemark(String nodeids,int reqId,int wfid){
        String remarkStr;
        String[] nodeArr = nodeids.split(",");

        ResourceComInfo rci = null;
        CustomerInfoComInfo cic = null;

        try {
            cic = new CustomerInfoComInfo();
            rci = new ResourceComInfo();
        } catch (Exception var31) {
            var31.printStackTrace();
        }

        StringBuilder logStr = new StringBuilder();
        String var7 = "";
        new RequestLogOperateName();
        RecordSet rs = new RecordSet();
        boolean var3 = true;
        String var11 = var3 ? "\n" : "&nbsp;";
        //String timeStamp = "@@@@" + System.currentTimeMillis() + "~~~~";
        String timeStamp = "<br />";
        ArrayList<String> nodeList = new ArrayList<String>();
        nodeList.add("-1");
        nodeList.addAll(Arrays.asList(nodeArr));

        FreeNodeBiz.loadViewLogFreeNodeIds(reqId, nodeList);
        String nodeListStr = CollectionUtil.list2String(nodeList, ",");
        ArrayList reqLogList = RequestLogBiz.getRequestLog(reqId, wfid, nodeListStr, "", 9999, 1, "");

        for (int var18 = 0; var18 < reqLogList.size(); ++var18) {
            Hashtable var19 = (Hashtable) reqLogList.get(var18);
            String operatedate = Util.null2String(var19.get("operatedate"));
            String operatetime = Util.null2String(var19.get("operatetime"));
            String nodename = Util.null2String(var19.get("nodename"));
            int _nodeid = Util.getIntValue(Util.null2String(var19.get("nodeid")), 0);
            String logtype = Util.null2String(var19.get("logtype"));
            String operator = Util.null2String(var19.get("operator"));
            String operatortype = Util.null2String(var19.get("operatortype"));
            String agenttype = Util.null2String(var19.get("agenttype"));
            String agentorbyagentid = Util.null2String(var19.get("agentorbyagentid"));
            String remarkHtml = RequestFormBiz.manageCssPollute(Util.null2String((String) var19.get("remarkHtml")))
                    .replaceAll("<p>","<div>")
                    .replaceAll("</p>","</div>");

            String remarkBottomStr = "";
            if ("0".equals(operatortype)) {
                if ("2".equals(agenttype)) {
                    remarkBottomStr = Util.null2String(rci.getLastname(agentorbyagentid)) + " -&gt; ";
                }

                remarkBottomStr = remarkBottomStr + Util.null2String(rci.getLastname(operator));
            } else if ("1".equals(operatortype)) {
                remarkBottomStr = Util.null2String(cic.getCustomerInfoname(operator));
            } else {
                remarkBottomStr = SystemEnv.getHtmlLabelName(468, 7);
            }

            logStr.append("<div>[").append(nodename).append("]")

            ;
            if (!"".equals(remarkHtml)) {
                logStr.append(timeStamp).append("<div>").append(remarkHtml).append("</div>");
            }

            logStr.append("<div class='tempBR'><br /></div>").append(remarkBottomStr).append(" ").append(operatedate).append(" ").append(operatetime)
                    .append("<div class='tempBR'><br /></div><br />")
            ;
        }

        remarkStr = ServiceUtil.convertChar(logStr.toString());

        return remarkStr;
    }
}
