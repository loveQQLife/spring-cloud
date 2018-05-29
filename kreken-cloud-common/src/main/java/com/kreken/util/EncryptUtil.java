package com.kreken.util;

import java.security.MessageDigest;

public class EncryptUtil {

    /**
     * sha1加密  
     * @param sourceString
     * @return
     */
    public final static String SHA1Encode(String sourceString) {  
        String resultString = null;  
        try {  
           resultString = new String(sourceString);  
           MessageDigest md = MessageDigest.getInstance("SHA-1");  
           resultString = EncryptUtil.byte2hexString(md.digest(resultString.getBytes()));  
        } catch (Exception ex) {  
        }  
        return resultString;  
    }  
    public final static String byte2hexString(byte[] bytes) {  
        StringBuffer buf = new StringBuffer(bytes.length * 2);  
        for (int i = 0; i < bytes.length; i++) {  
            if (((int) bytes[i] & 0xff) < 0x10) {  
                buf.append("0");  
            }  
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));  
        }  
        return buf.toString().toUpperCase();  
    } 
    
	/**
	 * @Description:使用MD5加密指定的字符串.
	 * @param s
	 * @return 
	 * @author 黄东
	 * @date  June 30, 2016
	 * @modify 
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @Description:加密指定的整型数，通常用来加密ID值！
	 * 编码的原理是根据当前的currentTimeMillis在指定的位置插入其ID值，然后再附加
	 * ID值的长度来进行编码.System.currentTimeMillis获取的值为13位长度，即使100
	 * 年后也是这样，所以这个基本上是不变的！
	 * @param id
	 * @return 
	 * @author 黄东
	 * @date  June 30, 2016
	 * @modify 
	 */
	
	private final static int ENCODE_ID_INSERT_INDEX = 5; // 插入的索引位置，0-12之间

	public final static String encodeId(int id) {
		String _id = String.valueOf(id);
		if (id < 0 || _id.length() > 9)
			return "";
		StringBuilder buf = new StringBuilder();
		buf.append(System.currentTimeMillis());
		for (int i = 0; i < _id.length(); i++) {
			buf.insert(ENCODE_ID_INSERT_INDEX, _id.charAt(i));
		}
		buf.append(_id.length()); // 长度校验位
		return buf.toString();
	}


	/**
	 * @Description:反编码ID后的字符串.<P>
	 * 注意，编码和解码的数字长度不能超过9位！
	 * @param str
	 * @return 
	 * @author 黄东
	 * @date  June 30, 2016
	 * @modify 
	 */
	public final static int unencodeId(String str) {
		String times = System.currentTimeMillis() + "";
		if (str == null || str.length() < times.length())
			return 0;
		if (!str.matches("\\d{13,}"))
			return 0;
		int len = Integer.parseInt(str.substring(str.length() - 1));
		if (str.length() != (times.length() + len + 1))
			return 0;
		return Integer.parseInt(new StringBuilder(str.substring(
				ENCODE_ID_INSERT_INDEX, ENCODE_ID_INSERT_INDEX + len))
				.reverse().toString());
	}
	/**
	 * @Description:随机生成六位数，纯数字
	 *
	 * @param str
	 * @return 
	 * @author 黄东
	 * @date  July 28, 2016
	 * @modify 
	 */
	public final static String randomSix(int lenght) {
//		String[] randomValues = new String[]{"0","1","2","3","4","5","6","7","8","9",
//		"a","b","c","d","e","f","g","h","i","j","k","l","m","n","u",
//		"t","s","o","x","v","p","q","r","w","y","z"};
		String[] randomValues = new String[]{"0","1","2","3","4","5","6","7","8","9"};
		StringBuffer str = new StringBuffer();
		for(int i = 0;i < lenght; i++)
		{
		Double number=Math.random()*(randomValues.length-1);
		str.append(randomValues[number.intValue()]);
		}
		return str.toString();
	}

	// 测试
//	public static void main(String[] args) {
//		StringUtil.debug(MD5("gjtsg"));
//		//StringUtil.debug(MD5("123"));
//		String tmp = encodeId(13455787);
//		StringUtil.debug(tmp);
//		StringUtil.debug(unencodeId(tmp));
//	}
}
