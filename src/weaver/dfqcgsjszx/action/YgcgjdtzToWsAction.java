package weaver.dfqcgsjszx.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.general.BaseBean;
import com.weaver.general.TimeUtil;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.ws.service_client.ISendMessageServiceProxy;
import weaver.file.ImageFileManager;
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
 * 因公出国季度调整调用外事接口
 *
 * @author Alex.Du
 */
public class YgcgjdtzToWsAction extends BaseBean implements Action {
    public String execute(RequestInfo requestInfo) {
        final String tableName = "formtable_main_176";

        //获取主表参数
        String guid = requestInfo.getRequestid();//GUID，使用当前请求的RequestId
        String bt = "";//标题
        String jjcd = "";//紧急程度
        String tzxm = "";//团长姓名
        String tzsfzh = "";//团长身份证号
        String bdwgsqk = "0";//本单位公示情况
        String cgxcfj = "";//出国行程附件
        String cgjlx = "";//出国境类型
        String tzlx = "";//团组类型
        String tzmc = "";//团组名称
        String tzcy = "";//团组成员
        String cfgjn = "";//出访国家
        String newcflxx = "";//出访类型
        String tlsx = "";//停留时限
        String cfmd = "";//出访目的
        String cfrwxxms = "";//出访任务详细描述
        String cflx = "";//出访路线
        String cfrs = "";//出访人数
        String ksrq = "";//开始日期（出访开始时间）
        String jsrq = "";//结束日期（出访结束时间）
        String tltianshu = "";//停留天数
        String fyqdfyh = "";//费用渠道（费用来源）
        String sqr = ""; //团组填报人
        String cjrbh = "";//创建人编号，通过团组填报人进行数据库查询
        String cjryx = "";//创建人邮箱，通过团组填报人进行数据库查询
        String cjrxm = "";//创建人姓名，通过团组填报人进行数据库查询
        String lxdh = "";//联系电话，通过团组填报人进行数据库查询
        String sqrq = "";//申请日期（创建时间）
        String xgfj = "";//邀请函(邀请函附件)
        String tzzj = "";//调用团组组建接口状态
        String cgqgs = "";//调用出国前公示接口状态
        String ygcgsb = "";//调用因公出国申报接口状态
        String qtfj = "";//其他附件

        Property[] properties = requestInfo.getMainTableInfo().getProperty();
        for(int i = 0;i<properties.length;i++){
            String name = properties[i].getName();
            String value = properties[i].getValue();

            if(name.trim().equals("bt")){
                bt = value.trim();
                continue;
            }

            if(name.trim().equals("jjcd")){
                jjcd = value.trim();
                continue;
            }

            if(name.trim().equals("tcxm")){
                tzxm = value.trim();
                continue;
            }

//            if(name.trim().equals("bdwgsqk")){
//                bdwgsqk = value.trim();
//                continue;
//            }

            if(name.trim().equals("xcfj")){
                cgxcfj = value.trim();
                continue;
            }


            if(name.trim().equals("cgjlx")){
                cgjlx = value.trim();
                continue;
            }

            if(name.trim().equals("tzlx")){
                tzlx = value.trim();
                continue;
            }

            if(name.trim().equals("tzmc")){
                tzmc = value.trim();
                continue;
            }

            if(name.trim().equals("tzcy")){
                tzcy = value.trim();
                continue;
            }

            if(name.trim().equals("cfgjn")){
                cfgjn = value.trim();
                continue;
            }

            if(name.trim().equals("newcflxx")){
                newcflxx = value.trim();
                continue;
            }

            if(name.trim().equals("tlsx")){
                tlsx = value.trim();
                continue;
            }

            if(name.trim().equals("cfmd")){
                cfmd = value.trim();
                continue;
            }

            if(name.trim().equals("cfrwxxms")){
                cfrwxxms = value.trim();
                continue;
            }

            if(name.trim().equals("cflx")){
                cflx = value.trim();
                continue;
            }

            if(name.trim().equals("cfrs")){
                cfrs = value.trim();
                continue;
            }

            if(name.trim().equals("ksrq")){
                ksrq = value.trim();
                continue;
            }

            if(name.trim().equals("jsrq")){
                jsrq = value.trim();
                continue;
            }

            if(name.trim().equals("tltianshu")){
                tltianshu = value.trim();
                continue;
            }

            if(name.trim().equals("fyqdfyh")){
                fyqdfyh = value.trim();
                continue;
            }

            if(name.trim().equals("sqr")){
                sqr = value.trim();
                continue;
            }

            if(name.trim().equals("sqrq")){
                sqrq = value.trim();
                continue;
            }

            if(name.trim().equals("xgfj")){
                xgfj = value.trim();
                continue;
            }

            if(name.trim().equals("tzzj")){
                tzzj = value.trim();
                continue;
            }

            if(name.trim().equals("cgqgs")){
                cgqgs = value.trim();
                continue;
            }

            if(name.trim().equals("ygcgsb")){
                ygcgsb = value.trim();
                continue;
            }

            if(name.trim().equals("qtfj")){
                qtfj = value.trim();
                continue;
            }
        }

        //主表单参数处理
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();

        //处理紧急程度
        writeLog("[YGcgjdtzToWsAction]紧急程度jjcd："+jjcd);

//        if(jjcd!=null&&!jjcd.equals("")){
//            rs.execute("select selectname from workflow_SelectItem where fieldid=146014 and selectvalue="+jjcd);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("普通")){
//                    jjcd="1";
//                }else if(rs.getString("name").trim().equals("加急")){
//                    jjcd="2";
//                }else if(rs.getString("name").trim().equals("特急")){
//                    jjcd="3";
//                }
//            }
//        }

        //处理团长姓名、团长身份证号
        writeLog("[YGcgjdtzToWsAction]团长姓名tzxm："+tzxm);

        if(tzxm!=null&&!tzxm.equals("")){
            rs.execute("select lastname,certificatenum from hrmresource where id="+tzxm);
            if(rs.next()){
                tzxm = rs.getString("lastname");
                tzsfzh = rs.getString("certificatenum");
            }
        }

        //处理紧急程度
        writeLog("[YGcgjdtzToWsAction]本单位公示情况bdwgsqk："+bdwgsqk);

//        if(bdwgsqk!=null&&!bdwgsqk.equals("")){
//            rs.execute("select selectname from workflow_SelectItem where fieldid=146019 and selectvalue="+bdwgsqk);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("已公示，公示期间无异议")){
//                    bdwgsqk="0";
//                }else if(rs.getString("name").trim().equals("已公示，公示期间有异议")){
//                    bdwgsqk="1";
//                }else if(rs.getString("name").trim().equals("未公示")){
//                    bdwgsqk="2";
//                }
//            }
//        }

        //处理出国境类型
        writeLog("[YGcgjdtzToWsAction]出国境类型cgjlx："+cgjlx);

//        if(cgjlx!=null&&!cgjlx.trim().equals("")){
//            rs.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+cgjlx);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("出国")){
//                    cgjlx = "1";
//                }else if(rs.getString("name").trim().equals("赴港澳")){
//                    cgjlx = "2";
//                }else if(rs.getString("name").trim().equals("赴台")){
//                    cgjlx = "3";
//                }
//            }
//        }

        //处理团组类型
        writeLog("[YGcgjdtzToWsAction]团组类型tzlx："+tzlx);
//        if(tzlx!=null&&!tzlx.trim().equals("")){
//            rs.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+tzlx);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("东风公司组团")){
//                    tzlx = "1";
//                }else if(rs.getString("name").trim().equals("双跨团")){
//                    tzlx = "2";
//                }
//            }
//        }

        //处理出访类型
        writeLog("[YGcgjdtzToWsAction]出访类型newcflxx："+newcflxx);
//        if(newcflxx!=null&&!newcflxx.trim().equals("")){
//            rs.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+newcflxx);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("临时")){
//                    newcflxx = "1";
//                }else if(rs.getString("name").trim().equals("常驻")){
//                    newcflxx = "2";
//                }else if(rs.getString("name").trim().equals("探亲")){
//                    newcflxx = "3";
//                }else if(rs.getString("name").trim().equals("访问")){
//                    newcflxx = "4";
//                }else if(rs.getString("name").trim().equals("信使")){
//                    newcflxx = "5";
//                }else if(rs.getString("name").trim().equals("部内临时")){
//                    newcflxx = "6";
//                }else if(rs.getString("name").trim().equals("进修")){
//                    newcflxx = "7";
//                }else if(rs.getString("name").trim().equals("其他")){
//                    newcflxx = "8";
//                }
//            }
//        }

        //处理停留时限
        writeLog("[YGcgjdtzToWsAction]停留时限tlsx："+tlsx);
//        if(tlsx!=null&&!tlsx.trim().equals("")){
//            rs.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+tlsx);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("临时")){
//                    tlsx = "1";
//                }else if(rs.getString("name").trim().equals("长期")){
//                    tlsx = "2";
//                }else if(rs.getString("name").trim().equals("常驻")){
//                    tlsx = "3";
//                }
//            }
//        }

        //处理出访目的
        writeLog("[YGcgjdtzToWsAction]出访目的cfmd："+cfmd);
//        if(cfmd!=null&&!cfmd.trim().equals("")){
//            rs.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+cfmd);
//            if(rs.next()){
//                if(rs.getString("name").trim().equals("经济贸易")){
//                    cfmd = "1";
//                }else if(rs.getString("name").trim().equals("考察")){
//                    cfmd = "2";
//                }else if(rs.getString("name").trim().equals("会议")){
//                    cfmd = "3";
//                }else if(rs.getString("name").trim().equals("科学技术")){
//                    cfmd = "4";
//                }else if(rs.getString("name").trim().equals("培训")){
//                    cfmd = "5";
//                }else if(rs.getString("name").trim().equals("文化体育")){
//                    cfmd = "6";
//                }else if(rs.getString("name").trim().equals("友好访问")){
//                    cfmd = "7";
//                }else if(rs.getString("name").trim().equals("高访")){
//                    cfmd = "8";
//                }else if(rs.getString("name").trim().equals("留学")){
//                    cfmd = "9";
//                }else if(rs.getString("name").trim().equals("民航")){
//                    cfmd = "10";
//                }else if(rs.getString("name").trim().equals("维和、军援、军控")){
//                    cfmd = "11";
//                }else if(rs.getString("name").trim().equals("工程项目")){
//                    cfmd = "12";
//                }else if(rs.getString("name").trim().equals("馆工")){
//                    cfmd = "13";
//                }else if(rs.getString("name").trim().equals("巡检")){
//                    cfmd = "14";
//                }else if(rs.getString("name").trim().equals("突发")){
//                    cfmd = "15";
//                }else if(rs.getString("name").trim().equals("验收")){
//                    cfmd = "16";
//                }else if(rs.getString("name").trim().equals("谈判")){
//                    cfmd = "17";
//                }else if(rs.getString("name").trim().equals("省外")){
//                    cfmd = "18";
//                }else if(rs.getString("name").trim().equals("常驻")){
//                    cfmd = "19";
//                }else if(rs.getString("name").trim().equals("随任")){
//                    cfmd = "20";
//                }else if(rs.getString("name").trim().equals("探亲")){
//                    cfmd = "21";
//                }else if(rs.getString("name").trim().equals("已办因私")){
//                    cfmd = "22";
//                }
//            }
//        }

        //处理创建人编号、创建人邮箱、创建人姓名、联系电话
        writeLog("[YGcgjdtzToWsAction]团组填报人sqr："+sqr);
        if(sqr!=null&&!sqr.trim().equals("")){
            rs.execute("select workcode,email,lastname,mobile from hrmresource where id = "+sqr);
            if(rs.next()){
                cjrbh = rs.getString("workcode");
                cjryx = rs.getString("email");
                cjrxm = rs.getString("lastname");
                lxdh = rs.getString("mobile");
            }
        }

        //处理出访国家
        writeLog("[YGcgjdtzToWsAction]出访国家cfgjn："+cfgjn);
        if(cfgjn!=null&&!cfgjn.trim().equals("")){
            rs.execute("select * from uf_cfgjxx where id in ("+cfgjn+")");
            cfgjn = "";
            for(int i=0;rs.next();i++){
                if(i!=0) {
                    cfgjn += ",";
                }
                cfgjn += rs.getString("cfgjdq");
            }
        }

        //处理标题字段
        bt = tzcy+"赴"+cfgjn+"执行"+tzmc+"任务公示";

        //获取子表单参数（出访地）
        JSONArray cfdJSONArray = new JSONArray();//用于封装出访地子表单参数的Json数组对象
        rs.execute("select * from "+tableName+"_dt1 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("获取出访地子表单参数SQL为：select * from "+tableName+"_dt1 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("共查询到数据"+rs.getCounts()+"条");

        while(rs.next()){
            JSONObject cfdJSONObject = new JSONObject();
            //明细表GUID，使用明细表ID字段
            cfdJSONObject.put("id",rs.getString("id").trim());

            //出访国家/地区
            if(rs.getString("cfgjdq")!=null&&!rs.getString("cfgjdq").trim().equals("")){
                rs2.execute("select cfgjdq from uf_cfgjxx where id = "+rs.getString("cfgjdq").trim());
                if(rs2.next()){
                    cfdJSONObject.put("cfgjdq",rs2.getString("cfgjdq").trim());
                }else{
                    cfdJSONObject.put("cfgjdq","");
                }
            }else{
                cfdJSONObject.put("cfgjdq","");
            }

            //停留天数（出访天数）
            cfdJSONObject.put("tltsn",rs.getString("tltsn").trim());

            //到达出访地时间
            cfdJSONObject.put("ddcfdsj",rs.getString("ddcfdsj").trim());

            //离开出访地时间
            cfdJSONObject.put("lkcfdsj",rs.getString("lkcfdsj").trim());

            //入境城市
            cfdJSONObject.put("rjcs",rs.getString("rjcs").trim());

            //出境城市
            cfdJSONObject.put("cjcs",rs.getString("cjcs").trim());

            //签证种类
            cfdJSONObject.put("qzzl",rs.getString("qzzl").trim());

//            if(rs.getString("qzzl")!=null&&!rs.getString("qzzl").trim().equals("")){
//                rs2.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+rs.getString("qzzl").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("一次过境")){
//                        cfdJSONObject.put("qzzl","1");
//                    }else if(rs2.getString("name").trim().equals("一次入境")){
//                        cfdJSONObject.put("qzzl","2");
//                    }else if(rs2.getString("name").trim().equals("二次过境")){
//                        cfdJSONObject.put("qzzl","3");
//                    }else if(rs2.getString("name").trim().equals("二次入境")){
//                        cfdJSONObject.put("qzzl","4");
//                    }else if(rs2.getString("name").trim().equals("多次过境")){
//                        cfdJSONObject.put("qzzl","5");
//                    }else if(rs2.getString("name").trim().equals("多次入境")){
//                        cfdJSONObject.put("qzzl","6");
//                    }else{
//                        cfdJSONObject.put("qzzl","");
//                    }
//                }else{
//                    cfdJSONObject.put("qzzl","");
//                }
//            }else{
//                cfdJSONObject.put("qzzl","");
//            }

            //签证类型
            cfdJSONObject.put("qzlx",rs.getString("qzlx").trim());

//            if(rs.getString("qzlx")!=null&&!rs.getString("qzlx").trim().equals("")){
//                rs2.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+rs.getString("qzlx").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("待签")){
//                        cfdJSONObject.put("qzlx","1");
//                    }else if(rs2.getString("name").trim().equals("返签")){
//                        cfdJSONObject.put("qzlx","2");
//                    }else if(rs2.getString("name").trim().equals("免签")){
//                        cfdJSONObject.put("qzlx","3");
//                    }else if(rs2.getString("name").trim().equals("落地签")){
//                        cfdJSONObject.put("qzlx","4");
//                    }else{
//                        cfdJSONObject.put("qzlx","");
//                    }
//                }else{
//                    cfdJSONObject.put("qzlx","");
//                }
//            }else{
//                cfdJSONObject.put("qzlx","");
//            }

            //出访事由
            cfdJSONObject.put("cfsy",rs.getString("cfsy").trim());

            //邀请单位
            cfdJSONObject.put("yqdw",rs.getString("yqdw").trim());

            //团组ID
            cfdJSONObject.put("GroupId",guid);

            cfdJSONArray.add(cfdJSONObject);
            cfdJSONObject = null;
        }

        writeLog("处理后的cfdJSONArray.size()="+cfdJSONArray.size());


        //获取子表单参数（团组成员）
        JSONArray tzcyJSONArray = new JSONArray();//用于封装团组成员子表单参数的Json数组对象
        rs.execute("select * from "+tableName+"_dt2 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("获取团组成员子表单参数SQL为：select * from "+tableName+"_dt2 where mainid=(select id from "+tableName+" where requestid="+requestInfo.getRequestid()+")");

        writeLog("共查询到数据"+rs.getCounts()+"条");

        while(rs.next()) {
            JSONObject tzcyJSONObject = new JSONObject();

            //明细表GUID，使用明细表ID字段
            tzcyJSONObject.put("id",rs.getString("id").trim());

            //姓
            tzcyJSONObject.put("xhz",rs.getString("xhz").trim());

            //名
            tzcyJSONObject.put("mhz",rs.getString("mhz").trim());

            //姓的拼音
            tzcyJSONObject.put("x",rs.getString("x").trim());

            //名的拼音
            tzcyJSONObject.put("m",rs.getString("m").trim());

            //身份证号
            tzcyJSONObject.put("sfzh",rs.getString("sfzh").trim());

            //性别
            tzcyJSONObject.put("xb",rs.getString("xb").trim());

//            if(rs.getString("xb")!=null&&!rs.getString("xb").trim().equals("")){
//                rs2.execute("select selectname from workflow_SelectItem where fieldid=52886 and selectvalue="+rs.getString("xb").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("男")){
//                        tzcyJSONObject.put("xb","1");
//                    }else if(rs2.getString("name").trim().equals("女")){
//                        tzcyJSONObject.put("xb","2");
//                    }else{
//                        tzcyJSONObject.put("xb","");
//                    }
//                }else{
//                    tzcyJSONObject.put("xb","");
//                }
//            }else{
//                tzcyJSONObject.put("xb","");
//            }

            //生日
            tzcyJSONObject.put("csnyr",rs.getString("csnyr").trim());

            //民族
            tzcyJSONObject.put("mz",rs.getString("mz").trim());

            //出生地（省/直辖市）
            tzcyJSONObject.put("csdsf",rs.getString("csdsf").trim());

            //出生地（市）
            tzcyJSONObject.put("csds",rs.getString("csds").trim());

            //职务
            //对外职务
            if(rs.getString("zw")!=null&&!rs.getString("zw").trim().equals("")){
                rs2.execute("select jobactivitymark from hrmjobactivities where id="+rs.getString("zw").trim());
                if(rs2.next()){
                    tzcyJSONObject.put("zw",rs2.getString("jobactivitymark").trim());
                }else{
                    tzcyJSONObject.put("zw","");
                }
            }else{
                tzcyJSONObject.put("zw","");
            }

            //工作单位
            tzcyJSONObject.put("gzdw",rs.getString("gzdw").trim());

            //对外单位
            //tzcyJSONObject.put("dwdw",rs.getString("dwdw").trim());
            tzcyJSONObject.put("dwdw","东风汽车集团有限公司技术中心");


            //证照情况
            tzcyJSONObject.put("zzqk",rs.getString("zzqk").trim());

//            if(rs.getString("zzqk")!=null&&!rs.getString("zzqk").trim().equals("")){
//                rs2.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+rs.getString("zzqk").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("无")){
//                        tzcyJSONObject.put("zzqk","1");
//                    }else if(rs2.getString("name").trim().equals("准持")){
//                        tzcyJSONObject.put("zzqk","2");
//                    }else{
//                        tzcyJSONObject.put("zzqk","");
//                    }
//                }else{
//                    tzcyJSONObject.put("zzqk","");
//                }
//            }else{
//                tzcyJSONObject.put("zzqk","");
//            }

            //准持护照号(证照号)
            tzcyJSONObject.put("zchzh",rs.getString("zchzh").trim());

            //证照签发日期
            tzcyJSONObject.put("zzqfrq",rs.getString("zzqfrq").trim());

            //证照有效期(证照有效期日期)
            tzcyJSONObject.put("zzyxq",rs.getString("zzyxq").trim());

            //照片回执编号
            tzcyJSONObject.put("zphzbh",rs.getString("zphzbh").trim());

            //护照种类
            tzcyJSONObject.put("hzzl",rs.getString("hzzl").trim());

//            if(rs.getString("hzzl")!=null&&!rs.getString("hzzl").trim().equals("")){
//                rs2.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+rs.getString("hzzl").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("公务普通")){
//                        tzcyJSONObject.put("hzzl","1");
//                    }else if(rs2.getString("name").trim().equals("其他")){
//                        tzcyJSONObject.put("hzzl","2");
//                    }else{
//                        tzcyJSONObject.put("hzzl","");
//                    }
//                }else{
//                    tzcyJSONObject.put("hzzl","");
//                }
//            }else{
//                tzcyJSONObject.put("hzzl","");
//            }

            //签证级别
            tzcyJSONObject.put("qzjb",rs.getString("qzjb").trim());
//            if(rs.getString("qzjb")!=null&&!rs.getString("qzjb").trim().equals("")){
//                rs2.execute("select name from MODE_SELECTITEMPAGEDETAIL where id="+rs.getString("qzjb").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("普通")){
//                        tzcyJSONObject.put("qzjb","1");
//                    }else if(rs2.getString("name").trim().equals("司局级")){
//                        tzcyJSONObject.put("qzjb","2");
//                    }else if(rs2.getString("name").trim().equals("省部级")){
//                        tzcyJSONObject.put("qzjb","3");
//                    }else if(rs2.getString("name").trim().equals("高访")){
//                        tzcyJSONObject.put("qzjb","4");
//                    }else{
//                        tzcyJSONObject.put("qzjb","");
//                    }
//                }else{
//                    tzcyJSONObject.put("qzjb","");
//                }
//            }else{
//                tzcyJSONObject.put("qzjb","");
//            }

            //是否有预算
            tzcyJSONObject.put("sfyys",rs.getString("sfyys").trim());
//            if(rs.getString("sfyys")!=null&&!rs.getString("sfyys").trim().equals("")){
//                rs2.execute("select selectname from workflow_SelectItem where fieldid=146017 and selectvalue="+rs.getString("sfyys").trim());
//                if(rs2.next()){
//                    if(rs2.getString("name").trim().equals("有")){
//                        tzcyJSONObject.put("sfyys","1");
//                    }else if(rs2.getString("name").trim().equals("无")){
//                        tzcyJSONObject.put("sfyys","0");
//                    }else{
//                        tzcyJSONObject.put("sfyys","");
//                    }
//                }else{
//                    tzcyJSONObject.put("sfyys","");
//                }
//            }else{
//                tzcyJSONObject.put("sfyys","");
//            }

            //团组ID
            tzcyJSONObject.put("GroupId",guid);

            tzcyJSONArray.add(tzcyJSONObject);
            tzcyJSONObject = null;
        }

        writeLog("处理后的tzcyJSONArray.size()="+tzcyJSONArray.size());

        JSONObject paramHead = new JSONObject();
        JSONObject paramBody = new JSONObject();

        JSONArray placeDetail = new JSONArray();//出访地明细参数
        JSONArray memberDetail = new JSONArray();//团组成员明细参数
        JSONArray scheduleAtt = new JSONArray();//出国行程附件明细参数

        String result = null;
        //**********************************************调用团组组建接口
        writeLog("[YGcgjdtzToWsAction]团组组建tzzj:"+tzzj);
        if(tzzj==null||tzzj.trim().equals("")) {
            //构建接口头部参数
            paramHead.put("clientCode", "DFTC_COS");
            paramHead.put("reqSerialNo", UUID.randomUUID().toString());
            paramHead.put("tradeCode", "DFG_FAM_001");
            paramHead.put("tradeDescription", "因公出国季度调整调用团组组建接口");
            paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
            paramHead.put("version", "1.0");

            //构建接口内容参数

            paramBody.put("GUID", guid);
            paramBody.put("OutType", cgjlx);
            paramBody.put("GroupType", tzlx);
            paramBody.put("GroupName", tzmc);
            paramBody.put("TripType", newcflxx);
            paramBody.put("StayType", tlsx);
            paramBody.put("TripAim", cfmd);
            paramBody.put("TripDuty", cfrwxxms);
            paramBody.put("TripRoute", cflx);
            paramBody.put("TripStartTime", ksrq);
            paramBody.put("TripEndTime", jsrq);
            paramBody.put("StayDays", tltianshu);
            paramBody.put("CostSource", fyqdfyh);
            paramBody.put("CreatorNo", cjrbh);
            paramBody.put("CreatorEmail", cjryx);
            paramBody.put("CreatorName", cjrxm);
            paramBody.put("Tel", lxdh);
            paramBody.put("CreaterDept", "东风汽车集团有限公司技术中心");
            paramBody.put("CreateTime", sqrq + " 00:00");
            paramBody.put("SourceSys", "COS");

            //出访地明细参数
            for (int i = 0; i < cfdJSONArray.size(); i++) {
                JSONObject placeJson = new JSONObject();
                placeJson.put("GUID", cfdJSONArray.getJSONObject(i).getString("id"));
                placeJson.put("Nation", cfdJSONArray.getJSONObject(i).getString("cfgjdq"));
                placeJson.put("StayDays", cfdJSONArray.getJSONObject(i).getString("tltsn"));
                placeJson.put("EntryTime", cfdJSONArray.getJSONObject(i).getString("ddcfdsj"));
                placeJson.put("ExitTime", cfdJSONArray.getJSONObject(i).getString("lkcfdsj"));
                placeJson.put("EntryCity", cfdJSONArray.getJSONObject(i).getString("rjcs"));
                placeJson.put("ExitCity", cfdJSONArray.getJSONObject(i).getString("cjcs"));
                placeJson.put("VisaKind", cfdJSONArray.getJSONObject(i).getString("qzzl"));
                placeJson.put("VisaType", cfdJSONArray.getJSONObject(i).getString("qzlx"));
                placeJson.put("TripReason", cfdJSONArray.getJSONObject(i).getString("cfsy"));
                placeJson.put("InvitingUnit", cfdJSONArray.getJSONObject(i).getString("yqdw"));
                placeJson.put("InvitingAddress", "");
                placeJson.put("Inviter", "");
                placeJson.put("InviterTel", "");
                placeJson.put("Remark", "");
                placeJson.put("GroupId", cfdJSONArray.getJSONObject(i).getString("GroupId"));

                placeDetail.add(placeJson);
                placeJson = null;
            }
            paramBody.put("PlaceDetail", placeDetail);
            placeDetail = null;

            //团组成员明细参数
            for (int i = 0; i < tzcyJSONArray.size(); i++) {
                JSONObject memberJson = new JSONObject();
                memberJson.put("GUID", tzcyJSONArray.getJSONObject(i).getString("id"));
                memberJson.put("FirstName", tzcyJSONArray.getJSONObject(i).getString("xhz"));
                memberJson.put("LastName", tzcyJSONArray.getJSONObject(i).getString("mhz"));
                memberJson.put("FirstNamePY", tzcyJSONArray.getJSONObject(i).getString("x"));
                memberJson.put("LastNamePY", tzcyJSONArray.getJSONObject(i).getString("m"));
                memberJson.put("IDNumber", tzcyJSONArray.getJSONObject(i).getString("sfzh"));
                memberJson.put("Sex", tzcyJSONArray.getJSONObject(i).getString("xb"));
                memberJson.put("Birthday", tzcyJSONArray.getJSONObject(i).getString("csnyr"));
                memberJson.put("Nation", tzcyJSONArray.getJSONObject(i).getString("mz"));
                memberJson.put("BirthPlaceProvince", tzcyJSONArray.getJSONObject(i).getString("csdsf"));
                memberJson.put("BirthPlaceCity", tzcyJSONArray.getJSONObject(i).getString("csds"));
                memberJson.put("Post", tzcyJSONArray.getJSONObject(i).getString("zw"));
                memberJson.put("ExternalPost", tzcyJSONArray.getJSONObject(i).getString("zw"));
                memberJson.put("WorkUnit", tzcyJSONArray.getJSONObject(i).getString("gzdw"));
                memberJson.put("ExternalWorkUnit", tzcyJSONArray.getJSONObject(i).getString("dwdw"));
                memberJson.put("HasPassport", tzcyJSONArray.getJSONObject(i).getString("zzqk"));
                //根据证照情况处理数据
                if (tzcyJSONArray.getJSONObject(i).getString("zzqk").equals("1")) {
                    //如果证照情况为1（无）,则传递照片回执编号、护照种类、签证级别
                    memberJson.put("PhoneCode", tzcyJSONArray.getJSONObject(i).getString("zphzbh"));
                    memberJson.put("PassportType", tzcyJSONArray.getJSONObject(i).getString("hzzl"));
                    memberJson.put("VisaLevel", tzcyJSONArray.getJSONObject(i).getString("qzjb"));
                    memberJson.put("PassportNo", "");
                    memberJson.put("PassportSignDate", "");
                    memberJson.put("PassportValidityDage", "");
                } else if (tzcyJSONArray.getJSONObject(i).getString("zzqk").equals("2")) {
                    //如果证照情况为2（准持）,则传递证照号、证照签发日期、证照有效期日期
                    memberJson.put("PassportNo", tzcyJSONArray.getJSONObject(i).getString("zchzh"));
                    memberJson.put("PassportSignDate", tzcyJSONArray.getJSONObject(i).getString("zzqfrq"));
                    memberJson.put("PassportValidityDage", tzcyJSONArray.getJSONObject(i).getString("zzyxq"));
                    memberJson.put("PhoneCode", "");
                    memberJson.put("PassportType", "");
                    memberJson.put("VisaLevel", "");
                }

                memberJson.put("HuKouBookName", "");
                memberJson.put("HuKouBookType", "");
                memberJson.put("HuKouBook", "");
                memberJson.put("IDCardName", "");
                memberJson.put("IDCardType", "");
                memberJson.put("IDCard", "");

                memberJson.put("GroupId", tzcyJSONArray.getJSONObject(i).getString("GroupId"));

                memberDetail.add(memberJson);
                memberJson = null;
            }
            paramBody.put("MemberDetail", memberDetail);
            memberDetail = null;

            writeLog("[YGcgjdtzToWsAction]开始调用第一个团组组建接口");
            writeLog("[YGcgjdtzToWsAction]paramHead=" + paramHead);
            writeLog("[YGcgjdtzToWsAction]paramBody=" + paramBody);
            result = null;
            try {
                result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("[YGcgjdtzToWsAction]调用第一个团组组建接口时出现异常：" + e.getMessage());
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用外事接口返回异常: " + e.getMessage());// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }
            writeLog("[YGcgjdtzToWsAction]result=" + result);
            writeLog("[YGcgjdtzToWsAction]第一个团组组建接口调用完毕");

            writeLog("[YGcgjdtzToWsAction]开始解析第一个团组组建接口的调用结果");
            JSONArray tzzjResultJson = JSONArray.parseArray(result);
            writeLog("[YGcgjdtzToWsAction]解析第一个团组组建接口的调用结果完成");

            if (!tzzjResultJson.getJSONObject(0).getString("status").trim().equals("1")) {
                //接口返回的状态表示调用失败，则阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用外事接口返回失败: " + tzzjResultJson.getJSONObject(0).getString("message"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            } else {
                writeLog("[YGcgjdtzToWsAction]调用第一个团组组建接口的为成功，执行状态更新语句:update " + tableName + " set tzzj=1 where requestid=" + requestInfo.getRequestid());
                rs.execute("update " + tableName + " set tzzj=1 where requestid=" + requestInfo.getRequestid());
            }
            tzzjResultJson = null;
            paramHead = null;
            paramBody = null;
        }

        //**********************************************调用出国前公示接口
        writeLog("[YGcgjdtzToWsAction]出国前公示cgqgs:"+cgqgs);
        if(cgqgs==null||cgqgs.trim().equals("")) {
            //构建接口头部参数
            paramHead = new JSONObject();
            paramHead.put("clientCode", "DFTC_COS");
            paramHead.put("reqSerialNo", UUID.randomUUID().toString());
            paramHead.put("tradeCode", "DFG_FAM_002");
            paramHead.put("tradeDescription", "因公出国季度调整调用出国前公示接口");
            paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
            paramHead.put("version", "1.0");

            //构建接口内容参数
            paramBody = new JSONObject();
            paramBody.put("GUID", guid);
            paramBody.put("DocSubject", bt);
            paramBody.put("GroupId", guid);
            paramBody.put("GroupName", tzmc);
            paramBody.put("Emergency", jjcd);

            paramBody.put("OutType", "");
            paramBody.put("GroupType", "");
            paramBody.put("OutCompanyName", "");
            paramBody.put("MisstionOtherno", "");

            paramBody.put("GroupLeaderIdNumber", tzsfzh);
            paramBody.put("GroupLeaderName", tzxm);
            paramBody.put("GroupNum", cfrs);
            paramBody.put("TripStartTime", ksrq);
            paramBody.put("TripEndTime", jsrq);
            paramBody.put("StayDays", tltianshu);
            paramBody.put("CostSource", fyqdfyh);
            paramBody.put("TripDuty", cfrwxxms);
            paramBody.put("TripRoute", cflx);

            paramBody.put("Remark", "");

            paramBody.put("CreatorNo", cjrbh);
            paramBody.put("CreatorEmail", cjryx);
            paramBody.put("LinkMobile", lxdh);
            paramBody.put("CreateTime", sqrq + " 00:00");
            paramBody.put("SourceSys", "COS");

            //出访地明细参数
            placeDetail = new JSONArray();
            for (int i = 0; i < cfdJSONArray.size(); i++) {
                JSONObject placeJson = new JSONObject();
                placeJson.put("GUID", cfdJSONArray.getJSONObject(i).getString("id"));
                placeJson.put("Nation", cfdJSONArray.getJSONObject(i).getString("cfgjdq"));
                placeJson.put("StayDays", cfdJSONArray.getJSONObject(i).getString("tltsn"));
                placeJson.put("EntryTime", cfdJSONArray.getJSONObject(i).getString("ddcfdsj"));
                placeJson.put("ExitTime", cfdJSONArray.getJSONObject(i).getString("lkcfdsj"));
                placeJson.put("TripReason", cfdJSONArray.getJSONObject(i).getString("cfsy"));
                placeJson.put("InvitingUnit", cfdJSONArray.getJSONObject(i).getString("yqdw"));
                placeJson.put("BeforeId", cfdJSONArray.getJSONObject(i).getString("GroupId"));

                placeDetail.add(placeJson);
                placeJson = null;
            }
            paramBody.put("PlaceDetail", placeDetail);
            placeDetail = null;

            //团组成员明细参数
            memberDetail = new JSONArray();
            for (int i = 0; i < tzcyJSONArray.size(); i++) {
                JSONObject memberJson = new JSONObject();
                memberJson.put("GUID", tzcyJSONArray.getJSONObject(i).getString("id"));
                memberJson.put("UserName", tzcyJSONArray.getJSONObject(i).getString("xhz") + tzcyJSONArray.getJSONObject(i).getString("mhz"));
                memberJson.put("IDNumber", tzcyJSONArray.getJSONObject(i).getString("sfzh"));
                memberJson.put("Sex", tzcyJSONArray.getJSONObject(i).getString("xb"));
                memberJson.put("Birthday", tzcyJSONArray.getJSONObject(i).getString("csnyr"));
                memberJson.put("BirthPlace", tzcyJSONArray.getJSONObject(i).getString("csds"));
                memberJson.put("Post", tzcyJSONArray.getJSONObject(i).getString("zw"));
                memberJson.put("WorkUnit", tzcyJSONArray.getJSONObject(i).getString("gzdw"));
                memberJson.put("HasBedget", tzcyJSONArray.getJSONObject(i).getString("sfyys"));
                memberJson.put("BeforeId", tzcyJSONArray.getJSONObject(i).getString("GroupId"));

                memberDetail.add(memberJson);
                memberJson = null;
            }
            paramBody.put("MemberDetail", memberDetail);
            memberDetail = null;

            //出国行程附件明细参数
            if (cgxcfj != null && !cgxcfj.trim().equals("")) {
                String[] cgxcfjIds = cgxcfj.split(",");
                for (int i = 0; i < cgxcfjIds.length; i++) {
                    if (!cgxcfjIds[i].trim().equals("")) {
                        JSONObject fileJson = getFileJson(cgxcfjIds[i].trim());
                        if (fileJson != null) {
                            scheduleAtt.add(fileJson);
                        }
                    }
                }
            }
            paramBody.put("ScheduleAtt", scheduleAtt);

            //邀请函附件明细参数
            JSONArray invitationAtt = new JSONArray();

            if (xgfj != null && !xgfj.trim().equals("")) {
                String[] xgfjIds = xgfj.split(",");
                for (int i = 0; i < xgfjIds.length; i++) {
                    if (!xgfjIds[i].trim().equals("")) {
                        JSONObject fileJson = getFileJson(xgfjIds[i].trim());
                        if (fileJson != null) {
                            invitationAtt.add(fileJson);
                        }
                    }
                }
            }
            paramBody.put("InvitationAtt", invitationAtt);
            invitationAtt = null;

            writeLog("[YGcgjdtzToWsAction]开始调用第二个出国前公示接口");
            writeLog("[YGcgjdtzToWsAction]paramHead=" + paramHead);
            writeLog("[YGcgjdtzToWsAction]paramBody=" + paramBody);
            result = null;
            try {
                result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("[YGcgjdtzToWsAction]调用第二个出国前公示接口时出现异常：" + e.getMessage());
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用第二个出国前公示接口返回异常: " + e.getMessage());// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }
            writeLog("[YGcgjdtzToWsAction]result=" + result);
            writeLog("[YGcgjdtzToWsAction]第二个出国前公示接口调用完毕");

            writeLog("[YGcgjdtzToWsAction]开始解析第二个出国前公示接口的调用结果");
            JSONArray cgqgsResultJson = JSONArray.parseArray(result);
            writeLog("[YGcgjdtzToWsAction]解析第二个出国前公示接口的调用结果完成");


            if (!cgqgsResultJson.getJSONObject(0).getString("status").trim().equals("1")) {
                //接口返回的状态表示调用失败，则阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用出国前公示接口返回失败: " + cgqgsResultJson.getJSONObject(0).getString("message"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            } else {
                writeLog("[YGcgjdtzToWsAction]调用第二个出国前公示接口的为成功，执行状态更新语句:update " + tableName + " set cgqgs=1 where requestid=" + requestInfo.getRequestid());
                rs.execute("update " + tableName + " set cgqgs=1 where requestid=" + requestInfo.getRequestid());
            }
            cgqgsResultJson = null;
            paramHead = null;
            paramBody = null;
        }

        //**********************************************调用因公出国申报接口
        writeLog("[YGcgjdtzToWsAction]因公出国申报ygcgsb:"+ygcgsb);
        if(ygcgsb==null||ygcgsb.trim().equals("")) {
            //构建接口头部参数
            paramHead = new JSONObject();
            paramHead.put("clientCode", "DFTC_COS");
            paramHead.put("reqSerialNo", UUID.randomUUID().toString());
            paramHead.put("tradeCode", "DFG_FAM_003");
            paramHead.put("tradeDescription", "因公出国季度调整调用因公出国申报接口");
            paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
            paramHead.put("version", "1.0");

            //构建接口内容参数
            paramBody = new JSONObject();
            paramBody.put("GUID", guid);
            paramBody.put("GroupId", guid);
            paramBody.put("GroupName", tzmc);
            paramBody.put("DocSubject", bt);
            paramBody.put("OutType", cgjlx);
            paramBody.put("Emergency", jjcd);
            paramBody.put("PublicState", bdwgsqk);
            paramBody.put("GroupType", tzlx);

            paramBody.put("OutCompanyName", "");
            paramBody.put("MisstionOtherno", "");

            paramBody.put("GroupLeaderIdNumber", tzsfzh);
            paramBody.put("GroupLeaderName", tzxm);
            paramBody.put("GroupLeaderDept", "158baaf4d641c38edd8231b4297b9a23");
            paramBody.put("GLDeptLeader", "1558540e21d7f166caa2ca54957845b6");
            paramBody.put("GroupNum", cfrs);
            paramBody.put("TripStartTime", ksrq);
            paramBody.put("TripEndTime", jsrq);
            paramBody.put("StayDays", tltianshu);
            paramBody.put("CostSource", fyqdfyh);
            paramBody.put("TripDuty", cfrwxxms);
            paramBody.put("TripRoute", cflx);

            paramBody.put("Remark", "");

            paramBody.put("CreatorNo", cjrbh);
            paramBody.put("LinkMobile", lxdh);
            paramBody.put("CreatorEmail", cjryx);
            paramBody.put("CreatorName", cjrxm);
            paramBody.put("CreaterDept", "东风汽车集团有限公司技术中心");
            paramBody.put("CreateTime", sqrq + " 00:00");
            paramBody.put("SourceSys", "COS");

            //出访地明细参数
            placeDetail = new JSONArray();
            for (int i = 0; i < cfdJSONArray.size(); i++) {
                JSONObject placeJson = new JSONObject();
                placeJson.put("GUID", cfdJSONArray.getJSONObject(i).getString("id"));
                placeJson.put("Nation", cfdJSONArray.getJSONObject(i).getString("cfgjdq"));
                placeJson.put("StayDays", cfdJSONArray.getJSONObject(i).getString("tltsn"));
                placeJson.put("EntryTime", cfdJSONArray.getJSONObject(i).getString("ddcfdsj"));
                placeJson.put("ExitTime", cfdJSONArray.getJSONObject(i).getString("lkcfdsj"));
                placeJson.put("TripReason", cfdJSONArray.getJSONObject(i).getString("cfsy"));
                placeJson.put("InvitingUnit", cfdJSONArray.getJSONObject(i).getString("yqdw"));
                placeJson.put("TripId", cfdJSONArray.getJSONObject(i).getString("GroupId"));

                placeDetail.add(placeJson);
                placeJson = null;
            }
            paramBody.put("PlaceDetail", placeDetail);
            placeDetail = null;

            //团组成员明细参数
            memberDetail = new JSONArray();
            for (int i = 0; i < tzcyJSONArray.size(); i++) {
                JSONObject memberJson = new JSONObject();
                memberJson.put("GUID", tzcyJSONArray.getJSONObject(i).getString("id"));
                memberJson.put("UserName", tzcyJSONArray.getJSONObject(i).getString("xhz") + tzcyJSONArray.getJSONObject(i).getString("mhz"));
                memberJson.put("IDNumber", tzcyJSONArray.getJSONObject(i).getString("sfzh"));
                memberJson.put("Sex", tzcyJSONArray.getJSONObject(i).getString("xb"));
                memberJson.put("Birthday", tzcyJSONArray.getJSONObject(i).getString("csnyr"));
                memberJson.put("BirthPlace", tzcyJSONArray.getJSONObject(i).getString("csds"));
                memberJson.put("Post", tzcyJSONArray.getJSONObject(i).getString("zw"));
                memberJson.put("WorkUnit", tzcyJSONArray.getJSONObject(i).getString("gzdw"));
                memberJson.put("HasPassport", tzcyJSONArray.getJSONObject(i).getString("zzqk"));
                memberJson.put("TripId", tzcyJSONArray.getJSONObject(i).getString("GroupId"));

                memberDetail.add(memberJson);
                memberJson = null;
            }
            paramBody.put("MemberDetail", memberDetail);
            memberDetail = null;

            //出国行程附件明细参数
            paramBody.put("ScheduleAtt", scheduleAtt);
            scheduleAtt = null;

            //其他附件明细参数
            JSONArray otherAtt = new JSONArray();

            if (qtfj != null && !qtfj.trim().equals("")) {
                String[] qtfjIds = qtfj.split(",");
                for (int i = 0; i < qtfjIds.length; i++) {
                    if (!qtfjIds[i].trim().equals("")) {
                        JSONObject fileJson = getFileJson(qtfjIds[i].trim());
                        if (fileJson != null) {
                            otherAtt.add(fileJson);
                        }
                    }
                }
            }

            paramBody.put("OtherAtt", otherAtt);
            otherAtt = null;

            writeLog("[YGcgjdtzToWsAction]开始调用第三个因公出国申报接口");
            writeLog("[YGcgjdtzToWsAction]paramHead=" + paramHead);
            writeLog("[YGcgjdtzToWsAction]paramBody=" + paramBody);
            result = null;
            try {
                result = new ISendMessageServiceProxy().sendMessage(paramHead.toJSONString(), paramBody.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("[YGcgjdtzToWsAction]调用第三个因公出国申报接口时出现异常：" + e.getMessage());
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用第三个因公出国申报接口返回异常: " + e.getMessage());// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            }
            writeLog("[YGcgjdtzToWsAction]result=" + result);
            writeLog("[YGcgjdtzToWsAction]第三个因公出国申报接口调用完毕");

            writeLog("[YGcgjdtzToWsAction]开始解析第三个因公出国申报接口的调用结果");
            JSONArray ygcgsbResultJson = JSONArray.parseArray(result);
            writeLog("[YGcgjdtzToWsAction]解析第三个因公出国申报接口的调用结果完成");

            if (!ygcgsbResultJson.getJSONObject(0).getString("status").trim().equals("1")) {
                //接口返回的状态表示调用失败，则阻止流程提交
                requestInfo.getRequestManager().setMessageid(
                        requestInfo.getRequestid() + "-"
                                + TimeUtil.getCurrentTimeString());// 提醒信息id
                requestInfo.getRequestManager().setMessagecontent(
                        "调用因公出国申报接口返回失败: " + ygcgsbResultJson.getJSONObject(0).getString("message"));// 提醒信息内容
                return Action.FAILURE_AND_CONTINUE;
            } else {
                writeLog("[YGcgjdtzToWsAction]调用第三个因公出国申报接口的为成功，执行状态更新语句:update " + tableName + " set ygcgsb=1,zbwsxtcgrwsbid='"+ygcgsbResultJson.getJSONObject(0).getString("FAMTripId").trim()+"' where requestid=" + requestInfo.getRequestid());
                rs.execute("update " + tableName + " set ygcgsb=1,zbwsxtcgrwsbid='"+ygcgsbResultJson.getJSONObject(0).getString("FAMTripId").trim()+"' where requestid=" + requestInfo.getRequestid());
            }
            ygcgsbResultJson = null;
            paramHead = null;
            paramBody = null;
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
