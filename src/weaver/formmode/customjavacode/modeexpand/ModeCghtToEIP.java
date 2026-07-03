package weaver.formmode.customjavacode.modeexpand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.integration.Base;
import com.weaver.general.TimeUtil;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.EIP.ht_service_client.ISendMessageServiceProxy;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;


/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
@SuppressWarnings("ALL")
public class ModeCghtToEIP extends AbstractModeExpandJavaCodeNew {
	/**
	 * 执行模块扩展动作
	 * @param param
	 *  param包含(但不限于)以下数据
	 *  user 当前用户
	 *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
	 *  页面链接拼接的参数，比如b=2,可以通过param.get("b")来获取参数
	 * @return 
	 */
	@Override
	public Map<String, String> doModeExpand(Map<String, Object> param) {
		Map<String, String> result = new HashMap<String, String>();
		final String FAILTAG = "1";
		try {
			User user = (User)param.get("user");
			//数据id
			int billid = -1;
			//模块id
			int modeid = -1;
			RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
			if(requestInfo!=null){
				billid = Util.getIntValue(requestInfo.getRequestid());
				modeid = Util.getIntValue(requestInfo.getWorkflowid());
				if(billid>0&&modeid>0){
					//------请在下面编写业务逻辑代码------//
					/* 参考Action weaver.dfqcgsjszx.action.ContractInfoToEIPAction
					* 通过BILLID查询相关数据候 调用EIP接口将数据导入到EIP中。
					* 注意！！此接口不导入采购合同正文文件，因为老OA的数据中合同正文和合同附件无法进行分离
					* */
					//合同编号（合同编号）
					String htbh = "";
					//采购需求申请部门顶级部门（签约部门编号）
					String bg3bm = "";
					//（签约部门名称）
					String qybmmc = "";
					//申请类型(合同状态)
					String sqlx = "";
					//(审计金额)
					String sjje = "";
					//合同金额(合同总金额)
					String htjehsy = "";
					//采购项目名称(项目名称)，取明细表数据，用逗号分隔
					StringBuffer cgxmmc = new StringBuffer();
					//项目编号/投资计划号(项目编号)，取明细表数据，用逗号分隔
					StringBuffer xmbhtzjhh = new StringBuffer();
					//合同内容及附件内容（合同内容）
					String htnrjfjnr = "";
					//对方签约单位名称（相对方）
					String dfqydwmc = "";
					//合同文档(合同正文)
					String htwd = "";
					//（文件类型）
					String wjlx = "";
					//合同类别（合同类别）
					String htlb = "";
					//采购需求资金渠道（采购资金渠道）
					String cgxqzjqd = "";
					//是否有质保金（是否有质保金）
					String sfyzbj = "";
					//卖方单位 如果有就拼接到对方签约单位名称
					String mfdw = "";
					//是否涉外
					String sfsw = "";
					// step 1. 通过billid查询主表数据
					RecordSet rs_MT = new RecordSet();
					String mt_sql = "select * from uf_cghtjbxx where id = " + billid;
					rs_MT.execute(mt_sql);
					if(rs_MT.next()){
						htbh = rs_MT.getString("htbh");
						bg3bm = rs_MT.getString("bg3bm");
						sqlx = rs_MT.getString("sqlx");
						htjehsy = rs_MT.getString("htjehsy");
						htnrjfjnr = rs_MT.getString("htnrjfjnr");
						dfqydwmc = rs_MT.getString("dfqydwmc");
						htlb = rs_MT.getString("kkht");
						cgxqzjqd = rs_MT.getString("cgxqzjqd");
						sfyzbj = rs_MT.getString("sfyzbj");
						mfdw = rs_MT.getString("mfdw");
						sfsw = rs_MT.getString("sfsw");
					}

					if(!mfdw.equals("")){
						dfqydwmc = dfqydwmc + "," + mfdw;
					}

					//处理采购需求申请顶级部门id转编号
					RecordSet rs_Temp = new RecordSet();
					rs_Temp.execute("select departmentcode from hrmdepartment where id='"+bg3bm+"'");
					if(rs_Temp.next()){
						bg3bm = rs_Temp.getString("departmentcode");
					}

					//处理采购合同中的采购需求明细
					RecordSet rs_DT = new RecordSet();
					String dt_sql = "select * from uf_cghtjbxx_dt1 where mainid = " + billid;
					rs_DT.execute(dt_sql);
					while (rs_DT.next()){

						if (cgxmmc.length() > 1) {
							cgxmmc.append(",");
						}
						cgxmmc.append(rs_DT.getString("cgxmmc"));

						if (xmbhtzjhh.length() > 1) {
							xmbhtzjhh.append(",");
						}
						xmbhtzjhh.append(rs_DT.getString("xmbh"));
					}


					//调用接口将合同数据传送给EIP
					//构建参数，调用接口
					//构建接口头部参数
					JSONObject paramHead = new JSONObject();
					paramHead.put("clientCode", "DFG_EIP");
					paramHead.put("reqSerialNo", UUID.randomUUID().toString());
					paramHead.put("tradeCode", "DFG_EIP_014");
					paramHead.put("tradeDescription", "COS合同信息推送到EIP");
					paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
					paramHead.put("version", "1.0");

					//构建接口内容参数
					JSONObject paramData = new JSONObject();
					paramData.put("HTBH", htbh);
					paramData.put("QYBMBH", bg3bm);
					paramData.put("QYBMZT", qybmmc);
					paramData.put("HTZT", sqlx);
					paramData.put("SJJE", "");
					paramData.put("HTZJE", htjehsy);
					paramData.put("XMMC", cgxmmc.toString());
					paramData.put("XMBH", xmbhtzjhh.toString());
					paramData.put("HTNR", htnrjfjnr);
					paramData.put("XDF", dfqydwmc);
					paramData.put("HTWBFJ", htwd);
					paramData.put("WJLX", wjlx);
					paramData.put("HTLB", htlb);
					paramData.put("CGZJQD", cgxqzjqd);
					paramData.put("SFYZBJ", sfyzbj);
					paramData.put("sfsw", sfsw);
					//封装数据格式
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(paramData);

					JSONObject paramBody = new JSONObject();
					paramBody.put("DATA", jsonArray);

					new BaseBean().writeLog("[ContractInfoToEIPAction]paramBody：" + paramBody.toJSONString());

					String resultInterface = null;
					try {
						resultInterface = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
					} catch (Exception e) {
						e.printStackTrace();
						new BaseBean().writeLog("[ContractInfoToEIPAction]调用合同信息接口时出现异常：" + e.getMessage());
					}

					new BaseBean().writeLog("[]result=" + resultInterface);

					try {
						JSONObject resultJson = JSONObject.parseObject(resultInterface);

						if (FAILTAG.equals(resultJson.getString("STATUS").trim())) {
							//接口返回的状态表示调用失败，则阻止流程提交
						}
					} catch (Exception e) {
						e.printStackTrace();
						new BaseBean().writeLog("[ContractInfoToEIPAction]解析合同信息接口的调用结果出现异常：" + e.getMessage());
					}

					paramHead = null;
					paramData = null;
					rs_MT = null;
					rs_DT = null;
					rs_Temp = null;
				}
			}
		} catch (Exception e) {
			result.put("errmsg","自定义出错信息");
			result.put("flag", "false");
		}
		return result;
	}

	@Override
	public Object execute(Map<String, Object> map) {
		return null;
	}
}