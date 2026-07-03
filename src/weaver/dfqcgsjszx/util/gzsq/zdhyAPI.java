package weaver.dfqcgsjszx.util.gzsq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.PrintStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import weaver.general.BaseBean;

public class zdhyAPI
{
    public static JSONObject sentData(JSONObject reqBody, String url)
            throws Exception
    {
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        RequestEntity reqst = new StringRequestEntity(reqBody.toString(), "application/json", "utf-8");
        postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

        postMethod.setRequestEntity(reqst);
        int code = client.executeMethod(postMethod);
        JSONObject res = null;
        String infotemp = postMethod.getResponseBodyAsString();
        if (code == 200)
        {
            infotemp = postMethod.getResponseBodyAsString();

            res = JSONObject.parseObject(infotemp);

            postMethod.releaseConnection();
            ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            return res;
        }
        String msg = "通讯异常" + code + infotemp;
        throw new Exception(msg);
    }

    public static void main(String[] args)
            throws Exception
    {
        JSONObject reqBody = new JSONObject();
        reqBody.put("userAccount", "8114853");
        reqBody.put("beginTime", "2019-07-01");
        reqBody.put("endTime", "2019-07-30");
        JSONObject res = sentData(reqBody, new BaseBean().getPropValue("integration_address_config","old_auth_ip"));
//        JSONObject res = sentData(reqBody, "http://10.4.10.233:8080/DFTCLeaderSchedule/mobileservice/seachMeeting.action");
        JSONArray mtList = res.getJSONArray("data");


        JSONArray datas = new JSONArray();
        int _start = 1;
        int _end = 11;
        for (int i = _start - 1; i < _end - 1; i++)
        {
            JSONObject srcData = mtList.getJSONObject(i);
            JSONObject desData = new JSONObject();
            desData.put("id", srcData.getString("id"));
            desData.put("hyyt", srcData.getString("content"));
            desData.put("hydd", srcData.getString("address") + srcData.getString("room"));
            desData.put("ksrq", srcData.getString("beginTime").substring(0, 10));
            desData.put("kssj", srcData.getString("beginTime").substring(11, 19));
            desData.put("jsrq", srcData.getString("endTime").substring(0, 10));
            desData.put("jssj", srcData.getString("endTime").substring(11, 19));
            desData.put("chld", srcData.getString("attendee"));
            desData.put("hylx", srcData.getString("type"));
            desData.put("zbf", srcData.getString("unit"));
            desData.put("bz", srcData.getString("other"));
            desData.put("oracle_rownum", Integer.valueOf(i + 1));
            desData.put("my_rownum", Integer.valueOf(i + 1));
            datas.add(desData);
        }
        System.out.println(datas.toString());
        JSONObject outJson = new JSONObject();
        outJson.put("datas", datas);
        outJson.put("totalSize", Integer.valueOf(mtList.size()));
    }
}
