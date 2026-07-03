package weaver.dfqcgsjszx.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaver.conn.RecordSet;

/**
 * 获取联想地址的Servlet
 * 
 * @author Alex.Du
 * 
 */
public class ReceptAddressServlet extends HttpServlet {
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		// 获取地址内容
		String dz = request.getParameter("dz");

		// 如果地址为空则直接回传空，并退出
		if (null == dz || dz.trim().equals("")) {
			response.getOutputStream().write("".getBytes("utf-8"));
			return;
		}

		// 查询跟该地址内容相关的地址数据，以使用次数进行降序
		RecordSet rs = new RecordSet();
		rs.execute("select dzmc from uf_dzk where dzmc like '%" + dz.trim()
				+ "%' order by sycs desc");

		// 将查询到的地址拼接起来，以|dz|进行地址分隔
		StringBuffer resultDz = new StringBuffer();
		while (rs.next()) {
			resultDz.append(rs.getString("dzmc"));
			resultDz.append("|dz|");
		}

		response.getOutputStream().write(resultDz.toString().getBytes("utf-8"));
		return;
	}
}
