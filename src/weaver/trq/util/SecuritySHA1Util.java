package weaver.trq.util;

import java.security.MessageDigest;

import weaver.general.BaseBean;

/**
 * SHA1加密算法工具类
 * 
 * @author Alex.Du
 * 
 */
public class SecuritySHA1Util {
	/**
	 * SHA1加密
	 * @param inStr
	 * @return
	 * @throws Exception
	 */
    public static String encode(String inStr) throws Exception {
        MessageDigest md = null;
        try {
        	md = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            e.printStackTrace();
            new BaseBean().writeLog("SHA1加密算法出现异常："+e.toString());
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
