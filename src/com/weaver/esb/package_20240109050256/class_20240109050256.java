package com.weaver.esb.package_20240109050256;

import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;

import java.util.*;

public class class_20240109050256 {

	/**
	 * @param:  param(Map collections)
	 * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
	 */
	public Map execute(Map<String,Object> params) {
		// 示例：data：定义的请求数据，code:定义的响应数据
		 String requestid = (String) params.get("requestid");
		 Map<String,String> ret = new HashMap<>();
		//事件标识
		String eventKey = "updateCgxqBgsj";
		//事件请求参数
		String param = "{\"requestid\":\"" + requestid + "\"}";

		//EsbService其他方法及说明见ESB API接口说明文档
		//获取 ESB 服务
		EsbService service = EsbClient.getService();
		String result = service.execute(eventKey, param);
		ret.put("result",result);
		 ret.put("code","1");
		 return ret;

	}
}