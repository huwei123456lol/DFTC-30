package com.api.customization.customRemarkReport;


import com.alibaba.fastjson.JSONObject;
import com.api.workflow.util.ServiceUtil;
import com.engine.workflow.biz.RequestLogBiz;
import com.engine.workflow.biz.freeNode.FreeNodeBiz;
import com.engine.workflow.biz.requestForm.RequestFormBiz;
import com.engine.workflow.util.CollectionUtil;
import weaver.conn.RecordSet;
import weaver.crm.Maint.CustomerInfoComInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.request.RequestLogOperateName;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * @author hanjun
 */
@Path("/customRemarkReport")
public class UpdateHistoryData {
    @POST
    @Path("/updateForID")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateForID(String param){
        JSONObject req = JSONObject.parseObject(param);
        JSONObject result = new JSONObject();

        String wfID = req.getString("wfid");


        RecordSet rs = new RecordSet();
        RecordSet rsdt = new RecordSet();
        rs.executeQuery("select * from uf_sfwbbzqpz where lcmc = ?",wfID);
        while (rs.next()){
            //获取写入表名
            String remarkTable = rs.getString("bbmc");
            //获取写入字段名称
            String reamrkCol = rs.getString("qzyjlm");
            //获取节点 对应节点nodeid
            String remarkNodeId = rs.getString("qzyjjd");

            String reqSql = "select requestid from " + remarkTable;

            rsdt.execute(reqSql);
            while (rsdt.next()){
                String dtReqID = rsdt.getString("requestid");
                new BaseBean().writeLog("[UpdateHistoryData.java] ____ requestid ____" + dtReqID);
                //result.put(dtReqID,rsdt.getString("lwbt"));
                //提取对应节点签字意见
                String remarkStr = getCustomRemark(remarkNodeId, Integer.parseInt(dtReqID), Integer.parseInt(wfID));
                //更新数据到对应的字段
                RecordSet rss = new RecordSet();
                rss.executeQuery("update " + remarkTable + " set " + reamrkCol + "  = ?  where requestid = ?",remarkStr,dtReqID);
            }
            rsdt.execute("update " + remarkTable + " a  set a.lczt = 1 WHERE EXISTS (select 1 from  workflow_requestbase b where a.requestid = b.requestid and b.currentnodetype = 3 )");

        }



        return String.valueOf(result);
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
