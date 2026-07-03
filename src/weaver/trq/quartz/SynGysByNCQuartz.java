package weaver.trq.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.trq.webservice.TransferPortTypeProxy;

/**
 * ��NCͬ����Ӧ�̵Ķ�ʱ����
 *
 * @author Alex.Du
 */
public class SynGysByNCQuartz extends BaseCronJob {
    public void execute() {
        BaseBean log = new BaseBean();
        log.writeLog("��ʼִ��NCͬ����Ӧ�̵Ķ�ʱ����");

        TransferPortTypeProxy proxy = new TransferPortTypeProxy();
        try {
            log.writeLog("��ʼ������ת��������ȡNC�Ĺ�Ӧ������");

            String result = proxy.send(2, "");

            //log.writeLog("NC�ķ��ؽ��Ϊ��" + result);

            Document document = DocumentHelper.parseText(result);

            Element root = document.getRootElement();

            List<Element> supplierElements = root.elements();

            RecordSet rs = new RecordSet();

            String nowDate = new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date());
            String nowTime = new SimpleDateFormat("HH:mm:ss")
                    .format(new Date());

            for (int i = 0; i < supplierElements.size(); i++) {
                Element supplierElement = supplierElements.get(i);
                // NC����
                String supplierPK = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("pk_supplier"))));
                // ��Ӧ������
                String name = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("Name"))));
                // �ص�
                String corpAddress = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("corpaddress"))));
                // �����˺�PKֵ
                String pkBankaccbas = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("pk_bankaccbas"))));
                // �˺�
                String account = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("Account"))));
                // ����
                String bankName = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("BankName"))));
                // �˺�����
                String accountProperty = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("accountproperty"))));
                // ���к�
                String combineNum = new String(Base64.decode(Util.null2String(supplierElement
                        .attributeValue("combinenum"))));

                // ���ݹ�Ӧ�̵�NF�������˺�PKֵ��ѯ�����Ƿ����
                rs.execute("select id from uf_gysk where ncpk='" + supplierPK + "' and zh='" + account + "'");

                if (rs.next()) {
                    // ���������
                    String id = rs.getString("id");

                    rs.execute("update uf_gysk set gysmc='" + name
                            + "',ncpk='" + supplierPK + "',dd='" + corpAddress
                            + "',yhpk='"+pkBankaccbas+"',zh='" + account + "',yh='" + bankName
                            + "',zhlx='" + accountProperty + "',lhh='" + combineNum + "' where id=" + id);
                } else {
                    // �����������
                    rs.execute("insert into uf_gysk(gysmc,gysbm,ncpk,dd,yhpk,zh,yh,zhlx,lhh,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('"
                            + name
                            + "','','"
                            + supplierPK
                            + "','"
                            + corpAddress
                            + "','"
                            + pkBankaccbas
                            + "','"
                            + account
                            + "','"
                            + bankName
                            + "','" + accountProperty + "','" + combineNum + "',82,1,0,'"
                            + nowDate + "','" + nowTime + "')");

                    rs.execute("select max(id) from uf_gysk");
                    int id = 0;
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }

                    // ���ڹ�����ģ���ݵĹ���Ȩ�޵Ĺ�����
                    ModeRightInfo mri = new ModeRightInfo();
                    mri.setNewRight(true);
                    mri.editModeDataShare(1, 82, id);
                    mri = null;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.writeLog("ִ��NCͬ����Ӧ�̵Ķ�ʱ����ʱ�����쳣��" + e.getMessage());
        }
        log.writeLog("ִ��NCͬ����Ӧ�̵Ķ�ʱ�������");

    }

    public static void main(String[] args) {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<xmlInfo>"
                + "<supplier pk_supplier=\"MTAwMUEyMTAwMDAwMDAwMDBaWVE=\" Name=\"zuS6usrQtcLtrr/GvLy3otW509DP3rmry74=\"  corpaddress=\"zuS6ug==\" Account=\"NTY5MDY5MjE4MTQ5\" BankName=\"1tDQ0Mz6x8XWp9DQ\" pk_bankaccbas=\"MTAwMUEyMTAwMDAwMDAwNFAwNE0=\" PK_Org=\"MDAwMUEyMTAwMDAwMDAwMDJLT1E=\" orgbh=\"\" />"
                + "<supplier pk_supplier=\"MTAwMUEyMTAwMDAwMDAwMDBaWVE=\" Name=\"zuS6usrQtcLtrr/GvLy3otW509DP3rmry74=\"  corpaddress=\"zuS6ug==\" Account=\"MjAwNzU0NzEwODEwMDEx\" BankName=\"zuS6usWpyczQ0NXUvNLM9dan0NA=\" pk_bankaccbas=\"MTAwMUEyMTAwMDAwMDAwMDJGWUw=\" PK_Org=\"MDAwMUEyMTAwMDAwMDAwMDJLUjQ=\" orgbh=\"d2huZzAwMQ==\" />"
                + "<supplier pk_supplier=\"MTAwMUEyMTAwMDAwMDAwMDEwMDY=\" Name=\"uv6xscGssO60tNDCyO28/tPQz965q8u+\"  corpaddress=\"zuS6ug==\" Account=\"MTI3OTA1MzUwMDEwODAy\" BankName=\"1dDJzNL40NC54rnIv8a8vNan0NA=\" pk_bankaccbas=\"MTAwMUEyMTAwMDAwMDAwMjE2QVI=\" PK_Org=\"MDAwMUEyMTAwMDAwMDAwMDNSTlQ=\" orgbh=\"\" />"
                + "</xmlInfo>";

        try {
            Document document = DocumentHelper.parseText(xmlStr);

            Element root = document.getRootElement();

            List<Element> supplierElements = root.elements();

            for (int i = 0; i < supplierElements.size(); i++) {
                Element supplierElement = supplierElements.get(i);
                System.out.println("pk_supplier="
                        + new String(new Base64()
                        .decode(supplierElement
                                .attributeValue("pk_supplier"))));
                System.out
                        .println("Name="
                                + new String(
                                new Base64().decode(supplierElement
                                        .attributeValue("Name"))));
                System.out.println("orgbh="
                        + new String(
                        new Base64()
                                .decode(supplierElement
                                        .attributeValue("orgbh"))));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
