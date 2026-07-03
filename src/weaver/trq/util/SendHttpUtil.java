package weaver.trq.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 发送HTTP请求工具类
 * 
 * @author Alex.Du
 * 
 */
public class SendHttpUtil {

	public static String sendHttp(String path, String params) throws Exception {
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStreamWriter out = null;

		try {
			URL url = new URL(path);
			// 打开和url之间的连接
			conn = (HttpURLConnection) url.openConnection();

			// 请求方式
			conn.setRequestMethod("GET");
			// //设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			// 最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
			// post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			// 获取URLConnection对象对应的输出流
			// out = new PrintWriter(conn.getOutputStream());
			out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");

			// 发送请求参数即数据
			// out.print(data);
			out.write(params.toString());
			// 缓冲数据
			out.flush();
			// 获取URLConnection对象对应的输入流
			is = conn.getInputStream();
			// 构造一个字符流缓存
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String str = "";
			while ((str = br.readLine()) != null) {
				return str;

			}
		} finally {
			// 关闭流
			is.close();
			out.close();
			// 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
			// 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
			conn.disconnect();

		}
		return null;
	}
}
