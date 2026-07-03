package weaver.dfqcgsjszx.util.syn;

import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocAttachment;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocInfo;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocServicePortType;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocServicePortTypeProxy;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.interfaces.datasource.DataSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 同步发文文档
 *
 * @author Alex.Du
 */
public class SynFwDoc {
    private final String OLD_FILE_DIR = "/data/oldOaDoc/";

    public void start() {
        new BaseBean().writeLog("开始同步老OA发文附件数据");

        //查询要同步的发文主文件，循环将每个主文件处理成知识文档
        DataSource ds = (DataSource) StaticObj.getServiceByFullname(("datasource.ec"), DataSource.class);
        Connection conn = ds.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        Statement statement2 = null;
        ResultSet resultSet2 = null;

        RecordSet rs= new RecordSet();


        //查询当前已同步了多少篇文档
        rs.execute("select max(keyword+0) as maxKeyword from docdetail where seccategory in (51,52)");
        String maxKeyword = "";
        if(rs.next()){
            maxKeyword = rs.getString("maxKeyword");
        }

        String appendWhere = "";
        if(maxKeyword!=null&&!maxKeyword.trim().equals("")){
            appendWhere = " and docuid >"+maxKeyword;
        }


        List<DocAttachment> fileList = null;

        // 实例文档接口对象
        DocServicePortType docService = new DocServicePortTypeProxy()
                .getDocServicePortType();

        // 调用文档接口对象的登陆方法，获取用来处理文档的session码
        String docSessionCode = null;

        String fileNameUUID = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select DOCUID,DOCULEVELID,DOCUTITLE,FILETYPE,DOCUCONTENT from OLD_GS_FWJBXX where FILETYPE is not null and DOCULEVELID is not null and DOCUTITLE is not null"+appendWhere+" order by docuid");


            while (resultSet.next()) {
                fileList = new ArrayList<>();
                //通过DOCULEVELID计算文档所处目录ID
                new BaseBean().writeLog("当前同步的DOCUID为："+resultSet.getString("DOCUID"));
                int docCategory = resultSet.getString("DOCULEVELID").trim().equals("0") ? 51 : 52;

                Blob fjContent = resultSet.getBlob("DOCUCONTENT");//附件内容，字节输入流

                fileNameUUID = UUID.randomUUID().toString();
                new BaseBean().writeLog("fileNameUUID:"+fileNameUUID);

                //定义一个接收的文件对象
                File file = new File(OLD_FILE_DIR + fileNameUUID);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        new BaseBean().writeLog("创建老OA发文主附件id为：" + resultSet.getString("DOCUID") + "的文件时出现异常(exception:+" + e.getMessage() + ")");
                        continue;
                    }
                }

                InputStream is = null;//读取Blob字段的输入流
                OutputStream os = null;//输出到文件的输出流

                int len = 0;
                byte[] b = new byte[4096];

                try {
                    is = fjContent.getBinaryStream();
                    os = new FileOutputStream(file);

                    while (-1 != (len = is.read(b, 0, b.length))) {
                        os.write(b, 0, len);
                    }

                    DocAttachment docAttachment = new DocAttachment();
                    docAttachment.setDocid(0);
                    docAttachment.setImagefileid(0);
                    docAttachment.setFilerealpath(OLD_FILE_DIR + file.getName());
                    docAttachment.setIszip(0);
                    docAttachment.setFilename(resultSet.getString("DOCUTITLE").trim()+ resultSet.getString("FILETYPE").trim());
                    fileList.add(docAttachment);
                    docAttachment = null;
                }catch(Exception e){
                    e.printStackTrace();
                    new BaseBean().writeLog("同步老OA发文主附件出现异常1："+e.getMessage());
                    continue;
                }finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            new BaseBean().writeLog("关闭输出流时出现异常");
                        } finally {
                            os = null;
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            new BaseBean().writeLog("关闭输入流时出现异常");
                        } finally {
                            is = null;
                        }
                    }
                    b = null;
                    file = null;
                }

                //查询老OA发文次附件表，获取次附件文件
                statement2 = conn.createStatement();
                resultSet2 = statement2.executeQuery("select DOCUID,FILENAME,ACONTENT from OLD_GS_FWFJ where DOCUID="+resultSet.getString("DOCUID"));

                try {
                    while (resultSet2.next()) {
                        Blob fjContent2 = resultSet2.getBlob("ACONTENT");//附件内容，字节输入流

                        fileNameUUID = UUID.randomUUID().toString();
                        new BaseBean().writeLog("fileNameUUID2:" + fileNameUUID);

                        //定义一个接收的文件对象
                        file = new File(OLD_FILE_DIR + fileNameUUID);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                                new BaseBean().writeLog("创建老OA发文次附件id为：" + resultSet2.getString("DOCUID") + "的文件时出现异常(exception:+" + e.getMessage() + ")");
                                continue;
                            }
                        }

                        is = null;//读取Blob字段的输入流
                        os = null;//输出到文件的输出流

                        len = 0;
                        b = new byte[4096];

                        try {
                            is = fjContent2.getBinaryStream();
                            os = new FileOutputStream(file);

                            while (-1 != (len = is.read(b, 0, b.length))) {
                                os.write(b, 0, len);
                            }

                            DocAttachment docAttachment = new DocAttachment();
                            docAttachment.setDocid(0);
                            docAttachment.setImagefileid(0);
                            docAttachment.setFilerealpath(OLD_FILE_DIR + file.getName());
                            docAttachment.setIszip(0);
                            docAttachment.setFilename(resultSet2.getString("FILENAME").trim());
                            fileList.add(docAttachment);
                            docAttachment = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            new BaseBean().writeLog("同步老OA发文主附件出现异常2：" + e.getMessage());
                            continue;
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    new BaseBean().writeLog("关闭输出流时出现异常");
                                } finally {
                                    os = null;
                                }
                            }
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    new BaseBean().writeLog("关闭输入流时出现异常");
                                } finally {
                                    is = null;
                                }
                            }

                            b = null;
                            file = null;
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    new BaseBean().writeLog("同步老OA发文主附件出现异常3：" + e.getMessage());
                }finally{
                    if(resultSet2!=null){
                        try {
                            resultSet2.close();
                        }catch(Exception e1){
                            e1.printStackTrace();
                            new BaseBean().writeLog("关闭rs时出现异常");
                        }finally {
                            resultSet2 = null;
                        }
                    }
                    if(statement2!=null){
                        try {
                            statement2.close();
                        }catch(Exception e1){
                            e1.printStackTrace();
                            new BaseBean().writeLog("关闭statement时出现异常");
                        }finally {
                            statement2 = null;
                        }
                    }
                }



                //将查询到的附件集合转换为数组
                DocAttachment[] da = null;
                if (fileList.size() > 0) {
                    da = new DocAttachment[fileList.size()];
                    fileList.toArray(da);
                }

                // 调用文档接口的创建方法，根据老OA的文档创建对应的文档，并将文档ID存储在文档的关键字字段中
                // 创建文档对象
                int docCreaterId = 1;
                String docCreaterLoginid = "";

                try {
                    docCreaterLoginid = "sysadmin";//指定固定的创建者
                    docSessionCode = docService.login(docCreaterLoginid,
                            "cos@123", 0, "127.0.0.1");// 参数：用户名，密码，登陆方式(0数据库验证;1动态密码验证;2LDAP验证)，ip
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                DocInfo doc = new DocInfo();
                doc.setId(0);
                doc.setDoccreaterid(docCreaterId);
                doc.setDoccreatertype(1);
                doc.setAccessorycount(0);
                doc.setMaincategory(0);
                // 主目录id
                doc.setSubcategory(0);
                // 分目录id
                doc.setSeccategory(Integer.valueOf(docCategory));
                doc.setDocStatus(1);
                doc.setDocType(2);
                doc.setDoccreatedate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                doc.setDoccreatetime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                doc.setDoclastmoddate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                doc.setDoclastmodtime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                // 将新闻ID存储在文档的关键字字段
                doc.setKeyword(String.valueOf(resultSet.getString("DOCUID")));
                // 设置文档的标题和内容(内容对部分html转义符进行转义)
                doc.setDocSubject(resultSet.getString("DOCUTITLE"));
                doc.setDoccontent("");


                if (null != da && da.length > 0) {
                    doc.setAttachments(da);
                }

                int docId = 0;
                try {
                    docId = docService.createDoc(doc, docSessionCode);
                    new BaseBean().writeLog("创建的docId为：" + docId);

                    rs.execute("update docdetail set keyword='" + resultSet.getString("DOCUID") + "' where id='" + docId + "'");
                    new BaseBean().writeLog("修改文档keyword的sql语句：update docdetail set keyword='" + resultSet.getString("DOCUID") + "' where id='" + docId + "'");

                    // 更新当前文档的docType、文档创建日期、文档创建时间、文档修改日期、文档修改时间
                    rs.execute("update docdetail set doctype='2' where id=" + docId);

                    // 如果当前文档是以office文档作为正文的，那么将office文档的isextfile更新为null(因为特意把office文档作为最后一个附件存入的，所以它的ID是最大的)
                    rs.execute("update docimagefile set isextfile=null where id=(select min(id) from docimagefile where docid='"
                            + docId + "')");

                    // 构建迁移的文档基于所有人类型的共享权限。
                    //fq(oldDocId, docId);
                } catch (Exception e) {
                    e.printStackTrace();
                    new BaseBean().writeLog("根据素材创建文档时出现问题："
                            + e.getMessage());
                }

                rs.execute("update uf_fwk set zwnr='"+docId+"' where bz='"+resultSet.getString("DOCUID")+"'");

            }
            deleteTempDocFile(OLD_FILE_DIR);
        } catch (Throwable e) {
            e.printStackTrace();
            new BaseBean().writeLog("老OA发文附件同步出现异常：" + e.getMessage());
        }finally {
            if(resultSet!=null){
                try {
                    resultSet.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                    new BaseBean().writeLog("关闭rs时出现异常");
                }finally {
                    resultSet = null;
                }
            }
            if(statement!=null){
                try {
                    statement.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                    new BaseBean().writeLog("关闭statement时出现异常");
                }finally {
                    statement = null;
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                    new BaseBean().writeLog("关闭conn时出现异常");
                }finally {
                    conn = null;
                }
            }
            if(ds!=null){
                conn = null;
            }
            rs = null;
            fileList = null;
            docService = null;
        }
        new BaseBean().writeLog("同步老OA发文附件数据结束");
    }


    public void deleteTempDocFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            temp = new File(path + tempList[i]);
            temp.delete();
            temp = null;
        }

        file = null;

    }
}
