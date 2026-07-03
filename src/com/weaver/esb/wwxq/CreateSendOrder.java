package com.weaver.esb.wwxq;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import weaver.general.BaseBean;
import weaver.general.StringUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建发货单
 */

public class CreateSendOrder {

    public Map<String, Object> execute(Map<String,Object> params) {
        // 返回值
        Map<String,Object> ret = new HashMap<>();
        String requestid = String.valueOf(params.get("requestid"));
        //系统标识
        String systemid = "RLWW";
        //密码
        String d_password = "CC66A24E1D624689A8CE89EC213E211A";
        // 当前时间戳
        String currentTimeTamp = getTimestamp();
        //当前日期
        String currentDate = getCurrentDate();
        //当前时间
        String currentTime = getCurrentTime();
        // 组装请求头
        Map<String,Object> header = new HashMap<>();
        header.put("systemid", systemid);
        header.put("currentDateTime",currentTimeTamp);
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
        // 封装主表字段
        JSONObject mainTable = new JSONObject();
        // ip端口号
        mainTable.put("ipdkh", "http://supplier.dfmc.com.cn");
        // 公司
        mainTable.put("gs", "1668");
        // 发货日期
        mainTable.put("fhrq", currentDate);
        // 预计到货时间
        mainTable.put("yjdhsj", currentDate);
        // 收货地址
        mainTable.put("shdz", "东风汽车集团有限公司研发总院");
        // 运输类型
        mainTable.put("yslx", "3");
        // 发货标题
        mainTable.put("fhbt", "外委需求发货单");
        // 状态
        mainTable.put("zt", "1");
        // 查询面试反馈主表数据
        RecordSet mainRs = new RecordSet();
        String msfkSql = "select gyszh, gshj, jehj, wwddsqr, lxfs, gysbm, bhsje, se from formtable_main_633 where requestid  = " + requestid;
        mainRs.execute(msfkSql);
        if (ObjectUtil.isNotNull(mainRs) && mainRs.next()){
            // 操作人
            String gyszh = mainRs.getString("gyszh");
            operationinfo.put("operator", StringUtil.isEmpty(gyszh)?"" : gyszh);
            // 采方收货总数量
            String gshj = mainRs.getString("gshj");
            mainTable.put("cfshzsl", StringUtil.isEmpty(gshj)?"" : gshj);
            // 发货总数量
            mainTable.put("fhzsl", StringUtil.isEmpty(gshj)?"" : gshj);
            // 发货含税金额（元）
            String jehj = mainRs.getString("jehj");
            mainTable.put("fhhsjey", StringUtil.isEmpty(jehj)?"" : jehj);
            // 收货人
            String wwddsqr = mainRs.getString("wwddsqr");
            mainTable.put("shr", StringUtil.isEmpty(wwddsqr)?"" : wwddsqr);
            // 收货人电话
            String lxfs = mainRs.getString("lxfs");
            mainTable.put("shrdh", StringUtil.isEmpty(lxfs)?"" : lxfs);
            // 供应商名称
            String gysbm = mainRs.getString("gysbm");
            mainTable.put("gysmc", StringUtil.isEmpty(gysbm)?"" : gysbm);
            // 发货不含税金额（元）
            String bhsje = mainRs.getString("bhsje");
            mainTable.put("fhbhsjey", StringUtil.isEmpty(bhsje)?"" : bhsje);
            // 发货税额（元）
            String se = mainRs.getString("se");
            mainTable.put("fhsey", StringUtil.isEmpty(se)?"" : se);
        }
        // 封装明细表数据列表
        List<JSONObject> detailTableList = new ArrayList<>();
        // 查询面试反馈明细表数据
        RecordSet detailRs = new RecordSet();
        String msfkDetailSql = "select detail.ddmxid as ddmxid, main.wwddsqr as wwddsqr, main.lxfs as lxfs, " +
                "detail.ddh as ddh, detail.gwmc as gwmc, detail.zj as zj, detail.xqsl as xqsl, detail.xqrq as xqrq," +
                " detail.slxj as slxj, detail.bhsdj as bhsdj, detail.slz as slz, detail.hsdj as hsdj, detail.jexj as jexj," +
                " detail.ddhh as ddhh, main.gysbm as gysbm, detail.ddhhh as ddhhh, detail.ddid as ddid from formtable_main_633 as main " +
                "left join formtable_main_633_dt2 as detail on main.id = detail.mainid where requestid = " + requestid;
        detailRs.execute(msfkDetailSql);
        List<String> wlmcs = new ArrayList<>();
        int count = 1;
        while (detailRs.next()){
            // 封装明细表数据
            JSONObject detailTable = new JSONObject();
            // 封装明细表下面的operate
            JSONObject operate = new JSONObject();
            operate.put("action", "SaveOrUpdate");
            operate.put("actionDescribe", "新增或修改");
            detailTable.put("operate", operate);
            // 封装明细表下面的明细表字段
            JSONObject detailData = new JSONObject();
            // 需求日期
            String xqrq = detailRs.getString("xqrq");
            detailData.put("xqrq", StringUtil.isEmpty(xqrq)?"" : xqrq);
            // 税率值
            String slz = detailRs.getString("slz");
            detailData.put("slz", StringUtil.isEmpty(slz)?"" : slz);
            // 币种
            detailData.put("bz", "39");
            // ID
//            detailData.put("id", "");
            // 发货单行号
            detailData.put("fhdxh", String.valueOf(count));
            count += 1;
            // 收货地址
            detailData.put("shdz", "东风汽车集团有限公司研发总院");
            // 含税单价
            String hsdj = detailRs.getString("hsdj");
            detailData.put("hsdj", StringUtil.isEmpty(hsdj)?"" : hsdj);
            // 不含税单价
            String bhsdj = detailRs.getString("bhsdj");
            detailData.put("bhsdj", StringUtil.isEmpty(bhsdj)?"" : bhsdj);
            // 订单号-行号
            String ddhhh = detailRs.getString("ddhhh");
            detailData.put("ddhxh", StringUtil.isEmpty(ddhhh)?"" : ddhhh);
            // 公司
            detailData.put("gs", "1668");
            // 发货含税金额（元）
            String jexj = detailRs.getString("jexj");
            detailData.put("fhhsje", StringUtil.isEmpty(jexj)?"" : jexj);
            // 订单行号
            String ddhh = detailRs.getString("ddhh");
            detailData.put("xh", StringUtil.isEmpty(ddhh)?"" : ddhh);
            // 供应商名称
            String gysbm = detailRs.getString("gysbm");
            detailData.put("gysmc", StringUtil.isEmpty(gysbm)?"" : gysbm);
            // 发货数量
            String slxj = detailRs.getString("slxj");
            detailData.put("fhsl", StringUtil.isEmpty(slxj)?"" : slxj);
            // 待收货数量
            detailData.put("dshsl", StringUtil.isEmpty(slxj)?"" : slxj);
            // 占用数量
            detailData.put("zysl", StringUtil.isEmpty(slxj)?"" : slxj);
            // 最大发货量
            detailData.put("zdfhl", StringUtil.isEmpty(slxj)?"" : slxj);
            // 采方收货数量
            detailData.put("cfshsl", StringUtil.isEmpty(slxj)?"" : slxj);
            // 订单ID
            String ddid = detailRs.getString("ddid");
            detailData.put("ddid", StringUtil.isEmpty(ddid)?"" : ddid);
            String ddmxid = detailRs.getString("ddmxid");
            // 订单明细id
            detailData.put("ddmxid", StringUtil.isEmpty(ddmxid)?"" : ddmxid);
            // 收货人
            String wwddsqr = detailRs.getString("wwddsqr");
            detailData.put("shr", StringUtil.isEmpty(wwddsqr)?"" : wwddsqr);
            // 收货电话
            String lxfs = detailRs.getString("lxfs");
            detailData.put("shdh", StringUtil.isEmpty(lxfs)?"" : lxfs);
            // 订单号
            String ddh = detailRs.getString("ddh");
            detailData.put("ddh", StringUtil.isEmpty(ddh)?"" : ddh);
            // 物料名称
            String gwmc = detailRs.getString("gwmc");
            wlmcs.add(gwmc);
            detailData.put("wlmc", StringUtil.isEmpty(gwmc)?"" : gwmc);
            // 物料规格
            String zj = detailRs.getString("zj");
            detailData.put("wlgg", StringUtil.isEmpty(zj)?"" : zj);
            // 需求数量
            String xqsl = detailRs.getString("xqsl");
            detailData.put("xqsl", StringUtil.isEmpty(xqsl)?"" : xqsl);
            // 是否已收货
            detailData.put("sfysh", "0");
            // 状态
            detailData.put("zt", "1");
            detailTable.put("data", detailData);
            detailTableList.add(detailTable);
        }
        // 物料名称
        String wlmc = String.join(",", wlmcs);
        mainTable.put("wlmc", wlmc);
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
                    "https://10.124.2.95:81/api/cube/restful/interface/saveOrUpdateModeData/MSFKCJFHD",
                    "datajson", sendParams);
//            ret.put("result", result);
            JSONObject resultObj = JSON.parseObject(result);
            String code = resultObj.getString("status");
            String datajsonStr = resultObj.getString("datajson");
            String billid = getBillid(datajsonStr);
            ret.put("billid", billid);
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

    private String getBillid(String datajson) {
        JSONObject dataJsonObject = JSON.parseObject(datajson);
        JSONArray data = dataJsonObject.getJSONArray("data");
        if (ObjectUtil.isNotNull(data) && !data.isEmpty()) {
            JSONObject jsonObject = data.getJSONObject(0);
            return jsonObject.getString("billid");
        }
        return null;
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

    private static String null2String(Object s) {
        return s == null ? "" : s.toString();

    }
}
