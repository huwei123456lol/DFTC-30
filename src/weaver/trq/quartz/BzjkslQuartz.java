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
 * 报装监控 --受理报装
 * 
 * @author peixuan
 * 
 */
public class BzjkslQuartz extends BaseCronJob {

	public void execute() {

		RecordSet rs = new RecordSet();
		RecordSet rs2 = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("受理报装查询---start---");
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
			jssj = rs.getString("bzjssj");
			if (jssj.equals("")) {
				jssj = df.format(new Date());// 当前系统日期
			}
			log.writeLog("结束时间:" + jssj);
			if (!rs.getString("bzkssj").equals("")
					&& rs.getString("bzkssj") != null) {
				List list = new WorkDayDao(user).getWorkDays(""
						+ rs.getString("bzkssj").substring(0, 10) + "", ""
						+ jssj.substring(0, 10) + "");
				log.writeLog("List:" + list, "ID:" + rs.getString("id"));
				if (list.size() > 1) {
					rs2.execute("update bzmk set cqzt1 = 0 where id='"
							+ rs.getString("id") + "'");
				}
			}
		}

		log.writeLog("受理报装---end---");
		rs = null;
		rs2 = null;
		log = null;
	}

}
