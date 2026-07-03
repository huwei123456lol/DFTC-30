package weaver.dfqcgsjszx.util.http_request;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * http接口工具类
 * @author jzm
 */
public class HttpSendUtil {
    /**
     * Http请求方法
     *
     * @param requestUrl
     *            请求网络地址
     * @param requestMethod
     *            请求方式 GET/POST
     * @param outputStr
     *            输出参数字符串
     * @return 获取到的JSON对象
     */
    public String httpRequest(String requestUrl,
                              String requestMethod, String outputStr, Map<String, String> requestPropertes) throws Exception{
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;

            if(null!=requestPropertes) {
                for (Map.Entry<String, String> entry : requestPropertes.entrySet()) {
                    String mapKey = entry.getKey();
                    String mapValue = entry.getValue();
                    conn.setRequestProperty(mapKey, mapValue);
                }
            }
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod.toUpperCase());//GET和POST必须全大写

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            byte[] b = new byte[4096];
            int len = 0;

            while ((len = is.read(b,0,b.length))!= -1) {
                buffer.append(new String(b,0,len,"UTF-8"));
            }
            //关闭流
            b = null;
            is.close();
            is = null;
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();

            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally {
            buffer = null;
        }
    }


    /**
     * Http请求方法并存储文件
     *
     * @param requestUrl
     *            请求网络地址
     * @param requestMethod
     *            请求方式 GET/POST
     * @param outputStr
     *            输出参数字符串
     * @return 获取到的JSON对象
     */
    public void httpRequestSaveFile(String requestUrl,
                                    String requestMethod, String outputStr, Map<String, String> requestPropertes,String filePath) throws Exception{
        OutputStream os = null;
        InputStream is = null;
        byte[] b = new byte[4096];
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();

            if(null!=requestPropertes) {
                for (Map.Entry<String, String> entry : requestPropertes.entrySet()) {
                    String mapKey = entry.getKey();
                    String mapValue = entry.getValue();
                    conn.setRequestProperty(mapKey, mapValue);
                }
            }
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod.toUpperCase());//GET和POST必须全大写

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //创建文件
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }

            //获取URLConnection对象对应的输入流
            os = new FileOutputStream(file);
            is = conn.getInputStream();
            int len = 0;

            while ((len = is.read(b,0,b.length))!= -1) {
                os.write(b,0,len);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally {
            //关闭流
            b = null;
            try {
                is.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                is = null;
            }
            try {
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                os = null;
            }

            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            url= null;
        }
    }

    /**
     * 调用对方接口方法
     *
     * @param path 对方或第三方提供的路径
     * @param data 向对方或第三方发送的数据，大多数情况下给对方发送JSON数据让对方解析
     */
    public static void interfaceUtil(String path, String data) {
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;

            /**设置URLConnection的参数和普通的请求属性****start***/

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            /**设置URLConnection的参数和普通的请求属性****end***/

            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("GET");//GET和POST必须全大写
            /**GET方法请求*****start*/
            /**
             * 如果只是发送GET方式请求，使用connet方法建立和远程资源之间的实际连接即可；
             * 如果发送POST方式的请求，需要获取URLConnection实例对应的输出流来发送请求参数。
             conn.connect();
             * */

            /**GET方法请求*****end*/

            /***POST方法请求****start*/

            out = new PrintWriter(conn.getOutputStream());//获取URLConnection对象对应的输出流

            out.print(data);//发送请求参数即数据

            out.flush();//缓冲数据

            /***POST方法请求****end*/

            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                str = new String(str.getBytes(), "UTF-8");//解决中文乱码问题
                System.out.println(str);
            }
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            System.out.println("完整结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)throws Exception  {
        String requestUrl = "http://101.39.230.182:11002/api/file/showImage?token="+ URLEncoder.encode("Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTU1NzA4NTgsInVzZXJfbmFtZSI6InpkaGoiLCJqdGkiOiJmM2UwZDhmYi00OThjLTQzZTAtYTYyMi0xZWZkM2M3MzQ4YmUiLCJjbGllbnRfaWQiOiJidXMtc2VhbCIsInNjb3BlIjpbImFsbCJdfQ.Ws46OWARhfffDT1lcC2at5hVzJ8QgJISsoK9QQ2UM52dwYnmkgMGzRdqZ7Z0q4cOCzOwPQDaGO6WaAvV04R9kUeIWWdfAHoWZdI6jdBmfBFVvC-Mjm_WhqaOkKV8OjeZhUOB3DmQb6c7FPzfd2HCgzYQrkZ2L5KlZw5nSpQnz1EjuLC2rsbPIacN3WJbRPrpu0J23jvwT5Gr2JQi1pkBRDG1xyVHtYOqW4Ck3F6if-KvdLz16Ddw4PecHR27ad1pcCEdULjGiODEZS4bSOAwOjBAH7T5Waa3p6uRmfdvG5WSlzybasshhoEAnO_NxdM_Ql_5b5nr73josvf1-2vvjA","utf-8")+
                "&code=192.168.1.115-da225aa3-90f6-46d5-beb7-bc947a63ffbd";
        String requestMethod = "GET";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTU1NzA4NTgsInVzZXJfbmFtZSI6InpkaGoiLCJqdGkiOiJmM2UwZDhmYi00OThjLTQzZTAtYTYyMi0xZWZkM2M3MzQ4YmUiLCJjbGllbnRfaWQiOiJidXMtc2VhbCIsInNjb3BlIjpbImFsbCJdfQ.Ws46OWARhfffDT1lcC2at5hVzJ8QgJISsoK9QQ2UM52dwYnmkgMGzRdqZ7Z0q4cOCzOwPQDaGO6WaAvV04R9kUeIWWdfAHoWZdI6jdBmfBFVvC-Mjm_WhqaOkKV8OjeZhUOB3DmQb6c7FPzfd2HCgzYQrkZ2L5KlZw5nSpQnz1EjuLC2rsbPIacN3WJbRPrpu0J23jvwT5Gr2JQi1pkBRDG1xyVHtYOqW4Ck3F6if-KvdLz16Ddw4PecHR27ad1pcCEdULjGiODEZS4bSOAwOjBAH7T5Waa3p6uRmfdvG5WSlzybasshhoEAnO_NxdM_Ql_5b5nr73josvf1-2vvjA";
        Map<String, String> requestPropertes = new HashMap<>();
        requestPropertes.put("Content-type", "application/x-www-form-urlencoded");
        requestPropertes.put("Authorization", "Bearer " + token);
        //String str = new HttpSendUtil().httpRequest(requestUrl, requestMethod, null, requestPropertes);
        new HttpSendUtil().httpRequestSaveFile(requestUrl, requestMethod, null, requestPropertes,"D:/123.jpg");
        //System.out.println("str:"+str);
    }
}