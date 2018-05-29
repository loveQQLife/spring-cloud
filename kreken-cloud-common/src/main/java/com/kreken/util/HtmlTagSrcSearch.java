package com.kreken.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @category html标签提取工具
 *
 */
public class HtmlTagSrcSearch {

	/**
	 * @category 提取html字符串参数中的img的src地址
	 * @param inputStr
	 * @return
	 */
	public static List<String> findImgSrc(String inputStr) {
		List<String> imgTagStrList = new ArrayList<String>();
		List<String> srcStrList = new ArrayList<String>();

		/*
		 * 以<img作为为分隔符分割字符串
		 */
		Pattern patternSplit = Pattern.compile("<(img|IMG)");
		String[] strSplit = patternSplit.split(inputStr);

		/*
		 * 逐个获取<img .../>标签
		 */
		StringBuffer imgBuffer = null;
		Pattern imgMatch = Pattern.compile("<img.*/>");
		Matcher imgMatcher = null;

		for (int i = 0; i < strSplit.length; i++) {
			imgBuffer = new StringBuffer();
			// 由于上面的分割会去除"<img"的内容，所以在此补上"<img"
			imgMatcher = imgMatch.matcher("<img" + strSplit[i]);
			while (imgMatcher.find()) {
				imgBuffer.append(imgMatcher.group());
				imgBuffer.append("\r\n");
				imgTagStrList.add(imgBuffer.toString());
			}
		}

		/*
		 * 逐个提取<img src="..." .../>标签内src的内容
		 */
//		String srcTemp = null;
		String[] srcStrTemp = null;

		for (String imgStr : imgTagStrList) {
			srcStrTemp = imgStr.split("\"");
			for (int j = 0; j < srcStrTemp.length - 1; j++) {
				if (srcStrTemp[j].contains("src") || srcStrTemp[j].contains("SRC")) {
					srcStrList.add(srcStrTemp[j + 1]);
					break;
				}
			}
		}

		return srcStrList;
	}

	/**
	 * @category 提取html字符串参数中a的href地址
	 * @param inputStr
	 * @return
	 */
	public static List<String> findLinkHref(String inputStr) {
		List<String> linkTagStrList = new ArrayList<String>();
		List<String> hrefStrList = new ArrayList<String>();

		/*
		 * 以<a作为为分隔符分割字符串
		 */
		Pattern patternSplit = Pattern.compile("<(a|A)");
		String[] strSplit = patternSplit.split(inputStr);

		/*
		 * 逐个获取<a .../>...</a>标签
		 */
		StringBuffer linkBuffer = null;
		Pattern linkMatch = Pattern.compile("<(a|A).*>.*</(a|A)>");
		Matcher linkMatcher = null;

		for (int i = 0; i < strSplit.length; i++) {
			linkBuffer = new StringBuffer();
			// 由于上面的分割会去除"<a"的内容，所以在此补上"<a"
			linkMatcher = linkMatch.matcher("<a" + strSplit[i]);
			while (linkMatcher.find()) {
				linkBuffer.append(linkMatcher.group());
				linkBuffer.append("\r\n");
				// System.out.println(imgBuffer.toString());
				linkTagStrList.add(linkBuffer.toString());
			}
		}

		/*
		 * 逐个提取<a href="..." .../>...</a>标签内href的内容
		 */
//		String srcTemp = null;
		String[] srcStrTemp = null;

		for (String imgStr : linkTagStrList) {
			srcStrTemp = imgStr.split("\"");
			for (int j = 0; j < srcStrTemp.length - 1; j++) {
				if (srcStrTemp[j].contains("href") || srcStrTemp[j].contains("HREF")) {
					hrefStrList.add(srcStrTemp[j + 1]);
					System.out.println(srcStrTemp[j + 1]);
					break;
				}
			}
		}

		return hrefStrList;
	}

	public static void main(String[] args) {
		HtmlTagSrcSearch.findImgSrc("<img width=\"12\" height=\"30\" src=\"images/tab_03.gif\" />skdlflfa<img SRC=\"./img/b.gif\" />");
		HtmlTagSrcSearch.findLinkHref("<a a=\"dfa\" href=\"./a.html\">a</a>gs<A HREF=\"./A.html\">a</a>");
	}
}
