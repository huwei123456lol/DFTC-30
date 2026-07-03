package weaver.trq.quartz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.icu.util.Calendar;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.trq.util.report.RelatedWorkflowUtil;

/**
 * 计算流程
 * @author FW
 *
 */
public class MonthCheckQuartz extends BaseCronJob {
	private final static String zwTime = "12:00";// 午休开始时间
	private final static String wdTime = "17:30";// 下班时间
	@Override
	public void execute() {
		
		
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		log.writeLog("开始执行考勤同步定时任务");
		List<String> list = getDays("2019-02-01", "2019-02-28");// 获得两个时间段直接所有日期的集合
		rs.execute("select hrm.id,hrm.lastname,hrmt.supdepid,hrmt.departmentname from hrmresource hrm,hrmdepartment hrmt where hrm.departmentid=hrmt.id");
		while(rs.next()){
			
			for (String string : list) {
				
				int ryid = rs.getInt("id");//人员ID
				String lastname = rs.getString("lastname");//人员姓名
				String supdepid = rs.getString("supdepid");//上级部门ID
				String departmentname = rs.getString("departmentname");//部门名称
				String sjbmmc = "";
				log.writeLog("人员ID", ryid);
				log.writeLog("人员姓名", lastname);
				log.writeLog("上级部门ID", supdepid);
				log.writeLog("部门名称", departmentname);
				log.writeLog("日期", string);
				rs1.execute("select departmentname from hrmdepartment where id='"+supdepid+"'");
				if(rs.next()){
					sjbmmc = rs.getString("departmentname");
				}
				log.writeLog("上级部门名称", sjbmmc);
				
				
				//补休/外出申请（管理人员）
				List<String> bxwc = RelatedWorkflowUtil.bxwc(ryid, string);
				log.writeLog("补休/外出申请（管理人员）", bxwc);
				
				//年休/外出申请（管理人员）
				List<String> nxwc = RelatedWorkflowUtil.nxwc(ryid, string);
				log.writeLog("年休/外出申请（管理人员）", nxwc);
				
				//病假、事假、丧假、公假/外出申请（管理人员）-病假
				List<String> bj = RelatedWorkflowUtil.bj(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）-病假", bj);
				
				//病假、事假、丧假、公假/外出申请（管理人员）-事假
				List<String> sj = RelatedWorkflowUtil.sj(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）-事假", sj);
				
				//病假、事假、丧假、公假/外出申请（管理人员）----丧假
				List<String> sangj = RelatedWorkflowUtil.sangj(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）----丧假", sangj);
				
				//病假、事假、丧假、公假/外出申请（管理人员）---公假
				List<String> gj = RelatedWorkflowUtil.gj(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）---公假", gj);
				
				//出差/外出申请（管理人员）
				List<String> ccwc = RelatedWorkflowUtil.ccwc(ryid, string);
				log.writeLog("出差/外出申请（管理人员）", ccwc);
				
				//补休申请（员工）
				List<String> bxsq = RelatedWorkflowUtil.bxsq(ryid, string);
				log.writeLog("补休申请（员工）", bxsq);
				
				//年休申请（员工）
				List<String> nxsq = RelatedWorkflowUtil.nxsq(ryid, string);
				log.writeLog("年休申请（员工）", nxsq);
				
				//病假、事假、丧假、公假/外出申请（员工）-病假
				List<String> bjyg = RelatedWorkflowUtil.bjyg(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（员工）-病假", bjyg);
				
				//病假、事假、丧假、公假/外出申请（员工）-事假
				List<String> sjyg = RelatedWorkflowUtil.sjyg(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（员工）-事假", sjyg);
				
				//病假、事假、丧假、公假/外出申请（员工）----丧假
				List<String> sangjyg = RelatedWorkflowUtil.sangjyg(ryid, string);
				log.writeLog("病假、事假、丧假、公假/外出申请（员工）----丧假", sangjyg);
				
				//出差申请（员工）
				List<String> ccsq = RelatedWorkflowUtil.ccsq(ryid, string);
				log.writeLog("出差申请（员工）", ccsq);
				
				//婚假、计划生育假/外出申请（管理人员及员工）
				List<String> hjjhsyj = RelatedWorkflowUtil.hjjhsyj(ryid, string);
				log.writeLog("婚假、计划生育假/外出申请（管理人员及员工）", hjjhsyj);
				
				//加班申请表dt1
				List<String> jbsqdt1 = RelatedWorkflowUtil.jbsqdt1(ryid, string);
				log.writeLog("加班申请表dt1", jbsqdt1);
				
				//加班申请表dt2
				List<String> jbsqdt2 = RelatedWorkflowUtil.jbsqdt2(ryid, string);
				log.writeLog("加班申请表dt2", jbsqdt2);
				
				//加班申请表dt3
				List<String> jbsqdt3 = RelatedWorkflowUtil.jbsqdt3(ryid, string);
				log.writeLog("加班申请表dt3", jbsqdt3);
				
				
				String sw = "";//用于存储下午的信息
				String xw = "";//用于存储下午的信息
				
				
				/**
				 * 上午
				 */
				//补休/外出申请（管理人员）
				if(!bxwc.get(0).equals("") && bxwc.get(0).compareTo(zwTime)<0){
					log.writeLog("上午补休/外出申请（管理人员）");
					sw = "补";
				}
				//年休/外出申请（管理人员）
				if(!nxwc.get(0).equals("") && nxwc.get(0).compareTo(zwTime)<0){
					log.writeLog("上午年休/外出申请（管理人员）");
					sw = "年";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）---病假
				if(!bj.get(0).equals("") && bj.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（管理人员）---病假");
					sw = "病";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）----事假
				if(!sj.get(0).equals("") && sj.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（管理人员）----事假");
					sw = "事";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）----丧假
				if(!sangj.get(0).equals("") && sangj.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（管理人员）----丧假");
					sw = "丧";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）---公假
				if(!gj.get(0).equals("") && sj.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（管理人员）---公假");
					sw = "公";
				}
				
				//出差/外出申请（管理人员）
				if(!ccwc.get(0).equals("") && ccwc.get(0).compareTo(zwTime)<0){
					log.writeLog("上午出差/外出申请（管理人员）");
					sw = "差";
				}
				
				//补休申请（员工）
				if(!bxsq.get(0).equals("") && bxsq.get(0).compareTo(zwTime)<0){
					log.writeLog("上午补休申请（员工）");
					sw = "补";
				}
				
				//年休申请（员工）
				if(!nxsq.get(0).equals("") && nxsq.get(0).compareTo(zwTime)<0){
					log.writeLog("上午年休申请（员工）");
					sw = "年";
				}
				
				//病假、事假、丧假、公假/外出申请（员工）-病假
				if(!bjyg.get(0).equals("") && bjyg.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（员工）-病假");
					sw = "病";
				}
				
				//病假、事假、丧假、公假/外出申请（员工）-事假
				if(!sjyg.get(0).equals("") && sjyg.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（员工）-事假");
					sw = "事";
				}
				
				//病假、事假、丧假、公假/外出申请（员工）----丧假
				if(!sangjyg.get(0).equals("") && sangjyg.get(0).compareTo(zwTime)<0){
					log.writeLog("上午病假、事假、丧假、公假/外出申请（员工）----丧假");
					sw = "丧";
				}
				
				//出差申请（员工）
				if(!ccsq.get(0).equals("") && ccsq.get(0).compareTo(zwTime)<0){
					log.writeLog("上午出差申请（员工）");
					sw = "差";
				}
				
				//婚假、计划生育假/外出申请（管理人员及员工）
				if(!hjjhsyj.get(0).equals("") && hjjhsyj.get(0).compareTo(zwTime)<0){
					log.writeLog("上午婚假、计划生育假/外出申请（管理人员及员工）");
					sw = "计";
				}
				
				//加班申请表--dt1
				if(!jbsqdt1.get(0).equals("") && jbsqdt1.get(0).compareTo(zwTime)<0){
					log.writeLog("上午加班申请表--dt1");
					sw = "加";
				}
				
				//加班申请表--dt2
				if(!jbsqdt2.get(0).equals("") && jbsqdt2.get(0).compareTo(zwTime)<0){
					log.writeLog("上午加班申请表--dt2");
					sw = "加";
				}
				
				//加班申请表--dt3
				if(!jbsqdt3.get(0).equals("") && jbsqdt3.get(0).compareTo(zwTime)<0){
					log.writeLog("上午加班申请表--dt3");
					sw = "加";
				}
				log.writeLog("sw", sw);
				/*if(sw.equals("")){
					sw = "1";
				}*/
				
				
				
				
				/**
				 * 下午
				 */
				
				
				
				
				//补休/外出申请（管理人员）
				if(!bxwc.get(1).equals("") && bxwc.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午补休/外出申请（管理人员）");
					xw = "补";
				}
				//年休/外出申请（管理人员）
				if(!nxwc.get(1).equals("") && nxwc.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午年休/外出申请（管理人员）");
					xw = "年";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）---病假
				if(!bj.get(1).equals("") && bj.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（管理人员）---病假");
					xw = "病";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）----事假
				if(!sj.get(1).equals("") && sj.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（管理人员）----事假");
					xw = "事";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）----丧假
				if(!sangj.get(1).equals("") && sangj.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（管理人员）----丧假");
					xw = "丧";
				}
				
				//病假、事假、丧假、公假/外出申请（管理人员）---公假
				if(!gj.get(1).equals("") && sj.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（管理人员）---公假");
					xw = "公";
				}
				
				//出差/外出申请（管理人员）
				if(!ccwc.get(1).equals("") && ccwc.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午出差/外出申请（管理人员）");
					xw = "差";
				}
				
				//补休申请（员工）
				if(!bxsq.get(1).equals("") && bxsq.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午补休申请（员工）");
					xw = "补";
				}
				
				//年休申请（员工）
				if(!nxsq.get(1).equals("") && nxsq.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午年休申请（员工）");
					xw = "年";
				}
				
				//病假、事假、丧假、公假/外出申请（员工）-病假
				if(!bjyg.get(1).equals("") && bjyg.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（员工）-病假");
					xw = "病";
				}
				
				//病假、事假、丧假、公假/外出申请（员工）-事假
				if(!sjyg.get(1).equals("") && sjyg.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（员工）-事假");
					xw = "事";
				}
				
				//病假、事假、丧假、公假/外出申请（员工）----丧假
				if(!sangjyg.get(1).equals("") && sangjyg.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午病假、事假、丧假、公假/外出申请（员工）----丧假");
					xw = "丧";
				}
				
				//出差申请（员工）
				if(!ccsq.get(1).equals("") && ccsq.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午出差申请（员工）");
					xw = "差";
				}
				
				//婚假、计划生育假/外出申请（管理人员及员工）
				if(!hjjhsyj.get(1).equals("") && hjjhsyj.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午婚假、计划生育假/外出申请（管理人员及员工）");
					xw = "计";
				}
				
				//加班申请表--dt1
				if(!jbsqdt1.get(1).equals("") && jbsqdt1.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午加班申请表--dt1");
					xw = "加";
				}
				
				//加班申请表--dt2
				if(!jbsqdt2.get(1).equals("") && jbsqdt2.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午加班申请表--dt2");
					xw = "加";
				}
				
				//加班申请表--dt3
				if(!jbsqdt3.get(1).equals("") && jbsqdt3.get(1).compareTo(wdTime)>=0){
					log.writeLog("下午加班申请表--dt3");
					xw = "加";
				}
				log.writeLog("xw", xw);
				/*if(xw.equals("")){
					xw = "1";
				}
				String swxw = "";//合并上午下午信息
				if(sw.equals(xw)){
					swxw = xw;
				}else{
					swxw = sw+"<br><br>"+xw;
				}
				log.writeLog("swxw", swxw);
				*/
				
				
				rs1.execute("insert into uf_kqb(xm,bh,bm,zb,rq,sw,xw) values('"+lastname+"','"+ryid+"','"+sjbmmc+"','"+departmentname+"','"+string+"','"+sw+"','"+xw+"');");
			
			}
		}
		
		log.writeLog("考勤定时任务执行完毕！");
		
		
	}
	
	public static List<String> getDays(String startTime, String endTime) {

		// 返回的日期集合
		List<String> days = new ArrayList<String>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return days;
	}
}
