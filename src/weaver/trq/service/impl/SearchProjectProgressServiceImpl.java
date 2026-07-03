package weaver.trq.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.trq.service.SearchProjectProgressService;

/**
 * 查询项目进度的对外接口
 * 
 * @author Alex.Du
 * 
 */
public class SearchProjectProgressServiceImpl implements
		SearchProjectProgressService {

	public String searchProjectProgress(String projectCode) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();

		Map<String, String> resultMap = new HashMap<String, String>();

		log.writeLog("进入查询项目进度的对外接口");

		try {
			// 通过项目编号查询项目名称、安装类型、地区、项目安装地址、申请报装渠道、客户姓名、客户电话
			String xmmc = null;
			String azlx = null;
			String dq = null;
			String xmazdz = null;
			String sqbzqd = null;
			String khxm = null;
			String khdh = null;

			
			rs.execute("select xmmc,azlx,dq,azdz,sqqd,khxm,khdh from uf_xm where xmbh='"
					+ filter(projectCode)+ "'");
			if (rs.next()) {
				xmmc = rs.getString("xmmc");
				azlx = rs.getString("azlx");
				dq = rs.getString("dq");
				xmazdz = rs.getString("azdz");
				sqbzqd = rs.getString("sqqd");
				khxm = rs.getString("khxm");
				khdh = rs.getString("khdh");
			}else{
				resultMap.put("status", "2");
				resultMap.put("message", "失败，无法查找到此项目编号的项目信息");
			}

			// 在bzmk_w视图中查询当前项目的状态
			rs.execute("select bz,tk,ht,dh from bzmk_w where xmbh='"
					+ filter(projectCode)+ "'");
			if (rs.next()) {
				if (rs.getString("dh").trim().equals("已完成")) {
					resultMap.put("projectProgress", "3");
				} else if (rs.getString("ht").trim().equals("已完成")) {
					resultMap.put("projectProgress", "2");
				} else if (rs.getString("tk").trim().equals("已完成")) {
					resultMap.put("projectProgress", "1");
				} else {
					resultMap.put("projectProgress", "0");
				}
				
				// 封装返回参数
				resultMap.put("status", "1");
				resultMap.put("message", "成功");
				resultMap.put("projectName", xmmc);
				resultMap.put("installType", azlx);
				resultMap.put("region", dq);
				resultMap.put("installAdd", xmazdz);
				resultMap.put("applyMethod", sqbzqd);
				resultMap.put("customerName", khxm);
				resultMap.put("customerPhone", khdh);
			}else{
				resultMap.put("status", "2");
				resultMap.put("message", "失败，无法查找到此项目编号的项目进度");
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "2");
			resultMap.put("message", "失败，出现Exception:" + e.getMessage());
		}

		JSONObject jsonObject = new JSONObject(resultMap);
		log.writeLog("查询项目进度的对外接口执行完毕");
		log = null;
		rs = null;
		return jsonObject.toString();
	}


	public final static String regex = "'|and|exec|execute|insert|select|delete|update|count|drop|\\*|\\&|\\>|\\<|\\!|\\||%|chr|mid|master|truncate|" +
			"char|declare|sitename|net user|xp_cmdshell|;|or|-|\\+|,|like'|and|exec|execute|insert|create|drop|" +
			"table|from|grant|use|group_concat|column_name|" +
			"information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|\\*|" +
			"chr|mid|master|truncate|char|declare|or|;|-|--|\\+|,|like|//|/|%|#";

	/**
	 * 把SQL关键字替换为空字符串
	 *
	 * @param param
	 * @return
	 */
	public static String filter(String param) {
		if (param == null) {
			return param;
		}
		return param.replaceAll("(?i)" + regex, ""); // (?i)不区分大小写替换
	}



}
