package com.engine.dfxm.cmd.dd;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.conn.RecordSet;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单校验，
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class CheckPoOrderCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public CheckPoOrderCmd(Map<String, Object> params, User user) {
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
        Map<String, Object> reMap=checkData(ids,type,doMsg+"(360)");
        return reMap;
    }

    private Map<String, Object> checkData(String ids,String type,String doMsg) {
        Map<String, Object> reMap=new HashMap<>();
        RecordSet rs=new RecordSet();
        boolean status=true;
        reMap.put("status",status);
        String msg="";
        String sql="select * from uf_cs_ddgl t left join  uf_cs_ddgl_dt1 t1 on t.id=t1.mainid where t.id='"+ids+"' and  t.SECOND_ORDER_TAG='1' and  t1.PA_ORDER_CODE='' ";
        rs.executeQuery(sql);
        if(rs.next()){
            status=false;
            msg+= rs.getString("ORDER_CODE")+",上级订单还未生成，不允许发货";
            reMap.put("status",status);
            reMap.put("msg",doMsg+"("+msg.substring(0,msg.length()-1)+")无法确认，只有未确认过的订单才允许确认，请重新选择！");
        }
        if(!status){
            reMap.put("status",status);
            reMap.put("msg",doMsg+"("+msg.substring(0,msg.length()-1)+")无法确认，只有未确认过的订单才允许确认，请重新选择！");
        }
        return reMap;
    }
}
