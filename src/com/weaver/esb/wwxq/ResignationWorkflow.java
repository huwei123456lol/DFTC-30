package com.weaver.esb.wwxq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.rlww.workflow.webservices.*;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;

import java.io.*;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 离职申请流程
 */
public class ResignationWorkflow {

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String,Object> ret = new HashMap<>();
        // 获取当天
        String currentDate = getCurrentDate();
        // 填充临时工作证申请表主表字段
        JSONObject mainField = new JSONObject();
        // 人员类别
        mainField.put("rylb","2");
        // 申请人 (接待联系人)
        mainField.put("sqrjdlxr", String.valueOf(params.getOrDefault("xqr", "1")));
        // 员工编号
        mainField.put("ygbh", String.valueOf(params.getOrDefault("ygbh", "8328067")));
        // 联系方式
        mainField.put("lxfs", String.valueOf(params.getOrDefault("xqrdh", "13163393775")));
        // 所属单位
        mainField.put("ssdw", "1522");
        // 所在部门
        mainField.put("szbm", "78626");
        // 申请日期
        mainField.put("sqrq", String.valueOf(params.getOrDefault("sqrq", currentDate)));
        // 申请类型 根据后台判断删除
        mainField.put("sqlx", String.valueOf(params.getOrDefault("sqlx", "2")));
        // 工作证类别
        mainField.put("gzzlb", "1");
        // 是否正式员工
        mainField.put("sfzsyg", "1");
        // 数据来源 0 商务管理系统 1 COS系统
        mainField.put("sjly", "0");
        // 项目名称
        mainField.put("xmmc", String.valueOf(params.getOrDefault("xmmc", "")));
        // 合同编号
        mainField.put("htbh01", String.valueOf(params.getOrDefault("htbh", "")));
        // 合同名称
        mainField.put("htmc", String.valueOf(params.getOrDefault("htmc", "")));
        // 供应商编码
        String gysbm = String.valueOf(params.getOrDefault("gysbm", ""));
        if (StringUtils.isNotEmpty(gysbm)){
            RecordSetDataSource cosces = new RecordSetDataSource("coscs");
            String gysSql = "select id from uf_cggl_gysjbxxb where gysbm like " + "'" + gysbm + "'";
            cosces.executeSql(gysSql);
            if (cosces.next()){
                mainField.put("gysbm", cosces.getString("id"));
            }
        }
        // 外来人员所属公司/单位
        mainField.put("wlryssgsdw", String.valueOf(params.getOrDefault("gysmc", "")));
        // 进入总院日期
        mainField.put("jrjszxyqrq", String.valueOf(params.getOrDefault("jrzyrq", "")));
        // 预计离开日期
        mainField.put("yjlkrq", String.valueOf(params.getOrDefault("xmlyjzrq", "")));
        // 来访期限
        mainField.put("lfqx", String.valueOf(params.getOrDefault("lfqx", "")));
        // 外来人员人数
        mainField.put("wlryrs", String.valueOf(params.getOrDefault("wlryrs", "")));
        // 工作区域
        mainField.put("gzqy", String.valueOf(params.getOrDefault("gzqys", "")));
        // 选择科室负责人/对应项目副总师审批
        mainField.put("xzksfzrdyxmfzrsp", "17507");
        // 临时工作证申请表明细表列表
        JSONArray detailList = new JSONArray();
        // 填充临时工作证申请表明细表1字段
        JSONArray detail1s = new JSONArray();
        detailList.add(detail1s);
        // 填充临时工作证申请表明细表2字段
        JSONArray detail2s = new JSONArray();
        detailList.add(detail2s);
        // 填充临时工作证申请表明细表3字段
        JSONArray detail3s = new JSONArray();
        detailList.add(detail3s);
        // 获取数据
        String detail1Param = String.valueOf(params.getOrDefault("detail1", ""));
        if (StringUtils.isNotEmpty(detail1Param)){
            // 填充临时工作证申请表明细表1字段
            JSONArray detail4s = JSONArray.parseArray(detail1Param);
            detailList.add(detail4s);
        }
        // 附件字段填充
        JSONObject attachmentJo = new JSONObject();
        //填充相关附件字段
        // 身份证/护照号（附件）
//		JSONArray sfzhjoArr = new JSONArray();
//		File sfzhzhfj = new File("/Users/hanjun/Downloads/护照号.pdf");
//		// 照片（1寸）
//		JSONArray zp1cjoArr = new JSONArray();
//		File zp1c = new File("/Users/hanjun/Downloads/照片.pdf");
//		JSONObject sfzhzhfjjo = new JSONObject();
//		sfzhzhfjjo.put("fileName","base64:护照号.pdf");
//		sfzhzhfjjo.put("fileBase64",fileBase64(sfzhzhfj));
//		sfzhjoArr.add(sfzhzhfjjo);
//		JSONObject zp1cjo = new JSONObject();
//		zp1cjo.put("fileName","base64:照片.pdf");
//		zp1cjo.put("fileBase64",fileBase64(zp1c));
//		zp1cjoArr.add(zp1cjo);
//		attachmentJo.put("sfzhzhfj",sfzhjoArr);
//		attachmentJo.put("zp1c",zp1cjoArr);
        String requestId = projectApplication(mainField,attachmentJo,detailList);
        scheduleWithTimer(requestId, detail1Param);
        ret.put("code", "100");
        ret.put("requestId", requestId);
        return ret;
    }

    // 使用定时器方式实现
    public static void scheduleWithTimer(String requestId, String detail1Param) {
        System.out.println("使用Timer启动定时器监控流程状态，requestId: " + requestId);

        final Timer timer = new Timer();
        final int[] checkCount = {0};
        final int maxChecks = 60;
        JSONArray detail1 = JSONArray.parseArray(detail1Param);
        String sfzhzh = detail1.stream().map(item -> "'" + ((JSONObject) item).getString("sfzhzh") + "'").collect(Collectors.joining(","));
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (checkCount[0] > maxChecks) {
                    timer.cancel();
                }
                checkCount[0]++;
                String rczt = "1";
                String status = getProcessStatus(requestId);
                if ("3".equals(status)) {
                    RecordSet rs = new RecordSet();
                    String updatesql = "update uf_jlk set gh = ?,mm = ?, rczt = ? where sfzhhzh in (?)";
                    rs.executeUpdate(updatesql, "", "", rczt, sfzhzh);
                    // 取消定时器
                    timer.cancel();
                }
            }
        };
        // 立即执行，然后一天执行一次
        long period = 1000 * 60 * 60 * 24;
        timer.scheduleAtFixedRate(task, 0, period);
    }
    // 根据请求ID获取流程状态
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

    public static String projectApplication(JSONObject fieldInfo,JSONObject attachmentInfo,JSONArray detailInfo){

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

        for (int i = 0; i < detailInfo.size(); i++) {
            JSONArray jrr = detailInfo.getJSONArray(i);

            //行数据
            WorkflowRequestTableRecord[] detailTableRecord = new WorkflowRequestTableRecord[jrr.size()];
            for (int k = 0; k < jrr.size(); k++) {
                JSONObject detailJo = jrr.getJSONObject(k);

                int fieldCount = detailJo.size();
                WorkflowRequestTableField[] detailFields = new WorkflowRequestTableField[fieldCount];
                for (Map.Entry entry : detailJo.entrySet()) {

                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();

                    WorkflowRequestTableField wfField = new WorkflowRequestTableField();
                    wfField.setFieldName(key);
                    wfField.setFieldValue(value);
                    wfField.setView(true);
                    wfField.setEdit(true);
                    fieldCount--;
                    detailFields[fieldCount] =  wfField;
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
        wbi.setWorkflowId("222566");
        //wbi.setWorkflowId("7");

        // 流程基本信息
        WorkflowRequestInfo wri = new WorkflowRequestInfo();
        // 创建人id
        wri.setCreatorId("1");
        // 0 正常，1重要，2紧急
        wri.setRequestLevel("0");
        // 流程标题
        wri.setRequestName("东风汽车集团股份有限公司研发总院园区办理离场申请");
        // 添加主字段数据
        wri.setWorkflowMainTableInfo(wmi);
        wri.setWorkflowBaseInfo(wbi);
        wri.setWorkflowDetailTableInfos(workflowDetailTableInfo);
        WorkflowServicePortTypeProxy workflowServicePortTypeProxy = new WorkflowServicePortTypeProxy();
        String requestid = null;
        try {
            requestid = workflowServicePortTypeProxy.doCreateWorkflowRequest(
                    wri, Integer.parseInt("1"));
            System.out.println("[SendlcUtil]requestid="+requestid);
        } catch (NumberFormatException | RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("创建流程结束");
        return requestid;

    }


    public static String fileBase64(File file){
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes=new byte[(int)file.length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
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


    public static WorkflowRequestInfo getRequestInfo(){
        WorkflowServicePortTypeProxy workflowServicePortTypeProxy = new WorkflowServicePortTypeProxy();
        WorkflowRequestInfo rsinfo = null;
        try {
            rsinfo = workflowServicePortTypeProxy.getWorkflowRequest(95153,1,0);
            System.out.println(rsinfo.getLastOperateTime());
            WorkflowRequestLog[] requestLogs = rsinfo.getWorkflowRequestLogs();
            System.out.println(requestLogs.length);
            for (WorkflowRequestLog requestLog : requestLogs) {
                System.out.println(requestLog.getOperatorName() + " ==== " + requestLog.getOperateType());
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return rsinfo;
    }

    public static String submitWorkflow(JSONObject fieldInfo,JSONObject attachmentInfo,JSONArray detailInfo,int operator,int requestid,String remark ){

        System.out.println("提交流程开始");

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

        for (Map.Entry entry : attachmentInfo.entrySet()) {

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

        for (int i = 0; i < detailInfo.size(); i++) {
            JSONArray jrr = detailInfo.getJSONArray(i);

            //行数据
            WorkflowRequestTableRecord[] detailTableRecord = new WorkflowRequestTableRecord[jrr.size()];
            for (int k = 0; k < jrr.size(); k++) {
                JSONObject detailJo = jrr.getJSONObject(k);

                int fieldCount = detailJo.size();
                WorkflowRequestTableField[] detailFields = new WorkflowRequestTableField[fieldCount];
                for (Map.Entry entry : detailJo.entrySet()) {

                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();

                    WorkflowRequestTableField wfField = new WorkflowRequestTableField();
                    wfField.setFieldName(key);
                    wfField.setFieldValue(value);
                    wfField.setView(true);
                    wfField.setEdit(true);
                    fieldCount--;
                    detailFields[fieldCount] =  wfField;
                }
                detailTableRecord[k] = new WorkflowRequestTableRecord();
                detailTableRecord[k].setWorkflowRequestTableFields(detailFields);
            }
            workflowDetailTableInfo[i] = new WorkflowDetailTableInfo();
            workflowDetailTableInfo[i].setWorkflowRequestTableRecords(detailTableRecord);

        }



        String type = "submit";
        // 添加工作流id
        WorkflowBaseInfo wbi = new WorkflowBaseInfo();
        // workflowid 流程接口
        wbi.setWorkflowId("222566");
        // 流程基本信息
        WorkflowRequestInfo wri = new WorkflowRequestInfo();
        // 创建人id
        wri.setCreatorId("1");
        // 0 正常，1重要，2紧急
        wri.setRequestLevel("0");
        // 流程标题
        wri.setRequestName("东风汽车集团股份有限公司研发总院园区办理临时工作证申请");
        // 设置流程requestid
        wri.setRequestId(String.valueOf(requestid));
        //显示
        wri.setCanView(true);
        //可编辑
        wri.setCanEdit(true);
        // 添加主字段数据
        wri.setWorkflowMainTableInfo(wmi);
        wri.setWorkflowBaseInfo(wbi);
        wri.setWorkflowDetailTableInfos(workflowDetailTableInfo);
        WorkflowServicePortTypeProxy workflowServicePortTypeProxy = new WorkflowServicePortTypeProxy();
        String response = "";
        try {
            response = workflowServicePortTypeProxy.submitWorkflowRequest(wri, requestid, operator, type, remark);
            System.out.println("[submitWorkflow]response="+response);
        } catch (NumberFormatException | RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("提交流程结束");
        return response;

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
