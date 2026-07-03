package com.customization.service_proxy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.engine.core.cfg.annotation.ServiceDynamicProxy;
import com.engine.core.cfg.annotation.ServiceMethodDynamicProxy;
import com.engine.core.impl.aop.AbstractServiceProxy;
import com.engine.portal.service.ElementsService;
import com.engine.portal.service.impl.ElementsServiceImpl;
import weaver.dfqcgsjszx.util.AMS.service_client.ITaskToDoServiceProxy;
import weaver.dfqcgsjszx.util.EIP.service_client.ISendMessageServiceProxy;
import weaver.dfqcgsjszx.util.MAS.service_client.OASoapProxy;
import weaver.dfqcgsjszx.util.PAM.service_client.DbWorkWebServiceProxy;
import weaver.dfqcgsjszx.util.QIS.service_client.IUserWSProxy;
import weaver.dfqcgsjszx.util.SLM.service_client.TaskServiceProxy;
import weaver.dfqcgsjszx.util.IPMS.service_client.PatWaitReviewServiceSoapProxy;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.hrm.User;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程中心的待办列表增加第三方系统待办数量数据
 * @author Alex.Du
 */
@ServiceDynamicProxy(target = ElementsServiceImpl.class, desc = "为流程中心的待办列表增加第三方系统待办数量数据")
public class CustomElementsService extends AbstractServiceProxy implements ElementsService {

    @Override
    @ServiceMethodDynamicProxy(desc = "为流程中心的待办列表增加第三方系统待办数量数据")
    public Map<String, Object> getElementJson(Map<String, Object> params, User user) throws Exception {
        //对参数做预处理
        //TODO

        new BaseBean().writeLog("为流程中心的待办列表增加第三方系统待办数量数据Begin");

        new BaseBean().writeLog("params:"+params);

        //调用被代理类方法
        Map<String, Object> result = (Map<String, Object>) executeMethod(params, user);

        new BaseBean().writeLog("result:"+result);


        try {
            //将map转换成JSONObject对象
            JSONObject resultJson = JSONObject.parseObject(JSONObject.toJSONString(result),Feature.OrderedField);

            //判断当前处理的数据是否为待办事宜，如果不是则直接return
            if(!resultJson.getJSONObject("data").getJSONObject("tabsetting").getJSONObject("more").getString("tabTitle").equals("~`~`7 待办事宜`~`8 To-do`~`~")){
                new BaseBean().writeLog("当前不是待办事宜的TAB数据，直接return");
                return result;
            }

            //获取JSON对象中的待办数据数组
            JSONArray dataJsonArray = resultJson.getJSONObject("data").getJSONArray("data");

            /**
            //获取各个系统的待办数量
            Map<String, JSONObject> dbslMap = new HashMap<String,JSONObject>();//用于存储各个系统待办数据的Map

            //获取EIP的待办数量
            new BaseBean().writeLog("开始获取EIP的待办数量："+new Date());
            try{
                String sendHead = "{\"clientCode\":\"DFTC_COS\",\"tradeCode\":\"DFG_EIP_011\",\"reqSerialNo\":\""+TimeUtil.getCurrentTimeString()+"\",\"tradeDescription\":\"COS待办集成\",\"tradeTime\":\"" + TimeUtil.getCurrentTimeString() + "\",\"version\":\"1.0\"}";
                String sendBody = "{\"PARAM\":{\"workcode\": \"" + user.getLoginid() + "\"}}";
                String res = new ISendMessageServiceProxy().sendMessage(sendHead, sendBody);
                new BaseBean().writeLog("EIP的待办数量返回："+res);
                JSONObject data = JSONObject.parseObject(res);

                //如果EIP系统没有目前的loginid，url为空
                if ("".equals(data.getString("url"))) {
                    data.put("url", "http://10.2.19.187/erm/sys/portal/page.jsp?from=COS");
                }

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("EIP",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取EIP待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取EIP的待办数量结束："+new Date());


            //获取AMS的待办数量
            new BaseBean().writeLog("开始获取AMS的待办数量："+new Date());
            try{
                String amsResutlt = new ITaskToDoServiceProxy().taskToDo(user.getLoginid());
                new BaseBean().writeLog("AMS的待办数量返回："+amsResutlt);
                JSONObject data = JSONObject.parseObject(amsResutlt);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("AMS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取AMS待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取AMS的待办数量结束："+new Date());

            //获取MAS的待办数量
            new BaseBean().writeLog("开始获取MAS的待办数量："+new Date());
            try{
                String masResult = new OASoapProxy().getTaskByEmpNo(user.getLoginid());
                new BaseBean().writeLog("MAS的待办数量返回："+masResult);
                JSONObject data = JSONObject.parseObject(masResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("MAS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取MAS待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取MAS的待办数量结束："+new Date());

            //获取PAM的待办数量
            new BaseBean().writeLog("开始获取PAM的待办数量："+new Date());
            try{
                String pamResult = new DbWorkWebServiceProxy().getPamDbWork(user.getLoginid());
                new BaseBean().writeLog("PAM的待办数量返回："+pamResult);
                JSONObject data = JSONObject.parseObject(pamResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("PAM",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取PAM待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取PAM的待办数量结束："+new Date());

            //获取QIS的待办数量
            new BaseBean().writeLog("开始获取QIS的待办数量："+new Date());
            try{
                String qisResult = new IUserWSProxy().queryoanum(user.getLoginid());
                new BaseBean().writeLog("QIS的待办数量返回："+qisResult);
                JSONObject data = JSONObject.parseObject(qisResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("QIS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取QIS待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取QIS的待办数量结束："+new Date());

            //获取SLM的待办数量
            new BaseBean().writeLog("开始获取SLM的待办数量："+new Date());
            try{
                String slmResult = new TaskServiceProxy().taskToDo(user.getLoginid());
                new BaseBean().writeLog("SLM的待办数量返回："+slmResult);
                JSONObject data = JSONObject.parseObject(slmResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("SLM",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取SLM待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取SLM的待办数量结束："+new Date());

            //获取IPMS的待办数量
            new BaseBean().writeLog("开始获取IPMS的待办数量："+new Date());
            try{
                String ipmsResult = new PatWaitReviewServiceSoapProxy().getJobCountAndUrl(user.getLoginid());
                new BaseBean().writeLog("IPMS的待办数量返回："+ipmsResult);
                JSONObject data = JSONObject.parseObject(ipmsResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("IPMS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取IPMS待办数量时出现异常:"+e.getMessage());
            }
            new BaseBean().writeLog("获取IPMS的待办数量结束："+new Date());

            */

            //循环处理每个待办，如果receivedata大于当前日期3天，则在标题前面加上红色的超时，否则在标题前面加上绿色的正常
            //获取三天前的日期
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.add(Calendar.DAY_OF_MONTH,-3);
            String lastThreeDay = new SimpleDateFormat("yyyy-MM-dd").format(nowCalendar.getTime());

            new BaseBean().writeLog("begin循环处理COS的待办");

            for(int i = 0;i<dataJsonArray.size();i++){
                //如果该COS系统的待办的日期小于三天前的日期，则在标题前面加上红色的超时
                if(null!=dataJsonArray.getJSONObject(i).getString("receivedate")&&(dataJsonArray.getJSONObject(i).getString("receivedate").compareTo(lastThreeDay)<0)){
                    Map cosRequestName = (Map)dataJsonArray.getJSONObject(i).get("requestname");
                    cosRequestName.put("name","<span style='color:red;'>超时</span> "+cosRequestName.get("name"));
                }else{
                    Map cosRequestName = (Map)dataJsonArray.getJSONObject(i).get("requestname");
                    cosRequestName.put("name","<span style='color:green;'>正常</span> "+cosRequestName.get("name"));
                }
            }

            new BaseBean().writeLog("end循环处理COS的待办");

            /**

            //待办跳转处理中转JSP地址
            final String dbsl_jump = "/dfqcgsjszx/dbsl_jump.jsp?url=";

            //循环处理dbslMap中含有的异构系统待办数量，将每个异构系统的待办信息放入待办列表
            Iterator<String> keyIterator = dbslMap.keySet().iterator();
            while(keyIterator.hasNext()){
                String sysName = keyIterator.next();


                new BaseBean().writeLog("while sysName:"+sysName);

                JSONObject sysData = dbslMap.get(sysName);
                //增加一条新的数据
                JSONObject newObject = new JSONObject(new LinkedHashMap());
                Map<String,String> newObjectRequestname = new HashMap<String,String>();
                newObjectRequestname.put("lasttitle","");
                newObjectRequestname.put("img","");
                newObjectRequestname.put("pretitle","");
                newObjectRequestname.put("requestid","");
                newObjectRequestname.put("name",sysName+"系统有【<span style='color:red;'>"+sysData.getString("count")+"</span>】条新的待办");
                newObjectRequestname.put("link",dbsl_jump+ URLEncoder.encode(sysData.getString("url"),"utf-8")+"&sysName="+sysName);
                newObjectRequestname.put("mobilelink",dbsl_jump+ URLEncoder.encode(sysData.getString("url"),"utf-8")+"&sysName="+sysName);
                newObject.put("sysname",sysName);
                newObject.put("requestname",newObjectRequestname);
                newObject.put("receivedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                dataJsonArray.add(0,newObject);

                //如果末尾的一条数据属于COS系统，删除末尾的一条数据
//                new BaseBean().writeLog("remove last by sysName:"+dataJsonArray.getJSONObject(dataJsonArray.size() - 1).getString("sysname").equals("COS"));
//                if(dataJsonArray.getJSONObject(dataJsonArray.size() - 1).getString("sysname").equals("COS")) {
//                    dataJsonArray.remove(dataJsonArray.size() - 1);
//                }
            }
             */

            //将处理过的JSON数据转换回Map
            Map changeResult = dataJsonArray.toJavaObject(resultJson, Map.class);

            new BaseBean().writeLog("result2:" + changeResult);
            result = changeResult;

        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("为流程中心的待办列表增加第三方系统待办数量数据出现异常:"+e.getMessage());
        }

        new BaseBean().writeLog("为流程中心的待办列表增加第三方系统待办数量数据End");
        return result;
    }

    @Override
    @ServiceMethodDynamicProxy(desc = "为流程中心的待办列表增加第三方系统待办数量数据(点击tab页）")
    public Map<String, Object> getElementTabContentDataJson(Map<String, Object> params, User user) throws Exception {
        new BaseBean().writeLog("为流程中心的待办列表(点击tab页）增加第三方系统待办数量数据Begin");

        new BaseBean().writeLog("params:"+params);

        //调用被代理类方法
        Map<String, Object> result = (Map<String, Object>) executeMethod(params, user);

        new BaseBean().writeLog("result:"+result);

        try {
            //将map转换成JSONObject对象
            JSONObject resultJson = JSONObject.parseObject(JSONObject.toJSONString(result),Feature.OrderedField);

            //判断当前处理的数据是否为待办事宜，如果不是则直接return
            if(!resultJson.getJSONObject("tabsetting").getJSONObject("more").getString("tabTitle").equals("~`~`7 待办事宜`~`8 To-do`~`~")){
                new BaseBean().writeLog("当前不是待办事宜(点击tab页）的TAB数据，直接return");
                return result;
            }

            //获取JSON对象中的待办数据数组
            JSONArray dataJsonArray = resultJson.getJSONArray("data");

            /**
            //获取各个系统的待办数量
            Map<String, JSONObject> dbslMap = new HashMap<String,JSONObject>();//用于存储各个系统待办数据的Map

            //获取EIP的待办数量
            new BaseBean().writeLog("开始获取EIP的待办数量："+new Date());
            try{
                String sendHead = "{\"clientCode\":\"DFTC_COS\",\"tradeCode\":\"DFG_EIP_011\",\"reqSerialNo\":\"f16sad54f65sa1df3a5s\",\"tradeDescription\":\"COS待办集成\",\"tradeTime\":\"" + TimeUtil.getCurrentTimeString() + "\",\"version\":\"1.0\"}";
                String sendBody = "{\"PARAM\":{\"workcode\": \"" + user.getLoginid() + "\"}}";
                String res = new ISendMessageServiceProxy().sendMessage(sendHead, sendBody);
                JSONObject data = JSONObject.parseObject(res);

                //如果EIP系统没有目前的loginid，url为空
                if ("".equals(data.getString("url"))) {
                    data.put("url", "http://10.2.19.187/erm/sys/portal/page.jsp?from=COS");
                }

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("EIP",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取EIP待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取EIP的待办数量结束："+new Date());

            //获取AMS的待办数量
            new BaseBean().writeLog("开始获取AMS的待办数量："+new Date());
            try{
                String amsResutlt = new ITaskToDoServiceProxy().taskToDo(user.getLoginid());
                JSONObject data = JSONObject.parseObject(amsResutlt);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("AMS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取AMS待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取AMS的待办数量结束："+new Date());

            //获取MAS的待办数量
            new BaseBean().writeLog("开始获取MAS的待办数量："+new Date());
            try{
                String masResult = new OASoapProxy().getTaskByEmpNo(user.getLoginid());
                JSONObject data = JSONObject.parseObject(masResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("MAS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取MAS待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取MAS的待办数量结束："+new Date());

            //获取PAM的待办数量
            new BaseBean().writeLog("开始获取PAM的待办数量："+new Date());
            try{
                String pamResult = new DbWorkWebServiceProxy().getPamDbWork(user.getLoginid());
                JSONObject data = JSONObject.parseObject(pamResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("PAM",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取PAM待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取PAM的待办数量结束："+new Date());

            //获取QIS的待办数量
            new BaseBean().writeLog("开始获取QIS的待办数量："+new Date());
            try{
                String qisResult = new IUserWSProxy().queryoanum(user.getLoginid());
                JSONObject data = JSONObject.parseObject(qisResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("QIS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取QIS待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取QIS的待办数量结束："+new Date());

            //获取SLM的待办数量
            new BaseBean().writeLog("开始获取SLM的待办数量："+new Date());
            try{
                String slmResult = new TaskServiceProxy().taskToDo(user.getLoginid());
                JSONObject data = JSONObject.parseObject(slmResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("SLM",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取SLM待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取SLM的待办数量结束："+new Date());

            //获取IPMS的待办数量
            new BaseBean().writeLog("开始获取IPMS的待办数量："+new Date());
            try{
                String ipmsResult = new PatWaitReviewServiceSoapProxy().getJobCountAndUrl(user.getLoginid());
                JSONObject data = JSONObject.parseObject(ipmsResult);

                //如果待办数量存在并大于0，则将当前异构系统的待办信息放入dbslMap中
                if(null!=data.getString("count")&&!data.getString("count").trim().equals("")&&!data.getString("count").trim().equals("0")){
                    dbslMap.put("IPMS",data);
                }
            }catch(Exception e){
                e.printStackTrace();
                new BaseBean().writeLog("获取IPMS待办数量时出现异常(点击tab页）:"+e.getMessage());
            }
            new BaseBean().writeLog("获取IPMS的待办数量结束："+new Date());

            */

            //循环处理每个待办，如果receivedata大于当前日期3天，则在标题前面加上红色的超时，否则在标题前面加上绿色的正常
            //获取三天前的日期
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.add(Calendar.DAY_OF_MONTH,-3);
            String lastThreeDay = new SimpleDateFormat("yyyy-MM-dd").format(nowCalendar.getTime());

            new BaseBean().writeLog("begin循环处理COS的待办(点击tab页）");

            for(int i = 0;i<dataJsonArray.size();i++){
                //如果该COS系统的待办的日期小于三天前的日期，则在标题前面加上红色的超时
                if(null!=dataJsonArray.getJSONObject(i).getString("receivedate")&&(dataJsonArray.getJSONObject(i).getString("receivedate").compareTo(lastThreeDay)<0)){
                    Map cosRequestName = (Map)dataJsonArray.getJSONObject(i).get("requestname");
                    cosRequestName.put("name","<span style='color:red;'>超时</span> "+cosRequestName.get("name"));
                }else{
                    Map cosRequestName = (Map)dataJsonArray.getJSONObject(i).get("requestname");
                    cosRequestName.put("name","<span style='color:green;'>正常</span> "+cosRequestName.get("name"));
                }
            }

            new BaseBean().writeLog("end循环处理COS的待办(点击tab页）");


            /**
            //待办跳转处理中转JSP地址
            final String dbsl_jump = "/dfqcgsjszx/dbsl_jump.jsp?url=";

            //循环处理dbslMap中含有的异构系统待办数量，将每个异构系统的待办信息放入待办列表
            Iterator<String> keyIterator = dbslMap.keySet().iterator();
            while(keyIterator.hasNext()){
                String sysName = keyIterator.next();


                new BaseBean().writeLog("while sysName:"+sysName);

                JSONObject sysData = dbslMap.get(sysName);
                //增加一条新的数据
                JSONObject newObject = new JSONObject(new LinkedHashMap());
                Map<String,String> newObjectRequestname = new HashMap<String,String>();
                newObjectRequestname.put("lasttitle","");
                newObjectRequestname.put("img","");
                newObjectRequestname.put("pretitle","");
                newObjectRequestname.put("requestid","");
                newObjectRequestname.put("name",sysName+"系统有【<span style='color:red;'>"+sysData.getString("count")+"</span>】条新的待办");
                newObjectRequestname.put("link",dbsl_jump+ URLEncoder.encode(sysData.getString("url"),"utf-8")+"&sysName="+sysName);
                newObjectRequestname.put("mobilelink",dbsl_jump+ URLEncoder.encode(sysData.getString("url"),"utf-8")+"&sysName="+sysName);
                newObject.put("sysname",sysName);
                newObject.put("requestname",newObjectRequestname);
                newObject.put("receivedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                dataJsonArray.add(0,newObject);

                //如果末尾的一条数据属于COS系统，删除末尾的一条数据
//                new BaseBean().writeLog("remove last by sysName:"+dataJsonArray.getJSONObject(dataJsonArray.size() - 1).getString("sysname").equals("COS"));
//                if(dataJsonArray.getJSONObject(dataJsonArray.size() - 1).getString("sysname").equals("COS")) {
//                    dataJsonArray.remove(dataJsonArray.size() - 1);
//                }

            }
             */


            //将处理过的JSON数据转换回Map
            Map changeResult = dataJsonArray.toJavaObject(resultJson, Map.class);

            new BaseBean().writeLog("result2:" + changeResult);
            result = changeResult;
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("为流程中心的待办列表(点击tab页）增加第三方系统待办数量数据出现异常:"+e.getMessage());
        }

        new BaseBean().writeLog("为流程中心的待办列表(点击tab页）增加第三方系统待办数量数据End");
        return result;
    }

}
