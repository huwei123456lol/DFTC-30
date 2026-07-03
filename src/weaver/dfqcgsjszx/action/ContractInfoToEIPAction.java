package weaver.dfqcgsjszx.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.general.TimeUtil;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.EIP.ht_service_client.ISendMessageServiceProxy;
import weaver.file.ImageFileManager;
import weaver.general.Base64;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 合同信息推送到EIP
 * @author Alex.Du
 */
public class ContractInfoToEIPAction extends BaseBean implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        writeLog("[ContractInfoToEIPAction]开始执行合同信息推送到EIP");
        //获取表单数据
        String htbh = "";//合同编号（合同编号）
        String bg3bm = "";//采购需求申请部门顶级部门（签约部门编号）
        String qybmmc = "";//（签约部门名称）
        String sqlx = "";//申请类型(合同状态)
        String sjje = "";//(审计金额)
        String htjehsy = "";//合同金额(合同总金额)
        StringBuffer cgxmmc = new StringBuffer();//采购项目名称(项目名称)，取明细表数据，用逗号分隔
        StringBuffer xmbhtzjhh = new StringBuffer();//项目编号/投资计划号(项目编号)，取明细表数据，用逗号分隔
        String htnrjfjnr = "";//合同内容及附件内容（合同内容）
        String dfqydwmc = "";//对方签约单位名称（相对方）
        String htwd = "";//合同文档(合同正文)
        String wjlx = "";//（文件类型）
        String htlb = "";//合同类别（合同类别）
        String cgxqzjqd = "";//采购需求资金渠道（采购资金渠道）
        String sfyzbj = "";//是否有质保金（是否有质保金）
        String mfdw = ""; //卖方单位 如果有就拼接到对方签约单位名称
        String sfsw = ""; //是否涉外
        //获取主表单数据
        Property[] properties = requestInfo.getMainTableInfo().getProperty();
        for (int i = 0; i < properties.length; i++) {
            String name = properties[i].getName().trim();
            String value = properties[i].getValue().trim();

            if (name.equals("htbh")) {
                htbh = value;
                continue;
            }

            if (name.equals("bg3bm")) {
                bg3bm = value;
                continue;
            }

            if (name.equals("sqlx")) {
                sqlx = value;
                continue;
            }

            if (name.equals("htjehsy")) {
                htjehsy = value;
                continue;
            }

            if (name.equals("htnrjfjnr")) {
                htnrjfjnr = value;
                continue;
            }

            if (name.equals("dfqydwmc")) {
                dfqydwmc = value;
                continue;
            }

            if (name.equals("htwd")) {
                htwd = value;
                continue;
            }

            if (name.equals("htlb")) {
                htlb = value;
                continue;
            }

            if (name.equals("cgxqzjqd")) {
                cgxqzjqd = value;
                continue;
            }

            if (name.equals("sfyzbj")) {
                sfyzbj = value;
                continue;
            }
            if (name.equals("mfdw")) {
                mfdw = value;
                continue;
            }
            if (name.equals("sfsw")) {
                sfsw = value;
            }
        }

        if(!mfdw.equals("")){
            dfqydwmc = dfqydwmc + ',' + mfdw;
        }

        //处理主表单数据
        RecordSet rs = new RecordSet();
        rs.execute("select departmentname,departmentcode from hrmdepartment where id='"+bg3bm+"'");
        if(rs.next()){
            bg3bm = rs.getString("departmentcode");
            qybmmc = rs.getString("departmentname");
        }
    //文件读取操作
        rs.execute("select imagefileid,imagefilename,filerealpath,fileSize,iszip from imagefile where imagefileid=(select imagefileid from docimagefile where docid='" + htwd + "')");
        if (rs.next()) {
            String imageFileName = rs.getString("imagefilename");

            wjlx = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);

            String fileRealPath = rs.getString("filerealpath");

            List<Byte> list = new ArrayList<Byte>();
            ZipFile zf = null;
            ZipInputStream zin = null;
            ZipEntry ze = null;
            InputStream is = null;
            byte[] b = new byte[1024];
            byte[] b2 = null;
            int len = 0;
            try {
                // 判断当前文件是否是压缩文件
                if (1 == rs.getInt("iszip")) {
                    // 如果是压缩文件，获取压缩包内的文件流
                    writeLog("压缩文件");
                    zf = new ZipFile(rs.getString("filerealpath"));
                    zin = new ZipInputStream(new BufferedInputStream(
                            new FileInputStream(rs.getString("filerealpath"))));
                    ze = zin.getNextEntry();
                    is = zf.getInputStream(ze);
                } else {
                    // 非压缩文件，获取该文件的文件流
                    writeLog("非压缩文件");
                    ImageFileManager imageFileManager = new ImageFileManager();
                    imageFileManager.getImageFileInfoById(Util.getIntValue(
                            rs.getString("imagefileid"), 0));
                    is = imageFileManager.getInputStream();
                }

                while (-1 != (len = is.read(b, 0, b.length))) {
                    for (int i = 0; i < len; i++) {
                        list.add(b[i]);
                    }
                }

                b2 = new byte[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    b2[i] = list.get(i);
                }

                htwd = new String(Base64.encode(b2));
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("[ContractInfoToEIPAction]获取合同文档时出现异常：" + e.getMessage());
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "获取合同文档时出现异常：" + e.getMessage());// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            } finally {
                // 释放对象
                if (is != null) {
                    try {
                        is.close();
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
                ze = null;
                zin = null;
                zf = null;
                list = null;
                b2 = null;
            }

        }

        //获取子表单数据
        Row[] rows = requestInfo.getDetailTableInfo().getDetailTable(0).getRow();
        for (int i = 0; i < rows.length; i++) {
            Cell[] cells = rows[i].getCell();
            for (int j = 0; j < cells.length; j++) {
                String name = cells[j].getName().trim();
                String value = cells[j].getValue().trim();

                if (name.equals("cgxmmc")) {
                    if (cgxmmc.length() > 1) {
                        cgxmmc.append(",");
                    }
                    cgxmmc.append(value);
                    continue;
                }

                if (name.equals("xmbhtzjhh")) {
                    if (xmbhtzjhh.length() > 1) {
                        xmbhtzjhh.append(",");
                    }
                    xmbhtzjhh.append(value);
                    continue;
                }
            }
        }

        //调用接口将合同数据传送给EIP
        //构建参数，调用接口
        //构建接口头部参数
        JSONObject paramHead = new JSONObject();
        paramHead.put("clientCode", "DFTC_COS");
        paramHead.put("reqSerialNo", UUID.randomUUID().toString());
        paramHead.put("tradeCode", "DFG_EIP_014");
        paramHead.put("tradeDescription", "COS合同信息推送到EIP");
        paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
        paramHead.put("version", "1.0");

        //构建接口内容参数
        JSONObject paramData = new JSONObject();
        paramData.put("HTBH", htbh);
        paramData.put("QYBMBH", bg3bm);
        paramData.put("QYBMZT", qybmmc);
        paramData.put("HTZT", sqlx);
        paramData.put("SJJE", "");
        paramData.put("HTZJE", htjehsy);
        paramData.put("XMMC", cgxmmc.toString());
        paramData.put("XMBH", xmbhtzjhh.toString());
        paramData.put("HTNR", htnrjfjnr);
        paramData.put("XDF", dfqydwmc);
        paramData.put("HTWBFJ", htwd);
        paramData.put("WJLX", wjlx);
        paramData.put("HTLB", htlb);
        paramData.put("CGZJQD", cgxqzjqd);
        paramData.put("SFYZBJ", sfyzbj);
        paramData.put("SFSW", sfsw);


        //封装数据格式
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(paramData);

        JSONObject paramBody = new JSONObject();
        paramBody.put("DATA", jsonArray);

        writeLog("[ContractInfoToEIPAction]paramBody：" + paramBody.toJSONString());

        String result = null;
        try {
            result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("[ContractInfoToEIPAction]调用合同信息接口时出现异常：" + e.getMessage());
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "调用合同信息接口时出现异常: " + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }

        writeLog("[ContractInfoToEIPAction]result=" + result);

        try {
            JSONObject resultJson = JSONObject.parseObject(result);

            if (resultJson.getString("STATUS").trim().equals("1")) {
                //接口返回的状态表示调用失败，则阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用合同信息接口返回失败: " + resultJson.getString("MSG"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("[ContractInfoToEIPAction]解析合同信息接口的调用结果出现异常：" + e.getMessage());
            requestInfo.getRequestManager().setMessageid(
                    requestInfo.getRequestid() + "-"
                            + TimeUtil.getCurrentTimeString());// 提醒信息id
            requestInfo.getRequestManager().setMessagecontent(
                    "解析合同信息接口的调用结果出现异常: " + e.getMessage());// 提醒信息内容
            return Action.FAILURE_AND_CONTINUE;
        }

        paramHead = null;
        paramData = null;
        rs = null;

        writeLog("[ContractInfoToEIPAction]执行合同信息推送到EIP结束");

        return Action.SUCCESS;
    }
}
