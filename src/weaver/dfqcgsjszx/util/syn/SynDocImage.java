package weaver.dfqcgsjszx.util.syn;

import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocAttachment;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.interfaces.datasource.DataSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 同步老OA文档中用到的image图片
 * @author Alex.Du
 */
public class SynDocImage {
    private final String OLD_IMAGE_FILE="/home/weaver/ecology/old_oa_doc_img/";

    public void start(){
        DataSource ds = (DataSource) StaticObj.getServiceByFullname(("datasource.ec"), DataSource.class);
        Connection conn = ds.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select ID,CONTENT from OLD_NEWS_FJ where ORDERNO=0 ");

            while(resultSet.next()) {
                Blob fjContent = resultSet.getBlob("CONTENT");//附件内容，字节输入流
                new BaseBean().writeLog("获取到的图片内容：" + fjContent + ")");

                //定义一个接收的文件对象
                File file = new File(OLD_IMAGE_FILE + resultSet.getString("ID"));
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        new BaseBean().writeLog("创建新闻图片id为：" + resultSet.getString("ID") + "的文件时出现异常(exception:+" + e.getMessage() + ")");
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
                }catch(Exception e){
                    e.printStackTrace();
                    new BaseBean().writeLog("同步老OA新闻图片出现异常1："+e.getMessage());
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
            }
        } catch (Throwable e) {
            e.printStackTrace();
            new BaseBean().writeLog("新闻图片同步出现异常：" + e.getMessage());
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
        }
    }
}
