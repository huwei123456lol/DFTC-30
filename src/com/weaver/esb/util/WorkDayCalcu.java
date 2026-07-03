package com.weaver.esb.util;

import com.engine.common.service.impl.HrmCommonServiceImpl;
import com.engine.workflow.biz.newRule.function.CheckIsHoliday;
import com.weaver.general.TimeUtil;
import weaver.blog.WorkDayDao;
import weaver.general.BaseBean;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanjun
 * @date 2024/1/11
 * @apiNote
 */
public class WorkDayCalcu {

	/**
	 *  接口请求数据（可获取接口不同类别的数据）
	 *  优先级：url>request>header (request参数中可以获取到 URL 类别的参数，获取不到 header 类别的参数)
	 *  contextParams.get("url") ：URL地址参数
	 *  contextParams.get("request") ：表单参数
	 *  contextParams.get("header") ：Header参数
	 *
	 */
	private Map<String,Map<String,String>> contextParams = new HashMap<>();

	/**
	 * 上下文数据（可获取转换规则映射位置的其它同级参数）
	 * allParams.get("a")：获取同级别名为a的参数值
	 */
	private Map<String,String> allParams = new HashMap<>();

	/**
	 * @param:  param Map collections
	 * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
	 */
	public String execute(Map<String,String> params) {

		// 示例：data：转换规则定义的请求参数
		String dataStart = params.get("date");
		String userid = params.get("userid");
		int day = Integer.parseInt(params.get("day"));
		HrmCommonServiceImpl hrmCommonService = new HrmCommonServiceImpl();
		String dateCourser = dataStart;
		int i = day;
		int safeCourse = 99;
		while (i >= 0 && safeCourse > 0) {
			dateCourser = TimeUtil.dateAdd(dateCourser,1);
			new BaseBean().writeLog("WorkDayCalcu >>  " + dateCourser);
			if(hrmCommonService.isWorkDay(userid,dateCourser)){
				new BaseBean().writeLog("WorkDayCalcu >> is workday  " + hrmCommonService.isWorkDay(userid,dateCourser)  );
				i--;
			}
			safeCourse--;
		}
		return dateCourser;
	}
}
