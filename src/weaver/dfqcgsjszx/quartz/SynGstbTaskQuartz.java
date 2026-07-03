package weaver.dfqcgsjszx.quartz;

import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.gsgl.service_client.IGatewayServiceProxy;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.schedule.BaseCronJob;


/**
 * @author jzm
 *
 * 工时填报同步任务数据
 */
public class SynGstbTaskQuartz extends BaseCronJob {
    BaseBean log = new BaseBean();
    public void execute() {
        log.writeLog("======================SynGstbTaskQuartz_Star[" + TimeUtil.getCurrentTimeString() + "]======================");
        IGatewayServiceProxy proxy = new IGatewayServiceProxy();
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();

        rs.execute("select stage_id,project_id from uf_gstb_stage");
        while (rs.next()){
            String stage_id = rs.getString("stage_id");                     //阶段id
            String project_id = rs.getString("project_id");                 //项目id

            synTask(project_id,stage_id,proxy,rs2);
        }
        proxy = null;
        rs = null;
        log.writeLog("======================SynGstbTaskQuartz_Star[" + TimeUtil.getCurrentTimeString() + "]======================");
    }


    /**
     *通过项目id和阶段id同步任务数据
     * @param projectId         项目id
     * @param stageId           阶段id
     * @param proxy             webService接口
     * @param rs                本地数据源
     */
    public void synTask(String projectId,String stageId,IGatewayServiceProxy proxy, RecordSet rs){
        try {
            JSONObject json = new JSONObject();
            json.put("userid","admin");
            json.put("projectid",projectId);                               //项目id
            json.put("phaseid",stageId);                                   //阶段id

            String para = json.toString();
            String result = proxy.queryWorkTaskListByPro(para);

            JSONObject object = new JSONObject(result);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                String taskId = jsonObject.getString("id");                    //任务id
                String taskName = jsonObject.getString("text");                //任务名称

                //查询有无对应数据，如果有就更新如果没有就插入数据
                String selSql = "select task_id from uf_gstb_task where project_id = '"+projectId+"' and stage_id = '"+stageId+"' and task_id = '"+taskId+"'";
                rs.execute(selSql);
                if (rs.next()){
                    //通过projectId和stageId、taskId来获取要更新的task_text
                    String updateSql = "update uf_gstb_task set task_text where project_id = '"+projectId+"' and stage_id = '"+stageId+"' and task_id = '"+taskId+"'";
                    log.writeLog("updateSql="+updateSql);
                    rs.execute(updateSql);
                }else {
                    //查询当前阶段的数据ID
                    String stageDataID = "";
                    rs.execute("select id from uf_gstb_stage where project_id = '"+projectId+"' and stage_id = '"+stageId+"'");
                    if(rs.next()){
                        stageDataID = rs.getString("id");
                    }

                    String insertSql = "insert into uf_gstb_task(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,project_id,stage_id,stage_data_id,task_id,task_text) " +
                            "values('40001','1','0','" + TimeUtil.getCurrentDateString() + "','" + TimeUtil.getOnlyCurrentTimeString() + "','"+projectId+"','"+stageId+"','"+stageDataID+"','"+taskId+"','"+taskName+"')";
                    log.writeLog("insertSql ="+insertSql);
                    rs.execute(insertSql);
                    rs.execute("select max(id) as id from uf_gstb_task");
                    if (rs.next()){
                        new ModeRightInfo().editModeDataShare(1, 40001, rs.getInt("id"));
                    }
                }
            }


        } catch (Exception e) {
            log.writeLog(e);
            e.printStackTrace();
        }


    }




}
