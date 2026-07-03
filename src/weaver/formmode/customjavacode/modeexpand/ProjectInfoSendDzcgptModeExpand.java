package weaver.formmode.customjavacode.modeexpand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.FileCryptoUtil;
import weaver.dfqcgsjszx.util.http_request.HttpsSendUtil;
import weaver.file.ImageFileManager;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.Base64;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 采购需求申请数据发送至电子采购平台
 * @author Alex.Du
 */
public class ProjectInfoSendDzcgptModeExpand  extends AbstractModeExpandJavaCodeNew {
    /**
     * 执行模块扩展动作
     * @param param
     *  param包含(但不限于)以下数据
     *  user 当前用户
     *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
     *  页面链接拼接的参数，比如b=2,可以通过param.get("b")来获取参数
     * @return
     */
    @Override
    public Map<String, String> doModeExpand(Map<String, Object> param) {
        RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
        int billid = Util.getIntValue(requestInfo.getRequestid());
        Map<String, String> returnResult = new HashMap<String, String>();

        new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]开始执行");

        String projectCode = "";//项目编号
        String projectId = ""+billid;//项目唯一ID
        String projectName = "";//项目名称
        String tenderType = "0";//采购方式，固定值0
        String isAgent = "1";//是否委托项目，固定值1
        String purchaserLinkmen = "";//采购担当账号
        String purchaserLinkmenUserName = "";//采购担当名称
        String purchaserTel = "";//采购担当手机号码
        String checkPlan = "";//评审方法
        String budget = "";//预算价（含税）
        String isPublic = "0";//公开范围，固定值0
        String organNO = "91533103MA6PAL7N5N";//社会统一信用码,随便写个值
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//创建时间
        String callbackUrl = "xxx";//结果回推地址，随便给个值
        String bjmb = "";//报价模板
        String zhpsb = "";//综合评审表
        String jsrws = "";//技术任务书


        RecordSet rs = new RecordSet();

        rs.execute("select * from uf_cgxqjbxxb where id = "+billid);


        if(rs.next()){
            projectCode = rs.getString("cgxqbh");//项目编号
            projectName = rs.getString("cgxqmc");//项目名称
            purchaserLinkmen = rs.getString("swzjbr");//采购担当账号
            checkPlan = rs.getString("psff");//评审方法
            budget = rs.getString("xmzeyghsfyy");//预算价（含税）
            bjmb = rs.getString("bjmb");//报价模板
            zhpsb = rs.getString("zhpsb");//综合评审表
            jsrws = rs.getString("jsrws");//技术任务书
        }

        //处理表单数据
        //处理评审方法数据
        if(checkPlan.equals("0")){
            checkPlan = "1";
        }else{
            checkPlan = "0";
        }

        //处理采购担当数据
        rs.execute("select workcode,lastname,mobile from hrmresource where id="+purchaserLinkmen);
        if(rs.next()){
            purchaserLinkmen = rs.getString("lastname");
            purchaserLinkmenUserName = rs.getString("workcode");
            purchaserTel = rs.getString("mobile");
        }

        //处理附件数据
        JSONArray attachmentsJSONArray = new JSONArray();
        try {
            //生成用于加密文件的key
            String encryptFileKey = FileCryptoUtil.getKey();
            //报价模板附件
            String[] bjmbIds = bjmb.split(",");
            for (int i = 0; i < bjmbIds.length; i++) {
                if (bjmbIds[i].trim().equals("")) {
                    continue;
                }
                attachmentsJSONArray.add(getFileJSON(bjmbIds[i], "5", encryptFileKey));
            }
            //综合评审表附件
            String[] zhpsbIds = zhpsb.split(",");
            for (int i = 0; i < zhpsbIds.length; i++) {
                if (zhpsbIds[i].trim().equals("")) {
                    continue;
                }
                attachmentsJSONArray.add(getFileJSON(zhpsbIds[i], "99", encryptFileKey));
            }
            //技术任务书附件
            String[] jsrwsIds = jsrws.split(",");
            for (int i = 0; i < jsrwsIds.length; i++) {
                if (jsrwsIds[i].trim().equals("")) {
                    continue;
                }
                attachmentsJSONArray.add(getFileJSON(jsrwsIds[i], "4", encryptFileKey));
            }
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]调用电子采购平台出现异常："+e.getMessage());
            rs.execute("update uf_cgxqjbxxb set zcpthxxx='失败' where id="+billid);
            return returnResult;
        }


        JSONObject sendJSON = new JSONObject();
        //设置请求头部数据
        sendJSON.put("dataCode","projectInfo");
        sendJSON.put("platformCode","00270013");
        sendJSON.put("abbreviationCode","dfzx");
        sendJSON.put("token","473b1c1b-1cde-44c1-9a91-8aa95734dba7");
        sendJSON.put("version","V1.0");

        //获取表单数据，用表单数据作为数据部分（消息体data）进行传递
        JSONObject dataJSON = new JSONObject();

        dataJSON.put("projectCode",projectCode);
        dataJSON.put("projectId",projectId);
        dataJSON.put("projectName",projectName);
        dataJSON.put("tenderType",tenderType);
        dataJSON.put("isAgent",isAgent);
        dataJSON.put("purchaserLinkmen",purchaserLinkmen);
        dataJSON.put("purchaserLinkmenUserName",purchaserLinkmenUserName);
        dataJSON.put("purchaserTel",purchaserTel);
        dataJSON.put("checkPlan",checkPlan);
        //dataJSON.put("budget",budget);
        dataJSON.put("isPublic",isPublic);
        dataJSON.put("organNO",organNO);
        dataJSON.put("createTime",createTime);
        dataJSON.put("projectCode",projectCode);
        dataJSON.put("callbackUrl",callbackUrl);

        //封装消息体
        sendJSON.put("data",dataJSON);


        //封装附件（暂时为空）
        sendJSON.put("attachments",attachmentsJSONArray);

        new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]发送到电子采购平台的数据内容为："+sendJSON.toJSONString());

        String result = null;
        try {
            Map<String, String> requestPropertes = new HashMap<>();
            requestPropertes.put("Content-type", "application/json");
            result = HttpsSendUtil.httpRequest("https://www.dfmjyzx.com/DTCService/receive", "POST", sendJSON.toJSONString(), requestPropertes);
            new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]电子采购平台返回的数据内容为："+result);
        }catch(Throwable e){
            e.printStackTrace();
            new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]调用电子采购平台出现异常："+e.getMessage());
            rs.execute("update uf_cgxqjbxxb set zcpthxxx='失败' where id="+billid);
            return returnResult;
        }

        JSONObject resultJSON = JSONObject.parseObject(result);

        //判断接口是否调用返回成功
        if(!resultJSON.getBoolean("success")){
            new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]调用电子采购平台返回失败：" + result);
            rs.execute("update uf_cgxqjbxxb set zcpthxxx='失败' where id="+billid);
            return returnResult;
        }else{
            rs.execute("update uf_cgxqjbxxb set zcpthxxx='成功',zcptjg='等待结果' where id="+billid);
        }

        new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand]调用电子采购平台返回成功结束");
        
        return returnResult;
    }

    /**
     * 将文件进行加密，获取加密文件的BASE64，调用电子采购平台的接口，上传文件获取文件在电子采购平台的路径。将路径和文件名、加密key、文件类型、文件传递时间组成一个JSON对象进行返回
     * @param docID 文件在ecology系统中的docid
     * @param fileType 文件类型：5为报价模板，99为综合评审表，4为技术任务书。文件类型的数字编号来自于电子采购平台的接口定义书
     * @param encryptFileKey 用于对文件进行加密的key
     * @return
     */
    public JSONObject getFileJSON(String docID, String fileType, String encryptFileKey) throws Exception{
        File ysFile = new File(File.separator+"home"+File.separator+"weaver"+File.separator+"ecology"+File.separator+"tempfile"+File.separator+docID);//原始文件
        File jmFile = new File(File.separator+"home"+File.separator+"weaver"+File.separator+"ecology"+File.separator+"tempfile"+File.separator+docID+"_encryptFile");//加密文件
        JSONObject fileJSON = new JSONObject();
        fileJSON.put("ftype",fileType);
        fileJSON.put("base64key",encryptFileKey);
        fileJSON.put("dataTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        RecordSet rs = new RecordSet();
        rs.execute("select imagefileid,imagefilename,filerealpath,fileSize,iszip from imagefile where imagefileid=(select imagefileid from docimagefile where docid='" + docID + "')");
        if (rs.next()) {
            fileJSON.put("name",rs.getString("imagefilename"));

            List<Byte> list = new ArrayList<Byte>();
            ZipFile zf = null;
            ZipInputStream zin = null;
            ZipEntry ze = null;
            InputStream is = null;
            OutputStream os = null;
            byte[] b = new byte[2048];
            byte[] b2 = null;
            int len = 0;
            try {
                // 判断当前文件是否是压缩文件
                if (1 == rs.getInt("iszip")) {
                    // 如果是压缩文件，获取压缩包内的文件流
                    new BaseBean().writeLog("压缩文件");
                    zf = new ZipFile(rs.getString("filerealpath"));
                    zin = new ZipInputStream(new BufferedInputStream(
                            new FileInputStream(rs.getString("filerealpath"))));
                    ze = zin.getNextEntry();
                    is = zf.getInputStream(ze);
                } else {
                    // 非压缩文件，获取该文件的文件流
                    new BaseBean().writeLog("非压缩文件");
                    ImageFileManager imageFileManager = new ImageFileManager();
                    imageFileManager.getImageFileInfoById(Util.getIntValue(
                            rs.getString("imagefileid"), 0));
                    is = imageFileManager.getInputStream();
                }

                //将文件先存储到/home/weaver/ecology/tempfile/下面，然后进行加密
                os = new FileOutputStream(new File(File.separator+"home"+File.separator+"weaver"+File.separator+"ecology"+File.separator+"tempfile"+File.separator+docID));
                while (-1 != (len = is.read(b, 0, b.length))) {
                    os.write(b,0,len);
                }
                //将文件进行加密
                FileCryptoUtil.encryptFile(encryptFileKey,ysFile.getPath(),jmFile.getPath());

                //读取加密文件，将加密文件Base64为一个字符串
                is.close();//释放之前连接到原始文件的流
                is = null;
                is = new FileInputStream(jmFile);
                while (-1 != (len = is.read(b, 0, b.length))) {
                    for (int i = 0; i < len; i++) {
                        list.add(b[i]);
                    }
                }

                b2 = new byte[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    b2[i] = list.get(i);
                }

                String fileBase64 = new String(Base64.encode(b2));

                //调用电子采购平台的接口，将文件传递到电子采购平台，并获取平台返回的文件地址
                JSONObject sendJSON = new JSONObject();
                //设置请求头部数据
                sendJSON.put("dataCode","fileList");
                sendJSON.put("platformCode","00270013");
                sendJSON.put("abbreviationCode","dfzx");
                sendJSON.put("token","473b1c1b-1cde-44c1-9a91-8aa95734dba7");
                sendJSON.put("version","V1.0");

                //获取表单数据，用表单数据作为数据部分（消息体data）进行传递
                JSONArray dataJSONArray = new JSONArray();
                JSONObject dataJSON = new JSONObject();
                dataJSON.put("fileBase64",fileBase64);
                dataJSON.put("fileName",rs.getString("imagefilename"));
                dataJSONArray.add(dataJSON);
                sendJSON.put("data",dataJSONArray);

                new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand.getFileJSON]附件发送到电子采购平台的数据内容为："+sendJSON.toJSONString());

                String result = null;
                try {
                    Map<String, String> requestPropertes = new HashMap<>();
                    requestPropertes.put("Content-type", "application/json");
                    result = HttpsSendUtil.httpRequest("https://www.dfmjyzx.com/DTCService/receive", "POST", sendJSON.toJSONString(), requestPropertes);
                }catch(Throwable e){
                    e.printStackTrace();
                    new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand.getFileJSON]调用电子采购平台文件上传接口出现异常："+e.getMessage());
                }
                new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand.getFileJSON]电子采购平台返回的数据内容为："+result);

                JSONObject resultJSON = JSONObject.parseObject(result);

                //判断接口是否调用返回成功
                if(!resultJSON.getBoolean("success")){
                    new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand.getFileJSON]调用电子采购平台返回失败：" + result);
                    throw new Exception("调用电子采购平台文件上传接口失败");
                }else{
                    fileJSON.put("path",resultJSON.getJSONArray("data").getJSONObject(0).getString("filePath"));
                }
            } catch (Exception e) {
                new BaseBean().writeLog("[ProjectInfoSendDzcgptModeExpand.getFileJSON]处理文件出现异常：" + e.getMessage());
                throw e;
            } finally {
                if(ysFile!=null&&ysFile.exists()){
                    ysFile.delete();
                    ysFile = null;
                }

                if(jmFile!=null&&jmFile.exists()){
                    jmFile.delete();
                    jmFile = null;
                }

                // 释放对象
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (zin != null) {
                    try {
                        zin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (zf != null) {
                    try {
                        zf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                is = null;
                os = null;
                ze = null;
                zin = null;
                zf = null;
                list = null;
                b2 = null;
            }

        }

        rs = null;


        return fileJSON;
    }

}
