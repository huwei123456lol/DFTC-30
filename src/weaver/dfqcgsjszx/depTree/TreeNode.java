package weaver.dfqcgsjszx.depTree;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreeNode {

    private String id;
    private String supId;
    private String name;
    private String info;
    private String info2;



    private ArrayList<TreeNode> children = new ArrayList<TreeNode>();

    public TreeNode(String depId, String supDepId, String depName) {
        this.id = depId;
        this.supId = supDepId;
        this.name = depName;
        new BaseBean().writeLog("创建树对象" + depId + " ___" + supDepId + "___" + depName);
        RecordSet rs = new RecordSet();
        if(!depId.contains("usr")){
            new BaseBean().writeLog("初始化部门讯息");
            this.info = "(" + perCount(depId, rs).get("plan") + "/" + perCount(depId, rs).get("count") + ")";
            this.info2 = Util.null2String(selectLeader(depId).get("job")) + "   " + Util.null2String(selectLeader(depId).get("lastname"));
        }else{
            new BaseBean().writeLog("初始化人员讯息");
            this.info2 = new User(Integer.parseInt(depId.replace("usr",""))).getLastname();
        }
    }

    private Map<String, Integer> perCount(String depid, RecordSet rss) {
         int count = 0;
         int plan = 0;
        if (Integer.parseInt(depid) < 10000 && !depid.equals("0")) {
            //查询当前部门人数
            rss.execute("select count(*) count from hrmresource where status in (0,1,2) and departmentid = " + depid);
            if (rss.next()) {
                count += rss.getInt("count");
            }
            new BaseBean().writeLog("当前部门人数" + count);
            //查询编制人数
            rss.execute("select bm, SUM(zbz) planPerson from uf_gwbzk where bm = "+ depid + " group by bm ");
            if (rss.next()) {
                plan += rss.getInt("planPerson");
            }
            //查询子部门
            rss.execute("select * from hrmdepartment where isnull(canceled,0) <> 1 and  supdepid =" + depid);
            ArrayList<String> supDepId = new ArrayList<String>();
            while (rss.next()) {
                supDepId.add(rss.getString("id"));
            }
            for (String s : supDepId) {
                count += perCount(s, rss).get("count");
                plan += perCount(s, rss).get("plan");
            }
        } else if (Integer.parseInt(depid) >= 10000 || depid.equals("0")) {
            //查询当前分部人员
            rss.execute("select count(*) count from hrmresource where status in (0,1,2) and subcompanyid1*10000  =" + depid);
            if (rss.next()) {
                count += rss.getInt("count");
            }
            //查询当前分部编制
            rss.execute("select fb, SUM(zbz) planPerson from uf_gwbzk where fb*10000 = " + depid + " group by fb" );
            if (rss.next()) {
                plan += rss.getInt("planPerson");
            }
            //查询子分部depid
            rss.execute("select id*10000 subid from hrmsubcompany where isnull(canceled,0) <> 1 and  supsubcomid*10000 =" + depid);
            ArrayList<String> supDepId = new ArrayList<String>();
            while (rss.next()) {
                supDepId.add(rss.getString("subid"));
            }
            for (String s : supDepId) {
                count += perCount(s, rss).get("count");
                plan += perCount(s, rss).get("plan");
            }
        }
        Map<String,Integer> result = new HashMap<String, Integer>();
        result.put("count",count);
        result.put("plan",plan);
        return  result;
    }

    public void add(TreeNode node) {//递归添加节点
        if ("0".equals(node.supId)) {
            this.children.add(node);
        } else if (node.supId.equals(this.id)) {
            this.children.add(node);
        } else {
            for (TreeNode tmp_node : children) {
                tmp_node.add(node);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    private Map<String, String> selectLeader(String depid) {
        String leaderid = "";
        String sql = "";
        Map<String, String> result = new HashMap<String, String>();
        //判断depid是否大于10000  大于10000则为分部 小于10000则为部门  分别查询对应的矩阵获取相应的负责人
        RecordSet rs = new RecordSet();
        if (Integer.parseInt(depid) >= 10000) {
            sql = "select zjl leader from Matrixtable_6 where fb*10000 = " + depid;
        } else if (Integer.parseInt(depid) < 10000) {
            sql = "select bmfzr leader from Matrixtable_7 where bm = " + depid;
        }
        rs.execute(sql);
        if (rs.next()) {
            leaderid = rs.getString("leader");
            rs.execute("select a.lastname,b.JOBTITLENAME from hrmresource a left join hrmjobtitles b on a.JOBTITLE = b.id where a.id = " + leaderid);
            if (rs.next()) {
                result.put("job", rs.getString("JOBTITLENAME"));
                result.put("lastname", rs.getString("lastname"));
            }
        }
        //开始处理
        return result;
    }

    public String getInfo() {
        return info;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

}
