package com.hhl.filter;

import weaver.conn.RecordSet;
import weaver.general.AES;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.integration.util.SessionUtil;
import weaver.session.util.RedisSessionCheck;
import weaver.session.util.RedisSessionUtil;
import weaver.weaversso.VerifyWeaverSSO;
import weaver.integration.util.SessionUtil;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class KjySsoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String usercode = Util.null2String(request.getHeader("iv-user"));

        User usr = HrmUserVarify.getUser(request,response);
        String usrLoginid = usr != null ? Util.null2String(usr.getLoginid()) : "";
        //new BaseBean().writeLog("[KjySsoFilter.java]员工code  : " + usercode);
        if(  !"".equals(usrLoginid) ){

            //new BaseBean().writeLog("[KjySsoFilter.java]员工已登录  : " + usrLoginid);

            filterChain.doFilter(request, response);

        }else if ( !"".equals(usercode) ) {

            //new BaseBean().writeLog("[KjySsoFilter.java]进入kjySSO过滤器 usercode : " + usercode);
            //new BaseBean().writeLog("[KjySsoFilter.java]进入kjySSO过滤器 usercode : " + request.getRequestURL());

            String sql = "select * from hrmresource where workcode ='" + usercode + "'";
            String logintype = "1";
            String loginid = "";
            RecordSet rs = new RecordSet();

            rs.execute(sql);
            if (rs.next()) {
                loginid = rs.getString("loginid");
            }



            HashMap<String, Object> loginInof = new HashMap<String, Object>();
            loginInof.put("accountType", "loginid");
            loginInof.put("loginType", logintype);
            loginInof.put("principalName", loginid);
            loginInof.put("customSQL", "");
            String userIdByRule = SessionUtil.getUserIdByRule(loginInof);


            if ("".equals(userIdByRule)) {
                response.getWriter().write("错误！！！ OA系统没有该账号:" + usercode + "， 请联系系统管理员处理。");
            }

            SessionUtil.createSession(userIdByRule, request, response);
            SessionUtil.setCookie(request,response);
            filterChain.doFilter(request, response);
 //           response.sendRedirect( SessionUtil.getHomePageAfterLogin(request,response));

            //员工登录并重定向
//            VerifyRtxLogin verifylogin = new VerifyRtxLogin();
//            String usercheck = null;
//            try {
//                usercheck = verifylogin.getUserCheck(request, response, loginid, password, "1", loginfile, validateCode);
//                new BaseBean().writeLog("[KjySsoFilter.java]进入kjySSO过滤器 usercheck : " + usercheck);
//                new BaseBean().writeLog("[KjySsoFilter.java]进入kjySSO过滤器 登录成功 ");
//                    response.sendRedirect("/kjyPortal/wui/index.html");
//
//
//            } catch (Exception e) {
//                new BaseBean().writeLog("[KjySsoFilter.java]异常 : " + e.getMessage());
//                e.printStackTrace();
//            }

        }else {
            //new BaseBean().writeLog("[KjySsoFilter.java]未获取员工编号 : " + usrLoginid);
            filterChain.doFilter(request, response);
        }


    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    private String getToken(String loginid){

        String result;

        String type = "ecology";

        String tempString1 = "yjcust";

        if (loginid == null || "".equals(loginid)) {
            return " has no account: !!!";

        }

        String timeString = (new Date()).getTime() + "";
        String token = AES.encrypt(loginid + "|" + timeString + "|" + type, tempString1);
        RecordSet rs = new RecordSet();
        rs.executeQuery("select * from hrmresource where loginid=? ", loginid);
        if (rs.next()) {
            rs.execute("select max(id ) maxid from sso_login_oa ");
            rs.next();
            String maxid = rs.getString("maxid");
            if ("".equals(maxid)) {
                maxid = "1";
            } else {
                maxid = Integer.valueOf(maxid) + 1 + "";
            }

            rs.executeUpdate("insert into sso_login_oa values(?,?,?,'0',?,?,'','','' )", maxid, type, loginid, timeString, token);
            result = token;
        } else {
            result = " has no account: " + loginid + "!!!";
        }

        return result;
    }

}

