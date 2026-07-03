package weaver.trq.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.DetailTable;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.Row;
import weaver.trq.util.SendXmlUtil;
import weaver.trq.webservice.TransferPortTypeProxy;

/**
 * 合同数据发到送NC接口的Action
 * 
 * @author Alex.Du
 * 
 */
public class ContractSendNCAction implements Action {
	public String execute(RequestInfo requestInfo) {
		String nowDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());// 当前时间
		int logId = 0;// 记录插入传递日志的数据ID，用来插入数据权限

		// 用于构建建模数据的共享权限的工具类
		ModeRightInfo mri = new ModeRightInfo();
		mri.setNewRight(true);

		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();

		String fsContractName = "";// 合同名称
		String fscreateDateTime = "";// 申请时间
		String fsksDateTime = "";// 开始日期
		String fsjsDateTime = "";// 结束日期
		String gysncpk = "";// 供应商
		String fsSqDeptId = "";// 所属部门
		String sqrId = ""; // 申请人ID
		String user = "";// 申请人登录名
		String fsHtbh = "";// 合同编号
		String httype = "买卖合同-供应类合同";
		String orgbh = "whng001";
		List<Map<String, String>> cljgList = new ArrayList<Map<String, String>>();

		log.writeLog("开始向NC发送合同信息");

		// 获取主表数据
		Property[] propertise = requestInfo.getMainTableInfo().getProperty();

		for (int i = 0; i < propertise.length; i++) {
			String name = propertise[i].getName().trim(); // 主表字段名
			String value = propertise[i].getValue().trim(); // 主表字段值

			log.writeLog("获取到的属性为：" + name + "，值为：" + value);

			if (name.trim().equals("htzl")) {
				// 當htzl=149的時候觸發發送NC
				if (!value.trim().equals("149")) {
					log.writeLog("htzl!=149,跳出发送NC");
					return Action.SUCCESS;
				}
			}

			if (name.trim().equals("htmc")) {
				fsContractName = value.trim();
			}

			if (name.trim().equals("sqrq")) {
				fscreateDateTime = value.trim();
			}

			if (name.trim().equals("kssj")) {
				fsksDateTime = value.trim();
			}

			if (name.trim().equals("jssj")) {
				fsjsDateTime = value.trim();
			}

			if (name.trim().equals("gys")) {
				gysncpk = value.trim();
			}

			if (name.trim().equals("ssbm")) {
				fsSqDeptId = value.trim();
			}

			if (name.trim().equals("sqr")) {
				sqrId = value.trim();
			}

			if (name.trim().equals("htbh1")) {
				fsHtbh = value.trim();
			}
		}

		log.writeLog("开始获取明细表");
		// 获取明细表数据
		DetailTable dt = requestInfo.getDetailTableInfo().getDetailTable(0);

		Row[] rows = dt.getRow();

		for (int i = 0; i < rows.length; i++) {
			Cell[] cells = rows[i].getCell();
			Map<String, String> cljgMap = new HashMap<String, String>();

			log.writeLog("开始获取第" + i + "行数据");

			for (int j = 0; j < cells.length; j++) {
				Cell cell = cells[j];// 获取列
				String name = cell.getName();// 明细字段名称
				String value = cell.getValue();// 明细字段的值

				log.writeLog("获取到的属性为：" + name + "，值为：" + value);

				if (name.trim().equals("wlbh")) {
					rs.execute("select wlpk from uf_wlk where id='"
							+ value.trim() + "'");
					if (rs.next()) {
						cljgMap.put("fsZbId", rs.getString("wlpk"));
					}
				}

				if (name.trim().equals("sl")) {
					cljgMap.put("ffCpNum", value.trim());
				}

				if (name.trim().equals("dj")) {
					cljgMap.put("fsdj", value.trim());
				}

				if (name.trim().equals("je")) {
					cljgMap.put("fsprice", value.trim());
				}

				if (name.trim().equals("shl")) {
					cljgMap.put("fsshuilv", value.trim());
				}
			}

			cljgList.add(cljgMap);
		}

		// 相关数据的处理
		log.writeLog("开始进行数据处理");

		try {
			log.writeLog("fscreateDateTime:" + fscreateDateTime);

			fscreateDateTime = fscreateDateTime.substring(0, 4) + "年"
					+ fscreateDateTime.substring(5, 7) + "月"
					+ fscreateDateTime.substring(8) + "日";

			log.writeLog("处理后的fscreateDateTime:" + fscreateDateTime);

			log.writeLog("fsksDateTime:" + fsksDateTime);

			fsksDateTime = fsksDateTime.substring(0, 4) + "年"
					+ fsksDateTime.substring(5, 7) + "月"
					+ fsksDateTime.substring(8) + "日";

			log.writeLog("处理后的fsksDateTime:" + fsksDateTime);

			log.writeLog("fsjsDateTime:" + fsjsDateTime);

			fsjsDateTime = fsjsDateTime.substring(0, 4) + "年"
					+ fsjsDateTime.substring(5, 7) + "月"
					+ fsjsDateTime.substring(8) + "日";

			log.writeLog("处理后的fsjsDateTime:" + fsjsDateTime);
		} catch (Exception e) {
			e.printStackTrace();

			// 记录传递失败的日志
			rs.execute("insert into uf_cdrz(lcid,sqr,cdsj,cdjg,ycxx,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
					+ requestInfo.getRequestid()
					+ "','"
					+ sqrId
					+ "','"
					+ nowDateStr
					+ "','失败','流程数据提交到NC发生异常,错误信息为:"
					+ e.getMessage()
					+ "',89,1,0,'"
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
					+ "','"
					+ new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ "')");

			rs.execute("select max(id) from uf_cdrz");
			if (rs.next()) {
				logId = rs.getInt(1);
			}

			mri.editModeDataShare(1, 89, logId);
			mri = null;

			log.writeLog("处理日期出现异常：" + e.getMessage());

			// 阻止流程提交
			requestInfo.getRequestManager().setMessageid(
					requestInfo.getRequestid() + "-"
							+ TimeUtil.getCurrentTimeString());// 提醒信息id
			requestInfo.getRequestManager().setMessagecontent(
					"流程数据提交到NC发生异常,错误信息为:" + e.getMessage());// 提醒信息内容
			return Action.FAILURE_AND_CONTINUE;
		}

		log.writeLog("1");

		rs.execute("select ncpk from uf_gysk where id='" + gysncpk + "'");
		if (rs.next()) {
			gysncpk = rs.getString("ncpk");
		}

		log.writeLog("2");

		rs.execute("select ncbm from uf_ncdygx where bm='" + fsSqDeptId + "'");
		if (rs.next()) {
			fsSqDeptId = rs.getString("ncbm");
		}

		log.writeLog("3");

		rs.execute("select lastname from hrmresource where id='" + sqrId + "'");
		if (rs.next()) {
			user = rs.getString("lastname");
		}

		log.writeLog("4");

		rs.execute("select htbh from uf_fgshtk where id='" + fsHtbh + "'");
		if (rs.next()) {
			fsHtbh = rs.getString("htbh");
		}

		log.writeLog("5");

		String sendXMLStr = new SendXmlUtil().createContractXML(fsContractName,
				fscreateDateTime, fsksDateTime, fsjsDateTime, gysncpk,
				fsSqDeptId, user, fsHtbh, httype, orgbh, cljgList);

		log.writeLog("生成的xml:" + sendXMLStr);
		log.writeLog("开始调用NC合同接口");

		TransferPortTypeProxy trans = new TransferPortTypeProxy();
		try {
			String sendResult = trans.send(1, sendXMLStr);
			log.writeLog("sendResult:" + sendResult);

			if (!sendResult.trim().equals("ok")) {
				// 记录传递失败的日志
				rs.execute("insert into uf_cdrz(lcid,sqr,cdsj,cdjg,ycxx,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
						+ requestInfo.getRequestid()
						+ "','"
						+ sqrId
						+ "','"
						+ nowDateStr
						+ "','失败','流程数据提交到NC失败,错误信息为"
						+ sendResult
						+ "',89,1,0,'"
						+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ "','"
						+ new SimpleDateFormat("HH:mm:ss").format(new Date())
						+ "')");

				rs.execute("select max(id) from uf_cdrz");
				if (rs.next()) {
					logId = rs.getInt(1);
				}

				mri.editModeDataShare(1, 89, logId);
				mri = null;

				// 阻止流程提交
				requestInfo.getRequestManager().setMessageid(
						requestInfo.getRequestid() + "-"
								+ TimeUtil.getCurrentTimeString());// 提醒信息id
				requestInfo.getRequestManager().setMessagecontent(
						"流程数据提交的NC失败,错误信息为: " + sendResult);// 提醒信息内容
				return Action.FAILURE_AND_CONTINUE;
			}

		} catch (Exception e) {
			e.printStackTrace();

			// 记录传递失败的日志
			rs.execute("insert into uf_cdrz(lcid,sqr,cdsj,cdjg,ycxx,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
					+ requestInfo.getRequestid()
					+ "','"
					+ sqrId
					+ "','"
					+ nowDateStr
					+ "','失败','流程数据提交到NC发生异常,错误信息为:"
					+ e.getMessage()
					+ "',89,1,0,'"
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
					+ "','"
					+ new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ "')");

			rs.execute("select max(id) from uf_cdrz");
			if (rs.next()) {
				logId = rs.getInt(1);
			}

			mri.editModeDataShare(1, 89, logId);
			mri = null;

			log.writeLog("流程数据提交到NC发生异常,错误信息为:" + e.getMessage());
			// 阻止流程提交
			requestInfo.getRequestManager().setMessageid(
					requestInfo.getRequestid() + "-"
							+ TimeUtil.getCurrentTimeString());// 提醒信息id
			requestInfo.getRequestManager().setMessagecontent(
					"流程数据提交到NC发生异常,错误信息为:" + e.getMessage());// 提醒信息内容
			return Action.FAILURE_AND_CONTINUE;
		}

		log.writeLog("测试向NC发送合同信息完毕");

		// 记录传递成功的日志
		rs.execute("insert into uf_cdrz(lcid,sqr,cdsj,cdjg,ycxx,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
				+ requestInfo.getRequestid()
				+ "','"
				+ sqrId
				+ "','"
				+ nowDateStr
				+ "','成功','',89,1,0,'"
				+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
				+ "','"
				+ new SimpleDateFormat("HH:mm:ss").format(new Date()) + "')");

		rs.execute("select max(id) from uf_cdrz");
		if (rs.next()) {
			logId = rs.getInt(1);
		}

		mri.editModeDataShare(1, 89, logId);
		mri = null;

		return Action.SUCCESS;
	}
}
