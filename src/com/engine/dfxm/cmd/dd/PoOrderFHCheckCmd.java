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
 * 二级订单发货校验
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class PoOrderFHCheckCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();

    public PoOrderFHCheckCmd(Map<String, Object> params, User user) {
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
        String doMsg = (String) params.get("doMsg");
        Map<String, Object> reMap360=doSend360("360FHCK",ids,"",doMsg+"(360)");
        return reMap360;
    }

    private Map<String, Object> doSend360(String operateCode, String ids,String type,String doMsg) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        String sql="select id from uf_cs_ddgl_dt1 where mainid='"+ids+"' and (PA_ORDER_CODE is null or PA_ORDER_CODE='' )";
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql);
        reMap.put("status",true);
        if(rs.next()){
            reMap.put("status",false);
            reMap.put("msg","当前订单是二级订单，且存在未关联一级订单的零件行，不允许创建发货单");
        }
        return reMap;
    }


}
