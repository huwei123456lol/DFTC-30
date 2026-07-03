package com.api.hrm.service;

import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.api.hrm.bean.HrmFieldBean;
import com.api.hrm.util.HrmFieldUtil;

import com.api.hrm.util.PasswordReuseUtil;
import com.api.hrm.util.ServiceUtil;
import com.engine.common.biz.SimpleBizLogger;
import com.engine.common.constant.BizLogOperateAuditType;
import com.engine.common.constant.BizLogOperateType;
import com.engine.common.constant.BizLogSmallType4Hrm;
import com.engine.common.constant.BizLogType;
import com.engine.common.entity.BizLogContext;
import com.engine.common.util.LogUtil;
import com.engine.common.util.ParamUtil;
import com.engine.hrm.util.HrmWeakPasswordUtil;
import com.engine.hrm.util.face.HrmFaceCheckManager;
import com.weaver.integration.ldap.sync.oa.OaSync;
import com.weaver.integration.ldap.util.AuthenticUtil;
import net.sf.json.JSONException;
import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.tydlpt.service_client.CommonServiceProxy;
import weaver.dfqcgsjszx.util.tydlpt.service_client.Result;
import weaver.file.FileUpload;
import weaver.file.Prop;
import weaver.general.*;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.common.DbFunctionUtil;
import weaver.hrm.common.Tools;
import weaver.hrm.common.pattern.PatternUtil4Hrm;
import weaver.hrm.passwordprotection.domain.HrmPasswordProtectionQuestion;
import weaver.hrm.passwordprotection.manager.HrmPasswordProtectionQuestionManager;
import weaver.hrm.passwordprotection.manager.HrmPasswordProtectionSetManager;
import weaver.hrm.passwordprotection.manager.HrmResourceManager;
import weaver.hrm.passwordprotection.manager.HrmResourceManagerManager;
import weaver.hrm.resource.ResourceComInfo;
import weaver.hrm.settings.ChgPasswdReminder;
import weaver.hrm.settings.RemindSettings;
import weaver.interfaces.hrm.HrmServiceManager;
import weaver.ldap.LdapUtil;
import weaver.rsa.security.RSA;
import weaver.rtx.OrganisationCom;
import weaver.rtx.RTXConfig;
import weaver.systeminfo.SysMaintenanceLog;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.msg.PoppupRemindInfoUtil;


/***
 * 密码设置相关
 * @author lvyi
 *
 */
public class HrmPasswordService extends BaseBean {
    private static final char separator = Util.getSeparator();

    /**
     * 获取密码相关设置
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> getPasswordSetting(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retmap = new HashMap<String, Object>();
        User user = HrmUserVarify.getUser(request, response);
        if (user == null) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(10000303, Util.getIntValue(7)));
            return retmap;
        }
        String id = Util.null2String(request.getParameter("id"));
        int userid = user.getUID();
        if (id.length() == 0) {
            id = "" + userid;
        }

        ChgPasswdReminder reminder = new ChgPasswdReminder();
        RemindSettings settings = reminder.getRemindSettings();
        String passwordComplexity = settings.getPasswordComplexity();
        int minpasslen = settings.getMinPasslen();
        retmap.put("passwordComplexity", passwordComplexity);
        retmap.put("minpasslen", minpasslen);
        Map<String, Object> otherParams = new HashMap<String, Object>();
        String title = "";
        if (passwordComplexity.equals("1")) {
            title = SystemEnv.getHtmlLabelName(24080, user.getLanguage());
        } else if (passwordComplexity.equals("2")) {
            title = SystemEnv.getHtmlLabelName(24081, user.getLanguage());
        } else if (passwordComplexity.equals("3")) {
            title = SystemEnv.getHtmlLabelName(512563, user.getLanguage());
        }
        otherParams.put("tip", title);
        otherParams.put("tipLength", "100");
        if (!passwordComplexity.equals("0")) {
            otherParams.put("passwordStrength", true);
            otherParams.put("passwordStrengthIdx", 1);
        }
        retmap.put("otherParams", otherParams);
        retmap.put("hrmId", id);
        //是否开启了RSA加密
        String openRSA = Util.null2String(Prop.getPropValue("openRSA", "isrsaopen"));
        retmap.put("openRSA", openRSA);
        return retmap;
    }

    /**
     * 验证密码是否正确
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> verifyPswd(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> retmap = new HashMap<String, Object>();
        User user = HrmUserVarify.getUser(request, response);
        String id = StringUtil.getURLDecode(request.getParameter("id"));
        if (id.length() == 0 || id == null) {
            id = "" + user.getUID();
        }
		/*验证码是否正确 start*/
        String validatecode = Util.null2String(request.getParameter("validatecode"));
        String validateRand = Util.null2String((String) request.getSession(true).getAttribute("validateRand_changePass"));
        request.getSession(true).removeAttribute("validateRand_changePass");
        if (!"".equals(validatecode.trim().toLowerCase()) && !validateRand.toLowerCase().equals(validatecode.trim().toLowerCase())) {
            retmap.put("result", "false");
			retmap.put("message", SystemEnv.getHtmlLabelName(	10000304, user.getLanguage()));
            request.getSession(true).setAttribute("isCode","true");
            return retmap;
        }
        /*验证码是否正确 end*/
		
        String password = request.getParameter("pswd");
        //是否开启了RSA加密
        String openRSA = Util.null2String(Prop.getPropValue("openRSA","isrsaopen"));
        List<String> passwordList = new ArrayList<String>();
        if("1".equals(openRSA)){
            passwordList.add(password);

            RSA rsa = new RSA();
            List<String> resultList = rsa.decryptList(request,passwordList);
            password = resultList.get(0);
        }
        request.getSession(true).setAttribute("verifyPswd", null);
        if (user == null) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(10000303, Util.getIntValue(7)));
            return retmap;
        } else {
            boolean isExsit = true;
            RecordSet rs = new RecordSet();
            String isADAccount = "";
            String loginId = "";
            String isADAccountSql = "select isADAccount,loginId from HrmResource where id = " + id;
            rs.executeSql(isADAccountSql);
            if (rs.next()) {
                isADAccount = rs.getString("isADAccount");
                loginId = rs.getString("loginId");
            }
            AuthenticUtil authenticUtil = new AuthenticUtil();
            boolean isUseLdap =authenticUtil.checkType(loginId);
            if (isUseLdap && ifEqlTarget(isADAccount, "1") && !"1".equals(id)) {
                if (!authenticUtil.checkLogin(loginId, password).equals("100")) {
                    isExsit = false;
                } else {
                    isExsit = true;
                }
            } else {

                String paramPwd = password;
                String dbSalt = PasswordUtil.getResourceSalt(id);
                String[] encrypts = PasswordUtil.encrypt(paramPwd, dbSalt);
                String pswd = encrypts[0];

                Map<String, Comparable> map = new HashMap<String, Comparable>();
                map.put("id", id);
                map.put("password", pswd);
                isExsit = new HrmResourceManager().get(map) != null;
                if (!isExsit) {
                    isExsit = new HrmResourceManagerManager().get(map) != null;
                }
            }
            try {
                if (isExsit) {
                    request.getSession(true).setAttribute("verifyPswd", user);
                    request.getSession(true).setAttribute("isExsit", String.valueOf(isExsit));
                }
                retmap.put("result", String.valueOf(isExsit));
                retmap.put("message", SystemEnv.getHtmlLabelName(386525, user.getLanguage()));
            } catch (JSONException e) {
            }
        }

        return retmap;
    }

    /**
     * 修改 密保开关
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> changePswSwitch(HttpServletRequest request, HttpServletResponse response) {
        String checked = StringUtil.getURLDecode(request.getParameter("checked"));
        //单独标识修改密保设置的保存：有值的情况下，直接保存
        String preserve = StringUtil.getURLDecode(request.getParameter("preserve"));
        if ("".equals(preserve) && checked != null && !"".equals(checked)) {
            HrmPasswordService hps = new HrmPasswordService();
            hps.verifyPswd(request, response);
        }

        User user = HrmUserVarify.getUser(request, response);
        Map<String, Object> retmap = new HashMap<String, Object>();

        if (user == null) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(10000303, Util.getIntValue(7)));
            return retmap;
        }

        String id = StringUtil.getURLDecode(request.getParameter("id"));

        if (id.length() == 0 || id == null) {
            id = "" + user.getUID();
        }

        String isCode = Util.null2String(request.getSession(true).getAttribute("isCode"));
        String isExsit = Util.null2String(request.getSession(true).getAttribute("isExsit"));
        try {
            request.getSession(true).removeAttribute("isCode");
            request.getSession(true).removeAttribute("isExsit");
        } catch (Exception e) {
            writeLog("清除isExsit失败" + e.getMessage());
        }
        if ("".equals(preserve) &&isCode.equals("true")) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(10000304, Util.getIntValue(user.getLanguage())));
            return retmap;
        }
        if ("".equals(preserve) &&!isExsit.equals("true")) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(386525, Util.getIntValue(user.getLanguage())));
            return retmap;
        }
        HrmPasswordProtectionSetManager manager = new HrmPasswordProtectionSetManager();
        manager.set(StringUtil.parseToLong(id), Boolean.valueOf(checked));

        retmap.put("result", "true");
        return retmap;
    }

    /**
     * 保存密码问题设置
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> insertQuestion(HttpServletRequest request, HttpServletResponse response) {
        User user = HrmUserVarify.getUser(request, response);
        Map<String, Object> retmap = new HashMap<String, Object>();
        if (user == null) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(10000303, Util.getIntValue(7)));
            return retmap;
        } else {
            long userid = StringUtil.parseToLong(user.getUID() + "");
            HrmPasswordProtectionQuestion bean = null;
            Map<String, HrmPasswordProtectionQuestion> qmap = new LinkedHashMap<String, HrmPasswordProtectionQuestion>();
            Enumeration enu = request.getParameterNames();
            int maxSize = 0;
            String indexs = "";
            while (enu.hasMoreElements()) {
                String paraName = StringUtil.vString(enu.nextElement());
                if (paraName.equalsIgnoreCase("userid") || paraName.equalsIgnoreCase("cmd")){
                    continue;
                }
                String[] params = paraName.split("_");
                if (params == null || params.length != 2){
                    continue;
                }
                String key = "q" + params[1];
                if (qmap.containsKey(key)) {
                    bean = qmap.get(key);
                } else {
                    bean = new HrmPasswordProtectionQuestion();
                    qmap.put(key, bean);
                    maxSize++;
                    indexs += (indexs.length() == 0 ? "" : ",") + params[1];
                }
                if (params[0].equalsIgnoreCase("question")) {
                    bean.setQuestion(StringUtil.getURLDecode(request.getParameter(paraName)));
                } else if (params[0].equalsIgnoreCase("answer")) {
                    bean.setAnswer(StringUtil.getURLDecode(request.getParameter(paraName)));
                }
            }
            HrmPasswordProtectionQuestionManager manager = new HrmPasswordProtectionQuestionManager();
            Map<String, Long> map = new HashMap<String, Long>();
            map.put("userId", userid);
            manager.delete(map);

            String[] indexArray = indexs.split(",");
            int[] iArray = new int[indexArray.length];
            for (int i = 0; i < indexArray.length; i++) {
                iArray[i] = StringUtil.parseToInt(indexArray[i]);
            }
            Arrays.sort(iArray);
            for (int i = 0; i < iArray.length; i++) {
                if (qmap.containsKey("q" + iArray[i])) {
                    bean = (HrmPasswordProtectionQuestion) qmap.get("q" + iArray[i]);
                    bean.setUserId(userid);
                    manager.insert(bean);
                }
            }
        }
        retmap.put("result", "true");
        return retmap;
    }

    /**
     * 获取问题列表
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> getPasswordQuestion(HttpServletRequest request, HttpServletResponse response) {

        User user = HrmUserVarify.getUser(request, response);
        Map<String, Object> retmap = new HashMap<String, Object>();
        HrmFieldBean hrmFieldBean = null;
        List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        Map<String, Object> table = new HashMap<String, Object>();
        List<HrmFieldBean> titles = new ArrayList<HrmFieldBean>();

        /*
         * 防止直接越过密码校验获取问题的答案
         * */
        String isExsit = Util.null2String(request.getSession(true).getAttribute("isExsit"));
        try {
            request.getSession(true).removeAttribute("isExsit");
        } catch (Exception e) {

        }

        if (!isExsit.equals("true")) {
            retmap.put("result", "false");
            retmap.put("message", SystemEnv.getHtmlLabelName(10000337, Util.getIntValue(user.getLanguage())));
            return retmap;
        }
        /*
         * end
         * */

        hrmFieldBean = new HrmFieldBean();
        hrmFieldBean.setFieldname("question");
        hrmFieldBean.setFieldlabel("24419");
        hrmFieldBean.setFieldhtmltype("1");
        hrmFieldBean.setType("1");
        hrmFieldBean.setViewAttr(3);
        hrmFieldBean.setWidth("80%");
        hrmFieldBean.setMultilang(false);
        titles.add(hrmFieldBean);

        hrmFieldBean = new HrmFieldBean();
        hrmFieldBean.setFieldname("answer");
        hrmFieldBean.setFieldlabel("24122");
        hrmFieldBean.setFieldhtmltype("1");
        hrmFieldBean.setType("1");
        hrmFieldBean.setViewAttr(3);
        hrmFieldBean.setWidth("80%");
        hrmFieldBean.setMultilang(false);
        titles.add(hrmFieldBean);

        columns = HrmFieldUtil.getHrmDetailTable(titles, null, user);
        table.put("columns", columns);

        RecordSet rs = new RecordSet();
        HrmPasswordProtectionQuestionManager questionManager = new HrmPasswordProtectionQuestionManager();
        int pStep = Tools.parseToInt(request.getParameter("pStep"), 1);
        Map<String, Comparable> map = new HashMap<String, Comparable>();
        map.put("userId", String.valueOf(user.getUID()));
        if (pStep == 2) {
            map.put("sqlorderby", rs.getDBType().equals("oracle") ? "t.id * dbms_random.value()" : "newid()");
        } else {
            map.put("sqlorderby", "t.id asc");
        }
        List list = questionManager.find(map);
        int qSize = list == null ? 0 : list.size();
        HrmPasswordProtectionQuestion question = null;
        for (int i = 0; i < qSize; i++) {
            question = (HrmPasswordProtectionQuestion) list.get(i);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("question", Util.null2String(question.getQuestion()));
            data.put("answer", Util.null2String(question.getAnswer()));
            datas.add(data);
        }
        table.put("datas", datas);
        table.put("rownum", "rownum");
        table.put("tablename", SystemEnv.getHtmlLabelName(81611, user.getLanguage()));
        retmap.put("tableinfo", table);

        return retmap;
    }


    /**
     * 修改密码
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> changePassword(HttpServletRequest request, HttpServletResponse response) {
        writeLog("排查达梦测试环境问题。。。。回头删除");
        Map<String, Object> retmap = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            HttpSession session = ((HttpServletRequest) request).getSession(true);
            RecordSet rs = new RecordSet();
            ResourceComInfo ResourceComInfo = new ResourceComInfo();
            SysMaintenanceLog SysMaintenanceLog = new SysMaintenanceLog();
            PoppupRemindInfoUtil PoppupRemindInfoUtil = new PoppupRemindInfoUtil();
            RTXConfig RTXConfig = new RTXConfig();
            OrganisationCom OrganisationCom = new OrganisationCom();
            HrmServiceManager HrmServiceManager = new HrmServiceManager();
            int userid = user.getUID();

            FileUpload fu = new FileUpload(request);
            String logintype = user.getLogintype();     //当前用户类型  1: 类别用户  2:外部用户
            String id = Util.null2String(fu.getParameter("id"));
            String from = Util.null2String(request.getParameter("from"));
            if (id.length() == 0) {
                id = "" + user.getUID();
            }
            if (user.getUID() == 1 && from.equals("doc")) {//公文交换维度管理员可以修改其他人密码

            } else {
                if ("2".equals(logintype) || !id.equals(String.valueOf(userid))) {
                    retmap.put("status", "-1");
                    retmap.put("message", SystemEnv.getHtmlLabelName(22620, user.getLanguage()));
                    return retmap;
                }
            }

            //旧密码
            String oldPassword = fu.getParameter("passwordold");
            //新密码
            String newPassword = fu.getParameter("passwordnew");
            //是否开启了RSA加密
            boolean isOpenRSA = "1".equals(Prop.getPropValue("openRSA","isrsaopen"));
            List<String> passwordList = new ArrayList<String>();
            if (isOpenRSA) {
                passwordList.add(oldPassword);
                passwordList.add(newPassword);

                RSA rsa = new RSA();
                List<String> resultList = rsa.decryptList(request,passwordList) ;
                oldPassword = resultList.get(0);
                newPassword = resultList.get(1);
            }

            /*验证码是否正确 start*/
            String validatecode = Util.null2String(request.getParameter("validatecode"));
            String validateRand = Util.null2String((String) request.getSession(true).getAttribute("validateRand_changePass"));
            request.getSession(true).removeAttribute("validateRand_changePass");
            if (!validateRand.toLowerCase().equals(validatecode.trim().toLowerCase()) || "".equals(validatecode.trim().toLowerCase())) {
                retmap.put("message", SystemEnv.getHtmlLabelName(10000304, Util.getIntValue(user.getLanguage())));
                retmap.put("status", "-1");
                return retmap;
            }
            /*验证码是否正确 end*/

            SimpleBizLogger logger = new SimpleBizLogger();
            Map<String, Object> params = ParamUtil.request2Map(request);
            BizLogContext bizLogContext = new BizLogContext();
            bizLogContext.setDateObject(new Date());
            bizLogContext.setUserid(user.getUID());
            bizLogContext.setUsertype(Util.getIntValue(user.getLogintype()));
            bizLogContext.setLogType(BizLogType.HRM);//模块类型
            //bizLogContext.setBelongType(BizLogSmallType4Hrm.HRM_RSOURCE_CARD);//所属大类型
            bizLogContext.setLogSmallType(BizLogSmallType4Hrm.HRM_PASSWPRD);//当前小类型
            bizLogContext.setOperateType(BizLogOperateType.UPDATE);//更新
            bizLogContext.setOperateAuditType(BizLogOperateAuditType.WARNING);//警告
            bizLogContext.setTargetId(id);
            bizLogContext.setTargetName(ResourceComInfo.getLastname(id));
            bizLogContext.setParams(params);//当前request请求参数
            bizLogContext.setClientIp(Util.getIpAddr(request));
            logger.setUser(user);//当前操作人
//			String mainSql = "select * from HrmResource where id="+id;
//			logger.setMainSql(mainSql,"id");//主表sql
//			logger.setMainPrimarykey("id");//主日志表唯一key
//			logger.setMainTargetNameColumn("lastname");//当前targetName对应的列（对应日志中的对象名）
//			logger.before(bizLogContext);//写入操作前日志

            PoppupRemindInfoUtil.updatePoppupRemindInfo(userid, 6, (logintype).equals("1") ? "0" : "1", -1);


            String dbSalt = PasswordUtil.getResourceSalt(id);
            String[] oldencrypts = PasswordUtil.encrypt(Util.null2String(oldPassword), dbSalt);
            String passwordold = oldencrypts[0];
            String[] newEncrypts = PasswordUtil.encrypt(Util.null2String(newPassword));
            String passwordnew = newEncrypts[0];
            //用于判断密码重复次数
            String passwordnewtemp = PasswordUtil.encrypt(Util.null2String(newPassword), dbSalt)[0];
            String newSalt = newEncrypts[1];

            String passwordnew1 = Util.null2String(newPassword);
            ChgPasswdReminder reminder = new ChgPasswdReminder();
            RemindSettings settings1 = reminder.getRemindSettings();

            //判断是否开启了【禁止弱密码保存】
            String weakPasswordDisable = Util.null2s(settings1.getWeakPasswordDisable(), "0");
            if (weakPasswordDisable.equals("1")) {
                //判断是否为弱密码
                HrmWeakPasswordUtil hrmWeakPasswordUtil = new HrmWeakPasswordUtil();
                if (hrmWeakPasswordUtil.isWeakPsd(passwordnew1)) {
                    retmap.put("status", "-1");
                    retmap.put("message", SystemEnv.getHtmlLabelName(515420, user.getLanguage()));
                    return retmap;
                }
            }
            writeLog("测试修改密码"+"1".equals(settings1.getPasswordReuse()));
            // 校验此次密码是否和前几天密码一致
            if("1".equals(settings1.getPasswordReuse())){
                if (PasswordReuseUtil.isPasswordReuse(settings1.getPasswordReuseNum(), user.getLoginid(), passwordnewtemp)) {
                    // 存在和前几次密码重复
                    retmap.put("status", "-1");
                    String messageTip = SystemEnv.getHtmlLabelName(526455, user.getLanguage())+settings1.getPasswordReuseNum()+SystemEnv.getHtmlLabelName(526456, user.getLanguage());
                    retmap.put("message",messageTip);
                    return retmap;
                }
            }
            String passwordComplexity = settings1.getPasswordComplexity();
            int minpasslen = settings1.getMinPasslen();
            if (passwordnew1.length() < minpasslen) {
                retmap.put("status", "-1");
                retmap.put("message", SystemEnv.getHtmlLabelName(20172, user.getLanguage()) + minpasslen);
                return retmap;
            }

            if ("1".equals(passwordComplexity)) {
                if (!PatternUtil4Hrm.isPasswordComplexity1(passwordnew1)) {
                    retmap.put("status", "-1");
                    retmap.put("message", SystemEnv.getHtmlLabelName(	512768, user.getLanguage()) + minpasslen);
                    return retmap;
                }
            } else if ("2".equals(passwordComplexity)) {
                if (!PatternUtil4Hrm.isPasswordComplexity2(passwordnew1)) {
                    retmap.put("status", "-1");
                    retmap.put("message", SystemEnv.getHtmlLabelName(512769, user.getLanguage()) + minpasslen);
                    return retmap;
                }
            } else if ("3".equals(passwordComplexity)) {
                if (!PatternUtil4Hrm.isPasswordComplexity3(passwordnew1)) {
                    retmap.put("status", "-1");
                    retmap.put("message", SystemEnv.getHtmlLabelName(512767, user.getLanguage()));
                    return retmap;
                }
            }
            RecordSet rs_ad = new RecordSet();
            String isADAccount = "";
            String isADAccountSql = "select isADAccount from HrmResource where id = " + id;
            rs_ad.executeSql(isADAccountSql);
            if (rs_ad.next()) {
              isADAccount = rs_ad.getString("isADAccount");
            }
            boolean isExcSuccess = false;
            String passwdchgdate = Util.null2String(TimeUtil.getCurrentDateString());
            if (ifEqlTarget(isADAccount, "1") && !"1".equals(id) && new AuthenticUtil().checkType(ResourceComInfo.getLoginID(id))) {
                Map<String, String> map = new HashMap<>();
                map.put("userid", id);//OA人员id
                map.put("loginid", ResourceComInfo.getLoginID(id));//人员帐号
                map.put("oldPassword", Util.null2String(oldPassword));//旧密码。当issysadmin为1时，必须要传入。
                map.put("password", Util.null2String(newPassword));//新密码
                map.put("issysadmin", Boolean.toString(ServiceUtil.isAdmin("" + user.getUID())));//是否需要检验旧密码。0，是检验。1,不需要检验。
                String optype=Util.null2String(fu.getParameter("optype")); //1，强制修改密码操作。2，首次登录密码操作，3，忘记密码找回。其它，系统修改ad操作。
                map.put("optype", optype);
                Map<String,Object>  retInfo = new OaSync("", "").modifyADPWDNew(map);
                if (Util.null2String(retInfo.get("code")).equals("0")){
                    retmap.put("status", "1");
                    retmap.put("message", SystemEnv.getHtmlLabelName(16092, user.getLanguage()));
                    session.setAttribute("password", passwordnew1);
                    rs.executeUpdate("update hrmresource set passwdchgdate=?, haschangepwd='y' where id = ?", passwdchgdate, userid);
                    return retmap;
                } else {
                    retmap.put("message", SystemEnv.getHtmlLabelNames(Util.null2String(retInfo.get("msg")), user.getLanguage()));
                    retmap.put("status", "-1");
                    return retmap;
                }
            }

            Result ptResult = null;

            if (user.getUID() == 1 && from.equals("doc")) {
                String sql = " update hrmresource set passwdchgdate=?, haschangepwd='y', password = ? where id = ? ";
                isExcSuccess = rs.executeUpdate(sql, passwdchgdate, passwordnew, id);
                PasswordUtil.updateResourceSalt(id, newSalt);
                user.setPwd(passwordnew);
                session.setAttribute("weaver_user@bean", user);
                //BBS集成相关
                String bbsLingUrl = new BaseBean().getPropValue(GCONST.getConfigFile(), "ecologybbs.linkUrl");
                if (!bbsLingUrl.equals("")) {
                    new Thread(new weaver.bbs.BBSRunnable(user.getLoginid() + "", passwordnew)).start();
                }
            } else {
                //调用第3方接口修改密码start      金喆铭修改
                if(user.getUserDepartment()!=1) {
                    ptResult = new CommonServiceProxy().modifyPassword(user.getLoginid(), oldPassword, newPassword);
                    new BaseBean().writeLog("[HrmPasswordService]ptResult-getReturnCode:" + ptResult.getReturnCode());
                    new BaseBean().writeLog("[HrmPasswordService]ptResult-getDescription:" + ptResult.getDescription());
                    new BaseBean().writeLog("[HrmPasswordService]ptResult-getJsonString:" + ptResult.getJsonString());
                    isExcSuccess = true;
                }
                //调用第3方接口修改密码end


                String procedurepara = id + separator + passwordold + separator + passwordnew;
                rs.executeProc("HrmResource_UpdatePassword", procedurepara);
                if (rs.next()) {
                    if (!rs.getString(1).equals("2")) {
                        isExcSuccess = true;
                        PasswordUtil.updateResourceSalt(id, newSalt);
                    }

                    user.setPwd(passwordnew);
                    session.setAttribute("weaver_user@bean", user);
                    //BBS集成相关
                    String bbsLingUrl = new BaseBean().getPropValue(GCONST.getConfigFile(), "ecologybbs.linkUrl");
                    if (!bbsLingUrl.equals("")) {
                        new Thread(new weaver.bbs.BBSRunnable(user.getLoginid() + "", passwordnew)).start();
                    }
                }
            }

            writeLog("执行PasswordReuseUtil.saveHistoryPassword前11"+SystemEnv.getHtmlLabelName(16092, user.getLanguage()));
            RTXConfig rtxConfig = new RTXConfig();
            String RtxOrElinkType = (Util.null2String(rtxConfig.getPorp(RTXConfig.RtxOrElinkType))).toUpperCase();
            //修改密码同步到ELINK中
            if ("ELINK".equals(RtxOrElinkType)) {
                OrganisationCom.editUser(Integer.parseInt(id));
            }

            LogUtil.writeBizLog(bizLogContext);

            // 改为自进行修正
            ResourceComInfo.updateResourceInfoCache(id);

            //OA与第三方接口单条数据同步方法开始
            HrmServiceManager.SynInstantHrmResource(id, "2");
            //OA与第三方接口单条数据同步方法结束

            rs.execute("update HrmResource set " + DbFunctionUtil.getUpdateSetSql(rs.getDBType(), user.getUID()) + " where id=" + id);
            rs.execute("update HrmResourceManager set " + DbFunctionUtil.getUpdateSetSql(rs.getDBType(), user.getUID()) + " where id=" + id);

            try{
                //登录信息签名
                PasswordUtil.saveSign(""+id);
            }catch (Exception e){
                writeLog(e);
            }
            if (isExcSuccess) {
                HrmFaceCheckManager.setUserPassowrd(id, passwordnew1);
                HrmFaceCheckManager.sync(id, HrmFaceCheckManager.getOptUpdate(), "hrmPasswordService_save", HrmFaceCheckManager.getOaResource());
            }

            if (rs.getString(1).equals("2")) {
                retmap.put("status", "2");
                retmap.put("message", SystemEnv.getHtmlNoteName(17, user.getLanguage()));
            } else {
                retmap.put("status", "1");
                retmap.put("message", SystemEnv.getHtmlLabelName(16092, user.getLanguage()));
                session.setAttribute("password", passwordnew1);
                //rs.executeSql("update HrmSysMaintenanceLog set operatedesc='y' where relatedid = "+userid+" and  id = (select MAX(id) from HrmSysMaintenanceLog where relatedid = "+userid+" and operatetype = 6 and operateitem = 60)");
                rs.executeUpdate("update hrmresource set passwdchgdate=?, haschangepwd='y' where id = ?", passwdchgdate, userid);
                // 密码修改成功 保存密码到历史记录
                writeLog("执行PasswordReuseUtil.saveHistoryPassword前"+SystemEnv.getHtmlLabelName(16092, user.getLanguage()));
                PasswordReuseUtil.saveHistoryPassword(user.getLoginid(), passwordnew);
                writeLog("执行PasswordReuseUtil.saveHistoryPassword后");
            }
            if(retmap.get("status").equals("1")){
                try{
                    //修改密码成功调用下线接口
                    ServiceUtil serviceUtil = new ServiceUtil();
                    ServletContext servletContext = session.getServletContext();
                    //下线EM
                    serviceUtil.emOffline(id);
                    //下线PC
                    serviceUtil.offLine4PC(id,servletContext);
                }catch (Exception e) {
                    writeLog("修改个人密码处修改密码之后调用人员下线出现异常：" + e);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            writeLog(e);
            retmap.put("status", "-1");
        }
        return retmap;
    }

    public static boolean ifEqlTarget(String val, String target) {
        if (val == null || val.equals("")) {
            return false;
        }
        if (!val.equals(target)) {
            return false;
        }
        return true;
    }


    /*
     *
     * 验证是否设置了密保问题  以及  是否启用密保
     */


    public Map<String, Object> IsSettedQuestion(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retmap = new HashMap<String, Object>();

        try {

            boolean flog = false;
            User user = HrmUserVarify.getUser(request, response);
            int uid = user.getUID();
            RecordSet rs = new RecordSet();
            String sql = "SELECT * FROM hrm_protection_question WHERE user_id=" + uid + "";
            rs.executeSql(sql);

            if (rs.next()) {

                flog = true;
                boolean secStatus = false;
                sql = "select * from  hrm_password_protection_set  where  user_id  =" + uid + "  ";
                rs.executeSql(sql);
                rs.first();
                if (rs.getInt(3) == 1) {
                    secStatus = true;
                }
                retmap.put("secStatus", secStatus);
            }
            retmap.put("isSetted", flog);
            retmap.put("status", 1);
            //判断是否开启了RSA加密
            String openRSA = Util.null2String(Prop.getPropValue("openRSA","isrsaopen"));
            retmap.put("openRSA",openRSA);
        } catch (Exception e) {
            // TODO: handle exception
            writeLog("判断是否设置密码失败 " + e.getMessage());
            retmap.put("message", e.getMessage());
            retmap.put("status", -1);
        }
        return retmap;
    }

}
