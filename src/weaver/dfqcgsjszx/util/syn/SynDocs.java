package weaver.dfqcgsjszx.util.syn;

import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocAttachment;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocInfo;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocServicePortType;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocServicePortTypeProxy;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.interfaces.datasource.DataSource;

import java.io.*;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 同步客户之前老OA的新闻文档
 *
 * @author Alex.Du
 */
public class SynDocs {
    public void start() {
        DataSource ds = (DataSource) StaticObj.getServiceByFullname(("datasource.ec"), DataSource.class);
        Connection conn = ds.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        RecordSet oldoaRs2 = new RecordSet();
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();

        try {
            new BaseBean().writeLog("开始同步老OA文档数据");
            // 老OA的数据源


            // 实例文档接口对象
            DocServicePortType docService = new DocServicePortTypeProxy()
                    .getDocServicePortType();

            // 调用文档接口对象的登陆方法，获取用来处理文档的session码
            String docSessionCode = null;
            // 查询老OA下面的需同步目录
            rs.execute("select loalmid,coszsmlid from uf_catalog");

            while (rs.next()) {

                // 老OA的目录id
                String loaId = rs.getString("loalmid");

                // E9的目录id
                String cosId = rs.getString("coszsmlid");

                //查询当前已同步了多少篇文档
                rs1.execute("select max(keyword+0) as maxKeyword from docdetail where seccategory ="+cosId);
                String maxKeyword = "";
                if(rs1.next()){
                    maxKeyword = rs1.getString("maxKeyword");
                }

                String appendWhere = "";
                if(maxKeyword!=null&&!maxKeyword.trim().equals("")){
                    appendWhere = " and id >"+maxKeyword;
                }


                // 查询老OA下面的文档，并循环一个个的同步到COS中
                String mlsql = "select id,title,content,publishdate from OLD_NEWS_JBXX where entryid in (" + loaId + ")"+appendWhere+" order by id";

                oldoaRs2.execute(mlsql);
                new BaseBean().writeLog("查找目录下文档的sql：" + mlsql);
                new BaseBean().writeLog("查找到的文档数量为" + oldoaRs2.getCounts());
                while (oldoaRs2.next()) {
                    // 查询老OA文档创建人的workcode，然后在E9中通过workcode查找id与登录名
                    int docCreaterId = 1;
//					new BaseBean().writeLog("当前文档老OA中的创建人id：" + docCreaterId);
                    String docCreaterLoginid = "";
//					String sql = "select workcode from hrmresource where id =  "
//							+ docCreaterId;
//					oldoaRs.execute(sql);
//					if (oldoaRs.next()) {
//						new BaseBean().writeLog("当前对应人员老OA的Workcode为："
//								+ oldoaRs.getString("workcode"));
//						String sql1 = "select id,loginid from hrmresource where workcode = '"
//								+ oldoaRs.getString("workcode") + "'";
//						new BaseBean().writeLog("查询E9人员信息的SQL为：" + sql1);
//						rs2.execute(sql1);
//						if (rs2.next()) {
//							new BaseBean().writeLog("查找到了E9中的对应人员");
//							docCreaterId = rs2.getInt("id");
//							docCreaterLoginid = rs2.getString("loginid");
//						} else {
//							// 如果查找不到E9中的对应人员，则默认使用王叶的账号创建
//							new BaseBean().writeLog("未查找E9中的对应人员");
//							docCreaterId = 6077;
//							docCreaterLoginid = "10112338";
//						}
//					}
                    // 文档ID
                    int oldDocId = oldoaRs2.getInt("id");
                    // 文档标题
                    String docsubject = oldoaRs2.getString("title");
                    // 文档内容
                    String doccontent = oldoaRs2.getString("content");

                    String docCreateDate = "2020-01-01";
                    String docCreateTime = "00:00:00";
                    String docLastModDate = "2020-01-01";
                    String docLastModTime = "00:00:00";

                    if (oldoaRs2.getString("publishdate") != null && !oldoaRs2.getString("publishdate").trim().equals("")) {
                        // 文档创建日期
                        docCreateDate = oldoaRs2.getString("publishdate").substring(0, 10);
                        // 文档创建时间
                        docCreateTime = oldoaRs2.getString("publishdate").substring(11,19);
                        // 文档最后修改日期
                        docLastModDate = oldoaRs2
                                .getString("publishdate").substring(0, 10);
                        // 文档最后修改时间
                        docLastModTime = oldoaRs2
                                .getString("publishdate").substring(11,19);
                    }
                    // 文档类型
//					int docType = oldoaRs2.getInt("DOCTYPE");

                    new BaseBean().writeLog("开始同步的新闻标题为：'" + docsubject
                            + "',ID为：" + oldDocId);
                    new BaseBean().writeLog("开始同步的新闻内容为：'" + doccontent + "'");

                    //查询新闻附件表，获取该新闻的所有附件，循环存储到对应目录
                    final String OLD_FILE_DIR = "/data/oldOaDoc/";
                    List<DocAttachment> fileList = new ArrayList<>();

                    try {
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery("select ID,NAME,CONTENT from OLD_NEWS_FJ where ORDERNO=1 and NEWSID="+oldDocId);

                        while(resultSet.next()){
                            Blob fjContent = resultSet.getBlob("CONTENT");//附件内容，字节输入流
                            new BaseBean().writeLog("获取到的附件内容：" + fjContent + ")");
                            //定义一个接收的文件对象
                            File file = new File(OLD_FILE_DIR + resultSet.getString("NAME"));
                            if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    new BaseBean().writeLog("创建新闻附件id为：" + resultSet.getString("ID") + "的文件时出现异常(exception:+" + e.getMessage() + ")");
                                    return;
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
                                docAttachment.setFilename(file.getName());
                                fileList.add(docAttachment);
                                docAttachment = null;
                            }catch(Exception e){
                                e.printStackTrace();
                                new BaseBean().writeLog("同步老OA新闻附件出现异常1："+e.getMessage());
                                return;
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
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        new BaseBean().writeLog("同步老OA的新闻附件出现异常2："+e.getMessage());
                        return;
                    }finally{
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
                    }

                    // 查询当前文档的附件(循环附件文件夹，模糊匹配老新闻ID开头的文件夹，然后逐个获取每个附件)
                    new BaseBean().writeLog("系统编码为：" + System.getProperty("file.encoding"));

                    //将查询到的附件集合转换为数组
                    DocAttachment[] da = null;
                    if (fileList.size() > 0) {
                        da = new DocAttachment[fileList.size()];
                        fileList.toArray(da);
                    }

                    // 调用文档接口的创建方法，根据老OA的文档创建对应的文档，并将文档ID存储在文档的关键字字段中
                    // 创建文档对象
                    try {
                        docCreaterLoginid = "sysadmin";//指定固定的创建者
                        docSessionCode = docService.login(docCreaterLoginid,
                                "cos@123", 0, "127.0.0.1");// 参数：用户名，密码，登陆方式(0数据库验证;1动态密码验证;2LDAP验证)，ip
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                        new BaseBean().writeLog("文档创建登陆状态出现异常");
                        return;
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
                    doc.setSeccategory(Integer.valueOf(cosId));
                    doc.setDocStatus(1);
//					new BaseBean().writeLog("文档docType：" + docType);
//					doc.setDocType(docType);
                    new BaseBean().writeLog("文档创建日期：" + docCreateDate);
                    doc.setDoccreatedate(docCreateDate);
                    new BaseBean().writeLog("文档创建时间：" + docCreateTime);
                    doc.setDoccreatetime(docCreateTime);
                    new BaseBean().writeLog("文档修改日期：" + docLastModDate);
                    doc.setDoclastmoddate(docLastModDate);
                    new BaseBean().writeLog("文档修改时间：" + docLastModTime);
                    doc.setDoclastmodtime(docLastModTime);
                    // 将新闻ID存储在文档的关键字字段
                    doc.setKeyword(String.valueOf(oldDocId));
                    // 设置文档的标题和内容(内容对部分html转义符进行转义)
                    doc.setDocSubject(docsubject);
//					new BaseBean()
//							.writeLog("当前同步的文档内容替换后为："
//									+ doccontent
//											.replaceAll(
//													"/weaver/weaver.file.FileDownload\\?fileid=",
//													"http://10.16.3.118/SendDoc?imagefileid="));
//					doc.setDoccontent(doccontent.replaceAll(
//							"/weaver/weaver.file.FileDownload\\?fileid=",
//							"http://10.16.3.118/SendDoc?imagefileid="));
                    doc.setDoccontent(doccontent.replaceAll("&quot;", "\"")
                            .replaceAll("&amp;", "&")
                            .replaceAll("&lt;", "<")
                            .replaceAll("&gt;", ">")
                            .replaceAll("&nbsp;", " ")
                            .replaceAll("/NewsManage/DownloadBinaryPic.aspx\\?id=","/old_oa_doc_img/"));


                    if (null != da && da.length > 0) {
                        doc.setAttachments(da);
                    }

                    int docId = 0;
                    try {
                        docId = docService.createDoc(doc, docSessionCode);
                        new BaseBean().writeLog("创建的docId为：" + docId);

                        rs1.execute("update docdetail set keyword='" + oldoaRs2.getString("id") + "' where id='" + docId + "'");
                        new BaseBean().writeLog("修改文档keyword的sql语句：update docdetail set keyword='" + oldoaRs2.getString("id") + "' where id='" + docId + "'");

                        // 更新当前文档的docType、文档创建日期、文档创建时间、文档修改日期、文档修改时间
                        rs1.execute("update docdetail set DOCCREATEDATE='"
                                + docCreateDate + "',DOCCREATETIME='"
                                + docCreateTime + "',DOCLASTMODDATE='"
                                + docLastModDate + "',DOCLASTMODTIME='"
                                + docLastModTime + "' where id=" + docId);

                        // 如果当前文档是以office文档作为正文的，那么将office文档的isextfile更新为null(因为特意把office文档作为最后一个附件存入的，所以它的ID是最大的)
//						if (docType == 2) {
//							rs1.execute("update docimagefile set isextfile=null where id=(select max(id) from docimagefile where docid='"
//									+ docId + "')");
//						}

                        // 构建迁移的文档基于所有人类型的共享权限。
                        //fq(oldDocId, docId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new BaseBean().writeLog("根据素材创建文档时出现问题："
                                + e.getMessage());
                        return;
                    }

					deleteTempDocFile(OLD_FILE_DIR);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            new BaseBean().writeLog("新闻同步出现异常：" + e.getMessage());
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

            oldoaRs2 = null;
            rs = null;
            rs1 = null;
        }
        new BaseBean().writeLog("同步老OA文档数据结束");
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


    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    private int getFileSize(File file) {
        String filesize = "0";
        DecimalFormat df = new DecimalFormat("#.##");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            filesize = df.format((double) ((double) fis.available() / 1024));
        } catch (Exception e) {

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {

                }
            }
        }
        if (filesize != null && filesize.contains(".")) {
            filesize = filesize.split("\\.")[0];
        }

        return Integer.parseInt(filesize);
    }


    public static void main(String[] args) {
        String date = "2020-02-02 11:11:11.0";
        System.out.println(date.substring(0, 10));
        System.out.println(date.substring(11, 19));
    }
}
