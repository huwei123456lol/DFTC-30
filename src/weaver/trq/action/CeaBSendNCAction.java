package weaver.trq.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis.encoding.Base64;

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
 * CeaB流程数据发送到NC系统
 * 
 * @author Alex.Du
 * 
 */
public class CeaBSendNCAction implements Action {

	@Override
	public String execute(RequestInfo requestInfo) {
		String nowDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());// 当前时间
		int logId = 0;// 记录插入传递日志的数据ID，用来插入数据权限

		// 用于构建建模数据的共享权限的工具类
		ModeRightInfo mri = new ModeRightInfo();
		mri.setNewRight(true);

		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		log.writeLog("开始获取流程表数据");
		String ceaID = ""; // CEAID
		String ceaNo = ""; // CEA编号
		String projectName = ""; // 项目名称
		String xmid = ""; // 项目ID
		String bmid = ""; // 部门ID
		String sszb = ""; // 所属组别
		String dept = ""; // 部门编号
		String fszxbm = ""; // 部门名称
		String orgbh = "whng001"; // 公司名称
		String sqrId = ""; // 申请人ID
		String user = ""; // 申请人登入名
		String username = ""; // 申请人姓名
		String money = ""; // 总金额
		String place = ""; // 地点
		String stateid = "";// 新增增补ID
		String state = ""; // 新增/增补
		String fsWorkNo = ""; // 流水号
		String ceatype = ""; // CEA类型
		String fsxbdq = "";// 地区
		String fsxbdqid = "";// 地区ID
		String fsxbgcxmllID = ""; // EPSID
		String fsxbgcxmll = "";// EPS分类编码
		String fsxbgcxmh = "";// 工程项目号
		String fsxbyjgckgsj = "";// 预计开始时间
		String fsxbyjgcjgsj = "";// 预计竣工时间
		String tqsj = "";// 通气时间
		String gzzsr = "";// 总收入
		String fsxbgcl = "";// 工程量
		String fsxbgcxmgs = "";// 工程项目概述
		String lcstate = "";// 流程状态
		String isgczcb = "";// 是否工程总承包

		String fsxbgcjafje = "";// 工程建安费
		String fsxbgcclfje = "";// 工程材料费
		String fsxbgcsjfje = "";// 工程设计费
		String fsxbgcjlfje = "";// 工程监理费
		String fsxbgcbcfje = "";// 工程补偿费
		String fsxbgckcfje = "";// 工程勘测费
		String fsxbgfje = "";// 规费
		String fsxbqtje = "";// 其他
		String fsxbbkyjfyje = "";// 不可预计费用

		log.writeLog("开始发送");
		Property[] propertise = requestInfo.getMainTableInfo().getProperty();

		for (int i = 0; i < propertise.length; i++) {
			String name = propertise[i].getName().trim(); // 主表字段名
			String value = propertise[i].getValue().trim(); // 主表字段值
			log.writeLog("获取的字段名为：" + name + ",获取的字段值为:'" + value + "'");
			if (name.equals("xmbh")) {
				ceaID = value;
			}
			if (name.equals("xmbh1")) {
				xmid = value;
			}
			if (name.equals("xmmc")) {
				projectName = value;
			}
			if (name.equals("ssbm")) {
				bmid = value;
			}
			if (name.equals("sszb")) {
				sszb = value;
			}
			// if (name.equals("ssgs")) {
			// orgbh = value;
			// }
			if (name.equals("sqr")) {
				sqrId = value;
			}
			if (name.equals("zje")) {
				money = value;
			}
			if (name.equals("dd")) {
				place = value;
			}
			if (name.equals("ceazt")) {
				stateid = value;
			}
			if (name.equals("lsh1")) {
				fsWorkNo = value;
			}
			if (name.equals("cealx")) {
				ceatype = value;
				if (ceatype.trim().equals("0")) {
					ceatype = "A";
				}
				if (ceatype.trim().equals("1")) {
					ceatype = "B";
				}
				if (ceatype.trim().equals("2")) {
					ceatype = "C";
				}
				if (ceatype.trim().equals("3")) {
					ceatype = "EA";
				}
			}
			if (name.equals("diqu")) {
				fsxbdqid = value;
			}
			if (name.equals("gcxmxl1")) {
				fsxbgcxmllID = value;
			}
			if (name.equals("yjkgsj")) {
				fsxbyjgckgsj = value;
			}
			if (name.equals("yjjgsj")) {
				fsxbyjgcjgsj = value;
			}
			if (name.equals("yjgctqsj")) {
				tqsj = value;
			}
			if (name.equals("zsl")) {
				gzzsr = value;
			}
			if (name.equals("gcl1")) {
				fsxbgcl = value;
			}
			if (name.equals("gcxmgs")) {
				fsxbgcxmgs = value;
			}
			if (name.equals("sfgczcb")) {
				isgczcb = value;
			}
		}
		// 取明细数据

		DetailTable detailtable = requestInfo.getDetailTableInfo()
				.getDetailTable(0);// 获取明细表
		Row[] s = detailtable.getRow();// 当前明细表的所有数据,按行存储
		for (int j = 0; j < s.length; j++) {
			String fymx = "";// 费用明细
			String gcmxje = "";// 明细金额
			Row r = s[j];// 指定行
			Cell c[] = r.getCell();// 每行数据再按列存储
			for (int k = 0; k < c.length; k++) {
				Cell c1 = c[k];// 指定列
				String name = c1.getName();// 明细字段名称
				String value = c1.getValue();// 明细字段的值
				if (name.equals("fymx")) {
					fymx = value;
				}

				if (name.equals("gcmxje")) {
					gcmxje = value;
				}
			}
			if (fymx.trim().equals("0")) {
				fsxbgcjafje = gcmxje;
			}
			if (fymx.trim().equals("1")) {
				fsxbgcclfje = gcmxje;
			}
			if (fymx.trim().equals("2")) {
				fsxbgcsjfje = gcmxje;
			}
			if (fymx.trim().equals("3")) {
				fsxbgcjlfje = gcmxje;
			}
			if (fymx.trim().equals("4")) {
				fsxbgcbcfje = gcmxje;
			}
			if (fymx.trim().equals("5")) {
				fsxbgckcfje = gcmxje;
			}
			if (fymx.trim().equals("6")) {
				fsxbgfje = gcmxje;
			}
			if (fymx.trim().equals("7")) {
				fsxbqtje = gcmxje;
			}
			if (fymx.trim().equals("8")) {
				fsxbbkyjfyje = gcmxje;
			}
		}

		rs.execute("select ncepsfl from uf_ceaxmxl where id = '" + fsxbgcxmllID
				+ "'");
		if (rs.next()) {
			fsxbgcxmll = rs.getString("ncepsfl");// EPS分类编码
			if (fsxbgcxmll == null || fsxbgcxmll.trim().equals("")) {
				// 如果為空則不觸發NC接口
				return Action.SUCCESS;
			}
		} else {
			// 查詢不到，不觸發NC接口
			return Action.SUCCESS;
		}

		rs.execute("select ceah from uf_cea where id = '" + ceaID + "'");
		if (rs.next()) {
			ceaNo = rs.getString("ceah"); // CEA编号
		}

		rs.execute("select xmmc,xmbh from uf_xm where id = '" + xmid + "'");
		if (rs.next()) {
			if(null!= rs.getString("xmmc") && !rs.getString("xmmc").trim().equals("")){
				projectName = rs.getString("xmmc"); // 项目名称
			}
			fsxbgcxmh = rs.getString("xmbh"); // 项目号

		}
		log.writeLog("select xmmc,xmbh from uf_xm where id = '" + xmid + "'");

		// 如果bmid有值就使用bmid，没有就使用sszb
		bmid = bmid == null || bmid.trim().equals("0")
				|| bmid.trim().equals("") ? sszb : bmid;

		rs.execute("select departmentname from HrmDepartment where id = '"
				+ bmid + "'");
		if (rs.next()) {
			fszxbm = rs.getString("departmentname"); // 部门名称
		}
		rs.execute("select ncbm from uf_ncdygx where bm = '" + bmid + "'");
		if (rs.next()) {
			dept = rs.getString("ncbm"); // 部门编号
		}
		rs.execute("select loginid,lastname from hrmresource where id = '"
				+ sqrId + "'");
		if (rs.next()) {
			user = rs.getString("loginid");// 申请人登录名
			username = rs.getString("lastname");// 申请人姓名
		}

		rs.execute("select selectname from Workflow_Selectitem where fieldid = 9934 and selectvalue = '"
				+ stateid + "'");
		if (rs.next()) {
			state = rs.getString("selectname");// 新增or增补
		}
		// 通过state来产生lcstate的值
		lcstate = state.trim().equals("新增") ? "A" : "U";

		isgczcb = isgczcb.trim().equals("0") ? "Y" : "N";

		rs.execute("select dq from uf_dq where id= '" + fsxbdqid + "'");
		if (rs.next()) {
			fsxbdq = rs.getString("dq");
		}

		fsxbyjgckgsj = fsxbyjgckgsj.substring(0, 4) + "年"
				+ fsxbyjgckgsj.substring(5, 7) + "月"
				+ fsxbyjgckgsj.substring(8) + "日";

		fsxbyjgcjgsj = fsxbyjgcjgsj.substring(0, 4) + "年"
				+ fsxbyjgcjgsj.substring(5, 7) + "月"
				+ fsxbyjgcjgsj.substring(8) + "日";

		tqsj = tqsj.substring(0, 4) + "年" + tqsj.substring(5, 7) + "月"
				+ tqsj.substring(8) + "日";

		log.writeLog("CEA编号:" + ceaNo);
		log.writeLog("项目名称:" + projectName);
		log.writeLog("项目号:" + fsxbgcxmh);
		log.writeLog("部门编号:" + dept);
		log.writeLog("部门名称:" + fszxbm);
		log.writeLog("公司名称:" + orgbh);
		log.writeLog("申请人登入名:" + user);
		log.writeLog("申请人姓名:" + username);
		log.writeLog("工程投资总金额:" + money);
		log.writeLog("地点:" + place);
		log.writeLog("新增/增补:" + state);
		log.writeLog("流水号:" + fsWorkNo);
		log.writeLog("EPS分类编码:" + fsxbgcxmll);
		log.writeLog("CEA类型:" + ceatype);
		log.writeLog("地区:" + fsxbdq);
		log.writeLog("预计开始时间:" + fsxbyjgckgsj);
		log.writeLog("预计竣工时间:" + fsxbyjgcjgsj);
		log.writeLog("通气时间:" + tqsj);
		log.writeLog("总金额:" + money);
		log.writeLog("总收入:" + gzzsr);
		log.writeLog("工程量:" + fsxbgcl);
		log.writeLog("工程项目概述:" + fsxbgcxmgs);
		log.writeLog("工程建安费:" + fsxbgcjafje);
		log.writeLog("工程材料费:" + fsxbgcclfje);
		log.writeLog("工程设计费:" + fsxbgcsjfje);
		log.writeLog("工程监理费:" + fsxbgcjlfje);
		log.writeLog("工程补偿费:" + fsxbgcbcfje);
		log.writeLog("工程勘测费:" + fsxbgckcfje);
		log.writeLog("规费:" + fsxbgfje);
		log.writeLog("其他:" + fsxbqtje);
		log.writeLog("不可预计费用:" + fsxbbkyjfyje);
		log.writeLog("流程状态:" + lcstate);
		log.writeLog("是否工程总承包:" + isgczcb);

		try {
			fsxbgcl = new String(Base64.encode(fsxbgcl.getBytes("UTF-8")));
			fsxbgcxmgs = new String(Base64.encode(fsxbgcxmgs.getBytes("UTF-8")));
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

		SendXmlUtil sxu = new SendXmlUtil();
		String str = sxu.createCEAXML(ceaNo, projectName, fsxbgcxmh, dept,
				fszxbm, orgbh, user, username, money, fsxbgcxmll, place, state,
				fsWorkNo, ceatype, fsxbdq, fsxbyjgckgsj, fsxbyjgcjgsj, tqsj,
				gzzsr, fsxbgcl, fsxbgcxmgs, fsxbgcjafje, fsxbgcclfje,
				fsxbgcsjfje, fsxbgcjlfje, fsxbgcbcfje, fsxbgckcfje, fsxbgfje,
				fsxbqtje, fsxbbkyjfyje, lcstate, isgczcb);
		log.writeLog("生成的xml:" + str);
		log.writeLog("开始测试CEA调用");
		TransferPortTypeProxy trans = new TransferPortTypeProxy();
		try {
			String send = trans.send(0, str);
			log.writeLog("send:" + send);

			if (!send.trim().equals("ok")) {
				// 记录传递失败的日志
				rs.execute("insert into uf_cdrz(lcid,sqr,cdsj,cdjg,ycxx,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
						+ requestInfo.getRequestid()
						+ "','"
						+ sqrId
						+ "','"
						+ nowDateStr
						+ "','失败','流程数据提交到NC失败,错误信息为"
						+ send
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
						"流程数据提交到NC失败,错误信息为: " + send);// 提醒信息内容
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

		log.writeLog("测试CEA调用完毕");

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
