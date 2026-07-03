package weaver.dfqcgsjszx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import weaver.general.BaseBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * post请求工具类
 *
 * @author GodWei
 * @useBY Jzm
 */
public class PostUtil extends BaseBean {

    /**
     * post请求发送XML
     *
     * @param path
     * @param params
     * @return
     */
    public Map<String, String> postXml(String path, String params) {

        Map<String, String> resultMap = new HashMap<String, String>();
        String result = "";
        try {
            String xml = new String(params.getBytes(), "UTF-8");
            // 创建url资源
            URL url = new URL(path);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);

            conn.setDoInput(true);

            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            byte[] data = xml.getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "text/html;charset=UTF-8");
            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            out.write(data);
            out.flush();
            out.close();
            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                // 请求返回的数据
                InputStream in = conn.getInputStream();

                try {
                    byte[] data1 = new byte[in.available()];
                    in.read(data1);
                    // 转成字符串
                    result = new String(data1, "UTF-8");
                } catch (Exception e) {

                    writeLog("[PostUtil.postXml] error:" + e.getMessage());
                }
            } else {
                StringBuilder sb = new StringBuilder();
                InputStream inputStream = conn.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                writeLog(sb.toString());
                writeLog("[PostUtil.postXml] path:" + path + " params:" + params + " ResponseCode:" + conn.getResponseCode());
            }

            resultMap.put("responseCode", conn.getResponseCode() + "");
            resultMap.put("result", result);
        } catch (Exception e) {
            writeLog("[PostUtil.postXml] error:" + e.getMessage());
        }

        return resultMap;

    }

    /**
     * 发送soap到ESB通过sendHead中的tradeCode字段（文档中的交易码）来判定事什么接口，接口详情请查看文档。
     *
     * @param sendHead 参数请求头
     * @param sendBody 参数请求内容
     * @return
     */
    public String sendMessageService(String sendHead, String sendBody) {
        String result = "";
        final String SERVICEADDRESS = "http://10.3.251.119:7080/ESB/sendMessageService"; //esb调用接口地址

        //拼接soap发送报文
        StringBuilder soap = new StringBuilder();
        soap.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.esb.dawnpro.com/\">");
        soap.append("<soapenv:Header/>");
        soap.append("<soapenv:Body>");
        soap.append("<ws:sendMessage>");
        soap.append("<baseParams>");
        soap.append(sendHead);
        soap.append("</baseParams>");
        soap.append("<bizParams>");
        soap.append(sendBody);
        soap.append("</bizParams>");
        soap.append("</ws:sendMessage>");
        soap.append("</soapenv:Body>");
        soap.append("</soapenv:Envelope>");

        writeLog("[Method-sendMessageService]:soap=" + soap.toString());
        Map<String, String> responseSoapMap = postXml(SERVICEADDRESS, soap.toString());
        writeLog("[Method-sendMessageService]:responseSoapMap=" + responseSoapMap);
        if (responseSoapMap.get("responseCode").equals("200")) {
            String resultXML = responseSoapMap.get("result");
            try {
                Document document = DocumentHelper.parseText(resultXML);
                JSONObject json = (JSONObject) JSON.parse(document.selectSingleNode("//return").getText());
                result = json.getString("retmsg");
                writeLog("result="+result);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
