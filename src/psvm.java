
import com.alibaba.fastjson.JSONObject;


import com.weaver.general.TimeUtil;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;


/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
class RestfulDemo {


	public static void main(String[] args) {
		//RestfulDemo restfulDemo = new RestfulDemo();
		//restfulDemo.doAction();
		String date = TimeUtil.dateAdd("2023-10-10",30);
		System.out.println(date);
		new File("").getName();

	}

	/**
	 *restful接口调用案例
	 *以getModeDataPageList为例
	 */
	public void doAction(){

		CloseableHttpResponse response;// 响应类,
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//restful接口url
		HttpPost httpPost = new HttpPost("http://10.4.10.240/api/cube/restful/interface/getModeDataPageList/getGysjbxx");

		//当前日期
		String currentDate = getCurrentDate();
		//当前时间
		String currentTime = getCurrentTime();
		//获取时间戳
		String currentTimeTamp = getTimestamp();

		Map<String,Object> params = new HashMap<>();
		Map<String,Object> paramDatajson = new HashMap<>();


		//header
		Map header = new HashMap<>();

		//系统标识
		String systemid = "DI360";
		//密码
		String d_password = "D35921A371094A89B204F47E8B66DD4B";
		//封装header里的参数
		header.put("systemid",systemid);
		header.put("currentDateTime",currentTimeTamp);
		String md5Source = systemid+d_password+currentTimeTamp;
		String md5OfStr = getMD5Str(md5Source).toLowerCase();
		//Md5是：系统标识+密码+时间戳 并且md5加密的结果
		header.put("Md5",md5OfStr);
		paramDatajson.put("header",header);

		//封装pageinfo
		JSONObject pageInfo = new JSONObject();
		pageInfo.put("pageNo", 1);
		pageInfo.put("pageSize", 10);
		paramDatajson.put("pageInfo",pageInfo);

		//封装mainTable参数
		JSONObject mainTable = new JSONObject();
		//mainTable.put("gysbm", "GYS-2020-0014");
		paramDatajson.put("mainTable",mainTable);

		//封装operationinfo参数
		JSONObject operationinfo = new JSONObject();
		operationinfo.put("operator", "8189996");
		paramDatajson.put("operationinfo",operationinfo);

		System.out.println("===请求参数datajson==="+paramDatajson);
		params.put("datajson",paramDatajson);
		System.out.println("datajson :  " + JSONObject.toJSONString(params));
		//装填参数
		List nvps = new ArrayList();
		if(params!=null){
			for (Map.Entry<String,Object> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), JSONObject.toJSONString(entry.getValue())));
			}
		}
		try{
			httpPost.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			response = httpClient.execute(httpPost);
			if (response != null && response.getEntity() != null) {
				//返回信息
				String resulString = EntityUtils.toString(response.getEntity());

				//todo这里处理返回信息

				System.out.println("成功"+ resulString);


			}else{
				System.out.println("获取数据失败，请查看日志"+currentDate+" "+currentTime);
			}
		}catch (Exception e){
			System.out.println("请求失败"+currentDate+" "+currentTime+"====errormsg:"+e.getMessage());
		}


	}

	public String getMD5Str(String plainText){
		//定义一个字节数组
		byte[] secretBytes = null;
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			//对字符串进行加密
			md.update(plainText.getBytes());
			//获得加密后的数据
			secretBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			//throw new RuntimeException("没有md5这个算法！");
			//throw new RuntimeException(SystemEnv.getHtmlLabelName(517545,userLanguage));
		}
		//将加密后的数据转换为16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);
		// 如果生成数字未满32位，需要前面补0
		// 不能把变量放到循环条件，值改变之后会导致条件变化。如果生成30位 只能生成31位md5
		int tempIndex = 32 - md5code.length();
		for (int i = 0; i < tempIndex; i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	public static String getCurrentTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currenttime = (timestamp.toString()).substring(11, 13) + ":" + (timestamp.toString()).substring(14, 16) + ":"
				+ (timestamp.toString()).substring(17, 19);
		return currenttime;
	}

	public static String getCurrentDate() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-"
				+ (timestamp.toString()).substring(8, 10);
		return currentdate;
	}

	/**
	 * 获取当前日期时间。 YYYY-MM-DD HH:MM:SS
	 * @return		当前日期时间
	 */
	public static String getCurDateTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(0, 19);
	}

	/**
	 * 获取时间戳   格式如：19990101235959
	 * @return
	 */
	public static String getTimestamp(){
		return getCurDateTime().replace("-", "").replace(":", "").replace(" ", "");
	}

	public static int getIntValue(String v, int def) {
		try {
			return Integer.parseInt(v);
		} catch (Exception ex) {
			return def;
		}
	}


	public static String null2String(Object s) {
		return s == null ? "" : s.toString();

	}


}
