package com.weaver.esb.wwxq;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.StringUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Timestamp;

/**
 * 创建采购订单
 */

public class CreateOrderWwxq {

	/**
	 * @param:  param(Map collections)
	 * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
	 */
	public Map<String, Object> execute(Map<String,Object> params) {
		// 示例：data：定义的请求数据，code:定义的响应数据
		//获取外委订单明细ID
		String wwddmxid = (String) params.get("wwxqzxmxid");
		Map<String,Object> ret = new HashMap<>();
		 //1、查询外委需求执行明细主表 合同 供应商
		String mainTableSql="select bdzhjg, cosgysbm, sqr, gysmc, coshtbhwb, htje, htsqlx, coshtbh, coshtmc, coscgxqid, sapgysbm, htlsysje, coshtbh, xqgszsl from uf_rlwwddzxmx where id =" + wwddmxid;
		// 采购订单明细表 明细表1 执行SQL语句
		String detailTableSql = "select xqgs, sl, bhsdj, hsdj, hhszj, xqr, xqrdh, xqrdz, zj, gwmcwb, hhszj, xqgs, rwmc, cbzxbm, cbzxmc, rwbm, cbzx, xmbh, xmmc, rwmcwb from uf_rlwwddzxmx_dt1 where mainid  =" + wwddmxid;
		RecordSet mainRs = new RecordSet();
		RecordSet detailRs = new RecordSet();
		//封装mainTable参数
		JSONObject mainTable = new JSONObject();

		mainRs.execute(mainTableSql);
//		System.out.println("主表值" + mainRs);
		//封装operationinfo参数
		JSONObject operationinfo = new JSONObject();
		List<Map<String, Object>> detail1s = new ArrayList<>();
		if(ObjectUtil.isNotNull(mainRs) && mainRs.next()){
			// 供应商名称
			String gysmc = mainRs.getString("gysmc");
			mainTable.put("gys", StringUtil.isEmpty(gysmc)?"" : gysmc);
			// COS供应商编码
			String cosgysbm = mainRs.getString("cosgysbm");
			// 合同金额
			String htje = mainRs.getString("htje");
			mainTable.put("htsxjey", StringUtil.isEmpty(htje)?"" : htje);
			// COS采购需求ID
			String coscgxqid = mainRs.getString("coscgxqid");
			mainTable.put("cgxqyjcgjehsy", StringUtil.isEmpty(coscgxqid)?"" : coscgxqid);
			// SAP供应商编码
			String sapgysbm = mainRs.getString("sapgysbm");
			mainTable.put("sapgysbm", StringUtil.isEmpty(sapgysbm)?"" : sapgysbm);
			mainTable.put("coscgxqdhn", StringUtil.isEmpty(coscgxqid)?"" : coscgxqid);
			// COS合同名称
			String coshtmc = mainRs.getString("coshtmc");
			mainTable.put("coshtmc", StringUtil.isEmpty(coshtmc)?"" : coshtmc);
			// 申请人
			String sqr = mainRs.getString("sqr");
			mainTable.put("cgyid", StringUtil.isEmpty(sqr)?"" : sqr);
			operationinfo.put("operator", StringUtil.isEmpty(sqr)?"" : sqr);
			// COS合同编号
			String coshtbh = mainRs.getString("coshtbh");
			mainTable.put("coshtid", StringUtil.isEmpty(coshtbh)?"" : coshtbh);
			mainTable.put("sqr", StringUtil.isEmpty(sqr)?"" : sqr);
			// 根据COS供应商编号（cosgysbm）查询采购管理系统供应商相关信息 数据源 供应商平台
			String gysSql = "select id, mrlxr, mrlxfs, userid, mrdzyj from uf_company_info where cosgysbm = '" + cosgysbm + "'";
//			ret.put("gysSql", gysSql);
			RecordSet gysRs = new RecordSet();
			gysRs.execute(gysSql);
			if(ObjectUtil.isNotNull(gysRs) && gysRs.next()){
				// 默认联系邮箱
				String mrlxfs = gysRs.getString("mrlxfs");
				mainTable.put("lxyx", StringUtil.isEmpty(mrlxfs)?"" : mrlxfs);
				//供应商账号
				String userid = gysRs.getString("userid");
				mainTable.put("gyszh", StringUtil.isEmpty(userid)?"" : userid);
				// 默认联系人
				String mrlxr = gysRs.getString("mrlxr");
				mainTable.put("lxdh", StringUtil.isEmpty(mrlxr)?"" : mrlxr);
				mainTable.put("lxr", StringUtil.isEmpty(mrlxr)?"" : mrlxr);
				// 供应商ID
				String id = gysRs.getString("id");
				mainTable.put("gysmc", id);
			}
			// COS合同编号文本
			String coshtbhwb = mainRs.getString("coshtbhwb");
			mainTable.put("coshtbh", StringUtil.isEmpty(coshtbhwb)?"" : coshtbhwb);
			mainTable.put("xjczr", StringUtil.isEmpty(sqr)?"" : sqr);
			// 根据采购合同ID查询采购合同相关信息 数据源 COS uf_cghtjbxx
			RecordSetDataSource cosces = new RecordSetDataSource("coscs");
			String cosHtlSql = "select htzt, kkht, sfsw, htlx from uf_cghtjbxx where id = '" + coshtbh + "'";
//			ret.put("cosHtlSql", cosHtlSql);
			cosces.execute(cosHtlSql);
			if(ObjectUtil.isNotNull(cosces) && cosces.next()){
				// 合同类型
				String htlx = cosces.getString("htlx");
				mainTable.put("coshtlx", StringUtil.isEmpty(htlx)?"" : htlx);
				String htzt = cosces.getString("htzt");
				mainTable.put("coshtzt", StringUtil.isEmpty(htzt)?"" : htzt);
			}
			// 需求工时总数量
			String xqgszsl = mainRs.getString("xqgszsl");
			mainTable.put("xqzsl", StringUtil.isEmpty(xqgszsl)?"" : xqgszsl);
			// 本单执行价格
			String bdzhjg = mainRs.getString("bdzhjg");
			mainTable.put("hszjey", StringUtil.isEmpty(bdzhjg)?"" : bdzhjg);
			mainTable.put("LIFNR", StringUtil.isEmpty(sapgysbm)?"" : sapgysbm);
			detailRs.execute(detailTableSql);
//			System.out.println("采购订单明细表" + detailRs);
			int row_index = 1;
			// 日期格式
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
			LocalDate now = LocalDate.now();
			while(detailRs.next()){
				// 添加明细表1
				HashMap<String, Object> detail1 = new HashMap<>();
				if(ObjectUtil.isNotNull(cosces) && cosces.next()) {
					// 合同类型
					String htlx = cosces.getString("htlx");
					detail1.put("cgzjqd", StringUtil.isEmpty(htlx)?"" : htlx);
				}
				// 职级
				String zj = detailRs.getString("zj");
				detail1.put("wlgg", StringUtil.isEmpty(zj)?"" : zj);
				// 项目任务名称
				String rwmc = detailRs.getString("rwmc");
				detail1.put("rwmcllan", StringUtil.isEmpty(rwmc)?"" : rwmc);
				// 任务编号
				String rwbh = detailRs.getString("rwbm");
				detail1.put("PS_PSP_PNR", StringUtil.isEmpty(rwbh)?"" : rwbh);
				// 成本中心编码
				String cbzxbm = detailRs.getString("cbzxbm");
				detail1.put("cbzxbm", StringUtil.isEmpty(cbzxbm)?"" : cbzxbm);
				// 成本中心名称
				String cbzxmc = detailRs.getString("cbzxmc");
				detail1.put("cbzxmc", StringUtil.isEmpty(cbzxmc)?"" : cbzxmc);
				 // 项目编号
				String xmbh = detailRs.getString("xmbh");
				detail1.put("xmbhllan ", StringUtil.isEmpty(xmbh)?"" : xmbh);
				// 需求工时
				String xqgs = detailRs.getString("xqgs");
				detail1.put("dfhsl", StringUtil.isEmpty(xqgs)?"" : xqgs);
				// 行含税金额
				String hhszj = detailRs.getString("hhszj");
				detail1.put("dfhje", StringUtil.isEmpty(hhszj)?"" : hhszj);
				detail1.put("cfsl", StringUtil.isEmpty(xqgs)?"" : xqgs);
				detail1.put("cgyid", StringUtil.isEmpty(sqr)?"" : sqr);
				String sl = detailRs.getString("sl");
				detail1.put("sl", StringUtil.isEmpty(sl)?"" : String.valueOf(Double.parseDouble(sl)));
				// 岗位名称文本
				String gwmcwb = detailRs.getString("gwmcwb");
				detail1.put("wlmc", StringUtil.isEmpty(gwmcwb)?"" : gwmcwb);
				// 需求人地址
				String xqrdz = detailRs.getString("xqrdz");
				detail1.put("shdz", StringUtil.isEmpty(xqrdz)?"" : xqrdz);
				//含税单价
				String hsdj = detailRs.getString("hsdj");
				detail1.put("hsdj", StringUtil.isEmpty(hsdj)?"" : hsdj);
				//不含税单价
				String bhsdj = detailRs.getString("bhsdj");
				detail1.put("bhsdj", StringUtil.isEmpty(bhsdj)?"" : bhsdj);
				// 需求工时
				detail1.put("zdfhl", StringUtil.isEmpty(xqgs)?"" : xqgs);
				detail1.put("xqsl", StringUtil.isEmpty(xqgs)?"" : xqgs);
				//需求人电话
				String xqrdh = detailRs.getString("xqrdh");
				detail1.put("shdh", StringUtil.isEmpty(xqrdh)?"" : xqrdh);
				// 行号
				detail1.put("xh", row_index);
				row_index += 1;
				// 行含税金额
				detail1.put("xhsje", StringUtil.isEmpty(hhszj)?"" : hhszj);
				// 项目名称
				String xmh = detailRs.getString("xmbh");
				detail1.put("xmh", StringUtil.isEmpty(xmh)?"" : xmh);
				//成本中心
				String cbzx = detailRs.getString("cbzx");
				detail1.put("cbzx", StringUtil.isEmpty(cbzx)?"" : cbzx);
				// 任务名称
				String rwmcwb = detailRs.getString("rwmcwb");
				detail1.put("rwmc", StringUtil.isEmpty(rwmcwb)?"" : rwmcwb);
				// 需求人
				String xqr = detailRs.getString("xqr");
				detail1.put("shr", StringUtil.isEmpty(xqr)?"" : xqr);
				detail1.put("zyje", "0");
				detail1.put("fylbmc", "3");
				detail1.put("yskmbh", "5500170000");
				detail1.put("yskmmc", "共性费用－劳务费");
				detail1.put("gbsl", "0");
				detail1.put("dw", "工时");
				detail1.put("yrksl", "0");
				detail1.put("yshje", "0");
				detail1.put("thhsl", "0");
				detail1.put("zysl", "0");
				detail1.put("kthsl", "0");
				detail1.put("yrkje", "0");
				detail1.put("nf", now.format(formatter));
				detail1.put("yshsl", "0");
				detail1.put("thhje", "0");
				HashMap<String, Object> detail1Data = new HashMap<>();
				detail1Data.put("data", detail1);
				HashMap<String, Object> operate = new HashMap<>();
				operate.put("action", "Save");
				operate.put("actionDescribe", "新增");
				detail1Data.put("operate", operate);
				detail1s.add(detail1Data);
			}
		}
		//2、查询外委需求执行明细明细表 标的物
		//3、根据采购需求ID查询采购需求相信息 数据源 COS uf_cgxqjbxxb
		//4、根据采购合同ID查询采购合同相关信息 数据源 COS uf_cghtjbxx
		//5、根据COS供应商编号（cosgysbm）查询采购管理系统供应商相关信息 数据源 供应商平台
		//6、创建采购订单
		//当前日期
		String currentDate = getCurrentDate();
		//当前时间
		String currentTime = getCurrentTime();
		//获取时间戳
		String currentTimeTamp = getTimestamp();

//		Map<String, Object> queryParams = new HashMap<>();
		Map<String, Object> paramDatajson = new HashMap<>();
		//header
		Map<String, Object> header = new HashMap<>();
		//系统标识
		String systemid = "RLWW";
		//密码
		String d_password = "CC66A24E1D624689A8CE89EC213E211A";

		//封装data
		List<Map<String, Object>> paramDataList = new ArrayList<>();
		Map<String, Object> paramData =  new HashMap<>();

		operationinfo.put("operationDate", currentDate);
		operationinfo.put("operationTime", currentTime);
		paramData.put("operationinfo",operationinfo);

		mainTable.put("gs", "1668");
		mainTable.put("id", "1");
		mainTable.put("zxzt", "5");
		mainTable.put("sfsw", "1");
		mainTable.put("xdzje", "0");
		mainTable.put("lsysje", "0");
		mainTable.put("xmjehj", "0");
		mainTable.put("cgxqzjqd", "0");
		mainTable.put("BUKRS", "1224");
		mainTable.put("cglx", "14");
		mainTable.put("gyszt", "0");
		mainTable.put("ddlx", "0");
		mainTable.put("wlxx", "人力外委");
		mainTable.put("sqrq", currentDate);
		mainTable.put("coshtsqlx", "1");
		mainTable.put("coscgxqdh", wwddmxid);
		mainTable.put("EKORG", "1224");
		mainTable.put("htlb", "0");
		mainTable.put("sfwhtjesx", "1");
		mainTable.put("sfzt", "1");
		mainTable.put("zt", "1");
		mainTable.put("fbsj", getCurDateTime());
		mainTable.put("wwddbs", "1");
		paramData.put("mainTable",mainTable);
		paramData.put("detail1",detail1s);

		paramDataList.add(paramData);
		//封装header里的参数
		header.put("systemid",systemid);
		header.put("currentDateTime",currentTimeTamp);
		String md5Source = systemid+d_password+currentTimeTamp;
		String md5OfStr = getMD5Str(md5Source).toLowerCase();
		//Md5是：系统标识+密码+时间戳 并且md5加密的结果
		header.put("Md5",md5OfStr);

		paramDatajson.put("header",header);

		paramDatajson.put("data",paramDataList);

//		queryParams.put("datajson",paramDatajson);

//		String sendJSON = JSON.toJSONString(queryParams);
		//装填参数
		String result = null;
		String sendParams = JSON.toJSONString(paramDatajson);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 创建订单
			result = executeHttpPost(httpClient,
					"https://10.124.2.95:81/api/cube/restful/interface/saveOrUpdateModeData/GJWWDDZZXXCJCGDD",
					"datajson", sendParams);

			Map<String, Object> orderStatus = getOrderStatus(result);
//			ret.put("orderStatus", orderStatus);
			new BaseBean().writeLog("创建订单返回结果: " + result);
//			ret.put("result", result);
			ret.put("code", orderStatus.getOrDefault("code", ""));
			// 更新状态  通过调用接口不行，那么直接使用SQL语句
//			List<BasicNameValuePair> updateParams = new ArrayList<>();
//			updateParams.add(new BasicNameValuePair("cgddcfzt", String.valueOf(orderStatus.get("zxzt"))));
//			updateParams.add(new BasicNameValuePair("cgddh", String.valueOf(orderStatus.get("billid"))));
//			updateParams.add(new BasicNameValuePair("cgddcjxx", String.valueOf(orderStatus.get("data"))));
//			updateParams.add(new BasicNameValuePair("id", wwddmxid));
//
//			ret.put("updateParam", JSON.toJSONString(updateParams));
//
//			String updateResult = executeHttpPostWithParams(httpClient,
//					"http://10.124.3.40:8080/api/esb/oa/execute/gxwwddzxmxsj",
//					updateParams);
//
//			ret.put("updateResult", updateResult);
//			new BaseBean().writeLog("更新状态返回结果: " + updateResult);
			RecordSet updateRs = new RecordSet();
			RecordSet updateOrder = new RecordSet();
			RecordSet recordSet = new RecordSet();
			// 创建信息字段太长了，超出了字段限制
			String data = getData(result).replaceAll("\\\\", "").substring(0, 200);
			// 查看是否有billid，如果没有直接返回异常
			String billid = String.valueOf(orderStatus.getOrDefault("billid", ""));
			if (StringUtil.isEmpty(billid)) {
				updateRs.executeUpdate("update uf_rlwwddzxmx set cgddcfzt = ?, cgddcjwb = ?, cgddh = ?, cgddcjxx = ? where id = ?",
						2, 2, "", data, wwddmxid);
				ret.put("message", "数据异常，没有创建订单");
			} else {
				updateRs.executeUpdate("update uf_rlwwddzxmx set cgddcfzt = ?, cgddcjwb = ?, cgddh = ?, cgddcjxx = ? where id = ?",
						1, 1, billid, data, wwddmxid);
				updateOrder.executeUpdate("update uf_cgddb_dt1 set xmbhllan=xmh where mainid = ?", billid);
				recordSet.executeUpdate("UPDATE uf_rlwwddzxmx_dt1 wwdt1\n" +
						"INNER JOIN uf_rlwwddzxmx wwmt ON wwmt.id = wwdt1.mainid\n" +
						"INNER JOIN uf_cgddb ddmt ON wwmt.cgddh = ddmt.id\n" +
						"INNER JOIN uf_cgddb_dt1 dddt1 \n" +
						"    ON ddmt.id = dddt1.mainid\n" +
						"    AND wwdt1.xqgs = dddt1.xqsl\n" +
						"    AND wwdt1.gwmcwb = dddt1.wlmc\n" +
						"    AND wwdt1.zj = dddt1.wlgg\n" +
						"    AND wwdt1.rwmc = dddt1.rwmcllan\n" +
						"    AND wwdt1.xqr = dddt1.shr\n" +
						"SET \n" +
						"    wwdt1.ddhhh = dddt1.ddhxh,\n" +
						"    wwdt1.ddhh = dddt1.xh\n" +
						"WHERE wwmt.id = ?;", wwddmxid);
			}
			ret.put("billid", billid);
			if (updateRs.next()){
				ret.put("updateCount", String.valueOf(updateRs.getInt("updateCount")));
			}
			if (updateOrder.next()){
				ret.put("updateOrderCount", String.valueOf(updateOrder.getInt("updateCount")));
			}
			new BaseBean().writeLog("更新状态返回结果: " + updateRs.getData());
		} catch (Throwable e) {
			e.printStackTrace();
			ret.put("message", e.getMessage());
			new BaseBean().writeLog("" + e.getMessage());
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				ret.put("message", e.getMessage());
				new BaseBean().writeLog("" + e.getMessage());
			}
		}

//		ret.put("code","1");
//		ret.put("params", sendParams);
//		ret.put("mainTable", JSON.toJSONString(mainRs));
//		ret.put("detailTable", JSON.toJSONString(detailRs));
//		ret.put("mainTableSql", mainTableSql);
//		ret.put("detailTableSql", detailTableSql);
		return ret;
	}

	private String executeHttpPostWithParams(CloseableHttpClient httpClient, String url, List<BasicNameValuePair> params) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		// 设置请求头
		httpPost.setHeader("Content-Encoding", "UTF-8");
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

		// 设置请求体
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		httpPost.setEntity(entity);

		// 设置超时时间（毫秒）
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(30000)
				.setSocketTimeout(60000)
				.build();
		httpPost.setConfig(requestConfig);

		// 执行请求并返回结果
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} finally {
			response.close();
		}
	}
	private String executeHttpPost(CloseableHttpClient httpClient, String url, String paramName, String paramValue) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		// 设置请求头
		httpPost.setHeader("Content-Encoding", "UTF-8");
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

		// 设置请求体为 param 格式
		List<BasicNameValuePair> paramList = new ArrayList<>();
		paramList.add(new BasicNameValuePair(paramName, paramValue));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
		httpPost.setEntity(entity);

		// 设置超时时间（毫秒）
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(30000)
				.setSocketTimeout(60000)
				.build();
		httpPost.setConfig(requestConfig);

		// 执行请求并返回结果
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} finally {
			response.close();
		}

	}
	private Map<String, Object> getOrderStatus(String result){
		HashMap<String, Object> resp = new HashMap<>();
		resp.put("data", result);
		JSONObject resultObj = JSON.parseObject(result);
		resp.put("code", resultObj.getString("status"));
		String datajsonStr = resultObj.getString("datajson");
		if (!StringUtil.isEmpty(datajsonStr)) {
			JSONObject datajsonObj = JSON.parseObject(datajsonStr);
			com.alibaba.fastjson.JSONArray dataArray = datajsonObj.getJSONArray("data");
			if (ObjectUtil.isNotNull(dataArray) && !dataArray.isEmpty()) {
				JSONObject firstData = dataArray.getJSONObject(0);
				resp.put("billid", StringUtil.isEmpty(firstData.getString("billid"))?"":firstData.getString("billid"));

				String originaldataStr = firstData.getString("originaldata");
				if (!StringUtil.isEmpty(originaldataStr)) {
					JSONObject originaldataObj = JSON.parseObject(originaldataStr);
					JSONObject mainTableData = originaldataObj.getJSONObject("mainTable");
					if (ObjectUtil.isNotNull(mainTableData)) {
						resp.put("zxzt", mainTableData.getString("zxzt"));
					}
				}
			}
		}
		return resp;
	}

	private String getData(String result){
		JSONObject resultObj = JSON.parseObject(result);
		String datajsonStr = resultObj.getString("datajson");
		if (!StringUtil.isEmpty(datajsonStr)) {
			JSONObject datajsonObj = JSON.parseObject(datajsonStr);
			return JSON.toJSONString(datajsonObj.getJSONArray("data"));
		}
		return "";
	}

	private String getMD5Str(String plainText){
		//定义一个字节数组
		byte[] secretBytes = null;
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			//对字符串进行加密
			md.update(plainText.getBytes());
			//获得加密后的数据
			secretBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			//throw new RuntimeException("没有md5这个算法！");
			throw new RuntimeException("");
		}
		//将加密后的数据转换为16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);
		// 如果生成数字未满32位，需要前面补0
		// 不能把变量放到循环条件，值改变之后会导致条件变化。如果生成30位 只能生成31位md5
		int tempIndex = 32 - md5code.length();
		for (int i = 0; i < tempIndex; i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	private static String getCurrentTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currenttime = (timestamp.toString()).substring(11, 13) + ":" + (timestamp.toString()).substring(14, 16) + ":"
				+ (timestamp.toString()).substring(17, 19);
		return currenttime;
	}

	private static String getCurrentDate() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-"
				+ (timestamp.toString()).substring(8, 10);
		return currentdate;
	}

	/**
	 * 获取当前日期时间。 YYYY-MM-DD HH:MM:SS
	 * @return		当前日期时间
	 */
	private static String getCurDateTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(0, 19);
	}

	/**
	 * 获取时间戳   格式如：19990101235959
	 * @return
	 */
	private static String getTimestamp(){
		return getCurDateTime().replace("-", "").replace(":", "").replace(" ", "");
	}

	private static int getIntValue(String v, int def) {
		try {
			return Integer.parseInt(v);
		} catch (Exception ex) {
			return def;
		}
	}

	private static String null2String(Object s) {
		return s == null ? "" : s.toString();

	}
}