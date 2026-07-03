package weaver.dfqcgsjszx.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.weaver.general.BaseBean;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 记录地址到地址库
 * 
 * @author Alex.Du
 * 
 */
public class RecordAddressDzkAction implements Action {

	@Override
	public String execute(RequestInfo requestInfo) {
		// 获取主表单的数据
		String fydz = null;// 发运地址
		String jsdz = null;// 接收地址
		String qdjd = null;// 起点经度
		String qdwd = null;// 起点纬度
		String zdjd = null;// 终点经度
		String zdwd = null;// 终点纬度

		Property[] propertys = requestInfo.getMainTableInfo().getProperty();

		for (int i = 0; i < propertys.length; i++) {
			String name = propertys[i].getName();
			String value = propertys[i].getValue();

			if (name.trim().equals("fydz")) {
				fydz = value.trim();
			}

			if (name.trim().equals("jsdz")) {
				jsdz = value.trim();
			}

			if (name.trim().equals("qdjd")) {
				qdjd = value.trim();
			}

			if (name.trim().equals("qdwd")) {
				qdwd = value.trim();
			}

			if (name.trim().equals("zdjd")) {
				zdjd = value.trim();
			}

			if (name.trim().equals("zdwd")) {
				zdwd = value.trim();
			}
		}

		RecordSet rs = new RecordSet();

		if (null != fydz) {
			// 如果发运地址不为空则记录发运地址
			try {
				recordAddress(fydz, qdjd, qdwd);
			} catch (Exception e) {
				e.printStackTrace();
				new BaseBean().writeLog("插入发运地址时出现异常：" + e.getMessage());
			}
		}

		if (null != jsdz) {
			// 如果接收地址不为空则记录接收地址
			try {
				recordAddress(jsdz, zdjd, zdwd);
			} catch (Exception e) {
				e.printStackTrace();
				new BaseBean().writeLog("插入接收地址时出现异常：" + e.getMessage());
			}
		}

		return Action.SUCCESS;
	}

	/**
	 * 封装记录地址的方法
	 * 
	 * @param dz
	 *            地址
	 * @param jd
	 *            经度
	 * @param wd
	 *            纬度
	 */
	private void recordAddress(String dz, String jd, String wd)
			throws Exception {
		RecordSet rs = new RecordSet();
		String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
		try {
			// 查询此地址是否已经存在
			rs.execute("select * from uf_dzk where dzmc = '" + dz + "'");

			if (rs.next()) {
				// 存在则对使用次数+1
				rs.execute("update uf_dzk set sycs=sycs+1 where dzmc = '" + dz
						+ "'");
			} else {
				// 不存在则新增此地址
				rs.execute("insert into uf_dzk(dzmc,jd,wd,sycs,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
						+ dz
						+ "','"
						+ jd
						+ "','"
						+ wd
						+ "',0,97,1,0,'"
						+ nowDate + "','" + nowTime + "')");
				
				rs.execute("select max(id) from uf_dzk");
				int id = 0;
				if (rs.next()) {
					id = rs.getInt(1);
				}
				
				// 用于构建建模数据的共享权限的工具类
				ModeRightInfo mri = new ModeRightInfo();
				mri.setNewRight(true);
				mri.editModeDataShare(1, 97, id);
				mri = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			rs = null;
			nowDate = null;
			nowTime = null;
		}

	}
}
