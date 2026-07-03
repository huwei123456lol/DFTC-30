package com.engine.dfxm.cmd.fhd;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.dfxm.manager.PropertiesManager;
import com.engine.dfxm.util.DFXMModeUtil;
import com.engine.dfxm.util.TransUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 发货信息接口
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class CeptSrmDeliveryDetailCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public CeptSrmDeliveryDetailCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }



    /**
     * 1.发货时校验检查卡是否全部发送
     * 2.未全部发送时不允许发货
     * 3.用户填写检查卡，填写完后可在列表页面批量发送
     * @author wangkun
     * @date 2024-6-4 18:01
     * @param commandContext
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        String ids = (String) params.get("ids");
        String doMsg = (String) params.get("doMsg");
        String ORDER_CODE = (String) params.get("ORDER_CODE");
        //先根据发货单id和订单号检验发货单数量是否超出
        Map<String, Object> reMap= DFXMModeUtil.doCheckFHDSL(ids,ORDER_CODE);
        boolean slcheck=(boolean)reMap.get("status");
        if(slcheck){
            //创建检查卡
            //reMap=doSend360JCK("360JCK",ids,"",doMsg+"(360)");
            //boolean jck=(boolean)reMap.get("status");
            //if(jck){
                reMap=doSend360("360FH",ids,"",doMsg+"(360)");
            //}
        }
        return reMap;
    }


    /**
     * 检查卡信息推送，已去除，换成附件上传
     * @author wangkun
     * @date 2024-10-16 16:35
     * @param operateCode
     * @param ids
     * @param type
     * @param doMsg
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> doSend360JCK(String operateCode, String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
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
            //表
            String modetablename=pmObj.getString("modetablename");
            String tableto=pmObj.getString("tableto");
            String sql="select  *  from "+modetablename+" where id="+ids;
            RecordSet rs=new RecordSet();
            rs.executeQuery(sql);
            reMap.put("status",true);
            String msg="";
            JSONObject data=new JSONObject();
            JSONArray mainData=pmObj.getJSONArray("mainData");
            if (rs.next()){
                if("1".equals(rs.getString("process_control"))){
                    //重点管控需要填写检查卡
                    if("0".equals(rs.getString("jcksftscg"))){
                    //    检查卡已经推送成功，不重复推送
                        reMap.put("status",true);
                        return reMap;
                    }
                    JSONObject mainParamData=new JSONObject();
                    for (int i = 0; i < mainData.size(); i++) {
                        JSONObject field=mainData.getJSONObject(i);
                        TransUtil.setFieldValue(mainParamData,field,Util.null2String(rs.getString(field.getString("f"))));
                    }
                    data.put(tableto,mainParamData);
                    if(pmObj.containsKey("detailData")){
                        JSONArray detailData=pmObj.getJSONArray("detailData");
                        for (int i = 0; i < detailData.size(); i++) {
                            JSONObject detail=detailData.getJSONObject(i);
                            JSONArray dtFieldData=detail.getJSONArray("dtFieldData");
                            String fromTable=detail.getString("fromTable");
                            String toTable=detail.getString("toTable");
                            String mainPKFieldFrom=detail.getString("mainPKFieldFrom");
                            String mainPKFieldTo=detail.getString("mainPKFieldTo");
                            String detailSql="select * from "+modetablename+"_"+fromTable+" where "+mainPKFieldTo+"='"+rs.getString(mainPKFieldFrom)+"'";
                            RecordSet detailRs=new RecordSet();
                            detailRs.executeQuery(detailSql);
                            if(!detailRs.next()){
                                reMap.put("status",false);
                                reMap.put("msg","检查卡数据未填写");
                                return reMap;
                            }
                            detailRs.beforFirst();
                            JSONArray detailDataArr=new JSONArray();
                            //获取多行数据
                            while (detailRs.next()){
                                JSONObject detailDataRow=new JSONObject();
                                //遍历字段数组
                                for (int j = 0; j < dtFieldData.size(); j++) {
                                    JSONObject field=dtFieldData.getJSONObject(j);
                                    TransUtil.setFieldValue(detailDataRow,field,Util.null2String(detailRs.getString(field.getString("f"))));
                                }
                                detailDataArr.add(detailDataRow);
                            }
                            //    同一个明细数据获取完毕，放入数据
                            data.put(toTable,detailDataArr);
                        }
                    }
                    //数据全部封装完毕，创建请求，推送数据
                    log.info(doMsg+"推送报文: "+"  params:"+data);
                    //if(true){
                    //    RecordSet uprs=new RecordSet();
                    //    uprs.executeUpdate("update "+modetablename+" set jcksftscg=0 where id="+ids);
                    //    reMap.put("status",true);
                    //    return reMap;
                    //}
                    HttpRequest request=HttpUtil.createPost(apiUrl);
                    request.header("Authorization",password);
                    request.header("Content-Type",contentType);
                    request.body(data.toString());
                    HttpResponse response=request.execute();
                    if(response.isOk()){
                        String ret=response.body();
                        log.info(doMsg+"返回报文："+ret);
                        JSONObject retJson=JSONObject.fromObject(ret);
                        if("200".equals(retJson.getString("code"))){
                            JSONObject retdata=retJson.getJSONObject("data");
                            if("S".equals(retdata.getString("MESSAGE_TYPE"))){
                                RecordSet uprs=new RecordSet();
                                uprs.executeUpdate("update "+modetablename+" set jcksftscg=0 where id="+ids);
                                reMap.put("status",true);
                                reMap.put("msg",doMsg+"成功，"+retdata.getString("MESSAGE"));
                            }else{
                                reMap.put("status",false);
                                reMap.put("msg",doMsg+"失败，"+retdata.getString("MESSAGE"));
                            }
                        }else{
                            reMap.put("status",false);
                            reMap.put("msg",doMsg+"失败,"+response.body());
                        }
                    }else{
                        reMap.put("status",false);
                        reMap.put("msg",doMsg+"失败,"+response.body());
                    }
                }else{
                    //不需填写检查卡
                    //不是管控，则需要上传附件，判断附件是否上传

                    reMap.put("status",true);
                }
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
        }
        return reMap;
    }


    /**
     * 推送发货单到360
     * @author wangkun
     * @date 2024-10-16 15:38
     * @param operateCode
     * @param ids
     * @param type
     * @param doMsg
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> doSend360(String operateCode, String ids, String type, String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
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
            //表
            String modetablename=pmObj.getString("modetablename");
            String tableto=pmObj.getString("tableto");
            String sql="";
            if(pmObj.containsKey("mainDataSql")){
                sql=pmObj.getString("mainDataSql")+ids;
            }else{
                sql="select  *  from "+modetablename+" where id="+ids+"";
            }
            RecordSet rs=new RecordSet();
            rs.executeQuery(sql);
            reMap.put("status",true);
            String msg="";
            JSONObject data=new JSONObject();
            JSONArray dataArr=new JSONArray();
            JSONArray mainData=pmObj.getJSONArray("mainData");
            String paramstr="";
            while (rs.next()){
                if("1".equals(rs.getString("fhzt"))){
                //    已发货,不允许重复发
                    reMap.put("status",true);
                    return reMap;
                }
                JSONObject mainParamData=new JSONObject();
                for (int i = 0; i < mainData.size(); i++) {
                    JSONObject field=mainData.getJSONObject(i);
                    TransUtil.setFieldValue(mainParamData,field,Util.null2String(rs.getString(field.getString("f"))));
                }
                if(!"".equals(tableto)){
                    data.put(tableto,mainParamData);
                }else{
                    dataArr.add(mainParamData);
                }
            }
            if(dataArr.size()>0){
                //数据全部封装完毕，创建请求，推送数据
                log.info(doMsg+"推送报文: "+"  params:"+dataArr);
                //if(true){
                //    return reMap;
                //}
                HttpRequest request= HttpUtil.createPost(apiUrl);
                request.header("Authorization",password);
                request.header("Content-Type",contentType);
                request.body(dataArr.toString());
                HttpResponse response=request.execute();
                if(response.isOk()){
                    String ret=response.body();
                    log.info(doMsg+"返回报文："+ret);
                    JSONObject retJson=JSONObject.fromObject(ret);
                    if("200".equals(retJson.getString("code"))){
                        JSONObject retdata=retJson.getJSONObject("data");
                        if("S".equals(retdata.getString("MESSAGE_TYPE"))){
                            reMap.put("status",true);
                            RecordSet uprs=new RecordSet();
                            uprs.executeUpdate("update "+modetablename+" set fhzt=1 where id="+ids);
                            DFXMModeUtil.calculateOrderNumByFHOrderCode(ids);
                            reMap.put("msg",doMsg+"成功，"+retdata.getString("MESSAGE"));
                        }else{
                            reMap.put("status",false);
                            reMap.put("msg",doMsg+"失败，"+retdata.getString("MESSAGE"));
                        }
                    }else{
                        reMap.put("status",false);
                        reMap.put("msg",doMsg+"失败,"+response.body());
                    }
                }else{
                    reMap.put("status",false);
                    reMap.put("msg",doMsg+"失败,"+response.body());
                }
            }else{
                reMap.put("status",false);
                reMap.put("msg",doMsg+"失败,未获取到推送数据");
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
        }
        return reMap;
    }


    private Map<String, Object> doSend3601(String operateCode, String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
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
            //表
            String modetablename=pmObj.getString("modetablename");
            String tableto=pmObj.getString("tableto");
            String sql="select  *  from "+modetablename+" where id="+ids+"";
            RecordSet rs=new RecordSet();
            rs.executeQuery(sql);
            reMap.put("status",true);
            String msg="";
            JSONObject data=new JSONObject();
            JSONArray dataArr=new JSONArray();
            JSONArray mainData=pmObj.getJSONArray("mainData");
            String paramstr="";
            if (rs.next()){
                JSONObject mainParamData=new JSONObject();
                for (int i = 0; i < mainData.size(); i++) {
                    JSONObject field=mainData.getJSONObject(i);
                    TransUtil.setFieldValue(mainParamData,field,Util.null2String(rs.getString(field.getString("f"))));
                }
                if(!"".equals(tableto)){
                    data.put(tableto,mainParamData);
                    if(pmObj.containsKey("detailData")){
                        JSONArray detailData=pmObj.getJSONArray("detailData");
                        for (int i = 0; i < detailData.size(); i++) {
                            JSONObject detail=detailData.getJSONObject(i);
                            JSONArray dtFieldData=detail.getJSONArray("dtFieldData");
                            String fromTable=detail.getString("fromTable");
                            String toTable=detail.getString("toTable");
                            String mainPKFieldFrom=detail.getString("mainPKFieldFrom");
                            String mainPKFieldTo=detail.getString("mainPKFieldTo");
                            String detailSql="select * from "+modetablename+"_"+fromTable+" where "+mainPKFieldTo+"='"+rs.getString(mainPKFieldFrom)+"'";
                            RecordSet detailRs=new RecordSet();
                            detailRs.executeQuery(detailSql);
                            JSONArray detailDataArr=new JSONArray();
                            //获取多行数据
                            while (detailRs.next()){
                                JSONObject detailDataRow=new JSONObject();
                                //遍历字段数组
                                for (int j = 0; j < dtFieldData.size(); j++) {
                                    JSONObject field=dtFieldData.getJSONObject(j);
                                    TransUtil.setFieldValue(detailDataRow,field,Util.null2String(detailRs.getString(field.getString("f"))));
                                }
                                detailDataArr.add(detailDataRow);
                            }
                            //    同一个明细数据获取完毕，放入数据
                            data.put(toTable,detailDataArr);
                        }
                    }
                    paramstr=data.toString();
                }else{
                    dataArr.add(mainParamData);
                    paramstr=dataArr.toString();
                }
                //JSONArray d=JSONArray.fromObject("[ " +
                //        "    { " +
                //        "        \"DELIVERY_CODE\": \"202311200001\", " +
                //        "        \"DELIVERY_LINECODE\": \"1\", " +
                //        "        \"FROM_ORDER_CODE\": \"O202311010001\", " +
                //        "        \"FROM_ORDER_LINECODE\": \"1\", " +
                //        "        \"MATTER_CODE\": \"cdsccdc11\", " +
                //        "        \"MATTER_NAME\": \"焊接四角螺母\", " +
                //        "        \"SAP_MATTER_CODE\": \"1\", " +
                //        "        \"SUP_MATTER_CODE\": \"2\", " +
                //        "        \"NOTICE\": \"notice1\", " +
                //        "        \"PROCESS_STATE\": \"comprehensive_process1\", " +
                //        "        \"ICODE\": \"cdsccdc11147\", " +
                //        "        \"ZSJ_FLAG\": \"Y\", " +
                //        "        \"ZSJ_ICODE\": \"cdsccdc11147\", " +
                //        "        \"DELIVERY_BATCH\": \"202311030003\", " +
                //        "        \"DATA_SOURCE_EID\": \"BU_PUR_ORDER_DETAIL_4585e5fdeb1118r894626b47f4e591rt\", " +
                //        "        \"STYLIST_USERCODE\": \"2\", " +
                //        "        \"COLOR\": \"part_color1\", " +
                //        "        \"STANDARD_PRICE\": \"1\", " +
                //        "        \"STANDARD_ACCOUNT\": \"2\", " +
                //        "        \"SAP_ORDER_LINECODE\": \"1\", " +
                //        "        \"SAP_ORDER_CODE\": \"2\", " +
                //        "        \"FIRST_WEIGHT_FLAG\": \"Y\", " +
                //        "        \"CHECK_FLAG\": \"Y\", " +
                //        "        \"URGENT_FLAG\": \"1\", " +
                //        "        \"APPLY_CODE\": \"A202311200001\", " +
                //        "        \"PRO_CODE\": \"A0091\", " +
                //        "        \"PRO_NAME\": \"A0091\", " +
                //        "        \"ORDER_TYPE\": \"30\", " +
                //        "        \"SEND_WH_NAME\": \"A01\", " +
                //        "        \"DELIVERY_FROM\": \"1\", " +
                //        "        \"CHECK_TYPE\": \"2\", " +
                //        "        \"SUPPLIER_CODE\": \"1\", " +
                //        "        \"SUPPLIER_NAME\": \"2\", " +
                //        "        \"XTDD_FLAG\": \"1\", " +
                //        "        \"ZYS_USERCODE\": \"2\", " +
                //        "        \"SZZG_USERCODE\": \"1\", " +
                //        "        \"DELIVERY_STATE\": \"2\", " +
                //        "        \"OPTYPE\": \"1\", " +
                //        "        \"READFLAG\": \"1\", " +
                //        "        \"RECEIVE_MSG\": \"2\", " +
                //        "        \"ARRIVAL_TIME\": \"2023-11-20\", " +
                //        "        \"ORDER_NUM\": \"1\", " +
                //        "        \"SEND_NUM\": \"2\" " +
                //        "    } " +
                //        "]");

                //paramstr=d.toString();
                //数据全部封装完毕，创建请求，推送数据
                log.info(doMsg+"推送报文: "+"  params:"+paramstr);
                HttpRequest request=HttpUtil.createPost(apiUrl);
                request.header("Authorization",password);
                request.header("Content-Type",contentType);
                request.body(paramstr);
                HttpResponse response=request.execute();
                if(response.isOk()){
                    String ret=response.body();
                    log.info(doMsg+"返回报文："+ret);
                    JSONObject retJson=JSONObject.fromObject(ret);
                    if("200".equals(retJson.getString("code"))){
                        JSONObject retdata=retJson.getJSONObject("data");
                        if("S".equals(retdata.getString("MESSAGE_TYPE"))){
                            reMap.put("status",true);
                            reMap.put("msg",doMsg+"成功，"+retdata.getString("MESSAGE"));
                        }else{
                            reMap.put("status",false);
                            reMap.put("msg",doMsg+"失败，"+retdata.getString("MESSAGE"));
                        }
                    }else{
                        reMap.put("status",false);
                        reMap.put("msg",doMsg+"失败,"+response.body());
                    }
                }else{
                    reMap.put("status",false);
                    reMap.put("msg",doMsg+"失败,"+response.body());
                }
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
        }
        return reMap;
    }

}
