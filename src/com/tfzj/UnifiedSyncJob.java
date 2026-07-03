package com.tfzj;

import java.util.ArrayList;
import java.util.List;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 统一待办定时同步任务
 * @author feng
 *
 */
public class UnifiedSyncJob extends BaseCronJob{

	@Override
	public void execute() {
		BaseBean baseBean=new BaseBean();
		baseBean.writeLog("[UnifiedSyncJob] 统一代办定时同步任务开始");
		
		UnifiedTodoManager todoManager=new UnifiedTodoManager();
		todoManager.doPushAll();
		
		baseBean.writeLog("[UnifiedSyncJob] 统一代办定时同步任务结束");
	}
	
}

