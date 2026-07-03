package com.engine.dfxm.cmd.fhd;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
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

/**
 * 检查卡结果接口
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class AcceptCheckCardCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public AcceptCheckCardCmd(Map<String, Object> params, User user) {
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
        Map<String, Object> reMap360=doSend360("360JCK",ids,type,doMsg+"(360)");
        return reMap360;
    }

    /**
     * 检查卡数据接口
     * @author wangkun
     * @date 2024-10-16 15:48
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
            JSONArray mainData=pmObj.getJSONArray("mainData");
            if (rs.next()){
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
            }
        }catch (Exception e){
            reMap.put("status",false);
            reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
        }
        return reMap;
    }
}
