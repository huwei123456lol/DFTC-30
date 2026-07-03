package com.api.df;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.AMS.service_client.ITaskToDoServiceProxy;
import weaver.dfqcgsjszx.util.EIP.service_client.ISendMessageServiceProxy;
import weaver.dfqcgsjszx.util.IPMS.service_client.PatWaitReviewServiceSoapProxy;
import weaver.dfqcgsjszx.util.MAS.service_client.OASoapProxy;
import weaver.dfqcgsjszx.util.PAM.service_client.DbWorkWebServiceProxy;
import weaver.dfqcgsjszx.util.QIS.service_client.IUserWSProxy;
import weaver.dfqcgsjszx.util.SLM.service_client.TaskServiceProxy;
import weaver.dfqcgsjszx.util.SLM2.service_client.TaskAggregationProxy;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 获取异构系统的待办事宜
 * @author Alex.Du
 */
@Path("/todoAction")
public class GetTodoAction {
    @POST
    @Path("/getTodo")
    @Produces(MediaType.TEXT_PLAIN)
    /**
     * 获取
     *
     * @author Alex.Du
     * @param [request, response]
     * @return
     */
    public String getPrivateKey(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String sysName = request.getParameter("sysName");
        User loginUser=(User)request.getSession().getAttribute("weaver_user@bean");

        RecordSet rs = new RecordSet();
        //查询移动端跳转地址
        rs.execute("select mobile_url from uf_unify_login_sys where sys_name='"+sysName+"'");
        String mobileURL = "";
        if(rs.next()){
            mobileURL = rs.getString("mobile_url");
        }

        //用于当前方法的返回
        JSONObject result = new JSONObject();

        //用于存储各个系统返回的数据
        JSONObject resultData = null;
        try {
            if (sysName.equals("EIP")) {
                String sendHead = "{\"clientCode\":\"DFTC_COS\",\"tradeCode\":\"DFG_EIP_011\",\"reqSerialNo\":\"" + TimeUtil.getCurrentTimeString() + "\",\"tradeDescription\":\"COS待办集成\",\"tradeTime\":\"" + TimeUtil.getCurrentTimeString() + "\",\"version\":\"1.0\"}";
                String sendBody = "{\"PARAM\":{\"workcode\": \"" + loginUser.getLoginid() + "\"}}";
                String res = new ISendMessageServiceProxy().sendMessage(sendHead, sendBody);
                resultData = JSONObject.parseObject(res);
            }else if(sysName.equals("AMS")){
                String amsResutlt = new ITaskToDoServiceProxy().taskToDo(loginUser.getLoginid());
                resultData = JSONObject.parseObject(amsResutlt);
            }else if(sysName.equals("MAS")){
                String masResult = new OASoapProxy().getTaskByEmpNo(loginUser.getLoginid());
                resultData = JSONObject.parseObject(masResult);
            }else if(sysName.equals("PAM")){
                String pamResult = new DbWorkWebServiceProxy().getPamDbWork(loginUser.getLoginid());
                resultData = JSONObject.parseObject(pamResult);
            }else if(sysName.equals("QIS")){
                String qisResult = new IUserWSProxy().queryoanum(loginUser.getLoginid());
                resultData = JSONObject.parseObject(qisResult);
            }else if(sysName.equals("SLM")){
                String slmResult = new TaskServiceProxy().taskToDo(loginUser.getLoginid());
                resultData = JSONObject.parseObject(slmResult);
            }else if(sysName.equals("IPMS")){
                String ipmsResult = new PatWaitReviewServiceSoapProxy().getJobCountAndUrl(loginUser.getLoginid());
                resultData = JSONObject.parseObject(ipmsResult);
            }else if(sysName.equals("SLM2.0")){
                String slm20Result = new TaskAggregationProxy().getCount(loginUser.getLoginid());
                resultData = JSONObject.parseObject(slm20Result);
            }

            if(resultData == null){
                result.put("state",1);
                result.put("msg","未获取到任何数据，请检查sysName参数");
                return result.toJSONString();
            }

            resultData.put("mobile_url",mobileURL);
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("[GetTodoAction.getPrivateKey]获取待办数量出现异常："+e.getMessage());
            result.put("state",1);
            result.put("msg",e.getMessage());
            return result.toJSONString();
        }
        result.put("state",0);
        result.put("data",resultData);
        return result.toJSONString();
    }
}
