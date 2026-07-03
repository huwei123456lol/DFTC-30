package weaver.dfqcgsjszx.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

/**
 * 跳转登陆到第三方系统的Servlet
 * 
 * @author Alex.Du
 * 
 */
public class JumpLoginServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();

		log.writeLog("开始集成单点登录！");

		// 获取系统当前日期和时间：yyyy-MM-dd HH:mm:ss
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()); 

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");

		// 获取第三方系统类型编号
		String sysType = request.getParameter("type");
		log.writeLog("sysType", sysType);

		// 判断第三方系统类型编号是否为空
		if (sysType == null || sysType.equals("")) {
			// 如果第三方系统类型编号为空就跳转到根目录
			request.getRequestDispatcher("/").forward(request, response);
			return;
		}

		// 获取存储在session中的登录用户对象
		User users = (User) request.getSession().getAttribute(
				"weaver_user@bean");

		// 供应商用户登录名
		String userId = users.getLoginid();
		log.writeLog("1");

		// 使用系统自带MD5对（用户登陆名+当前时间+第三方系统类型编号）加密，得到的MD5码即为token
		String token = Util.getEncrypt(userId + date + sysType);
		log.writeLog("2");

		rs.execute("insert into uf_jump_token(systype,loginname,token,cjsj) values('"
				+ sysType + "','" + userId + "','" + token + "','" + date
				+ "')");

		request.setAttribute("jumpUrl",
				new BaseBean().getPropValue("jump_url", sysType));
		request.setAttribute("jumpUserId", userId);
		request.setAttribute("token", token);
		log.writeLog("即将转发到/nky/sso_jump.jsp");
		request.getRequestDispatcher("/nky/sso_jump.jsp").forward(request,
				response);
		return;

	}
}
