package weaver.dfqcgsjszx.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.general.TimeUtil;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.ws.service_client.ISendMessageServiceProxy;
import weaver.file.ImageFileManager;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 因公出国出访报告调用外事接口
 * @author Alex.Du
 */
public class YgcgcfbgToWsAction extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo requestInfo) {
        //获取主表参数
        String guid = requestInfo.getRequestid();//GUID，使用当前请求的RequestId
        String bt1 = "";//标题
        String tcmc = "";//团长名称（团长姓名）

        String xglc = "";//相关流程(团组ID)
        String misstionId = "";//因公出国表主键
        String misstionNo = "";//因公出国表批件号

        String tzmc = "";//团组名称
        String cfrw = "";//出访任务
        String cfrs = "";//出访人数
        String sjcfrs = "";//实际出访人数
        String cfgjdx = "";//出访国家
        String sjcfgjdx = "";//实际出访国家
        String cflx = "";//出访路线
        String sjcflx = "";//实际出访路线
        String cfksrq = "";//出访开始日期（出访开始时间）
        String cfjsrq = "";//出访结束日期（出访结束时间）
        String sjcfksrq = "";//实际出访开始日期（实际出访开始时间）
        String sjcfjsrq = "";//实际出访结束日期（实际出访结束时间）
        String bz = "";//备注

        String sqr = "";//创建人
        String creatorNo = "";//创建人编号
        String creatorEmail = "";//创建人邮箱
        String creatorName = "";//创建人姓名

        String cfbgfj = "";//出访报告附件

        String cjsj = "";//创建时间
        String sourceSys = "COS";//来源系统

        Property[] properties = requestInfo.getMainTableInfo().getProperty();
        for(int i = 0;i<properties.length;i++) {
            String name = properties[i].getName();
            String value = properties[i].getValue();

            if (name.trim().equals("bt1")) {
                bt1 = value.trim();
                continue;
            }

            if (name.trim().equals("tcmc")) {
                tcmc = value.trim();
                continue;
            }

            if (name.trim().equals("xglc")) {
                xglc = value.trim();
                continue;
            }

            if (name.trim().equals("tzmc")) {
                tzmc = value.trim();
                continue;
            }

            if (name.trim().equals("cfrw")) {
                cfrw = value.trim();
                continue;
            }

            if (name.trim().equals("cfrs")) {
                cfrs = value.trim();
                continue;
            }

            if (name.trim().equals("sjcfrs")) {
                sjcfrs = value.trim();
                continue;
            }

            if (name.trim().equals("cfgjdx")) {
                cfgjdx = value.trim();
                continue;
            }

            if (name.trim().equals("sjcfgjdx")) {
                sjcfgjdx = value.trim();
                continue;
            }

            if (name.trim().equals("cflx")) {
                cflx = value.trim();
                continue;
            }

            if (name.trim().equals("sjcflx")) {
                sjcflx = value.trim();
                continue;
            }

            if (name.trim().equals("cfksrq")) {
                cfksrq = value.trim();
                continue;
            }

            if (name.trim().equals("cfjsrq")) {
                cfjsrq = value.trim();
                continue;
            }

            if (name.trim().equals("sjcfksrq")) {
                sjcfksrq = value.trim();
                continue;
            }

            if (name.trim().equals("sjcfjsrq")) {
                sjcfjsrq = value.trim();
                continue;
            }

            if (name.trim().equals("bz")) {
                bz = value.trim();
                continue;
            }

            if (name.trim().equals("sqr")) {
                sqr = value.trim();
                continue;
            }

            if (name.trim().equals("cfbgfj")) {
                cfbgfj = value.trim();
                continue;
            }

            if (name.trim().equals("cjsj")) {
                cjsj = value.trim();
                continue;
            }
        }

        //主表单参数处理
        RecordSet rs = new RecordSet();

        //通过相关流程查询因公出国表主键、因公出国表批件号、团组ID
        writeLog("[YgcgcfbgToWsAction]相关流程xglc："+xglc);
        rs.execute("select zbwsxtcgrwsbid,jlhcpjh from formtable_main_176 where requestid = "+xglc);
        if(rs.next()){
            misstionId = rs.getString("zbwsxtcgrwsbid");
            misstionNo = rs.getString("jlhcpjh");
        }

        //处理出访国家
        writeLog("[YgcgcfbgToWsAction]出访国家cfgjdx："+cfgjdx);
        if(cfgjdx!=null&&!cfgjdx.trim().equals("")){
            rs.execute("select * from uf_cfgjxx where id in ("+cfgjdx+")");
            cfgjdx = "";
            for(int i=0;rs.next();i++){
                if(i!=0) {
                    cfgjdx += ",";
                }
                cfgjdx += rs.getString("cfgjdq");
            }
        }

        //处理实际出访国家
        writeLog("[YgcgcfbgToWsAction]实际出访国家sjcfgjdx："+sjcfgjdx);
        if(sjcfgjdx!=null&&!sjcfgjdx.trim().equals("")){
            rs.execute("select * from uf_cfgjxx where id in ("+sjcfgjdx+")");
            sjcfgjdx = "";
            for(int i=0;rs.next();i++){
                if(i!=0) {
                    sjcfgjdx += ",";
                }
                sjcfgjdx += rs.getString("cfgjdq");
            }
        }

        //处理创建人数据,获取创建人编号、邮箱、姓名
        writeLog("[YgcgcfbgToWsAction]创建人sqr："+sqr);
        if(sqr!=null&&!sqr.equals("")){
            rs.execute("select workcode,email,lastname from hrmresource where id="+sqr);
            if(rs.next()){
                creatorNo = rs.getString("workcode");
                creatorEmail = rs.getString("email");
                creatorName = rs.getString("lastname");
            }
        }

        //处理创建时间
        writeLog("[YgcgcfbgToWsAction]创建时间cjsj："+cjsj);
        if(cjsj!=null&&!cjsj.trim().equals("")) {
            cjsj += " 00:00:00";
        }


        //构建参数，调用接口
        //构建接口头部参数
        JSONObject paramHead = new JSONObject();
        paramHead.put("clientCode", "DFTC_COS");
        paramHead.put("reqSerialNo", UUID.randomUUID().toString());
        paramHead.put("tradeCode", "DFG_FAM_004");
        paramHead.put("tradeDescription", "因公出国出访报告调用出访报告接口");
        paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
        paramHead.put("version", "1.0");

        //构建接口内容参数
        JSONObject paramBody = new JSONObject();
        paramBody.put("GUID", guid);
        paramBody.put("DocSubject", bt1);
        paramBody.put("GroupLeaderName", tzmc);
        paramBody.put("MisstionId", misstionId);
        paramBody.put("MisstionNo", misstionNo);
        paramBody.put("GroupId", xglc);
        paramBody.put("GroupName", tzmc);
        paramBody.put("TripTask", cfrw);
        paramBody.put("PersonNum", cfrs);
        paramBody.put("PersonNumReal", sjcfrs);
        paramBody.put("TripNation", cfgjdx);
        paramBody.put("TripNationReal", sjcfgjdx);
        paramBody.put("TripRoute", cflx);
        paramBody.put("TripRouteReal", sjcflx);
        paramBody.put("TripStartDate", cfksrq);
        paramBody.put("TripEndDate", cfjsrq);
        paramBody.put("TripStartDateReal", sjcfksrq);
        paramBody.put("TripEndDateReal",sjcfjsrq);
        paramBody.put("Remark",bz);
        paramBody.put("CreatorNo",creatorNo);
        paramBody.put("CreatorEmail",creatorEmail);
        paramBody.put("CreatorName",creatorName);
        paramBody.put("CreateTime",cjsj);
        paramBody.put("SourceSys", "COS");

        JSONArray attDetail = new JSONArray();//出访报告附件明细参数

        //出国行程附件明细参数
        if (cfbgfj != null && !cfbgfj.trim().equals("")) {
            String[] cfbgfjIds = cfbgfj.split(",");
            for (int i = 0; i < cfbgfjIds.length; i++) {
                if (!cfbgfjIds[i].trim().equals("")) {
                    JSONObject fileJson = getFileJson(cfbgfjIds[i].trim());
                    if (fileJson != null) {
                        attDetail.add(fileJson);
                    }
                }
            }
        }
        paramBody.put("AttDetail", attDetail);


        writeLog("[YgcgcfbgToWsAction]开始调用因公出国出访报告接口");
        writeLog("[YgcgcfbgToWsAction]paramHead=" + paramHead);
        writeLog("[YgcgcfbgToWsAction]paramBody=" + paramBody);
        String result = null;
        try {
            result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("[YgcgcfbgToWsAction]调用因公出国出访报告接口时出现异常：" + e.getMessage());
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "调用外事因公出国出访报告接口返回异常: " + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }
        writeLog("[YgcgcfbgToWsAction]result=" + result);
        writeLog("[YgcgcfbgToWsAction]因公出国出访报告接口调用完毕");

        try {
            writeLog("[YgcgcfbgToWsAction]开始解析因公出国出访报告接口的调用结果");
            JSONArray tzzjResultJson = JSONArray.parseArray(result);
            writeLog("[YgcgcfbgToWsAction]解析因公出国出访报告接口的调用结果完成");

            if (!tzzjResultJson.getJSONObject(0).getString("status").trim().equals("1")) {
                //接口返回的状态表示调用失败，则阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用外事接口返回失败: " + tzzjResultJson.getJSONObject(0).getString("message"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }

        }catch(Exception e){
            e.printStackTrace();
            writeLog("[YgcgcfbgToWsAction]解析因公出国出访报告接口的调用结果出现异常："+e.getMessage());
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "调用外事因公出国出访报告接口返回异常: " + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }

        return Action.SUCCESS;
    }

    /**
     * 通过附件的docId生成对应外事接口所需的JSON对象
     * @param docId
     * @return
     */
    private JSONObject getFileJson(String docId){
        JSONObject fileJson = null;
        RecordSet rs = new RecordSet();
        try {
            rs
                    .execute("select docimagefile.imagefileid as imagefileid,filerealpath,imagefiletype,imagefile.imagefilename,iszip,fileSize from imagefile,docimagefile "
                            + " where imagefile.imagefileid =docimagefile.imagefileid and docimagefile.docid=" + docId);

            if (rs.next()) {
                fileJson = new JSONObject();
                String filerealpath = rs.getString("filerealpath");
                String imagefileid = rs.getString("imagefileid");
                String imagefilename = rs.getString("imagefilename");
                writeLog("[YGcgjdtzToWsAction]当前附件imagefilename为："+imagefilename);
                int fileSize = rs.getInt("fileSize");

                fileJson.put("Name", imagefilename.substring(0, imagefilename.lastIndexOf(".")));
                writeLog("[YGcgjdtzToWsAction]处理的附件Name为："+fileJson.getString("Name"));
                fileJson.put("Type", imagefilename.substring(imagefilename.lastIndexOf(".")+1));
                writeLog("[YGcgjdtzToWsAction]处理的附件Type为："+fileJson.getString("Type"));

                int iszip = rs.getInt("iszip");

                writeLog("iszip==>" + iszip + ",filerealpath=====>" + filerealpath);
                ZipFile zf = null;
                ZipInputStream zin = null;
                ZipEntry ze = null;
                InputStream is = null;

                byte[] b = null;

                try {
                    if (1 == iszip) {
                        writeLog("压缩文件");
                        zf = new ZipFile(filerealpath);
                        zin = new ZipInputStream(new BufferedInputStream(
                                new FileInputStream(filerealpath)));
                        ze = zin.getNextEntry();
                        is = zf.getInputStream(ze);
                    } else {
                        writeLog("非压缩文件");
                        ImageFileManager imageFileManager = new ImageFileManager();
                        imageFileManager.getImageFileInfoById(Util.getIntValue(
                                imagefileid, 0));
                        is = imageFileManager.getInputStream();
                    }

                    b = new byte[fileSize];
                    is.read(b, 0, b.length);

                    String encodeFileStr = Base64.getEncoder().encodeToString(b);

                    fileJson.put("Value", encodeFileStr);

                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("[YGcgjdtzToWsAction]处理附件（" + docId + "）JSON出现异常：" + e.getMessage());
                    return null;
                } finally {
                    b = null;
                    zf = null;
                    if (zin != null) {
                        try {
                            zin.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            zin = null;
                        }
                    }
                    ze = null;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            is = null;
                        }
                    }
                }
            }

            return fileJson;
        }catch(Exception e){
            e.printStackTrace();
            writeLog("[YGcgjdtzToWsAction]处理附件（" + docId + "）JSON出现异常：" + e.getMessage());
            return null;
        }finally {
            fileJson = null;
            rs = null;
        }

    }
}
