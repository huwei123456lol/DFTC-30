package weaver.dfqcgsjszx.util;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @description：用DES对文件加密解密工具类
 * @author 楊佰鵬
 */
public class FileCryptoUtil {

    /**
     * 文件加密
     *
     * @param key      密钥
     * @param file     源文件
     * @param destFile 加密后的文件
     */
    public static void encryptFile(String key, String file, String destFile) {
        try {
            // 生成key
            DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            byte[] Keys = key.getBytes();
            IvParameterSpec iv = new IvParameterSpec(Keys);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];// 原文件长度和缓冲区大小没有关系
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件解密
     *
     * @param key  密钥
     * @param file 加密文件
     * @param dest 解密后的文件
     * @throws Exception
     */
    public static void decryptFile(String key, String file, String dest) {
        try {
            // 生成key
            DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            byte[] Keys = key.getBytes();
            IvParameterSpec iv = new IvParameterSpec(Keys);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(dest);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];// 原文件长度和缓冲区大小没有关系
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description：解析文件加密密钥
     * @author shengcai.zhou
     */
    public static String analysisKey(String encodedText) {
        String result = "";
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            //解码
            String BaseKey = new String(decoder.decode(encodedText), "UTF-8");
            //截取第16位到24位
            result = BaseKey.substring(16,24);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;//返回结果
    }

    /**
     * @description ：生成文件加密密钥
     * @author shengcai.zhou
     */
    public static String getKey() {
        //最大数量
        int  maxNum = 36;
        int i;
        //初始值
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();

        while(count < 8){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }

        return pwd.toString();
    }

    /**
     * @description：获取BASE64KEY
     * @param strKey
     */
    public static String getBASE64Key(String strKey,String strName) {
        String result = "";
        try {
            //文件名MD5加密
            String nameKey = getMD5(strName);
            //key前16位
            String firstKey =  nameKey.substring(0,16);
            //key后16位
            String afterKey =  nameKey.substring(16,32);
            //拼接key
            String key = firstKey+ strKey + afterKey;
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] textByte = key.getBytes("UTF-8");
            //编码
            result = encoder.encodeToString(textByte);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;//返回结果
    }

    /**
     * @description：获取md5加密字符串
     * @param sourceStr
     * @return
     */
    private static String getMD5(String sourceStr) {
        String result = "";
        try {
            //1.初始化MessageDigest信息摘要对象,并指定为MD5不分大小写都可以
            MessageDigest md = MessageDigest.getInstance("MD5");
            //2.传入需要计算的字符串更新摘要信息，传入的为字节数组byte[],将字符串转换为字节数组使用getBytes()方法完成
            md.update(sourceStr.getBytes());
            //3.计算信息摘要digest()方法,返回值为字节数组
            byte b[] = md.digest();
            //定义整型
            int i;
            //声明StringBuffer对象
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];//将首个元素赋值给i
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");//前面补0
                buf.append(Integer.toHexString(i));//转换成16进制编码
            }
            //转换成字符串
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;//返回结果
    }



}