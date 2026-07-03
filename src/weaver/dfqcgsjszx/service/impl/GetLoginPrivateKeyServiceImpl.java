package weaver.dfqcgsjszx.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.weaver.general.BaseBean;

import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.service.GetLoginPrivateKeyService;

/**
 * 用于获取登陆私钥的WebService
 * 
 * @author Alex.Du
 * 
 */
public class GetLoginPrivateKeyServiceImpl implements GetLoginPrivateKeyService {

	/**
	 * 获取登陆私钥
	 * 
	 * @param oldVersion
	 *            老版本的版本号
	 * @param oldPrivateKey
	 *            老版本的私钥
	 * @return 
	 *         返回JSON格式的数据，其中包含状态码（0为成功，1位失败）、错误信息（状态码为失败时，存在错误信息）、当前最新的私钥版本号、当前最新的私钥
	 *         。 例：{status:0,msg:"XXXXXX",version:127,keyValue:"XXXXXXXXXX"}
	 */
	@Override
	public String getLoginPrivateKey(long oldVersion, String oldPrivateKey) {
		new BaseBean().writeLog("开始执行获取登陆私钥的service");
		int status = 1;
		String msg = "";
		int version = 0;
		String privateKey = "";
		try {

			RecordSet rs = new RecordSet();
			// 先验证老版本的版本号及老版本的私钥是否存在
			rs.execute("select * from uf_login_key where key_version='"
					+ oldVersion + "' and private_key='" + oldPrivateKey + "'");

			if (rs.next()) {
				// 老版本的版本号及私钥存在，查询最新版本的版本号、私钥
				rs.execute("select key_version,private_key from uf_login_key order by key_version desc");

				if (rs.next()) {
					status = 0;
					version=rs.getInt("key_version");
					privateKey = rs.getString("private_key");
				}
			} else {
				new BaseBean().writeLog("未查询到对应的老版本私钥");
				status = 1;
				msg="版本信息违法，无法获取对应数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("获取登陆私钥出现异常:" + e.getMessage());
			status=1;
			msg = e.getMessage();
		}
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("status", status);
		returnMap.put("msg", msg);
		returnMap.put("version", version);
		returnMap.put("privateKey", privateKey);
		
		JSONObject jsonObject = new JSONObject(returnMap);
		
		new BaseBean().writeLog("返回的json数据为:"+jsonObject.toString());
		
		return jsonObject.toString();
	}

}
