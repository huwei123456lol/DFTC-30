package weaver.dfqcgsjszx.depTree;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

import java.util.ArrayList;

/**
 * @Author : Hanjun
 * @Date : 19-6-13 上午11:43
 * @Description : 用于处理组织结构树JSON数据生成
 */


public class TreeDataInit {
    /**
     * @Author : Hanjun
     * @Date : 19-6-13 下午12:33
     * @parm : [subid] 分部ID
     */
    public JSONObject treeJson(String subid) {
        new BaseBean().writeLog("开始填充做组织树");
        TreeNode mainNode = new TreeNode("0", "0", "友芝友集团");
        boolean subCheck = true;
        String supSubid = subid;
        String allSubid = "";
        if (!supSubid.equals("0")){
            //处理分公司数据
            supSubid = supSubid + "0000";
            new BaseBean().writeLog("处理分公司数据" + subid);
            RecordSet rss = new RecordSet();
            rss.execute("select subcompanydesc from HrmSubCompany where id =" + subid);
            String depname = "";
            if(rss.next()){
                depname = rss.getString("subcompanydesc");
            }
            TreeNode subNode = new TreeNode(supSubid, "0", depname);
            mainNode.add(subNode);

        }

        //处理分部数据

        new BaseBean().writeLog("处理分部数据" + supSubid);
        while (subCheck) {
            RecordSet rsSub = new RecordSet();
            rsSub.execute("select id*10000 id,subcompanydesc,supsubcomid*10000 supsubcomid from HrmSubCompany where isnull(canceled,0) <> 1 and supsubcomid*10000 in (" + supSubid + ") order by showorder");
            new BaseBean().writeLog(rsSub.getCounts());
            ArrayList<Integer> supList = new ArrayList<Integer>();
            if(rsSub.getCounts() >0 ){
                while (rsSub.next()) {
                    TreeNode childNode = new TreeNode(rsSub.getString("id"), rsSub.getString("supsubcomid"), rsSub.getString("subcompanydesc"));
                    mainNode.add(childNode);
                    supList.add(rsSub.getInt("id"));
                    allSubid += rsSub.getString("id") + ",";
                }
                if (supList.size() <= 0) {
                    subCheck = false;
                    new BaseBean().writeLog("分部循环终止");
                } else {
                    supSubid = supList.toString().replace("[", "").replace("]", "");
                }
                rsSub = null;
            }else{   //无子分部时
                subCheck = false;
            }

        }

        if(allSubid.equals("")){
            allSubid = supSubid;
        }else{
            allSubid = allSubid.substring(0, allSubid.length() - 1);
        }
        //处理一级部门数据
        new BaseBean().writeLog("处理一级部门数据" + allSubid);
        String leve1Dep = "";
        RecordSet rsDep1 = new RecordSet();
        rsDep1.execute("select id,departmentname,subcompanyid1*10000 subcompanyid1 from HrmDepartment where isnull(canceled,0) <> 1 and supdepid = 0 and subcompanyid1*10000 in  (" + allSubid + ") order by showorder");
        while (rsDep1.next()) {
            TreeNode childNode = new TreeNode(rsDep1.getString("id"), rsDep1.getString("subcompanyid1"), rsDep1.getString("departmentname"));
            mainNode.add(presonConvert(childNode));
            leve1Dep += rsDep1.getString("id") + ",";
        }
        leve1Dep = leve1Dep.substring(0, leve1Dep.length() - 1);


        //处理一级部门以外所有部门
        boolean depCheck = true;
        while (depCheck) {
            RecordSet rsSub = new RecordSet();
            rsSub.execute("select id,departmentname,supdepid from HrmDepartment where isnull(canceled,0) <> 1 and supdepid <> 0 and supdepid in  (" + leve1Dep + ") order by showorder");
            new BaseBean().writeLog(rsSub.getCounts());
            ArrayList<Integer> supList = new ArrayList<Integer>();
            while (rsSub.next()) {
                TreeNode childNode = new TreeNode(rsSub.getString("id"), rsSub.getString("supdepid"), rsSub.getString("departmentname"));
                mainNode.add(presonConvert(childNode));
                supList.add(rsSub.getInt("id"));
            }
            if (supList.size() <= 0) {
                depCheck = false;
            } else {
                leve1Dep = supList.toString().replace("[", "").replace("]", "");
            }
        }

        new BaseBean().writeLog(JSONObject.toJSONString(mainNode));
        return JSONObject.parseObject(JSONObject.toJSONString(mainNode));
    }

    //处理部门中的人员
    private TreeNode presonConvert(TreeNode deptree){
        int depid = Integer.parseInt(deptree.getId());
        RecordSet rss = new RecordSet();
        String sql = "select a.id,a.lastname,b.JOBTITLENAME from hrmresource a left join hrmjobtitles b on a.JOBTITLE = b.id where status in (0,1,2) and departmentid = " + depid + " order by a.seclevel desc,a.dsporder asc";
        String tempid = String.valueOf(depid);
        rss.execute(sql);
        while (rss.next()){
            deptree.add(new TreeNode("usr"+rss.getString("id"),tempid,rss.getString("JOBTITLENAME")));
            new BaseBean().writeLog(deptree.getName() +"中人员" + rss.getString("lastname"));
            tempid = "usr" + rss.getString("id");
        }
        return deptree;
    }

}
