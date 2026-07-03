package com.customization.task;

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
import java.util.ArrayList;
import java.util.Map;


@Path("/taskService")
public class taskService {
    BaseBean bb = new BaseBean();

    @POST
    @Path("/getTaskInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSignInfo(@Context HttpServletRequest request, @Context HttpServletResponse response)  {

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

        //获取字段配置信息表
        bb.writeLog("[taskService.getSignInfo]获取主信息模板  >>> "  + bb.getPropValue("taskServiceRefInfo","mainTask"));
        JSONObject mainTaskRef = JSONObject.parseObject(bb.getPropValue("taskServiceRefInfo","mainTask"));
        result.put("mainTaskRef",mainTaskRef);
        bb.writeLog("[taskService.getSignInfo]获取字段配置信息mainTaskRef  >>> " + mainTaskRef);

        JSONObject feedbackTaskRef = JSONObject.parseObject(bb.getPropValue("taskServiceRefInfo","feedbackTask"));
        result.put("feedbackTaskRef",feedbackTaskRef);
        bb.writeLog("[taskService.getSignInfo]获取字段配置信息detailTaskRef  >>> " + feedbackTaskRef);

        //设置主表信息查询语句
        String mainTaskSQL = "select * from TM_taskInfo where task_No = ? ";
        RecordSet rs = new RecordSet();
        RecordSet rsDt = new RecordSet();

        //查询任务主信息
        rs.executeQuery(mainTaskSQL,taskNo);
        if (rs.next()){
            JSONObject taskInfo = new JSONObject();

            //根据映射关系生成主数据
            for (Map.Entry<String, Object> entry : mainTaskRef.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                bb.writeLog("[taskService.getSignInfo]任务主信息字段  >>> " + key + " >>> " + value + "数据库值 >>> " +  rs.getString(value));
                taskInfo.put(key, rs.getString(value));
            }

            //处理任务反馈信息
            ArrayList<JSONObject> dtTaskList = new ArrayList();
            rsDt.executeQuery("select * from TM_TaskFeedback where id = ?",rs.getString("id"));
            while (rsDt.next()){
                JSONObject feedBackInfo = new JSONObject();
                //根据映射关系生成反馈数据
                for (Map.Entry<String, Object> entry : feedbackTaskRef.entrySet()) {
                    String key = entry.getKey();
                    String value = (String) entry.getValue();
                    feedBackInfo.put(key, rs.getString(value));
                    bb.writeLog("[taskService.getSignInfo]反馈信息字段  >>> " + key + " >>> " + value + "数据库值 >>> " +  rs.getString(value));
                }
                dtTaskList.add(feedBackInfo);
            }
            taskInfo.put("feedback",dtTaskList);
            result.put("state",0);
            result.put("data",taskInfo);
        }else{
            result.put("state",1);
            result.put("msg","没有查询到数据");
        }

        return String.valueOf(result);
    }


}
