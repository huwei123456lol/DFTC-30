package weaver.trq.util.report;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * 月相关流程
 * @author FW
 *
 */
public class RelatedWorkflowUtil {
		////08:30~17:00
	private static RecordSet rs = new RecordSet();
	private static BaseBean log = new BaseBean();
	
	//补休/外出申请（管理人员）
	private static String bxwc = "formtable_main_42";

	//年休/外出申请（管理人员）
	private static String nxwc = "formtable_main_41";

	//病假、事假、丧假、公假/外出申请（管理人员）
	private static String qjlxsys = "formtable_main_30";

	//出差/外出申请（管理人员）
	private static String ccwc = "formtable_main_199";

	//补休申请（员工）
	private static String bxsq = "formtable_main_29";

	//年休申请（员工）
	private static String nxsq = "formtable_main_26";

	//病假、事假、丧假申请（员工）
	private static String qjlxyg = "formtable_main_25";

	//出差申请（员工）
	private static String ccsq = "formtable_main_200";

	//婚假、计划生育假/外出申请（管理人员及员工）
	private static String 	hjjhsyj = "formtable_main_82";

	//加班申请表
	private static String jbsq = "formtable_main_27";
	private static String jbsqdt1 = "formtable_main_27_dt1";
	private static String jbsqdt2 = "formtable_main_27_dt2";
	private static String jbsqdt3 = "formtable_main_27_dt3";
	
	
	/**
	 * 补休/外出申请（管理人员）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> bxwc(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ bxwc
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("补休/外出申请（管理人员）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	
	/**
	 * 年休/外出申请（管理人员）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> nxwc(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ bxwc
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("年休/外出申请（管理人员）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 病假、事假、丧假、公假/外出申请（管理人员）-病假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> bj(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxsys
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=0 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）-病假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 病假、事假、丧假、公假/外出申请（管理人员）-事假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> sj(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxsys
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=1 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）-事假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 病假、事假、丧假、公假/外出申请（管理人员）----丧假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> sangj(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxsys
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=2 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）----丧假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 病假、事假、丧假、公假/外出申请（管理人员）---公假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> gj(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxsys
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=3 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（管理人员）---公假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 出差/外出申请（管理人员）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> ccwc(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ ccwc
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("出差/外出申请（管理人员）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 补休申请（员工）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> bxsq(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select bxkssj,bxjssj,bxksrq,bxjsrq from "
					+ bxsq
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between bxksrq and bxjsrq";
			log.writeLog("补休申请（员工）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("bxksrq"))
						&& rs.getString("bxkssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("bxkssj"));
				} else if (presentDay.trim().equals(rs.getString("bxjsrq"))
						&& rs.getString("bxjssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("bxjssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("bxksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("bxkssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("bxjsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("bxjssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 年休申请（员工）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> nxsq(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ nxsq
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("年休申请（员工）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	
	/**
	 * 病假、事假、丧假、公假/外出申请（员工）-病假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> bjyg(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxyg
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=0 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（员工）-病假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 病假、事假、丧假、公假/外出申请（员工）-事假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> sjyg(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxyg
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=1 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（员工）-事假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 病假、事假、丧假、公假/外出申请（员工）----丧假
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> sangjyg(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ qjlxyg
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.qjlx=2 and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("病假、事假、丧假、公假/外出申请（员工）----丧假", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 出差申请（员工）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> ccsq(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ ccsq
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3'and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("出差申请（员工）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 婚假、计划生育假/外出申请（管理人员及员工）
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> hjjhsyj(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ hjjhsyj
					+ " f,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("婚假、计划生育假/外出申请（管理人员及员工）", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	/**
	 * 加班申请表--dt1
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> jbsqdt1(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ jbsq
					+ " f,"+jbsqdt1+" dt1,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.id=dt1.mainid and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("加班申请表--dt1", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 加班申请表--dt2
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> jbsqdt2(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ jbsq
					+ " f,"+jbsqdt2+" dt2,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.id=dt2.mainid and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("加班申请表--dt2", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 加班申请表--dt3
	 * 
	 * @param sqr
	 *            申请人
	 * @param presentDay
	 *            日期
	 * @return
	 */
	public static List<String> jbsqdt3(int sqr, String presentDay) {
		List<String> list = new ArrayList<String>();
		try {
			String kssj = "";// 事假开始时间
			String jssj = "";// 事假结束时间

			String sql = "select kssj,jssj,ksrq,jsrq from "
					+ jbsq
					+ " f,"+jbsqdt3+" dt3,workflow_requestbase w where f.REQUESTID=w.REQUESTID and w.currentnodetype ='3' and f.id=dt3.mainid and f.sqr='"
					+ sqr + "' and '" + presentDay
					+ "' between ksrq and jsrq";
			log.writeLog("加班申请表--dt3", sql);
			rs.execute(sql);
			while (rs.next()) {
				if (presentDay.trim().equals(rs.getString("ksrq"))
						&& rs.getString("kssj").trim().equals("17:00")) {
					// 如果当前日期等于出差开始日期并且出差开始时间等于17：30就将出差开始时间赋值给出差结束时间
					log.writeLog("请假流程1");
					jssj = Util.null2String(rs.getString("kssj"));
				} else if (presentDay.trim().equals(rs.getString("jsrq"))
						&& rs.getString("jssj").trim().equals("08:30")) {
					// 如果当前日期等于出差结束日期并且出差结束时间等于08：00就将出差结束时间赋值给出差开始时间
					log.writeLog("请假流程2");
					// 如果当前日期等于结束日期并且结束时间等于08:01
					kssj = Util.null2String(rs.getString("jssj"));
				} else {

					log.writeLog("请假流程3");
					// 如果当前日期大于开始日期
					if (presentDay.trim().compareTo(rs.getString("ksrq")) > 0) {
						log.writeLog("请假流程4");
						kssj = "08:30";
					} else {
						log.writeLog("请假流程5");
						kssj = Util.null2String(rs.getString("kssj"));
					}
					// 如果当前日期小于结束日期
					if (presentDay.trim().compareTo(rs.getString("jsrq")) < 0) {
						log.writeLog("请假流程6");
						jssj = "17:00";
					} else {
						log.writeLog("请假流程7");
						jssj = Util.null2String(rs.getString("jssj"));
					}
				}

				// 如果结果开始时间是空，或者临时开始时间早于结果开始时间，那么就把临时开始时间中的时间赋值给结果开始时间
				// 如果结果结束时间是空，或者临时结束时间晚于结果结束时间，那么就把临时结束时间中的时间赋值给结果结束时间
			}
			list.add(kssj.equals("") ? "" : kssj);
			list.add(jssj.equals("") ? "" : jssj);
		} catch (Exception e) {
			log.writeLog("获取事假流程数据异常", e.getMessage());
		}
		return list;
	}
}
