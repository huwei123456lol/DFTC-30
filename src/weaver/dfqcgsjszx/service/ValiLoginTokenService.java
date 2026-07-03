package weaver.dfqcgsjszx.service;

/**
 * 用于验证跳转第三方系统时，验证用户名与令牌是否有效的Webservice接口
 * 
 * @author peixuan
 * 
 */
public interface ValiLoginTokenService {

	/**
	 * 验证用户名与令牌是否有效
	 * 
	 * @param user
	 *            第三方系统的用户名
	 * @param token
	 *            第三方系统该用户的登录令牌
	 * @param sysType
	 *            第三方系统标识
	 * @return
	 */
	/**
	 * 验证登陆是否有效
	 * @param user 用户名
	 * @param password 密码（MD5密文）
	 * @param token	登录令牌
	 * @param sysType 第三方系统标识
	 * @return
	 */
	public int valiLoginToken(String user, String password, String token,
			String sysType);
}
