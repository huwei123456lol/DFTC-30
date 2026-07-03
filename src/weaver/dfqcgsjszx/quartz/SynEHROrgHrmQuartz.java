package weaver.dfqcgsjszx.quartz;

import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.SynUtil;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.job.JobActivitiesComInfo;
import weaver.hrm.job.JobTitlesComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.matrix.MatrixUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 同步老PI（EHR）的组织、人员数据
 *
 * @author du
 */
public class SynEHROrgHrmQuartz extends BaseCronJob {
    @Override
    public void execute() {
        new BaseBean().writeLog("同步EHR组织、人员数据的定时任务开始执行");
        try {
            synHrmSubCompany();
            synHrmDepartment();
            synHrmResource();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseBean().writeLog("同步EHR中间表的组织、人员数据出现异常：" + e.getMessage());
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

        //更新职位、岗位信息的缓存
        try {
            new JobActivitiesComInfo().removeJobActivitiesCache();
            new JobTitlesComInfo().removeJobTitlesCache();
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("更新职位、岗位信息的缓存出现异常：" + e.getMessage());
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
        new BaseBean().writeLog("同步EHR组织、人员数据的定时任务执行结束");
    }


    /**
     * 同步分部数据
     *
     * @throws Exception
     */
    public void synHrmSubCompany() throws Exception {
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        try {
            // 查找上级为null且需要同步(ISSYN=0)的数据
            rs1.execute("select id,ORG_ID,PARENT_ORG_ID,ORG_NAME,ORG_CODE,ORG_STATUS,ACT from UF_EHR_ORG_DATA where PARENT_ORG_ID='000000' and ORG_ID!='000000' and IS_SYN=0 ORDER BY LENGTH(ORG_NAME)");

            // 循环处理查询到的组织
            while (rs1.next()) {
                // 通过组织编码对比分部表的subcompanycode，判断当前分部是否已经存在
                rs2.execute("select id from hrmsubcompany where SUBCOMPANYCODE='"
                        + rs1.getString("ORG_ID") + "'");

                // 上级分部的ID
                String superSubCompanyId = "0";

                if (rs2.next()) {
                    //当前分部的ID
                    String nowSubCompanyId = rs2.getString("id");

                    // 存在则更新
                    rs2.execute("update hrmsubcompany set COMPANYID=1,SUPSUBCOMID="
                            + superSubCompanyId
                            + ",SUBCOMPANYDESC='"
                            + rs1.getString("ORG_NAME")
                            + "',SUBCOMPANYNAME='"
                            + rs1.getString("ORG_NAME")
                            + "',CANCELED='"
                            + (rs1.getString("ORG_STATUS").trim().equals("dt_orgstatus_valid")&&!rs1.getString("ACT").trim().equals("delete") ? 0 : 1)
                            + "',modified=to_date('"
                            + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                            + " 00:00:00','YYYY-MM-DD HH24:MI:SS') where SUBCOMPANYCODE= '"
                            + rs1.getString("ORG_ID") + "'");

                    // 因为在EHR中，分部下有人员的存在，我们系统无法在分部下面挂人员，所以自动为该分部创建了一个对应用来挂人员的部门，修改分部时，同样修改此部门
                    rs2.execute("update HrmDepartment set departmentname='"
                            + (rs1.getString("ORG_NAME").equals("技术中心") ? rs1.getString("ORG_NAME") + "领导" : rs1.getString("ORG_NAME"))
                            + "',departmentmark='"
                            + (rs1.getString("ORG_NAME").equals("技术中心") ? rs1.getString("ORG_NAME") + "领导" : rs1.getString("ORG_NAME"))
                            + "',subcompanyid1='" + nowSubCompanyId + "',modified=to_date('"
                            + (new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00")
                            + "','YYYY-MM-DD HH24:MI:SS'),canceled='"
                            + (rs1.getString("ORG_STATUS").trim().equals("dt_orgstatus_valid")&&!rs1.getString("ACT").trim().equals("delete") ? 0
                            : 1) + "' where  departmentcode= '"
                            + rs1.getString("ORG_ID") + "'");


                } else {
                    // 不存在则插入
                    rs2.execute("insert into hrmsubcompany(SUBCOMPANYDESC,SUBCOMPANYNAME,SUBCOMPANYCODE,COMPANYID,SUPSUBCOMID,modified) values('"
                            + rs1.getString("ORG_NAME")
                            + "','"
                            + rs1.getString("ORG_NAME")
                            + "','"
                            + rs1.getString("ORG_ID")
                            + "',1,"
                            + superSubCompanyId
                            + ",to_date('"
                            + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00','YYYY-MM-DD HH24:MI:SS'))");

                    // 查询新插入的分部ID
                    rs2.execute("select id from hrmsubcompany where SUBCOMPANYCODE='"
                            + rs1.getString("ORGID") + "'");

                    int id = 0;
                    if (rs2.next()) {
                        id = rs2.getInt("id");
                    }

                    // 因为在同步EHR时，EHR分部下有人员的存在，我们系统无法在分部下面挂人员，所以自动为该分部创建了一个用来挂该分部人员的部门，插入分部时，自动创建此部门
                    rs2.execute("insert into HrmDepartment(subcompanyid1,supdepid,departmentname,departmentmark,departmentcode,modified) values("
                            + id
                            + ",0,'"
                            + (rs1.getString("ORG_NAME").equals("技术中心") ? rs1.getString("ORG_NAME") + "领导" : rs1.getString("ORG_NAME"))
                            + "','"
                            + (rs1.getString("ORG_NAME").equals("技术中心") ? rs1.getString("ORG_NAME") + "领导" : rs1.getString("ORG_NAME"))
                            + "','"
                            + rs1.getString("ORG_ID")
                            + "',to_date('"
                            + (new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00")
                            + "','YYYY-MM-DD HH24:MI:SS'))");

                }

                //判断此分部下是否有有效的人员，如果没有，则封存为此分部自动创建的部门
                rs2.execute("select id from UF_EHR_HRM_DATA where ORG_ID='" + rs1.getString("ORG_ID") + "' and EMP_STATUS='在职'");

                if (rs2.next()) {
                    //存在有效人员，启用此部门
                    rs2.execute("update HrmDepartment set canceled='0' where  departmentcode= '" + rs1.getString("ORG_ID") + "'");
                } else {
                    //不存在有效人员，封存此部门
                    rs2.execute("update HrmDepartment set canceled='1' where  departmentcode= '" + rs1.getString("ORG_ID") + "'");
                }


                // 查询当前处理的分部的ID
                String subCompanyId = null;
                rs2.execute("select id from hrmsubcompany where SUBCOMPANYCODE = '"
                        + rs1.getString("ORG_ID") + "'");
                if (rs2.next()) {
                    subCompanyId = rs2.getString("id");
                }

                // 更新机构权限数据：新增加的分部默认继承上级分部的所有机构权限。
                String para = subCompanyId + Util.getSeparator()
                        + superSubCompanyId;
                try {
                    rs2.executeProc("HrmRoleSRT_AddByNewSc", para);
                } catch (Exception e) {
                    new BaseBean().writeLog("更新机构权限数据产生异常(e3)："
                            + e.getMessage());
                    throw e;
                }

                // 更新左侧菜单，新增的分部继承上级分部的左侧菜单
                String strWhere = " where resourcetype=2 and resourceid="
                        + superSubCompanyId;
                if (superSubCompanyId.equals("0")) {
                    strWhere = " where resourcetype=1  and resourceid=1 ";
                }
                String strSql = "insert into leftmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex,"
                        + subCompanyId
                        + ",2,locked,lockedbyid,usecustomname,customname,customname_e from leftmenuconfig "
                        + strWhere;
                // System.out.println(strSql);
                try {
                    rs2.execute(strSql);
                } catch (Exception e) {
                    new BaseBean().writeLog("更新左侧菜单数据产生异常(e3)："
                            + e.getMessage());
                    throw e;
                }

                // 更新顶部菜单，新增的分部继承上级分部的顶部菜单
                strWhere = " where resourcetype=2 and resourceid="
                        + superSubCompanyId;
                if (superSubCompanyId.equals("0")) {
                    strWhere = " where resourcetype=1  and resourceid=1 ";
                }

                strSql = "insert into mainmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex,"
                        + subCompanyId
                        + ",2,locked,lockedbyid,usecustomname,customname,customname_e from mainmenuconfig "
                        + strWhere;
                // System.out.println(strSql);
                try {
                    rs2.execute(strSql);
                } catch (Exception e) {
                    new BaseBean().writeLog("更新顶部菜单数据产生异常(e3)："
                            + e.getMessage());
                    throw e;
                }

                rs2.execute("update UF_EHR_ORG_DATA set IS_SYN=1 where id="
                        + rs1.getString("id"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            rs1 = null;
            rs2 = null;
        }
    }


    /**
     * 同步部门数据
     *
     * @throws Exception
     */
    public void synHrmDepartment() throws Exception {
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();

        try {
            // 查找组织ID和上级组织ID不一致且需要同步(ISSYN=0)的数据
            rs1.execute("select id,ORG_ID,PARENT_ORG_ID,ORG_NAME,ORG_CODE,ORG_STATUS,ACT from UF_EHR_ORG_DATA where PARENT_ORG_ID!='000000' and ORG_ID!='000000' and IS_SYN=0 ORDER BY LENGTH(ORG_NAME)");

            // 循环处理查询到的组织
            while (rs1.next()) {
                // 通过上级编码去分部表查询，获取上级分部的ID，如果没有查询到则上级分部ID为0
                String superSubCompanyId = "0";// 上级分部的ID
                rs2.execute("select id from hrmsubcompany where SUBCOMPANYCODE = '"
                        + rs1.getString("PARENT_ORG_ID") + "'");
                if (rs2.next()) {
                    superSubCompanyId = rs2.getString("id");
                }

                // 通过上级编码去部门表查询，获取上级部门的ID，如果没有查询到则上级部门ID为0
                String superDeptId = "0";// 上级部门的ID
                if (superSubCompanyId.trim().equals("0")) {
                    // 如果通过上级编码没有在分部表中查询到上级，才在部门表中查询上级
                    rs2.execute("select id from hrmdepartment where DEPARTMENTCODE = '"
                            + rs1.getString("PARENT_ORG_ID") + "'");
                    if (rs2.next()) {
                        superDeptId = rs2.getString("id");
                    }
                }

                // 如果上级部门ID不为0（有上级部门），则使用上级部门的所属分部作为当前部门的所属分部
                if (!superDeptId.trim().equals("0")) {
                    rs2.execute("select SUBCOMPANYID1 from hrmdepartment where id = '"
                            + superDeptId + "'");
                    if (rs2.next()) {
                        superSubCompanyId = rs2.getString("SUBCOMPANYID1");
                    }
                }

                // 通过组织编码对比部门表的DEPARTMENTCODE，判断当前部门是否已经存在
                rs2.execute("select id from hrmdepartment where DEPARTMENTCODE='"
                        + rs1.getString("ORG_ID") + "'");

                if (rs2.next()) {
                    // 存在则更新
                    rs2.execute("update HrmDepartment set departmentname='"
                            + rs1.getString("ORG_NAME")
                            + "',departmentmark='"
                            + rs1.getString("ORG_NAME")
                            + "',supdepid = " + superDeptId + ",subcompanyid1="
                            + superSubCompanyId + ",modified=to_date('"
                            + (new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00")
                            + "','YYYY-MM-DD HH24:MI:SS'),canceled='"
                            + (rs1.getString("ORG_STATUS").trim().equals("dt_orgstatus_valid")&&!rs1.getString("ACT").trim().equals("delete") ? 0 : 1)
                            + "' where  departmentcode= '"
                            + rs1.getString("ORG_ID") + "'");
                } else {
                    // 不存在则插入
                    rs2.execute("insert into HrmDepartment(subcompanyid1,supdepid,departmentname,departmentmark,departmentcode,modified) values("
                            + superSubCompanyId
                            + ","
                            + superDeptId
                            + ",'"
                            + rs1.getString("ORG_NAME")
                            + "','"
                            + rs1.getString("ORG_NAME")
                            + "','"
                            + rs1.getString("ORG_ID")
                            + "',to_date('"
                            + (new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00")
                            + "','YYYY-MM-DD HH24:MI:SS'))");
                }

                rs2.execute("update UF_EHR_ORG_DATA set IS_SYN=1 where id="
                        + rs1.getString("id"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            rs1 = null;
            rs2 = null;
        }
    }


    /**
     * 同步人力资源
     *
     * @throws Exception
     */
    public void synHrmResource() throws Exception {
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();

        try {
            // 查找员工编码、员工名称、岗位名称不为空且需要同步(ISSYN=0)的数)--------------------------------------------
            rs1.execute("select * from UF_EHR_HRM_DATA where OPERATER_ID is not null and EMP_NAME is not null and IS_SYN=0 ORDER BY OPERATER_ID");

            // 循环处理查询到的人员
            while (rs1.next()) {
                new BaseBean().writeLog("开始同步OPERATER_ID为："+rs1.getString("OPERATER_ID")+"的人员数据");

                if(rs1.getString("OPERATER_ID").trim().equals("sysadmin")){
                    continue;
                }

                String jobActivityId = "0";

                String jobTitleId = "0";
                new BaseBean().writeLog("当前人员的EMP_POSITION为：'"+rs1.getString("EMP_POSITION")+"'");
                String secLevel = "10";//定义人员的安全级别，默认为10
                //判断当前人员是否有职务，没有职务则不处理该人员的职务及岗位
                if(rs1.getString("EMP_POSITION")!=null&&!rs1.getString("EMP_POSITION").equals("")) {
                    //获取当前人员的职务（职责），查看职务是否存在，不存在则新建此职务
                    rs2.execute("select zj,ptzj,aqjb from uf_zjdygx where ptzj='"+rs1.getString("EMP_POSITION")+"'");
                    if (rs2.next()) {
                        // 此职务存在
                        jobActivityId = rs2.getString("zj");

                        //获取该人员职务对应的安全级别
                        secLevel = rs2.getString("aqjb");

                        //判断当前人员是否有岗位，没有岗位则不处理该人员的岗位
                        if(rs1.getString("POSITION_ID")!=null&&!rs1.getString("POSITION_ID").equals("")) {
                            // 获取当前人员的岗位名称，查看岗位是否存在，不存在则创建此岗
                            rs2.execute("select * from hrmjobtitles where jobtitleremark = '"
                                    + rs1.getString("POSITION_ID") + "'");

                            if (rs2.next()) {
                                // 此岗位存在
                                jobTitleId = rs2.getString("id");

                                //更新此岗位数据
                                rs2.execute("update hrmjobtitles set jobtitlename='"
                                        + rs1.getString("POSITION_NAME").trim()
                                        + "',jobtitlemark='"
                                        + rs1.getString("POSITION_NAME").trim()
                                        + "',JOBACTIVITYID='"
                                        + jobActivityId
                                        + "' where jobtitleremark='"
                                        + rs1.getString("POSITION_ID") + "'");

                            } else {
                                // 此岗位不存在
                                rs2.execute("insert into hrmjobtitles(jobtitlename,jobtitlemark,jobtitleremark,jobdepartmentid,jobactivityid) values('"
                                        + rs1.getString("POSITION_NAME").trim()
                                        + "','"
                                        + rs1.getString("POSITION_NAME").trim()
                                        + "','"
                                        + rs1.getString("POSITION_ID")
                                        + "',"
                                        + null
                                        + ","
                                        + jobActivityId
                                        + ")");

                                // 查询新插入的岗位ID
                                rs2.execute("select id from hrmjobtitles where jobtitleremark='"
                                        + rs1.getString("POSITION_ID")+"'");

                                if (rs2.next()) {
                                    jobTitleId = rs2.getString("id");
                                }
                            }
                        }
                    }
                }

                // 获取人员的所属部门、分部ID
                String subCompanyID = null;
                String supDeptID = null;
                rs2.execute("select id,SUBCOMPANYID1 from hrmdepartment where DEPARTMENTCODE='"
                        + rs1.getString("ORG_ID") + "'");

                if (rs2.next()) {
                    subCompanyID = rs2.getString("SUBCOMPANYID1");
                    supDeptID = rs2.getString("id");
                } else {
                    // 没有查询到该人员的部门，跳过该人员的同步处理
                    new BaseBean().writeLog("人员同步异常：OPERATER_ID为【"
                            + rs1.getString("OPERATER_ID")
                            + "】的用户通过ORGID（部门ID）无法找到对应的部门，将跳过该人员的处理");
                    continue;
                }

                // 处理人员的婚姻状态数据
                String maritalStatus = null;
                if (rs1.getString("MARITAL_STATUS").equals("未婚")) {
                    maritalStatus = "0";
                } else if (rs1.getString("MARITAL_STATUS").equals("已婚")) {
                    maritalStatus = "1";
                } else if (rs1.getString("MARITAL_STATUS").equals("离异")) {
                    maritalStatus = "2";
                } else if (rs1.getString("MARITAL_STATUS").equals("丧偶")) {
                    maritalStatus = "3";
                }

                // 处理人员的性别数据
                String sex = null;
                if (rs1.getString("SEX").trim().equals("男性")) {
                    sex = "0";
                } else if (rs1.getString("SEX").trim().equals("女性")) {
                    sex = "1";
                }


                // 处理人员的上级数据
                String managerID = null;
                rs2.execute("select id from HrmResource where workcode='"
                        + rs1.getString("SUPERVISOR_NUMBER") + "'");
                if (rs2.next()) {
                    managerID = rs2.getString("id");
                }

                // 查询该人员是否存在，存在则更新，不存在则插入
                rs2.execute("select id from HrmResource where workcode='"
                        + rs1.getString("OPERATER_ID") + "'");

                // 当前人员ID
                String userID = null;

                if (rs2.next()) {
                    userID = rs2.getString("id");

                    // 员工已经存在，更新员工数据
                    rs2.execute("update HrmResource set loginid='"
                            + rs1.getString("OPERATER_ID")
                            + "',lastname='"
                            + rs1.getString("EMP_NAME")
                            + "',DEPARTMENTID='"
                            + supDeptID
                            + "',SUBCOMPANYID1='"
                            + subCompanyID
                            + "',MANAGERID="
                            + managerID
                            + ",jobtitle='"
                            + jobTitleId
                            + "',CERTIFICATENUM='"
                            + Util.null2String(rs1.getString("NATIONAL_IDENTIFIER"))
                            + "',MARITALSTATUS="
                            + maritalStatus
                            + ",FOLK='"
                            + Util.null2String(rs1.getString("NATIONAL_MZ"))
                            + "',SEX="
                            + sex
                            + ",BIRTHDAY='"
                            + changeDateStr(rs1.getString("DATE_OF_BIRTH"))
                            + "',MOBILE='"
                            + Util.null2String(rs1.getString("MOBILE"))
                            + "',TELEPHONE='"
                            + Util.null2String(rs1.getString("OFF_TEL"))
                            //+ "',FAX='"
                            //+ Util.null2String(rs1.getString("MAILSTOP"))
                            + "',EMAIL='"
                            + Util.null2String(rs1.getString("EMAIL"))
                            + "',POLICY='"
                            + Util.null2String(rs1.getString("POLITICAL_FACE"))
                            + "',workstartdate='"
                            + changeDateStr(Util.null2String(rs1.getString("WORK_START_DATE")))
                            + "',companystartdate='"
                            + changeDateStr(Util.null2String(rs1.getString("COMPANY_START_DATE")))
                            + "',LASTMODDATE='"
                            + changeDateStr(rs1.getString("LASTMODIFY_TIME"))
                            + "',STATUS="
                            + (rs1.getString("EMP_STATUS").equals("在职")&&!rs1.getString("ACT").trim().equals("delete") ? 1
                            : 5)
                            + ",SECLEVEL='"
                            + secLevel
                            + "' where workcode='"
                            + rs1.getString("OPERATER_ID") + "'");

                    // 先将自定义表的数据清除，再插入自定义表的数据(因为有可能人员是存在的，但人员的自定义信息数据不存在，所以先删再增),以id为条件;
                    //rs2.execute("delete from cus_fielddata where id = '" + userID + "'");

                } else {
                    // 员工不存在，插入员工数据

                    // 处理该员工的头像图片(只在新增用户时处理用户头像)
                    String photo = null;

                    // 判断当前用户是否有头像
                    if (null != rs1.getString("PHOTO")
                            && !rs1.getString("PHOTO").trim().equals("")) {
                        // 先通过该员工的头像URL下载该员工的头像JPG到硬盘
                        URL url = null;
                        DataInputStream dataInputStream = null;
                        FileOutputStream fileOutputStream = null;
                        ByteArrayOutputStream output = null;
                        byte[] buffer = null;
                        try {
                            url = new URL(rs1.getString("PHOTO"));
                            dataInputStream = new DataInputStream(
                                    url.openStream());

                            fileOutputStream = new FileOutputStream(new File(
                                    File.separator + "home" + File.separator
                                            + "newoa" + File.separator
                                            + rs1.getString("OPERATER_ID")
                                            + ".jpg"));
                            output = new ByteArrayOutputStream();

                            buffer = new byte[1024];
                            int length = 0;

                            while ((length = dataInputStream.read(buffer)) > 0) {
                                output.write(buffer, 0, length);
                            }
                            fileOutputStream.write(output.toByteArray());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (dataInputStream != null) {
                                dataInputStream.close();
                                dataInputStream = null;
                            }
                            if (output != null) {
                                output.close();
                                output = null;
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                                fileOutputStream = null;
                            }
                        }

                        // 创建imagefile
                        File file = new File(File.separator + "home"
                                + File.separator + "newoa" + File.separator
                                + rs1.getString("OPERATER_ID") + ".jpg");
                        // 生成文件ID
                        int imagefileid = new ImageFileIdUpdate()
                                .getImageFileNewId();
                        new BaseBean().writeLog("生成的文件id为：" + imagefileid);
                        // 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
                        int createstatus = ImageFileUtil.createImageFile(
                                File.separator + "home" + File.separator
                                        + "newoa" + File.separator
                                        + rs1.getString("OPERATER_ID") + ".jpg",
                                imagefileid, file.getName(),
                                new FileInputStream(file).available());
                        new BaseBean().writeLog("创建图片结果为：" + createstatus);

                        if (createstatus > 0) {
                            photo = String.valueOf(imagefileid);
                        }

                        if (file.exists()) {
                            file.delete();
                        }
                    }

                    // 获取要插入的人员ID，调用存储过程HrmResourceMaxId_Get
                    rs2.executeProc("HrmResourceMaxId_Get", "");
                    rs2.next();
                    userID = rs2.getString(1);

                    rs2.execute("insert into HrmResource(id,loginid,PASSWORD,lastname,jobtitle,CERTIFICATENUM,MARITALSTATUS,RESOURCEIMAGEID,FOLK,SEX,BIRTHDAY,MOBILE,TELEPHONE,EMAIL,POLICY,workstartdate,companystartdate,LASTMODDATE,workcode,DEPARTMENTID,SUBCOMPANYID1,STATUS,SECLEVEL,creater,createdate) values('"
                            + userID
                            + "','"
                            + rs1.getString("OPERATER_ID")
                            + "','E10ADC3949BA59ABBE56E057F20F883E','"
                            + rs1.getString("EMP_NAME")
//							+ "','"
//							+ managerID
							+ "','"
							+ jobTitleId
                            + "','"
                            + Util.null2String(rs1.getString("NATIONAL_IDENTIFIER"))
                            + "',"
                            + maritalStatus
                            + ","
                            + photo
                            + ",'"
                            + Util.null2String(rs1.getString("NATIONAL_MZ"))
                            + "',"
                            + sex
                            + ",'"
                            + changeDateStr(rs1.getString("DATE_OF_BIRTH"))
                            + "','"
                            + Util.null2String(rs1.getString("MOBILE"))
                            + "','"
                            + Util.null2String(rs1.getString("OFF_TEL"))
                            + "','"
                            //+ Util.null2String(rs1.getString("MAILSTOP"))
                            //+ "','"
                            + Util.null2String(rs1.getString("EMAIL"))
                            + "','"
                            + Util.null2String(rs1.getString("POLITICAL_FACE"))
                            + "','"
                            + changeDateStr(Util.null2String(rs1.getString("WORK_START_DATE")))
                            + "','"
                            + changeDateStr(Util.null2String(rs1.getString("COMPANY_START_DATE")))
                            + "','"
                            + changeDateStr(rs1.getString("LASTMODIFY_TIME"))
                            + "','"
                            + rs1.getString("OPERATER_ID")
                            + "','"
                            + supDeptID
                            + "','"
                            + subCompanyID
                            + "',"
                            + (rs1.getString("EMP_STATUS").equals("在职") ? 1
                            : 5) + ",'"
                            + secLevel
                            + "',1,'"
                            + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                            + "')");

                }
                rs2.execute("update UF_EHR_HRM_DATA set IS_SYN=1 where id="
                        + rs1.getString("id"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            rs1 = null;
            rs2 = null;
        }
    }

    /**
     * 转换时间字符串格式
     *
     * @param dateStr yyyyMMdd的时间字符串
     * @return 返回 yyyy-MM-dd 的时间字符串
     */
    public static String changeDateStr(String dateStr) {
        if (null != dateStr && !"".equals(dateStr.trim())) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd")
                        .format(new SimpleDateFormat("yyyyMMdd").parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
