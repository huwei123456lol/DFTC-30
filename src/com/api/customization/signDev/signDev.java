package com.api.customization.signDev;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.engine.workflow.constant.menu.SystemMenuType;
import com.engine.workflow.entity.operationMenu.ConsultationEntity;
import com.engine.workflow.entity.operationMenu.ForwardEntity;
import com.engine.workflow.entity.operationMenu.OperationMenuEntity;
import com.engine.workflow.entity.operationMenu.RejectEntity;
import com.engine.workflow.entity.system.SystemMenuEntity;
import com.engine.workflow.util.ListUtil;
import com.engine.workflow.util.MenuOrderSetUtil;
import com.engine.workflow.util.ProxyUtil;
import com.engine.workflow.util.WorkflowOvertimeSettingsUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.workflow.mode.FieldInfo;
import weaver.workflow.workflow.WFNodeFieldManager;
import weaver.workflow.workflow.WorkflowNodeMenuComInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/signDev")
public class signDev {
    int requestid;
    int isprint;
    int modeid = 0;
    int wfid;
    int ismobile;
    int nodeid;
    int stnull = 0;
    int showType = 0;
    String remarkTypeSqlWhere = "";
    @POST
    @Path("/getSignInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSignInfo(@Context HttpServletRequest request, @Context HttpServletResponse response) throws JSONException {
        JSONObject result = new JSONObject();

        new BaseBean().writeLog("[signDev] ==== start =====");
        this.requestid = Util.getIntValue(request.getParameter("requestid"), 0);
        this.isprint = Util.getIntValue(request.getParameter("isprint"), 0);
        this.wfid = Util.getIntValue(request.getParameter("wfid"), 0);
        this.ismobile = Util.getIntValue(request.getParameter("ismobile"), 0);
        this.nodeid = Util.getIntValue(request.getParameter("nodeid"), 0);
        new BaseBean().writeLog("[signDev] ==== param =====");
        new BaseBean().writeLog("[signDev] ==== requestid " + requestid + " =====");
        new BaseBean().writeLog("[signDev] ==== isprint " + isprint + " =====");
        new BaseBean().writeLog("[signDev] ==== wfid " + wfid + " =====");
        new BaseBean().writeLog("[signDev] ==== ismobile " + ismobile + " =====");
        new BaseBean().writeLog("[signDev] ==== nodeid " + nodeid + " =====");
        //获取用户
        User user = HrmUserVarify.getUser(request, response);





        //处理签字意见显示类型 根据当前打开签字意见的节点
        RecordSet rsRemarkType = new RecordSet();  //定义显示类型的 SQL 条件字符串
        rsRemarkType.execute("select PRINTVIEWTYPE,vtChuanyue,vtChuanyueRec,vttakforward,vttakend,stnull,showtype from workflow_flownode where nodeid = " + nodeid);
        if (rsRemarkType.next()) {
            String viewTypeStr = rsRemarkType.getString("PRINTVIEWTYPE");
            int vtChuanyue = Util.getIntValue(rsRemarkType.getString("vtChuanyue"), 0);
            int vtChuanyueRec = Util.getIntValue(rsRemarkType.getString("vtChuanyueRec"), 0);
            int vttakforward = Util.getIntValue(rsRemarkType.getString("vttakforward"), 0);
            int vttakend = Util.getIntValue(rsRemarkType.getString("vttakend"), 0);
            this.stnull = Util.getIntValue(rsRemarkType.getString("stnull"), 0); //是否显示空意见
            this.showType = Util.getIntValue(rsRemarkType.getString("showtype"), 0); //是否显示最后一次意见
            remarkTypeSqlWhere = getRemarkTypeSqlWhere(viewTypeStr, vtChuanyue, vtChuanyueRec, vttakforward, vttakend);
        }




        RecordSet rss = new RecordSet();
        rss.execute("select * from workflow_nodehtmllayout where WORKFLOWID=" + wfid + " and nodeid= " + nodeid  +  (isprint == 0? "and ISACTIVE=1" : "")  +" and type = " + (this.ismobile == 0 ? isprint : 2));
        if (rss.next()) {
            this.modeid = rss.getInt("id");
            new BaseBean().writeLog("[signDev] ==== modeid " + modeid + " =====");
        }

        //表单字段 参考FieldInfo.class 用于原生签字意见获取方法
        FieldInfo signFieldInfo;
        try {
            signFieldInfo = ProxyUtil.getInstance().getProxyInstance(new FieldInfo());
            signFieldInfo.setRequestid(requestid);
            signFieldInfo.setUser(user);
            signFieldInfo.setIsprint(isprint);
            signFieldInfo.setPrintModeId(modeid);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("errInfo", "表单字段");
            result.put("errMsg", e.getMessage());
            return result.toJSONString();
        }

        String layoutJsonStr = ""; //用户存放表单模板JSON
        try {
            RecordSet rs = new RecordSet();
            //通过modeid获取当前的html模板
            rs.executeQuery("select version,syspath,datajson from workflow_nodehtmllayout where id=?", modeid);
            if (rs.next()) {
                int version = Util.getIntValue(rs.getString("version"), 0);
                if (version == 2) {
                    layoutJsonStr = Util.null2String(rs.getString("datajson"));
                } else {
                    //应该是兼容性代码 读取老HTML末班
                    WFNodeFieldManager var5 = new WFNodeFieldManager();
                    layoutJsonStr = var5.readHtmlFile(Util.null2String(rs.getString("syspath")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("errInfo", "通过modeid获取当前的html模板");
            result.put("errMsg", e.getMessage());
            return result.toJSONString();
        }

        try {
            JSONObject layoutJson = JSONObject.parseObject(layoutJsonStr);
            JSONObject emaintable = layoutJson.getJSONObject("eformdesign").getJSONObject("etables").getJSONObject("emaintable");
            JSONArray ec = emaintable.getJSONArray("ec"); //获取单元格配置信息
            ArrayList<JSONObject> dataList = new ArrayList<>();
            for (int i = 0; i < ec.size(); i++) {
                JSONObject tdInfo = ec.getJSONObject(i);
                //获取独立单元格签字意见数据 etype为5的数据
                JSONObject signData = new JSONObject();
                if ("5".equals(tdInfo.getString("etype"))) {
                    //获取nodeid  "field": "57",
                    int nodeid = Integer.parseInt(tdInfo.getString("field"));
                    String rowid = tdInfo.getString("id").split(",")[0];
                    String cellid = tdInfo.getString("id").split(",")[1];
                    //原生方法remark
                    String remarkStr;
                    remarkStr = signFieldInfo.GetNodeRemark(wfid, nodeid, nodeid, 2, null);
                    signData.put("remarkStr", remarkStr);
                    //查询签字意见
                    ArrayList<JSONObject> remarkJoData = getNewSignData(nodeid);
                    signData.put("remarkJoData", remarkJoData);
                    signData.put("tdInfo", "main_" + rowid + "_" + cellid);
                    signData.put("nodeid", nodeid);
                    signData.put("stnull", stnull);
                    signData.put("showType", showType);
                    dataList.add(signData);
                } else if ("13".equals(tdInfo.getString("etype"))) {
                    String mcLabel = tdInfo.getString("mcpoint");
                    JSONObject mctable = layoutJson.getJSONObject("eformdesign").getJSONObject("etables").getJSONObject(mcLabel);
                    JSONArray mcec = mctable.getJSONArray("ec"); //获取单元格配置信息
                    for (int j = 0; j < mcec.size(); j++) {
                        JSONObject mcsignData = new JSONObject();
                        JSONObject mctdInfo = mcec.getJSONObject(j);
                        //获取独立单元格签字意见数据 etype为5的数据
                        if ("5".equals(mctdInfo.getString("etype"))) {
                            //获取nodeid  "field": "57",
                            int mcnodeid = Integer.parseInt(mctdInfo.getString("field"));
                            String mcrowid = mctdInfo.getString("id").split(",")[0];
                            String mccellid = mctdInfo.getString("id").split(",")[1];
                            //原生方法remark
                            String mcremarkStr;
                            mcremarkStr = signFieldInfo.GetNodeRemark(wfid, mcnodeid, mcnodeid, 2, null);
                            mcsignData.put("remarkStr", mcremarkStr);
                            //查询签字意见
                            ArrayList<JSONObject> mcremarkJoData = getNewSignData(mcnodeid);
                            mcsignData.put("remarkJoData", mcremarkJoData);
                            mcsignData.put("tdInfo", mcLabel + "_" + mcrowid + "_" + mccellid);
                            mcsignData.put("nodeid", mcnodeid);
                            mcsignData.put("stnull", stnull);
                            mcsignData.put("showType", showType);
                            dataList.add(mcsignData);
                        }
                    }
                }
            }
            result.put("data", JSON.parseArray(JSONObject.toJSONString(dataList)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("errInfo", "获取签字意见失败");
            result.put("errMsg", e.getMessage());
            return result.toJSONString();
        }
        return result.toJSONString();
    }

    private String getRemarkTypeSqlWhere( String viewTypeStr, int vtChuanyue, int vtChuanyueRec, int vttakforward, int vttakend) {
        String remarkTypeSqlWhere = "";
        int allType = 0;
        int viewtype_approve = 0;
        int viewtype_realize = 0;
        int viewtype_forward = 0;
        int view_handleForward = 0;
        int view_takingOpinions = 0;
        int viewtype_postil = 0;
        int viewtype_tpostil = 0;
        int viewtype_recipient = 0;
        int viewtype_rpostil = 0;
        int viewtype_reject = 0;
        int viewtype_superintend = 0;
        int viewtype_over = 0;
        int viewtype_intervenor = 0;
        int viewtype_withdraw = 0;
        if (!viewTypeStr.contains("viewtype_all") && !viewTypeStr.equals("1") && !"all".equals(viewTypeStr)) {
            if (viewTypeStr.contains("viewtype_approve")) {
                viewtype_approve = 1;
            }
            if (viewTypeStr.contains("viewtype_realize")) {
                viewtype_realize = 1;
            }

            if (viewTypeStr.contains("viewtype_forward")) {
                viewtype_forward = 1;
            }

            if (viewTypeStr.contains("viewtype_postil")) {
                viewtype_postil = 1;
            }

            if (viewTypeStr.contains("view_handleForward")) {
                view_handleForward = 1;
            }

            if (viewTypeStr.contains("view_takingOpinions")) {
                view_takingOpinions = 1;
            }

            if (viewTypeStr.contains("viewtype_tpostil")) {
                viewtype_tpostil = 1;
            }

            if (viewTypeStr.contains("viewtype_recipient")) {
                viewtype_recipient = 1;
            }

            if (viewTypeStr.contains("viewtype_rpostil")) {
                viewtype_rpostil = 1;
            }

            if (viewTypeStr.contains("viewtype_reject")) {
                viewtype_reject = 1;
            }

            if (viewTypeStr.contains("viewtype_superintend")) {
                viewtype_superintend = 1;
            }

            if (viewTypeStr.contains("viewtype_over")) {
                viewtype_over = 1;
            }

            if (viewTypeStr.contains("viewtype_intervenor")) {
                viewtype_intervenor = 1;
            }

            if (viewTypeStr.contains("viewtype_withdraw")) {
                viewtype_withdraw = 1;
            }
        } else {
            allType = 1;
        }
        if (allType != 1) {
            if (viewtype_approve == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='2'";
            }

            if (viewtype_realize == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='0'";
            }

            if (viewtype_forward == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='7'";
            }

            if (view_takingOpinions == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='a'";
            }

            if (view_handleForward == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='h'";
            }

            if (viewtype_tpostil == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='b'";
            }

            if (viewtype_rpostil == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='9'";
            }

            if (viewtype_postil == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='9'";
            }

//            if (viewtype_recipient == 1) {
//                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='t'";
//            }

            if (viewtype_reject == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='3'";
            }

            if (viewtype_superintend == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='s'";
            }

            if (viewtype_over == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='e'";
            }

            if (viewtype_intervenor == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='i'";
            }

            if (vtChuanyue == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='c'";
            }

            if (vtChuanyueRec == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='y'";
            }

            if (vttakforward == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='z'";
            }

            if (vttakend == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='x'";
            }

            if (viewtype_withdraw == 1) {
                remarkTypeSqlWhere = remarkTypeSqlWhere + " or logtype='r'";
            }

            remarkTypeSqlWhere = remarkTypeSqlWhere.trim();
            if ("".equals(remarkTypeSqlWhere)) {
                remarkTypeSqlWhere = " 1=1";
            } else {
                remarkTypeSqlWhere = remarkTypeSqlWhere.substring(2);  //去除or字符
            }
        }
        return remarkTypeSqlWhere;
    }

    private ArrayList<JSONObject> getNewSignData(int nodeid) {
        //查询签字意见
        ArrayList<JSONObject> SignDataList = new ArrayList<>();

        RecordSet rsSign = new RecordSet();
        String sqlStr = "select a.logid," +
                "a.logtype,a.remark,a.operatedate,a.operatetime,a.operator,b.lastname ,b.seclevel,c.departmentname " +
                "from workflow_requestlog a " +
                "left join HrmResource b on a.operator =b.id " +
                "left join HrmDepartment c on a.operatorDept =c.id where 1=1 and a.logtype != '1' " +
                "and a.requestid = '" + this.requestid + "' " +
                "and a.nodeid = '" + nodeid + "' and ( " + remarkTypeSqlWhere +" ) order by a.operatedate desc,a.operatetime desc " ;
        new BaseBean().writeLog("[signDev] ==== sqlStr " + sqlStr + " =====");



        rsSign.execute(sqlStr);
        while (rsSign.next()) {
            JSONObject tempSign = new JSONObject();
            tempSign.put("nodeid",nodeid);
            tempSign.put("logtype", rsSign.getString("logtype"));
            tempSign.put("remark", rsSign.getString("remark"));
            tempSign.put("operatedate", rsSign.getString("operatedate"));
            tempSign.put("operatetime", rsSign.getString("operatetime"));
            tempSign.put("operator", rsSign.getString("operator"));
            tempSign.put("lastname", rsSign.getString("lastname"));
            tempSign.put("departmentname", rsSign.getString("departmentname"));
            tempSign.put("seclevel", rsSign.getString("seclevel"));
//            tempSign.put("lableEntries",getMenu(this.wfid,nodeid));
            int logtypeid = getSystemMenuType(rsSign.getString("logtype")).getId();
            String loginstruction = getSystemMenuType(rsSign.getString("logtype")).getInstruction();
            tempSign.put("logtypeid",logtypeid);
            tempSign.put("loginstruction",loginstruction);
//            List<OperationMenuEntity> operationMenuEntities = getMenu(this.wfid,nodeid);
//            for (OperationMenuEntity op : operationMenuEntities) {
//                if (op.getId() == getSystemMenuType(rsSign.getString("logtype")).getId()) {
//                    if (!Util.null2String(op.getCustomName()).equals("")) {
//                        tempSign.put("logtypename", op.getCustomName());
//                    } else {
//                        tempSign.put("logtypename", SystemEnv.getHtmlLabelName(Integer.parseInt(getSystemMenuType(rsSign.getString("logtype")).getDefaultName()), 7));
//                    }
//                }
//            }
            List<SystemMenuEntity> systemMenuEntities = MenuOrderSetUtil.getOrder(wfid,nodeid,7);
            ArrayList<JSONObject> menuList = new ArrayList<>();
            for (SystemMenuEntity systemMenuEntity : systemMenuEntities) {
                String customerName = systemMenuEntity.getCustomerName();
                String defaultName = systemMenuEntity.getDefaultName();
                String instruction = systemMenuEntity.getInstruction();
                JSONObject jo = new JSONObject();
                jo.put("getCustomerName", systemMenuEntity.getCustomerName());
                jo.put("getDefaultName", systemMenuEntity.getDefaultName());
                jo.put("getId", systemMenuEntity.getId());
                menuList.add(jo);
                tempSign.put("menuType" , menuList);
                int id =  systemMenuEntity.getId();
                tempSign.put("instruction",instruction);
                if( id == logtypeid ) {
                    String logtypename = customerName.equals("") ? defaultName : customerName;
                    tempSign.put("logtypename", logtypename);
                }
            }
            SignDataList.add(tempSign);
        }

        return SignDataList;
    }

    private List<OperationMenuEntity> getMenu(int wfid, int nodeid) {
        WorkflowNodeMenuComInfo nodeMenuComInfo = new WorkflowNodeMenuComInfo();
        nodeMenuComInfo.getHasovertime(nodeid + "");
        ArrayList<OperationMenuEntity> operationMenuEntityList = new ArrayList<>();
        if (wfid >= 0 && nodeid >= 0) {
            OperationMenuEntity menuEntity1 = new OperationMenuEntity();
            menuEntity1.setId(1);
            operationMenuEntityList.add(menuEntity1);
            OperationMenuEntity var6 = new OperationMenuEntity();
            var6.setId(2);
            operationMenuEntityList.add(var6);
            OperationMenuEntity var7 = new OperationMenuEntity();
            var7.setId(3);
            operationMenuEntityList.add(var7);
            ForwardEntity var8 = new ForwardEntity();
            var8.setId(4);
            operationMenuEntityList.add(var8);
            OperationMenuEntity var9 = new OperationMenuEntity();
            var9.setId(5);
            operationMenuEntityList.add(var9);
            RejectEntity var10 = new RejectEntity();
            var10.setId(6);
            operationMenuEntityList.add(var10);
            OperationMenuEntity var11 = new OperationMenuEntity();
            var11.setId(24);
            operationMenuEntityList.add(var11);
            OperationMenuEntity var12 = new OperationMenuEntity();
            var12.setId(25);
            operationMenuEntityList.add(var12);
            OperationMenuEntity var13 = new OperationMenuEntity();
            var13.setId(7);
            operationMenuEntityList.add(var13);
            OperationMenuEntity var14 = new OperationMenuEntity();
            var14.setId(8);
            operationMenuEntityList.add(var14);
            OperationMenuEntity var15 = new OperationMenuEntity();
            var15.setId(9);
            operationMenuEntityList.add(var15);
            OperationMenuEntity var16 = new OperationMenuEntity();
            var16.setId(29);
            operationMenuEntityList.add(var16);
            ConsultationEntity var17 = new ConsultationEntity();
            var17.setId(10);
            operationMenuEntityList.add(var17);
            OperationMenuEntity var18 = new OperationMenuEntity();
            var18.setId(30);
            operationMenuEntityList.add(var18);
            OperationMenuEntity var19 = new OperationMenuEntity();
            var19.setId(31);
            operationMenuEntityList.add(var19);
            OperationMenuEntity var20 = new OperationMenuEntity();
            var20.setId(11);
            operationMenuEntityList.add(var20);
            OperationMenuEntity var21 = new OperationMenuEntity();
            var21.setId(12);
            operationMenuEntityList.add(var21);
            OperationMenuEntity var22 = new OperationMenuEntity();
            var22.setId(13);
            operationMenuEntityList.add(var22);
            OperationMenuEntity var23 = new OperationMenuEntity();
            var23.setId(14);
            operationMenuEntityList.add(var23);
            OperationMenuEntity var24 = new OperationMenuEntity();
            var24.setId(15);
            operationMenuEntityList.add(var24);
            OperationMenuEntity var25 = new OperationMenuEntity();
            var25.setId(16);
            operationMenuEntityList.add(var25);
            OperationMenuEntity var26 = new OperationMenuEntity();
            var26.setId(17);
            operationMenuEntityList.add(var26);
            OperationMenuEntity var27 = new OperationMenuEntity();
            var27.setId(18);
            operationMenuEntityList.add(var27);
            OperationMenuEntity var28 = new OperationMenuEntity();
            var28.setId(19);
            operationMenuEntityList.add(var28);
            OperationMenuEntity var29 = new OperationMenuEntity();
            var29.setId(20);
            operationMenuEntityList.add(var29);
            OperationMenuEntity var30 = new OperationMenuEntity();
            var30.setId(21);
            operationMenuEntityList.add(var30);
            OperationMenuEntity var31 = new OperationMenuEntity();
            var31.setId(22);
            operationMenuEntityList.add(var31);
            OperationMenuEntity var32 = new OperationMenuEntity();
            var32.setId(23);
            operationMenuEntityList.add(var32);
            OperationMenuEntity var33 = new OperationMenuEntity();
            var33.setId(26);
            operationMenuEntityList.add(var33);
            OperationMenuEntity var34 = new OperationMenuEntity();
            var34.setId(27);
            var34.setDefaultName("");
            operationMenuEntityList.add(var34);
            OperationMenuEntity var35 = new OperationMenuEntity();
            var35.setId(28);
            var35.setDefaultName("");
            operationMenuEntityList.add(var35);
            RecordSet var36 = new RecordSet();
            RecordSet var37 = new RecordSet();
            String var38 = "";
            String var39 = "";
            int var40 = 0;
            String var41 = "";
            String var42 = "";
            int var43 = 0;
            int var44 = 0;
            int var45 = 0;
            String var46 = "";
            String var47 = "";
            String var48 = "";
            int var49 = 0;
            String var50 = "";
            String var51 = "";
            int var52 = 0;
            String var53 = "";
            String var54 = "";
            int var55 = 0;
            boolean var56 = true;
            String var57 = "";
            boolean var58 = false;
            boolean var59 = false;
            boolean var60 = false;
            boolean var61 = false;
            boolean var62 = false;
            boolean var63 = false;
            boolean var65 = false;
            String var66 = "";
            boolean var67 = false;
            boolean var68 = false;
            boolean var69 = true;
            boolean var70 = Util.isEnableMultiLang();
            boolean var71 = false;
            var36.executeQuery("select * from workflow_nodecustomrcmenu where wfid=? and nodeid= ? ", wfid, nodeid);
            String var72 = "";
            String var73 = "";
            String var74 = "";
            String var75 = "";
            String var76 = "";
            String var77 = "";
            String var78 = "";
            String var79 = "";
            String var80 = "";
            String var81 = "";
            String var82 = "";
            String var83 = "";
            String var84 = "";
            String var85 = "";
            String var86 = "";
            String var87 = "";
            String var88 = "";
            String var89 = "";
            String var90 = "";
            String var91 = "";
            String var92 = "";
            String var93 = "";
            String var94 = "";
            String var95 = "";
            String var96;
            String var97;
            String var98;
            String var99;
            String var100;
            String var101;
            String var102;
            if (var36.next()) {
                var72 = Util.null2String(var36.getString("submitName7"));
                var73 = Util.null2String(var36.getString("submitName8"));
                var74 = Util.null2String(var36.getString("submitName9"));
                menuEntity1.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var72, var73, var74}) : var72);
                var96 = Util.null2String(var36.getString("forwardName7"));
                var97 = Util.null2String(var36.getString("forwardName8"));
                var98 = Util.null2String(var36.getString("forwardName9"));
                var8.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var96, var97, var98}) : var96);
                var99 = Util.null2String(var36.getString("forwardBackName7"));
                var100 = Util.null2String(var36.getString("forwardBackName8"));
                var101 = Util.null2String(var36.getString("forwardBackName9"));
                var32.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var99, var100, var101}) : var99);
                var102 = Util.null2String(var36.getString("saveName7"));
                String var103 = Util.null2String(var36.getString("saveName8"));
                String var104 = Util.null2String(var36.getString("saveName9"));
                var9.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var102, var103, var104}) : var102);
                String var105 = Util.null2String(var36.getString("rejectName7"));
                String var106 = Util.null2String(var36.getString("rejectName8"));
                String var107 = Util.null2String(var36.getString("rejectName9"));
                var10.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var105, var106, var107}) : var105);
                String var108 = Util.null2String(var36.getString("chuanyueName7"));
                String var109 = Util.null2String(var36.getString("chuanyueName8"));
                String var110 = Util.null2String(var36.getString("chuanyueName9"));
                var11.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var108, var109, var110}) : var108);
                String var111 = Util.null2String(var36.getString("chuanyueRecName7"));
                String var112 = Util.null2String(var36.getString("chuanyueRecName8"));
                String var113 = Util.null2String(var36.getString("chuanyueRecName9"));
                var12.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var111, var112, var113}) : var111);
                var12.setDefaultSign("");
                String var114 = Util.null2String(var36.getString("shareName7"));
                String var115 = Util.null2String(var36.getString("shareName8"));
                String var116 = Util.null2String(var36.getString("shareName9"));
                var33.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var114, var115, var116}) : var114);
                var78 = Util.null2String(var36.getString("forsubName7"));
                var79 = Util.null2String(var36.getString("forsubName8"));
                var80 = Util.null2String(var36.getString("forsubName9"));
                var23.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var78, var79, var80}) : var78);
                var75 = Util.null2String(var36.getString("forhandName7"));
                var76 = Util.null2String(var36.getString("forhandName8"));
                var77 = Util.null2String(var36.getString("forhandName9"));
                var13.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var75, var76, var77}) : var75);
                String var117 = Util.null2String(var36.getString("takingOpName7"));
                String var118 = Util.null2String(var36.getString("takingOpName8"));
                String var119 = Util.null2String(var36.getString("takingOpName9"));
                var17.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var117, var118, var119}) : var117);
                String var120 = Util.null2String(var36.getString("takeBackName7"));
                String var121 = Util.null2String(var36.getString("takeBackName8"));
                String var122 = Util.null2String(var36.getString("takeBackName9"));
                var16.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var120, var121, var122}) : var120);
                var84 = Util.null2String(var36.getString("takingOpinionsName7"));
                var85 = Util.null2String(var36.getString("takingOpinionsName8"));
                var86 = Util.null2String(var36.getString("takingOpinionsName9"));
                var20.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var84, var85, var86}) : var84);
                var81 = Util.null2String(var36.getString("ccsubName7"));
                var82 = Util.null2String(var36.getString("ccsubName8"));
                var83 = Util.null2String(var36.getString("ccsubName9"));
                var26.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var81, var82, var83}) : var81);
                String var123 = Util.null2String(var36.getString("newOverTimeName7"));
                String var124 = Util.null2String(var36.getString("newOverTimeName8"));
                String var125 = Util.null2String(var36.getString("newOverTimeName9"));
                var29.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var123, var124, var125}) : var123);
                String var126 = Util.null2String(var36.getString("submitDirectName7"));
                String var127 = Util.null2String(var36.getString("submitDirectName8"));
                String var128 = Util.null2String(var36.getString("submitDirectName9"));
                var30.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var126, var127, var128}) : var126);
                var87 = Util.null2String(var36.getString("printName7"));
                var88 = Util.null2String(var36.getString("printName8"));
                var89 = Util.null2String(var36.getString("printName9"));
                var34.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var87, var88, var89}) : var87);
                var90 = Util.null2String(var36.getString("printLogName7"));
                var91 = Util.null2String(var36.getString("printLogName8"));
                var92 = Util.null2String(var36.getString("printLogName9"));
                var35.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var90, var91, var92}) : var90);
                var93 = Util.null2String(var36.getString("takForward7"));
                var94 = Util.null2String(var36.getString("takForward8"));
                var95 = Util.null2String(var36.getString("takForward9"));
                var18.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var93, var94, var95}) : var93);
                var47 = Util.null2String(var36.getString("hasnoback"));
                var48 = Util.null2String(var36.getString("hasback"));
                var49 = Util.getIntValue(Util.null2String(var36.getString("subbackCtrl")), 0);
                var38 = Util.null2String(var36.getString("hasforhandback"));
                var39 = Util.null2String(var36.getString("hasforhandnoback"));
                var40 = Util.getIntValue(Util.null2String(var36.getString("forhandbackCtrl")), 0);
                var41 = Util.null2String(var36.getString("hastakingOpinionsback"));
                var42 = Util.null2String(var36.getString("hastakingOpinionsnoback"));
                var43 = Util.getIntValue(Util.null2String(var36.getString("takingOpinionsbackCtrl")), 0);
                var44 = Util.getIntValue(Util.null2String(var36.getString("chuanyuebackCtrl")), 0);
                var45 = Util.getIntValue(Util.null2String(var36.getString("takForwardBackCtrl")), 0);
                var50 = Util.null2String(var36.getString("hasfornoback"));
                var51 = Util.null2String(var36.getString("hasforback"));
                var52 = Util.getIntValue(Util.null2String(var36.getString("forsubbackCtrl")), 0);
                var53 = Util.null2String(var36.getString("hasccnoback"));
                var54 = Util.null2String(var36.getString("hasccback"));
                var55 = Util.getIntValue(Util.null2String(var36.getString("ccsubbackCtrl")), 0);
                var46 = Util.null2String(var36.getString("hasovertime"));
                var57 = Util.null2String(var36.getString("isSubmitDirect"));
            }

            var37.executeQuery("select * from workflow_flownode where workflowid = ? and nodeid = ?", wfid, nodeid);
            if (var37.next()) {
                var96 = Util.null2String(var37.getString("freewfsetcurnamecn"));
                var97 = Util.null2String(var37.getString("freewfsetcurnameen"));
                var98 = Util.null2String(var37.getString("freewfsetcurnametw"));
                var31.setCustomName(var70 ? Util.toMultiLangScreenFromArray(new String[]{var96, var97, var98}) : var96);
                var99 = Util.null2String(var37.getString("doEndTakCustomName"));
                var19.setCustomName(var99);
            }

            var96 = WorkflowOvertimeSettingsUtil.getOverTimeSettingsEntity().getChangestatus() + "";
            if ("".equals(var96)) {
                var49 = 0;
                var47 = "";
                var48 = "";
                var40 = 0;
                var39 = "";
                var38 = "";
                var52 = 0;
                var50 = "";
                var51 = "";
                var55 = 0;
                var53 = "";
                var54 = "";
                var43 = 0;
                var44 = 0;
                var45 = 0;
                var42 = "";
                var41 = "";
            } else {
                var97 = "";
                var98 = "";
                var99 = "";
                var100 = "";
                var101 = "";
                var102 = "";
                if (var49 == 2) {
                    var97 = Util.null2String(var36.getString("subbackName7"));
                    var98 = Util.null2String(var36.getString("subbackName8"));
                    var99 = Util.null2String(var36.getString("subbackName9"));
                    var100 = Util.null2String(var36.getString("subnobackName7"));
                    var101 = Util.null2String(var36.getString("subnobackName8"));
                    var102 = Util.null2String(var36.getString("subnobackName9"));
                    menuEntity1.getParams().put("feedback", 1);
                    menuEntity1.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var98, var99}) : var97);
                    menuEntity1.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var101, var102}) : var100);
                }

                if (2 == var40) {
                    var97 = Util.null2String(var36.getString("forhandbackName7"));
                    var98 = Util.null2String(var36.getString("forhandbackName8"));
                    var99 = Util.null2String(var36.getString("forhandbackName9"));
                    var100 = Util.null2String(var36.getString("forhandnobackName7"));
                    var101 = Util.null2String(var36.getString("forhandnobackName8"));
                    var102 = Util.null2String(var36.getString("forhandnobackName9"));
                    var13.getParams().put("feedback", 1);
                    var13.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var98, var99}) : var97);
                    var13.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var101, var102}) : var100);
                }

                if (2 == var52) {
                    var97 = Util.null2String(var36.getString("forsubbackName7"));
                    var98 = Util.null2String(var36.getString("forsubbackName8"));
                    var99 = Util.null2String(var36.getString("forsubbackName9"));
                    var100 = Util.null2String(var36.getString("forsubnobackName7"));
                    var101 = Util.null2String(var36.getString("forsubnobackName8"));
                    var102 = Util.null2String(var36.getString("forsubnobackName9"));
                    var23.getParams().put("feedback", 1);
                    var23.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var98, var99}) : var97);
                    var23.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var101, var102}) : var100);
                }

                if (2 == var55) {
                    var97 = Util.null2String(var36.getString("ccsubbackName7"));
                    var98 = Util.null2String(var36.getString("ccsubbackName8"));
                    var99 = Util.null2String(var36.getString("ccsubbackName9"));
                    var100 = Util.null2String(var36.getString("ccsubnobackName7"));
                    var101 = Util.null2String(var36.getString("ccsubnobackName8"));
                    var102 = Util.null2String(var36.getString("ccsubnobackName9"));
                    var26.getParams().put("feedback", 1);
                    var26.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var98, var99}) : var97);
                    var26.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var101, var102}) : var100);
                }

                if (2 == var43) {
                    var97 = Util.null2String(var36.getString("takingOpinionsbackName7"));
                    var98 = Util.null2String(var36.getString("takingOpinionsbackName8"));
                    var99 = Util.null2String(var36.getString("takingOpinionsbackName9"));
                    var100 = Util.null2String(var36.getString("takingOpinionsnobackName7"));
                    var101 = Util.null2String(var36.getString("takingOpinionsnobackName8"));
                    var102 = Util.null2String(var36.getString("takingOpinionsnobackName9"));
                    var20.getParams().put("feedback", 1);
                    var20.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var98, var99}) : var97);
                    var20.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var101, var102}) : var100);
                }

                if (var44 == 2) {
                    var97 = Util.null2String(var36.getString("chuanyuebackName7"));
                    var98 = Util.null2String(var36.getString("chuanyuebackName8"));
                    var99 = Util.null2String(var36.getString("chuanyuebackName9"));
                    var100 = Util.null2String(var36.getString("chuanyueNobackName7"));
                    var101 = Util.null2String(var36.getString("chuanyueNobackName8"));
                    var102 = Util.null2String(var36.getString("chuanyueNobackName9"));
                    var12.getParams().put("feedback", 1);
                    var12.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var98, var99}) : var97);
                    var12.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var101, var102}) : var100);
                }

                if (var45 == 2) {
                    var97 = Util.null2String(var36.getString("takForwardBackName"));
                    var100 = Util.null2String(var36.getString("takForwardNoBackName"));
                    var18.getParams().put("feedback", 1);
                    var18.getParams().put("feedbackName", var70 ? Util.toMultiLangScreenFromArray(new String[]{var97, var97, var97}) : var97);
                    var18.getParams().put("feedbackName_NO", var70 ? Util.toMultiLangScreenFromArray(new String[]{var100, var100, var100}) : var100);
                }
            }

            menuEntity1.setFeedBackControl(var49 + "");
            var13.setFeedBackControl(var40 + "");
            var23.setFeedBackControl(ListUtil.ToZero(var52) + "");
            var26.setFeedBackControl(ListUtil.ToZero(var55) + "");
            var20.setFeedBackControl(ListUtil.ToZero(var43) + "");
            var12.setFeedBackControl(ListUtil.ToZero(var44) + "");
            var18.setFeedBackControl(ListUtil.ToZero(var45) + "");
        }
        return operationMenuEntityList;
    }


    public SystemMenuType getSystemMenuType(String logType) {
        SystemMenuType[] values = SystemMenuType.values();

        for (SystemMenuType systemMenuType : values) {
            if (logType.equals(systemMenuType.getLogType())) {
                return systemMenuType;
            }
        }

        return null;
    }
}
