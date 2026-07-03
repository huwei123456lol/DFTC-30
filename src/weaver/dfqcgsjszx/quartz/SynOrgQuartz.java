package weaver.dfqcgsjszx.quartz;

import weaver.dfqcgsjszx.util.SynUtil;
import weaver.general.BaseBean;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.matrix.MatrixUtil;

/**
 * 同步组织数据的定时任务(已弃用）
 * 
 * @author Alex.Du
 * 
 */
public class SynOrgQuartz extends BaseCronJob {
	@Override
	public void execute() {
		new BaseBean().writeLog("同步组织数据的定时任务开始执行");
		SynUtil su = new SynUtil();
		try {
			su.synHrmSubCompany();
			su.synHrmDepartment();
			su.synHrmResource();
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("同步中间表的组织、人员数据出现异常：" + e.getMessage());
		}

		// 更新分部、部门信息和人力资源的缓存,同步矩阵数据
		DepartmentComInfo dc = null;
		SubCompanyComInfo sc = null;
		try {
			// 更新分部信息缓存
			sc = new SubCompanyComInfo();
			sc.removeCompanyCache();
			// 更新部门信息缓存
			dc = new DepartmentComInfo();
			dc.removeCompanyCache();

			// 根据部门的数据进行矩阵数据表同步
			MatrixUtil.sysDepartmentData();
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("更新分部、部门信息的缓存出现异常：" + e.getMessage());
		} finally {
			dc = null;
		}

		// 更新人力资源基础信息的缓存
		ResourceComInfo rc = null;
		try {
			// 更新人力资源基础信息的缓存
			rc = new ResourceComInfo();
			rc.removeResourceCache();

		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("更新人力资源信息的缓存出现异常：" + e.getMessage());
		} finally {
			rc = null;
		}
		new BaseBean().writeLog("同步组织数据的定时任务执行结束");
	}
}
