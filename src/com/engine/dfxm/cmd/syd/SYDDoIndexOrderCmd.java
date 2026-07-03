package com.engine.dfxm.cmd.syd;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.cube.biz.CodeBuilder;
import com.engine.dfxm.manager.PropertiesManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.*;

/**
 * 索引单确认取消接口
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class SYDDoIndexOrderCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public SYDDoIndexOrderCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }


    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.format(new Date(),"yyyyMMddHHmmss"));
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            String ids = (String) params.get("ids");
            String type = (String) params.get("type");
            String doMsg = (String) params.get("doMsg");
            String sendSap =params.containsKey ("sendSap")?(String) params.get("sendSap"):"";
            String send360 =params.containsKey ("send360")?(String) params.get("send360"):"";
            String msg="";
            boolean status=true;
            boolean statussap=true;
            boolean status360=true;
            //Map<String,Object> checkMap=checkData(ids,type);
            if(!"0".equals(sendSap)){
                //发送索引单到SAP
                Map<String, Object> reMapSap=doSendSap("SAPSYDDO",ids,type,doMsg+"(SAP)");
                //if(!(boolean) reMapSap.get("status")){
                msg+=reMapSap.get("msg");
                statussap=(boolean)reMapSap.get("status");
                //}
            }
            if(!"".equals(msg)){
                msg+=";";
            }
            if(!"0".equals(send360)){
                //发送索引单到360
                Map<String, Object> reMap360=doSend360("360SYDDO",ids,type,doMsg+"(360)");
                //if(!(boolean) reMap360.get("status")){
                msg+=reMap360.get("msg");
                statussap=(boolean) reMap360.get("status");
                //}
            }
            //"X".equals(type)&&status360&&statussap
            //"X".equals(type)&&status360&&statussap
            if("X".equals(type)&&status360&&statussap){
                RecordSet rs=new RecordSet();
                rs.executeUpdate("update uf_sydlb set zt=0 where id in ("+ids+")");
            }else if("X".equals(type)&&status360&&statussap){
                RecordSet rs=new RecordSet();
                rs.executeUpdate("update uf_sydlb set zt=1 where id in ("+ids+")");
            }
            reMap.put("status",status);
            reMap.put("msg","".equals(msg)?"索引单确认完成！":msg);
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg","索引单确认异常："+e);
        }
        return reMap;
    }

    private Map<String, Object> checkData(String ids, String type,String doMsg) {
        RecordSet rs=new RecordSet();
        Map<String,Object> checkMap=new HashMap<>();
        boolean status=true;
        String msg="";
        if("X".equals(type)){
            String sql="select  ZFLAG_QR360,ZFLAG_QR  from uf_sydlb where id in ("+ids+")";
            rs.executeQuery(sql);
            String ZFLAG_QR="";
            String ZFLAG_QR360="";
            while(rs.next()){
                ZFLAG_QR=rs.getString("ZFLAG_QR");
                ZFLAG_QR360=rs.getString("ZFLAG_QR360");
                if("X".equals(ZFLAG_QR)){
                    msg+= rs.getString("ZSYH")+"已确认,";
                }
                status=false;
                msg+= rs.getString("ZSYH")+",";
            }
            if(!status){
                checkMap.put("status",status);
                checkMap.put("msg",doMsg+"("+msg.substring(0,msg.length()-1)+")无法确认，只有未确认过的索引单才允许确认，请重新选择！");
                return checkMap;
            }
        }else if("Y".equals(type)){
            String sql="select * from uf_sydlb where id in ("+ids+") and (ZFLAG_QR360<>'X' or EKGRP1='X') ";
            rs.executeQuery(sql);
            while(rs.next()){
                status=false;
                msg+= rs.getString("ZSYH")+",";
            }
            if(!status){
                checkMap.put("status",status);
                checkMap.put("msg",doMsg+"("+msg.substring(0,msg.length()-1)+")无法取消确认，只有已确认但未结算条目才可以取消，请重新选择！");
                return checkMap;
            }
        }
        return checkMap;
    }

    /**
     * 发送确认索引单号到360
     * @author wangkun
     * @date 2024-10-16 14:49
     * @param operateCode 
     * @param ids 
     * @param type 
     * @param doMsg
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> doSend360(String operateCode, String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            reMap.put("status",true);
            log.info("开始360索引单确认！"+operateCode+"==="+ids+"---"+type+"----"+doMsg);
            PropertiesManager pm=new PropertiesManager(operateCode,1);
            JSONObject pmObj=pm.getFieldValueObj();
            String password="";
            String contentType="";
            String apiUrl="";
            if(pmObj.containsKey("urlCode")){
                String urlCode=pmObj.getString("urlCode");
                PropertiesManager apipm=new PropertiesManager(urlCode,1);
                JSONObject apipmObj=apipm.getFieldValueObj();
                password=apipmObj.getString("token");
                contentType=apipmObj.getString("contentType");
                apiUrl=apipmObj.getString("url")+pmObj.getString("api");
            }else{
                password=pmObj.getString("token");
                contentType=pmObj.getString("contentType");
                apiUrl=pmObj.getString("url");
            }
            RecordSet rs=new RecordSet();
            int modeid = pmObj.getInt("modeid");
            int formid = pmObj.getInt("formid");
            String codefieldid = pmObj.getString("codefieldid");
            log.info(doMsg+"配置信息"+pmObj);
            String sql="select ZQRSYH,ZFLAG_QR360,EKGRP1,id,SAP_SUPPLIER_CODE,SAP_VOUCHER_CODE,SAP_VOUCHER_PROJECT_CODE from uf_sydlb where id in ("+ids+")";
            log.info("索引单360确认数据查询"+sql);
            rs.executeQuery(sql);
            JSONArray dataArr360=new JSONArray();

            while (rs.next()){
                String ZFLAG_QR360=rs.getString("ZFLAG_QR360");
                String EKGRP1=rs.getString("EKGRP1");
                if("X".equals(ZFLAG_QR360)&&"X".equals(type)){
                    //当前类型为确认“X”，且当前确认标识为已确认“X”，则跳过处理
                    continue;
                }else if(("Y".equals(type)&&"X".equals(ZFLAG_QR360)&&"X".equals(EKGRP1))||("Y".equals(type)&&!"X".equals(ZFLAG_QR360))){
                    //当前类型为取消确认“Y”，且当前确认标识为已确认“X”，且发票结算标识为已确认“X” ，则不允许取消确认，跳过处理
                    //或者当前类型为取消确认“Y”，且当前确认标识为未确认，则不允许取消确认，跳过处理
                    continue;
                }
                String ZQRSYH=Util.null2String(rs.getString("ZQRSYH"));
                //if("".equals(ZQRSYH)){
                //    CodeBuilder cbuild = new CodeBuilder(modeid);
                //    Map<String,Object>  codeMap=cbuild.getModeCodeStr(formid,rs.getInt("id"));//生成编号
                //    ZQRSYH=Util.null2String(codeMap.get(codefieldid));
                //}
                setArr(dataArr360,rs.getString("id"),ZQRSYH,type);
            }
            if(dataArr360.size()>0){
                log.info(doMsg+"推送报文: "+"  params:"+dataArr360);
                HttpRequest request=HttpUtil.createPost(apiUrl);
                request.header("Authorization",password);
                request.header("Content-Type",contentType);
                request.body(dataArr360.toString());
                HttpResponse response=request.execute();
                String errmsg=doMsg;
                if(response.isOk()){
                    String ret=response.body();
                    log.info(doMsg+"返回报文："+ret);
                    //[{"MESSAGE":"成功","CONFIRM_INDEX_CODE":"TC1224202300002","CONFIRM_FLAG":"X",
                    //        "MESSAGE_TYPE":"S","SAP_SUPPLIER_CODE":"1008773","SAP_VOUCHER_PROJECT_CODE":"1","SAP_VOUCHER_CODE":"5012723078"}]
                    JSONArray retJson=JSONArray.fromObject(ret);
                    sql="update uf_sydlb set ZFLAG_QR360=?,ZMSG360=? where ZQRSYH=?";
                    String backsql="update uf_sydlb set ZMSG360=? where ZQRSYH=?";
                    List<List> upList=new ArrayList<>();
                    List<List> backupList=new ArrayList<>();
                    for (int i = 0; i < retJson.size(); i++) {
                        JSONObject row=retJson.getJSONObject(i);
                        List upRow=new ArrayList();
                        log.info("开始处理行数据"+row);
                        String ZQRSYH=row.getString("CONFIRM_INDEX_CODE");
                        if("S".equals(row.getString("MESSAGE_TYPE"))){
                            errmsg+="("+ZQRSYH+")"+row.getString("MESSAGE");
                            upRow.add(type);
                            upRow.add(row.getString("MESSAGE"));
                            upRow.add(ZQRSYH);
                            upList.add(upRow);
                        }else{
                            reMap.put("status",false);
                            errmsg+="("+ZQRSYH+")"+row.getString("MESSAGE");
                            upRow.add(row.getString("MESSAGE"));
                            upRow.add(ZQRSYH);
                            backupList.add(upRow);
                        }
                    }
                    log.info("开始更新数据");
                    if(upList.size()>0){
                        log.info(doMsg+"处理成功！");
                        rs.executeBatchSql(sql,upList);
                    }
                    if(backupList.size()>0){
                        log.info(doMsg+"处理失败！");
                        rs.executeBatchSql(backsql,backupList);
                    }
                    reMap.put("msg",errmsg);
                    log.info(errmsg);
                }else{
                    log.error(doMsg+"失败,"+response.body());
                    reMap.put("status",false);
                    reMap.put("msg",doMsg+"失败,"+response.body());
                    sql="update uf_sydlb set ZMSG360='"+"取消失败,"+response.body()+"' where id in ("+ids+")";
                    log.info(doMsg+"==="+sql);
                    rs.executeUpdate(sql);
                }
            }else{
                log.info(doMsg+"没有需要处理的数据type:"+type);
                reMap.put("status",false);
                reMap.put("msg",doMsg+"没有需要处理的数据type:"+type);
            }
        }catch (Exception e){
            log.error(doMsg+"==异常"+e);
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
            String sql="update uf_sydlb set ZMSG360='"+doMsg+"失败,第三方接口异常："+e.getMessage()+"' where id in ("+ids+")";
            RecordSet rs=new RecordSet();
            rs.executeUpdate(sql);
        }
        return reMap;
    }

    private void setArr(JSONArray dataArr360, String id, String ZQRSYH, String type) {
        String sql="select * from uf_sydlb_dt1 where mainid="+id;
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql);
        while (rs.next()){
            JSONObject row360=new JSONObject();
            row360.put("CONFIRM_INDEX_CODE", ZQRSYH);//确认索引单号
            row360.put("SAP_SUPPLIER_CODE",Util.null2String(rs.getString("SAP_SUPPLIER_CODE")));//SAP供应商编码
            row360.put("SAP_VOUCHER_CODE",Util.null2String(rs.getString("SAP_VOUCHER_CODE")));//SAP物料凭证号
            row360.put("SAP_VOUCHER_PROJECT_CODE",Util.null2String(rs.getString("SAP_VOUCHER_PROJECT_CODE")));//SAP物料凭证项目号
            row360.put("CONFIRM_FLAG",type);//确认标识
            dataArr360.add(row360);
        }
    }

    private void setSAPArr(JSONArray dataArr, String id, String ZQRSYH, String type) {
        String sql="select * from uf_sydlb_dt1 where mainid="+id;
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql);
        while (rs.next()){
            JSONObject row=new JSONObject();
            row.put("ZQRSYH", ZQRSYH);//确认索引单号
            row.put("LIFNR",Util.null2String(rs.getString("SAP_SUPPLIER_CODE")));//SAP供应商编码
            row.put("BELNR",Util.null2String(rs.getString("SAP_VOUCHER_CODE")));//SAP物料凭证号
            row.put("BUZEI",Util.null2String(rs.getString("SAP_VOUCHER_PROJECT_CODE")));//SAP物料凭证项目号
            row.put("ZFLAG_QR",type);//确认标识
            dataArr.add(row);
        }
    }

    /**
     * 发送确认索引单号到SAP
     * @author wangkun
     * @date 2024-10-16 14:49
     * @param operateCode
     * @param ids
     * @param type
     * @param doMsg
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> doSendSap(String operateCode,String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            PropertiesManager pm=new PropertiesManager(operateCode,1);
            JSONObject pmObj=pm.getFieldValueObj();
            String loginid=pmObj.getString("loginid");
            String password=pmObj.getString("token");
            String Xappid=pmObj.getString("Xappid");
            String sapClient=pmObj.getString("sapClient");
            String contentType=pmObj.getString("contentType");
            int modeid = pmObj.getInt("modeid");
            int formid = pmObj.getInt("formid");
            String codefieldid = pmObj.getString("codefieldid");
            log.info(doMsg+"配置信息"+pmObj);
            reMap.put("status",false);
            RecordSet rs=new RecordSet();
            //String sql="select *  from uf_sydlb where id in ("+ids+")";
            String sql="select ZQRSYH,ZFLAG_QR,EKGRP1,id,SAP_SUPPLIER_CODE,SAP_VOUCHER_CODE,SAP_VOUCHER_PROJECT_CODE from uf_sydlb where id in ("+ids+")";
            log.info("索引单SAP确认数据查询"+sql);
            rs.executeQuery(sql);
            JSONObject paramObj=new JSONObject();
            JSONArray dataArr=new JSONArray();
            while (rs.next()){
                String ZFLAG_QR=rs.getString("ZFLAG_QR");
                String EKGRP1=rs.getString("EKGRP1");
                if("X".equals(ZFLAG_QR)&&"X".equals(type)){
                    //当前类型为确认“X”，且当前确认标识为已确认“X”，则跳过处理
                    continue;
                }else if(("Y".equals(type)&&"X".equals(ZFLAG_QR)&&"X".equals(EKGRP1))||("Y".equals(type)&&!"X".equals(ZFLAG_QR))){
                    //当前类型为取消确认“Y”，且当前确认标识为已确认“X”，且发票结算标识为已确认“X” ，则不允许取消确认，跳过处理
                    continue;
                }
                //每次确认重新生成索引单号
                String ZQRSYH=Util.null2String(rs.getString("ZQRSYH"));
                if("X".equals(type)){
                    //    如果是确认，则重新生成索引单号
                    //20240826调整为每次确认重新生成编号
                    CodeBuilder cbuild = new CodeBuilder(modeid);
                    Map<String,Object>  codeMap=cbuild.getModeCodeStr(formid,rs.getInt("id"));//生成编号
                    ZQRSYH=Util.null2String(codeMap.get(codefieldid));
                }
                setSAPArr(dataArr,rs.getString("id"),ZQRSYH,type);
            }

            if(dataArr.size()>0){
                paramObj.put("INFID","COS001");
                paramObj.put("ZSCOS001_REQ",dataArr);
                log.info(doMsg+"推送报文 params:"+paramObj);
                HttpRequest request=HttpUtil.createPost(pmObj.getString("url"));
                request.basicAuth(loginid,password);
                Map header=new HashMap();
                String timeStamp=DateUtil.format(new Date(),"yyyyMMddHHmmss");
                header.put("X-App-Id",Xappid);
                header.put("X-Sequence-No", timeStamp);
                header.put("X-Timestamp",timeStamp);
                header.put("sap-client",sapClient);
                header.put("Content-Type",contentType);
                request.addHeaders(header);
                request.body(paramObj.toString());
                log.info(doMsg+"推送报文：header:"+JSONObject.fromObject(header));
                HttpResponse response=request.execute();
                String errmsg=doMsg;
                if(response.getStatus()==200){
                    String ret=response.body();
                    log.info(doMsg+"返回报文："+ret);
                    //{"ZSCOS001_RES":[{"ZQRSYH":"TC1224202300008","LIFNR":"1008773","BELNR":"5012723078","BUZEI":"0002","ZFLAG_QR":"X","ZRET":"S","ZMSG":"更新成功"}]
                    // ,"RETCODE":"S","RETMSG":"数据接收成功"}
                    JSONObject retJson=JSONObject.fromObject(ret);
                    if(retJson.containsKey("RETCODE")&&"S".equals(retJson.getString("RETCODE"))){
                        sql="update uf_sydlb set ZFLAG_QR=?,ZMSG=? where ZQRSYH=?";
                        String backsql="update uf_sydlb set ZMSG=? where ZQRSYH=?";
                        List<List> upList=new ArrayList<>();
                        List<List> backupList=new ArrayList<>();
                        log.info("开始处理返回数据");
                        if(retJson.containsKey("ZSCOS001_RES")){
                            JSONArray ZSCOS001_RES=retJson.getJSONArray("ZSCOS001_RES");
                            for (int i = 0; i < ZSCOS001_RES.size(); i++) {
                                JSONObject row=ZSCOS001_RES.getJSONObject(i);
                                log.info("遍历返回数据"+ZSCOS001_RES+row);
                                String ZQRSYH=row.containsKey("ZQRSYH")?row.getString("ZQRSYH"):"";
                                List upRow=new ArrayList();
                                if("S".equals(row.getString("ZRET"))){
                                    //处理成功，标识打上
                                    errmsg+="("+ZQRSYH+")"+row.getString("ZMSG");
                                    upRow.add(type);
                                    upRow.add(row.getString("ZMSG"));
                                    upRow.add(ZQRSYH);
                                    upList.add(upRow);
                                }else{
                                    //处理失败，标识维持原有,即如果是取消确认，则原标识为X，如果是确认，则原标识为空
                                    errmsg+="("+ZQRSYH+")"+row.getString("ZMSG");
                                    reMap.put("status",false);
                                    upRow.add(row.getString("ZMSG"));
                                    upRow.add(ZQRSYH);
                                    backupList.add(upRow);
                                }
                            }
                            if(upList.size()>0){
                                log.info("执行更新");
                                rs.executeBatchSql(sql,upList);
                            }
                            if(backupList.size()>0){
                                log.info("执行更新历史");
                                rs.executeBatchSql(backsql,backupList);
                            }
                            reMap.put("msg",errmsg);
                            log.info(doMsg+"完成！");
                        }else{
                            log.info(doMsg+"数据已推送，但未返回成功结果,确认失败！");
                            reMap.put("status",false);
                            reMap.put("msg",doMsg+"数据已推送，但未返回成功结果,确认失败！");
                            sql="update uf_sydlb set ZMSG='"+doMsg+"数据已推送，但未返回成功结果,确认失败！"+"' where id in ("+ids+")";
                            rs.executeUpdate(sql);
                        }
                    }else{
                        log.info(doMsg+"失败,"+retJson.getString("RETMSG"));
                        reMap.put("status",false);
                        reMap.put("msg",doMsg+"失败,"+retJson.getString("RETMSG"));
                        sql="update uf_sydlb set ZMSG='"+retJson.getString("RETMSG")+"' where id in ("+ids+")";
                        rs.executeUpdate(sql);
                    }
                }else{
                    log.info(doMsg+"失败,"+response.body());
                    reMap.put("status",false);
                    reMap.put("msg",doMsg+"失败,"+response.body());
                    sql="update uf_sydlb set ZMSG='"+"取消失败,"+response.body()+"' where id in ("+ids+")";
                    rs.executeUpdate(sql);
                }
            }else{
                log.info(doMsg+"没有需要处理的数据type:"+type);
                reMap.put("status",false);
                reMap.put("msg",doMsg+"没有需要处理的数据type:"+type);
            }
        }catch (Exception e){
            log.info(doMsg+"失败,第三方接口异常："+e);
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
            String sql="update uf_sydlb set ZMSG='"+doMsg+"失败,第三方接口异常："+e.getMessage()+"' where id in ("+ids+")";
            RecordSet rs=new RecordSet();
            rs.executeUpdate(sql);
        }
        return reMap;
    }
}
