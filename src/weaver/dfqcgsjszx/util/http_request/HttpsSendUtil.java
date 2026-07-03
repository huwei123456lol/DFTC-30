package weaver.dfqcgsjszx.util.http_request;

import weaver.general.BaseBean;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Http接口工具类
 * @author Alex.Du
 */
public class HttpsSendUtil {

    /**
     * Https请求方法
     *
     * @param requestUrl
     *            请求网络地址
     * @param requestMethod
     *            请求方式 GET/POST
     * @param outputStr
     *            输出参数字符串
     * @return 获取到的JSON对象
     */
    public static String httpRequest(String requestUrl,
                                     String requestMethod, String outputStr, Map<String,String> requestPropertes) throws Exception {
        new BaseBean().writeLog("开始使用https方式调用接口");
        StringBuffer buffer = new StringBuffer();
        try {

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
                    .openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            if(requestPropertes!=null) {
                Set<String> keys = requestPropertes.keySet();
                Iterator<String> keyIterator = keys.iterator();
                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    httpUrlConn.setRequestProperty(key, requestPropertes.get(key));
                }
            }

            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

            // new BaseBean().writeLog("result = " + buffer.toString());

            return buffer.toString();

        } catch (ConnectException e) {
            e.printStackTrace();
            new BaseBean().writeLog("调用接口时出现异常（1）：" + e.getMessage());
            new BaseBean().writeLog("返回的原始信息为：" + buffer.toString());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            new BaseBean().writeLog("调用接口时出现异常（2）：" + e.getMessage());
            new BaseBean().writeLog("返回的原始信息为：" + buffer.toString());
            throw e;
        }
    }
}
