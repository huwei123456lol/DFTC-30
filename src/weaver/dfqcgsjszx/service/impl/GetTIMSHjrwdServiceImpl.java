package weaver.dfqcgsjszx.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.dfqcgsjszx.service.GetTIMSHjrwdService;
import weaver.dfqcgsjszx.util.cms.service_client.ICmsItemInfoToCosServiceProxy;
import weaver.docs.docs.DocImageManager;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取TIMS核价任务单数据
 * @author Alex.Du
 */
public class GetTIMSHjrwdServiceImpl extends BaseBean implements GetTIMSHjrwdService {
    /**
     * 获取核价任务单
     * @param gydh 工艺单号
     * @return
     */
    @Override
    public String getHjrwd(String gydh) {
        writeLog("[GetTIMSHjrwdServiceImpl]接收到核价任务单数据："+gydh);
        //参数为工艺单号，整体逻辑因对接方的开发能力有限，改成对方传递工艺单号，我们这边根据工艺单号到他们提供的视图进行查询。
        JSONObject returnJSON = new JSONObject();
        RecordSet rs = new RecordSet();
        RecordSetDataSource rsds = new RecordSetDataSource("TIMS");
        RecordSetDataSource rsds2 = new RecordSetDataSource("TIMS");
        int mainid = 0;
        try {
            //处理主表单数据
            //处理附件，根据工艺单号查询附件，FUJXX字段不为空的视图数据，则是附件数据
            rsds.execute("select * from tims.view_tims_cos_yjxx where FUJXX is not null and YJDH = "+ gydh);
            StringBuffer docIds = new StringBuffer();

            while(rsds.next()){
                String tempFileName = UUID.randomUUID().toString();//临时文件名称
                String fileName = rsds.getString("FILENAME");

                //构建文件路径
                String filePath = rsds.getString("FUJXX");

                writeLog("[GetTIMSHjrwdServiceImpl]原始文件路径为："+filePath);

                filePath ="/trans_from_tims/data"+filePath.substring(filePath.lastIndexOf("/data")+5,filePath.lastIndexOf("/")+1);

                writeLog("[GetTIMSHjrwdServiceImpl]处理后的文件路径为："+filePath);

                //使用批处理命令（封装成了sh文件），转换为临时文件
                String linuxCmd = "/home/weaver/ecology/cpTIMSFile.sh "+filePath+" "+tempFileName;
                this.writeLog("[GetTIMSHjrwdServiceImpl]执行的命令为:"+linuxCmd);
                String result = executeLinuxCmd(linuxCmd);
                this.writeLog("[GetTIMSHjrwdServiceImpl]执行的命令结果为:"+result);

                File tempfile = new File("/tmp/timsFile",tempFileName);

                // 生成文件ID
                int imagefileid = new ImageFileIdUpdate().getImageFileNewId();
                // 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
                try {
                    ImageFileUtil.createImageFile(new FileInputStream(tempfile), imagefileid, fileName, getFileSize(tempfile));
                }catch(Exception e){
                    new BaseBean().writeLog("[GetTIMSHjrwdServiceImpl]附件上传到OA文档系统失败："+e.getMessage());
                    returnJSON.put("state",0);
                    returnJSON.put("msg","创建文件失败："+e.getMessage());
                    return returnJSON.toJSONString();
                }

                // 创建docdetail
                int docId = createDocDetail(18060, fileName, 1);
                // 创建docimagefile
                createDocImageFile(docId, imagefileid, fileName);
                // 创建文档共享
                createDocShare(18060, 1, docId);

                if(!docIds.toString().trim().equals("")){
                    docIds.append(",");
                }
                docIds.append(docId);

                tempfile.delete();
            }


            //记录主表是更新还是新增
            boolean isInsert = false;

            //查询主数据
            rsds.execute("select * from tims.view_tims_cos_yjxx where SQDH is not null and YJDH = " + gydh);
            if(rsds.next()) {
                //计算单据编号
                Calendar calendar = Calendar.getInstance();
                String djbh = null;
                rs.execute("select max(djbh) maxdjbh from uf_cggl_hjrwd where djbh like '%" + calendar.get(Calendar.YEAR) + "%'");
                if (rs.next()) {
                    String maxdjbh = rs.getString("maxdjbh");

                    if (null == maxdjbh || maxdjbh.trim().equals("")) {
                        //未查询到今年最大的记录，则使用该年份的0001号编号
                        djbh = calendar.get(Calendar.YEAR) + "0001";
                    } else {
                        //查询到今年的最大记录，则将最大记录编号加1
                        String xh = String.format("%04d", Integer.parseInt(maxdjbh.substring(4)) + 1);
                        djbh = calendar.get(Calendar.YEAR) + xh;
                    }
                } else {
                    //未查询到今年最大的记录，则使用该年份的0001号编号
                    djbh = calendar.get(Calendar.YEAR) + "0001";
                }
                calendar = null;

                //计算类别
                String lb = null;
                if (rsds.getString("YJDLX").trim().equals("DB")) {
                    lb = "0";
                } else {
                    lb = "1";
                }

                //转换专业师
                String zys = null;
                rs.execute("select id from hrmresource where workcode='" + rsds.getString("ZYS") + "'");
                if (rs.next()) {
                    zys = rs.getString("id");
                }
                //查询万元以上采购工程师
                String htqddh = rsds.getString("HTQDDH");
                String cggcs = "11189";
                new BaseBean().writeLog("合同启动单号" + htqddh );
                if(!"".equals(htqddh)){
                    RecordSet rshtqdd = new RecordSet();
                    rshtqdd.execute("select swzjbr from uf_cgxqjbxxb where cgxqbh ='" + htqddh + "'");
                    if(rshtqdd.next()){
                        cggcs = rshtqdd.getString("swzjbr");
                    }
                }

                //根据工艺单号查询主表中是否存在这条数据，存在则更新，不存在则插入
                rs.execute("select * from uf_cggl_hjrwd where gydh='"+gydh+"'");
                if(rs.next()) {





                    mainid = rs.getInt("id");
                    rs.execute("update uf_cggl_hjrwd set djbh='"+djbh
                            +"',cgxqdh='"+htqddh
                            +"',gydh='"+rsds.getString("YJDH")
                            +"',sqdh='"+rsds.getString("SQDH")
                            +"',rwmc='"+rsds.getString("SQRWMS")
                            +"',lb='"+lb
                            +"',lycs='"+rsds.getString("LYCS")
                            +"',zys="+zys+",cggcs='" + cggcs + "',zt=0,cgfs='"+rsds.getString("YJDLX")
                            +"',sfzj='"+rsds.getString("SFZJ")
                            +"',sflcgys='"+rsds.getString("SFLCGYS")
                            +"',fjxx='"+docIds
                            +"',xmfyh='"+ rsds.getString("xmh")
                            +"' where id = "+mainid);
                }else{
                    isInsert = true;
                    rs.executeQuery("insert into uf_cggl_hjrwd(djbh,cgxqdh,gydh,sqdh,rwmc,lb,lycs,zys,cggcs,zt,cgfs,sfzj,sflcgys,fjxx,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid,xmfyh) values('"
                                    +djbh
                                    +"','"
                                    +htqddh
                                    +"','"
                                    +rsds.getString("YJDH")
                                    +"','"
                                    +rsds.getString("SQDH")
                                    +"','"
                                    +rsds.getString("SQRWMS")
                                    +"','"
                                    +lb
                                    +"','"
                                    +rsds.getString("LYCS")
                                    +"',"
                                    + zys
                                    +",'" + cggcs + "',0,'"
                                    + rsds.getString("YJDLX")
                                    +"','"
                                    + rsds.getString("SFZJ")
                                    +"','"
                                    + rsds.getString("SFLCGYS")
                                    +"','"
                                    +docIds
                                    +"',82008,1,0,'"
                                    +new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                                    +"','"
                                    +new SimpleDateFormat("HH:mm:ss").format(new Date())
                                    +"','"
                                    +UUID.randomUUID()
                                    +"','"
                                    +rsds.getString("xmh")
                                    +"')");

                    //获取插入的主表id值
                    rs.execute("select max(id) maxid from uf_cggl_hjrwd");
                    if (rs.next()) {
                        mainid = rs.getInt("maxid");
                    }
                }
            }

            //查询零件数据
            rsds.execute("select * from tims.view_tims_cos_yjxx where WLBM is not null and YJDH = "+gydh);
            //插入明细表1（零件信息）
            ICmsItemInfoToCosServiceProxy proxy = new ICmsItemInfoToCosServiceProxy();
            JSONObject cmsParamJSON = new JSONObject();

            while (rsds.next()) {
                //通过CMS的接口查询CMS量产价格
                cmsParamJSON.remove("dataList");
                cmsParamJSON.remove("billId");

                JSONArray cmsDataListJSONArray = new JSONArray();
                JSONObject cmsDataListParamJSON = new JSONObject();
                cmsDataListParamJSON.put("itemCode",rsds.getString("WLBM"));
                cmsDataListJSONArray.add(cmsDataListParamJSON);

                cmsParamJSON.put("billId",UUID.randomUUID().toString());
                cmsParamJSON.put("dataList",cmsDataListJSONArray);
                String result = "";
                float cmsPrice = 0f;
                try {
                    writeLog("[GetTIMSHjrwdServiceImpl]调用CMS接口参数为："+cmsParamJSON.toJSONString());
                    result = proxy.getItemInfoByItemCode(cmsParamJSON.toJSONString());
//                    writeLog("[GetTIMSHjrwdServiceImpl]CMS接口返回："+result);
                    JSONArray resultJSONArray = JSONArray.parseArray(result);
                    cmsPrice = resultJSONArray.getJSONObject(0).getJSONArray("dataList").getJSONObject(0).getFloat("price");
                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("[GetTIMSHjrwdServiceImpl]调用CMS接口查询CMS量产价格出现异常："+e.getMessage());
                }

                cmsDataListParamJSON = null;
                cmsDataListJSONArray = null;

                //查询TIMS的最新、最高、最低价格
                rsds2.execute("select ZXJG,ZDJG,ZGJG from tims.VIEW_TIMS_COS_LJJG where WLH = '"+rsds.getString("WLBM")+"'");
                String timsZxjg = "";
                String timsZdjg = "";
                String timsZgjg = "";
                if(rsds2.next()){
                    timsZxjg = rsds2.getString("ZXJG");
                    timsZdjg = rsds2.getString("ZDJG");
                    timsZgjg = rsds2.getString("ZGJG");
                }

                //根据零部件编号和主表id查询明细表1中是否存在这条数据，存在则更新，不存在则插入
                rs.execute("select * from uf_cggl_hjrwd_dt1 where lbjbh='"+rsds.getString("WLBM")+"' and mainid="+mainid);
                if(rs.next()){
                    int detailId = rs.getInt("id");
                    rs.executeQuery("update uf_cggl_hjrwd_dt1 set mainid=?,lbjbh=?,lbjmc=?,gzfa=?,tzsh=?,szsl=?,gyfa=?,dhjd=?,dhdz=?,ckjg=?,yjdhrq=?,dw=?,cmslcjg=?,timszxjg=?,timszgjg=?,timszdjg=? where id=?"
                            , mainid
                            , rsds.getString("WLBM")
                            , rsds.getString("WLMC")
                            , rsds.getString("GZFA")
                            , rsds.getString("WLBB")
                            , rsds.getString("SL")
                            , rsds.getString("GYLC")
                            , rsds.getString("DHDMC")
                            , rsds.getString("DHDZ")
                            , rsds.getString("CKJG")
                            , rsds.getString("JYSJ")
                            , rsds.getString("JLDW")
                            , cmsPrice
                            , timsZxjg
                            , timsZgjg
                            , timsZdjg
                            , detailId);
                }else {
                    rs.executeQuery("insert into uf_cggl_hjrwd_dt1(mainid,lbjbh,lbjmc,gzfa,tzsh,szsl,gyfa,dhjd,dhdz,ckjg,yjdhrq,dw,cmslcjg,timszxjg,timszgjg,timszdjg) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                            , mainid
                            , rsds.getString("WLBM")
                            , rsds.getString("WLMC")
                            , rsds.getString("GZFA")
                            , rsds.getString("WLBB")
                            , rsds.getString("SL")
                            , rsds.getString("GYLC")
                            , rsds.getString("DHDMC")
                            , rsds.getString("DHDZ")
                            , rsds.getString("CKJG")
                            , rsds.getString("JYSJ")
                            , rsds.getString("JLDW")
                            , cmsPrice
                            , timsZxjg
                            , timsZgjg
                            , timsZdjg);
                }
            }
            proxy = null;
            cmsParamJSON = null;

            //查询供应商信息
            rsds.execute("select * from tims.view_tims_cos_yjxx where GYSBM is not null and YJDH = " + gydh);
            //插入明细表2（供应商信息）
            while(rsds.next()){
                //根据供应商编号和主表id查询明细表2中是否存在这条数据，存在则更新，不存在则插入
                rs.execute("select * from uf_cggl_hjrwd_dt2 where mainid='"+mainid+"' and gysbh='"+rsds.getString("GYSBM")+"'");
                if(rs.next()){
                    int detailId = rs.getInt("id");
                    rs.executeQuery("update uf_cggl_hjrwd_dt2 set gysqc=? where id=?",rsds.getString("GYSMC"),detailId);
                }else {
                    rs.executeQuery("insert into uf_cggl_hjrwd_dt2(mainid,gysbh,gysqc) values(?,?,?)"
                            , mainid
                            , rsds.getString("GYSBM")
                            , rsds.getString("GYSMC"));
                }
            }

            //查询工装信息
            rsds.execute("select * from tims.view_tims_cos_yjxx where GZBM is not null and YJDH = " + gydh);

            //插入明细表3（工装信息）
            while(rsds.next()){
                //根据工装零件号和主表id查询明细表3中是否存在这条数据，存在则更新，不存在则插入
                rs.executeQuery("select * from uf_cggl_hjrwd_dt3 where mainid=? and gzljh=?",mainid,rsds.getString("GZBM"));
                if(rs.next()){
                    int detailId = rs.getInt("id");
                    rs.executeQuery("update uf_cggl_hjrwd_dt3 set mainid=?,ljbh=?,ljmc=?,gzljh=?,gztzsh=?,gzmc=?,gzsl=?,gzly=?,gzlx=? where id=?", mainid
                            , rsds.getString("WLBM")
                            , rsds.getString("WLMC")
                            , rsds.getString("GZBM")
                            , rsds.getString("GZBB")
                            , rsds.getString("GZMC")
                            , rsds.getString("GZSL")
                            , rsds.getString("GZLY")
                            , rsds.getString("GZLX")
                            , detailId);
                }else {
                    rs.executeQuery("insert into uf_cggl_hjrwd_dt3(mainid,ljbh,ljmc,gzljh,gztzsh,gzmc,gzsl,gzly,gzlx) values(?,?,?,?,?,?,?,?,?)"
                            , mainid
                            , rsds.getString("WLBM")
                            , rsds.getString("WLMC")
                            , rsds.getString("GZBM")
                            , rsds.getString("GZBB")
                            , rsds.getString("GZMC")
                            , rsds.getString("GZSL")
                            , rsds.getString("GZLY")
                            , rsds.getString("GZLX"));
                }
            }

            if(isInsert) {
                // 构建数据权限
                ModeRightInfo modeRightInfo = new ModeRightInfo();
                modeRightInfo.setNewRight(true);
                modeRightInfo.editModeDataShare(1, 82008, mainid);
            }

            returnJSON.put("state",1);
            returnJSON.put("msg","成功");
        }catch(Exception e){
            e.printStackTrace();
            writeLog("[GetTIMSHjrwdServiceImpl]获取核价任务单数据出现异常："+e.getMessage());
            returnJSON.put("state",0);
            returnJSON.put("msg","获取核价任务单数据出现异常："+e.getMessage());

            //出现异常，删除插入过的数据
            if(mainid!=0){
                rs.execute("delete from uf_cggl_hjrwd_dt1 where mainid="+mainid);
                rs.execute("delete from uf_cggl_hjrwd_dt2 where mainid="+mainid);
                rs.execute("delete from uf_cggl_hjrwd_dt3 where mainid="+mainid);
                rs.execute("delete from uf_cggl_hjrwd where id="+mainid);
            }
        }finally {
            rs = null;
            rsds = null;
        }
        new BaseBean().writeLog("[GetTIMSHjrwdServiceImpl]获取核价任务单数据 result" + returnJSON.toJSONString());
        return returnJSON.toJSONString();
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
        String dateStr = new com.ibm.icu.text.SimpleDateFormat("yyyy-MM-dd").format(date);
        String timeStr = new com.ibm.icu.text.SimpleDateFormat("HH:mm:ss").format(date);
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
     * 执行cmd命令（linux命令）
     * @param cmd
     * @return
     */
    public String executeLinuxCmd(String cmd) {
        System.out.println("执行命令[ " + cmd + "]");
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmd);
            String line;
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer out = new StringBuffer();
            while ((line = stdoutReader.readLine()) != null ) {
                out.append(line);
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            process.destroy();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "http://10.4.10.203:9080/ctmserver/usr/tims/data/dcwj/1605481116395/SZ19080101-V34（不含电四驱0） - 副本.xls";
        String fileName =url.substring(url.lastIndexOf("/data")+5,url.lastIndexOf("/")+1);

        System.out.println("/trans_from_tims/data"+fileName);
    }

}
