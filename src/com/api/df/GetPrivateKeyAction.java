package com.api.df;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取秘钥
 *
 * @author Alex.Du
 */
@Path("/pkAction")
public class GetPrivateKeyAction {
    @GET
    @Path("/getPrivateKey")
    @Produces(MediaType.TEXT_PLAIN)
    /**
     * 获取
     *
     * @author Alex.Du
     * @param [request, response]
     * @return java.lang.String true为已人脸识别签到，false为未人脸识别签到
     */
    public String getPrivateKey(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //获取参数
        String sysName = request.getParameter("sysName");
        String keyVersion = request.getParameter("keyVersion");
        String keyContent = request.getParameter("keyContent");
        new BaseBean().writeLog("得到的原始keyContent:"+keyContent);
        String decodeKeyContent = "";
        try {
            decodeKeyContent = URLDecoder.decode(keyContent, "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("对keyContent进行Decode出现异常："+e.getMessage());
        }
        new BaseBean().writeLog("Decode后的keyContent:"+decodeKeyContent);

        Map<String,String> resultMap = new HashMap<String,String>();

        //验证客户系统名称参数
        if(sysName==null||sysName.trim().equals("")){
            resultMap.put("status","1");
            resultMap.put("msg","客户系统名称内容不可为空");
            resultMap.put("keyVersion","0");
            resultMap.put("keyContent","");

            return getMapJson(resultMap);
        }

        //验证秘钥版本参数
        if(keyVersion==null||keyVersion.trim().equals("")){
            resultMap.put("status","1");
            resultMap.put("msg","错误版本号");
            resultMap.put("keyVersion","0");
            resultMap.put("keyContent","");

            return getMapJson(resultMap);
        }

        //验证秘钥内容参数
        if(keyContent==null||keyContent.trim().equals("")){
            if(!keyVersion.trim().equals("0")){
                resultMap.put("status","1");
                resultMap.put("msg","错误秘钥内容");
                resultMap.put("keyVersion","0");
                resultMap.put("keyContent","");

                return getMapJson(resultMap);
            }
        }

        RecordSet rs = new RecordSet();

        //验证客户是否为有效客户，验证客户的服务器IP

        rs.execute("select * from uf_unify_login_sys where sys_name='"+sysName+"' and state=0");
        if(rs.next()){
            new BaseBean().writeLog("得到的请求方IP为："+request.getRemoteAddr());
            /*
            if(!(","+rs.getString("host_ip")+",").contains((","+request.getRemoteAddr()+","))){
                //如果该客户的请求IP不在该客户的IP白名单中
                resultMap.put("status","1");
                resultMap.put("msg","非法访问权限，不得使用本接口");
                resultMap.put("keyVersion","0");
                resultMap.put("keyContent","");

                return getMapJson(resultMap);
            }
            */
        }else{
            //没有查询到对应的客户系统
            resultMap.put("status","1");
            resultMap.put("msg","非法客户，不得使用本接口");
            resultMap.put("keyVersion","0");
            resultMap.put("keyContent","");

            return getMapJson(resultMap);
        }

        //判断客户当前秘钥版本，如果秘钥版本为0，判断数据库中，该客户系统是否没有任何秘钥产生过
        if(keyVersion.trim().equals("0")){
            rs.execute("select key_version,private_key from uf_unify_login_key where sys_name='"+sysName+"' order by key_version desc");
            if(rs.next()){
                if(!rs.getString("key_version").trim().equals("1")){
                    resultMap.put("status","1");
                    resultMap.put("msg","非法秘钥版本，请求秘钥失败");
                    resultMap.put("keyVersion","0");
                    resultMap.put("keyContent","");

                    return getMapJson(resultMap);
                }else{
                    resultMap.put("status","0");
                    resultMap.put("msg","成功");
                    resultMap.put("keyVersion",rs.getString("key_version"));
                    resultMap.put("keyContent",rs.getString("private_key"));

                    return getMapJson(resultMap);
                }
            }else{
                resultMap.put("status","1");
                resultMap.put("msg","无可用秘钥数据");
                resultMap.put("keyVersion","0");
                resultMap.put("keyContent","");

                return getMapJson(resultMap);
            }
        }else{
            //如果当前客户传递过来的秘钥版本不是0，则根据客户传递的秘钥版本和秘钥内容判断历史秘钥的真实性，存在则返回最新秘钥内容
            rs.execute("select * from uf_unify_login_key where sys_name='"+sysName+"' and key_version='"+keyVersion+"' and (private_key='"+keyContent+"' or private_key='"+decodeKeyContent+"')");
            new BaseBean().writeLog("select * from uf_unify_login_key where sys_name='"+sysName+"' and key_version='"+keyVersion+"' and (private_key='"+keyContent+"' or private_key='"+decodeKeyContent+"')");
            if(rs.next()){
                new BaseBean().writeLog("查询到了");
                rs.execute("select key_version,private_key from uf_unify_login_key where sys_name='"+sysName+"' order by key_version desc");
                rs.next();

                resultMap.put("status","0");
                resultMap.put("msg","成功");
                resultMap.put("keyVersion",rs.getString("key_version"));
                resultMap.put("keyContent",rs.getString("private_key"));

                return getMapJson(resultMap);
            }else{
                new BaseBean().writeLog("未查询到");
                resultMap.put("status","1");
                resultMap.put("msg","非法历史秘钥数据");
                resultMap.put("keyVersion","0");
                resultMap.put("keyContent","");

                return getMapJson(resultMap);
            }
        }
    }

    public String getMapJson(Map resultMap){
        JSONObject jsonObject = new JSONObject(resultMap);
        return jsonObject.toJSONString();
    }

    public static void main(String[] args) {
        String keyContent = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALHf37OGf3GVZQ0UVR9SmHG+0eAqyWdVXlbxWQhRPb26Rk+nF/KdP8SeLt8Ta8cLjiz1fNDJbt0Kp3q5wU65muUrvd3ZleqnlsON3Dy3WzvO+2/fbUGzpaNzklYl3JuXRpCWDrKA7Q9JX7CRLIWRC3Ly+poH/N1Rw8RkMWqtr7CtAgMBAAECgYEAjogJNk8JIlgHAEMRrqa9ty+j94/xkVbr4JlGsY5//d8c5kjLvMJazc+/WUuzqmzNAaO434j5yC/4YjQk/VNnFPQSHCBNsz56Xe4uNu7flUCnL3rT+3Jf+S0URy76etkKOHEIjoOlrnJ6nH4RfUZsdBXE1OVj9/LOHRK0Sy9dsfUCQQDdLA6hamMCSFiKdXwsTKWJE3qoIyqexGr0/ajPcne+AFyBzQjB+Yh3iTStzjgppXQkz3VNMdGEpUN21Kl4H7ULAkEAzeJkzjFaQq+p0ofj9AsA374pJGq0wpEXSD56yh6cWyczxpEdXsfCn3dcteQ3XEkDHlNCg3q/LOdcSgLM9YrUJwJADne2UUrDRT/0QsJiqAcvgOL8UAlU3WPd1z7Mjx1exdgCNR9zZjTrf15DSudFdzvyeAH2G2GZ4gJpgllw6IGydwJAVWYCykROXDrhBiK3uxKVWjlWd3SLZeko7x57cDsiP+0S3Np0hEv3vo2UNx5imDTwxjNi84aGpemRrmFbAr6DIQJAXSZ7aV3dhx1qWlQ+l8ne1chiFlUva7XJH8ZjKEleT3JSu482rx5ZOJPWFkTCvtUFVvOdmwI7gD0w+ziE2ydqAA==";
        try {
            keyContent = URLEncoder.encode(keyContent, "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("对keyContent进行Decode出现异常："+e.getMessage());
        }
        System.out.println(keyContent);
    }
}
