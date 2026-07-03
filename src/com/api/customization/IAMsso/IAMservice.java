package com.api.customization.IAMsso;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;

/**
 * @author hanjun
 */

@Path("/IAMservice")
public class IAMservice {

    @POST
    @Path("/checkToken")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String checkToken(String param) throws JSONException {
        JSONObject req = JSONObject.parseObject(param);
        JSONObject result = new JSONObject();
        new BaseBean().writeLog("[222222]" + param);
        String token = req.getString("token");
        String sign = req.getString("sign");
        new BaseBean().writeLog("[222222]" + token);
        new BaseBean().writeLog("[222222]" + sign);
        long timestamp = Long.parseLong(Util.null2String(req.getString("timestamp")));
        String signStr = DigestUtils.shaHex("IAM" + timestamp);
        if(!sign.equals(signStr)){
            result.put("status","1");
            result.put("msg","签名异常");
        } else if (Math.abs(System.currentTimeMillis()/1000 - timestamp ) > 30){
            result.put("status","1");
            result.put("msg","已超时");
        }else{
            RecordSet rs = new RecordSet();
            rs.execute("select b.workcode,b.lastname from uf_IAMprivateKEY a left join hrmresource b on a.yh=b.id  where a.sy = '" + token + "'");
            if(rs.next()){
                result.put("status","0");
                result.put("msg","成功");
                result.put("employeeNum",rs.getString("workcode"));
                result.put("fullname",rs.getString("lastname"));
            }else{
                result.put("status","1");
                result.put("msg","未查询到该用户登录信息");
            }
        }

        return String.valueOf(result);
    }
    @POST
    @Path("/getToken")
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken(@Context HttpServletRequest request, @Context HttpServletResponse response) throws JSONException {
        JSONObject result = new JSONObject();

        String workcode = Util.null2String(request.getParameter("workcode"));
        Date nowDate = new Date();
        String tokenStr = nowDate.getTime() + "_" + workcode;
        String token =  stringToMd5(tokenStr);
        String uid ;
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmresource where workcode = '" + workcode + "'");
        if(rs.next()){
            uid = rs.getString("id");

            //查询当前人员在IAMtoken表中是否存在 存在则更新 不存在则插入
            RecordSet rsDt =  new RecordSet();
            rsDt.execute("select * from uf_IAMprivateKEY where yh = " + uid);

            if(rsDt.next()){
                //有数据 更新
                rsDt.execute("update uf_IAMprivateKEY set sy = '" + token + "' where yh = " + uid);
            }else {
                //无数据 插入
                rsDt.execute("insert into uf_IAMprivateKEY (yh,sy)values('" + uid + "','" + token + "') ");
            }
            result.put("workcode",workcode);
            result.put("token",token);

        }else{
            result.put("status","1");
            result.put("msg","员工不存在  49BA59ABBE56E057");
        }


        return String.valueOf(result);
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@Context HttpServletRequest request, @Context HttpServletResponse response) throws JSONException {
        JSONObject result = new JSONObject();
        User usr = HrmUserVarify.getUser(request,response);
        String appid = Util.null2String(request.getParameter("appid"));
        RecordSet rs = new RecordSet();
        String workcode = "";
        rs.execute("select workcode from hrmresource where id = "+usr.getUID());
        if(rs.next()){
            workcode = rs.getString("workcode");
        }
        Date nowDate = new Date();
        String tokenStr = nowDate.getTime() + "_" + workcode;
        String token =  stringToMd5(tokenStr);

        int uid = usr.getUID() ;
            //查询当前人员在IAMtoken表中是否存在 存在则更新 不存在则插入
            RecordSet rsDt =  new RecordSet();
            rsDt.execute("select * from uf_IAMprivateKEY where yh = " + uid);

            if(rsDt.next()){
                //有数据 更新
                rsDt.execute("update uf_IAMprivateKEY set sy = '" + token + "' where yh = " + uid);
            }else {
                //无数据 插入
                rsDt.execute("insert into uf_IAMprivateKEY (yh,sy)values('" + uid + "','" + token + "') ");
            }
            result.put("workcode",workcode);
            result.put("token",token);


        try {
            response.sendRedirect("https://iam.dfmc.com.cn:32198/idp/otherAppSso?ticket=" + token + "&companyId=COS&appId=" + appid);
            return String.valueOf(result);
        } catch (IOException e) {
            e.printStackTrace();
            return String.valueOf(result);
        }
    }

    public static String stringToMd5(String plainText) {
        byte[] secretBytes ;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code.insert(0, "0");
        }
        return md5code.toString().toUpperCase(Locale.ROOT);
    }

}
