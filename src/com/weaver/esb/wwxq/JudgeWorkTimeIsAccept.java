package com.weaver.esb.wwxq;

import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JudgeWorkTimeIsAccept {

    public Map<String, Object> execute(Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        String ids = String.valueOf(params.getOrDefault("ids", ""));
        // 通过外委订单执行明细id获取采购订单号
        RecordSet wwddzxmx = new RecordSet();
        String zxmxSql = "select cgddh from uf_rlwwddzxmx where FIND_IN_SET(id, ?) > 0";
        wwddzxmx.executeQuery(zxmxSql, ids);
        List<String> cgddhs = new ArrayList<>();
        while (wwddzxmx.next()){
            String cgddh = wwddzxmx.getString("cgddh");
            if (StringUtils.isNotEmpty(cgddh)){
                cgddhs.add(cgddh);
            }
        }
        RecordSet rbtb = new RecordSet();
        String gsSql = "select spzt from formtable_main_637 where where FIND_IN_SET(cgddhllan, ?) > 0";
        rbtb.executeQuery(gsSql, cgddhs);
        List<String> spzts = new ArrayList<>();
        while (rbtb.next()){
            String spzt = rbtb.getString("spzt");
            if (StringUtils.isNotEmpty(spzt)){
                spzts.add(spzt);
            }
        }
        boolean isAccept = spzts.stream().filter(spzt -> !StringUtils.equalsIgnoreCase("2", spzt)).count() <= 0;
        result.put("isAccept", isAccept);
        result.put("code", "200");
        return result;
    }
}
