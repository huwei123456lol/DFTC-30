package com.weaver.esb.wwxq;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description 获取外委人员打卡时间
 */

public class GetWWPersonCheckInTime {

    public Map<String, Object> execute(Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String workday = fmt.format(calendar.getTime());

            String url = "http://odp.tc.dfmc.com.cn/odp/api/financeDepartment/attendStatus" +
                    "?limit=9999&page=0&workday=" + workday;

            new BaseBean().writeLog("[GetWWPersonCheckInTime] 请求URL: " + url);

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpGet.setHeader("Authorization", "DFTCODP");
            httpGet.setHeader("X-Timestamp", String.valueOf(System.currentTimeMillis()));
            httpGet.setHeader("X-Sequence-No", UUID.randomUUID().toString().replaceAll("-", ""));
            httpGet.setHeader("Connection", "keep-alive");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                // 获取所有班次信息
//                RecordSet rs = new RecordSet();
//                String bcSql = "select kssj, jssj from uf_kqbc;";
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity, "UTF-8");
                JSONObject jsonObject = JSON.parseObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");
                // 记录
                JSONArray records = data.getJSONArray("records");
                // 获取工号
                String gh = String.valueOf(params.getOrDefault("gh", ""));
                result.put("gh", gh);
                // 获取人员ID
                String xm = String.valueOf(params.getOrDefault("xm", ""));
                // 获取外委人员班次时间
                RecordSet bcsj = new RecordSet();
                String bcSql = "select swkssj, swjssj, xwkssj, xwjssj from uf_gsgzszcp where find_in_set(?, kqry) > 0";
                bcsj.executeQuery(bcSql, xm);
                if (bcsj.next()){
                    result.put("oweswStartTime", bcsj.getString("swkssj"));
                    result.put("oweswEndTime", bcsj.getString("swjssj"));
                    result.put("owexwStartTime", bcsj.getString("xwkssj"));
                    result.put("owexwEndTime", bcsj.getString("xwjssj"));
                }
                String personSql = "select id from hrmresource where workcode ='" + gh + "'";
                if (StringUtils.isNotEmpty(gh)){
                    RecordSetDataSource cosces = new RecordSetDataSource("coscs");
                    cosces.execute(personSql);
                    if (cosces.next()){
                        String cosId = cosces.getString("id");
                        result.put("cosId", cosId);
                        Object person = records.stream().filter(record -> record instanceof JSONObject &&
                                ((JSONObject) record).getString("cosId").equals(cosId)).findAny().orElseGet(null);
                        if (ObjectUtil.isNotNull(person)){
                            // 打卡开始时间
                            String minTime = ((JSONObject) person).getString("minTime");
                            result.put("startTime", minTime);
                            // 打卡结束时间
                            String maxTime = ((JSONObject) person).getString("maxTime");
                            result.put("endTime", maxTime);
                        }
                    } else {
                        result.put("cosId", JSON.toJSONString(cosces));
                    }
                }
                new BaseBean().writeLog("[GetWWPersonCheckInTime] 响应数据: " + responseBody);
                result.put("success", true);
            } else {
                new BaseBean().writeLog("[GetWWPersonCheckInTime] 请求失败，状态码: " + statusCode);
                result.put("success", false);
                result.put("message", "请求失败，状态码: " + statusCode);
            }

            response.close();
        } catch (Exception e) {
            new BaseBean().writeLog("[GetWWPersonCheckInTime] 异常: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "异常: " + e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
