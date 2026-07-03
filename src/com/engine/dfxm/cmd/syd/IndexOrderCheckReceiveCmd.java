package com.engine.dfxm.cmd.syd;

import cn.hutool.core.date.DateUtil;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.dfxm.manager.PropertiesManager;
import com.engine.dfxm.webservice.FormModeServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 索引单发票校验反馈接口
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class IndexOrderCheckReceiveCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();
    private String paramsStr="";

    public IndexOrderCheckReceiveCmd(Map<String, Object> paramsMap, User user,String params) {
        this.user = user;
        this.params = paramsMap;
        this.paramsStr=params;
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
        reMap.put("status",false);
        try {
            JSONObject reqObj=JSONObject.fromObject(paramsStr);
            PropertiesManager pm= new PropertiesManager("indexOrderCheck",1);
            JSONObject propJson=pm.getFieldValueObj();
            JSONArray dataArr=new JSONArray();
            if(propJson.containsKey("dataFrom")){
                dataArr=reqObj.getJSONArray(propJson.getString("dataFrom"));
            }else{
                reMap.put("msg","接收异常,数据不存在"+paramsStr);
                return  reMap;
            }
            FormModeServiceImpl formModeService=new FormModeServiceImpl();
            if(propJson.containsKey("checkPK")&&propJson.getBoolean("checkPK")){
                if(propJson.containsKey("dataType") && propJson.getString("dataType").indexOf("update")>-1){
                    reMap=formModeService.updateDataByPK(dataArr,propJson);
                }else{
                    reMap=formModeService.saveDataByPK(dataArr,propJson);
                }
            }else{
                reMap=formModeService.saveData(dataArr,propJson);
            }
        }catch (Exception e){
            reMap.put("msg","接收异常"+e);
        }
        boolean status=(boolean)reMap.get("status");
        reMap.put("status",status?"true":"false");
        return reMap;
    }
}
