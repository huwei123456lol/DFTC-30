package com.engine.dfxm.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.taobao.api.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 *@ClassName DongfengHttpUtil
 *@Description 请说明该类的作用
 *@Author 86157
 *@Date 2024-1-10 11:01
 *@Version 1.0
 **/
public class DongfengHttpUtil implements TrustManager, X509TrustManager {

    private final static String BOUNDARY = "----WebKitFormBoundarygrBcuHVTeNQcBtqn";

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }


    public boolean isServerTrusted(X509Certificate[] certs)
    {
        return true;
    }

    public boolean isClientTrusted(X509Certificate[] certs)
    {
        return true;
    }


    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException
    {
        return;
    }


    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException
    {
        return;
    }


    public static class TrustAnyTrustManager implements X509TrustManager {
        /**
         * 该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这
         * 个方法。
         * JSSE中，默认的信任管理器类为TrustManager。
         */
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        /**
         * 该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。
         * 在实现该方法时，也可以简单的不做任何处理， 即一个空的函数体，由于不会抛出异常，它就会信任任何证书。(non-Javadoc)
         */
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        /**
         * @return 返回受信任的X509证书数组。
         */
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }

    /**
     * Description:sendJsonAndFileToHttpsPost 保存文件和文本参数接口  todo:可以访问，但显示服务器内部错误
     * @date  2023/11/14 15:06
     * @auther zhuenci
     * @param url
     * @param fileBytes
     * @param map
     * @param token
     * @return
     **/
    public static String sendJsonAndFileToHttpsPost(String url, Map<String,Object> map, byte[] fileBytes, String fileName, String token){
        System.out.println("【"+ LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求开始===================================");
        if (StringUtils.isNotBlank(fileName)) {
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                        new java.security.SecureRandom());
                System.out.println("url路径为："+url);
                URL urlObj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
                con.setSSLSocketFactory(sc.getSocketFactory());
                //conn.setRequestMethod(requestMethod);
                con.setHostnameVerifier(new TrustAnyHostnameVerifier());
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);         //不使用缓存
                con.setInstanceFollowRedirects(true);
                //con.setFixedLengthStreamingMode(filee.length());
                String boundary = "----WebKitFormBoundaryH7JEPCAd6W4ylusd";
                con.setRequestProperty("Accept", "application/json, text/plain, */*");
                con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
                con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
                con.setRequestProperty("Connection", "keep-alive");
                //con.setRequestProperty("Content-Length", "198");
                con.setRequestProperty("Host", "api.cponline.cnipa.gov.cn");
                con.setRequestProperty("Origin", "https://interactive.cponline.cnipa.gov.cn");
                con.setRequestProperty("Referer", "https://interactive.cponline.cnipa.gov.cn/");
                con.setRequestProperty("Sec-Fetch-Des", "empty");
                con.setRequestProperty("Sec-Fetch-Mode", "cors");
                con.setRequestProperty("Sec-Fetch-Site", "same-site");
                con.setRequestProperty("User-Agen", "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.35 Safari/537.36 JiSu/100.0.0.35");
                con.setRequestProperty("Authorization", token);
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                //写入输出流
                OutputStream output = new DataOutputStream(con.getOutputStream());
                // text
                if (!CollectionUtils.isEmpty(map)) {
                    StringBuffer strBuf = new StringBuffer();
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String inputName = (String) entry.getKey();
                        Object inputValue = entry.getValue();
                        if (inputValue == null) {
                            continue;
                        }
                        strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue).append("\r\n");
                    }
                    output.write(strBuf.toString().getBytes("utf-8"));
                }
                byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");
                output.write(endData);
                // 刷新
                //output.flush();
                if(null != fileBytes && fileBytes.length>0){
                    output.write(("--" + boundary + "\r\n").getBytes("utf-8"));
                    output.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n").getBytes("utf-8"));
                    output.write("Content-Type: application/octet-stream\r\n\r\n".getBytes("utf-8"));
                    output.write(fileBytes);
                    output.write("\r\n".getBytes("utf-8"));
                    output.write(("--" + boundary + "--\r\n").getBytes("utf-8"));
                    output.flush();
                }
                int responseCode = con.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                //读取输入流
                InputStream is;
                if(responseCode == 200){
                    is = con.getInputStream();
                }else{
                    is = con.getErrorStream();
                }
                if(null != is){
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer2 = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer2)) != -1) {
                        outStream.write(buffer2, 0, len);
                    }
                    is.close();
                    String s = outStream.toString(Constants.CHARSET_UTF8);
                    System.out.println("专利业务办理系统上传文件返回结果为："+s);
                    System.out.println("【"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求结束===================================");
                    return s;
                }

            } catch (Exception e){
                System.out.println("JSON数据发送失败，异常："+ e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }



    /**
     * Description:sendJsonAndFileToHttpsPost 保存文件和文本参数接口
     * @date  2023/11/14 15:06
     * @auther zhuenci
     * @param strUrl
     * @param fileParams
     * @param params
     * @param token
     * @return
     **/
    public static String sendJsonAndFileToHttpsPost(String strUrl, Map<String, Object> params, Map<String, byte[]> fileParams, String fileName, String token) {
        System.out.println("【sendJsonAndFileToHttpsPost"+ LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求开始===================================");
        System.out.println("*******sendJsonAndFileToHttpsPost参数为：\n strUrl："+strUrl +"\n params:"+params +" \n fileParams:"+fileParams+" \n");
        URL url;
        InputStream in;
        String TWO_HYPHENS = "--";
        String LINE_END = "\r\n";
        try {
            url = new URL(strUrl);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());
            System.out.println("url路径为："+url);
            //得到connection对象
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
            //conn.setRequestMethod(requestMethod);
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
            /************************************设置请求头*************************************************/
            //设置请求方式为POST
            connection.setRequestMethod("POST");
            //允许写出
            connection.setDoOutput(true);
            //允许读入
            connection.setDoInput(true);
            //不使用缓存
            connection.setUseCaches(false);
            //本次连接是否自动处理重定向(true:系统自动处理重定向；false:则需要自己从http reply中分析新的url)(置所有的http连接是否自动处理重定向:public static void HttpURLConnection.setFollowRedirects(boolean followRedirects))
            connection.setInstanceFollowRedirects(true);
            //编码格式
            connection.setRequestProperty("Charset", "utf-8");
            // 设置发送数据的格式(form-data格式)   //boundary为头部分隔符，头部拼接时需要分隔符。例如下面的有多个"Content-Disposition"拼接时需要用到此分隔符
            connection.setRequestProperty("Content-Type", "multipart/form-data ; boundary=" + BOUNDARY);
            // 设置接收数据的格式(json格式)
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Host", "api.cponline.cnipa.gov.cn");
            connection.setRequestProperty("Origin", "https://interactive.cponline.cnipa.gov.cn");
            connection.setRequestProperty("Referer", "https://interactive.cponline.cnipa.gov.cn/");
            connection.setRequestProperty("Authorization", token);
            connection.connect(); //连接
            /************************************输出流，写数据,start*************************************************/
            //获得输出流对象,此种默认post
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            StringBuffer strBufparam = new StringBuffer();
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                //封装键值对数据
                Map.Entry<String, Object> entry = (Map.Entry) it.next();
                String key = entry.getKey();
                Object value = entry.getValue();

                strBufparam.append(TWO_HYPHENS);
                strBufparam.append(BOUNDARY);
                //"--" + BOUNDARY + "\r\n"
                strBufparam.append(LINE_END);
                strBufparam.append("Content-Disposition: form-data; name=\"" + key + "\"");
                strBufparam.append(LINE_END);
                strBufparam.append(LINE_END);
                strBufparam.append(value);
                strBufparam.append(LINE_END);
            }
            out.write(strBufparam.toString().getBytes("utf-8"));
            strBufparam.toString().getBytes();
            //写入图片参数
            if (fileParams != null && fileParams.size() > 0) {
                Iterator fileIt = fileParams.entrySet().iterator();
                while (fileIt.hasNext()) {
                    Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte []>) fileIt.next();
                    //拼接文件的参数
                    StringBuffer strBufFile = new StringBuffer();
                    strBufFile.append(TWO_HYPHENS);
                    strBufFile.append(BOUNDARY);
                    strBufFile.append(LINE_END);
                    //strBufFile.append("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\"" + file.getName() + "\"");
                    // fileEntry.getKey()：文件全路径。fileName：文件名称
                    strBufFile.append("Content-Disposition: form-data; name=\"" + fileEntry.getKey() + "\"; filename=\"" + fileName + "\"");
                    strBufFile.append(LINE_END);
                    //此处很关键----文件格式
                    strBufFile.append("Content-Type: application/octet-stream");
                    strBufFile.append(LINE_END);
                    strBufFile.append(LINE_END);
                    out.write(strBufFile.toString().getBytes());
                    //文件 (此参数之前调用了本页面的重写方法getBytes(File f)，将文件转换为字节数组了 )
                    out.write(fileEntry.getValue());
                    out.write((LINE_END).getBytes());
                }
            }

            //写入标记结束位
            byte[] endData = ( TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END).getBytes();
            out.write(endData);
            out.flush();
            out.close();
            /************************************输出流，写数据完成end*************************************************/
            int code = connection.getResponseCode(); //获得响应码（200为成功返回）
            try {
                if (code == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream(); //获取响应流
                } else {
                    in = connection.getErrorStream(); //获取错误响应流
                }
            } catch (SSLException e) {
                e.printStackTrace();
                return "";
            }
            /**********读取返回的输入流信息**************/
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) != -1) {
                baout.write(buff, 0, len);
            }
            byte[]  bytes = baout.toByteArray();
            in.close();
            String ret = new String(bytes, "utf-8") ;
            System.out.println("专利业务办理系统上传文件返回结果为："+ret);
            System.out.println("【sendJsonAndFileToHttpsPost"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求结束===================================");
            return ret;
        }catch(Exception e){
            System.out.println("sendJsonAndFileToHttpsPost方法JSON数据发送失败，异常:"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Description:单层get方法参数设置
     * @date  2023/11/13 17:00
     * @auther zhuenci
     * @param url
     * @return URL
     **/

    public static URL getUrl(String url, String content,String requestMethod) throws MalformedURLException {
        String returnUrl;
        if("GET".equals(requestMethod) && StringUtils.isNotBlank(content)){
            String[] paramsInfo = content.split(",");
            String newContent = "";
            for(int i =0;i<paramsInfo.length;i++){
                String[] param = paramsInfo[i].split(":");
                if(i == 0){
                    newContent = newContent + param[0].replace("{","").replace("\"","")+"=";
                }else{
                    newContent = newContent + param[0].replace("\"","")+"=";
                }
                if(StringUtils.isNotBlank(param[1])){
                    if (i == paramsInfo.length - 1) {
                        newContent = newContent + param[1].replace("}","").replace("\"","");
                    }else{
                        newContent = newContent + param[1].replace("\"","");
                    }
                }else{
                    newContent = newContent + "";
                }
                if(i  < paramsInfo.length-1){
                    newContent = newContent + "&";
                }
            }
            returnUrl = url + "?" +newContent;
        }else{
            returnUrl = url;
        }
        System.out.println("目前地址信息为"+returnUrl);
        URL console = new URL(returnUrl);
        return console;
    }

    public static String sendDataToHttpsPost(HttpServletRequest request, String url, String token){
        System.out.println("【sendDataToHttpsPost"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求开始===================================");
        System.out.println("url路径为："+url);
        String body = null;
        try (StringWriter writer = new StringWriter()) {
            BufferedReader reader = request.getReader();
            int read;
            char[] buf = new char[1024 * 8];
            while ((read = reader.read(buf)) != -1) {
                writer.write(buf, 0, read);
            }
            body = writer.getBuffer().toString();
            System.out.println("=========================================================");
            System.out.println("请求体参数为："+body);
            System.out.println("=========================================================");
        }catch (Exception e){
            e.printStackTrace();
        }
        HttpResponse execute = HttpRequest.post(url)
                .header("Authorization", token)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Host", "api.cponline.cnipa.gov.cn")
                .header("Origin", "https://interactive.cponline.cnipa.gov.cn")
                .header("Referer", "https://interactive.cponline.cnipa.gov.cn/")
                .setSSLSocketFactory(SSLUtils.getSSLSocketFactory())
                .body(body).execute();
        if (execute.isOk()){
            String post = execute.body();
            System.out.println("=========================================================");
            System.out.println("返回结果为："+post);
            System.out.println("=========================================================");
            return post;
        }else{
            //错误状态码
            int status = execute.getStatus();
            System.out.println("=========================================================");
            System.out.println("错误状态码为："+status);
            System.out.println("=========================================================");
        }
        System.out.println("【sendDataToHttpsPost"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求结束===================================");
        return null;
    }
    public static String sendDataToHttpsGet(HttpServletRequest request,String url,String token){
        System.out.println("【sendDataToHttpsGet"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求开始===================================");
        System.out.println("url路径为："+url);
        String body = null;
        try (StringWriter writer = new StringWriter()) {
            BufferedReader reader = request.getReader();
            int read;
            char[] buf = new char[1024 * 8];
            while ((read = reader.read(buf)) != -1) {
                writer.write(buf, 0, read);
            }
            body = writer.getBuffer().toString();
            System.out.println("=========================================================");
            System.out.println("请求体参数为："+body);
            System.out.println("=========================================================");
        }catch (Exception e){
            e.printStackTrace();
        }
        HttpResponse execute = HttpRequest.get(url)
                .header("Authorization", token)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Host", "api.cponline.cnipa.gov.cn")
                .header("Origin", "https://interactive.cponline.cnipa.gov.cn")
                .header("Referer", "https://interactive.cponline.cnipa.gov.cn/")
                .setSSLSocketFactory(SSLUtils.getSSLSocketFactory())
                .body(body).execute();
        if (execute.isOk()){
            String post = execute.body();
            System.out.println("=========================================================");
            System.out.println("返回结果为："+post);
            System.out.println("=========================================================");
            return post;
        }else{
            //错误状态码
            int status = execute.getStatus();
            System.out.println("=========================================================");
            System.out.println("错误状态码为："+status);
            System.out.println("=========================================================");
        }
        System.out.println("【sendDataToHttpsGet"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求结束===================================");
        return null;
    }

    /**
     * post方式请求服务器(https协议)
     * @param url     求地址
     * @param content 参数
     * @param requestMethod 请求类型（POST，GET）
     * @param token
     * @return
     */
    public static String sendJsonToHttpsPost(String url, String content,String requestMethod,String token) {
        System.out.println("【sendJsonToHttpsPost"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求开始===================================");
        try {
            /*
             * 类HttpsURLConnection似乎并没有提供方法设置信任管理器。其实，
             * HttpsURLConnection通过SSLSocket来建立与HTTPS的安全连接
             * ，SSLSocket对象是由SSLSocketFactory生成的。
             * HttpsURLConnection提供了方法setSSLSocketFactory
             * (SSLSocketFactory)设置它使用的SSLSocketFactory对象。
             * SSLSocketFactory通过SSLContext对象来获得，在初始化SSLContext对象时，可指定信任管理器对象。
             */
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());
            URL console = getUrl(url,content,requestMethod);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setRequestMethod(requestMethod);
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setUseCaches(false);         //不使用缓存
            conn.setInstanceFollowRedirects(true);
            // 设置请求头
            conn.setRequestProperty("Accept", "application/json, text/plain, */*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Host", "api.cponline.cnipa.gov.cn");
            conn.setRequestProperty("Origin", "https://interactive.cponline.cnipa.gov.cn");
            conn.setRequestProperty("Referer", "https://interactive.cponline.cnipa.gov.cn/");
            conn.setRequestProperty("Authorization",token);
            conn.connect();
            if(StringUtils.isNotBlank(content) && "POST".equals(requestMethod)){
                //该方法会默认为POST方法
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(content.getBytes(Constants.CHARSET_UTF8));
                // 刷新、关闭
                out.flush();
                out.close();
            }
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            InputStream is;
            if(responseCode == 200){
                is = conn.getInputStream();
            }else{
                is = conn.getErrorStream();
            }
            if(null != is){
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer2 = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer2)) != -1) {
                    outStream.write(buffer2, 0, len);
                }
                is.close();
                String s = outStream.toString(Constants.CHARSET_UTF8);
                System.out.println("专利业务办理系统返回结果为："+s);
                System.out.println("【sendJsonToHttpsPost"+LocalDateTime.now()+"】==================================调用专利业务办理系统接口请求结束===================================");
                return s;
            }
        } catch (Exception e) {
            System.out.println("JSON数据发送失败，异常："+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse post360IgnoreSSL(String apiUrl, Map<String,String> header, String body,boolean ignoreSSL){
        if(ignoreSSL){
            System.setProperty("javax.net.ssl.trustStoreType", "TrustAll");
            System.setProperty("javax.net.ssl.trustStore", "dummy");
        }
        HttpRequest request= HttpUtil.createPost(apiUrl);
        for (String key: header.keySet()) {
            request.header(key,header.get(key));
        }
        request.body(body);
        HttpResponse response=request.execute();
        return response;
    }

}
