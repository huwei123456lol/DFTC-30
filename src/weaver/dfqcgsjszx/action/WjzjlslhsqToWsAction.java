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
 * 外籍专家临时来华申请调用外事接口
 * @author Alex.Du
 */
public class WjzjlslhsqToWsAction extends BaseBean implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        final String tableName = "formtable_main_180";

        //获取主表参数
        String guid = requestInfo.getRequestid();//GUID，使用当前请求的RequestId
        String jjcd = "";//紧急程度
        String bt = "";//标题
        String sydwzwmc = "";//受邀单位中文名称
        String sydwywmc = "";//受邀单位英文名称
        String yqmd = "";//邀请目的
        String slg = "";//使领馆
        String nrjrq = "";//拟入境日期(入境日期)
        String fwdd = "";//访问地点
        String byqrlxdh = "";//被邀请人联系电话(受邀人电话)
        String cz = "";//传真(受邀人传真)
        String yxq = "";//签证有效期(有效期)
        String qzyxcs = "";//签证有效次数(有效次数)
        String mctl = "";//停留天数
        String lxfs = "";//联系方式(联系电话)

        String sqr = "";//创建人
        String creatorNo = "";//创建人编号
        String creatorEmail = "";//创建人邮箱
        String creatorName = "";//创建人姓名

        String sqrq = "";//创建时间(申请日期)
        String sourceSys = "COS";//来源系统
        String xgfj = "";//相关附件

        Property[] properties = requestInfo.getMainTableInfo().getProperty();
        for(int i = 0;i<properties.length;i++) {
            String name = properties[i].getName();
            String value = properties[i].getValue();

            if (name.trim().equals("jjcd")) {
                jjcd = value.trim();
                continue;
            }

            if (name.trim().equals("bt")) {
                bt = value.trim();
                continue;
            }

            if (name.trim().equals("sydwzwmc")) {
                sydwzwmc = value.trim();
                continue;
            }

            if (name.trim().equals("sydwywmc")) {
                sydwywmc = value.trim();
                continue;
            }

            if (name.trim().equals("yqmdn")) {
                yqmd = value.trim();
                continue;
            }

            if (name.trim().equals("slg")) {
                slg = value.trim();
                continue;
            }

            if (name.trim().equals("nrjrq")) {
                nrjrq = value.trim();
                continue;
            }

            if (name.trim().equals("fwdd")) {
                fwdd = value.trim();
                continue;
            }

            if (name.trim().equals("byqrlxdh")) {
                byqrlxdh = value.trim();
                continue;
            }

            if (name.trim().equals("cz")) {
                cz = value.trim();
                continue;
            }

            if (name.trim().equals("yxq")) {
                yxq = value.trim();
                continue;
            }

            if (name.trim().equals("qzyxcs")) {
                qzyxcs = value.trim();
                continue;
            }

            if (name.trim().equals("mctl")) {
                mctl = value.trim();
                continue;
            }

            if (name.trim().equals("lxfs")) {
                lxfs = value.trim();
                continue;
            }

            if (name.trim().equals("sqr")) {
                sqr = value.trim();
                continue;
            }

            if (name.trim().equals("sqrq")) {
                sqrq  = value.trim();
                continue;
            }

            if (name.trim().equals("xgfj")) {
                xgfj  = value.trim();
                continue;
            }
        }



        //主表单参数处理
        RecordSet rs = new RecordSet();

        //处理使领馆
        writeLog("[WjzjlslhsqToWsAction]使领馆slg："+slg);
        if(slg!=null&&!slg.trim().equals("")){
            rs.execute("select FDNAME from uf_slg where id in ("+slg+")");
            slg = "";
            for(int i=0;rs.next();i++){
                if(i!=0) {
                    slg += ",";
                }
                slg += rs.getString("FDNAME");
            }
        }

        //处理邀请目的
        writeLog("[WjzjlslhsqToWsAction]邀请目的："+yqmd);
        if(yqmd!=null&&!yqmd.trim().equals("")){
            rs.execute("select fdreasoncn from uf_yqmd where id = "+yqmd);
            yqmd = "";
            for(int i=0;rs.next();i++){
                if(i!=0) {
                    yqmd += ",";
                }
                yqmd += rs.getString("fdreasoncn");
            }
        }

        //处理创建人数据,获取创建人编号、邮箱、姓名
        writeLog("[WjzjlslhsqToWsAction]创建人sqr："+sqr);
        if(sqr!=null&&!sqr.equals("")){
            rs.execute("select workcode,email,lastname from hrmresource where id="+sqr);
            if(rs.next()){
                creatorNo = rs.getString("workcode");
                creatorEmail = rs.getString("email");
                creatorName = rs.getString("lastname");
            }
        }

        JSONObject paramHead = new JSONObject();
        JSONObject paramBody = new JSONObject();

        JSONArray invitaterDetail = new JSONArray();//受邀人明细参数
        JSONArray scheduleDetail = new JSONArray();//在华行程明细参数


        //构建接口头部参数
        paramHead.put("clientCode", "DFTC_COS");
        paramHead.put("reqSerialNo", UUID.randomUUID().toString());
        paramHead.put("tradeCode", "DFG_FAM_005");
        paramHead.put("tradeDescription", "外籍专家临时来华申请调用团组组建接口");
        paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
        paramHead.put("version", "1.0");

        //构建接口内容参数
        paramBody.put("GUID", guid);
        paramBody.put("Emergency", jjcd);
        paramBody.put("DocSubject", bt);
        paramBody.put("UnitCName", sydwzwmc);
        paramBody.put("UnitEName", sydwywmc);
        paramBody.put("Purpose", yqmd);
        paramBody.put("DeclareInNation", slg);
        paramBody.put("EntryDate", nrjrq);
        paramBody.put("VisitPlace", fwdd);
        paramBody.put("InviterPhone", byqrlxdh);
        paramBody.put("InviterFax", cz);
        paramBody.put("AccessDate", yxq);
        paramBody.put("AccessNum", qzyxcs);
        paramBody.put("StayDays", mctl);
        paramBody.put("Tel", lxfs);
        paramBody.put("CreatorEmail", creatorEmail);
        paramBody.put("CreatorNo", creatorNo);
        paramBody.put("CreatorName", creatorName);
        paramBody.put("CreateTime", sqrq+" 00:00:00");
        paramBody.put("SourceSys", sourceSys);

        //获取子表单参数（受邀人）
        rs.execute("select * from "+tableName+"_dt1 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("[WjzjlslhsqToWsAction]获取受邀人子表单参数SQL为：select * from "+tableName+"_dt1 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("[WjzjlslhsqToWsAction]共查询到数据"+rs.getCounts()+"条");

        while(rs.next()){
            JSONObject syrJSONObject = new JSONObject();
            //明细表GUID，使用明细表ID字段
            syrJSONObject.put("GUID",rs.getString("id").trim());

            //姓
            syrJSONObject.put("FirstName",rs.getString("x").trim());

            //名
            syrJSONObject.put("LastName",rs.getString("m").trim());

            //性别
            syrJSONObject.put("Sex",rs.getString("xbn").trim());

            //生日
            syrJSONObject.put("Birthday",rs.getString("csnyrn").trim());

            //职务
            syrJSONObject.put("Duty",rs.getString("zw").trim());

            //国籍
            syrJSONObject.put("Nationality",rs.getString("gj").trim());

            //护照号
            syrJSONObject.put("PassportNo",rs.getString("hzhm").trim());

            //邀请来访ID
            syrJSONObject.put("InvitationId",guid);

            invitaterDetail.add(syrJSONObject);
            syrJSONObject = null;
        }

        paramBody.put("InvitaterDetail", invitaterDetail);

        writeLog("[WjzjlslhsqToWsAction]处理后的invitaterDetail.size()="+invitaterDetail.size());


        //获取子表单参数（在华行程）
        rs.execute("select * from "+tableName+"_dt2 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("[WjzjlslhsqToWsAction]获取在华行程子表单参数SQL为：select * from "+tableName+"_dt2 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("[WjzjlslhsqToWsAction]共查询到数据"+rs.getCounts()+"条");

        while(rs.next()){
            JSONObject zhxcJSONObject = new JSONObject();
            //明细表GUID，使用明细表ID字段
            zhxcJSONObject.put("GUID",rs.getString("id").trim());

            //时间
            zhxcJSONObject.put("Time",rs.getString("sj").trim());

            //地点
            zhxcJSONObject.put("Place",rs.getString("dd").trim());

            //行程
            zhxcJSONObject.put("Schedule",rs.getString("xc").trim());

            //邀请来访ID
            zhxcJSONObject.put("InvitationId",guid);

            scheduleDetail.add(zhxcJSONObject);
            zhxcJSONObject = null;
        }

        paramBody.put("ScheduleDetail", scheduleDetail);

        writeLog("[WjzjlslhsqToWsAction]处理后的scheduleDetail.size()="+scheduleDetail.size());

        JSONArray attach = new JSONArray();//出访报告附件明细参数

        //相关附件明细参数
        if (xgfj != null && !xgfj.trim().equals("")) {
            String[] xgfjIds = xgfj.split(",");
            for (int i = 0; i < xgfjIds.length; i++) {
                if (!xgfjIds[i].trim().equals("")) {
                    JSONObject fileJson = getFileJson(xgfjIds[i].trim());
                    if (fileJson != null) {
                        attach.add(fileJson);
                    }
                }
            }
        }
        paramBody.put("Attach", attach);

        writeLog("[WjzjlslhsqToWsAction]开始调用外籍专家临时来华申请接口");
        writeLog("[WjzjlslhsqToWsAction]paramHead=" + paramHead);
        writeLog("[WjzjlslhsqToWsAction]paramBody=" + paramBody);
        String result = null;
        try {
            result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("[WjzjlslhsqToWsAction]调用外籍专家临时来华申请接口时出现异常：" + e.getMessage());
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "调用外事外籍专家临时来华申请接口返回异常: " + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }
        writeLog("[WjzjlslhsqToWsAction]result=" + result);
        writeLog("[WjzjlslhsqToWsAction]外籍专家临时来华申请接口调用完毕");

        try {
            writeLog("[WjzjlslhsqToWsAction]开始解析外籍专家临时来华申请接口的调用结果");
            JSONArray resultJson = JSONArray.parseArray(result);
            writeLog("[WjzjlslhsqToWsAction]解析外籍专家临时来华申请接口的调用结果完成");

            if (!resultJson.getJSONObject(0).getString("status").trim().equals("1")) {
                //接口返回的状态表示调用失败，则阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用外事外籍专家临时来华申请接口返回失败: " + resultJson.getJSONObject(0).getString("message"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }

        }catch(Exception e){
            e.printStackTrace();
            writeLog("[WjzjlslhsqToWsAction]解析外籍专家临时来华申请接口的调用结果出现异常："+e.getMessage());
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "调用外事外籍专家临时来华申请接口返回异常: " + e.getMessage());// 提醒信息内容
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
            rs.execute("select docimagefile.imagefileid as imagefileid,filerealpath,imagefiletype,imagefile.imagefilename,iszip,fileSize from imagefile,docimagefile "
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
