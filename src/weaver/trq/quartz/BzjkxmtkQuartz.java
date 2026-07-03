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
 * 报装监控 --项目踏勘
 * 
 * @author peixuan
 * 
 */
public class BzjkxmtkQuartz extends BaseCronJob {

	public void execute() {

		RecordSet rs = new RecordSet();
		RecordSet rs2 = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("项目踏勘---start---");
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
			jssj = rs.getString("tkjssj");
			if (jssj.equals("")) {
				jssj = df.format(new Date());// 当前系统日期
			}
			log.writeLog("结束时间:" + jssj);
			if (!rs.getString("bzjssj").equals("")
					&& rs.getString("bzjssj") != null) {
				List list = new WorkDayDao(user).getWorkDays(""
						+ rs.getString("bzjssj").substring(0, 10) + "", ""
						+ jssj.substring(0, 10) + "");
				log.writeLog("List:" + list, "ID:" + rs.getString("id"));
				if (list.size() > 1) {
					rs2.execute("update bzmk set cqzt2 = 0 where id='"
							+ rs.getString("id") + "'");
				}
			}
		}

		log.writeLog("项目踏勘---end---");
		rs = null;
		rs2 = null;
		log = null;
	}

}
