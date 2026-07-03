package com.engine.dfxm.cmd.fhd;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.dfxm.manager.PropertiesManager;
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
import java.util.UUID;

/**
 * 获取订单可发货数据明细
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class FHPoOrderDataCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public FHPoOrderDataCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }


    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            PropertiesManager pm=new PropertiesManager("360FHPoOrder",1);
            JSONObject pmObj=pm.getFieldValueObj();
            JSONArray detailData=pmObj.getJSONArray("detailData");
            String modetablename=pmObj.getString("modetablename");
            JSONObject dt1FieldObj=detailData.getJSONObject(0);
            JSONArray dt1FieldArr=dt1FieldObj.getJSONArray("dtFieldData");
            //JSONObject dt2FieldObj=detailData.getJSONObject(1);
            //JSONArray dt2FieldArr=dt2FieldObj.getJSONArray("dtFieldData");
            log.info("pmObj="+pmObj+"===========");

            String ORDER_CODE = (String) params.get("ORDER_CODE");
            String dtids = (String) params.get("DTIDS");

            JSONObject ret=new JSONObject();
            JSONArray dt1=new JSONArray();
            JSONArray dt2=new JSONArray();

            boolean hasData=false;
            String hasprocess_control="0";
            int hh=0;
            String sql="select t1.* from "+modetablename+" t left join "+modetablename+"_dt1 t1 on t.id=t1.mainid " +
                    "where t.ORDER_CODE='"+ORDER_CODE+"' and t1.id in("+dtids+") and t1.dfhsl>0";
            RecordSet rs=new RecordSet();
            log.info("获取订单发货数据sql"+sql);
            rs.executeQuery(sql);
            while(rs.next()){
                //是否追溯件
                String traceable_parts=rs.getString("traceable_parts");
                //重点管控-待确认 追溯件的检查卡如何创建，多条还是1条
                //疑问：追溯件，物料A 数量10，发货生成的10条明细，那检查卡填10条还是填1条:
                String process_control=rs.getString("process_control");

                if("1".equals(process_control)){
                    hasprocess_control=process_control;
                }
                String ddmxStr="{value:\""+rs.getString("id")+"\"," +
                        "specialobj:[{id:\""+rs.getString("id")+"\",name:\""+rs.getString("id")+"\"}]}";
                log.info("ddmxStr="+ddmxStr+"==========="+rs.getString("dfhsl"));
                double dfhsla=Util.getDoubleValue(rs.getString("dfhsl"),0d);
                if("1".equals(traceable_parts)){
                    int dfhsl= (int) Math.floor(dfhsla);
                    log.info("dfhsl="+dfhsl+"===========");
                    for (int i = 0; i < dfhsl; i++) {
                        hh+=10;
                        JSONObject row=new JSONObject();
                        row.put("ddmx",ddmxStr);
                        row.put("DELIVERY_LINECODE",hh);
                        row.put("ZSJ_ICODE", UUID.randomUUID().toString());
                        row.put("DELIVERY_FROM", "COS");
                        row.put("DELIVERY_STATE", "20");
                        row.put("DELIVERY_BATCH", TransUtil.getTimeStameByString(20,"yyyyMMdd"));
                        log.info("row111="+row+"===========");
                        for (int j = 0; j < dt1FieldArr.size(); j++) {
                            JSONObject field=dt1FieldArr.getJSONObject(j);
                            if("sfsl".equals(field.getString("t"))){
                                row.put(field.getString("t"),1);
                            }else{
                                row.put(field.getString("t"),rs.getString(field.getString("f")));
                            }
                            log.info("row=1"+row+"row1");
                        }
                        dt1.add(row);
                        //if("1".equals(process_control)){
                        //    //重点管控件
                        //    JSONObject row1=new JSONObject();
                        //    for (int j = 0; j < dt2FieldArr.size(); j++) {
                        //        JSONObject field=dt2FieldArr.getJSONObject(j);
                        //        row1.put(field.getString("t"),rs.getString(field.getString("f")));
                        //    }
                        //    dt2.add(row1);
                        //}
                    }
                    if((dfhsla-dfhsl)>0d){
                        hh+=10;
                        JSONObject row=new JSONObject();
                        row.put("ddmx",ddmxStr);
                        row.put("DELIVERY_LINECODE",hh);
                        row.put("ZSJ_ICODE", UUID.randomUUID().toString());
                        row.put("DELIVERY_FROM", "COS");
                        row.put("DELIVERY_STATE", "20");
                        row.put("DELIVERY_BATCH", TransUtil.getTimeStameByString(20,"yyyyMMdd"));
                        log.info("row111="+row+"===========");
                        for (int j = 0; j < dt1FieldArr.size(); j++) {
                            JSONObject field=dt1FieldArr.getJSONObject(j);
                            if("sfsl".equals(field.getString("t"))){
                                row.put(field.getString("t"),dfhsla-dfhsl);
                            }else{
                                row.put(field.getString("t"),rs.getString(field.getString("f")));
                            }
                            log.info("row=1"+row+"row1");
                        }
                        dt1.add(row);
                    }
                }else{
                    hh+=10;
                    JSONObject row=new JSONObject();
                    row.put("ddmx",ddmxStr);
                    row.put("DELIVERY_LINECODE",hh);
                    row.put("ZSJ_ICODE", "");
                    row.put("DELIVERY_FROM", "COS");
                    row.put("DELIVERY_STATE", "20");
                    row.put("DELIVERY_BATCH", TransUtil.getTimeStameByString(20,"yyyyMMdd"));
                    log.info("dfhsl="+dt1FieldArr+"===========");
                    for (int j = 0; j < dt1FieldArr.size(); j++) {
                        JSONObject field=dt1FieldArr.getJSONObject(j);
                        row.put(field.getString("t"),rs.getString(field.getString("f")));
                        log.info("row="+row+"row");
                    }
                    log.info("row222="+row+"===========");
                    dt1.add(row);

                    //if("1".equals(process_control)){
                    //    //重点管控件
                    //    JSONObject row1=new JSONObject();
                    //    for (int j = 0; j < dt2FieldArr.size(); j++) {
                    //        JSONObject field=dt2FieldArr.getJSONObject(j);
                    //        row1.put(field.getString("t"),rs.getString(field.getString("f")));
                    //    }
                    //    dt2.add(row1);
                    //}
                }
                hasData=true;
            }
            ret.put("dt1",dt1);
            ret.put("dt2",dt2);
            ret.put("process_control",hasprocess_control);
            ret.put("hasData",hasData);
            reMap.put("data",ret);
            reMap.put("status",hasData);
            if(hasData){
                reMap.put("msg","获取成功");
            }else{
                reMap.put("msg","当前订单已全部发货无未发货数据，不允许重复发货");
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg","获取订单发货信息异常"+e);
            log.info("获取订单发货信息异常="+e+"===========");
        }
        return reMap;
    }
}
