package weaver.dfqcgsjszx.listener;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.quartz.SynEHROrgHrmQuartz;
import weaver.general.BaseBean;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 老PI（EHR）组织、人员数据的队列获取监听器，将数据存储到中间建模表
 *
 * @author Alex.Du
 */
public class OldPIHornetQListener implements ServletContextListener {
    private BaseBean baseBean = new BaseBean();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Session session = (Session) sce.getServletContext().getAttribute(
                    "piSession");
            if (session != null) {
                session.close();
                session = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = (Connection) sce.getServletContext()
                    .getAttribute("piConn");
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("**************************开始启动PI的HonnetQ监听*********************");
        baseBean.writeLog("**************************开始启动PI的HonnetQ监听*********************");

        QueueConnection conn = null;
        QueueSession session = null;

        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.jnp.interfaces.NamingContextFactory");
            properties.put(Context.URL_PKG_PREFIXES,
                    "org.jnp.interfaces:org.jboss.naming");

            Properties propFile = new Properties();
            // 使用InPutStream流读取properties文件
            BufferedReader bufferedReader = null;

            String providerURL = "";
            try {
                bufferedReader = new BufferedReader(new FileReader("/home/weaver/ecology/WEB-INF/prop/integration_address_config.properties"));
                propFile.load(bufferedReader);
                // 获取key对应的value值
                propFile.getProperty("old_pi_hornet_ip");
                providerURL = "jnp://" + propFile.getProperty("old_pi_hornet_ip");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("加载properties文件读取url地址出现异常：" + e.getMessage());
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                    bufferedReader = null;
                }
                propFile = null;
            }


            System.out.println("providerURL:" + providerURL);
            baseBean.writeLog("providerURL:" + providerURL);


            properties.put(Context.PROVIDER_URL, providerURL);
            InitialContext iniCtx = new InitialContext(properties);
            Object tmp = iniCtx.lookup("java:/XAConnectionFactory");
            QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
            conn = qcf.createQueueConnection();
            Queue que = (Queue) iniCtx.lookup("queue/dftc-cos_gw");
            session = conn.createQueueSession(false,
                    QueueSession.AUTO_ACKNOWLEDGE);
            conn.start();
            //MessageConsumer consumer = session.createConsumer(que);

            QueueReceiver receiver = session.createReceiver(que);
//            MessageConsumer consumer = session.createConsumer(que);
//            ObjectMessage recivedMessage = (ObjectMessage)consumer.receive(5000);

            baseBean.writeLog("队列连接创建成功");
            System.out.println("队列连接创建成功");

            receiver.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    //收到消息
                    baseBean.writeLog("收到消息数据");
                    System.out.println("收到消息数据");

                    String contentXML = null;
                    byte[] contentByte = null;

                    try {
                        baseBean.writeLog("即将进行数据对象转换");
                        System.out.println("即将进行数据对象转换");
                        ObjectMessage objMessage = (ObjectMessage) message;
                        if (objMessage.getObject() instanceof byte[]) {
                            baseBean.writeLog("数据类型是byte[]");
                            System.out.println("数据类型是byte[]");
                            contentByte = (byte[]) objMessage.getObject();
                            baseBean.writeLog("获取到的内容为：" + new String(contentByte, "UTF-8"));
                            System.out.println("获取到的内容为：" + new String(contentByte, "UTF-8"));
                            contentXML = new String(contentByte, "UTF-8");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        baseBean.writeLog("解析PT消息内容出现异常:" + e.getMessage());
                        System.out.println("解析PT消息内容出现异常:" + e.getMessage());
                    }

                    //判断是否解析出对应内容
                    if (contentXML != null && !contentXML.trim().equals("")) {
                        Document doc = null;
                        try {
                            doc = DocumentHelper.parseText(contentXML);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        Element esbEnvelop = doc.getRootElement();// 指向根节点  <root>
                        try {
                            Element tradeCode = esbEnvelop.element("ESBBody").element("AppRequest").element("AppReqHead").element("TradeCode");
                            //获取xml的节点内容
                            baseBean.writeLog("tradeCode:" + tradeCode.getTextTrim());
                            System.out.println("tradeCode:" + tradeCode.getTextTrim());
                            //获取数据节点集合
                            List<Element> rowList = esbEnvelop.element("ESBBody").element("AppRequest").element("AppReqBody").element("table").element("rows").elements("row");
                            for (int i = 0; i < rowList.size(); i++) {
                                Element row = rowList.get(i);

                                //根据TradeCode节点的内容，判断数据类型(PT-COS-01为人员数据,PT-COS-02为组织数据）
                                if (tradeCode.getTextTrim().equals("PT-COS-01")) {
                                    //人员信息
                                    try {
                                        saveSynHrmInfo(row);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        baseBean.writeLog("存储同步的人员信息出现异常：" + e.getMessage());
                                        System.out.println("存储同步的人员信息出现异常：" + e.getMessage());
                                        continue;
                                    }
                                } else if (tradeCode.getTextTrim().equals("PT-COS-02")) {
                                    //组织信息
                                    try {
                                        saveSynOrgInfo(row);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        baseBean.writeLog("存储同步的组织信息出现异常：" + e.getMessage());
                                        System.out.println("存储同步的组织信息出现异常：" + e.getMessage());
                                        continue;
                                    }
                                }
                            }

                            baseBean.writeLog("数据同步到中间表结束，开始执行同步数据处理");
                            System.out.println("数据同步到中间表结束，开始执行同步数据处理");
                            new SynEHROrgHrmQuartz().execute();
                            baseBean.writeLog("执行同步数据处理结束");
                            System.out.println("执行同步数据处理结束");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            sce.getServletContext().setAttribute("piSession", session);
            sce.getServletContext().setAttribute("piConn", conn);

            baseBean.writeLog("**************************启动PI的HonnetQ监听成功*********************");
            System.out.println("**************************启动PI的HonnetQ监听成功*********************");
        } catch (Exception e) {
            e.printStackTrace();
            baseBean.writeLog("启动PI的HonnetQ监听出现异常:" + e.getMessage());
            baseBean.writeLog("**************************启动PI的HonnetQ监听失败*********************");
            System.out.println("**************************启动PI的HonnetQ监听失败*********************");
        }
    }


    /**
     * 存储同步的组织信息
     *
     * @param row 数据行
     * @throws Exception
     */
    private void saveSynOrgInfo(Element row) throws Exception {
        new BaseBean().writeLog("进入存储同步组织信息的方法");
        RecordSet rs = null;
        try {
            new BaseBean().writeLog("开始实例化RecordSet");
            rs = new RecordSet();
            new BaseBean().writeLog("RecordSet实例化完毕");
            // 通过部门ID查找该部门是否已经存在
            new BaseBean().writeLog("通过部门ID查找部门是否存在的SQL为：select id from uf_ehr_org_data where ORG_ID = '"
                    + row.attribute("ORG_ID").getValue() + "'");
            rs.execute("select id from uf_ehr_org_data where ORG_ID = '"
                    + row.attribute("ORG_ID").getValue() + "'");
            new BaseBean().writeLog("通过部门id查找到" + rs.getCounts() + "条");

            if (rs.next()) {
                new BaseBean().writeLog("进入修改部门信息");
                new BaseBean().writeLog("当前部门存在，更新此部门的SQL为：update uf_ehr_org_data set ORG_ID='" + row.attribute("ORG_ID").getValue()
                        + "',ORG_CODE='" + row.attribute("ORG_CODE").getValue()
                        + "',ORG_NAME='" + row.attribute("ORG_NAME").getValue()
                        + "',PARENT_ORG_ID='" + row.attribute("PARENT_ORG_ID").getValue()
                        + "',ORG_ADDRESS='" + row.attribute("ORG_ADDRESS").getValue()
                        + "',ZIP_CODE='" + row.attribute("ZIP_CODE").getValue()
                        + "',CONTACT_PERSON='" + row.attribute("CONTACT_PERSON").getValue()
                        + "',CONTACT_PERSON_TEL='" + row.attribute("CONTACT_PERSON_TEL").getValue()
                        + "',EMAIL='" + row.attribute("EMAIL").getValue()
                        + "',WEB_URL='" + row.attribute("WEB_URL").getValue()
                        + "',MEMO='" + row.attribute("MEMO").getValue()
                        + "',ORG_STATUS='" + row.attribute("ORG_STATUS").getValue()
                        + "',ORG_CLASS='" + row.attribute("ORG_CLASS").getValue()
                        + "',ACT='" + row.attribute("ACT").getValue()
                        + "',IS_SYN=0,LAST_UPDATE_TIME='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "' where ORG_ID = '"
                        + row.attribute("ORG_ID").getValue() + "'");
                // 如果部门已经存在，则更新
                rs.execute("update uf_ehr_org_data set ORG_ID='" + row.attribute("ORG_ID").getValue()
                        + "',ORG_CODE='" + row.attribute("ORG_CODE").getValue()
                        + "',ORG_NAME='" + row.attribute("ORG_NAME").getValue()
                        + "',PARENT_ORG_ID='" + row.attribute("PARENT_ORG_ID").getValue()
                        + "',ORG_ADDRESS='" + row.attribute("ORG_ADDRESS").getValue()
                        + "',ZIP_CODE='" + row.attribute("ZIP_CODE").getValue()
                        + "',CONTACT_PERSON='" + row.attribute("CONTACT_PERSON").getValue()
                        + "',CONTACT_PERSON_TEL='" + row.attribute("CONTACT_PERSON_TEL").getValue()
                        + "',EMAIL='" + row.attribute("EMAIL").getValue()
                        + "',WEB_URL='" + row.attribute("WEB_URL").getValue()
                        + "',MEMO='" + row.attribute("MEMO").getValue()
                        + "',ORG_STATUS='" + row.attribute("ORG_STATUS").getValue()
                        + "',ORG_CLASS='" + row.attribute("ORG_CLASS").getValue()
                        + "',ACT='" + row.attribute("ACT").getValue()
                        + "',IS_SYN=0,LAST_UPDATE_TIME='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "' where ORG_ID = '"
                        + row.attribute("ORG_ID").getValue() + "'");
            } else {
                new BaseBean().writeLog("进入新增部门信息");
                new BaseBean().writeLog("当前部门不存在，新建此部门的SQL为：insert into uf_ehr_org_data(ORG_ID,ORG_CODE,ORG_NAME,PARENT_ORG_ID,ORG_ADDRESS,ZIP_CODE,CONTACT_PERSON,CONTACT_PERSON_TEL,EMAIL,WEB_URL,MEMO,ORG_STATUS,ORG_CLASS,ACT,IS_SYN,LAST_UPDATE_TIME) values('" + row.attribute("ORG_ID").getValue()
                        + "','" + row.attribute("ORG_CODE").getValue()
                        + "','" + row.attribute("ORG_NAME").getValue()
                        + "','" + row.attribute("PARENT_ORG_ID").getValue()
                        + "','" + row.attribute("ORG_ADDRESS").getValue()
                        + "','" + row.attribute("ZIP_CODE").getValue()
                        + "','" + row.attribute("CONTACT_PERSON").getValue()
                        + "','" + row.attribute("CONTACT_PERSON_TEL").getValue()
                        + "','" + row.attribute("EMAIL").getValue()
                        + "','" + row.attribute("WEB_URL").getValue()
                        + "','" + row.attribute("MEMO").getValue()
                        + "','" + row.attribute("ORG_STATUS").getValue()
                        + "','" + row.attribute("ORG_CLASS").getValue()
                        + "','" + row.attribute("ACT").getValue()
                        + "',0,'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "')");
                // 如果部门不存在，则新建
                rs.execute("insert into uf_ehr_org_data(ORG_ID,ORG_CODE,ORG_NAME,PARENT_ORG_ID,ORG_ADDRESS,ZIP_CODE,CONTACT_PERSON,CONTACT_PERSON_TEL,EMAIL,WEB_URL,MEMO,ORG_STATUS,ORG_CLASS,ACT,IS_SYN,LAST_UPDATE_TIME) values('" + row.attribute("ORG_ID").getValue()
                        + "','" + row.attribute("ORG_CODE").getValue()
                        + "','" + row.attribute("ORG_NAME").getValue()
                        + "','" + row.attribute("PARENT_ORG_ID").getValue()
                        + "','" + row.attribute("ORG_ADDRESS").getValue()
                        + "','" + row.attribute("ZIP_CODE").getValue()
                        + "','" + row.attribute("CONTACT_PERSON").getValue()
                        + "','" + row.attribute("CONTACT_PERSON_TEL").getValue()
                        + "','" + row.attribute("EMAIL").getValue()
                        + "','" + row.attribute("WEB_URL").getValue()
                        + "','" + row.attribute("MEMO").getValue()
                        + "','" + row.attribute("ORG_STATUS").getValue()
                        + "','" + row.attribute("ORG_CLASS").getValue()
                        + "','" + row.attribute("ACT").getValue()
                        + "',0,'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "')");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            rs = null;
        }
    }

    /**
     * 存储同步的人员信息
     *
     * @param row 数据行
     * @throws Exception
     */
    private void saveSynHrmInfo(Element row) throws Exception {
        new BaseBean().writeLog("进入存储同步的人员信息");
        RecordSet rs = new RecordSet();

        try {
            rs.execute("select id from uf_ehr_hrm_data where EMP_CODE = '"
                    + row.attribute("EMP_CODE").getValue() + "'");

            if (rs.next()) {
                new BaseBean().writeLog("进入更新人员的语句");
                new BaseBean().writeLog("进入更新人员的语句：update uf_ehr_hrm_data set OPERATER_ID='" + row.attribute("OPERATER_ID").getValue()
                        + "',EMP_CODE='" + row.attribute("EMP_CODE").getValue()

                        + "',SUSPEND_OTHER_SIGN='" + row.attribute("SUSPEND_OTHER_SIGN").getValue()
                        + "',PT_PERSON_TYPE='" + row.attribute("PT_PERSON_TYPE").getValue()
                        + "',NOW_PT_LIST='" + row.attribute("NOW_PT_LIST").getValue()
                        + "',PT_PERSON_GRADE='" + row.attribute("PT_PERSON_GRADE").getValue()

                        + "',ORG_ID='" + row.attribute("ORG_ID").getValue()
                        + "',OFF_TEL='" + row.attribute("OFF_TEL").getValue()
                        + "',EMAIL='" + row.attribute("EMAIL").getValue()
                        + "',REG_DATE='" + row.attribute("REG_DATE").getValue()
                        + "',MOBILE='" + row.attribute("MOBILE").getValue()
                        + "',CREATE_TIME='" + row.attribute("CREATE_TIME").getValue()
                        + "',LASTMODIFY_TIME='" + row.attribute("LASTMODIFY_TIME").getValue()
                        + "',EMP_STATUS='" + row.attribute("EMP_STATUS").getValue()
                        + "',EMP_NAME='" + row.attribute("EMP_NAME").getValue()
                        + "',PASSWD='" + row.attribute("PASSWD").getValue()
                        + "',EMAIL_DESIGN='" + row.attribute("EMAIL_DESIGN").getValue()
                        + "',EMP_Position='" + row.attribute("EMP_Position").getValue()
                        + "',EMP_Position_label='" + row.attribute("EMP_Position_label").getValue()
                        + "',OFFICE='" + row.attribute("OFFICE").getValue()
                        + "',SEX='" + row.attribute("SEX").getValue()
                        + "',POSITION_ID='" + row.attribute("POSITION_ID").getValue()
                        + "',POSITION_NAME='" + row.attribute("POSITION_NAME").getValue()
                        + "',JOB_ID='" + row.attribute("JOB_ID").getValue()
                        + "',JOB_NAME='" + row.attribute("JOB_NAME").getValue()
                        + "',PERSON_TYPE='" + row.attribute("PERSON_TYPE").getValue()
                        + "',SUPERVISOR_NUMBER='" + row.attribute("SUPERVISOR_NUMBER").getValue()
                        + "',SUPERVISOR_NAME='" + row.attribute("SUPERVISOR_NAME").getValue()
                        + "',DATE_OF_BIRTH='" + row.attribute("DATE_OF_BIRTH").getValue()
                        + "',NATIONAL_IDENTIFIER='" + row.attribute("NATIONAL_IDENTIFIER").getValue()
                        + "',NATIONALITY='" + row.attribute("NATIONALITY").getValue()
                        + "',NATIONAL_MZ='" + row.attribute("NATIONAL").getValue()
                        + "',POLITICAL_FACE='" + row.attribute("POLITICAL_FACE").getValue()
                        + "',GRADE_ID='" + row.attribute("GRADE_ID").getValue()
                        + "',GRADE_NAME='" + row.attribute("GRADE_NAME").getValue()
                        + "',GRADE_TYPE='" + row.attribute("GRADE_TYPE").getValue()
                        + "',EMPLOYMENT_CATEGORY_MEANING='" + row.attribute("EMPLOYMENT_CATEGORY_MEANING").getValue()
                        + "',EMPLOYEE_CATEGORY_MEANING='" + row.attribute("EMPLOYEE_CATEGORY_MEANING").getValue()
                        + "',CUR_TIME='" + row.attribute("CUR_TIME").getValue()
                        + "',ROW_ID='" + row.attribute("ROW_ID").getValue()
                        + "',BATCH_ID='" + row.attribute("BATCH_ID").getValue()
                        + "',ROW_ID_OPER='" + row.attribute("ROW_ID_OPER").getValue()
                        + "',ACT='" + row.attribute("ACT").getValue()
                        + "',WORK_START_DATE='" + row.attribute("WORK_START_DATE").getValue()
                        + "',COMPANY_START_DATE='" + row.attribute("COMPANY_START_DATE").getValue()
                        + "',MARITAL_STATUS='" + row.attribute("MARITAL_STATUS").getValue()
                        + "',PHOTO='" + row.attribute("PHOTO").getValue()
                        + "',IS_SYN=0,LAST_UPDATE_TIME='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "' where EMP_CODE = '"
                        + row.attribute("EMP_CODE").getValue() + "'");
                // 如果人员已经存在，则更新
                rs.execute("update uf_ehr_hrm_data set OPERATER_ID='" + row.attribute("OPERATER_ID").getValue()
                        + "',EMP_CODE='" + row.attribute("EMP_CODE").getValue()

                        + "',SUSPEND_OTHER_SIGN='" + row.attribute("SUSPEND_OTHER_SIGN").getValue()
                        + "',PT_PERSON_TYPE='" + row.attribute("PT_PERSON_TYPE").getValue()
                        + "',NOW_PT_LIST='" + row.attribute("NOW_PT_LIST").getValue()
                        + "',PT_PERSON_GRADE='" + row.attribute("PT_PERSON_GRADE").getValue()

                        + "',ORG_ID='" + row.attribute("ORG_ID").getValue()
                        + "',OFF_TEL='" + row.attribute("OFF_TEL").getValue()
                        + "',EMAIL='" + row.attribute("EMAIL").getValue()
                        + "',REG_DATE='" + row.attribute("REG_DATE").getValue()
                        + "',MOBILE='" + row.attribute("MOBILE").getValue()
                        + "',CREATE_TIME='" + row.attribute("CREATE_TIME").getValue()
                        + "',LASTMODIFY_TIME='" + row.attribute("LASTMODIFY_TIME").getValue()
                        + "',EMP_STATUS='" + row.attribute("EMP_STATUS").getValue()
                        + "',EMP_NAME='" + row.attribute("EMP_NAME").getValue()
                        + "',PASSWD='" + row.attribute("PASSWD").getValue()
                        + "',EMAIL_DESIGN='" + row.attribute("EMAIL_DESIGN").getValue()
                        + "',EMP_Position='" + row.attribute("EMP_Position").getValue()
                        + "',EMP_Position_label='" + row.attribute("EMP_Position_label").getValue()
                        + "',OFFICE='" + row.attribute("OFFICE").getValue()
                        + "',SEX='" + row.attribute("SEX").getValue()
                        + "',POSITION_ID='" + row.attribute("POSITION_ID").getValue()
                        + "',POSITION_NAME='" + row.attribute("POSITION_NAME").getValue()
                        + "',JOB_ID='" + row.attribute("JOB_ID").getValue()
                        + "',JOB_NAME='" + row.attribute("JOB_NAME").getValue()
                        + "',PERSON_TYPE='" + row.attribute("PERSON_TYPE").getValue()
                        + "',SUPERVISOR_NUMBER='" + row.attribute("SUPERVISOR_NUMBER").getValue()
                        + "',SUPERVISOR_NAME='" + row.attribute("SUPERVISOR_NAME").getValue()
                        + "',DATE_OF_BIRTH='" + row.attribute("DATE_OF_BIRTH").getValue()
                        + "',NATIONAL_IDENTIFIER='" + row.attribute("NATIONAL_IDENTIFIER").getValue()
                        + "',NATIONALITY='" + row.attribute("NATIONALITY").getValue()
                        + "',NATIONAL_MZ='" + row.attribute("NATIONAL").getValue()
                        + "',POLITICAL_FACE='" + row.attribute("POLITICAL_FACE").getValue()
                        + "',GRADE_ID='" + row.attribute("GRADE_ID").getValue()
                        + "',GRADE_NAME='" + row.attribute("GRADE_NAME").getValue()
                        + "',GRADE_TYPE='" + row.attribute("GRADE_TYPE").getValue()
                        + "',EMPLOYMENT_CATEGORY_MEANING='" + row.attribute("EMPLOYMENT_CATEGORY_MEANING").getValue()
                        + "',EMPLOYEE_CATEGORY_MEANING='" + row.attribute("EMPLOYEE_CATEGORY_MEANING").getValue()
                        + "',CUR_TIME='" + row.attribute("CUR_TIME").getValue()
                        + "',ROW_ID='" + row.attribute("ROW_ID").getValue()
                        + "',BATCH_ID='" + row.attribute("BATCH_ID").getValue()
                        + "',ROW_ID_OPER='" + row.attribute("ROW_ID_OPER").getValue()
                        + "',ACT='" + row.attribute("ACT").getValue()
                        + "',WORK_START_DATE='" + row.attribute("WORK_START_DATE").getValue()
                        + "',COMPANY_START_DATE='" + row.attribute("COMPANY_START_DATE").getValue()
                        + "',MARITAL_STATUS='" + row.attribute("MARITAL_STATUS").getValue()
                        + "',PHOTO='" + row.attribute("PHOTO").getValue()
                        + "',IS_SYN=0,LAST_UPDATE_TIME='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "' where EMP_CODE = '"
                        + row.attribute("EMP_CODE").getValue() + "'");
            } else {
                // 如果人员不存在，则新建
                new BaseBean().writeLog("进入插入人员的语句");
                new BaseBean().writeLog("进入插入人员的语句：insert into uf_ehr_hrm_data(OPERATER_ID,EMP_CODE,SUSPEND_OTHER_SIGN,PT_PERSON_TYPE,NOW_PT_LIST,PT_PERSON_GRADE,ORG_ID,OFF_TEL,EMAIL,REG_DATE,MOBILE,CREATE_TIME,LASTMODIFY_TIME,EMP_STATUS,EMP_NAME,PASSWD,EMAIL_DESIGN,EMP_Position,EMP_Position_label,OFFICE,SEX,POSITION_ID,POSITION_NAME,JOB_ID,JOB_NAME,PERSON_TYPE,SUPERVISOR_NUMBER,SUPERVISOR_NAME,DATE_OF_BIRTH,NATIONAL_IDENTIFIER,NATIONALITY,NATIONAL_MZ,POLITICAL_FACE,GRADE_ID,GRADE_NAME,GRADE_TYPE,EMPLOYMENT_CATEGORY_MEANING,EMPLOYEE_CATEGORY_MEANING,CUR_TIME,ROW_ID,BATCH_ID,ROW_ID_OPER,ACT,WORK_START_DATE,COMPANY_START_DATE,MARITAL_STATUS,PHOTO,IS_SYN,LAST_UPDATE_TIME) values('" + row.attribute("OPERATER_ID").getValue()
                        + "','" + row.attribute("EMP_CODE").getValue()

                        + "','" + row.attribute("SUSPEND_OTHER_SIGN").getValue()
                        + "','" + row.attribute("PT_PERSON_TYPE").getValue()
                        + "','" +  row.attribute("NOW_PT_LIST").getValue()
                        + "','" + row.attribute("PT_PERSON_GRADE").getValue()

                        + "','" + row.attribute("ORG_ID").getValue()
                        + "','" + row.attribute("OFF_TEL").getValue()
                        + "','" + row.attribute("EMAIL").getValue()
                        + "','" + row.attribute("REG_DATE").getValue()
                        + "','" + row.attribute("MOBILE").getValue()
                        + "','" + row.attribute("CREATE_TIME").getValue()
                        + "','" + row.attribute("LASTMODIFY_TIME").getValue()
                        + "','" + row.attribute("EMP_STATUS").getValue()
                        + "','" + row.attribute("EMP_NAME").getValue()
                        + "','" + row.attribute("PASSWD").getValue()
                        + "','" + row.attribute("EMAIL_DESIGN").getValue()
                        + "','" + row.attribute("EMP_Position").getValue()
                        + "','" + row.attribute("EMP_Position_label").getValue()
                        + "','" + row.attribute("OFFICE").getValue()
                        + "','" + row.attribute("SEX").getValue()
                        + "','" + row.attribute("POSITION_ID").getValue()
                        + "','" + row.attribute("POSITION_NAME").getValue()
                        + "','" + row.attribute("JOB_ID").getValue()
                        + "','" + row.attribute("JOB_NAME").getValue()
                        + "','" + row.attribute("PERSON_TYPE").getValue()
                        + "','" + row.attribute("SUPERVISOR_NUMBER").getValue()
                        + "','" + row.attribute("SUPERVISOR_NAME").getValue()
                        + "','" + row.attribute("DATE_OF_BIRTH").getValue()
                        + "','" + row.attribute("NATIONAL_IDENTIFIER").getValue()
                        + "','" + row.attribute("NATIONALITY").getValue()
                        + "','" + row.attribute("NATIONAL").getValue()
                        + "','" + row.attribute("POLITICAL_FACE").getValue()
                        + "','" + row.attribute("GRADE_ID").getValue()
                        + "','" + row.attribute("GRADE_NAME").getValue()
                        + "','" + row.attribute("GRADE_TYPE").getValue()
                        + "','" + row.attribute("EMPLOYMENT_CATEGORY_MEANING").getValue()
                        + "','" + row.attribute("EMPLOYEE_CATEGORY_MEANING").getValue()
                        + "','" + row.attribute("CUR_TIME").getValue()
                        + "','" + row.attribute("ROW_ID").getValue()
                        + "','" + row.attribute("BATCH_ID").getValue()
                        + "','" + row.attribute("ROW_ID_OPER").getValue()
                        + "','" + row.attribute("ACT").getValue()
                        + "','" + row.attribute("WORK_START_DATE").getValue()
                        + "','" + row.attribute("COMPANY_START_DATE").getValue()
                        + "','" + row.attribute("MARITAL_STATUS").getValue()
                        + "','" + row.attribute("PHOTO").getValue()
                        + "',0,'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "')");

                rs.execute("insert into uf_ehr_hrm_data(OPERATER_ID,EMP_CODE,SUSPEND_OTHER_SIGN,PT_PERSON_TYPE,NOW_PT_LIST,PT_PERSON_GRADE,ORG_ID,OFF_TEL,EMAIL,REG_DATE,MOBILE,CREATE_TIME,LASTMODIFY_TIME,EMP_STATUS,EMP_NAME,PASSWD,EMAIL_DESIGN,EMP_Position,EMP_Position_label,OFFICE,SEX,POSITION_ID,POSITION_NAME,JOB_ID,JOB_NAME,PERSON_TYPE,SUPERVISOR_NUMBER,SUPERVISOR_NAME,DATE_OF_BIRTH,NATIONAL_IDENTIFIER,NATIONALITY,NATIONAL_MZ,POLITICAL_FACE,GRADE_ID,GRADE_NAME,GRADE_TYPE,EMPLOYMENT_CATEGORY_MEANING,EMPLOYEE_CATEGORY_MEANING,CUR_TIME,ROW_ID,BATCH_ID,ROW_ID_OPER,ACT,WORK_START_DATE,COMPANY_START_DATE,MARITAL_STATUS,PHOTO,IS_SYN,LAST_UPDATE_TIME) values('" + row.attribute("OPERATER_ID").getValue()
                        + "','" + row.attribute("EMP_CODE").getValue()

                        + "','" + row.attribute("SUSPEND_OTHER_SIGN").getValue()
                        + "','" + row.attribute("PT_PERSON_TYPE").getValue()
                        + "','" +  row.attribute("NOW_PT_LIST").getValue()
                        + "','" + row.attribute("PT_PERSON_GRADE").getValue()

                        + "','" + row.attribute("ORG_ID").getValue()
                        + "','" + row.attribute("OFF_TEL").getValue()
                        + "','" + row.attribute("EMAIL").getValue()
                        + "','" + row.attribute("REG_DATE").getValue()
                        + "','" + row.attribute("MOBILE").getValue()
                        + "','" + row.attribute("CREATE_TIME").getValue()
                        + "','" + row.attribute("LASTMODIFY_TIME").getValue()
                        + "','" + row.attribute("EMP_STATUS").getValue()
                        + "','" + row.attribute("EMP_NAME").getValue()
                        + "','" + row.attribute("PASSWD").getValue()
                        + "','" + row.attribute("EMAIL_DESIGN").getValue()
                        + "','" + row.attribute("EMP_Position").getValue()
                        + "','" + row.attribute("EMP_Position_label").getValue()
                        + "','" + row.attribute("OFFICE").getValue()
                        + "','" + row.attribute("SEX").getValue()
                        + "','" + row.attribute("POSITION_ID").getValue()
                        + "','" + row.attribute("POSITION_NAME").getValue()
                        + "','" + row.attribute("JOB_ID").getValue()
                        + "','" + row.attribute("JOB_NAME").getValue()
                        + "','" + row.attribute("PERSON_TYPE").getValue()
                        + "','" + row.attribute("SUPERVISOR_NUMBER").getValue()
                        + "','" + row.attribute("SUPERVISOR_NAME").getValue()
                        + "','" + row.attribute("DATE_OF_BIRTH").getValue()
                        + "','" + row.attribute("NATIONAL_IDENTIFIER").getValue()
                        + "','" + row.attribute("NATIONALITY").getValue()
                        + "','" + row.attribute("NATIONAL").getValue()
                        + "','" + row.attribute("POLITICAL_FACE").getValue()
                        + "','" + row.attribute("GRADE_ID").getValue()
                        + "','" + row.attribute("GRADE_NAME").getValue()
                        + "','" + row.attribute("GRADE_TYPE").getValue()
                        + "','" + row.attribute("EMPLOYMENT_CATEGORY_MEANING").getValue()
                        + "','" + row.attribute("EMPLOYEE_CATEGORY_MEANING").getValue()
                        + "','" + row.attribute("CUR_TIME").getValue()
                        + "','" + row.attribute("ROW_ID").getValue()
                        + "','" + row.attribute("BATCH_ID").getValue()
                        + "','" + row.attribute("ROW_ID_OPER").getValue()
                        + "','" + row.attribute("ACT").getValue()
                        + "','" + row.attribute("WORK_START_DATE").getValue()
                        + "','" + row.attribute("COMPANY_START_DATE").getValue()
                        + "','" + row.attribute("MARITAL_STATUS").getValue()
                        + "','" + row.attribute("PHOTO").getValue()
                        + "',0,'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            rs = null;
        }
    }


}
