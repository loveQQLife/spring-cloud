package com.kreken.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * AES 加解密工具
 *
 */
public class AesUtil {

	final static String COMMON_KEY = "ZJXXTEncry201112";

	/**
	 * 
	 * 方法：加密
	 * 
	 * @param sSrc
	 * @return
	 * @throws Exception
	 * 
	 *             Add By Ethan Lam At 2011-12-27
	 */
	public static String Encrypt(String sSrc) throws Exception {
		return Encrypt(sSrc, COMMON_KEY);
	}

	/**
	 * 
	 * 方法：解密
	 * 
	 * @param sSrc
	 * @return
	 * @throws Exception
	 * 
	 *             Add By Ethan Lam At 2011-12-27
	 */
	public static String Decrypt(String sSrc) throws Exception {
		return Decrypt(sSrc, COMMON_KEY);
	}

	/**
	 * 加密
	 * 
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	private static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());

		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	/**
	 * 获得加密后的口令
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String getEncryptedPwd(String password) throws Exception {
		return Encrypt(password);
	}

	/**
	 * 验证口令是否合法
	 * 
	 * @param password
	 * @param passwordInDb
	 * @return
	 * @throws Exception
	 */
	public static boolean validPassword(String password, String passwordInDb) throws Exception {
		if (password == null || "".equals(password))
			return false;
		String encryptStr = Encrypt(password);
		if (encryptStr.equals(passwordInDb))
			return true;
		else
			return false;
	}

	/**
	 * 解密
	 * 
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String Decrypt(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
		 * 此处使用AES-128-CBC加密模式，key需要为16位。
		 */
		String cKey = "1234567890123456";
		// 需要加密的字串
		String cSrc = "Email : arix04@xxx.com";
		System.out.println(cSrc);
		// 加密
		long lStart = System.currentTimeMillis();
		String enString = AesUtil.Encrypt(cSrc);
		System.out.println("加密后的字串是：" + enString);

		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("加密耗时：" + lUseTime + "毫秒");
		// 解密
		lStart = System.currentTimeMillis();
		String DeString = AesUtil.Decrypt(enString);
		System.out.println("解密后的字串是：" + DeString);
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("解密耗时：" + lUseTime + "毫秒");

		String ts = "aa12345678";
		String ec = getEncryptedPwd(ts);
		String ecStr = "iv001TNy/tiNooJg6sXuPg==";

		System.out.println(ecStr + "解密为：" + Decrypt(ecStr));
		System.out.println("口令“" + ts + "”的加密结果：" + ec);
		System.out.println("口令是否正确？：" + validPassword(ts, ec));

	}
}
