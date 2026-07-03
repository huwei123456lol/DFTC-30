package weaver.trq.quartz;

import java.util.Calendar;

import weaver.conn.RecordSet;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 创建车辆运行管理月报表初始数据（通过车辆基础信息）
 * 
 * @author Alex.Du
 * 
 */
public class CreateClyxglybbQuartz extends BaseCronJob {
	public void execute() {
		// 获取当前年份和月份
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);// 月份从日历对象中取出时正好是和月份的ID是相同的

		// 计算年份ID（当前年份减去2017正好是年份在数据库中的ID）
		int yearId = year - 2017;

		RecordSet rs = new RecordSet();
		RecordSet rs2 = new RecordSet();
		// 查询车辆基础信息表中状态为正常的车辆信息
		rs.execute("select id,clxh,bm from uf_cljcxx where zt=0");

		// 循环为每个车辆信息创建一条车辆运行管理月报表的数据
		while (rs.next()) {
			rs2.execute("insert into uf_ybb(nf,yf,cph,clxh,bm) values("
					+ yearId + "," + month + "," + rs.getInt("id") + ",'"
					+ rs.getString("clxh") + "'," + rs.getInt("bm") + ")");
		}

		rs = null;
		rs2 = null;

	}
}
