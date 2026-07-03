package weaver.dfqcgsjszx.util.ecology.service_client;

import org.apache.activemq.ActiveMQMessageTransformation;
import org.apache.axis.encoding.Base64;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocAttachment;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocInfo;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocServiceLocator;
import weaver.dfqcgsjszx.util.ecology.service_client.doc.DocServicePortType;


import javax.xml.rpc.ServiceException;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DocTest {
    public static void main(String[] args) throws RemoteException, MalformedURLException, ServiceException {
        //创建service
        DocServicePortType service = new DocServiceLocator().getDocServiceHttpPort(new URL("http://121.40.45.20:9998/Gundam/services/DocService"));

        //登陆
        String session = service.login("zdglxt", "zdgl@2021", 0, "127.0.0.1");

        //创建附件列表
        ArrayList<DocAttachment>  fileList = new ArrayList<>();


        //填充附件，可循环填充，注意如果正文为PDF或OFFICE文档，则将其填充到第一个附件中
        File fileTemp = new File("/Users/hanjun/Downloads/配置说明.pdf");
        DocAttachment docAttachment = getDocAttachment(fileTemp);
        fileList.add(docAttachment);


        //创建文档对象
        DocInfo doc = getDocInfo("测试文件1213001",12001 ,1 , fileList);

        //发送文档,返回值为OA系统中的文档id
        int doc1 = service.createDoc(doc, session);


        System.out.println("返回值==="+doc1);
    }



    private static DocInfo getDocInfo(String docName, int docCreator, int secCategory, ArrayList<DocAttachment> fileList) {
        DocInfo doc = new DocInfo();
        doc.setId(0);
        doc.setDoccreatertype(1);
        doc.setAccessorycount(0);
        //主文档id
        doc.setMaincategory(0);
        // 主目录id
        doc.setSubcategory(0);
        // 分目录id 联系OA管理员获取
        doc.setSeccategory(secCategory);
        doc.setDocStatus(1);
        doc.setDocType(2);

        //文档创建时间相关
        doc.setDoccreatedate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        doc.setDoccreatetime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        doc.setDoclastmoddate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        doc.setDoclastmodtime(new SimpleDateFormat("HH:mm:ss").format(new Date()));

        //文档名称
        doc.setDocSubject(docName);

        //文档HTML正文 为空时默认打开第一个附件
        doc.setDoccontent("");

        //文档创建人
        doc.setDoccreaterid(docCreator);

        //文档附件
        DocAttachment[] da = null;
        if (fileList.size() > 0) {
            da = new DocAttachment[fileList.size()];
            fileList.toArray(da);
        }

        if (null != da && da.length > 0) {
            doc.setAttachments(da);
        }

        return doc;
    }


    private static DocAttachment getDocAttachment(File fileTemp) {
        DocAttachment docAttachment = new DocAttachment();
        byte[] content = null;
        try {
            int byteread;
            byte data[] = new byte[1024];
            InputStream input = new FileInputStream(fileTemp);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((byteread = input.read(data)) != -1) {
                out.write(data, 0, byteread);
                out.flush();
            }
            content = out.toByteArray();
            input.close();
            out.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        docAttachment.setDocid(0);
        docAttachment.setImagefileid(0);
        docAttachment.setFilerealpath(fileTemp.getPath());
        docAttachment.setIszip(0);
        docAttachment.setFilecontent(Base64.encode(content));
        docAttachment.setFilename(fileTemp.getName());
        return docAttachment;
    }

}

