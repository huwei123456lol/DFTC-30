package com.engine.dfxm.cmd.dd;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.dfxm.manager.PropertiesManager;
import com.engine.dfxm.util.DongfengHttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.*;

/**
 * 订单确认接口
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class DDDoPoOrderCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public DDDoPoOrderCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }


    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        String ids = (String) params.get("ids");
        String type = (String) params.get("type");
        String doMsg = (String) params.get("doMsg");
        //checkData(ids,type,doMsg);
        //Map<String, Object> reMapSap=doSendSap("SAPDDDO",ids,type,doMsg+"(SAP)");
        //if(!(boolean) reMapSap.get("status")){
        //    msg+=reMapSap.get("msg");
        //}
        Map<String, Object> reMap360=doSend360("360DDDO",ids,type,doMsg+"(360)");
        //if(!(boolean) reMap360.get("status")){
        //    msg=(String) reMap360.get("msg");
        //}
        //reMap.put("msg",msg);


        return reMap360;
    }

    private Map<String, Object> checkData(String ids,String type,String doMsg) {
        Map<String, Object> reMap=new HashMap<>();
        RecordSet rs=new RecordSet();
        boolean status=true;
        reMap.put("status",status);
        String msg="";
        if("X".equals(type)){
            String sql="select *  from uf_cs_ddgl where id in ("+ids+") and  TRIAL_STATUS='X' ";
            rs.executeQuery(sql);
            while(rs.next()){
                status=false;
                msg+= rs.getString("ORDER_CODE")+",";
            }
            if(!status){
                reMap.put("status",status);
                reMap.put("msg",doMsg+"("+msg.substring(0,msg.length()-1)+")无法确认，只有未确认过的订单才允许确认，请重新选择！");
            }
        }
        return reMap;
    }

    /**
     * 订单确认消息发送360
     * @author wangkun
     * @date 2024-10-16 15:28
     * @param operateCode
     * @param ids
     * @param type
     * @param doMsg
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> doSend360(String operateCode, String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            PropertiesManager pm=new PropertiesManager(operateCode,1);
            JSONObject pmObj=pm.getFieldValueObj();
            String password="";
            String contentType="";
            String apiUrl="";
            boolean ignoreSSL=false;
            if(pmObj.containsKey("urlCode")){
                String urlCode=pmObj.getString("urlCode");
                PropertiesManager apipm=new PropertiesManager(urlCode,1);
                JSONObject apipmObj=apipm.getFieldValueObj();
                password=apipmObj.getString("token");
                ignoreSSL=apipmObj.getBoolean("ignoreSSL");
                contentType=apipmObj.getString("contentType");
                apiUrl=apipmObj.getString("url")+pmObj.getString("api");
            }else{
                password=pmObj.getString("token");
                contentType=pmObj.getString("contentType");
                apiUrl=pmObj.getString("url");
            }
            String sql="select *  from uf_cs_ddgl where id in ("+ids+")";
            RecordSet rs=new RecordSet();
            rs.executeQuery(sql);
            reMap.put("status",true);
            String msg="";
            while (rs.next()){
                String TRIAL_STATUS360=rs.getString("TRIAL_STATUS360");
                String id=rs.getString("id");
                if("X".equals(TRIAL_STATUS360)){
                    //    已确认过订单直接跳过
                    continue;
                }
                JSONObject row360=new JSONObject();
                String ORDER_CODE=Util.null2String(rs.getString("ORDER_CODE"));
                row360.put("ORDER_CODE",ORDER_CODE);
                row360.put("SAP_ORDER_CODE",Util.null2String(rs.getString("EBELN")));
                row360.put("ORDER_CONFIRM_FLAG","1");
                row360.put("REMARK","订单确认");
                //row360.put("REMARK",Util.null2String(rs.getString("REMARK")));
                //row360.put("ZRES1",Util.null2String(rs.getString("ZRES1")));
                //row360.put("ZRES2",Util.null2String(rs.getString("ZRES2")));
                //row360.put("ZRES3",Util.null2String(rs.getString("ZRES3")));
                log.info(doMsg+"推送报文: "+"  params:"+row360);
                Map<String,String> header=new HashMap<>();
                header.put("Authorization",password);
                header.put("Content-Type",contentType);
                HttpResponse response= DongfengHttpUtil.post360IgnoreSSL(apiUrl,header,row360.toString(),ignoreSSL);
                if(response.isOk()){
                    String ret=response.body();
                    log.info(doMsg+"返回报文："+ret);
                    //{"code":200,"msg":"操作成功","data":{"MESSAGE":"未找到对应的采购订单数据！","MESSAGE_TYPE":"E"}}
                    JSONObject retJson=JSONObject.fromObject(ret);
                    if("200".equals(retJson.getString("code"))){
                        JSONObject data=retJson.getJSONObject("data");
                        sql="update uf_cs_ddgl set TRIAL_STATUS_ZT=?,TRIAL_STATUS=?,ZMSG360=? where id=?";
                        String backsql="update uf_cs_ddgl set  TRIAL_STATUS_ZT=?,ZMSG360=? where id=?";
                        List<List> upList=new ArrayList<>();
                        List<List> backupList=new ArrayList<>();
                        List upRow=new ArrayList();
                        if("S".equals(data.getString("MESSAGE_TYPE"))){
                            upRow.add(2);
                            upRow.add(type);
                            upRow.add(data.getString("MESSAGE"));
                            upRow.add(id);
                            upList.add(upRow);
                            msg+=ORDER_CODE+"成功，"+data.getString("MESSAGE");
                        }else{
                            upRow.add(1);
                            upRow.add(data.getString("MESSAGE"));
                            upRow.add(id);
                            backupList.add(upRow);
                            msg+=ORDER_CODE+"失败，"+data.getString("MESSAGE");
                        }
                        RecordSet upRs=new RecordSet();
                        if(upList.size()>0){
                            upRs.executeBatchSql(sql,upList);
                            log.info("确认完成更新订单状态："+upRs.getExceptionMsg());
                        }
                        if(backupList.size()>0){
                            upRs.executeBatchSql(backsql,backupList);
                            log.info("确认失败更新订单状态："+upRs.getExceptionMsg());
                        }
                        reMap.put("msg",doMsg+msg);
                    }else{
                        log.error(doMsg+"失败："+retJson.get("msg"));
                        reMap.put("status",false);
                        reMap.put("msg",doMsg+"失败,"+response.body());
                        sql="update uf_cs_ddgl set ZMSG360='"+doMsg+"失败："+retJson.get("msg")+"' where id in='"+id+"'";
                        RecordSet upRs=new RecordSet();
                        upRs.executeUpdate(sql);
                    }
                }else{
                    reMap.put("status",false);
                    reMap.put("msg",doMsg+"失败,"+response.body());
                    sql="update uf_cs_ddgl set ZMSG360='"+doMsg+"失败,"+response.body()+"' where id in='"+id+"'";
                    RecordSet upRs=new RecordSet();
                    upRs.executeUpdate(sql);
                }
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
            String sql="update uf_cs_ddgl set ZMSG360='"+doMsg+"失败,第三方接口异常："+e.getMessage()+"' where id in ("+ids+")";
            RecordSet rs=new RecordSet();
            rs.executeUpdate(sql);
        }
        return reMap;
    }


    public Map<String, Object> doSendSap(String operateCode, String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            PropertiesManager pm=new PropertiesManager(operateCode,1);
            JSONObject pmObj=pm.getFieldValueObj();
            String loginid=pmObj.getString("loginid");
            String password=pmObj.getString("token");
            String Xappid=pmObj.getString("Xappid");
            String sapClient=pmObj.getString("sapClient");
            String contentType=pmObj.getString("contentType");

            String sql="select *  from uf_cs_ddgl where id in ("+ids+")";
            RecordSet rs=new RecordSet();
            rs.executeQuery(sql);
            JSONArray dataArr=new JSONArray();
            while (rs.next()){
                String TRIAL_STATUS=rs.getString("TRIAL_STATUS");
                if("X".equals(TRIAL_STATUS)){
                //    已确认过订单直接跳过
                    continue;
                }
                JSONObject row=new JSONObject();
                String ORDER_CODE=Util.null2String(rs.getString("ORDER_CODE"));
                row.put("ORDER_CODE", ORDER_CODE);//订单号
                row.put("TRIAL_STATUS",type);//确认订单标识
                dataArr.add(row);
            }
            if(dataArr.size()>0){
                JSONObject paramObj=new JSONObject();
                paramObj.put("INFID","COS001");
                paramObj.put("ZSCOS001_REQ",dataArr);
                HttpRequest request=HttpUtil.createPost(pmObj.getString("url"));
                Map header=new HashMap();
                String timeStamp=DateUtil.format(new Date(),"yyyyMMddHHmmss");
                header.put("X-App-Id",Xappid);
                header.put("X-Sequence-No", timeStamp);
                header.put("X-Timestamp",timeStamp);
                header.put("sap-client",sapClient);
                header.put("Content-Type",contentType);
                request.basicAuth(loginid,password);
                request.addHeaders(header);
                request.body(paramObj.toString());
                log.info(doMsg+"推送报文：header:"+JSONObject.fromObject(header)+"  params:"+paramObj);
                HttpResponse response=request.execute();
                if(response.getStatus()==200){
                    String ret=response.body();
                    log.info(doMsg+"返回报文："+ret);
                    JSONObject retJson=JSONObject.fromObject(ret);
                    if(retJson.containsKey("RETCODE")&&"S".equals(retJson.getString("RETCODE"))){
                        sql="update uf_cs_ddgl set TRIAL_STATUS=?,ZMSG=? where ZQRSYH=?";
                        String backsql="update uf_cs_ddgl set ZMSG=? where ZQRSYH=?";
                        List<List> upList=new ArrayList<>();
                        List<List> backupList=new ArrayList<>();
                        JSONArray ZSCOS001_RES=retJson.getJSONArray("ZSCOS001_RES");
                        for (int i = 0; i < ZSCOS001_RES.size(); i++) {
                            JSONObject row=ZSCOS001_RES.getJSONObject(i);
                            List upRow=new ArrayList();
                            if("S".equals(row.getString("ZRET"))){
                                //处理成功，标识打上
                                upRow.add(type);
                                upRow.add(row.getString("ZMSG"));
                                upRow.add(row.getString("ZQRSYH"));
                                upList.add(upRow);
                            }else{
                                //处理失败，标识维持原有,即如果是取消确认，则原标识为X，如果是确认，则原标识为空
                                upRow.add(row.getString("ZMSG"));
                                upRow.add(row.getString("ZQRSYH"));
                                backupList.add(upRow);
                            }
                        }
                        if(upList.size()>0){
                            rs.executeBatchSql(sql,upList);
                        }
                        if(backupList.size()>0){
                            rs.executeBatchSql(backsql,backupList);
                        }
                        reMap.put("msg",doMsg+"完成！");
                    }else{
                        reMap.put("status",false);
                        reMap.put("msg",doMsg+"失败,"+retJson.getString("RETMSG"));
                        sql="update uf_cs_ddgl set ZMSG='"+retJson.getString("RETMSG")+"' where id in ("+ids+")";
                        rs.executeUpdate(sql);
                    }
                }else{
                    reMap.put("status",false);
                    reMap.put("msg",doMsg+"失败,"+response.body());
                    sql="update uf_cs_ddgl set ZMSG='"+"取消失败,"+response.body()+"' where id in ("+ids+")";
                    rs.executeUpdate(sql);
                }
            }else{
                log.info(doMsg+"没有需要处理的数据type:"+type);
                reMap.put("status",false);
                reMap.put("msg",doMsg+"没有需要处理的数据type:"+type);
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
            String sql="update uf_cs_ddgl set ZMSG='"+doMsg+"失败,第三方接口异常："+e.getMessage()+"' where id in ("+ids+")";
            RecordSet rs=new RecordSet();
            rs.executeUpdate(sql);
        }
        return reMap;
    }
}
