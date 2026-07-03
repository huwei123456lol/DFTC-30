package weaver.dfqcgsjszx.service;

/**
 * 用于获取登陆私钥的WebService
 * 
 * @author Alex.Du
 * 
 */
public interface GetLoginPrivateKeyService {
	/**
	 * 获取登陆私钥
	 * 
	 * @param oldVersion
	 *            之前一个老版本的版本号
	 * @param oldPrivateKey
	 *            之前一个老版本的私钥
	 * @return 返回JSON格式的数据，其中包含当前最新的私钥版本和私钥。
	 *         例：{version:"127",keyValue:"XXXXXXXXXX"}
	 */
	public String getLoginPrivateKey(long oldVersion, String oldPrivateKey);
}
