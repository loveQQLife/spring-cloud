package com.kreken.util;

public class UnicodeUtil {

    /**
     * 中文转Unicode编码
     * @param str
     * @return
     */
    public static String encodeUnicode(String str) {
        char[] utfBytes = str.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * 解码Unicode
     *
     * @param data
     * @return
     */
    public static String decodeUnicode(String data) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = data.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = data.substring(start + 2, data.length());
            } else {
                charStr = data.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }


}
