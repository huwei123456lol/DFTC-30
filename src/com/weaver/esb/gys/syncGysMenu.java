package com.weaver.esb.gys;

import com.api.caigou.init.CaigouInitRoles;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanjun
 * @date 2023/7/10
 * @apiNote
 */
public class syncGysMenu {

	public Map execute(Map<String,Object> params) {
		// 示例：data：定义的请求数据，code:定义的响应数据
		String subid = String.valueOf(params.get("subid"));
		String templateName = String.valueOf(params.get("templateName"));
		// ……
		Map<String,String> ret = new HashMap<>();

		//todo..
		new CaigouInitRoles().synchronizeMenu(subid + "", templateName);

		ret.put("code","1");
		return ret;

	}
}
