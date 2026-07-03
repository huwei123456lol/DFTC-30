package com.weaver.esb.wwxq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import weaver.general.BaseBean;
import weaver.general.StringUtil;
import weaver.rsa.security.Base64;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预算校验
 * @author hw
 * @date 2026/4/28
 */
public class BugdetVerification {

    public Map<String, Object> execute(Map<String,Object> params) {
        Map<String, Object> verifyResult = new HashMap<>();
        String action = String.valueOf(params.getOrDefault("action", ""));
        if (StringUtil.isEmpty(action)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：操作类型不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String billNo = String.valueOf(params.getOrDefault("billNo", ""));
        if (StringUtil.isEmpty(billNo)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：单号为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String projCode = String.valueOf(params.getOrDefault("projCode", ""));
        if (StringUtil.isEmpty(projCode)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：项目编号不能为空！");
            verifyResult.put("code", "099");
        }
        String projName = String.valueOf(params.getOrDefault("projName", ""));
        if (StringUtil.isEmpty(projName)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：项目名称不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String taskNo = String.valueOf(params.getOrDefault("taskNo", ""));
        if (StringUtil.isEmpty(taskNo)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：任务编号不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String taskName = String.valueOf(params.getOrDefault("taskName", ""));
        if (StringUtil.isEmpty(taskName)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：任务名称不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String kostl = String.valueOf(params.getOrDefault("kostl", ""));
        if (StringUtil.isEmpty(kostl)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：成本中心编码不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String kostlName = String.valueOf(params.getOrDefault("kostlName", ""));
        if (StringUtil.isEmpty(kostlName)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：成本中心名称不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String ref_bill_no = String.valueOf(params.getOrDefault("ref_bill_no", ""));
        if (StringUtil.isEmpty(ref_bill_no)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：引用单号不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
        String amount = String.valueOf(params.getOrDefault("amount", ""));
        if (StringUtil.isEmpty(amount)){
            verifyResult.put("success", false);
            verifyResult.put("message", "参数错误：不含税单价不能为空！");
            verifyResult.put("code", "099");
            return verifyResult;
        }
		String rowNo = String.valueOf(params.getOrDefault("rowNo", "1"));
		return budgetVerification(
                action,
                billNo,
                projCode,
                projName,
                taskNo,
                taskName,
                kostl,
                kostlName,
                ref_bill_no,
                amount,
				rowNo
        );
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
	private static  String getTimestamp(){
		String baseTimestamp = getCurDateTime().replace("-", "").replace(":", "").replace(" ", "");
		int nacos = (int)(Math.random() * 1000);
		int random = (int)(Math.random() * 100000);
		return baseTimestamp + String.format("%03d", nacos) + String.format("%05d", random);
	}

	// 预算校验
	public Map<String, Object> budgetVerification(String action, String billNo, String projCode,
                                                  String projName, String taskNo, String taskName,
                                                  String kostl, String kostlName, String ref_bill_no,  String amount, String rowNo) {

		Map<String, Object> result = new HashMap<>();

		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();

			JSONObject requestData = new JSONObject();
			requestData.put("INFID", "COS019");

			JSONObject item = new JSONObject();
			item.put("GRPID", StringUtil.isEmpty(billNo) ? "" : billNo);
			// 操作类型
			item.put("ACTION", action);
			// 公司代码
			item.put("BUKRS", "1224");
			// 业务泛微
			item.put("GSBER", "1224");
			// 单据来源系统
			item.put("SYS_CODE", "COS");
			// 单据类型
			item.put("BILL_TYPE", "COS-CGDD");
			// 单号
			item.put("BILL_NO", StringUtil.isEmpty(billNo) ? "" : billNo);
			// 申请时间
            String billDate = getCurDateTime().replace("-", "").replace(":", "").replace(" ", "");
			item.put("BILL_DATE", StringUtil.isEmpty(billDate) ? "" : billDate);
			// 申请人工号
			item.put("BILL_USER", "");
			// 申请人名称
			item.put("BILL_USER_NAME", "系统管理员");
			// 订单说明
			item.put("BILL_DESC", "外委订单预算校验");
			// 行号
			item.put("ROW_NO", rowNo);
			// 不含税单价
			item.put("AMT", StringUtil.isEmpty(amount) ? "0" : amount);
            LocalDate now = LocalDate.now();
            // yyyy
            String year = String.format("%04d", now.getYear());
            item.put("YEAR", StringUtil.isEmpty(year) ? "" : year);
			// mm
            String month = String.format("%02d", now.getMonth().getValue());
            item.put("MON", StringUtil.isEmpty(month) ? "" : month);
			// 备注
			item.put("ROW_DESC", "外委订单预算校验");
			// 项目编号
			item.put("PROJ_CODE", StringUtil.isEmpty(projCode) ? "" : projCode);
			// 项目名称
			item.put("PROJ_NAME", StringUtil.isEmpty(projName) ? "" : projName);
			// 任务编号
			item.put("TASK_NO", StringUtil.isEmpty(taskNo) ? "" : taskNo);
			// 任务名称
			item.put("TASK_NAME", StringUtil.isEmpty(taskName) ? "" : taskName);
			// 成本中心编码
			item.put("KOSTL", StringUtil.isEmpty(kostl) ? "" : kostl);
			// 成本中心名称
			item.put("KOSTL_NAME", StringUtil.isEmpty(kostlName) ? "" : kostlName);
			// 会计科目编码
			item.put("HKONT", "5500170000");
			// 会计科目名称
			item.put("HKONT_NAME", "共性费用－劳务费");
			// 引用单据来源系统
			item.put("REF_SYS_CODE", "COS");
			// 引用单据类型
			item.put("REF_BILL_TYPE", "COS-CGDD");
			// 引用单号
			item.put("REF_BILL_NO", StringUtil.isEmpty(ref_bill_no) ? "" : ref_bill_no);
			item.put("REF_BILL_ROW_NO", "");

			com.alibaba.fastjson.JSONArray itemsArray = new com.alibaba.fastjson.JSONArray();
			itemsArray.add(item);
			requestData.put("ZSCOS019_I", itemsArray);

			String url = "https://apitest.dfmc.com.cn/sap/zinp_service/dfrd_cos";

			HttpPost httpPost = new HttpPost(url);

			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("X-App-Id", "10382");
			httpPost.setHeader("X-Sequence-No", getTimestamp());
			httpPost.setHeader("X-Timestamp", getTimestamp().substring(0, 15));

			String username = "CHpF7g86JyeSBakB";
			String password = "YcFmsikAEGtomKAZ";
			String authString = username + ":" + password;
			String encodedAuth = Base64.encodeBase64String(authString.getBytes("UTF-8"));
			httpPost.setHeader("Authorization", "Basic " + encodedAuth);

			StringEntity entity = new StringEntity(requestData.toJSONString(), "UTF-8");
			httpPost.setEntity(entity);

			result.put("param", JSON.toJSONString(requestData));
//			result.put("header", JSON.toJSONString(httpPost.getAllHeaders()));

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(30000)
					.setSocketTimeout(60000)
					.build();
			httpPost.setConfig(requestConfig);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
//                result.put("result", responseBody);
				new BaseBean().writeLog("预算校验返回结果: " + responseBody);

				JSONObject responseJson = JSON.parseObject(responseBody);
				JSONObject resultData = responseJson.getJSONObject("ZSCOS019_E");

				if (resultData != null) {
					String resultCode = resultData.getString("RESULT_CODE");
					String resultMessage = resultData.getString("RESULT_DESC");

					result.put("code", resultCode);
					result.put("message", resultMessage);
					result.put("success", "000".equals(resultCode));

					if ("000".equals(resultCode)) {
						new BaseBean().writeLog("预算校验成功");
					} else {
						new BaseBean().writeLog("预算校验失败: " + resultMessage);
					}
				} else {
					result.put("code", "-1");
					result.put("message", "返回数据格式错误");
					result.put("success", false);
				}

			} catch (Exception e){
                e.printStackTrace();
                result.put("code", "-1");
                result.put("message", "请求异常: " + e.getMessage());
                result.put("success", false);
                new BaseBean().writeLog("预算校验异常: " + e.getMessage());
            } finally {
                response.close();
            }

			httpClient.close();

		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "-1");
			result.put("message", "请求异常: " + e.getMessage());
			result.put("success", false);
			new BaseBean().writeLog("预算校验异常: " + e.getMessage());
		}

		return result;
	}

}
