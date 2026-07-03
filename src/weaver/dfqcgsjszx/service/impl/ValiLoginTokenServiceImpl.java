package weaver.dfqcgsjszx.service.impl;

import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.service.ValiLoginTokenService;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * 用于验证跳转第三方系统时，验证用户名与令牌是否有效的Webservice接口
 * 
 * @author Alex.Du
 * 
 */
public class ValiLoginTokenServiceImpl implements ValiLoginTokenService {

	/**
	 * 验证用户名与令牌是否有效
	 */
	@Override
	public int valiLoginToken(String user, String password, String token,
							  String sysType) {
		new BaseBean().writeLog("接口验证用户名被调用 user:" + user + ",password:"
				+ password + ",token:" + token + ",sysType:" + sysType);

		RecordSet rs = new RecordSet();

		// 判断是第三方的登陆页面登陆，还是我方系统跳转第三方的登陆
		if (null == token || token.trim().equals("")) {
			// 令牌为空，当前登陆是第三方系统的登陆页面登陆
			rs.execute("select * from hrmresource where loginid='" + user
					+ "' and password='" + Util.getEncrypt(password) + "'");

			if (rs.next()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			rs.execute("select * from uf_jump_token where loginname = '" + user
					+ "' and token = '" + token + "' and systype='" + sysType
					+ "'");

			if (rs.next()) {
				//rs.execute("delete from uf_jump_token where loginname = '"
				//		+ user + "' and token = '" + token + "' and systype='"
				//		+ sysType + "'");
				return 1;
			} else {
				return 0;
			}
		}
	}

}
