package com.api.customization.hanjuntest.service.impl;

import com.weaverboot.frame.ioc.anno.classAnno.WeaIocReplaceComponent;
import com.weaverboot.frame.ioc.anno.methodAnno.WeaReplaceAfter;
import com.weaverboot.frame.ioc.anno.methodAnno.WeaReplaceBefore;
import com.weaverboot.frame.ioc.handler.replace.weaReplaceParam.impl.WeaAfterReplaceParam;
import com.weaverboot.frame.ioc.handler.replace.weaReplaceParam.impl.WeaBeforeReplaceParam;
import com.weaverboot.tools.logTools.LogTools;
@WeaIocReplaceComponent
public class Test {
    @WeaReplaceBefore(value = "/api/workflow/reqlist/splitPageKey",order = 1,description = "测试拦截前置")
    public void beforeTest(WeaBeforeReplaceParam weaBeforeReplaceParam){
        //一顿操作
        LogTools.info("223222");
    }
    @WeaReplaceAfter(value = "/api/workflow/reqlist/splitPageKey",order = 1,description = "测试拦截后置")
    public String after(WeaAfterReplaceParam weaAfterReplaceParam){
        String data = weaAfterReplaceParam.getData();//这个就是接口执行完的报文
        LogTools.info("after:/api/workflow/reqlist/splitPageKey");
        LogTools.info(data);
        System.out.println("22222222222");
        return data;
    }
}