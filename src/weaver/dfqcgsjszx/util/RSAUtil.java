package weaver.dfqcgsjszx.util;

import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * RSA加密/解密工具类
 * 
 * @author Alex.Du
 * 
 */
public class RSAUtil {

	public static void main(String[] args) throws Exception {

		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIX9pM2ep9nfJYW/U+MFwBhhTsbHGhy77JhGlC3uDUe1sCsZeNwBxY5dR9NvMHeZTRKsV3hJBmVzPyrq9eD8e9qR960e7ICI1ZgIj8Q2fem4r3ZTKKu3N+cZGttqlOtcwcgNg8e5ZfAI3LilZIXmCWCOqRMCvroEg0OdLaZBYSDJAgMBAAECgYB88RqW3eWYpJrBj7oyTG9mjH4/nvL7bkTdbZrJBVx+zK3cUqE/Taug59yNDPZ934Zd7jSWJkn/maqCY5wpUmZN+iDuMy7SNli9IxzV7NFgheMtczHRaYWPO29fAkDpQmFQmFzS3AerM6/JeeKHXVAwbtMMgg76zBAUR6i4i7HbcQJBANPJqKpV8lPyregI0M30H4Q+ewgwY2YP9lf7e9/XlEMmXrSEHjG5/WgrxdJThdaHFdk9AV5s1p390Q6sz8126O0CQQCh9l44DSWwGKrDbK9cscPXKwn8lRLe/qy3DsyddgXa/X7DgCUnsnxo6/n34EE9SyF/C644t3ZC/4zF670LTqfNAkAuiAz9t7pxRU1+QImX5n4SMSQY7YC5SOKH/CIAUT4q7sA6CxkTNdj+TTe+eUlbk/xx6VxmoaUZu012WvOhIfIVAkEAleHyNXrtOrbJKMrmyih0LSZWOpoWealDWrDmaH21U4CuE+v99mu1uD9Q97Rfi4KAwBqmIzScy8nEk9DiBJnIrQJAfP0KQVsHehvdWYPmopI0eEz7npwaHhYKbDi81eXLMC/1384sBnomc4IK6KVoPn79DhPHZ3KFKkGMAO8LVYGxCw==";

		String code = "Z75ICu1k5mmEElPQ9hhbDaUVXnxWwExAZUxwNqj7VSWRe5Y7RvmgFh5REWTBrtp1wjAdgJn%2FaYWfdNy4lW5GeNOP%2FP%2Bu8KnVyzvUPvEtM01jM%2Fbxr%2FTBLYoyXeJlVOpUJPBsyrH8HGB8du9ULf%2Fq1Nf8u03AOeyMlMw1f2uAli4%3D";

		System.out.println(URLDecoder.decode(code,"UTF-8"));

		String messageDe = decrypt(URLDecoder.decode(code,"UTF-8"),privateKey);
		System.out.println("还原后的字符串为:" + messageDe);


		/*
		//生成公钥和私钥
		Map<Integer,String> keyMap = genKeyPair();
		//加密字符串
		String message = "你好";
		System.out.println("随机生成的公钥为:" + keyMap.get(0));
		System.out.println("随机生成的私钥为:" + keyMap.get(1));
		String messageEn = encrypt(message,keyMap.get(0));
		System.out.println(message + "\t加密后的字符串为:" + messageEn);
		String messageDe = decrypt(messageEn,keyMap.get(1));
		System.out.println("还原后的字符串为:" + messageDe);
		*/

	}

	/**
	 * 随机生成密钥对
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<Integer,String> genKeyPair() throws NoSuchAlgorithmException {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024,new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
		// 得到私钥字符串
		String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
		// 将公钥和私钥保存到Map

		Map<Integer,String> keyMap = new HashMap<Integer,String>();

		keyMap.put(0,publicKeyString);  //0表示公钥
		keyMap.put(1,privateKeyString);  //1表示私钥

		return keyMap;
	}
	/**
	 * RSA公钥加密
	 *
	 * @param str
	 *            加密字符串
	 * @param publicKey
	 *            公钥
	 * @return 密文
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static String encrypt( String str, String publicKey ) throws Exception{
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}

	/**
	 * RSA私钥解密
	 *
	 * @param str
	 *            加密字符串
	 * @param privateKey
	 *            私钥
	 * @return 铭文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static String decrypt(String str, String privateKey) throws Exception{
		//64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}

}
