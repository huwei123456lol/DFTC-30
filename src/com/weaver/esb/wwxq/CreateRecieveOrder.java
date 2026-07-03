package com.weaver.esb.wwxq;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.engine.integration.util.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.StringUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

/**
 *  根据发货单生成收货单
 */
public class CreateRecieveOrder {

    public Map<String, Object> execute(Map<String,Object> params) {
        Map<String,Object> ret = new HashMap<>();
        String billid = String.valueOf(params.get("billid"));
        //系统标识
        String systemid = "RLWW";
        //密码
        String d_password = "CC66A24E1D624689A8CE89EC213E211A";
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();
        String currentTimeTamp = getTimestamp();
        // 组装请求头
        Map<String,Object> header = new HashMap<>();
        header.put("systemid", systemid);
        header.put("currentDateTime", currentTimeTamp);
        String md5Source = systemid+d_password+currentTimeTamp;
        String md5OfStr = getMD5Str(md5Source).toLowerCase();
        //Md5是：系统标识+密码+时间戳 并且md5加密的结果
        header.put("Md5",md5OfStr);
        // 封装operationinfo
        JSONObject operationinfo = new JSONObject();
        // 操作日期
        operationinfo.put("operationDate", currentDate);
        // 操作时间
        operationinfo.put("operationTime", currentTime);
        // 封装mainTable
        JSONObject mainTable = new JSONObject();
        // 公司
        mainTable.put("gs", "1668");
        // 先查发货单主表
        RecordSet mainRs = new RecordSet();
        String mainSql = "select shr, fhdh, wlmc, fhzsl, fhhsjey, gysmc from uf_fhdb where id = " + billid;
        mainRs.execute(mainSql);
        if (ObjectUtil.isNotNull(mainRs) && mainRs.next()){
            // 质检人
            String shr = mainRs.getString("shr");
            mainTable.put("zjr", StringUtils.isEmpty(shr)?"":shr);
            operationinfo.put("operator", StringUtil.isEmpty(shr)?"" : shr);
            // 收货人ID
            mainTable.put("shr", StringUtils.isEmpty(shr)?"":shr);
            // 发货单号
            String fhdh = mainRs.getString("fhdh");
            mainTable.put("fhdh", StringUtils.isEmpty(fhdh)?"":fhdh);
            // 物料信息
            String wlmc = mainRs.getString("wlmc");
            mainTable.put("wlxx", StringUtils.isEmpty(wlmc)?"":wlmc);
            // 收货总数量
            String fhzsl = mainRs.getString("fhzsl");
            mainTable.put("shzsl", StringUtils.isEmpty(fhzsl)?"":fhzsl);
            // 收货含税金额（元）
            String fhhsjey = mainRs.getString("fhhsjey");
            mainTable.put("shhsjey", StringUtils.isEmpty(fhhsjey)?"":fhhsjey);
            // 供应商ID
            String gysmc = mainRs.getString("gysmc");
            mainTable.put("gys", StringUtils.isEmpty(gysmc)?"":gysmc);
        }
        // 收货日期
        mainTable.put("shrq", currentDate);
        // 状态
        mainTable.put("zt", "3");
        // 封装明细表数据列表
        List<JSONObject> detailTableList = new ArrayList<>();
        // 查询发货单以及明细表
        RecordSet detailRs = new RecordSet();
        String detailSql = "select dt1.ddh as ddh, dt1.ddhxh as ddhxh, dt1.ddid as ddid, dt1.ddmxid as ddmxid, " +
                "mt.fhdh as fhdh, mt.id as mid, dt1.id as did, dt1.wlmc as wlmc, dt1.wlgg as wlgg, " +
                "dt1.xqsl as xqsl, dt1.xqrq as xqrq, dt1.fhsl as fhsl, dt1.bhsdj as bhsdj, dt1.slz as slz, " +
                "dt1.hsdj as hsdj, dt1.fhhsje as fhhsje, mt.shr as shr, mt.gysmc as gysmc from uf_fhdb mt left join uf_fhdb_dt1 dt1 on mt.id=dt1.mainid where mt.id = " + billid;
        detailRs.execute(detailSql);
        int count = 1;
        List<String> ddmxids = new ArrayList<>();
        while (detailRs.next()){
            // 封装明细表数据
            JSONObject detailTable = new JSONObject();
            // 封装明细表下面的operate
            JSONObject operate = new JSONObject();
            operate.put("action", "SaveOrUpdate");
            operate.put("actionDescribe", "新增或修改");
            detailTable.put("operate", operate);
            // 封装detailTable
            JSONObject detailData = new JSONObject();
            // 行号
            detailData.put("xh", String.valueOf(count));
            count += 1;
            // 订单号
            String ddh = detailRs.getString("ddh");
            detailData.put("ddh", StringUtil.isEmpty(ddh)?"" : ddh);
            // 订单号-行号
            String ddhxh = detailRs.getString("ddhxh");
            detailData.put("ddhxh", StringUtil.isEmpty(ddhxh)?"" : ddhxh);
            // 订单ID
            String ddid = detailRs.getString("ddid");
            detailData.put("ddid", StringUtil.isEmpty(ddid)?"" : ddid);
            // 订单明细ID
            String ddmxid = detailRs.getString("ddmxid");
            ddmxids.add(ddmxid);
            detailData.put("ddmxid", StringUtil.isEmpty(ddmxid)?"" : ddmxid);
            // 发货单号
            String fhdh = detailRs.getString("fhdh");
            detailData.put("fhdh", StringUtil.isEmpty(fhdh)?"" : fhdh);
            // 发货单ID
            String mid = detailRs.getString("mid");
            detailData.put("fhdid", StringUtil.isEmpty(mid)?"" : mid);
            // 发货单明细ID
            String did = detailRs.getString("did");
            detailData.put("fhdmxid", StringUtil.isEmpty(did)?"" : did);
            // 物料名称
            String wlmc = detailRs.getString("wlmc");
            detailData.put("wlmc", StringUtil.isEmpty(wlmc)?"" : wlmc);
            // 物料规格
            String wlgg = detailRs.getString("wlgg");
            detailData.put("wlgg", StringUtil.isEmpty(wlgg)?"" : wlgg);
            // 需求数量
            String xqsl = detailRs.getString("xqsl");
            detailData.put("xqsl", StringUtil.isEmpty(xqsl)?"" : xqsl);
            // 需求日期
            String xqrq = detailRs.getString("xqrq");
            detailData.put("xqrq", StringUtil.isEmpty(xqrq)?"" : xqrq);
            // 发货数量
            String fhsl = detailRs.getString("fhsl");
            detailData.put("fhsl", StringUtil.isEmpty(fhsl)?"" : fhsl);
            // 收货数量
            detailData.put("shsl", StringUtil.isEmpty(fhsl)?"" : fhsl);
            // 待收货数量
            detailData.put("dshsl", StringUtil.isEmpty(fhsl)?"" : fhsl);
            // 不含税单价
            String bhsdj = detailRs.getString("bhsdj");
            detailData.put("bhsdj", StringUtil.isEmpty(bhsdj)?"" : bhsdj);
            // 税率值
            String slz = detailRs.getString("slz");
            detailData.put("slz", StringUtil.isEmpty(slz)?"" : slz);
            // 含税单价
            String hsdj = detailRs.getString("hsdj");
            detailData.put("hsdj", StringUtil.isEmpty(hsdj)?"" : hsdj);
            // 收货人ID
            String shr = detailRs.getString("shr");
            detailData.put("shr", StringUtil.isEmpty(shr)?"" : shr);
            // 收货含税金额（元）
            String fhhsje = detailRs.getString("fhhsje");
            detailData.put("xshhsje", StringUtil.isEmpty(fhhsje)?"" : fhhsje);
            // 状态
            detailData.put("zt", "3");
            detailData.put("biz", "39");
            // 供应商ID
            String gysmc = detailRs.getString("gysmc");
            detailData.put("gys", StringUtil.isEmpty(gysmc)?"" : gysmc);
            // 供应商账号
            RecordSet gysRs = new RecordSet();
            String gysSql = "select userid from uf_company_info where id = " + gysmc;
            gysRs.execute(gysSql);
            if (ObjectUtil.isNotNull(gysRs) && gysRs.next()) {
                String userid = gysRs.getString("userid");
                detailData.put("gyszh", StringUtil.isEmpty(userid)?"" : userid);
            }
            // 公司ID
            detailData.put("gs", "1668");
            // 运输类型
            detailData.put("yslx", "3");
            // 最大发货量
            detailData.put("zdfhl", StringUtil.isEmpty(fhsl)?"" : fhsl);
            // 订单明细ID发货单对应
            detailData.put("ddmxidfhdy", StringUtil.isEmpty(ddmxid)?"" : ddmxid);
            detailTable.put("data", detailData);
            detailTableList.add(detailTable);
        }
        String ddmxid = String.join(",", ddmxids);
        // 订单号
        mainTable.put("ddh", ddmxid);
        // 封装Data
        JSONObject data = new JSONObject();
        data.put("mainTable", mainTable);
        data.put("detail1", detailTableList);
        data.put("operationinfo", operationinfo);
        // 将data加入列表
        List<JSONObject> dataList = new ArrayList<>();
        dataList.add(data);
        JSONObject dataJson = new JSONObject();
        dataJson.put("data", dataList);
        dataJson.put("header", header);
        // 参数
        String sendParams = JSON.toJSONString(dataJson);
        // 调用接口的返回值
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 创建发货单
            result = executeHttpPost(httpClient,
                    "https://10.124.2.95:81/api/cube/restful/interface/saveOrUpdateModeData/GJFHDCJZHD",
                    "datajson", sendParams);
//            ret.put("result", result);
            JSONObject resultObj = JSON.parseObject(result);
            String code = resultObj.getString("status");
            String datajsonStr = resultObj.getString("datajson");
            ret.put("code", code);
            ret.put("datajson", datajsonStr);
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
        return ret;
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
}
