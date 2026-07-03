package weaver.dfqcgsjszx.quartz;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.pamTask.PamPlanTaskServiceProxy;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetPAMTaskInfoQuartz extends BaseCronJob {

    //同步表名
    private final String TABLENAME = "uf_pamTask";
    //同步模块moduleID
    private final int MODULEID = 103170;
    //数据创建人
    private final int  CREATEID = 1;

    @Override
    public void execute() {
        new BaseBean().writeLog("[GetPAMTaskInfoQuartz]开始执行获取PAM的任务信息");
        PamPlanTaskServiceProxy proxy =  new PamPlanTaskServiceProxy();
        RecordSet rs = new RecordSet();
        try {

            //获取需要同步的表字段信息
            rs.execute("select * from " + TABLENAME);
            String[] colName = rs.getColumnName();


            //调用接口获取任务信息
            String taskInfo = proxy.getPamPlanTaskService().getPlanTask("COS");
            //new BaseBean().writeLog("[GetPAMTaskInfoQuartz]PAM接口返回的数据projectInfos:"+taskInfo);


            //解析返回的json数据
            JSONArray joArr =  JSONArray.parseArray(taskInfo);

            //存放处理成功的任务信息JSONArray
            JSONArray resultJoArr = new JSONArray();

            //更新日期，时间
            String gxrq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String gxsj = new SimpleDateFormat("HH:mm:ss").format(new Date());


            //便利json数组
            RecordSet rsForLoop = new RecordSet();

            for (int index = 0; index < joArr.size(); index++) {
                try {
                    JSONObject jo = joArr.getJSONObject(index);

                    //获取任务编号
                    String taskNo = jo.getString("TASK_CODE");

                    //查询任务编号是否存在
                    Boolean existTask = false;
                    rsForLoop.executeQuery("select id from " + TABLENAME + " where TASK_CODE = ?",taskNo);
                    if(rsForLoop.next()){
                        existTask = true;
                    }

                    //存在该任务则更新 否则执行插入
                    if(existTask){
                        
                        String updateSQL = "update " + TABLENAME + "set ";
                        //拼接更新语句更新语句
                        for (String key : colName) {
                            String value = jo.getString(key);
                            if (value != null && !value.equals("null")) {
                                updateSQL = updateSQL + key + "='" + value + "',";
                            }
                        }
                        updateSQL = delLastComma(updateSQL) + "  where TASK_CODE = '" + taskNo + "'";

                        //rsForLoop.execute(updateSQL);

                        new BaseBean().writeLog("[GetPAMTaskInfoQuartz]跟新语句 ： " + updateSQL);

                    }else{

                        String insertSQL = "insert into " + TABLENAME + " ";
                        //拼接插入语句
                        String insertCol = "";
                        String insertValue = "";
                        for (String key : colName) {
                            String value = jo.getString(key);
                            if (value != null && !value.equals("null")) {
                                insertCol = insertCol + key + ",";
                                insertValue = insertValue + "'" + value + "',";
                            }

                        }
                        insertSQL = new StringBuilder().append(insertSQL)
                                .append(" (formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,")
                                .append(delLastComma(insertCol)).append(") values ('")
                                .append(MODULEID).append("','")
                                .append(CREATEID).append("','0','")
                                .append(gxrq).append("','")
                                .append(gxsj).append("','")
                                .append(UUID.randomUUID()).append("',")
                                .append(delLastComma(insertValue)).append(")").toString();

                        rsForLoop.execute(insertSQL);

                        //处理数据权限
                        rsForLoop.execute("select max(id) as id from " + TABLENAME);
                        if (rsForLoop.next()) {
                            new ModeRightInfo().editModeDataShare(CREATEID, MODULEID, rs.getInt("id"));
                        }
                        new BaseBean().writeLog("[GetPAMTaskInfoQuartz]插入语句 ： " + insertSQL);
                    }


                    //处理任务数据成功json
                    JSONObject resultJo = new JSONObject();
                    resultJo.put("taskCode",taskNo);
                    resultJoArr.add(resultJo);


                } catch (Exception e) {
                    e.printStackTrace();
                    new BaseBean().writeLog("处理单个任务信息时出现异常：" + e.getMessage());
                    continue;
                }


            }

            //调用isSuccess 发送处理成功的任务信息
            JSONObject taskResult = new JSONObject();
            taskResult.put("task",resultJoArr);
            new BaseBean().writeLog("[GetPAMTaskInfoQuartz] 调用isSuccess 发送处理成功的任务信息 请求" + taskResult.toString());
            String successResult = proxy.getPamPlanTaskService().isSuccess("COS", taskResult.toString());
            new BaseBean().writeLog("[GetPAMTaskInfoQuartz] 调用isSuccess 发送处理成功的任务信息 返回" + successResult);
        } catch (Exception e) {
            e.printStackTrace();
            new BaseBean().writeLog("获取PAM的任务信息出现异常：" + e.getMessage());
        }finally{
            proxy = null;
            rs = null;
        }
        new BaseBean().writeLog("[GetPAMTaskInfoQuartz]执行获取PAM的任务信息完毕");
    }

    private String delLastComma(String str){
        return str.substring(0, str.length()-1);
    }
}
