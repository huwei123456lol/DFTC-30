package com.weaver.esb.wwxq;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.cells.*;
import com.weaver.rlww.workflow.webservices.*;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.docs.docs.DocImageManager;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

import java.io.*;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hw
 * @date 2026/06/03
 * @description 外委项目验收
 */
public class WWProjectAcceptance {

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String,Object> ret = new HashMap<>();
        // 填充合同验收主表字段
        JSONObject mainField = new JSONObject();
        // 申请编号
        String sqbh = String.valueOf(params.getOrDefault("sqbh", ""));
        mainField.put("sqbh", sqbh);
        // 申请人
        String sqr = String.valueOf(params.getOrDefault("sqr", "1"));
        if (StringUtils.isEmpty(sqr)){
            sqr = "1";
        }
        mainField.put("sqr", sqr);
        // 申请部门
//        String sqbm = String.valueOf(params.getOrDefault("sqbm", ""));
//        mainField.put("sqbm", "62047");
        // 申请日期
        String sqrq = String.valueOf(params.getOrDefault("sqrq", ""));
        mainField.put("sqrq", sqrq);
        // 申请人联系方式
        String sqrlxfs = String.valueOf(params.getOrDefault("sqrlxfs", ""));
        mainField.put("lxfs", sqrlxfs);
        // 验收申请类型
        String yssqlx = String.valueOf(params.getOrDefault("yssqlx", ""));
        mainField.put("yssqlx", yssqlx);
        // 采购业务分类
//        mainField.put("cgywfl", "42");
        // 采购项目名称
//        String cgxmmc = String.valueOf(params.getOrDefault("cgxmmc", ""));
        // 通过sql语句将文本转换为ID
//        RecordSetDataSource cosces = new RecordSetDataSource("coscs");
//        String cgxqSql = "select id,cgxqmc from uf_cgxqjbxxb where cgxqmc like '" + cgxmmc + "'";
//        cosces.executeSql(cgxqSql);
//        if (cosces.next()){
//            ret.put("cgxmmc", cosces.getString("id"));
//            mainField.put("cgxmmc", cosces.getString("id"));
//        }
        // 采购计划号
//        String cgjhh = String.valueOf(params.getOrDefault("cgjhh", ""));
//        mainField.put("cgjhh", cgjhh);
        // 合同编号
        String htbh = String.valueOf(params.getOrDefault("htbh", ""));
        mainField.put("htbhn", htbh);
        // 合同金额（元）
//        String htjey = String.valueOf(params.getOrDefault("htjey", ""));
//        mainField.put("htjey", htjey);
        // 合同履约开始时间
        String htlykssj = String.valueOf(params.getOrDefault("htlykssj", ""));
        mainField.put("htlykssj", htlykssj);
        // 是否等效物抵偿
        String sfydxwdc = String.valueOf(params.getOrDefault("sfydxwdc", ""));
        mainField.put("sfdxwdc", sfydxwdc);
        // 履约责任人
        String lyzrr = String.valueOf(params.getOrDefault("lyzrr", ""));
        mainField.put("lyzrr", lyzrr);
        // 是否交付延期
        String sfjfyq = String.valueOf(params.getOrDefault("sfjfyq", ""));
        mainField.put("sfyq", sfjfyq);
        // 是否涉及乙方延期赔偿
        String sfsjyfyqpc = String.valueOf(params.getOrDefault("sfsjyfyqpc", ""));
        mainField.put("sfsjyfyqpc", sfsjyfyqpc);
        // 选择订单号
        String cgddh = String.valueOf(params.getOrDefault("cgddh", ""));
        // 获取采购订单编码
        RecordSet rs = new RecordSet();
        rs.executeQuery("select id,ddbh from uf_cgddb where id=?", cgddh);
        if (rs.next()){
            String ddbh = rs.getString("ddbh");
            RecordSetDataSource cosces = new RecordSetDataSource("coscs");
            cosces.executeSql("select id,ddbh from uf_cgddb where ddbh like '" + ddbh + "'");
            if (cosces.next()) {
                mainField.put("xzddh", cosces.getString("id"));
            }
        }
        // 合同签约单位编码
        String htqydwbm = String.valueOf(params.getOrDefault("htqydwbm", ""));
        mainField.put("gysbm1", htqydwbm);
        // 采购申请单号
        String cgsqdh = String.valueOf(params.getOrDefault("cgsqdh", ""));
        mainField.put("cgsqdh1", cgsqdh);
        // 对方联系人及方式
        String dflxrdh = String.valueOf(params.getOrDefault("dflxrdh", ""));
        mainField.put("dflxrjfs", dflxrdh);
        // 对方签约单位
        String dfqydw = String.valueOf(params.getOrDefault("dfqydw", ""));
        mainField.put("htqydwbm", dfqydw);
        // 本次验收金额（元）
        String bcysjehsy = String.valueOf(params.getOrDefault("bcysjehsy", ""));
        mainField.put("bcysjey", bcysjehsy);
        // 本次付款金额（元）
        String bcfkjehsy = String.valueOf(params.getOrDefault("bcfkjehsy", ""));
        mainField.put("bcfkjey", bcfkjehsy);
        // 验收类型
        String yslx = String.valueOf(params.getOrDefault("yslx", ""));
        mainField.put("yslx", yslx);
        // 验收节点
        String ysjd = String.valueOf(params.getOrDefault("ysjd", ""));
        mainField.put("ysjdnew", ysjd);
        // 合同终验收时间
//        String htzyssj = String.valueOf(params.getOrDefault("htzyssj", ""));
//        mainField.put("htzyssj", htzyssj);
        // 是否超期
//        String sfcq = String.valueOf(params.getOrDefault("sfcq", ""));
//        mainField.put("tj", sfcq);
        // 验收审批类型
        String yssplx = String.valueOf(params.getOrDefault("yssplx", ""));
        mainField.put("yssplx", yssplx);
        // 补充说明
        String bcsm = String.valueOf(params.getOrDefault("bcsm", ""));
        mainField.put("bcsm", bcsm);
        // 合同履约状态
        String htlyzt = String.valueOf(params.getOrDefault("htlyzt", ""));
        mainField.put("htlyzt", htlyzt);
        // 合同履约阶段
        String htlyjd = String.valueOf(params.getOrDefault("htlyjd", ""));
        mainField.put("htlyjd", htlyjd);
        // 合同变更
        String htbg = String.valueOf(params.getOrDefault("htbg", ""));
        mainField.put("htbg", htbg);
        // 合同是否有扣款
        String htsfykk = String.valueOf(params.getOrDefault("htsfykk", ""));
        mainField.put("htsfykk", htsfykk);
        // 验收单PDF
        String ysd = String.valueOf(params.getOrDefault("ysd", ""));
        /**
         * 合同验收明细表
         * 只需要传明细表4 明细表5
         */
        JSONArray detailList = new JSONArray();
        // 明细表1
        String detail2Param = String.valueOf(params.getOrDefault("detail2", ""));
        JSONArray tmp;
        if (StringUtils.isNotEmpty(detail2Param)){
            tmp = JSONArray.parseArray(detail2Param);
        } else {
            tmp = new JSONArray();
        }
        String ddxh = tmp.stream().map(detail -> {
            JSONObject data = (JSONObject) detail;
            return data.getString("ddxh");
        }).collect(Collectors.joining(","));
        Map<String, String> fileObj = createFileForDayReport(ddxh, ret, sqbh);

        String ysjfwzmwjFileName = "";
        String ysjfwzmwjBase64 = "";
        String ysqrwjFileName = "";
        String ysqrwjBase64 = "";

        if (fileObj.containsKey("filePath") && StringUtils.isNotEmpty(fileObj.get("filePath"))){
            String filePath = fileObj.get("filePath");
            String fileName = fileObj.get("fileName");
            File ysjfwzmwj = new File(filePath);
            ysjfwzmwjFileName = fileName;
            ysjfwzmwjBase64 = fileBase64(ysjfwzmwj, ret, fileName);
        }
        if (StringUtils.isNotEmpty(ysd)) {
            String[] ysds = ysd.split(",");
            RecordSet ysdRs = new RecordSet();
            StringBuilder ysdSql = new StringBuilder();
            ysdSql.append("SELECT imgfile.FILEREALPATH AS filePath,imgfile.imagefilename AS filename,imgfile.iszip AS iszip FROM docdetail docdt LEFT JOIN docimagefile docimg ON docdt.id = docimg.docid LEFT JOIN imagefile imgfile ON imgfile.IMAGEFILEID = docimg.IMAGEFILEID WHERE docdt.id in (");
            String[] param = new String[ysds.length];
            for(int i =0; i < ysds.length; i++){
                param[i] = ysds[i].trim();
                if (i > 0){
                    ysdSql.append(",");
                }
                ysdSql.append("?");
            }
            ysdSql.append(") ORDER BY COALESCE(docimg.isextfile, 0) ASC, docimg.versionid DESC");
            boolean ysdExecute = ysdRs.executeQuery(ysdSql.toString(), param);
            ret.put("pdf", ysdExecute);  // false
            if (ysdRs.next()) {
                String filePath = ysdRs.getString("filePath");
                String fileName = ysdRs.getString("filename");
                File ysqrwj = new File(filePath);
                ysqrwjFileName = fileName;
                ysqrwjBase64 = fileBase64(ysqrwj, ret, fileName);
            }
        }
        JSONArray detail1s = new JSONArray();
        String ysrs = tmp.stream().map(detail -> {
            JSONObject data = (JSONObject) detail;
            return data.getString("ysr");
        }).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(ysjfwzmwjFileName) || StringUtils.isNotEmpty(ysqrwjFileName)) {
            JSONObject detail1Item = new JSONObject();
            if (StringUtils.isNotEmpty(ysjfwzmwjFileName)) {
                detail1Item.put("ysjfwzmwj", ysjfwzmwjFileName + "|" + ysjfwzmwjBase64);
            }
            if (StringUtils.isNotEmpty(ysqrwjFileName)) {
                detail1Item.put("ysqrwj", ysqrwjFileName + "|" + ysqrwjBase64);
            }
            if (StringUtils.isNotEmpty(ysrs)){
                detail1Item.put("ysr", ysrs);
            }
            detail1s.add(detail1Item);
        }
        detailList.add(detail1s);
        // 明细表2
        JSONArray detail2s = new JSONArray();
        detailList.add(detail2s);
        // 明细表3
        JSONArray detail3s = new JSONArray();
        detailList.add(detail3s);
        String detail1Param = String.valueOf(params.getOrDefault("detail1", ""));
        JSONArray detail4s;
        if (StringUtils.isNotEmpty(detail1Param)){
//            detail4s = JSONArray.parseArray(detail1Param);
            detail4s = new JSONArray();
        } else {
            detail4s = new JSONArray();
        }
        detailList.add(detail4s);
        JSONArray detail5s;
        if (StringUtils.isNotEmpty(detail2Param)){
            detail5s = JSONArray.parseArray(detail2Param);
        } else {
            detail5s = new JSONArray();
        }
        detailList.add(detail5s);
        JSONObject attachmentJo = new JSONObject();
        // 从detail2中过滤出订单号行号  attachmentJo 主要用于处理主表中的附件
//        String ddxh = detail5s.stream().map(detail -> {
//            JSONObject data = (JSONObject) detail;
//            return data.getString("ddxh");
//        }).collect(Collectors.joining(","));
//        ret.put("ddhhh", ddxh);
//        Map<String, String> fileObj = createFileForDayReport(ddxh, ret);
//        JSONArray ysjfwzmwjArr = new JSONArray();
//        if (fileObj.containsKey("filePath") && StringUtils.isNotEmpty(fileObj.get("filePath"))){
//            // 验收交付物证明文件
//            String filePath = fileObj.get("filePath");
//            String fileName = fileObj.get("fileName");
//            File ysjfwzmwj = new File(filePath);
//            JSONObject ysjfwzmwjObj = new JSONObject();
//            ysjfwzmwjObj.put("fileName", "base64:" + fileName);
//            ysjfwzmwjObj.put("fileBase64", fileBase64(ysjfwzmwj, ret, fileName));
//            ysjfwzmwjArr.add(ysjfwzmwjObj);
//        }
//        // 验收单PDF
//        JSONArray ysdArr = new JSONArray();
//        if (StringUtils.isNotEmpty(ysd)) {
//            RecordSet ysdRs = new RecordSet();
//            String ysdSql = "SELECT imgfile.FILEREALPATH AS filePath,\n" +
//                    "       imgfile.imagefilename AS filename,\n" +
//                    "       imgfile.iszip AS iszip \n" +
//                    "FROM docdetail docdt \n" +
//                    "LEFT JOIN docimagefile docimg ON docdt.id = docimg.docid \n" +
//                    "LEFT JOIN imagefile imgfile ON imgfile.IMAGEFILEID = docimg.IMAGEFILEID \n" +
//                    "WHERE docdt.id = '?' \n" +
//                    "ORDER BY COALESCE(docimg.isextfile, 0) ASC, \n" +
//                    "         docimg.versionid DESC";
//            ysdRs.execute(ysdSql, ysd);
//            if (ysdRs.next()) {
//                // 验收确认文件
//                String filePath = ysdRs.getString("filePath");
//                String fileName = ysdRs.getString("filename");
//                File ysqrwj = new File(filePath);
//                JSONObject ysqrwjObj = new JSONObject();
//                ysqrwjObj.put("fileName", "base64:" + fileName);
//                ysqrwjObj.put("fileBase64", fileBase64(ysqrwj, ret, fileName));
//                ysdArr.add(ysqrwjObj);
//            }
//        }
//        attachmentJo.put("ysjfwzmwj", ysjfwzmwjArr);
//        attachmentJo.put("ysqrwj", ysdArr);
        String requestId = projectApplication(mainField,attachmentJo,detailList, sqr, ret);
        scheduleWithTimer(requestId, sqbh);
        ret.put("requestId", requestId);
        if (StringUtils.isNotEmpty(requestId)){
            ret.put("code", "200");
        } else {
            ret.put("code", "400");
        }
        return ret;
    }

    public static String fileBase64(File file, Map<String,Object> ret, String fileName){
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes=new byte[(int)file.length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
            ret.put(fileName, base64);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "base64:" + base64;
    }

    /**
     * 创建员工日报工时详细文件
     */
    public static Map<String, String> createFileForDayReport(String ddhhh, Map<String,Object> tmp, String sqbh) {
        Map<String, String> ret = new HashMap<>();
        try {
            RecordSet rs = new RecordSet();
            String[] ddhhhArray = ddhhh.split(",");
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select a.gh as gh, a.xm as xm, b.gs as gs, a.gysbh as gysbh, b.cgddhhh as cgddhhh, a.gysmc as gysmc, b.xmbhn as xmbhn, b.xmmc as xmmc, b.rwbh as rwbh,b.rwmcn as rwmcn from uf_ygrbcp as a left join uf_ygrbcp_dt1 as b on a.id=b.mainid where b.cgddhhh in (");
            for (int i = 0; i < ddhhhArray.length; i++) {
                if (i > 0) {
                    sqlBuilder.append(",");
                }
                sqlBuilder.append("?");
            }
            sqlBuilder.append(") and (b.spzt=2 or b.spzt is null or b.spzt='') and (b.zt=0 or b.zt is null or b.zt='')");

            String rbSql = sqlBuilder.toString();

            Object[] params = new Object[ddhhhArray.length];
            for (int i = 0; i < ddhhhArray.length; i++) {
                params[i] = ddhhhArray[i].trim();
            }

            rs.executeQuery(rbSql, params);

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
            JSONObject xls = new JSONObject();
            int rowIndex = 1;
            while (rs.next()) {
                String gh = rs.getString("gh");
                cells.get(rowIndex, 0).putValue(gh);
                xls.put("gh",  gh);
                String xm = rs.getString("xm");
                cells.get(rowIndex, 1).putValue(xm);
                xls.put("xm",  xm);
                String gs = rs.getString("gs");
                cells.get(rowIndex, 2).putValue(gs);
                xls.put("gs",  gs);
                String gysbh = rs.getString("gysbh");
                cells.get(rowIndex, 3).putValue(gysbh);
                xls.put("gysbh",  gysbh);
                String cgddhhh = rs.getString("cgddhhh");
                cells.get(rowIndex, 4).putValue(cgddhhh);
                xls.put("cgddhhh",  cgddhhh);
                String gysmc = rs.getString("gysmc");
                cells.get(rowIndex, 5).putValue(gysmc);
                xls.put("gysmc",  gysmc);
                String xmbhn = rs.getString("xmbhn");
                cells.get(rowIndex, 6).putValue(xmbhn);
                xls.put("xmbhn",  xmbhn);
                String xmmc = rs.getString("xmmc");
                cells.get(rowIndex, 7).putValue(xmmc);
                xls.put("xmmc",  xmmc);
                String rwbh = rs.getString("rwbh");
                cells.get(rowIndex, 8).putValue(rwbh);
                xls.put("rwbh",  rwbh);
                String rwmcn = rs.getString("rwmcn");
                cells.get(rowIndex, 9).putValue(rwmcn);
                xls.put("rwmcn",  rwmcn);
                rowIndex++;
            }
//            tmp.put("xls", JSON.toJSONString(xls));
            worksheet.autoFitColumns();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "日报明细_" + ddhhh + "_" + sdf.format(new Date()) + ".xlsx";
            String filePath = "/opt/weaver/ecology/filestore/" + fileName;

            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            workbook.save(filePath);
            int docId = createWeaverDocument(file, fileName, tmp);
            if (ObjectUtil.isNotNull(docId) && docId > 0){
                RecordSet wwys = new RecordSet();
                String wwySql = "update formtable_main_642 set rbxxxxexcel = " + String.valueOf(docId) +" where sqbh = '" + sqbh + "'";
//                boolean update = wwys.executeQuery(wwySql, String.valueOf(docId), sqbh);
                  wwys.execute(wwySql);
//                tmp.put("updateStatus", update);
            }
            ret.put("filePath", filePath);
            ret.put("fileName", fileName);
            tmp.put("docId", String.valueOf(docId));
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            tmp.put("message", e.getMessage());
            return ret;
        }
    }

    private static int createWeaverDocument(File file, String fileName, Map<String,Object> tmp) {
        BaseBean bb = new BaseBean();
        int docId = 0;
        try {
            int imagefileid = new ImageFileIdUpdate().getImageFileNewId();
            bb.writeLog("[createWeaverDocument]生成的文件id为：" + imagefileid);
//            tmp.put("[createWeaverDocument]生成的文件id", String.valueOf(imagefileid));

            int createstatus = ImageFileUtil.createImageFile(
                    file.getPath(),
                    imagefileid,
                    fileName,
                    getFileSize(file)
            );
//            tmp.put("createDocumentStatus", String.valueOf(createstatus));
            bb.writeLog("[createWeaverDocument]上传图片状态为：" + createstatus);

            if (createstatus == -1) {
                bb.writeLog("[createWeaverDocument]上传文件到OA文档系统失败");
                return 0;
            }

            int seccategory = 2;
            int docOwner = 1;

            docId = createDocDetail(seccategory, fileName, docOwner, tmp);
            bb.writeLog("[createWeaverDocument]创建的文档ID为：" + docId);

            createDocImageFile(docId, imagefileid, fileName);

            createDocShare(seccategory, docOwner, docId, tmp);

            bb.writeLog("[createWeaverDocument]创建泛微文档成功，docId=" + docId);
        } catch (Exception e) {
            bb.writeLog("[createWeaverDocument]创建泛微文档异常", e);
            e.printStackTrace();
            tmp.put("documentMessage", "[createWeaverDocument]创建泛微文档异常: " + e.getMessage());
        }
        return docId;
    }

    private static void createDocShare(int seccategoryId, int ownerId, int docid, Map<String,Object> tmp) {
        BaseBean bb = new BaseBean();
        bb.writeLog("======seccategoryId:" + seccategoryId + ",ownerId:" + ownerId + ",docid:" + docid);
        DocManager dm = new DocManager();
        dm.setUserid(ownerId);
        dm.setId(docid);
        dm.setDocCreaterType("1");
        dm.setSeccategory(seccategoryId);
        try {
            dm.AddShareInfo();
        } catch (Exception e) {
            bb.writeLog("======createDocShare异常", e);
            tmp.put("docShareMessage", "======createDocShare异常");
        }
        RecordSet rs = new RecordSet();
        rs.executeProc("Share_forDoc", "" + docid);
        dm.setUsertype("1");
        dm.setAboutCreaterShare(seccategoryId + "");
    }

    private static void createDocImageFile(int docid, int imagefileid, String filename) {
        DocImageManager imgManger = new DocImageManager();
        BaseBean bb = new BaseBean();
        imgManger.setDocid(docid);
        imgManger.setImagefileid(imagefileid);
        imgManger.setImagefilename(filename);
        imgManger.setIsextfile("1");
        String ext = getFileExt(filename);
        if (ext.equalsIgnoreCase("doc")) {
            imgManger.setDocfiletype("3");
        } else if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx")) {
            imgManger.setDocfiletype("4");
        } else if (ext.equalsIgnoreCase("ppt") || ext.equalsIgnoreCase("pptx")) {
            imgManger.setDocfiletype("5");
        } else if (ext.equalsIgnoreCase("wps")) {
            imgManger.setDocfiletype("6");
        } else if (ext.equalsIgnoreCase("docx")) {
            imgManger.setDocfiletype("7");
        } else if (ext.equalsIgnoreCase("et")) {
            imgManger.setDocfiletype("10");
        } else {
            imgManger.setDocfiletype("2");
        }
        imgManger.AddDocImageInfo();
        bb.writeLog("===创建docimagefile成功");
    }

    private static int createDocDetail(int seccategory, String filename, int docOwner, Map<String,Object> tmp) {
        DocManager dm = new DocManager();
        BaseBean bb = new BaseBean();
        int docid = 0;
        try {
            docid = dm.getNextDocId(new RecordSet());
//            tmp.put("docDetailId", String.valueOf(docid));
        } catch (Exception e) {
            e.printStackTrace();
            tmp.put("docDetailMessage", e.getMessage());
        }

        /**
         * rsp的值为 {}
         */
        Map<String, String> rsp = isOpenApproveWfByDocSeccategoryId(seccategory);
//        tmp.put("docDetailOpenApproveWf", JSON.toJSONString(rsp));
        dm.setId(docid);
        dm.setMaincategory(0);
        dm.setSubcategory(0);
        dm.setSeccategory(seccategory);
        dm.setDoclangurage(7);
        dm.setDocapprovable("");
        dm.setDocreplyable(rsp.get("replyable"));
        dm.setIsreply("");
        dm.setReplydocid(0);
        dm.setDocsubject(filename);
        dm.setDocpublishtype("");
        dm.setItemid(0);
        dm.setItemmaincategoryid(0);
        dm.setHrmresid(0);
        dm.setCrmid(0);
        dm.setProjectid(0);
        dm.setFinanceid(0);
        dm.setDoccreaterid(docOwner);

        ResourceComInfo hrc = null;
        try {
            hrc = new ResourceComInfo();
        } catch (Exception e1) {
            e1.printStackTrace();
            tmp.put("docDetailMessage", e1.getMessage());
        }
        dm.setDocdepartmentid(Util.getIntValue(hrc.getDepartmentID("" + docOwner)));

        Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String timeStr = new SimpleDateFormat("HH:mm:ss").format(date);
        dm.setDoccreatedate(dateStr);
        dm.setDoccreatetime(timeStr);
        dm.setDoclastmoduserid(docOwner);
        dm.setDoclastmoddate(dateStr);
        dm.setDoclastmodtime(timeStr);
        dm.setDocapproveuserid(0);
        dm.setDocapprovedate("");
        dm.setDocapprovetime("");
        dm.setDocarchiveuserid(docOwner);
        dm.setDocarchivedate("");
        dm.setDocarchivetime("");
        dm.setDocstatus("1");
        dm.setParentids(docid + "");
        dm.setAssetid(0);
        dm.setOwnerid(docOwner);
        dm.setKeyword("");
        dm.setAccessorycount(1);
        dm.setReplaydoccount(0);
        dm.setDocCreaterType("1");
        dm.setDocType(1);
        dm.setCanCopy("1");
        dm.setCanRemind("1");
        dm.setOrderable(rsp.get("orderable"));
        dm.setDocextendname(getFileExt(filename));
        dm.setDocCode("");
        dm.setDocEdition(-1);
        dm.setDocEditionId(-1);
        dm.setIsHistory(0);
        dm.setApproveType(0);
        dm.setMainDoc(docid);
        String readoptercanprint = rsp.get("readoptercanprint");
        dm.setReadOpterCanPrint("".equals(readoptercanprint) ? 0 : Integer.valueOf(readoptercanprint));
        dm.setDocValidUserId(docOwner);
        dm.setDocValidDate(dateStr);
        dm.setDocValidTime(timeStr);
        dm.setInvalidationDate("");
        dm.setDocCreaterType("1");
        dm.setDocLastModUserType("1");
        dm.setDocApproveUserType("");
        dm.setDocValidUserType("");
        dm.setDocInvalUserType("");
        dm.setDocArchiveUserType("");
        dm.setDocCancelUserType("");
        dm.setDocPubUserType("");
        dm.setDocCancelUserType("");
        dm.setDocPubUserType("");
        dm.setDocReopenUserType("");
        dm.setOwnerType("1");
        dm.setDoccontent("");

        try {
            dm.AddDocInfo();
            bb.writeLog("===创建docdetail成功");
        } catch (Exception e) {
            bb.writeLog("===创建docdetail异常", e);
            tmp.put("docDetailMessage", "===创建docdetail异常: " + e.getMessage());
        }
        return docid;
    }

    private static String getFileExt(String file) {
        if (file == null || file.trim().equals("")) {
            return "";
        } else {
            int idx = file.lastIndexOf(".");
            if (idx == -1) {
                return "";
            } else {
                if (idx + 1 >= file.length()) {
                    return "";
                } else {
                    return file.substring(idx + 1);
                }
            }
        }
    }
    private static Map<String, String> isOpenApproveWfByDocSeccategoryId(int seccategory) {
        RecordSet rs = new RecordSet();
        BaseBean bb = new BaseBean();
        Map<String, String> rsp = new HashMap<>();
        rs.execute("select isOpenApproveWf,replyable,orderable,readoptercanprint from docseccategory where id = " + seccategory);
        if (rs.next()) {
            rsp.put("isOpenApproveWf", rs.getString("isOpenApproveWf"));
            rsp.put("replyable", rs.getString("replyable"));
            rsp.put("orderable", rs.getString("orderable"));
            rsp.put("readoptercanprint", rs.getString("readoptercanprint"));
            bb.writeLog("======查询目录信息赋值");
            return rsp;
        }
        return rsp;
    }

    private static int getFileSize(File file) {
        String filesize = "0";
        DecimalFormat df = new DecimalFormat("#.##");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            filesize = df.format((double) ((double) fis.available()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (filesize != null && filesize.contains(".")) {
            filesize = filesize.split("\\.")[0];
        }
        return Integer.parseInt(filesize);
    }

    public static void scheduleWithTimer(String requestId, String sqbh) {

        final Timer timer = new Timer();
        final int[] checkCount = {0};
        final int maxChecks = 60;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (checkCount[0] > maxChecks) {
                    timer.cancel();
                }
                checkCount[0]++;
                String status = getProcessStatus(requestId);
                // 通过申请编号回显状态
                RecordSet rs = new RecordSet();
                String sql = "update uf_wwysjlb set coshtyslczt = ? where sqbh = ?";
                rs.executeUpdate(sql, status, sqbh);
                // 当状态为归档时，取消定时器
                if ("3".equals(status)) {
                    // 取消定时器
                    timer.cancel();
                }
            }
        };
        // 立即执行，然后一个小时执行一次
        long period = 1000 * 60 * 60;
        timer.scheduleAtFixedRate(task, 1000 * 60 * 30 , period);
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

    public static String projectApplication(JSONObject fieldInfo,JSONObject attachmentInfo,JSONArray detailInfo, String sqr, Map<String,Object> ret){

        System.out.println("创建流程开始");


        //处理主字段
        List<WorkflowRequestTableField> fieldList = new ArrayList<>();

        for (Map.Entry entry : fieldInfo.entrySet()) {

            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            WorkflowRequestTableField wfField = new WorkflowRequestTableField();
            wfField.setFieldName(key);
            wfField.setFieldValue(value);
            wfField.setView(true);
            wfField.setEdit(true);
            fieldList.add(wfField);
        }

        //处理附件
        List<WorkflowRequestTableField> attachmentList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : attachmentInfo.entrySet()) {

            String key = (String) entry.getKey();
            JSONArray value = (JSONArray) entry.getValue();

            for (int i = 0; i < value.size(); i++) {
                WorkflowRequestTableField wfField = new WorkflowRequestTableField();
                wfField.setFieldName(key);
                wfField.setFieldType(value.getJSONObject(i).getString("fileName"));
                wfField.setFieldValue(value.getJSONObject(i).getString("fileBase64"));

                wfField.setView(true);
                wfField.setEdit(true);

                attachmentList.add(wfField);
            }

        }


        // 主字段只有一行数据
        WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[fieldList.size() + attachmentList.size()];
        //拼接附件
        fieldList.addAll(attachmentList);
        //主字段LIST 装载到主表字段数组中
        fieldList.toArray(wrti);

        WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];
        wrtri[0] = new WorkflowRequestTableRecord();
        wrtri[0].setWorkflowRequestTableFields(wrti);
        WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
        wmi.setRequestRecords(wrtri);


        //处理明细表
        WorkflowDetailTableInfo[] workflowDetailTableInfo = new WorkflowDetailTableInfo[detailInfo.size()];

//        for (int i = 0; i < detailInfo.size(); i++) {
//            JSONArray jrr = detailInfo.getJSONArray(i);
//
//            //行数据
//            WorkflowRequestTableRecord[] detailTableRecord = new WorkflowRequestTableRecord[jrr.size()];
//            for (int k = 0; k < jrr.size(); k++) {
//                JSONObject detailJo = jrr.getJSONObject(k);
//
//                int fieldCount = detailJo.size();
//                WorkflowRequestTableField[] detailFields = new WorkflowRequestTableField[fieldCount];
//                for (Map.Entry entry : detailJo.entrySet()) {
//
//                    String key = (String) entry.getKey();
//                    String value = (String) entry.getValue();
//
//                    WorkflowRequestTableField wfField = new WorkflowRequestTableField();
//                    wfField.setFieldName(key);
//                    wfField.setFieldValue(value);
//                    wfField.setView(true);
//                    wfField.setEdit(true);
//                    fieldCount--;
//                    detailFields[fieldCount] =  wfField;
//                }
//                detailTableRecord[k] = new WorkflowRequestTableRecord();
//                detailTableRecord[k].setWorkflowRequestTableFields(detailFields);
//            }
//            workflowDetailTableInfo[i] = new WorkflowDetailTableInfo();
//            workflowDetailTableInfo[i].setWorkflowRequestTableRecords(detailTableRecord);
//
//        }

        for (int i = 0; i < detailInfo.size(); i++) {
            JSONArray jrr = detailInfo.getJSONArray(i);

            WorkflowRequestTableRecord[] detailTableRecord = new WorkflowRequestTableRecord[jrr.size()];
            for (int k = 0; k < jrr.size(); k++) {
                JSONObject detailJo = jrr.getJSONObject(k);

                int fieldCount = detailJo.size();
                WorkflowRequestTableField[] detailFields = new WorkflowRequestTableField[fieldCount];
                int index = 0;
                for (Map.Entry entry : detailJo.entrySet()) {

                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();

                    WorkflowRequestTableField wfField = new WorkflowRequestTableField();
                    wfField.setFieldName(key);

                    if ("ysjfwzmwj".equals(key) || "ysqrwj".equals(key)) {
                        String[] parts = value.split("\\|", 2);
                        if (parts.length == 2) {
                            String fileName = parts[0];
                            String fileBase64 = parts[1];
                            wfField.setFieldType("base64:" + fileName);
                            wfField.setFieldValue(fileBase64);
                        } else {
                            wfField.setFieldValue(value);
                        }
                    } else {
                        wfField.setFieldValue(value);
                    }

                    wfField.setView(true);
                    wfField.setEdit(true);
                    detailFields[index] = wfField;
                    index++;
                }
                detailTableRecord[k] = new WorkflowRequestTableRecord();
                detailTableRecord[k].setWorkflowRequestTableFields(detailFields);
            }
            workflowDetailTableInfo[i] = new WorkflowDetailTableInfo();
            workflowDetailTableInfo[i].setWorkflowRequestTableRecords(detailTableRecord);

        }

        // 添加工作流id
        WorkflowBaseInfo wbi = new WorkflowBaseInfo();
        // workflowid 流程接口
        wbi.setWorkflowId("222671");

        //wbi.setWorkflowId("7");

        // 流程基本信息
        WorkflowRequestInfo wri = new WorkflowRequestInfo();
        // 创建人id
        wri.setCreatorId(sqr);
//        wri.setCreatorId("1");
        // 0 正常，1重要，2紧急
        wri.setRequestLevel("0");
        // 流程标题
        wri.setRequestName("东风汽车集团股份有限公司研发总院合同验收");
        wri.setIsnextflow("0");

        // 添加主字段数据
        wri.setWorkflowMainTableInfo(wmi);
        wri.setWorkflowBaseInfo(wbi);
        wri.setWorkflowDetailTableInfos(workflowDetailTableInfo);
        WorkflowServicePortTypeProxy workflowServicePortTypeProxy = new WorkflowServicePortTypeProxy();
        String requestid = null;
        try {
            requestid = workflowServicePortTypeProxy.doCreateWorkflowRequest(
                    wri, Integer.parseInt(sqr));
            System.out.println("[SendlcUtil]requestid="+requestid);
        } catch (NumberFormatException | RemoteException e) {
            e.printStackTrace();
            ret.put("error", JSON.toJSONString(e.getStackTrace()));
        }
//        String requestid = workflowServicePortTypeProxy.doCreateWorkflowRequest(
//                    wri, Integer.parseInt(sqr));
        System.out.println("创建流程结束");
        return requestid;

    }
}
