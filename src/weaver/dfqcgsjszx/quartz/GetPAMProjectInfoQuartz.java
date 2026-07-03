package weaver.dfqcgsjszx.quartz;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.PAM.project.service_client.PamProjectServiceProxy;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * 获取PAM的项目信息
 *
 * @author Alex.Du
 */
public class GetPAMProjectInfoQuartz extends BaseCronJob {
    @Override
    public void execute() {
        new BaseBean().writeLog("[GetPAMProjectInfoQuartz]开始执行获取PAM的项目信息");
        PamProjectServiceProxy proxy = new PamProjectServiceProxy();
        RecordSet rs = new RecordSet();
        //2021-01-04 02:30:03,761 INFO  A2  - [null] MyClusteredScheduler_Worker-24-126660[weaver.dfqcgsjszx.quartz.GetPAMProjectInfoQuartz:90] - [GetPAMProjectInfoQuartz]执行更新语句：update uf_xmxxk set xmmc='乘用车新能源市场用户与整车试验场道路耐久关联性研究',xmzrbm='2526',xmzrbmbm = '170000',xmfzr = '6141',xmfzrgh='8093147',xmzt='A',cbzgbh='8155233',cbzg='4597',xmfylx='54',wbddh='',gxsj='2021-01-04' where xmbh='91224Y190056'
        try {
            //调用接口获取项目信息
            String projectInfos = proxy.getProject("COS");
            new BaseBean().writeLog("[GetPAMProjectInfoQuartz]PAM接口返回的数据projectInfos:"+projectInfos);

            //解析返回的XML数据
            Document document = new SAXReader().read(new StringReader(projectInfos));
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();

            //更新日期，时间
            String gxrq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String gxsj = new SimpleDateFormat("HH:mm:ss").format(new Date());
            while (iterator.hasNext()) {
                try {
                    Element row = (Element) iterator.next();
                    //项目名称
                    String xmmc = row.attribute("PROJ_NAME").getValue().trim();
                    //项目编号
                    String xmbh = row.attribute("PROJ_CODE").getValue().trim();
                    //项目责任部门
                    String xmzrbm = "";
                    //项目责任部门编码
                    String xmzrbmbm = row.attribute("PROJ_DEPT_CODE").getValue().trim();
                    //项目负责人
                    String xmfzr = "";
                    //项目负责人工号
                    String xmfzrgh = row.attribute("PROJ_PERSON_NUM").getValue().trim();
                    //项目状态
                    String xmzt = row.attribute("PROJ_STATUS").getValue().trim();
                    //成本主管编号
                    String cbzgbh = row.attribute("COST_SUPERVISORS_CODE").getValue().trim();
                    //成本主管
                    String cbzg = "";
                    //项目费用类型
                    String xmfylx = row.attribute("ABKRS").getValue().trim();
                    //外部订单号
                    String wbddh = row.attribute("AUFEX").getValue().trim();
                    //获取所属平台字段 SS_PT
                    String ssptStr = row.attribute("SS_PT").getValue().trim();



                    //处理数据
                    //处理所属平台自定义浏览按钮
                    String ssptid = "";
                    //查询数据库id
                    if(!ssptStr.trim().equals("")){
                        RecordSet rsSSPT = new RecordSet();
                        rsSSPT.execute("select id from uf_cggl_xmsspt where ptmc = '" + ssptStr + "'");
                        if(rsSSPT.next()){
                            //数据存在 获取ID
                            ssptid = rsSSPT.getString("id");
                        }else {
                            //数据不存在执行新建
                            new BaseBean().writeLog("[GetPAMProjectInfoQuartz]未查询到所属平台类型 执行所属平台类型新增 SQL"
                                    +"insert into uf_cggl_xmsspt(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,ptmc) values('84535','1','0','"
                                    + gxrq + "','"
                                    + gxsj + "','"
                                    + UUID.randomUUID() + "','"
                                    + ssptStr + "')");

                            rsSSPT.execute("insert into uf_cggl_xmsspt(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,ptmc) values('84535','1','0','"
                                    + gxrq + "','"
                                    + gxsj + "','"
                                    + UUID.randomUUID() + "','"
                                    + ssptStr + "')");

                            rsSSPT.execute("select max(id) as id from uf_cggl_xmsspt");

                            if (rsSSPT.next()) {
                                ssptid = rsSSPT.getString("id");
                                new ModeRightInfo().editModeDataShare(1, 84535, rsSSPT.getInt("id"));
                            }
                        }
                    }



                    //处理项目责任部门数据（获取部门id）
                    rs.execute("select id from hrmdepartment where departmentcode = '" + xmzrbmbm + "'");
                    if (rs.next()) {
                        xmzrbm = rs.getString("id");
                    }

                    //处理项目负责人数据（获取人员id）
                    rs.execute("select id from hrmresource where workcode = '" + xmfzrgh + "'");
                    if (rs.next()) {
                        xmfzr = rs.getString("id");
                    }

                    //处理成本主管数据（获取人员id）
                    rs.execute("select id from hrmresource where workcode = '" + cbzgbh + "'");
                    if (rs.next()) {
                        cbzg = rs.getString("id");
                    }

                    //通过项目编号查询该项目信息在建模表中是否存在
                    rs.execute("select * from uf_xmxxk where xmbh = '" + xmbh + "'");
                    if (rs.next()) {
                        new BaseBean().writeLog("[GetPAMProjectInfoQuartz]执行更新语句：update uf_xmxxk set xmmc='" + xmmc
                                + "',xmzrbm='" + xmzrbm
                                + "',xmzrbmbm = '" + xmzrbmbm
                                + "',xmfzr = '" + xmfzr
                                + "',xmfzrgh='" + xmfzrgh
                                + "',xmzt='" + xmzt
                                + "',cbzgbh='" + cbzgbh
                                + "',cbzg='" + cbzg
                                + "',xmfylx='" + xmfylx
                                + "',wbddh='" + wbddh
                                + "',gxsj='" + gxrq
                                + "',sspt='" + ssptid
                                + "' where xmbh='" + xmbh + "'");
                        //数据存在，则更新
                        rs.execute("update uf_xmxxk set xmmc='" + xmmc
                                + "',xmzrbm='" + xmzrbm
                                + "',xmzrbmbm = '" + xmzrbmbm
                                + "',xmfzr = '" + xmfzr
                                + "',xmfzrgh='" + xmfzrgh
                                + "',xmzt='" + xmzt
                                + "',cbzgbh='" + cbzgbh
                                + "',cbzg='" + cbzg
                                + "',xmfylx='" + xmfylx
                                + "',wbddh='" + wbddh
                                + "',gxsj='" + gxrq
                                + "',sspt='" + ssptid
                                + "' where xmbh='" + xmbh + "'");
                    } else {
                        new BaseBean().writeLog("[GetPAMProjectInfoQuartz]执行更新语句：insert into uf_xmxxk(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,xmmc,xmbh,xmzrbm,xmzrbmbm,xmfzr,xmfzrgh,xmzt,cbzgbh,cbzg,xmfylx,wbddh,sspt,gxsj) values('73001','1','0','"
                                + gxrq + "','"
                                + gxsj + "','"
                                + UUID.randomUUID() + "','"
                                + xmmc + "','"
                                + xmbh + "','"
                                + xmzrbm + "','"
                                + xmzrbmbm + "','"
                                + xmfzr + "','"
                                + xmfzrgh + "','"
                                + xmzt + "','"
                                + cbzgbh + "','"
                                + cbzg + "','"
                                + xmfylx + "','"
                                + wbddh + "','"
                                + ssptid + "','"
                                + gxrq + "')");
                        //数据不存在，则插入
                        rs.execute("insert into uf_xmxxk(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,xmmc,xmbh,xmzrbm,xmzrbmbm,xmfzr,xmfzrgh,xmzt,cbzgbh,cbzg,xmfylx,wbddh,sspt,gxsj) values('73001','1','0','"
                                + gxrq + "','"
                                + gxsj + "','"
                                + UUID.randomUUID() + "','"
                                + xmmc + "','"
                                + xmbh + "','"
                                + xmzrbm + "','"
                                + xmzrbmbm + "','"
                                + xmfzr + "','"
                                + xmfzrgh + "','"
                                + xmzt + "','"
                                + cbzgbh + "','"
                                + cbzg + "','"
                                + xmfylx + "','"
                                + wbddh + "','"
                                + ssptid + "','"
                                + gxrq + "')");

                        //进行权限重构
                        rs.execute("select max(id) as id from uf_xmxxk");
                        if (rs.next()) {
                            new ModeRightInfo().editModeDataShare(1, 72001, rs.getInt("id"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new BaseBean().writeLog("处理单个项目信息时出现异常：" + e.getMessage());
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            new BaseBean().writeLog("获取PAM的项目信息出现异常：" + e.getMessage());
        }finally{
            proxy = null;
            rs = null;
        }
        new BaseBean().writeLog("[GetPAMProjectInfoQuartz]执行获取PAM的项目信息完毕");
    }
}
