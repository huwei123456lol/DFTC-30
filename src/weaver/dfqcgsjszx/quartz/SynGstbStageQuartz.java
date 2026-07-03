package weaver.dfqcgsjszx.quartz;

import com.weaver.general.BaseBean;
import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.gsgl.service_client.IGatewayServiceProxy;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.TimeUtil;
import weaver.interfaces.schedule.BaseCronJob;

import java.rmi.RemoteException;

/**
 * @author jzm
 * 工时填报同步阶段数据
 */
public class SynGstbStageQuartz extends BaseCronJob {
    BaseBean log = new BaseBean();

    @Override
    public void execute() {
        log.writeLog("==============SynGstbStageQuartz_Star[" + TimeUtil.getCurrentTimeString() + "]==============");

        IGatewayServiceProxy proxy = new IGatewayServiceProxy();
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        //从数据库中查到所有项目id
        rs.execute("select tree_id from uf_gstb_project where tree_type = 'dt_tree_project' order by tree_id");          //dt_tree_project是可选的项目
        while (rs.next()){
            String tree_id = rs.getString("tree_id");                                   //项目id
            synStageListByPro(tree_id,proxy,rs2);
        }
        proxy = null;
        rs = null;
        rs2 = null;
        log.writeLog("==============SynGstbStageQuartz_End[" + TimeUtil.getCurrentTimeString() + "]==============");

    }


    //通过项目id来同步阶段
    public void synStageListByPro(String projectID, IGatewayServiceProxy proxy, RecordSet rs) {
        log.writeLog("查询的ProjectID:'"+projectID+"'");
        try {
            JSONObject json = new JSONObject();
            json.put("userid", "admin");
            json.put("projectid", projectID);

            String para = json.toString();
            String result = proxy.queryPhaseListByPro(para);

            log.writeLog("result:"+result);

            JSONObject object = new JSONObject(result);
            JSONArray data = object.getJSONArray("data");
            log.writeLog("查询到的数据数量为："+data.length());
            //遍历data。
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                String stage_id = jsonObject.getString("id");               //阶段id
                String stage_text = jsonObject.getString("text");           //阶段名称

                log.writeLog("查询到的stage_id为："+stage_id+",stage_text为："+stage_text);

                //查看数据库有没有这条数据如果有就执行更新语句，如果没有就进行插入。
                String selSql = "select stage_id from uf_gstb_stage where stage_id = '" + stage_id + "' and project_id = '"+projectID+"'";
                rs.execute(selSql);
                if (rs.next()) {
                    //查询到了数据，进行数据更新
                    String updateSql = "update uf_gstb_stage set stage_text = '" + stage_text + "' where stage_id = '" + stage_id + "' and project_id = '"+projectID+"'";
                    log.writeLog("updateSql=" + updateSql);
                    rs.execute(updateSql);
                } else {
                    //没有查询到对应的数据，插入新的项目数据，并且进行共享权限重构
                    String insertSql = "insert into uf_gstb_stage(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,stage_id,stage_text,project_id)" +
                            " values('39501','1','0','" + TimeUtil.getCurrentDateString() + "','" + TimeUtil.getOnlyCurrentTimeString() + "','" + stage_id + "','" + stage_text + "','"+projectID+"')";
                    log.writeLog("insertSql="+insertSql);
                    rs.execute(insertSql);
                    //进行权限重构
                    rs.execute("select max(id) as id from uf_gstb_stage");
                    if (rs.next()){
                        new ModeRightInfo().editModeDataShare(1, 39501, rs.getInt("id"));
                    }
                }

            }
        } catch (Exception e) {
            log.writeLog(e);
            e.printStackTrace();
        }

    }

}