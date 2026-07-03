package com.engine.dfxm.cmd.fhd;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wangkun
 * @date 2023-11-15 19:06
*/
public class ExportFHMXCmd extends AbstractCommonCommand<Map<String, Object>> {
    private Logger log= LoggerFactory.getLogger();
    public ExportFHMXCmd(Map<String, Object> params, User user) {
        this.user=user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        return reMap;
    }
}
