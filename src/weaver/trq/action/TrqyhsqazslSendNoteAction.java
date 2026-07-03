package weaver.trq.action;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.sms.SMSSaveAndSend;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 天然气用户申请安装受理流程，提交时发送短信
 * 
 * @author Alex.Du
 * 
 */
public class TrqyhsqazslSendNoteAction implements Action {
	public String execute(RequestInfo requestInfo) {
		try {
			new BaseBean().writeLog("天然气用户申请安装受理流程发送短信息程序开始执行");
			// 组建短信内容
			StringBuilder noteContent = new StringBuilder();

			noteContent.append("尊敬的客户，非常感谢您选择武汉市天然气有限公司，您的编号：");

			// 获取流程表单中的项目编号（实际是项目ID），通过项目编号查询项目表中的项目编号值
			String xmID = null;// 项目编号（实际是项目ID）

			String lxdh = null;// 客户电话

			Property[] propertys = requestInfo.getMainTableInfo().getProperty();

			for (int i = 0; i < propertys.length; i++) {
				String name = propertys[i].getName();
				String value = propertys[i].getValue();

				if (name.trim().equals("xmbh1")) {
					xmID = value.trim();
				}
				if (name.trim().equals("lxdh")) {
					lxdh = value.trim();
				}
			}

			if (null == lxdh || lxdh.trim().equals("")) {
				new BaseBean().writeLog("天然气用户申请安装受理流程中的客户电话为空。无法发送短信息");
				return Action.SUCCESS;
			} else if (null == xmID || xmID.trim().equals("")) {
				new BaseBean().writeLog("天然气用户申请安装受理流程中的项目编号为空。无法发送短信息");
				return Action.SUCCESS;
			} else {
				// 通过项目ID查询项目编号
				RecordSet rs = new RecordSet();
				rs.execute("select xmbh from uf_xm where id=" + xmID);
				rs.next();

				noteContent.append(rs.getString("xmbh"));

				noteContent
						.append("的项目我公司已正式受理，即将为您进行现场踏勘；请你保持电话畅通，我公司会及时与您取得联系（公司服务监督电话：027-85863370）");
				
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
					new BaseBean().writeLog("天然气用户申请安装受理流程发送短信息成功");
				} else {
					new BaseBean().writeLog("天然气用户申请安装受理流程发送短信息失败");
				}
			}

			new BaseBean().writeLog("天然气用户申请安装受理流程发送短信息程序执行完毕");
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("点火通气流程发送短信息出现异常：" + e.getMessage());
		}

		return Action.SUCCESS;
	}

}
