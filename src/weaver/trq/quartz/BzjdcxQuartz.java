package weaver.trq.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.User;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.mobile.plugin.ecology.service.HrmResourceService;
import weaver.blog.WorkDayDao;

/**
 * 报装进度查询 自动修改状态
 * 
 * @author peixuan
 * 
 */
public class BzjdcxQuartz extends BaseCronJob {

	public void execute() {

		RecordSet rs = new RecordSet();
		RecordSet rs2 = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("报装进度查询---start---");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// User user = new User(1);
		// log.writeLog("User>=="+user);
		// log.writeLog("User>=="+user.getUID());
		HrmResourceService hrs = new HrmResourceService();
		User user = hrs.getUserById(1);
		log.writeLog("User>==" + user.getUID());
		String jssj = "";
		rs.execute("select * from bzmk where kssj > '2019-05-06'");
		for (int i = 0; rs.next(); i++) {
			jssj = rs.getString("jssj");
			if (jssj.equals("")) {
				jssj = df.format(new Date());// 当前系统日期
			}
			//log.writeLog("结束时间:" + jssj);
			if (!rs.getString("kssj").equals("")
					&& rs.getString("kssj") != null) {
				if (rs.getString("xmlx").equals("0")) {
					List list = new WorkDayDao(user).getWorkDays(""
							+ rs.getString("kssj").substring(0, 10) + "", ""
							+ jssj + "");
					//log.writeLog("List:" + list, "ID:" + rs.getString("id"));
					if (list.size() > 15) {
						rs2.execute("update bzmk set cqzt = 0 where id='"
								+ rs.getString("id") + "'");
					}
				} else if (rs.getString("xmlx").equals("1")) {
					List list = new WorkDayDao(user).getWorkDays(""
							+ rs.getString("kssj").substring(0, 10) + "", ""
							+ jssj + "");
					//log.writeLog("List:" + list, "ID:" + rs.getString("id"));
					if (list.size() > 25) {
						rs2.execute("update bzmk set cqzt = 0 where id='"
								+ rs.getString("id") + "'");
					}
				} else if (rs.getString("xmlx").equals("2")) {
					List list = new WorkDayDao(user).getWorkDays(""
							+ rs.getString("kssj").substring(0, 10) + "", ""
							+ jssj + "");
					//log.writeLog("List:" + list, "ID:" + rs.getString("id"));
					if (list.size() > 35) {
						rs2.execute("update bzmk set cqzt = 0 where id='"
								+ rs.getString("id") + "'");
					}
				} else if (rs.getString("xmlx").equals("3")) {
					List list = new WorkDayDao(new User()).getWorkDays(""
							+ rs.getString("kssj").substring(0, 10) + "", ""
							+ jssj + "");
					//log.writeLog("List:" + list, "ID:" + rs.getString("id"));
					if (list.size() > 40) {
						rs2.execute("update bzmk set cqzt = 0 where id='"
								+ rs.getString("id") + "'");
					}
				}
			}
		}

		log.writeLog("报装进度查询---end---");
		rs = null;
		rs2 = null;
		log = null;
	}

}
