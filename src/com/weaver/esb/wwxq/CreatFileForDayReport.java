package com.weaver.esb.wwxq;

import com.aspose.cells.*;
import com.engine.integration.util.StringUtils;
import weaver.conn.RecordSet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 将日报的详细信息做成文件
 */
public class CreatFileForDayReport {

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String ddhhh = String.valueOf(params.getOrDefault("ddhhh", ""));
        if (StringUtils.isEmpty(ddhhh)){
            result.put("code", "200");
            return result;
        }
        try {
            RecordSet rs = new RecordSet();
            String rbSql = "select a.gh as gh, a.xm as xm, b.gs as gs, a.gysbh as gysbh, b.cgddhhh as cgddhhh, a.gysmc as gysmc, b.xmbhn as xmbhn, b.xmmc as xmmc, b.rwbh as rwbh,b.rwmcn as rwmcn from uf_ygrbcp as a left join uf_ygrbcp_dt1 as b on a.id=b.mainid where b.cgddhhh= ? and (b.spzt=2 or nullif(b.spzt, '') is null) and (b.zt=0 or nullif(b.zt, '') is null)";
            rs.executeQuery(rbSql, ddhhh);

            Workbook workbook = new Workbook();
            Worksheet worksheet = workbook.getWorksheets().get(0);
            Cells cells = worksheet.getCells();

            Style headerStyle = workbook.createStyle();
            Font headerFont = headerStyle.getFont();
            headerFont.setBold(true);
            headerFont.setSize(11);
            headerStyle.setHorizontalAlignment(com.aspose.cells.TextAlignmentType.CENTER);

            cells.get("A1").putValue("工号");
            cells.get("B1").putValue("姓名");
            cells.get("C1").putValue("工时");
            cells.get("D1").putValue("供应商编号");
            cells.get("E1").putValue("采购订单号-行号");
            cells.get("F1").putValue("供应商名称");
            cells.get("G1").putValue("项目编号");
            cells.get("H1").putValue("项目名称");
            cells.get("I1").putValue("任务编号");
            cells.get("J1").putValue("任务名称");

            for (int i = 0; i < 10; i++) {
                Cell cell = cells.get(0, i);
                cell.setStyle(headerStyle);
            }

            int rowIndex = 1;
            while (rs.next()) {
                cells.get(rowIndex, 0).putValue(rs.getString("gh"));
                cells.get(rowIndex, 1).putValue(rs.getString("xm"));
                cells.get(rowIndex, 2).putValue(rs.getString("gs"));
                cells.get(rowIndex, 3).putValue(rs.getString("gysbh"));
                cells.get(rowIndex, 4).putValue(rs.getString("cgddhhh"));
                cells.get(rowIndex, 5).putValue(rs.getString("gysmc"));
                cells.get(rowIndex, 6).putValue(rs.getString("xmbhn"));
                cells.get(rowIndex, 7).putValue(rs.getString("xmmc"));
                cells.get(rowIndex, 8).putValue(rs.getString("rwbh"));
                cells.get(rowIndex, 9).putValue(rs.getString("rwmcn"));
                rowIndex++;
            }

            worksheet.autoFitColumns();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "日报明细_" + ddhhh + "_" + sdf.format(new Date()) + ".xlsx";
            String filePath = "/opt/weaver/ecology/filestore/" + fileName;

            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            workbook.save(filePath);

            result.put("code", "200");
            result.put("message", "文件生成成功");
            result.put("filePath", filePath);
            result.put("fileName", fileName);
            result.put("rowCount", rowIndex - 1);

        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "文件生成失败: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

}
