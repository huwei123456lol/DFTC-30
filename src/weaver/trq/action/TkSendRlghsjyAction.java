package weaver.trq.action;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.weaver.general.TimeUtil;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.trq.webservice.rlghsjy.WebServiceSoapProxy;
import weaver.workflow.action.BaseAction;

/**
 * 踏勘数据发送到热力规划设计院接口的Action
 * 
 * @author Alex.Du
 * 
 */
public class TkSendRlghsjyAction extends BaseAction {
	public String execute(RequestInfo requestInfo) {
		BaseBean log = new BaseBean();
		log.writeLog("开始执行踏勘数据发送到热力规划设计院接口的Action");
		// 获取主表单中需要传送的数据
		String projectCode = "";// 项目编号
		String projectName = "";// 项目名称
		String addr = "";// 项目地址
		String designStage = ""; //项目阶段
		String proPlanDate = "";//计划完成日期
		String customerName = "";// 客户单位
		String customerREmail="";//客户代码邮箱
		String contact = "";// 联系人
		String phone = "";// 联系方式
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date());// 委托单下达时间

		Property[] properties = requestInfo.getMainTableInfo().getProperty();

		for (int i = 0; i < properties.length; i++) {
			Property property = properties[i];
			String name = property.getName();
			String value = property.getValue();

			if (name.trim().equals("xmbh1")) {
				projectCode = value.trim();
				continue;
			}
			if (name.trim().equals("xmmc")) {
				projectName = value.trim();
				continue;
			}
			if (name.trim().equals("tkdz")) {
				addr = value.trim();
				continue;
			}
			if (name.trim().equals("xmjd")) {
				designStage = value.trim();
				continue;
			}
			if (name.trim().equals("jhwcsj")) {
				proPlanDate = value.trim();
				continue;
			}
			if (name.trim().equals("sszb")) {
				customerName = value.trim();
				continue;
			}
//			if (name.trim().equals("lxry")) {
//				contact = value.trim();
//				continue;
//			}
//			if (name.trim().equals("lxfs2")) {
//				phone = value.trim();
//				continue;
//			}
		}

		// 处理数据(部分数据需要数据转换)
		RecordSet rs = new RecordSet();

		// 处理项目编号数据
		if (!projectCode.trim().equals("")) {
			rs.execute("select xmbh from uf_xm where id = '" + projectCode
					+ "'");
			if (rs.next()) {
				projectCode = rs.getString("xmbh");
			}
		}
		
		//处理项目阶段
		if(designStage.trim().equals("")){
			designStage = "3";
		}
		//处理客户单位
		rs.execute("select departmentname from hrmdepartment where id = "+customerName);
		if(rs.next()){
			customerName = rs.getString("departmentname");
		}
		

		log.writeLog("项目编号:" + projectCode);
		log.writeLog("项目名称:" + projectName);
		log.writeLog("项目地址:" + addr);
		log.writeLog("项目阶段:" + designStage);
		log.writeLog("计划完成时间:" + proPlanDate);
		log.writeLog("客户单位:" + customerName);
		log.writeLog("联系人:" + contact);
		log.writeLog("联系方式:" + phone);
		log.writeLog("委托单下达时间:" + createTime);

		// 将所有要传输的数据构建成一个Json数据
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("project_code", projectCode);
		jsonMap.put("project_name", projectName);
		jsonMap.put("addr", addr);
		jsonMap.put("design_stage", designStage);
		jsonMap.put("pro_plan_date", proPlanDate);
		jsonMap.put("customer_name", customerName);
		jsonMap.put("customer_r_email", customerREmail);
		jsonMap.put("contact", contact);
		jsonMap.put("phone", phone);
		jsonMap.put("createTime", createTime);

		JSONObject jsonObject = new JSONObject(jsonMap);
		log.writeLog("构建的Json参数为：[" + jsonObject.toString()+"]");

		WebServiceSoapProxy proxy = new WebServiceSoapProxy();
		try {
			String result = proxy.insertProject("["+jsonObject.toString()+"]");
			if (!result.trim().equals("1")) {
				log.writeLog("热力规划设计院接口返回失败结果");
				requestInfo.getRequestManager().setMessageid(
						requestInfo.getRequestid() + "-"
								+ TimeUtil.getCurrentTimeString());// 提醒信息id
				requestInfo.getRequestManager().setMessagecontent(
						"调用热力规划设计院接口失败");// 提醒信息内容
				return Action.FAILURE_AND_CONTINUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog("调用热力规划设计院接口出现异常："+e.getMessage());
			requestInfo.getRequestManager().setMessageid(
					requestInfo.getRequestid() + "-"
							+ TimeUtil.getCurrentTimeString());// 提醒信息id
			requestInfo.getRequestManager().setMessagecontent(
					"调用热力规划设计院接口出现异常："+e.getMessage());// 提醒信息内容
			return Action.FAILURE_AND_CONTINUE;
		}

		log.writeLog("执行踏勘数据发送到热力规划设计院接口的Action结束");

		return Action.SUCCESS;
	}
}
