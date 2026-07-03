package weaver.dfqcgsjszx.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.FileCryptoUtil;
import weaver.dfqcgsjszx.util.http_request.HttpsSendUtil;
import weaver.dfqcgsjszx.util.http_request.MyX509TrustManager;
import weaver.docs.docs.DocImageManager;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 调用电子采购平台接口，获取项目结果数据
 * @author Alex.Du
 */
public class SearchProjectInfoResultFromDzcgptQuartz extends BaseCronJob {
    @Override
    public void execute() {
        //用于SSL验证的内部类
        HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslsession) {
                System.out.println("WARNING: Hostname is not matched for cert.");
                return true;
            }
        };

        //设置接口请求的参数
        JSONObject sendJSON = new JSONObject();
        //设置请求头部数据
        sendJSON.put("dataCode","projectResult");
        sendJSON.put("platformCode","00270013");
        sendJSON.put("abbreviationCode","dfzx");
        sendJSON.put("token","473b1c1b-1cde-44c1-9a91-8aa95734dba7");
        sendJSON.put("version","V1.0");

        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        //查询所有招标平台结果为等待结果的项目信息，调用电子采购平台接口进行查询
        rs.execute("select * from uf_cgxqjbxxb where zcptjg='等待结果'");
        while(rs.next()){
            try {
                RecordSet rsdet = new RecordSet();
                rsdet.execute("update uf_cgzxjl set sfyx='history' where cgxqbh = '" + rs.getString("id") + "'");
                //获取projectId，作为数据部分（消息体data）参数进行传递
                JSONObject dataJSON = new JSONObject();
                dataJSON.put("projectId", rs.getString("id"));
                sendJSON.put("data", dataJSON);
                new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]发送到电子采购平台的数据内容为：" + sendJSON.toJSONString());

                String result = null;
                try {
                    Map<String, String> requestPropertes = new HashMap<>();
                    requestPropertes.put("Content-type", "application/json");
                    result = HttpsSendUtil.httpRequest("https://www.dfmjyzx.com/DTCService/query_data", "POST", sendJSON.toJSONString(), requestPropertes);
                    new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]电子采购平台的返回结果为：" + result);
                } catch (Throwable e) {
                    e.printStackTrace();
                    new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]调用电子采购平台出现异常：" + e.getMessage());
                    return;
                }

                JSONObject resultJSON = JSONObject.parseObject(result);

                //判断有没有得到项目数据，没有则跳过该循环
                if(!resultJSON.containsKey("data")){
                    new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]接口没有返回该项目的数据信息，跳过该项目处理下一个项目");
                    continue;
                }

                //获取返回的projectId、projectName
                String projectId = resultJSON.getJSONObject("data").getString("projectId");
                String projectName = resultJSON.getJSONObject("data").getString("projectName");

                //用于记录插入的主表ID
                String mainId = "";

                //循环每一个供应商，找到中标的供应商，将其相关数据存储到uf_cgzxjl主表
                for (int i = 0; i < resultJSON.getJSONObject("data").getJSONArray("selSupplier").size(); i++) {
                    //获取供应商
                    JSONObject supplierJSON = resultJSON.getJSONObject("data").getJSONArray("selSupplier").getJSONObject(i);
                    //判断该供应商是否中标
                    if (!supplierJSON.getString("isWin").equals("1")) {
                        //未中标则跳过处理该供应商
                        continue;
                    }

                    //处理根节点的所有附件，将附件添加到文件系统中（文档目录ID7559），后面用于储存在建模主表字段中
                    StringBuffer mainDocIds = new StringBuffer();
                    JSONArray attachmentsMainJSONArray = resultJSON.getJSONArray("attachments");
                    try {
                        //获取根节点的附件，将附件上传到系统中
                        for (int j = 0; j < attachmentsMainJSONArray.size(); j++) {
                            //循环处理附件
                            //先将附件下载到本地，进行解密，然后将文件上传到文档系统中，获取对应附件的docid
                            //String fileName = attachmentsJSONArray.getJSONObject(j).getString("name");//文件名
                            String tempFileName = UUID.randomUUID().toString();

                            //将文件下载到本地
                            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");//第一个参数为协议,第二个参数为提供者(可以缺省)
                            TrustManager[] tm = {new MyX509TrustManager()};
                            sslcontext.init(null, tm, new SecureRandom());
                            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
                            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
                            URL url = new URL(attachmentsMainJSONArray.getJSONObject(j).getString("path"));
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            //获取在响应头部中的下载文件名
                            List<String> headerFields = connection.getHeaderFields().get("Content-Disposition");
                            String fileName = headerFields.get(0).substring(headerFields.get(0).indexOf("filename=") + 10, headerFields.get(0).length() - 1);
                            //截取响应头部中的文件名后缀，和附件数据中的name节点内容拼接成文件名称
                            String suffixName = "";
                            if(fileName.indexOf(".")!=-1){
                                suffixName = fileName.substring(fileName.lastIndexOf("."));
                            }
                            fileName = attachmentsMainJSONArray.getJSONObject(j).getString("name")+suffixName;

                            InputStream is = connection.getInputStream();
                            //原始下载文件对象
                            File ysFile = new File(File.separator + "home" + File.separator + "weaver" + File.separator + "ecology" + File.separator + "tempfile" + File.separator + tempFileName);
                            OutputStream os = new FileOutputStream(ysFile);

                            byte[] b = new byte[2048];
                            int len = 0;
                            while (-1 != (len = is.read(b, 0, b.length))) {
                                os.write(b, 0, len);
                            }
                            os.flush();
                            if (os != null) {
                                os.close();
                                os = null;
                            }
                            if (is != null) {
                                is.close();
                                is = null;
                            }

                            //将文件进行解密
                            //解密文件对象
                            File jmFile = new File(File.separator + "home" + File.separator + "weaver" + File.separator + "ecology" + File.separator + "tempfile" + File.separator + tempFileName + "_jm");
                            FileCryptoUtil.decryptFile(FileCryptoUtil.analysisKey(attachmentsMainJSONArray.getJSONObject(j).getString("base64key")), ysFile.getPath(), jmFile.getPath());

                            //将解密后的文件上传到OA文档系统中，并获取上传文件的docid
                            // 生成文件ID
                            int imagefileid = new ImageFileIdUpdate().getImageFileNewId();
                            // 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
                            int createstatus = ImageFileUtil.createImageFile(jmFile.getPath(), imagefileid, fileName, getFileSize(jmFile));

                            if (createstatus == -1) {
                                new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]供应商附件上传到OA文档系统失败：" + createstatus);
                                return;
                            }

                            // 创建docdetail
                            int docId = createDocDetail(7559, fileName, 1);
                            // 创建docimagefile
                            createDocImageFile(docId, imagefileid, fileName);
                            // 创建文档共享
                            createDocShare(7559, 1, docId);

                            if (!mainDocIds.toString().trim().equals("")) {
                                mainDocIds.append(",");
                            }
                            mainDocIds.append(docId);

                            ysFile.delete();
                            jmFile.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]读取供应商主附件出现异常：" + e.getMessage());
                        return;
                    }

                    //获取供应商的名称、社会信用代码
                    String supplierName = supplierJSON.getString("supplierName");
                    String socialCreditcode = supplierJSON.getString("socialCreditcode");
                    String winPrice = supplierJSON.getString("winPrice");
                    if (winPrice == null || winPrice.trim().equals("") || winPrice.trim().equals("null")) {
                        winPrice = "0";
                    }

                    //通过社会信用代码到uf_cggl_gysjbxxb表去查询供应商id
                    rs2.execute("select id from uf_cggl_gysjbxxb where tyshxydm = '" + socialCreditcode + "'");
                    String zxgys = "";

                    if (rs2.next()) {
                        zxgys = rs2.getString("id");
                    }

                    //计算记录编号
                    Calendar calendar = Calendar.getInstance();
                    String jlbh = null;
                    rs2.execute("select max(jlbh) maxjlbh from uf_cgzxjl where jlbh like '%XS" + calendar.get(Calendar.YEAR) + "%'");
                    if (rs2.next()) {
                        String maxjlbh = rs2.getString("maxjlbh");
                        if (null == maxjlbh || maxjlbh.trim().equals("")) {
                            //未查询到今年最大的记录，则使用该年份的0001号编号
                            jlbh = "XS" + calendar.get(Calendar.YEAR) + "0001";
                        } else {
                            //查询到今年的最大记录，则将最大记录编号加1
                            String xh = String.format("%04d", Integer.parseInt(maxjlbh.substring(6)) + 1);
                            jlbh = "XS" + calendar.get(Calendar.YEAR) + xh;
                        }

                    } else {
                        //未查询到今年最大的记录，则使用该年份的0001号编号
                        jlbh = "XS" + calendar.get(Calendar.YEAR) + "0001";
                    }

                    //将中标供应商信息插入到uf_cgzxjl表
                    rs2.execute("insert into uf_cgzxjl(jlbh,zxgys,zxgysmc,tyshxydm,zxje,cgxqbh,cgxqmc,xgcl) values('" + jlbh + "','" + zxgys + "','" + supplierName + "','" + socialCreditcode + "','" + winPrice + "','" + projectId + "','" + projectName + "','" + mainDocIds.toString() + "')");
                    rs2.execute("select max(id) maxid from uf_cgzxjl");
                    if (rs2.next()) {
                        mainId = rs2.getString("maxid");
                    }
                    addFormmodeRight("1", 10006, "uf_cgzxjl");
                    break;
                }

                if(mainId.trim().equals("")){
                    new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]mainId为空，未找到中标供应商数据");
                    return;
                }

                //循环每一个供应商的数据，将附件添加到文件系统中（文档目录ID7559），并将数据添加到uf_cgzxjl明细表
                for (int i = 0; i < resultJSON.getJSONObject("data").getJSONArray("selSupplier").size(); i++) {
                    //获取供应商
                    JSONObject supplierJSON = resultJSON.getJSONObject("data").getJSONArray("selSupplier").getJSONObject(i);


                    //获取供应商的名称、社会信用代码
                    String supplierName = supplierJSON.getString("supplierName");
                    String socialCreditcode = supplierJSON.getString("socialCreditcode");
                    String winPrice = supplierJSON.getString("winPrice");
                    if (winPrice == null || winPrice.trim().equals("") || winPrice.trim().equals("null")) {
                        winPrice = "0";
                    }
                    String openPrice = supplierJSON.getString("openPrice");
                    if (openPrice == null || openPrice.trim().equals("") || openPrice.trim().equals("null")) {
                        openPrice = "0";
                    }
                    String isWin = supplierJSON.getString("isWin");

                    //用于记录所有附件的docid的变量
                    StringBuffer docIds = new StringBuffer();

                    try {
                        //获取中标供应商的附件，将附件上传到系统中
                        JSONArray attachmentsJSONArray = supplierJSON.getJSONArray("attachments");
                        for (int j = 0; j < attachmentsJSONArray.size(); j++) {
                            //循环处理附件
                            //先将附件下载到本地，进行解密，然后将文件上传到文档系统中，获取对应附件的docid
                            //String fileName = attachmentsJSONArray.getJSONObject(j).getString("name");//文件名
                            String tempFileName = UUID.randomUUID().toString();

                            //将文件下载到本地
                            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");//第一个参数为协议,第二个参数为提供者(可以缺省)
                            TrustManager[] tm = {new MyX509TrustManager()};
                            sslcontext.init(null, tm, new SecureRandom());
                            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
                            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
                            URL url = new URL(attachmentsJSONArray.getJSONObject(j).getString("path"));
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            //获取在响应头部中的下载文件名
                            List<String> headerFields = connection.getHeaderFields().get("Content-Disposition");
                            String fileName = headerFields.get(0).substring(headerFields.get(0).indexOf("filename=") + 10, headerFields.get(0).length() - 1);
                            //截取响应头部中的文件名后缀，和附件数据中的name节点内容拼接成文件名称
                            String suffixName = "";
                            if(fileName.indexOf(".")!=-1){
                                suffixName = fileName.substring(fileName.lastIndexOf("."));
                            }
                            fileName = attachmentsJSONArray.getJSONObject(j).getString("name")+suffixName;


                            InputStream is = connection.getInputStream();
                            //原始下载文件对象
                            File ysFile = new File(File.separator + "home" + File.separator + "weaver" + File.separator + "ecology" + File.separator + "tempfile" + File.separator + tempFileName);
                            OutputStream os = new FileOutputStream(ysFile);

                            byte[] b = new byte[2048];
                            int len = 0;
                            while (-1 != (len = is.read(b, 0, b.length))) {
                                os.write(b, 0, len);
                            }
                            os.flush();
                            if (os != null) {
                                os.close();
                                os = null;
                            }
                            if (is != null) {
                                is.close();
                                is = null;
                            }

                            //将文件进行解密
                            //解密文件对象
                            File jmFile = new File(File.separator + "home" + File.separator + "weaver" + File.separator + "ecology" + File.separator + "tempfile" + File.separator + tempFileName + "_jm");
                            FileCryptoUtil.decryptFile(FileCryptoUtil.analysisKey(attachmentsJSONArray.getJSONObject(j).getString("base64key")), ysFile.getPath(), jmFile.getPath());

                            //将解密后的文件上传到OA文档系统中，并获取上传文件的docid
                            // 生成文件ID
                            int imagefileid = new ImageFileIdUpdate().getImageFileNewId();
                            // 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
                            int createstatus = ImageFileUtil.createImageFile(jmFile.getPath(), imagefileid, fileName, getFileSize(jmFile));

                            if (createstatus == -1) {
                                new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]供应商附件上传到OA文档系统失败：" + createstatus);
                                return;
                            }

                            // 创建docdetail
                            int docId = createDocDetail(7559, fileName, 1);
                            // 创建docimagefile
                            createDocImageFile(docId, imagefileid, fileName);
                            // 创建文档共享
                            createDocShare(7559, 1, docId);

                            if (!docIds.toString().trim().equals("")) {
                                docIds.append(",");
                            }
                            docIds.append(docId);

                            ysFile.delete();
                            jmFile.delete();
                        }


                        //通过社会信用代码到uf_cggl_gysjbxxb表去查询供应商id
                        rs2.execute("select id from uf_cggl_gysjbxxb where tyshxydm = '" + socialCreditcode + "'");
                        String zxgys = "0";
                        if (rs2.next()) {
                            zxgys = rs2.getString("id");
                        }

                        //将供应商的相关数据存储到uf_cgzxjl_dt1明细表
                        rs2.execute("insert into uf_cgzxjl_dt1(mainid,gysbh,gysmc,tyshxydm,zbje,tbje,xgfj,sfzx) values('" + mainId + "'," + zxgys + ",'" + supplierName + "','" + socialCreditcode + "','" + winPrice + "','" + openPrice + "','" + docIds.toString() + "','" + isWin + "')");

                        //将供应商的附件也更新到uf_cgzxjl主表的相关材料中
                        rs2.execute("update uf_cgzxjl set xgcl=xgcl||','||'" + docIds.toString() + "' where id = " + mainId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]读取供应商附件出现异常：" + e.getMessage());
                        return;
                    }
                }

                rs2.execute("update uf_cgxqjbxxb set cgxqzt='12',zcptjg='已有结果' where id='" + projectId + "'");
            }catch(Throwable e){
                e.printStackTrace();
                new BaseBean().writeLog("[SearchProjectInfoResultFromDzcgptQuartz]处理id为"+rs.getString("id")+"的数据时，出现异常：" + e.getMessage());
                return;
            }
        }
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

    /**
     * 创建docimagefile表
     *
     * @param docid
     * @param imagefileid
     * @param filename
     */
    private void createDocImageFile(int docid, int imagefileid, String filename) {
        DocImageManager imgManger = new DocImageManager();
        BaseBean bb = new BaseBean();
        imgManger.setDocid(docid);
        imgManger.setImagefileid(imagefileid);
        imgManger.setImagefilename(filename);
        imgManger.setIsextfile("1");
        String ext = getFileExt(filename);
        if (ext.equalsIgnoreCase("doc")) {
            imgManger.setDocfiletype("3");
        } else if (ext.equalsIgnoreCase("xls")) {
            imgManger.setDocfiletype("4");
        } else if (ext.equalsIgnoreCase("ppt")) {
            imgManger.setDocfiletype("5");
        } else if (ext.equalsIgnoreCase("wps")) {
            imgManger.setDocfiletype("6");
        } else if (ext.equalsIgnoreCase("docx")) {
            imgManger.setDocfiletype("7");
        } else if (ext.equalsIgnoreCase("xlsx")) {
            imgManger.setDocfiletype("8");
        } else if (ext.equalsIgnoreCase("pptx")) {
            imgManger.setDocfiletype("9");
        } else if (ext.equalsIgnoreCase("et")) {
            imgManger.setDocfiletype("10");
        } else {
            imgManger.setDocfiletype("2");
        }
        imgManger.AddDocImageInfo();
        bb.writeLog("===创建docimagefile完成");
    }

    /**
     * 创建文档的共享
     *
     * @param seccategoryId 目录id
     * @param ownerId       文档所有者
     * @param docid         文档id
     */
    private void createDocShare(int seccategoryId, int ownerId, int docid) {
        BaseBean bb = new BaseBean();
        bb.writeLog("======seccategoryId:" + seccategoryId + ",ownerId:" + ownerId + ",docid:" + docid);
        DocManager dm = new DocManager();
        dm.setUserid(ownerId);
        dm.setId(docid);
        dm.setDocCreaterType("1");
        dm.setSeccategory(seccategoryId);
        try {
            dm.AddShareInfo();
        } catch (Exception e) {
            bb.writeLog("======createDocShare异常", e);
        }
        RecordSet rs = new RecordSet();
        rs.executeProc("Share_forDoc", "" + docid);
        dm.setUsertype("1");
        dm.setAboutCreaterShare(seccategoryId + "");
    }

    /**
     * 创建文档 docdetail表
     *
     * @param seccategory 目录
     * @param filename    文件名
     * @param docOwner    创建人
     * @return
     */
    private int createDocDetail(int seccategory, String filename, int docOwner) {
        DocManager dm = new DocManager();
        BaseBean bb = new BaseBean();
        int docid = 0;
        try {
            docid = dm.getNextDocId(new RecordSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> rsp = isOpenApproveWfByDocSeccategoryId(seccategory);    //获取目录信息
        dm.setId(docid);
        dm.setMaincategory(0);
        dm.setSubcategory(0);
        dm.setSeccategory(seccategory);
        dm.setDoclangurage(7);    //
        dm.setDocapprovable("");    //默认不审批
        dm.setDocreplyable(rsp.get("replyable"));
        dm.setIsreply("");
        dm.setReplydocid(0);
        dm.setDocsubject(filename);
        dm.setDocpublishtype("");
        dm.setItemid(0);
        dm.setItemmaincategoryid(0);
        dm.setHrmresid(0);
        dm.setCrmid(0);
        dm.setProjectid(0);
        dm.setFinanceid(0);
        dm.setDoccreaterid(docOwner);
        ResourceComInfo hrc = null;
        try {
            hrc = new ResourceComInfo();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        dm.setDocdepartmentid(Util.getIntValue(hrc.getDepartmentID("" + docOwner)));
        Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String timeStr = new SimpleDateFormat("HH:mm:ss").format(date);
        dm.setDoccreatedate(dateStr);
        dm.setDoccreatetime(timeStr);
        dm.setDoclastmoduserid(docOwner);
        dm.setDoclastmoddate(dateStr);
        dm.setDoclastmodtime(timeStr);
        dm.setDocapproveuserid(0);
        dm.setDocapprovedate("");
        dm.setDocapprovetime("");
        dm.setDocarchiveuserid(docOwner);
        dm.setDocarchivedate("");
        dm.setDocarchivetime("");
        dm.setDocstatus("1");
        dm.setParentids(docid + "");
        dm.setAssetid(0);
        dm.setOwnerid(docOwner);
        dm.setKeyword("");
        dm.setAccessorycount(1);    //附件个数，默认1
        dm.setReplaydoccount(0);
        dm.setDocCreaterType("1");
        dm.setDocType(1);
        dm.setCanCopy("1");
        dm.setCanRemind("1");
        dm.setOrderable(rsp.get("orderable"));
        dm.setDocextendname("html");    //默认html文档
        dm.setDocCode("");
        dm.setDocEdition(-1);
        dm.setDocEditionId(-1);
        dm.setIsHistory(0);
        dm.setApproveType(0);
        dm.setMainDoc(docid);
        String readoptercanprint = rsp.get("readoptercanprint");
        dm.setReadOpterCanPrint("".equals(readoptercanprint) ? 0 : Integer.valueOf(readoptercanprint));
        dm.setDocValidUserId(docOwner);
        dm.setDocValidDate(dateStr);
        dm.setDocValidTime(timeStr);
        dm.setInvalidationDate("");
        dm.setDocCreaterType("1");
        dm.setDocLastModUserType("1");
        dm.setDocApproveUserType("");
        dm.setDocValidUserType("");
        dm.setDocInvalUserType("");
        dm.setDocArchiveUserType("");
        dm.setDocCancelUserType("");
        dm.setDocPubUserType("");
        dm.setDocCancelUserType("");
        dm.setDocPubUserType("");
        dm.setDocReopenUserType("");
        dm.setOwnerType("1");
        dm.setDoccontent("");
        try {
            dm.AddDocInfo();
            bb.writeLog("===添加docdetail完成");
        } catch (Exception e) {
            bb.writeLog("===添加docdetail异常", e);
        }
        return docid;
    }

    /**
     * 得到文档的扩展名
     *
     * @param file 文档全名
     * @return 文档的扩展名
     */
    public String getFileExt(String file) {
        if (file == null || file.trim().equals("")) {
            return "";
        } else {
            int idx = file.lastIndexOf(".");
            if (idx == -1) {
                return "";
            } else {
                if (idx + 1 >= file.length()) {
                    return "";
                } else {
                    return file.substring(idx + 1);
                }
            }
        }
    }

    private Map<String, String> isOpenApproveWfByDocSeccategoryId(int seccategory) {
        RecordSet rs = new RecordSet();
        BaseBean bb = new BaseBean();
        Map<String, String> rsp = new HashMap<String, String>();
        rs.execute("select isOpenApproveWf,replyable,orderable,readoptercanprint from docseccategory where id = " + seccategory);
        if (rs.next()) {
            rsp.put("isOpenApproveWf", rs.getString("isOpenApproveWf"));
            rsp.put("replyable", rs.getString("replyable"));
            rsp.put("orderable", rs.getString("orderable"));
            rsp.put("readoptercanprint", rs.getString("readoptercanprint"));
            bb.writeLog("======查询目录信息有值");
            return rsp;
        }
        return rsp;
    }

    /**
     * 表单建模数据增加权限
     * @param creatorid
     * @param modeid
     * @param formtable
     */
    public int addFormmodeRight(String creatorid,int modeid,String formtable){

        RecordSet recordSet2=new RecordSet();
        int billid=0;
        String sql="select max(id) as maxid from "+formtable;
        recordSet2.execute(sql);
        if(recordSet2.next()){
            billid=recordSet2.getInt("maxid");
        }

        String modedatacreatedate= TimeUtil.getCurrentDateString();
        String modedatacreatetime= TimeUtil.getOnlyCurrentTimeString();

        sql="update "+formtable+" set formmodeid="+modeid+",modedatacreatertype=0,modedatacreater="+creatorid+",modedatacreatedate='"+modedatacreatedate+"',modedatacreatetime='"+modedatacreatetime+"' where id="+billid;
        recordSet2.execute(sql);

        //构建数据权限
        ModeRightInfo ModeRightInfo = new ModeRightInfo();
        ModeRightInfo.setNewRight(true);
        ModeRightInfo.editModeDataShare(Util.getIntValue(creatorid),modeid,billid);

        return billid;
    }

    public static void main(String[] args) throws Exception{
        String fileName = "123456.txt";
        String suffixName = "";
        if(fileName.indexOf(".")!=-1){
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        System.out.println(suffixName);

    }
}
