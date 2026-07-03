package weaver.trq.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.sms.SMSSaveAndSend;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.trq.util.SendHttpUtil;

/**
 * 点火通气流程，提交时发送短信
 * 
 * @author Alex.Du
 * 
 */
public class DhtqSendNoteAction implements Action {

	public String execute(RequestInfo requestInfo) {
		try {
			new BaseBean().writeLog("点火通气流程发送短信息程序开始执行");
			// 组建短信内容
			StringBuilder noteContent = new StringBuilder();

			noteContent.append("尊敬的客户，您的编号：");

			// 获取流程表单中的项目编号（实际是项目ID），通过项目编号查询项目表中的项目编号值
			String xmID = null;// 项目编号（实际是项目ID）

			String lxdh = null;// 客户电话

			String jcgc = null;// 进场工程时间

			Property[] propertys = requestInfo.getMainTableInfo().getProperty();

			for (int i = 0; i < propertys.length; i++) {
				String name = propertys[i].getName();
				String value = propertys[i].getValue();

				if (name.trim().equals("xmbh")) {
					xmID = value.trim();
				}
				if (name.trim().equals("khdh")) {
					lxdh = value.trim();
				}
				if (name.trim().equals("jcgc")) {
					jcgc = value.trim();
				}
			}

			if (null == lxdh || lxdh.trim().equals("")) {
				new BaseBean().writeLog("点火通气流程中的客户电话为空。无法发送短信息");
				return Action.SUCCESS;
			} else if (null == xmID || xmID.trim().equals("")) {
				new BaseBean().writeLog("点火通气流程中的项目编号为空。无法发送短信息");
				return Action.SUCCESS;
			} else if (null == jcgc || jcgc.trim().equals("")) {
				new BaseBean().writeLog("点火通气流程中的进场工程时间为空。无法发送短信息");
				return Action.SUCCESS;
			} else {
				// 通过项目ID查询项目编号
				RecordSet rs = new RecordSet();
				rs.execute("select xmbh from uf_xm where id=" + xmID);
				rs.next();

				noteContent.append(rs.getString("xmbh"));

				noteContent.append("的项目已进入施工阶段，我公司计划于");
				
				jcgc = jcgc.substring(0,4)+"年"+jcgc.substring(5,7)+"月"+jcgc.substring(8)+"日";
				
				noteContent.append(jcgc);
				noteContent
						.append("前后进场施工，待验收完成后我公司将及时点火通气，望您监督我们的服务。（公司服务监督电话：027-85863370）");

				new BaseBean().writeLog("短信发送的号码为：" + lxdh);
				new BaseBean().writeLog("短信发送的内容为：" + noteContent.toString());

				// 调用短信API进行短信发送
				SMSSaveAndSend smsSaveAndSend = new SMSSaveAndSend();

				// 重置数据
				smsSaveAndSend.reset();
				// 设置发送内容
				smsSaveAndSend.setMessage(noteContent.toString());
				// 设置接收号码
				smsSaveAndSend.setCustomernumber(lxdh);
				// 设置发送号码
				smsSaveAndSend.setSendnumber(null);
				smsSaveAndSend.setRequestid(0);
				// 设置发送人（系统管理员）
				smsSaveAndSend.setUserid(1);

				if (smsSaveAndSend.pageSend()) {
					new BaseBean().writeLog("点火通气流程发送短信息成功");
				} else {
					new BaseBean().writeLog("点火通气流程发送短信息失败");
				}
			}

			new BaseBean().writeLog("点火通气流程发送短信息程序执行完毕");
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("点火通气流程发送短信息出现异常：" + e.getMessage());
		}

		return Action.SUCCESS;
	}
}
