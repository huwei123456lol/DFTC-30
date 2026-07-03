package com.customization.api.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.engine.core.cfg.annotation.CommandDynamicProxy;
import com.engine.core.interceptor.AbstractCommandProxy;
import com.engine.core.interceptor.Command;
import com.engine.hrm.cmd.chart.GetOrgChartDataCmd;
import com.weaver.general.BaseBean;
import com.weaverboot.tools.logTools.LogTools;
import weaver.conn.RecordSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("ALL")
@CommandDynamicProxy(target = GetOrgChartDataCmd.class, desc="附加在类型保存上的示例代理程序")
public class orgTreeGetData extends AbstractCommandProxy<Map<String, Object>> {

    private JSONArray bfPersonCount ;//重计算人数前JSON数据
    private JSONArray afPersonCount = new JSONArray(); //重计算人数后JSON数据
    private JSONObject topNode; //头部节点

    //重计算人数方法，递归处理
    private void perCountCal(JSONObject jsonObject){
        LogTools.info("TEST11");
        Integer childCount = 0;
        String id = jsonObject.getString("id");
        String pid = jsonObject.getString("pid");
        Iterator it = bfPersonCount.iterator();
        while (it.hasNext()){
            JSONObject jo = (JSONObject) it.next();
            new BaseBean().writeLog("开始处理 " + jo.getString("title") + "部门人员");
            if(jo.getString("pid").equals(jsonObject.getString("id"))){
                perCountCal(jo);
                childCount = childCount + jo.getIntValue("subRCount");
            }
        }
        if(childCount == 0){
            jsonObject.put("hasChild","false");
            jsonObject.put("needPlus","false");
        }
        if(jsonObject.getString("type").equals("company")){
            jsonObject.put("num",childCount.toString());
            jsonObject.put("nTitle","技术中心人力资源总计" + childCount.toString());
        }else{
            childCount = childCount + jsonObject.getIntValue("num");
            jsonObject.put("subRCount",childCount.toString());
            jsonObject.put("subTitle","含下级部门人员" + childCount.toString());
        }
        afPersonCount.add(jsonObject);
    }
    @Override
    public Map<String, Object> execute(Command<Map<String, Object>> targetCommand) {
        new BaseBean().writeLog(getClass().getName() + "command 执行之前做一些事1111111");
        Map<String, Object> result = nextExecute(targetCommand);
        new BaseBean().writeLog(this.getClass().getName() + "command2 执行之后做一些事");
        RecordSet rs1 = new RecordSet();
        //格式化数据
        JSONArray jsonArr = JSONArray.parseArray((String) result.get("data"));
        //Step1.去除所有不属于技术中心的部门数据
        rs1.execute("select a.id from hrmdepartment a LEFT JOIN HRMDEPARTMENTDEFINED b on a.id = b.deptid  where a.subcompanyid1 = 1522 and nvl(b.ZZJGTZSFBXS,'0') != '1'");
        ArrayList<String> keyDepart = new ArrayList<>();
        while (rs1.next()){
            keyDepart.add(rs1.getString("id"));
        }
        rs1 = null;
        Iterator jsonit = jsonArr.iterator();
        while (jsonit.hasNext()) {
            JSONObject depInfo = (JSONObject) jsonit.next();
            boolean checked = false;
            for (Iterator<String> listit = keyDepart.iterator();listit.hasNext();) {
                String ls = "d" + listit.next();
                if(ls.equals(depInfo.getString("id"))){
                    listit.remove();
                    checked = true;
                }
            }
            if (!checked && !depInfo.getString("type").equals("subcompany")){
                jsonit.remove();
            }
        }

        new BaseBean().writeLog(JSONArray.toJSONString(jsonArr));
        //Step2.去除除技术中心外的所有分部数据、去除顶部company节点
        //Step3.修改技术中心分部类型 subcompany -> company
        //Step4.将pid=1522的type = dept类型数据，type改为subcompany
        Iterator jsonit1 = jsonArr.iterator();
        ArrayList<String> _firstDep = new ArrayList<>();
        while (jsonit1.hasNext()) {
            JSONObject depInfo = (JSONObject) jsonit1.next();
            String depid =  depInfo.getString("id");
            String depType =  depInfo.getString("type");
            if(!"1522".equals(depid) && "subcompany".equals(depType)){
                jsonit1.remove(); //去除除技术中心外的所有分部数据
            }else if("company".equals(depType) && !"1522".equals(depid)){
                jsonit1.remove(); //去除顶部company节点
            }else if("1522".equals(depid) && "subcompany".equals(depType)){
                depInfo.put("type","company"); //修改技术中心分部类型 subcompany -> company
                topNode = depInfo;
            }else if("1522".equals(depInfo.getString("pid"))){
                depInfo.put("type","subcompany"); //将pid=1522的type = dept类型数据，type改为subcompany
                _firstDep.add(depid);
            }
        }
        jsonit1 = jsonArr.iterator();
        new BaseBean().writeLog(getClass().getName() + "处理1及部门数据" + _firstDep.toString());
        while (jsonit1.hasNext()) {
            JSONObject depInfo = (JSONObject) jsonit1.next();
            String pid = depInfo.getString("pid");
            if(_firstDep.indexOf(pid) != -1){
                new BaseBean().writeLog(getClass().getName() + "====PID====" + pid);
                depInfo.put("type","subcompany");
            }
        }
        bfPersonCount = jsonArr;

        //Step5.人员重构
        perCountCal(topNode);

        result.put("data",JSONArray.toJSONString(afPersonCount));

        return result;
    }

}
