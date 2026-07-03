package weaver.trq.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.trq.service.CreateQzzxfwWorkflowService;
import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowServiceImpl;

/**
 * 创建前置咨询服务工作流程的对外接口
 * 
 * @author Alex.Du
 * 
 */
public class CreateQzzxfwWorkflowServiceImpl implements
		CreateQzzxfwWorkflowService {
	public String createQzzxfw(String data) {
		BaseBean log = new BaseBean();

		log.writeLog("进入创建前置咨询服务工作流程的对外接口");
		log.writeLog("获取到的参数内容是：" + data);

		// 将获取到的参数转换成JSON对象
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("将参数构建成JSON字符串出现异常:" + e.getMessage());
			return "2";
		}

		RecordSet rs = new RecordSet();

		// 申请人
		String sqr = "1224";
		// 申请人名称
		String sqrName = null;
		// 申请人编号
		String sqrCode = null;
		rs.execute("select lastname,workcode from hrmresource where id='" + sqr
				+ "'");
		if (rs.next()) {
			sqrName = rs.getString("lastname");
			sqrCode = rs.getString("workcode");
		}

		// 处理所属组别数据 1对应132，2对应108,3对应24，9对应78
		String zb = null;
		try {
			if (dataJson.getString("region").trim().equals("1")) {
				zb = "132";
			} else if (dataJson.getString("region").trim().equals("2")) {
				zb = "108";
			} else if (dataJson.getString("region").trim().equals("3")) {
				zb = "24";
			} else if (dataJson.getString("region").trim().equals("9")) {
				zb = "78";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog("处理组别数据时出现异常:" + e.getMessage());
			return "2";
		}

		// 处理所属部门(取组别的上级部门)
		String bm = null;
		rs.execute("select supdepid from hrmdepartment where id='" + zb + "'");
		if (rs.next()) {
			bm = rs.getString("supdepid");
		}

		// 处理地区码
		String dqm = null;
		try {
			rs.execute("select bm from uf_dq where id='"
					+ dataJson.getString("region") + "'");
			if (rs.next()) {
				dqm = rs.getString("bm");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("处理地区码数据时出现异常:" + e.getMessage());
		}

		// 主字段
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[16]; // 字段信息

		// 简要名称
		wrti[0] = new WorkflowRequestTableField();
		wrti[0].setFieldName("jybt");// 字段名
		try {
			wrti[0].setFieldValue(dataJson.getString("projectName"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[0].setView(true);// 字段是否可见
		wrti[0].setEdit(true);// 字段是否可编辑

		// 申请人
		wrti[1] = new WorkflowRequestTableField();
		wrti[1].setFieldName("sqr");// 字段名
		wrti[1].setFieldValue(sqr);// 字段值
		wrti[1].setView(true);// 字段是否可见
		wrti[1].setEdit(true);// 字段是否可编辑

		// 所属公司
		wrti[2] = new WorkflowRequestTableField();
		wrti[2].setFieldName("ssgs");// 字段名
		wrti[2].setFieldValue("1");// 字段值
		wrti[2].setView(true);// 字段是否可见
		wrti[2].setEdit(true);// 字段是否可编辑

		// 所属组别
		wrti[3] = new WorkflowRequestTableField();
		wrti[3].setFieldName("sszb");// 字段名
		wrti[3].setFieldValue(zb);// 字段值
		wrti[3].setView(true);// 字段是否可见
		wrti[3].setEdit(true);// 字段是否可编辑

		// 所属部门
		wrti[4] = new WorkflowRequestTableField();
		wrti[4].setFieldName("ssbm");// 字段名
		wrti[4].setFieldValue(bm);// 字段值
		wrti[4].setView(true);// 字段是否可见
		wrti[4].setEdit(true);// 字段是否可编辑

		// 申请时间
		wrti[5] = new WorkflowRequestTableField();
		wrti[5].setFieldName("sqsj");// 字段名
		wrti[5].setFieldValue(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));// 字段值
		wrti[5].setView(true);// 字段是否可见
		wrti[5].setEdit(true);// 字段是否可编辑

		// 员工编号
		wrti[6] = new WorkflowRequestTableField();
		wrti[6].setFieldName("ssbm");// 字段名
		wrti[6].setFieldValue(sqrCode);// 字段值
		wrti[6].setView(true);// 字段是否可见
		wrti[6].setEdit(true);// 字段是否可编辑

		// 安装类型
		wrti[7] = new WorkflowRequestTableField();
		wrti[7].setFieldName("azlx");// 字段名
		try {
			wrti[7].setFieldValue(dataJson.getString("installType"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[7].setView(true);// 字段是否可见
		wrti[7].setEdit(true);// 字段是否可编辑

		// 项目暂定名称
		wrti[8] = new WorkflowRequestTableField();
		wrti[8].setFieldName("sqxmmc");// 字段名
		try {
			wrti[8].setFieldValue(dataJson.getString("projectName"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[8].setView(true);// 字段是否可见
		wrti[8].setEdit(true);// 字段是否可编辑

		// 项目安装地址
		wrti[9] = new WorkflowRequestTableField();
		wrti[9].setFieldName("xmazdz");// 字段名
		try {
			wrti[9].setFieldValue(dataJson.getString("installAdd"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[9].setView(true);// 字段是否可见
		wrti[9].setEdit(true);// 字段是否可编辑

		// 客户姓名
		wrti[10] = new WorkflowRequestTableField();
		wrti[10].setFieldName("lxrxm");// 字段名
		try {
			wrti[10].setFieldValue(dataJson.getString("customerName"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[10].setView(true);// 字段是否可见
		wrti[10].setEdit(true);// 字段是否可编辑

		// 客户电话
		wrti[11] = new WorkflowRequestTableField();
		wrti[11].setFieldName("lxdh");// 字段名
		try {
			wrti[11].setFieldValue(dataJson.getString("customerPhone"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[11].setView(true);// 字段是否可见
		wrti[11].setEdit(true);// 字段是否可编辑

		// 报装申请渠道
		wrti[12] = new WorkflowRequestTableField();
		wrti[12].setFieldName("bzqd");// 字段名
		try {
			wrti[12].setFieldValue(dataJson.getString("applyMethod"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[12].setView(true);// 字段是否可见
		wrti[12].setEdit(true);// 字段是否可编辑

		// 地区
		wrti[13] = new WorkflowRequestTableField();
		wrti[13].setFieldName("dq");// 字段名
		try {
			wrti[13].setFieldValue(dataJson.getString("region"));// 字段值
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("获取参数内容出现异常：" + e.getMessage());
		}
		wrti[13].setView(true);// 字段是否可见
		wrti[13].setEdit(true);// 字段是否可编辑

		// CEA类型
		wrti[14] = new WorkflowRequestTableField();
		wrti[14].setFieldName("lx");// 字段名
		wrti[14].setFieldValue("0");// 字段值
		wrti[14].setView(true);// 字段是否可见
		wrti[14].setEdit(true);// 字段是否可编辑

		// 地区码
		wrti[15] = new WorkflowRequestTableField();
		wrti[15].setFieldName("dqm");// 字段名
		wrti[15].setFieldValue(dqm);// 字段值
		wrti[15].setView(true);// 字段是否可见
		wrti[15].setEdit(true);// 字段是否可编辑

		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];// 主字段只有一行数据
		wrtri[0] = new WorkflowRequestTableRecord();
		wrtri[0].setWorkflowRequestTableFields(wrti);
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
		wmi.setRequestRecords(wrtri);

		// 添加工作流id
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();
		wbi.setWorkflowId("325");// workflowid
		WorkflowRequestInfo wri = new WorkflowRequestInfo();// 流程基本信息
		wri.setCreatorId(sqr);// 创建人id
		wri.setRequestLevel("0");// 0 正常，1重要，2紧急
		wri.setRequestName("前置咨询-" + sqrName + "-"
				+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 流程标题
		wri.setWorkflowMainTableInfo(wmi);// 添加主字段数据
		wri.setWorkflowBaseInfo(wbi);

		String requestId = new WorkflowServiceImpl().doCreateWorkflowRequest(
				wri, Integer.parseInt(sqr));
		if (Integer.parseInt(requestId) < 1) {
			log.writeLog("创建流程出现错误，requestId：" + requestId);
			return "2";
		}

		return "1";
	}
}
