package weaver.dfqcgsjszx.util.syn;

import com.ibm.icu.text.SimpleDateFormat;
import weaver.conn.RecordSet;
import weaver.docs.docs.DocImageManager;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.datasource.DataSource;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 同步采购合同的附件
 *
 * @author Alex.Du
 */
public class SynCghtFile {
    public void start(){
        BaseBean baseBean = new BaseBean();
        RecordSet recordSet = new RecordSet();

        //查询当前已同步了多少篇文档
        recordSet.execute("select max(keyword+0) as maxKeyword from docdetail where seccategory=7556");
        String maxKeyword = "";
        if(recordSet.next()){
            maxKeyword = recordSet.getString("maxKeyword");
        }

        maxKeyword="78481";

        String appendWhere = "";
        if(maxKeyword!=null&&!maxKeyword.trim().equals("")){
            appendWhere = " and affixid>"+maxKeyword;
        }

        DataSource ds = (DataSource) StaticObj.getServiceByFullname(("datasource.ec"), DataSource.class);
        Connection conn = ds.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        try{
            statement = conn.createStatement();
            rs = statement.executeQuery("select * from OLD_CGHT_FJ where AUDITCONTENT is not null"+appendWhere+" and applybillid in (select htid from old_cght_jbxx where htid > 6000033405) order by affixid");
            while(rs.next()){
                baseBean.writeLog("当前处理的affixid："+rs.getString("affixid"));

                recordSet.execute("select * from docdetail where keyword='"+rs.getString("affixid")+"' and seccategory=7556");
                if(recordSet.next()){
                    baseBean.writeLog("affixid为【"+rs.getString("affixid")+"】的附件已经存在，将跳过该条数据的处理");
                    continue;
                }

                String sqdbh = rs.getString("applybillid");//申请单编号
                baseBean.writeLog("当前处理的申请单编号为：" + sqdbh);
                String fjName = rs.getString("AFFIXNAME");//附件名称
                baseBean.writeLog("附件名称为：" + fjName);
                Blob fjContent = rs.getBlob("AUDITCONTENT");//附件内容，字节输入流
                baseBean.writeLog("获取到的附件内容：" + fjContent + ")");
                //定义一个接收的文件对象
                File file = new File("/home/sqdfile/" + fjName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        baseBean.writeLog("创建合同编号为：" + sqdbh + "的采购合同附件文件时出现异常(exception:+" + e.getMessage() + ")");
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


                    // 生成文件ID
                    int imagefileid = new ImageFileIdUpdate().getImageFileNewId();
                    // 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
                    int createstatus = ImageFileUtil.createImageFile("/home/sqdfile/" + fjName, imagefileid, file.getName(), getFileSize(file));

                    if (createstatus == -1) {
                        new BaseBean().writeLog("文件上传文件系统失败,数据编号为：" + sqdbh);
                        continue;
                    }

                    // 创建docdetail
                    int docid = createDocDetail(7556, file.getName(), 1);
                    // 创建docimagefile
                    createDocImageFile(docid, imagefileid, file.getName());
                    // 创建文档共享
                    createDocShare(7556, 1, docid);

                    recordSet.execute("update docdetail set keyword='" + rs.getString("affixid") + "' where id='" + docid + "'");
                    baseBean.writeLog("修改文档keyword的sql语句：update docdetail set keyword='" + rs.getString("affixid") + "' where id='" + docid + "'");

                    new BaseBean().writeLog("存储到ecology系统中的文件ID为：" + docid);

                    recordSet.execute("update uf_cghtjbxx set htfj='" + docid + "'||','||htfj where bz='" + sqdbh + "'");
                    baseBean.writeLog("执行的更新语句：update uf_cghtjbxx set htfj='" + docid + "'||','||htfj where bz='" + sqdbh + "'");

                    recordSet.execute("select htsplc from uf_cghtjbxx where bz='" + sqdbh + "'");
                    while(recordSet.next()){
                        baseBean.writeLog("查询到的htsplc字段值为：'"+recordSet.getString("htsplc")+"'");
                        if(null!=recordSet.getString("htsplc")&&!recordSet.getString("htsplc").trim().equals("")){
                            recordSet.execute("update formtable_main_200 set htfj='" + docid + "'||','||htfj where requestid='"+recordSet.getString("htsplc")+"'");
                            baseBean.writeLog("执行的更新采购需求流程表的语句为：update formtable_main_200 set htfj='" + docid + "'||','||htfj where requestid='"+recordSet.getString("htsplc")+"'");
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    baseBean.writeLog("同步采购合同申请的附件出现异常1："+e.getMessage());
                    continue;
                }finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            baseBean.writeLog("关闭输出流时出现异常");
                        } finally {
                            os = null;
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            baseBean.writeLog("关闭输入流时出现异常");
                        } finally {
                            is = null;
                        }
                    }
                    b = null;
                    file.delete();
                    file = null;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            baseBean.writeLog("同步采购需求申请的附件出现异常2："+e.getMessage());
        }finally {
            if(rs!=null){
                try {
                    rs.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                    baseBean.writeLog("关闭rs时出现异常");
                }finally {
                    rs = null;
                }
            }
            if(statement!=null){
                try {
                    statement.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                    baseBean.writeLog("关闭statement时出现异常");
                }finally {
                    statement = null;
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                    baseBean.writeLog("关闭conn时出现异常");
                }finally {
                    conn = null;
                }
            }
            if(ds!=null){
                conn = null;
            }
            if(recordSet!=null){
                recordSet = null;
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
            filesize = df.format((double) ((double) fis.available()));
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
}
