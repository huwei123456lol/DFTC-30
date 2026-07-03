package com.engine.dfxm.webservice;

import cn.hutool.core.date.DateUtil;
import com.engine.dfxm.manager.PropertiesManager;
import com.engine.dfxm.util.DFXMModeUtil;
import com.engine.dfxm.util.TransUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.*;

/**
 *@ClassName FormModeServiceImpl
 *@Description 请说明该类的作用
 *@Author 86157
 *@Date 2023-11-14 11:05
 *@Version 1.0
 **/

public class FormModeServiceImpl implements FormModeService{
    private Logger logger= LoggerFactory.getLogger(FormModeServiceImpl.class);

    /**
     * 订单接收接口
     * PropertiesManager 对象为接口字段对应关系获取类，内容配置在uf_properties 建模中，
     * 根据fieldkey字段获取对应配置，实现动态字段调整，不需要调整代码
     * @author wangkun
     * @date 2024-10-16 14:28
     * @param xml
     * @return java.lang.String
     */
    @Override
    public String poOrderReceive(String xml) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("订单数据xml:"+xml);
            String dataStr= TransUtil.transXmlToJson(xml);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                PropertiesManager pm= new PropertiesManager("poOrder",1);
                JSONObject propJson=pm.getFieldValueObj();
                if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                    if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                        ret=updateDataByPK(dataArr,propJson);
                    }else{
                        ret=saveDataByPK(dataArr,propJson);
                    }
                }else{
                    ret=saveData(dataArr,propJson);
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret.toString();
    }


    /**
     * 订单关闭接口 更新订单状态字段
     * @author wangkun
     * @date 2024-10-16 14:28
     * @param xml
     * @return java.lang.String
     */
    @Override
    public String poOrderClose(String xml) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("订单关闭数据xml:"+xml);
            String dataStr= TransUtil.transXmlToJson(xml);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                PropertiesManager pm= new PropertiesManager("poOrderClose",1);
                JSONObject propJson=pm.getFieldValueObj();
                if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                    if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                        ret=updateDataByPK(dataArr,propJson);
                    }else{
                        ret=saveDataByPK(dataArr,propJson);
                    }
                }else{
                    ret=saveData(dataArr,propJson);
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","订单关闭数据异常："+e);
        }
        return ret.toString();
    }

    /**
     * 索引单接收，将索引单数据批量接收，根据订单号相同的放在一起，存入索引单建模中uf_sydlb
     * @author wangkun
     * @date 2024-10-16 14:27
     * @param xml
     * @return java.lang.String
     */
    @Override
    public String indexOrderReceive(String xml) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("索引单数据xml:"+xml);
            String dataStr= TransUtil.transXmlToJson(xml);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                logger.info("111=====");
                PropertiesManager pm= new PropertiesManager("indexOrder",1);
                logger.info("112=====");
                JSONObject propJson=pm.getFieldValueObj();
                logger.info("113=====");
                if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                    if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                        logger.info("114=====");
                        ret=updateDataByPK(dataArr,propJson);

                    }else{
                        logger.info("115=====");
                        ret=saveDataByPKBatch(dataArr,propJson);
                    }
                }else{
                    logger.info("116=====");
                    ret=saveData(dataArr,propJson);
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }finally {
            delSYDDT();
        }
        return ret.toString();
    }

    /**
     * 解决接口同时多次调用时推送了重复数据导致重复问题，删除多余重复数据，保留最新一条
     * @author wangkun
     * @date 2024-10-16 14:26
     * @return void
     */
    private void delSYDDT(){
        try {
            RecordSet rs=new RecordSet();
                //删除多次重复推送产生的重复明细数据，避免重复明细产生
            String sql="DELETE t1 FROM uf_sydlb_dt1 t1 " +
                    "JOIN (" +
                    "    SELECT min(id) as id " +
                    "    FROM uf_sydlb_dt1 " +
                    "    GROUP BY mainid, SAP_VOUCHER_CODE, SAP_VOUCHER_PROJECT_CODE, SAP_ORDER_PROJECT_CODE, SAP_MATERIAL_CODE " +
                    "    HAVING COUNT(id) > 1 " +
                    ") t2 ON t1.id = t2.id ";
            if("sqlserver".equals(rs.getDBType())){
                sql="delete from uf_sydlb_dt1 where id in ( " +
                        "select min(id) as id  from  uf_sydlb_dt1 group by mainid,SAP_VOUCHER_CODE,SAP_VOUCHER_PROJECT_CODE,SAP_ORDER_PROJECT_CODE,SAP_MATERIAL_CODE having count(id)>1 " +
                        ") ";
            }
            rs.executeUpdate(sql);
            sql= "select min(id) as id  from  uf_sydlb_dt1 group by mainid,SAP_VOUCHER_CODE,SAP_VOUCHER_PROJECT_CODE,SAP_ORDER_PROJECT_CODE,SAP_MATERIAL_CODE having count(id)>1 " ;
            rs.executeQuery(sql);
            if(rs.next()){
                delSYDDT();

            }
        }catch (Exception e){
            logger.error("删除重复数据异常"+e);
            return ;
        }
    }

    /**
     * 索引单发票校验反馈接口
     * @author wangkun
     * @date 2024-10-16 14:25
     * @param xml
     * @return java.lang.String
     */
    @Override
    public String indexOrderCheckReceive(String xml) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("索引单发票校验反馈数据xml:"+xml);
            String dataStr= TransUtil.transXmlToJson(xml);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                PropertiesManager pm= new PropertiesManager("indexOrderCheck",1);
                JSONObject propJson=pm.getFieldValueObj();
                if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                    if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                        ret=updateDataByPK(dataArr,propJson);
                    }else{
                        ret=saveDataByPK(dataArr,propJson);
                    }
                }else{
                    ret=saveData(dataArr,propJson);
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret.toString();
    }

    /**
     * 计划跟踪数据接收接口，对应建模计划进展反馈uf_JHJZFK
     * @author wangkun
     * @date 2024-10-16 14:30
     * @param dataStr
     * @return java.lang.String
     */
    @Override
    public String planTodoReceive(String dataStr) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("计划跟踪数据:"+dataStr);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                PropertiesManager pm= new PropertiesManager("planTodoReceive",1);
                JSONObject propJson=pm.getFieldValueObj();
                if(!"".equals(Util.null2String(pm.getFieldkey()))){
                    if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                        if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                            ret=updateDataByPK(dataArr,propJson);
                        }else{
                            ret=saveDataByPK(dataArr,propJson);
                        }
                    }else{
                        ret=saveData(dataArr,propJson);
                    }
                }else{
                    ret.put("msg","接收失败,planTodoReceive接口异常");
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret.toString();
    }

    /**
     * 收货确认接口，对应建模订单发货uf_df_ddfh ，更新发货单里面的收货数量
     * @author wangkun
     * @date 2024-10-16 14:31
     * @param dataStr 
     * @return java.lang.String
     */
    @Override
    public String receiveConfirm(String dataStr) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("收货确认数据:"+dataStr);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                PropertiesManager pm= new PropertiesManager("360SHQR",1);
                JSONObject propJson=pm.getFieldValueObj();
                if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                    if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                        ret=updateDataByPK(dataArr,propJson);
                    }else{
                        ret=saveDataByPK(dataArr,propJson);
                    }
                }else{
                    ret=saveData(dataArr,propJson);
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret.toString();
    }


    /**
     * 退货申请数据接收 对应建模 供应商退货uf_supplier_return
     * @author wangkun
     * @date 2024-10-16 14:33
     * @param dataStr
     * @return java.lang.String
     */
    @Override
    public String cancelGoods(String dataStr) {
        JSONObject ret=new JSONObject();
        ret.put("status",false);
        try {
            logger.info("退货申请数据:"+dataStr);
            if(dataStr!=null){
                JSONArray dataArr=JSONArray.fromObject(dataStr);
                PropertiesManager pm= new PropertiesManager("cancelGoods",1);
                JSONObject propJson=pm.getFieldValueObj();
                if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                    if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                        ret=updateDataByPK(dataArr,propJson);
                    }else{
                        ret=saveDataByPK(dataArr,propJson);
                    }
                }else{
                    ret=saveData(dataArr,propJson);
                }
            }else{
                ret.put("msg","接收失败,推送数据为空");
            }
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret.toString();
    }

    /**
     * 接收数据通用方法，只做新增
     * @author wangkun
     * @date 2024-10-16 14:33
     * @param dataArr
     * @param propJson
     * @return net.sf.json.JSONObject
     */
    public JSONObject saveData(JSONArray dataArr, JSONObject propJson) {
        JSONObject ret=new JSONObject();


        try{
            String modeid=propJson.getString("modeid");
            String modetablename=propJson.getString("modetablename");
            int defaultCreater=propJson.getInt("defaultCreater");
            RecordSet rs=new RecordSet();
            rs.executeQuery("select max(id) as id  from "+modetablename);
            Date sdate=new Date();
            String startDate=DateUtil.format(sdate, "yyyy-MM-dd");
            String startTime=DateUtil.format(sdate, "HH:mm:ss");
            String resetRightSql="select id from "+modetablename+" where modedatacreatedate>='"+startDate+"' and modedatacreatetime>='"+startTime+"' " ;
            if(rs.next()){
                resetRightSql+=" and  id>"+rs.getInt("id");
            }
            JSONArray mainData=propJson.getJSONArray("mainData");
            String insql="insert into "+modetablename+" (";
            String insertvsql="";
            for (int j = 0; j <mainData.size() ; j++) {
                JSONObject field=mainData.getJSONObject(j);
                String fromField=field.getString("f");
                String toField=field.containsKey("t")?field.getString("t"):fromField;
                insql+=toField+",";
                insertvsql+="?,";
            }
            insql+="formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,MODEUUID)values("+insertvsql+"?,?,?,?,?,?)";
            List<List> inList=new ArrayList<>();
            int inflag=0;
            RecordSet upRs=new RecordSet();
            RecordSet inRs=new RecordSet();
            RecordSet detailInRs=new RecordSet();
            boolean isdetail=propJson.containsKey("detailData");
            JSONArray detailData=isdetail?propJson.getJSONArray("detailData"):new JSONArray();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data=dataArr.getJSONObject(i);
                List row=new ArrayList();
                for (int j = 0; j <mainData.size() ; j++) {
                    JSONObject field=mainData.getJSONObject(j);
                    //row.add(data.getString(field.getString("f")));
                    TransUtil.setFieldValue(row,field,data);

                }
                inflag++;
                row.add(modeid);
                row.add(defaultCreater);
                row.add(0);
                Date date=new Date();
                row.add(DateUtil.format(date, "yyyy-MM-dd"));
                row.add(DateUtil.format(date, "HH:mm:ss"));
                row.add(UUID.randomUUID().toString());
                inList.add(row);

                //遍历所有明细配置，写入数据
                for (int j = 0; j < detailData.size(); j++) {
                    JSONObject dtData=detailData.getJSONObject(j);
                    String fromTable=dtData.getString("fromTable");
                    String toTable=dtData.getString("toTable");
                    String mainPKFieldFrom=dtData.getString("mainPKFieldFrom");

                    String mainPKFieldTo=dtData.getString("mainPKFieldTo");
                    JSONArray dtFieldData=dtData.getJSONArray("dtFieldData");
                    Object json=data.get(fromTable);
                    JSONArray fTableData=null;
                    if(json instanceof JSONArray){
                        fTableData=data.getJSONArray(fromTable);
                    }else{
                        fTableData=new JSONArray();
                        fTableData.add(data.getJSONObject(fromTable));
                    }
                    //开始循环行数据
                    String sql="insert into "+modetablename+"_"+toTable+" (";
                    String ins="values(";
                    for (int l = 0; l < dtFieldData.size(); l++) {
                        JSONObject dtfield=dtFieldData.getJSONObject(l);
                        String from=dtfield.getString("f");
                        String to=dtfield.containsKey("t")?dtfield.getString("t"):from;
                        sql+=to+",";
                        ins+="?,";
                    }
                    sql+=""+mainPKFieldTo+")";
                    ins+="?)";
                    sql+=ins;
                    List<List> detailList=new ArrayList<>();
                    for (int k = 0; k < fTableData.size(); k++) {
                        JSONObject rowData=fTableData.getJSONObject(k);
                        List rowList=new ArrayList();
                        for (int l = 0; l < dtFieldData.size(); l++) {
                            JSONObject dtfield=dtFieldData.getJSONObject(l);
                            TransUtil.setFieldValue(rowList,dtfield,rowData);
                            //rowList.add(rowData.getString(dtfield.getString("f")));
                        }
                        rowList.add(data.getString(mainPKFieldFrom));
                        detailList.add(rowList);
                    }
                    if(detailList.size()>0){
                        boolean insDt=detailInRs.executeBatchSql(sql,detailList);
                        if(!insDt){
                            ret.put("msg",detailInRs.getExceptionMsg());
                            logger.info("插入明细数据异常"+detailInRs.getExceptionMsg());
                            return ret;
                        }
                    }
                }
                if(inflag>1000){
                    boolean isok=inRs.executeBatchSql(insql,inList);
                    if(!isok){
                        ret.put("msg",inRs.getExceptionMsg());
                        return ret;
                    }
                    inflag=0;
                    inList=new ArrayList<>();
                }
            }
            if(inflag>0){
                boolean isok=inRs.executeBatchSql(insql,inList);
                if(!isok){
                    ret.put("msg",inRs.getExceptionMsg());
                    return ret;
                }
            }
            if(isdetail){
                for (int i = 0; i < detailData.size(); i++) {
                    JSONObject dtData=detailData.getJSONObject(i);
                    String toTable=modetablename+"_"+dtData.getString("toTable");
                    String mainPKFieldFrom=dtData.getString("mainPKFieldFrom");
                    String mainPKFieldTo=dtData.getString("mainPKFieldTo");
                    String sql="";
                    if("sqlserver".equals(rs.getDBType())){

                        sql="update "+toTable+
                                " set "+toTable+".mainid=t1.id " +
                                " from "+modetablename+" t1 where  t1."+mainPKFieldFrom+"="+toTable+"."+mainPKFieldTo+" " +
                                "  and "+toTable+".mainid is null ";
                    }else if("mysql".equals(rs.getDBType())){
                        sql="update "+toTable+" t , "+modetablename+" t1 "+
                                " set t.mainid=t1.id " +
                                " where t."+mainPKFieldTo+"=t1."+mainPKFieldFrom+"  and t.mainid is null " ;
                    }
                    logger.info("更新明细数据主表id==="+sql);
                    rs.executeUpdate(sql);
                }
            }
            // 创建权限重构线程
            String finalResetRightSql = resetRightSql;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        ModeRightInfo modeRightInfo=new ModeRightInfo();
                        modeRightInfo.setModeId(Integer.parseInt(modeid));
                        RecordSet rs=new RecordSet();
                        rs.executeQuery(finalResetRightSql);
                        while(rs.next()){
                            modeRightInfo.editModeDataShare(defaultCreater,Integer.parseInt(modeid),rs.getInt("id"));
                        }
                    }catch (Exception e){

                    }
                }
            });
            thread.start();
            ret.put("status",true);
            ret.put("msg","接收成功");
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret;
    }

    /**
     * 根据主键接收数据保存到建模，已存在则更新，不存则新增
     * @author wangkun
     * @date 2024-10-16 14:34 
     * @param dataArr 
     * @param propJson
     * @return net.sf.json.JSONObject
     */
    public JSONObject saveDataByPK(JSONArray dataArr, JSONObject propJson) {
        //前置ESB事件列表
        List<Map<String,String>> beforeEsbAction = new ArrayList<>();
        //后置ESB事件列表
        List<Map<String,String>> AfterEsbAction = new ArrayList<>();
        JSONObject ret=new JSONObject();
        try{
            String modeid=propJson.getString("modeid");
            String modetablename=propJson.getString("modetablename");
            JSONObject primaryKeyField=propJson.getJSONObject("primaryKeyField");
            String pkFrom=primaryKeyField.getString("f");
            String pkToT=primaryKeyField.containsKey("t")?primaryKeyField.getString("t"):pkFrom;
            String pkTo=pkToT;
            int defaultCreater=propJson.getInt("defaultCreater");
            JSONArray mainData=propJson.getJSONArray("mainData");
            String upsql="update "+modetablename+" set " ;
            String insql="insert into "+modetablename+" (";
            String insertvsql="";
            for (int j = 0; j <mainData.size() ; j++) {
                JSONObject field=mainData.getJSONObject(j);
                String fromField=field.getString("f");
                boolean onlyInsert=field.containsKey("i");
                String toField=field.containsKey("t")?field.getString("t"):fromField;
                insql+=toField+",";
                insertvsql+="?,";
                if(!onlyInsert){
                    upsql+=toField+"=?,";
                }
            }
            upsql+="modedatamodifier=?,modedatamodifydatetime=? where "+pkTo+"=? ";
            insql+=pkTo+",formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,MODEUUID)values("+insertvsql+"?,?,?,?,?,?,?)";
            List<List> upList=new ArrayList<>();
            List<List> inList=new ArrayList<>();
            List<String> sydList=new ArrayList<>();
            int upflag=0;
            int inflag=0;
            RecordSet upRs=new RecordSet();
            RecordSet inRs=new RecordSet();
            RecordSet detailInRs=new RecordSet();
            RecordSet checkDetailRs=new RecordSet();
            boolean isdetail=propJson.containsKey("detailData");
            JSONArray detailData=isdetail?propJson.getJSONArray("detailData"):new JSONArray();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data=dataArr.getJSONObject(i);
                String pkFromValue= Util.null2String(data.getString(pkFrom));
                List insertrow=new ArrayList();
                List uprow=new ArrayList();
                for (int j = 0; j <mainData.size() ; j++) {
                    JSONObject field=mainData.getJSONObject(j);
                    //row.add(data.getString(field.getString("f")));
                    boolean onlyInsert=field.containsKey("i");

                    TransUtil.setFieldValue(insertrow,field,data);
                    if(!onlyInsert){
                        TransUtil.setFieldValue(uprow,field,data);
                    }
                }
                sydList.add(data.getString(pkFrom));
                if(checkHas(modetablename,pkTo,pkFromValue)){
                    //如果存在数据，则更新
                    upflag++;
                    uprow.add(defaultCreater);
                    uprow.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    uprow.add(data.getString(pkFrom));
                    upList.add(uprow);
                }else{
                    inflag++;
                    insertrow.add(data.getString(pkFrom));
                    insertrow.add(modeid);
                    insertrow.add(defaultCreater);
                    insertrow.add(0);
                    Date date=new Date();
                    insertrow.add(DateUtil.format(date, "yyyy-MM-dd"));
                    insertrow.add(DateUtil.format(date, "HH:mm:ss"));
                    insertrow.add(UUID.randomUUID().toString());
                    inList.add(insertrow);
                    //不存在，则插入
                }
                if(upflag>1000){
                    boolean isok=upRs.executeBatchSql(upsql,upList);
                    if(!isok){
                        ret.put("msg",upRs.getExceptionMsg());
                        return ret;
                    }
                    upflag=0;
                    upList=new ArrayList<>();
                }
                if(inflag>1000){
                    boolean isok=inRs.executeBatchSql(insql,inList);
                    if(!isok){
                        ret.put("msg",inRs.getExceptionMsg());
                        return ret;
                    }
                    inflag=0;
                    inList=new ArrayList<>();
                }
                //遍历所有明细配置，写入数据
                for (int j = 0; j < detailData.size(); j++) {
                    JSONObject dtData=detailData.getJSONObject(j);
                    String fromTable=dtData.getString("fromTable");
                    String toTable=dtData.getString("toTable");
                    String mainPKFieldFrom=dtData.getString("mainPKFieldFrom");
                    String mainPKFieldFromV="";
                    if("id".equals(mainPKFieldFrom)){
                        RecordSet rsmaintable=new RecordSet();
                        rsmaintable.executeQuery("select id from "+modetablename+" where "+pkTo+" ='"+data.getString(pkFrom)+"'");
                        if(rsmaintable.next()){
                            mainPKFieldFromV= rsmaintable.getString("id");
                        }
                    }else{
                        mainPKFieldFromV=data.getString(mainPKFieldFrom);
                    }
                    String mainPKFieldTo=dtData.getString("mainPKFieldTo");
                    JSONArray dtFieldData=dtData.getJSONArray("dtFieldData");
                    JSONArray dtpk=dtData.containsKey("dtpk")?dtData.getJSONArray("dtpk"):new JSONArray();
                    if(dtpk.size()>0){
                        Object json=data.get(fromTable);
                        JSONArray fTableData=null;
                        if(json instanceof JSONArray){
                            fTableData=data.getJSONArray(fromTable);
                        }else{
                            fTableData=new JSONArray();
                            fTableData.add(data.getJSONObject(fromTable));
                        }
                        //开始循环行数据
                        String dtupsql="update "+modetablename+"_"+toTable+" set ";
                        String sql="insert into "+modetablename+"_"+toTable+" (";
                        String ins="values(";
                        for (int l = 0; l < dtFieldData.size(); l++) {
                            JSONObject dtfield=dtFieldData.getJSONObject(l);
                            String from=dtfield.getString("f");
                            boolean onlyInsert=dtfield.containsKey("i");
                            String to=dtfield.containsKey("t")?dtfield.getString("t"):from;
                            if(!onlyInsert){
                                dtupsql+=to+"=?,";
                            }
                            sql+=to+",";
                            ins+="?,";
                        }
                        String checkSql="select id from "+modetablename+"_"+toTable+" where 1=1 ";
                        dtupsql=dtupsql.substring(0,dtupsql.length()-1)+" where 1=1 ";
                        for (int k = 0; k < dtpk.size(); k++) {
                            JSONObject dtpkRow=dtpk.getJSONObject(k);
                            String dtpkf=dtpkRow.getString("f");
                            String dtpkt=dtpkRow.containsKey("t")?dtpkRow.getString("t"):dtpkf;
                            checkSql+=" and "+dtpkt+"=? ";
                            sql+=dtpkt+",";
                            ins+="?,";
                            dtupsql+=" and "+dtpkt+"=? ";
                        }
                        checkSql+=" and "+mainPKFieldTo+"=? ";

                        sql+=""+mainPKFieldTo+")";
                        ins+="?)";
                        dtupsql+=" and "+mainPKFieldTo+"=?";
                        sql+=ins;
                        List<List> detailList=new ArrayList<>();
                        List<List> updetailList=new ArrayList<>();
                        for (int k = 0; k < fTableData.size(); k++) {
                            JSONObject rowData=fTableData.getJSONObject(k);
                            List inrowList=new ArrayList();
                            List uprowList=new ArrayList();
                            for (int l = 0; l < dtFieldData.size(); l++) {
                                JSONObject dtfield=dtFieldData.getJSONObject(l);
                                boolean onlyInsert=dtfield.containsKey("i");
                                if(!onlyInsert){
                                    TransUtil.setFieldValue(uprowList,dtfield,rowData);
                                }
                                TransUtil.setFieldValue(inrowList,dtfield,rowData);
                                //rowList.add(rowData.getString(dtfield.getString("f")));
                            }
                            Object[] o=new Object[dtpk.size()+1];
                            for (int l = 0; l <dtpk.size() ; l++) {
                                JSONObject dtpkRow=dtpk.getJSONObject(l);
                                String dtpkf=dtpkRow.getString("f");
                                o[l]=rowData.getString(dtpkf);
                                uprowList.add(rowData.getString(dtpkf));
                                inrowList.add(rowData.getString(dtpkf));
                            }


                            o[o.length-1]=mainPKFieldFromV;
                            uprowList.add(mainPKFieldFromV);
                            inrowList.add(mainPKFieldFromV);
                            if(checkDetailRs.executeQuery(checkSql,o)&&checkDetailRs.next()){
                            //    存在明细数据
                                updetailList.add(uprowList);
                            }else{
                            //    不存在明细数据
                                detailList.add(inrowList);
                            }
                        }
                        if(detailList.size()>0){
                            boolean insDt=detailInRs.executeBatchSql(sql,detailList);
                            logger.info("插入明细数据sql："+ sql);
                            logger.info("插入明细数据list："+ detailList);
                            if(!insDt){
                                ret.put("msg",detailInRs.getExceptionMsg());
                                logger.info("插入明细数据异常"+detailInRs.getExceptionMsg());
                                return ret;
                            }
                        }
                        if(updetailList.size()>0){
                            boolean updt=detailInRs.executeBatchSql(dtupsql,updetailList);
                            logger.info("更新明细数据sql："+sql);
                            logger.info("更新明细数据list："+detailList);
                            if(!updt){
                                ret.put("msg",detailInRs.getExceptionMsg());
                                logger.info("更新明细数据异常"+detailInRs.getExceptionMsg());
                                return ret;
                            }
                        }
                    }else{
                        Object json=data.get(fromTable);
                        JSONArray fTableData=null;
                        if(json instanceof JSONArray){
                            fTableData=data.getJSONArray(fromTable);
                        }else{
                            fTableData=new JSONArray();
                            fTableData.add(data.getJSONObject(fromTable));
                        }
                        //开始循环行数据
                        String sql="insert into "+modetablename+"_"+toTable+" (";
                        String ins="values(";
                        for (int l = 0; l < dtFieldData.size(); l++) {
                            JSONObject dtfield=dtFieldData.getJSONObject(l);
                            String from=dtfield.getString("f");
                            String to=dtfield.containsKey("t")?dtfield.getString("t"):from;
                            sql+=to+",";
                            ins+="?,";
                        }
                        sql+=""+mainPKFieldTo+")";
                        ins+="?)";
                        sql+=ins;
                        List<List> detailList=new ArrayList<>();
                        for (int k = 0; k < fTableData.size(); k++) {
                            JSONObject rowData=fTableData.getJSONObject(k);
                            List rowList=new ArrayList();
                            for (int l = 0; l < dtFieldData.size(); l++) {
                                JSONObject dtfield=dtFieldData.getJSONObject(l);
                                TransUtil.setFieldValue(rowList,dtfield,rowData);
                                //rowList.add(rowData.getString(dtfield.getString("f")));
                            }
                            rowList.add(mainPKFieldFromV);
                            detailList.add(rowList);
                        }
                        if(detailList.size()>0){
                            boolean insDt=detailInRs.executeBatchSql(sql,detailList);
                            if(!insDt){
                                ret.put("msg",detailInRs.getExceptionMsg());
                                logger.info("插入明细数据异常"+detailInRs.getExceptionMsg());
                                return ret;
                            }
                        }
                    }
                }
            }
            if(upflag>0){
                boolean isok=upRs.executeBatchSql(upsql,upList);
                logger.info("更新明细数据sql 766："+upsql);
                logger.info("更新明细数据sql 767："+upList);

                if(!isok){
                    ret.put("msg",upRs.getExceptionMsg());
                    return ret;
                }
            }
            if(inflag>0){
                boolean isok=inRs.executeBatchSql(insql,inList);
                logger.info("插入明细数据sql 776："+upsql);
                logger.info("插入明细数据sql 777："+upList);
                if(!isok){
                    ret.put("msg",inRs.getExceptionMsg());
                    return ret;
                }
            }

            if(isdetail){
                RecordSet rs=new RecordSet();
                for (int i = 0; i < detailData.size(); i++) {
                    JSONObject dtData=detailData.getJSONObject(i);
                    String toTable=modetablename+"_"+dtData.getString("toTable");
                    String mainPKFieldFrom=dtData.getString("mainPKFieldFrom");
                    String mainPKFieldTo=dtData.getString("mainPKFieldTo");
                    String sql="";
                    if("sqlserver".equals(rs.getDBType())){

                        sql="update "+toTable+
                                " set "+toTable+".mainid=t1.id " +
                                " from "+modetablename+" t1 where  t1."+mainPKFieldFrom+"="+toTable+"."+mainPKFieldTo+" " +
                                "  and "+toTable+".mainid is null ";
                    }else if("mysql".equals(rs.getDBType())){
                        sql="update "+toTable+" t , "+modetablename+" t1 "+
                                " set t.mainid=t1.id " +
                                " where t."+mainPKFieldTo+"=t1."+mainPKFieldFrom+"  and t.mainid is null " ;
                    }
                    logger.info("更新明细数据主表id==="+sql);
                    rs.executeUpdate(sql);
                }
            }

            //数据接收后，计算采购订单金额等和数量等字段
            if("uf_cs_ddgl".equals(modetablename)){
                DFXMModeUtil.calculateOrderNum(sydList);
                DFXMModeUtil.calculateIndexOrder(sydList);
            }


            // 权限重构
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 调用自定义的方法
                    try{
                        ModeRightInfo modeRightInfo=new ModeRightInfo();
                        modeRightInfo.setModeId(Integer.parseInt(modeid));
                        RecordSet rs=new RecordSet();
                        for (int i = 0; i < sydList.size(); i++) {
                            rs.executeQuery("select id from "+modetablename+" where "+pkTo+"='"+sydList.get(i)+"'");
                            while(rs.next()){
                                modeRightInfo.editModeDataShare(defaultCreater,Integer.parseInt(modeid),rs.getInt("id"));
                            }
                        }
                    }catch (Exception e){

                    }
                }
            });
            thread.start();
            ret.put("status",true);
            ret.put("msg","接收成功");
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret;
    }

    /**
     * 根据主键接收数据保存到建模，已存在则更新，不存则新增
     * @author wangkun
     * @date 2024-5-28 9:51 
     * @param dataArr 
     * @param propJson
     * @return net.sf.json.JSONObject
     */
    public JSONObject saveDataByPKBatch(JSONArray dataArr, JSONObject propJson) {
        JSONObject ret=new JSONObject();
        try{
            //获取模块信息
            String modeid=propJson.getString("modeid");
            String modetablename=propJson.getString("modetablename");
            //获取主键字段
            JSONObject primaryKeyField=propJson.getJSONObject("primaryKeyField");
            String pkFrom=primaryKeyField.getString("f");
            String pkToT=primaryKeyField.containsKey("t")?primaryKeyField.getString("t"):pkFrom;
            String pkTo=pkToT;
            int defaultCreater=propJson.getInt("defaultCreater");
            JSONArray mainData=propJson.getJSONArray("mainData");
            String upsql="update "+modetablename+" set " ;
            String insql="insert into "+modetablename+" (";
            String insertvsql="";
            String pkvStr="'-1'";
            Set<String> mainPkList=new HashSet<>();
            logger.info("1===");
            for (int j = 0; j <mainData.size() ; j++) {
                JSONObject field=mainData.getJSONObject(j);
                String fromField=field.getString("f");
                boolean onlyInsert=field.containsKey("i");
                String toField=field.containsKey("t")?field.getString("t"):fromField;
                insql+=toField+",";
                insertvsql+="?,";
                if(!onlyInsert){
                    upsql+=toField+"=?,";
                }
            }
            logger.info("2===");

            upsql+="modedatamodifier=?,modedatamodifydatetime=? where "+pkTo+"=? ";
            insql+=pkTo+",formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,MODEUUID)values("+insertvsql+"?,?,?,?,?,?,?)";
            List<List> upList=new ArrayList<>();
            List<List> inList=new ArrayList<>();
            List<String> sydList=new ArrayList<>();
            int upflag=0;
            int inflag=0;
            RecordSet upRs=new RecordSet();
            RecordSet inRs=new RecordSet();
            RecordSet detailInRs=new RecordSet();
            RecordSet statusTagRs=new RecordSet();
            RecordSet checkDetailRs=new RecordSet();
            boolean isdetail=propJson.containsKey("detailData");
            JSONArray detailData=isdetail?propJson.getJSONArray("detailData"):new JSONArray();
            logger.info("3===");
            String statusUID = String.valueOf(UUID.randomUUID());

            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data=dataArr.getJSONObject(i);
                String pkFromValue= Util.null2String(data.getString(pkFrom));
                if(pkvStr.indexOf("'"+pkFromValue+"'")<0){
                    pkvStr+=",'"+pkFromValue+"'";
                }
                List insertrow=new ArrayList();
                List uprow=new ArrayList();
                for (int j = 0; j <mainData.size() ; j++) {
                    JSONObject field=mainData.getJSONObject(j);
                    //row.add(data.getString(field.getString("f")));
                    boolean onlyInsert=field.containsKey("i");
                    TransUtil.setFieldValue(insertrow,field,data);
                    if(!onlyInsert){
                        TransUtil.setFieldValue(uprow,field,data);
                    }
                }
                sydList.add(data.getString(pkFrom));
                if(mainPkList.contains(pkFromValue)||checkHas(modetablename,pkTo,pkFromValue)){
                    //如果存在数据，则更新
                    upflag++;
                    uprow.add(defaultCreater);
                    uprow.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    uprow.add(data.getString(pkFrom));
                    upList.add(uprow);
                }else{
                    inflag++;
                    insertrow.add(data.getString(pkFrom));
                    insertrow.add(modeid);
                    insertrow.add(defaultCreater);
                    insertrow.add(0);
                    Date date=new Date();
                    insertrow.add(DateUtil.format(date, "yyyy-MM-dd"));
                    insertrow.add(DateUtil.format(date, "HH:mm:ss"));
                    insertrow.add(UUID.randomUUID().toString());
                    inList.add(insertrow);
                    //不存在，则插入
                    mainPkList.add(pkFromValue);
                }

                if(inflag>1000){
                    boolean isok=inRs.executeBatchSql(insql,inList);
                    if(!isok){
                        ret.put("msg",inRs.getExceptionMsg());
                        return ret;
                    }
                    inflag=0;
                    inList=new ArrayList<>();
                }

                if(upflag>1000){
                    boolean isok=upRs.executeBatchSql(upsql,upList);
                    if(!isok){
                        ret.put("msg",upRs.getExceptionMsg());
                        return ret;
                    }
                    upflag=0;
                    upList=new ArrayList<>();
                }


                //遍历所有明细配置，写入数据
                for (int j = 0; j < detailData.size(); j++) {
                    JSONObject dtData=detailData.getJSONObject(j);
                    String fromTable=dtData.getString("fromTable");
                    String toTable=dtData.getString("toTable");
                    String mainPKFieldFrom=dtData.getString("mainPKFieldFrom");



                    String mainPKFieldFromV="";
                    if("id".equals(mainPKFieldFrom)){
                        RecordSet rsmaintable=new RecordSet();
                        rsmaintable.executeQuery("select id from "+modetablename+" where "+pkTo+" ='"+data.getString(pkFrom)+"'");
                        if(rsmaintable.next()){
                            mainPKFieldFromV= rsmaintable.getString("id");
                        }
                    }else{
                        mainPKFieldFromV=data.getString(mainPKFieldFrom);
                    }
                    String mainPKFieldTo=dtData.getString("mainPKFieldTo");
                    JSONArray dtFieldData=dtData.getJSONArray("dtFieldData");
                    JSONArray dtpk=dtData.containsKey("dtpk")?dtData.getJSONArray("dtpk"):new JSONArray();

                    //String status_field=dtData.getString("status_field");
                    ////获取无效状态字段值
                    //String invalid_value=dtData.getString("invalid_value");
                    //if(dtData.containsKey("status_field")){
                    //    //未更新数据无效化处理
                    //    String invalidTagSql = "update "+modetablename+"_"+toTable+ " set " + status_field + "='" + invalid_value + "' where 1=1 "
                    //            +" and "+mainPKFieldTo+"=? ";
                    //
                    //    boolean isupStatus = statusTagRs.executeUpdate(invalidTagSql,mainPKFieldFromV);
                    //    logger.info("已存在数据无效化处理SQL"+invalidTagSql);
                    //    logger.info("已存在数据无效化处理param" +mainPKFieldFromV);
                    //
                    //}


                    if(dtpk.size()>0){
                        Object json="".equals(fromTable)?data:data.get(fromTable);
                        JSONArray fTableData=null;
                        if(json instanceof JSONArray){
                            fTableData=(JSONArray)json;
                        }else{
                            fTableData=new JSONArray();
                            fTableData.add((JSONObject)json);
                        }
                        //开始循环行数据
                        String dtupsql="update "+modetablename+"_"+toTable+" set ";
                        String sql="insert into "+modetablename+"_"+toTable+" (";
                        String ins="values(";
                        for (int l = 0; l < dtFieldData.size(); l++) {
                            JSONObject dtfield=dtFieldData.getJSONObject(l);
                            String from=dtfield.getString("f");
                            boolean onlyInsert=dtfield.containsKey("i");
                            String to=dtfield.containsKey("t")?dtfield.getString("t"):from;
                            if(!onlyInsert){
                                dtupsql+=to+"=?,";
                            }
                            sql+=to+",";
                            ins+="?,";
                        }
                        if(dtData.containsKey("useStatusUID")){
                            dtupsql+="statusUID=?,";
                            sql+= "statusUID,";
                            ins+="?,";
                        }

                        String checkSql="select id from "+modetablename+"_"+toTable+" where 1=1 ";
                        dtupsql=dtupsql.substring(0,dtupsql.length()-1)+" where 1=1 ";
                        for (int k = 0; k < dtpk.size(); k++) {
                            JSONObject dtpkRow=dtpk.getJSONObject(k);
                            String dtpkf=dtpkRow.getString("f");
                            String dtpkt=dtpkRow.containsKey("t")?dtpkRow.getString("t"):dtpkf;
                            checkSql+=" and "+dtpkt+"=? ";
                            //invalidTagSql+=" and "+dtpkt+"=? ";
                            sql+=dtpkt+",";
                            ins+="?,";
                            dtupsql+=" and "+dtpkt+"=? ";
                        }
                        checkSql+=" and "+mainPKFieldTo+"=? ";

                        sql+=""+mainPKFieldTo+")";
                        ins+="?)";
                        dtupsql+=" and "+mainPKFieldTo+"=?";
                        sql+=ins;
                        List<List> detailList=new ArrayList<>();
                        List<List> updetailList=new ArrayList<>();
                        for (int k = 0; k < fTableData.size(); k++) {
                            JSONObject rowData=fTableData.getJSONObject(k);
                            List inrowList=new ArrayList();
                            List uprowList=new ArrayList();
                            for (int l = 0; l < dtFieldData.size(); l++) {
                                JSONObject dtfield=dtFieldData.getJSONObject(l);
                                boolean onlyInsert=dtfield.containsKey("i");
                                if(!onlyInsert){
                                    TransUtil.setFieldValue(uprowList,dtfield,rowData);
                                }
                                TransUtil.setFieldValue(inrowList,dtfield,rowData);
                                //rowList.add(rowData.getString(dtfield.getString("f")));
                            }
                            if(dtData.containsKey("useStatusUID")){
                                uprowList.add(statusUID);
                                inrowList.add(statusUID);
                            }

                            Object[] o=new Object[dtpk.size()+1];
                            for (int l = 0; l <dtpk.size() ; l++) {
                                JSONObject dtpkRow=dtpk.getJSONObject(l);
                                String dtpkf=dtpkRow.getString("f");
                                o[l]=rowData.getString(dtpkf);
                                uprowList.add(rowData.getString(dtpkf));
                                inrowList.add(rowData.getString(dtpkf));
                            }


                            o[o.length-1]=mainPKFieldFromV;


                            uprowList.add(mainPKFieldFromV);
                            inrowList.add(mainPKFieldFromV);
                            if(checkDetailRs.executeQuery(checkSql,o)&&checkDetailRs.next()){
                            //存在明细数据
                                //对已存在数据标记无效

                                updetailList.add(uprowList);
                            }else{
                            //    不存在明细数据
                                detailList.add(inrowList);
                            }
                        }


                        if(detailList.size()>0){
                            boolean insDt=detailInRs.executeBatchSql(sql,detailList);
                            if(!insDt){
                                ret.put("msg",detailInRs.getExceptionMsg());
                                logger.info("插入明细数据异常"+detailInRs.getExceptionMsg());
                                return ret;
                            }
                        }

                        if(updetailList.size()>0){
                            boolean updt=detailInRs.executeBatchSql(dtupsql,updetailList);
                            if(!updt){
                                ret.put("msg",detailInRs.getExceptionMsg());
                                logger.info("更新明细数据异常"+detailInRs.getExceptionMsg());
                                return ret;
                            }
                        }

                        if(dtData.containsKey("useStatusUID")){
                            String deleteInvalidDataSql = "delete from "+modetablename+"_"+toTable+ " where (" + "statusUID<>'" + statusUID + "' or statusUID is null)  and 1=1 "
                                        +" and "+mainPKFieldTo+"=? ";
                               boolean isDelStatus = statusTagRs.executeUpdate(deleteInvalidDataSql,mainPKFieldFromV);
                               logger.info("删除无效数据处理SQL"+deleteInvalidDataSql);
                               logger.info("删除无效数据处理param" +mainPKFieldFromV);

                        }
                    }else{
                        Object json=data.get(fromTable);
                        JSONArray fTableData=null;
                        if(json instanceof JSONArray){
                            fTableData=data.getJSONArray(fromTable);
                        }else{
                            fTableData=new JSONArray();
                            fTableData.add(data.getJSONObject(fromTable));
                        }
                        //开始循环行数据
                        String sql="insert into "+modetablename+"_"+toTable+" (";
                        String ins="values(";
                        for (int l = 0; l < dtFieldData.size(); l++) {
                            JSONObject dtfield=dtFieldData.getJSONObject(l);
                            String from=dtfield.getString("f");
                            String to=dtfield.containsKey("t")?dtfield.getString("t"):from;
                            sql+=to+",";
                            ins+="?,";
                        }
                        sql+=""+mainPKFieldTo+")";
                        ins+="?)";
                        sql+=ins;
                        List<List> detailList=new ArrayList<>();
                        for (int k = 0; k < fTableData.size(); k++) {
                            JSONObject rowData=fTableData.getJSONObject(k);
                            List rowList=new ArrayList();
                            for (int l = 0; l < dtFieldData.size(); l++) {
                                JSONObject dtfield=dtFieldData.getJSONObject(l);
                                TransUtil.setFieldValue(rowList,dtfield,rowData);
                                //rowList.add(rowData.getString(dtfield.getString("f")));
                            }
                            rowList.add(mainPKFieldFromV);
                            detailList.add(rowList);
                        }
                        if(detailList.size()>0){
                            boolean insDt=detailInRs.executeBatchSql(sql,detailList);
                            if(!insDt){
                                ret.put("msg",detailInRs.getExceptionMsg());
                                logger.info("插入明细数据异常"+detailInRs.getExceptionMsg());
                                return ret;
                            }
                        }
                    }
                    //清除无效数据
                    //if(dtData.containsKey("status_field")){
                    //    String deleteInvalidDataSql = "delete from "+modetablename+"_"+toTable+ " where " + status_field + "='" + invalid_value + "' and 1=1 "
                    //            +" and "+mainPKFieldTo+"=? ";
                    //    boolean isDelStatus = statusTagRs.executeUpdate(deleteInvalidDataSql,mainPKFieldFromV);
                    //    logger.info("删除无效数据处理SQL"+deleteInvalidDataSql);
                    //    logger.info("删除无效数据处理param" +mainPKFieldFromV);
                    //}

                }
            }

            logger.info("3===insql"+insql);
            logger.info("3===upsql"+upsql);

            if(inflag>0){
                boolean isok=inRs.executeBatchSql(insql,inList);
                if(!isok){
                    ret.put("msg",inRs.getExceptionMsg());
                    return ret;
                }
            }

            if(upflag>0){
                boolean isok=upRs.executeBatchSql(upsql,upList);
                if(!isok){
                    ret.put("msg",upRs.getExceptionMsg());
                    return ret;
                }
            }

            logger.info("4===");

            if(isdetail){
                RecordSet rs=new RecordSet();
                for (int i = 0; i < detailData.size(); i++) {
                    JSONObject dtData=detailData.getJSONObject(i);
                    String toTable=modetablename+"_"+dtData.getString("toTable");
                    String mainPKFieldFrom=dtData.getString("mainPKFieldFrom");
                    String mainPKFieldTo=dtData.getString("mainPKFieldTo");
                    String sql="";
                    if("sqlserver".equals(rs.getDBType())){

                        sql="update "+toTable+
                                " set "+toTable+".mainid=t1.id " +
                                " from "+modetablename+" t1 where  t1."+mainPKFieldFrom+"="+toTable+"."+mainPKFieldTo+" " +
                                "  and "+toTable+".mainid is null ";
                    }else if("mysql".equals(rs.getDBType())){
                        sql="update "+toTable+" t , "+modetablename+" t1 "+
                                " set t.mainid=t1.id " +
                                " where t."+mainPKFieldTo+"=t1."+mainPKFieldFrom+"  and t.mainid is null " ;
                    }
                    logger.info("更新明细数据主表id==="+sql);
                    rs.executeUpdate(sql);
                }

                logger.info("5===");

                if(propJson.containsKey("sumData")){
                    JSONArray sumData=propJson.getJSONArray("sumData");
                    for (int i = 0; i < sumData.size(); i++) {
                        JSONObject row=sumData.getJSONObject(i);
                        String table=modetablename+row.getString("table");
                        JSONArray sumfield=row.getJSONArray("sumfield");
                        String sumfiledStr="mainid";
                        String setfieldStr="";
                        for (int j = 0; j < sumfield.size(); j++) {
                            JSONObject sumRow=sumfield.getJSONObject(j);
                            String f=sumRow.getString("f");
                            String t=sumRow.getString("f");
                            sumfiledStr+=",sum("+f+") "+f +" ";
                            setfieldStr=modetablename+"."+t+"=a."+f+",";
                        }
                        setfieldStr=setfieldStr.substring(0,setfieldStr.length()-1);
                        String sql="";
                        if("mysql".equals(rs.getDBType())){
                            sql=  "update " +modetablename+" "+
                                    " join " +
                                    "(select "+sumfiledStr+" from "+table+" group by mainid ) a " +
                                    " on "+modetablename+".id=a.mainid and "+modetablename+"."+pkTo+" in ("+pkvStr+") " +
                                    "set "+setfieldStr ;
                        }else if("sqlserver".equals(rs.getDBType())){
                            sql="update " +modetablename+" "+
                                    " set " +setfieldStr+" "+
                                    " from  (select "+sumfiledStr+" from "+table+" group by mainid ) a " +
                                    " inner join "+modetablename+" t1 on t1.id = a.mainid " +
                                    " where t1."+pkTo+" in ("+pkvStr+")";
                        }
                        rs.executeUpdate(sql);

                    }
                }

            }
            logger.info("6===");
            //如果是订单，计算订单里面的数量和金额，以及更新索引单里面的金额信息
            if("uf_cs_ddgl".equals(modetablename)){
                DFXMModeUtil.calculateOrderNum(sydList);
                DFXMModeUtil.calculateIndexOrderByOrderCode(sydList);
            }
            //如果是索引单则计算下索引单里面的金额字段
            if("uf_sydlb".equals(modetablename)){
                DFXMModeUtil.calculateIndexOrder(sydList);
            }
            logger.info("7===");

            // 创建并启动线程
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 调用自定义的方法
                    try{
                        ModeRightInfo modeRightInfo=new ModeRightInfo();
                        modeRightInfo.setModeId(Integer.parseInt(modeid));
                        RecordSet rs=new RecordSet();
                        for (int i = 0; i < sydList.size(); i++) {
                            rs.executeQuery("select id from "+modetablename+" where "+pkTo+"='"+sydList.get(i)+"'");
                            while(rs.next()){
                                modeRightInfo.editModeDataShare(defaultCreater,Integer.parseInt(modeid),rs.getInt("id"));
                            }
                        }
                    }catch (Exception e){

                    }
                }
            });
            thread.start();
            ret.put("status",true);
            ret.put("msg","接收成功");
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret;
    }

    /**
     * 根据主键更新数据，只做更新，不存在不会创建
     * @author wangkun
     * @date 2024-10-16 14:37
     * @param dataArr
     * @param propJson
     * @return net.sf.json.JSONObject
     */
    public JSONObject updateDataByPK(JSONArray dataArr, JSONObject propJson) {
        JSONObject ret=new JSONObject();
        ret.put("status",true);
        try{
            String modeid=propJson.getString("modeid");
            String modetablename=propJson.getString("modetablename");
            JSONObject primaryKeyField=propJson.getJSONObject("primaryKeyField");
            String pkFrom=primaryKeyField.getString("pkFrom");
            String pkTo=primaryKeyField.getString("pkTo");
            int defaultCreater=propJson.getInt("defaultCreater");
            JSONArray mainData=propJson.getJSONArray("mainData");
            String upsql="update "+modetablename+" set " ;
            for (int j = 0; j <mainData.size() ; j++) {
                JSONObject field=mainData.getJSONObject(j);
                String fromField=field.getString("f");
                String toField=field.containsKey("t")?field.getString("t"):fromField;
                upsql+=toField+"=?,";
            }
            upsql+="modedatamodifier=?,modedatamodifydatetime=? where "+pkTo+"=? ";
            List<List> upList=new ArrayList<>();
            List<String> sydList=new ArrayList<>();
            int upflag=0;
            RecordSet upRs=new RecordSet();
            String msg="";
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data=dataArr.getJSONObject(i);
                String pkFromValue= Util.null2String(data.getString(pkFrom));
                List row=new ArrayList();
                for (int j = 0; j <mainData.size() ; j++) {
                    JSONObject field=mainData.getJSONObject(j);
                    TransUtil.setFieldValue(row,field,data);
                }
                sydList.add(data.getString(pkFrom));
                if(checkHas(modetablename,pkTo,pkFromValue)){
                    //如果存在数据，则更新
                    upflag++;
                    row.add(defaultCreater);
                    row.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    row.add(data.getString(pkFrom));
                    upList.add(row);
                }else{
                    ret.put("status",false);
                    msg+=pkFrom+"("+pkFromValue+")对应的数据不存在,无法处理！";
                }
                if(upflag>1000){
                    boolean isok=upRs.executeBatchSql(upsql,upList);
                    if(!isok){
                        ret.put("msg",upRs.getExceptionMsg());
                        return ret;
                    }
                    upflag=0;
                    upList=new ArrayList<>();
                }
            }
            if(upflag>0){
                boolean isok=upRs.executeBatchSql(upsql,upList);
                if(!isok){
                    ret.put("msg",upRs.getExceptionMsg());
                    return ret;
                }
            }
            // 创建并启动线程
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 调用自定义的方法
                    try{
                        ModeRightInfo modeRightInfo=new ModeRightInfo();
                        modeRightInfo.setModeId(Integer.parseInt(modeid));
                        RecordSet rs=new RecordSet();
                        for (int i = 0; i < sydList.size(); i++) {
                            rs.executeQuery("select id from "+modetablename+" where "+pkTo+"='"+sydList.get(i)+"'");
                            while(rs.next()){
                                modeRightInfo.editModeDataShare(defaultCreater,Integer.parseInt(modeid),rs.getInt("id"));
                            }
                        }
                    }catch (Exception e){
                    }
                }
            });
            thread.start();
            ret.put("msg","接收成功,"+("".equals(msg)?"":msg));
        }catch (Exception e){
            ret.put("msg","接收异常"+e);
        }
        return ret;
    }


    /**
     * 判断数据是否已存在
     * @author wangkun
     * @date 2024-10-16 14:38
     * @param modetablename
     * @param pkTo
     * @param pkValue
     * @return boolean
     */
    private boolean checkHas(String modetablename, String pkTo, String pkValue) {
        String sql="select id from "+ modetablename +" where "+pkTo+" ='"+pkValue+"' ";
        RecordSet rs=new RecordSet();
        return rs.executeQuery(sql)&&rs.next();
    }


    //通用字段转换方法，已迁移至TransUtil类
    //public static void setFieldValue(List list,JSONObject field,String v) {
    //    int transtype=field.containsKey("transtype")?field.getInt("transtype"):0;
    //    //字典表code
    //    String transcode=field.containsKey("transcode")?field.getString("transcode"):"";
    //    //通过字典表转换后的对象字段名
    //    String transname=field.containsKey("transname")?field.getString("transname"):"";
    //    String defaultvalue=field.containsKey("defaultvalue")?field.getString("defaultvalue"):v;
    //    boolean isnumber=field.containsKey("no");
    //    if(isnumber&&"".equals(v)){
    //        list.add(null);
    //        return ;
    //    }
    //    if(transtype==0){
    //        //原值传输，不需转换
    //        list.add(v);
    //    }else if(transtype==1){
    //        //无值时，取默认值并转整数
    //        //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
    //        list.add(Util.getIntValue(v,Integer.parseInt(defaultvalue)));
    //    }else if(transtype==2){
    //        //无值时，取默认值并转浮点数
    //        //model.put("FID",Util.getIntValue(mainMap.get("fid"),0));
    //        list.add(Util.getDoubleValue(v,Double.parseDouble(defaultvalue)));
    //    }else if(transtype==3){
    //        //字典表转换返回指定字段的对象
    //        //｛"transname":"FNumber","transcode":"MK000031","":""｝
    //        //FPOOrderFinance.put("FExchangeTypeId",getDataCodeObj("FNumber","MK000022",Util.null2String(mainMap.get("fexchangetypeid"))));
    //        list.add(getDataCodeObj(transname,transcode,v));
    //    }else if(transtype==4){
    //        //字典表转换直接返回
    //        //model.put("FBusinessType",getDataCode("MK000018",Util.null2String(mainMap.get("fbusinesstype"))));
    //        list.add(getDataCode(transcode,v));
    //    }else if(transtype==5){
    //        //不需通过字典表转换，直接封装为指定字段名的对象返回
    //        //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
    //        list.add(getDataCodeObj(transname,v));
    //    }else if(transtype==6){
    //        //在字段值后边拼接固定内容
    //        //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
    //        list.add(v+defaultvalue);
    //    }else if(transtype==7){
    //        //在字段值前边拼接固定内容
    //        //model.put("F_NBDF_CGY",getDataCodeObj("FSTAFFNUMBER",Util.null2String(mainMap.get("fpurchaserid"))));
    //        list.add(defaultvalue+v);
    //    }else if(transtype==8){
    //        //取默认值并转boolean
    //        //model.put("F_NBDF_CGY",false));
    //        list.add(Boolean.parseBoolean(defaultvalue));
    //    }else if(transtype==9){
    //        //直接设置字段为默认值，且默认值是字符串
    //        //model.put("F_NBDF_CGY","1111"));
    //        list.add(defaultvalue);
    //    }else if(transtype==10){
    //        //根据字段值判断，转换为boolean值传递
    //        JSONObject selectObj= JSONObject.fromObject(defaultvalue);
    //        list.add(selectObj.containsKey(v)?selectObj.getBoolean(v):false);
    //    }else if(transtype==11){
    //        //根据字典表做值转换为boolean
    //        list.add(Boolean.parseBoolean(getDataCode(transcode,v)));
    //    }else if(transtype==12){
    //        //根据转换sql做值转换
    //        list.add(getTransSqlValue(defaultvalue,v));
    //    }else if(transtype==13){
    //        //根据转换方法做值转换
    //        list.add(getTransMethodValue(defaultvalue,v));
    //    }else if(transtype==14||transtype==15||transtype==16||transtype==17||transtype==18||transtype==20||transtype==21||transtype==22||transtype==26){
    //        //14 时间戳yyyy-MM-dd HH:mm:ss
    //        //15 时间戳yyyy-MM-dd HH:mm
    //        //16 时间戳yyyy-MM-dd
    //        //17 秒级时间戳
    //        //18 毫秒级时间戳
    //        //20 时间戳 自定义
    //        //21 时间戳 HH:mm
    //        //22 时间戳 HH:mm:ss
    //        list.add(getTimeStameByString(transtype,defaultvalue));
    //    }else if(transtype>25&&transtype<34){
    //        //将字符串转为日期后再转换为指定类型的日期字符串
    //        //26 时间戳yyyy-MM-dd HH:mm:ss
    //        //27 时间戳yyyy-MM-dd HH:mm
    //        //28 时间戳yyyy-MM-dd
    //        //29 秒级时间戳
    //        //30 毫秒级时间戳
    //        //31 时间戳 自定义
    //        //32 时间戳 HH:mm
    //        //33 时间戳 HH:mm:ss
    //        String of=field.getString("of");
    //        list.add(getTimeStameByString(transtype,defaultvalue,v,of));
    //    }else if(transtype==19){
    //        //根据数据源做sql转换
    //        list.add(getTransSqlValueByDataSource(defaultvalue,v,field.getString("sourcecode")));
    //    }else if(transtype==23){
    //        //根据编号转人员id
    //        list.add(getUserIdByWorkcode(v,defaultvalue));
    //    }else if(transtype==24){
    //        //根据编号转部门id
    //        list.add(getDeptIdByDeptCode(v,defaultvalue));
    //    }else if(transtype==25){
    //        //根据编号转分部id
    //        list.add(getCompanyIdByCompanyCode(v,defaultvalue));
    //    }else if(transtype==34){
    //        //截取指定位数的字符串
    //        list.add(v.substring(0,Integer.parseInt(defaultvalue)));
    //    }else if(transtype==35){
    //        //截取指定位数的字符串
    //        int substart=Integer.parseInt(field.getString("substart"));
    //        list.add(v.substring(substart,Integer.parseInt(defaultvalue)));
    //    }else if(transtype==36){
    //        //替换字符串
    //        String repfrom=field.getString("repfrom");
    //        String repto=field.getString("repto");
    //        list.add(v.replace(repfrom,repto));
    //    }else if(transtype==37){
    //        //替换字符串
    //        String repfrom=field.getString("repfrom");
    //        String repto=field.getString("repto");
    //        list.add(v.replaceAll(repfrom,repto));
    //    }else if(transtype==38){
    //
    //    }else{
    //        list.add(v);
    //    }
    //}

    //private static Object getTimeStameByString(int transtype, String myformtStr, String v,String oldformatStr) {
    //    SimpleDateFormat dateFormat = new SimpleDateFormat(oldformatStr);
    //    Date date = null;
    //    try {
    //        date = dateFormat.parse(v);
    //        System.out.println(date);
    //        String formatStr;
    //        switch (transtype){
    //            case 26:
    //                formatStr="yyyy-MM-dd HH:mm:ss";
    //                break;
    //            case 27:
    //                formatStr="yyyy-MM-dd HH:mm";
    //                break;
    //            case 28:
    //                formatStr="yyyy-MM-dd";
    //                break;
    //            case 29:
    //                String timestamp = String.valueOf(date.getTime()/1000);
    //                return Integer.valueOf(timestamp)+"";
    //            case 30:
    //                return System.currentTimeMillis()+"";
    //            case 31:
    //                formatStr=myformtStr;
    //                break;
    //            case 32:
    //                formatStr="HH:mm";
    //                break;
    //            case 33:
    //                formatStr="HH:mm:ss";
    //                break;
    //            default:
    //                formatStr="yyyy-MM-dd HH:mm:ss";
    //                break;
    //        }
    //        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
    //        return sdf.format(date);
    //    } catch (ParseException e) {
    //        return v;
    //    }
    //}
    //
    //private static String getUserIdByWorkcode(String v,String defaultvalue) {
    //    String sql="select id from hrmresource where workcode='"+v+"'";
    //    RecordSet rs=new RecordSet();
    //    return (rs.executeQuery(sql)&&rs.next())? rs.getString("id"):"0";
    //}
    //
    //
    //private static String getDeptIdByDeptCode(String v,String defaultvalue) {
    //    String sql="select id from hrmdepartment where departmentcode='"+v+"'";
    //    RecordSet rs=new RecordSet();
    //    return (rs.executeQuery(sql)&&rs.next())? rs.getString("id"):("".equals(defaultvalue)?"0":defaultvalue);
    //}
    //
    //private static String getCompanyIdByCompanyCode(String v,String defaultvalue) {
    //    String sql="select id from hrmsubcompany where subcompanycode='"+v+"'";
    //    RecordSet rs=new RecordSet();
    //    return (rs.executeQuery(sql)&&rs.next())? rs.getString("id"):("".equals(defaultvalue)?"0":defaultvalue);
    //}

    ///**
    // * 根据转换编码、字段值和，转换后，封装为指定key名字的json对象返回
    // * @author wangkun
    // * @date 2023-10-20 17:24
    // * @param fieldname
    // * @param code
    // * @param v
    // * @return com.alibaba.fastjson.JSONObject
    // */
    //public static com.alibaba.fastjson.JSONObject getDataCodeObj(String fieldname, String code , String v) {
    //    String sql="select zdbm from uf_DataWarehouse t left join uf_DataWarehouse_dt1 t1 on t.id=t1.mainid where t.wybm='"+code+"' and t1.oadyz='"+v+"'";
    //    RecordSet rs=new RecordSet();
    //    String fnumber= rs.executeQuery(sql)&&rs.next()?rs.getString("zdbm"):"";
    //    com.alibaba.fastjson.JSONObject ret=new com.alibaba.fastjson.JSONObject();
    //    ret.put(fieldname,fnumber);
    //    return ret;
    //}
    //
    ///**
    // * 将字段值封装为指定key名字的json对象返回
    // * @author wangkun
    // * @date 2023-10-20 17:23
    // * @param fieldname
    // * @param v
    // * @return com.alibaba.fastjson.JSONObject
    // */
    //public static com.alibaba.fastjson.JSONObject getDataCodeObj(String fieldname, String v) {
    //    com.alibaba.fastjson.JSONObject ret=new com.alibaba.fastjson.JSONObject();
    //    if(!"".equals(fieldname)){
    //        ret.put(fieldname,v);
    //    }
    //    return ret;
    //}
    //
    ///**
    // * 根据转换编码code和字段值，转换后返回值
    // * @author wangkun
    // * @date 2023-10-20 17:22
    // * @param code
    // * @param v
    // * @return java.lang.String
    // */
    //public static String getDataCode(String code ,String v) {
    //    String sql="select zdbm from uf_DataWarehouse t left join uf_DataWarehouse_dt1 t1 on t.id=t1.mainid where t.wybm='"+code+"' and t1.oadyz='"+v+"'";
    //    RecordSet rs=new RecordSet();
    //    return rs.executeQuery(sql)&&rs.next()?rs.getString("zdbm"):"";
    //}
    //
    ///**
    // * 获取到转换sql的查询结果，将{currentvalue}占位字符串替换为当前字段值
    // * @author wangkun
    // * @date 2022/1/15 20:31
    // * @param zhsql
    // * @param lybzdvalue
    // * @return java.lang.String
    // */
    //public static String getTransSqlValue(String zhsql, String lybzdvalue) {
    //    try {
    //        if(zhsql.indexOf("{currentvalue}")>-1){
    //            zhsql=zhsql.replaceAll("\\{currentvalue}",lybzdvalue);
    //        }
    //        RecordSet rs=new RecordSet();
    //        rs.executeQuery(zhsql);
    //        if(rs.next()){
    //            return rs.getString(1);
    //        }
    //        return lybzdvalue;
    //    }catch (Exception e){
    //        return lybzdvalue;
    //    }
    //}
    //
    ///**
    // * 获取到转换sql的查询结果，将{currentvalue}占位字符串替换为当前字段值
    // * @author wangkun
    // * @date 2022/1/15 20:31
    // * @param zhsql
    // * @param lybzdvalue
    // * @return java.lang.String
    // */
    //public static String getTransSqlValueByDataSource(String zhsql, String lybzdvalue,String sourcecode) {
    //    try {
    //        if(zhsql.indexOf("{currentvalue}")>-1){
    //            zhsql=zhsql.replaceAll("\\{currentvalue}",lybzdvalue);
    //        }
    //        RecordSetDataSource rs=new RecordSetDataSource(sourcecode);
    //        rs.execute(zhsql);
    //        if(rs.next()){
    //            return rs.getString(1);
    //        }
    //        return lybzdvalue;
    //    }catch (Exception e){
    //        return lybzdvalue;
    //    }
    //}
    //
    ///**
    // * 获取到转换类的查询结果值，通过反射调用方法，将当前字段值传入方法获取方法返回值
    // * @author wangkun
    // * @date 2022/1/15 20:32
    // * @param zhff
    // * @param lybzd
    // * @return java.lang.String
    // */
    //public static String getTransMethodValue(String zhff, String lybzd) {
    //    try {
    //        // 注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
    //        String[] zhffArr=zhff.split("#");
    //        String classPath = zhffArr[0];
    //        Class stuClass = Class.forName(classPath);
    //        Object obj = stuClass.newInstance();
    //        Method md = stuClass.getDeclaredMethod(zhffArr[1], String.class);
    //        return (String) md.invoke(obj, lybzd);
    //    } catch (Exception e) {
    //        return lybzd;
    //    }
    //}

    ///**
    // * 根据类型值获取不同时间戳类型
    // * @author wangkun
    // * @date 2022/1/15 20:34
    // * @param transtype
    // * @param myformtStr
    // * @return java.lang.String
    // */
    //public static String getTimeStameByString(int transtype, String myformtStr) {
    //    Date date = new Date();
    //    String formatStr;
    //    switch (transtype){
    //        case 14:
    //            formatStr="yyyy-MM-dd HH:mm:ss";
    //            break;
    //        case 15:
    //            formatStr="yyyy-MM-dd HH:mm";
    //            break;
    //        case 16:
    //            formatStr="yyyy-MM-dd";
    //            break;
    //        case 17:
    //            String timestamp = String.valueOf(date.getTime()/1000);
    //            return Integer.valueOf(timestamp)+"";
    //        case 18:
    //            return System.currentTimeMillis()+"";
    //        case 20:
    //            formatStr=myformtStr;
    //            break;
    //        case 21:
    //            formatStr="HH:mm";
    //            break;
    //        case 22:
    //            formatStr="HH:mm:ss";
    //            break;
    //        default:
    //            formatStr="yyyy-MM-dd HH:mm:ss";
    //            break;
    //    }
    //    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
    //    return sdf.format(date);
    //}


}
