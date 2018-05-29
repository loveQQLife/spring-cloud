package com.kreken.util;

import com.mysql.jdbc.StringUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
	 * MD5加密算法
	 * 
	 * @param s
	 * @return
	 */
	public static String MD5(String s) {

		if (StringUtils.isNullOrEmpty(s)) {
			return s;
		}

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取该输入流的MD5值
	 * 
	 * @param is
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String MD5(InputStream is) throws NoSuchAlgorithmException, IOException {
		StringBuffer md5 = new StringBuffer();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] dataBytes = new byte[1024];

		int nread = 0;
		while ((nread = is.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}
		;
		byte[] mdbytes = md.digest();

		// convert the byte to hex format
		for (int i = 0; i < mdbytes.length; i++) {
			md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return md5.toString();
	}

	/**
	 * 获取MD5值
	 * 
	 * @param bytes
	 * @return
	 */
	public static String MD5(byte[] bytes) {
		try {
			return MD5(new ByteArrayInputStream(bytes));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取该文件的MD5值
	 * 
	 * @param file
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String MD5(File file) throws NoSuchAlgorithmException, IOException {
		FileInputStream fis = new FileInputStream(file);
		return MD5(fis);
	}

	/**
	 * 获取指定路径文件的MD5值
	 * 
	 * @param filePath
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getMD5(String filePath) throws NoSuchAlgorithmException, IOException {
		FileInputStream fs = new FileInputStream(filePath);
		return MD5(fs);
	}
}
