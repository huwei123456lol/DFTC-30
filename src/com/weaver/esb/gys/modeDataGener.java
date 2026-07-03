package com.weaver.esb.gys;

import com.alibaba.fastjson.JSONObject;
import com.api.caigou.init.CaigouInitRoles;
import com.engine.esb.bean.transform.EsbTransformBean;
import com.icbc.api.internal.apache.http.O;
import com.icbc.api.internal.apache.http.impl.cookie.R;
import com.icbc.api.internal.apache.http.impl.cookie.S;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.sql.Timestamp;

import java.util.*;

/**
 * @author Hanjun
 * @date 2023/7/19
 * @apiNote
 */
public class modeDataGener {

	public Map execute(Map<String, Object> params) {
		// 示例：data：定义的请求数据，code:定义的响应数据
		String dataids = String.valueOf(params.get("dataids"));
		String templateID = String.valueOf(params.get("templateID"));
		String operator = String.valueOf(params.get("operator"));
		// ……
		Map<String, String> ret = new HashMap<>();
		List<Map<String, Object>> datas = new ArrayList<>();

		//获取配置主表
		RecordSet rs = new RecordSet();
		String mainTable = null;
		rs.execute("select * from uf_cosdatasync where id = " + templateID);
		if (rs.next()){
			mainTable = rs.getString("zbmc");
		}

		//处理主表信息
		List<Map<String,String>> mainConfig = getConfig(templateID,"");
		rs.execute("select id from " + mainTable + " where id in (" + dataids + ")");

		while (rs.next()){

			Map<String,Object> data = new HashMap<>();
			String dataid = rs.getString("id");
			//处理操作者信息 默认系统管理员
			Map<String,String> operationinfo = new HashMap<>();
			operationinfo.put("operator", operator);
			data.put("operationinfo",operationinfo);

			//处理主表信息
			Map<String,Object> mainTableInfo = getData(mainConfig,mainTable,dataid).get(0);
			data.put("mainTable",mainTableInfo);


			//处理明细表信息
			RecordSet rsdt = new RecordSet();
			rsdt.execute("select mxbxh,ysmxbbm from uf_cosdatasync_dt1 where zbmxb = 1 and  mainid = "+ templateID);
			while (rsdt.next()){
				String tablename = rsdt.getString("ysmxbbm");
				String mxbxh = rsdt.getString("mxbxh");

				//获取明细数据
				List<Map<String,Object>> dtInfo = getData(getConfig(templateID,tablename),tablename,dataid);
				if(dtInfo.size() > 0){
					data.put("detail"+mxbxh,dtInfo);
				}


			}
			datas.add(data);

		}

		ret.put("data",JSONObject.toJSONString(datas));

		//提取模版配置

		ret.put("code", "1");
		return ret;

	}

	private List<Map<String,String>> getConfig(String templateId,String tablename){
		List<Map<String,String>> result = new ArrayList<>();

		RecordSet rs = new RecordSet();
		rs.execute("select * from uf_cosdatasync_dt2 where ifnull(szb,'') = '" + tablename + "' and mainid = " + templateId);
		while (rs.next()){

			Map<String,String> filedMap = new HashMap<>();

			filedMap.put("cossjkzdm",rs.getString("cossjkzdm"));
			filedMap.put("sjkzdm",rs.getString("sjkzdm"));
			filedMap.put("coszdlx",rs.getString("coszdlx"));

			result.add(filedMap);
		}

		return result;
	}

	private List<Map<String,Object>> getData(List<Map<String,String>> configTemplate ,String tablename, String dataid){
		List<Map<String,Object>> result = new ArrayList<>();
		boolean dt = tablename.contains("_dt");
		String sqlWhere = dt ? "mainid = " + dataid : "id = " + dataid ;
		RecordSet rs = new RecordSet();
		rs.execute("select * from " + tablename + " where " + sqlWhere);
		while (rs.next()){
			Map<String,Object> fieldValue = new HashMap<>();
			for (Map<String, String> stringStringMap : configTemplate) {
				String cosFileName = stringStringMap.get("cossjkzdm");
				String sjkzd  = stringStringMap.get("sjkzdm");
				String zdlx = stringStringMap.get("coszdlx");
				String value = rs.getString(sjkzd);
				if(!"".equals(value) && value != null){
					value = "6".equals(zdlx) ? JSONObject.toJSONString(attachmentUrl(value)) : value ;
					fieldValue.put(cosFileName,value);
				}
			}

			if(dt){
				Map<String,Object> dtInfo = new HashMap<>();
				Map<String,String> operate = new HashMap<>();
				operate.put("action","SaveOrUpdate");
				dtInfo.put("operate",operate);
				dtInfo.put("data",fieldValue);
				result.add(dtInfo);
			}else{
				result.add(fieldValue);
			}
		}

		return result;

	}


	private List<Map<String, String>> attachmentUrl(String docId) {
		List<Map<String, String>> result = new ArrayList<>();


		//todo ..
		RecordSet rs = new RecordSet();
		rs.execute("select concat('http://10.4.9.189:8080/api/getJQTGysInfo/getfile?docid=',a.id) content,d.IMAGEFILENAME name from docdetail a left join docimagefile d on a.id = d.DOCID where a.id in (" + docId + ")");
		while(rs.next()){
			Map<String,String> tempMap = new HashMap<>();
			tempMap.put("content",rs.getString("content"));
			tempMap.put("name",rs.getString("name"));
			result.add(tempMap);
		}



		return result;
	}

	public static String getCurrentTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(11, 13) + ":" + (timestamp.toString()).substring(14, 16) + ":"
				+ (timestamp.toString()).substring(17, 19);
	}

	public static String getCurrentDate() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-"
				+ (timestamp.toString()).substring(8, 10);
	}


}
