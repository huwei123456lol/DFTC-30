package com.api.customization.task;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Path("/taskService2")
public class taskService2 {
    BaseBean bb = new BaseBean();

    @POST
    @Path("/getTaskInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTaskInfo(@Context HttpServletRequest request, @Context HttpServletResponse response)  {

        /**
         * author : Hanjun
         * descript : 通过task_No获取任务详细信息
         * request param : "task_No": "20210001"
        * */

        //获取request参数task_No
        String taskNo = Util.null2String(request.getParameter("task_No"));
        bb.writeLog("[taskService.getSignInfo]查询参数task_No  >>> " + taskNo );


        //定义result
        JSONObject result = new JSONObject();

        //设置主表信息查询语句
        String mainTaskSQL = "select a.id,b.task_scene,b.task_sceneNo,b.rwdh task_No,b.reason,a.name,b.supplier_no,b.supplier_name," +
                "a.parentid,a.principalid,a.lev,a.begindate,a.enddate,b.completedata,a.remark,a.risk,a.difficulty,a.assist" +
                " from tm_taskinfo a left join ecme_tableextend_501 b on a.id = b.dataid where b.rwdh = '" + taskNo +"'";
        RecordSet rs = new RecordSet();
        RecordSet rsDt = new RecordSet();

        //查询任务主信息
        rs.execute(mainTaskSQL);
        if (rs.next()){
            JSONObject taskInfo = new JSONObject();
            for (int i = 1; i <= rs.getColCounts(); i++) {
                String colName = rs.getColumnName(i);
                String value = rs.getString(colName);
                if(colName.equals("PRINCIPALID")){
                    value = convertName(value);
                }
                bb.writeLog("[taskService.getSignInfo]任务信息字段  >>> " + colName + " >>> " + value );
                taskInfo.put(colName,value);
            }

            String taskid = rs.getString("id");
            RecordSet rsPt = new RecordSet();
            rsPt.execute("select listagg(partnerid,',') partnerids from TM_TaskPartner where taskid = " + taskid);
            if(rsPt.next()){
                taskInfo.put("PARTNERIDS",convertName(rsPt.getString("PARTNERIDS")));
            }


            //处理任务反馈信息
            ArrayList<JSONObject> dtTaskList = new ArrayList();
            rsDt.execute("select dbms_lob.substr(content) feedback_content,createdate feedback_time,hrmid feedback_person from TM_TaskFeedback where taskid = " + taskid);
            while (rsDt.next()){
                JSONObject feedBackInfo = new JSONObject();
                //根据映射关系生成反馈数据
                for (int j = 1; j <= rsDt.getColCounts(); j++) {
                    String colName = rsDt.getColumnName(j);
                    String value = rsDt.getString(colName);
                    if(colName.equals("FEEDBACK_PERSON") ){
                        value = convertName(value);
                    }
                    feedBackInfo.put(colName,value);
                    bb.writeLog("[taskService.getSignInfo]反馈信息字段  >>> " + colName + " >>> " + value );
                }
                dtTaskList.add(feedBackInfo);
            }
            if(dtTaskList.size() > 0 ){
                taskInfo.put("FEEDBACK",dtTaskList);
            }

            result.put("state",0);
            result.put("data",taskInfo);
        }else{
            result.put("state",1);
            result.put("msg","没有查询到数据");
        }

        return String.valueOf(result);
    }

    @POST
    @Path("/addTaskFeedBack")
    @Produces(MediaType.TEXT_PLAIN)
    public String addTaskFeedBack(@Context HttpServletRequest request, @Context HttpServletResponse response)  {
        //获取request参数task_No
        String taskNo = Util.null2String(request.getParameter("task_No"));
        String feedbackContent = Util.null2String(request.getParameter("feedback_content"));
        bb.writeLog("[taskService.addTaskFeedBack]查询参数task_No  >>> " + taskNo );
        bb.writeLog("[taskService.addTaskFeedBack]查询参数feedback_content  >>> " + feedbackContent );

        //定义result
        JSONObject result = new JSONObject();

        //根据taskNo 查询任务主信息ID
        RecordSet rs = new RecordSet();
        String taskID;
        String principalID;
        String mainTaskSQL = "select a.id,a.principalid" +
                " from tm_taskinfo a left join ecme_tableextend_501 b on a.id = b.dataid where b.rwdh = '" + taskNo +"'";
        rs.execute(mainTaskSQL);

        if(rs.next()){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            String nowDay = dateFormat.format(new Date());
            String nowTime = timeFormat.format(new Date());

            taskID = rs.getString("id");
            principalID = rs.getString("principalid");
            RecordSet rs1 = new RecordSet();

            try {
                Boolean check = rs1.execute("insert into TM_TaskFeedback (taskid,content,hrmid,createdate,createtime)" +
                        "values('" + taskID +
                        "','" + feedbackContent +
                        "','" + principalID +
                        "','" + nowDay +
                        "','" + nowTime + "')");
                bb.writeLog("[taskService.addTaskFeedBack]insert结果  >>> " + rs1.getFlag() + " msg" + rs1.getMsg() );
                if(check){
                    result.put("state","0");
                }else{
                    result.put("state","1");
                    result.put("msg","插入失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("state","1");
                result.put("msg",e.getMessage());
            }
        }else{
            result.put("state","1");
            result.put("msg","没有查询到任务主信息");
        }

        return String.valueOf(result);
    }




        private String convertName(String ids){
        RecordSet rs = new RecordSet();
        String lastnames = ids;
        rs.execute("select listagg(lastname,',') lastnames from hrmresource where id in (" + ids + ")");
        if(rs.next()){
            lastnames = rs.getString("lastnames");
        }
        return lastnames;
    }

}
