package com.weaver.esb.wwxq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  发送订单给供应商
 */
public class SendOrderToSupplier {

    public Map<String, Object> execute(Map<String,Object> params) {
        // 数据库字段列表
        List<String> fields = new ArrayList<>();
        // 数据库字段值列表
        List<Object> values = new ArrayList<>();
        // 采购需求预计采购金额（含税,元）
        Object cgxqyjcgjehsy = params.get("cgxqyjcgjehsy");
        fields.add("cgxqyjcgjehsy");
        values.add(cgxqyjcgjehsy);
        // 是否涉外
        fields.add("sfsw");
        values.add("1");
        // 是否无合同金额上限
        fields.add("sfwhtjesx");
        values.add("1");
        //合同类别
        fields.add("htlb");
        values.add("0");
        // 申请日期
        fields.add("sqrq");
        values.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        // 付款条件
        Object fktj = params.get("fktj");
        fields.add("fktj");
        values.add(fktj);
        // 	付款条件文本
        Object fktjwb = params.get("fktjwb");
        fields.add("fktjwb");
        values.add(fktjwb);
        // 操作类型
        Object czlx = params.get("czlx");
        fields.add("czlx");
        values.add(czlx);
        // 	申请人
        Object sqr = params.get("sqr");
        fields.add("sqr");
        values.add(sqr);
        // SAP供应商编码
        Object sapgysbm = params.get("sapgysbm");
        fields.add("sapgysbm");
        values.add(sapgysbm);
        // cos采购需求单号
        Object coscgxqdh = params.get("coscgxqdhn");
        fields.add("coscgxqdh");
        values.add(coscgxqdh);
        // 项目金额合计
        Object xmjehj = params.get("xmjehj");
        fields.add("xmjehj");
        values.add(xmjehj);
        // COS采购需求id
        Object coscgxqid = params.get("coscgxqdh");
        fields.add("coscgxqid");
        values.add(coscgxqid);
        // 订单编号
        Object ddbh = params.get("ddbh");
        fields.add("ddbh");
        values.add(ddbh);
        // 含税总金额（元）
        Object hszjey = params.get("hszjey");
        fields.add("hszjey");
        values.add(hszjey);
        // 	关联合同编号
        Object glhtbh = params.get("glhtbh");
        fields.add("glhtbh");
        values.add(glhtbh);
        // 	供应商编码
        Object gysmc = params.get("gysmc");
        fields.add("gysmc");
        values.add(gysmc);
        // 状态
        Object zt = params.get("zt");
        fields.add("zt");
        values.add(zt);
        // 需求总数量
        Object xqzsl = params.get("xqzsl");
        fields.add("xqzsl");
        values.add(xqzsl);
        // 供应商状态
        Object gyszt = params.get("gyszt");
        fields.add("gyszt");
        values.add(gyszt);
        // 供应商名称
        Object gys = params.get("gys");
        fields.add("gys");
        values.add(gys);
        // 	COS合同编号(文本)
        Object coshtbh = params.get("coshtbh");
        fields.add("coshtbh");
        values.add(coshtbh);
        // 合同上限金额（元）
        Object htsxjey = params.get("htsxjey");
        fields.add("htsxjey");
        values.add(htsxjey);
        // 已下单未验收总金额（元）
        Object yxdzjey = params.get("yxdzjey");
        fields.add("yxdzjey");
        values.add(yxdzjey);
        // COS供应商编码
        Object cosgysbm = params.get("cosgysbm");
        fields.add("cosgysbm");
        values.add(cosgysbm);
        // COS合同编号
        Object coshtid = params.get("coshtid");
        fields.add("coshtid");
        values.add(coshtid);
        // COS合同名称
        Object coshtmc = params.get("coshtmc");
        fields.add("coshtmc");
        values.add(coshtmc);
        // COS合同类型
        Object coshtlx = params.get("coshtlx");

        return params;
    }
}
