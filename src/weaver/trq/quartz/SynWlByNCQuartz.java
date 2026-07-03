package weaver.trq.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.trq.webservice.TransferPortTypeProxy;

/**
 * 从NC同步物料的定时任务
 * 
 * @author Alex.Du
 * 
 */
public class SynWlByNCQuartz extends BaseCronJob {
	private static List<String> wlfl = new ArrayList<String>();

	static {
		wlfl.add("施工材料采购1201");
		wlfl.add("施工材料采购1202");
		wlfl.add("施工材料采购1203");
		wlfl.add("施工材料采购1204");
		wlfl.add("施工材料采购1205");
		wlfl.add("施工材料采购1206");
		wlfl.add("施工材料采购1207");
		wlfl.add("施工材料采购1208");
		wlfl.add("施工材料采购1209");
		wlfl.add("施工材料采购1301");
		wlfl.add("施工材料采购1302");
		wlfl.add("施工材料采购1303");
		wlfl.add("施工材料采购1304");
		wlfl.add("施工材料采购1305");
		wlfl.add("施工材料采购1306");
		wlfl.add("施工材料采购1401");
		wlfl.add("施工材料采购1402");
		wlfl.add("施工材料采购1403");
		wlfl.add("施工材料采购1404");
		wlfl.add("施工材料采购1405");
		wlfl.add("施工材料采购1406");
		wlfl.add("施工材料采购1407");
		wlfl.add("施工材料采购1408");
		wlfl.add("施工材料采购1409");
		wlfl.add("施工材料采购1410");
		wlfl.add("施工材料采购1501");
		wlfl.add("施工材料采购1502");
		wlfl.add("施工材料采购1503");
		wlfl.add("施工材料采购1504");
		wlfl.add("施工材料采购1505");
		wlfl.add("施工材料采购1601");
		wlfl.add("施工材料采购1602");
		wlfl.add("施工材料采购1604");
		wlfl.add("施工材料采购1609");
		wlfl.add("施工材料采购1610");
		wlfl.add("施工材料采购1611");
		wlfl.add("施工材料采购1612");
	}

	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("开始执行NC同步物料的定时任务");
		
		String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
		
		RecordSet rs = new RecordSet();

		TransferPortTypeProxy proxy = new TransferPortTypeProxy();
		try {
			log.writeLog("开始调用中转服务器获取NC的物料数据");

			for (int c = 0; c < wlfl.size(); c++) {

				String result = proxy.send(3, wlfl.get(c));

				// log.writeLog("NC的返回结果为：" + result);

				Document document = DocumentHelper.parseText(result);

				Element root = document.getRootElement();

				List<Element> materialElements = root.elements();

				for (int i = 0; i < materialElements.size(); i++) {
					Element materialElement = materialElements.get(i);
					// NC主键
					String materialPk = new String(Base64.decode(Util
							.null2String(materialElement
									.attributeValue("pk_Material"))));
					// 物料名称
					String name = new String(
							Base64.decode(Util.null2String(materialElement
									.attributeValue("Name"))));
					// 物料规格
					String model = new String(Base64.decode(Util
							.null2String(materialElement
									.attributeValue("Model"))));
					// 型号
					String type = new String(
							Base64.decode(Util.null2String(materialElement
									.attributeValue("Type"))));
					// 单位
					String unit = new String(
							Base64.decode(Util.null2String(materialElement
									.attributeValue("Unit"))));

					// 根据供应商的NF主键和账号查询数据是否存在
					rs.execute("select id from uf_wlk where wlpk='" + materialPk
							+ "'");

					if (rs.next()) {
						// 存在则更新
						String id = rs.getString("id");

						rs.execute("update uf_wlk set wlmc='" + name
								+ "',wlpk='" + materialPk + "',gg='" + model
								+ "',xh='" + type + "',dw='" + unit
								+ "' where id=" + id);
					} else {
						// 不存在则插入
						rs.execute("insert into uf_wlk(wlpk,wlmc,gg,xh,dw,wlbm,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
								+ materialPk
								+ "','"
								+ name
								+ "','"
								+ model
								+ "','"
								+ type
								+ "','"
								+ unit
								+ "','',83,1,0,'"
								+ nowDate
								+ "','"
								+ nowTime
								+ "')");
						
						rs.execute("select max(id) from uf_wlk");
						int id = 0;
						if (rs.next()) {
							id = rs.getInt(1);
						}

						// 用于构建建模数据的共享权限的工具类
						ModeRightInfo mri = new ModeRightInfo();
						mri.setNewRight(true);
						mri.editModeDataShare(1, 83, id);
						mri = null;
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog("执行NC同步物料的定时任务时出现异常：" + e.getMessage());
		}
		log.writeLog("执行NC同步物料的定时任务结束");
	}
}
