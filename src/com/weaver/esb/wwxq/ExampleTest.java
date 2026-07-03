package com.weaver.esb.wwxq;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleTest {

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String,Object> ret = new HashMap<>();
        String requestId = String.valueOf(params.getOrDefault("id", ""));
        String sfzh = String.valueOf(params.getOrDefault("sfzh", ""));
        if (StringUtils.isNotEmpty(requestId)) {
            String status = getProcessStatus(requestId);
            ret.put("status", status);
            // 当状态为归档时，回填员工编号
            if ("3".equals(status)) {
                List<Map<String, String>> gzkhs = getPersonCardNumber(requestId, sfzh);
                String mm = "123456";
                ret.put("gzkhs", JSON.toJSONString(gzkhs));
                // 更新简历表单
                for (Map<String, String> gzkh : gzkhs) {
                    String kh = gzkh.getOrDefault("gzkh", "");
                    String sfzhzh = gzkh.getOrDefault("sfzhzh", "");
                    if (StringUtils.isNotEmpty(kh) && StringUtils.isNotEmpty(sfzhzh)) {
                        RecordSet rs = new RecordSet();
                        String updatesql = "update formtable_main_627 set ygkbh = ?, mm = ? where sfzhzh = ?";
                        rs.executeUpdate(updatesql, kh, mm, sfzhzh);
                        ret.put("rs", JSON.toJSONString(rs));
                    }
                }
            }
        }
        return ret;
    }
    public static String getProcessStatus(String requestId) {
        // 查询流程状态
        RecordSetDataSource workflow = new RecordSetDataSource("coscs");
        String workflowSql = "select currentnodetype as status from workflow_requestbase where requestid = '" + requestId + "'";
        workflow.executeSql(workflowSql);
        if (workflow.next()){
            return workflow.getString("status");
        }
        return "";
    }

    public static List<Map<String, String>> getPersonCardNumber(String requestId, String sfzh) {
        // 根据请求ID和身份证号查询员工卡编号
        RecordSetDataSource cosces = new RecordSetDataSource("coscs");
        String sql = "select dt.gzkh as gzkh, dt.sfzhzh as sfzhzh from uf_yfzylsgzzsqtz as mt left join uf_yfzylsgzzsqtz_dt1 as dt on mt.id = dt.mainid where mt.lczt = 0 and mt.lch = '" + requestId + "' and dt.sfzhzh in ('" + sfzh + "')";
        cosces.executeSql(sql);
        List<Map<String, String>> gzkhs = new ArrayList<>();
        while (cosces.next()){
            Map<String, String> map = new HashMap<>();
            String gzkh = cosces.getString("gzkh");
            map.put("gzkh", gzkh);
            String sfzhzh = cosces.getString("sfzhzh");
            map.put("sfzhzh", sfzhzh);
            gzkhs.add(map);
        }
        return gzkhs;
    }
}
