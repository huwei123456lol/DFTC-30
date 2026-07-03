package weaver.dfqcgsjszx.util.syn;

import com.weaver.general.BaseBean;
import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.gsgl.service_client.IGatewayServiceProxy;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.TimeUtil;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * @author jzm
 *
 * 工时填报-项目树同步
 */
public class SynGstbProjectQuartz extends BaseCronJob {

    @Override
    public void execute() {
        new BaseBean().writeLog("==============SynGstbProjectQuartz_Star["+TimeUtil.getCurrentTimeString() +"]==============");
        //同步项目，最上级树节点id
        String projectRoot =  new weaver.general.BaseBean().getPropValue("gstb_project_root","root");

        //String [] tree_id = {"dt_platform_parea_00","dt_platform_parea_01","dt_platform_parea_02"};
        String [] treeId = projectRoot.split(",");
        IGatewayServiceProxy proxy = new IGatewayServiceProxy();
        RecordSet rs = new RecordSet();

        for (String treeID : treeId) {
            synProjectList(treeID,proxy,rs);
        }

        proxy = null;
        rs = null;
        new BaseBean().writeLog("==============SynGstbProjectQuartz_End["+TimeUtil.getCurrentTimeString() +"]==============");
    }


    public void synProjectList(String parentTreeID,IGatewayServiceProxy proxy,RecordSet rs) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("userid","admin");
            obj.put("treeid", parentTreeID);
            String para = obj.toString();


            //从接口获取返回数据
            String result = proxy.queryProjectList(para);
            new BaseBean().writeLog("[SynGstbProjectQuartz]result=" + result);
            JSONObject jsonObject = new JSONObject(result);                            //解析result数据
            JSONArray data = jsonObject.getJSONArray("data");

            //递归当data为空时就不会在递归。
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                String treeID = object.getString("tree_id");                        //树节点ID
                String treeName = object.getString("tree_name");                                    //树名称
                String treeType = object.getString("tree_type");                    //节点类型
                String pid = dataConversionPid(object.getString("pid"));                               //父节点ID
                int subnodeCount  = object.getInt("subnode_count");                                 //下级节点数量

                //查看数据库有没有这条数据如果有就执行更新语句，如果没有就进行插入。
                String selectSql = "select tree_id from uf_gstb_project where tree_id = '"+treeID+"'";
                rs.execute(selectSql);
                if (rs.next()){
                    //查询到了数据，进行数据更新
                    String updateSql = "update uf_gstb_project set tree_name='"+treeName+"',tree_type='"+treeType+"',pid='"+pid+"' where tree_id = '"+treeID+"'";
                    new BaseBean().writeLog("update sql = " + updateSql);
                    rs.execute(updateSql);
                }else {
                    //没有查询到对应的数据，插入新的项目数据，并且进行共享权限重构
                    String insertSql = "insert into uf_gstb_project(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,tree_id,tree_name,tree_type,pid) " +
                            "values('39001','1','0','" + TimeUtil.getCurrentDateString() + "','" + TimeUtil.getOnlyCurrentTimeString() + "','" + treeID + "','" + treeName + "','" + treeType + "','" + pid + "')";
                    new BaseBean().writeLog("insert sql = " + insertSql);
                    rs.execute(insertSql);
                    //进行权限重构
                    rs.execute("select max(id) as id from uf_gstb_project");
                    if (rs.next()) {
                        new ModeRightInfo().editModeDataShare(1, 39001, rs.getInt("id"));
                    }
                }

                //如果下级节点数量大于0，则以当前节点的treeID进行同步
                if(subnodeCount>0) {
                    synProjectList(treeID, proxy, rs );
                }
            }
        } catch (Exception e) {
            new BaseBean().writeLog(e);
            e.printStackTrace();
        }
    }

    //处理上级ID，将dt_platform_parea_00替换成00，将dt_platform_parea_01替换成01，将dt_platform_parea_02替换成02
    public static String dataConversionPid(String pID){
        return pID.replaceAll("dt_platform_parea_","");
    }
}
