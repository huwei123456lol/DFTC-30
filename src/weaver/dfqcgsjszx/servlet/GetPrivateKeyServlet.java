package weaver.dfqcgsjszx.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取秘钥的Servlet
 * 
 * @author Alex.Du
 * 
 */
public class GetPrivateKeyServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");

		// 获取参数
		String sysName = request.getParameter("sysName");
		String keyVersion = request.getParameter("keyVersion");
		String keyContent = request.getParameter("keyContent");

		System.out.println("得到的原始keyContent:" + keyContent);

		String decodeKeyContent = "";
		try {
			decodeKeyContent = URLDecoder.decode(keyContent, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("对keyContent进行Decode出现异常：" + e.getMessage());
		}
		System.out.println("Decode后的keyContent:" + decodeKeyContent);

		Map<String, String> resultMap = new HashMap<String, String>();

		// 验证客户系统名称参数
		if (sysName == null || sysName.trim().equals("")) {
			resultMap.put("status", "1");
			resultMap.put("msg", "客户系统名称内容不可为空");
			resultMap.put("keyVersion", "0");
			resultMap.put("keyContent", "");

			response.getOutputStream().write(
					getMapJson(resultMap).getBytes("UTF-8"));
			return;
		}

		// 验证秘钥版本参数
		if (keyVersion == null || keyVersion.trim().equals("")) {
			resultMap.put("status", "1");
			resultMap.put("msg", "错误版本号");
			resultMap.put("keyVersion", "0");
			resultMap.put("keyContent", "");

			response.getOutputStream().write(
					getMapJson(resultMap).getBytes("UTF-8"));
			return;
		}

		// 验证秘钥内容参数
		if (keyContent == null || keyContent.trim().equals("")) {
			if (!keyVersion.trim().equals("0")) {
				resultMap.put("status", "1");
				resultMap.put("msg", "错误秘钥内容");
				resultMap.put("keyVersion", "0");
				resultMap.put("keyContent", "");

				response.getOutputStream().write(
						getMapJson(resultMap).getBytes("UTF-8"));
				return;
			}
		}

		// 1.加载驱动程序
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// 获取数据库连接
			//正式环境
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@10.4.3.7:1521/dfmc", "ecology", "ecology");
			//测试环境
			conn = DriverManager.getConnection("jdbc:oracle:thin:@10.4.10.242:1521:orcl", "ecology", "ecology");

			// 验证客户是否为有效客户，验证客户的服务器IP
			ps = conn
					.prepareStatement("select * from uf_unify_login_sys where sys_name=? and state=0");
			ps.setString(1, sysName);
			rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("得到的请求方IP为：" + request.getRemoteAddr());
				/*
				 * if(!(","+rs.getString("host_ip")+",").contains((","+request.
				 * getRemoteAddr()+","))){ //如果该客户的请求IP不在该客户的IP白名单中
				 * resultMap.put("status","1");
				 * resultMap.put("msg","非法访问权限，不得使用本接口");
				 * resultMap.put("keyVersion","0");
				 * resultMap.put("keyContent","");
				 * 
				 * return getMapJson(resultMap); }
				 */
			} else {
				// 没有查询到对应的客户系统
				resultMap.put("status", "1");
				resultMap.put("msg", "非法客户，不得使用本接口");
				resultMap.put("keyVersion", "0");
				resultMap.put("keyContent", "");

				response.getOutputStream().write(
						getMapJson(resultMap).getBytes("UTF-8"));
				return;
			}

			// 判断客户当前秘钥版本，如果秘钥版本为0，判断数据库中，该客户系统是否没有任何秘钥产生过
			if (keyVersion.trim().equals("0")) {
				ps = conn
						.prepareStatement("select key_version,private_key from uf_unify_login_key where sys_name=? order by key_version desc");
				ps.setString(1, sysName);
				rs = ps.executeQuery();
				if (rs.next()) {
					if (!rs.getString("key_version").trim().equals("1")) {
						resultMap.put("status", "1");
						resultMap.put("msg", "非法秘钥版本，请求秘钥失败");
						resultMap.put("keyVersion", "0");
						resultMap.put("keyContent", "");

						response.getOutputStream().write(
								getMapJson(resultMap).getBytes("UTF-8"));
						return;
					} else {
						resultMap.put("status", "0");
						resultMap.put("msg", "成功");
						resultMap
								.put("keyVersion", rs.getString("key_version"));
						resultMap
								.put("keyContent", rs.getString("private_key"));

						response.getOutputStream().write(
								getMapJson(resultMap).getBytes("UTF-8"));
						return;
					}
				} else {
					resultMap.put("status", "1");
					resultMap.put("msg", "无可用秘钥数据");
					resultMap.put("keyVersion", "0");
					resultMap.put("keyContent", "");

					response.getOutputStream().write(
							getMapJson(resultMap).getBytes("UTF-8"));
					return;
				}
			} else {
				// 如果当前客户传递过来的秘钥版本不是0，则根据客户传递的秘钥版本和秘钥内容判断历史秘钥的真实性，存在则返回最新秘钥内容
				ps = conn
						.prepareStatement("select * from uf_unify_login_key where sys_name=? and key_version=? and (private_key=? or private_key=?)");
				ps.setString(1, sysName);
				ps.setString(2, keyVersion);
				ps.setString(3, keyContent);
				ps.setString(4, decodeKeyContent);
				rs = ps.executeQuery();
				
				System.out.println("select * from uf_unify_login_key where sys_name='"
								+ sysName
								+ "' and key_version='"
								+ keyVersion
								+ "' and (private_key='"
								+ keyContent
								+ "' or private_key='"
								+ decodeKeyContent
								+ "')");
				if (rs.next()) {
					System.out.println("查询到了");
					
					ps = conn
							.prepareStatement("select key_version,private_key from uf_unify_login_key where sys_name=? order by key_version desc");
					ps.setString(1, sysName);
					rs = ps.executeQuery();
					rs.next();

					resultMap.put("status", "0");
					resultMap.put("msg", "成功");
					resultMap.put("keyVersion", rs.getString("key_version"));
					resultMap.put("keyContent", rs.getString("private_key"));

					response.getOutputStream().write(
							getMapJson(resultMap).getBytes("UTF-8"));
					return;
				} else {
					System.out.println("未查询到");
					resultMap.put("status", "1");
					resultMap.put("msg", "非法历史秘钥数据");
					resultMap.put("keyVersion", "0");
					resultMap.put("keyContent", "");

					response.getOutputStream().write(
							getMapJson(resultMap).getBytes("UTF-8"));
					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "1");
			resultMap.put("msg", "服务器出现异常("+e.getMessage()+")");
			resultMap.put("keyVersion", "0");
			resultMap.put("keyContent", "");

			response.getOutputStream().write(
					getMapJson(resultMap).getBytes("UTF-8"));
			return;
		} finally {
			// 5.关闭资源
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					ps = null;
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn = null;
				}
			}
		}
		
	}

	public String getMapJson(Map resultMap) {
		JSONObject jsonObject = new JSONObject(resultMap);
		return jsonObject.toJSONString();
	}
}
