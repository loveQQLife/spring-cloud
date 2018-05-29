package com.kreken.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 下载微信支付返回的错误信息的转换
 * @author 偶木
 *
 * 2017年9月1日
 */
public class WxChatUnit {

	public static String getValue(String str){
		if("SYSTEMERROR".equals(str)){
			return "系统超时";
		} else if("invalid bill_type".equals(str)) {
			return "参数错误";
		} else if("data format error".equals(str)) {
			return "参数错误";
		} else if("missing parameter".equals(str)) {
			return "参数错误";
		} else if("SIGN ERROR".equals(str)) {
			return "参数错误";
		} else if("NO Bill Exist".equals(str)) {
			return "账单不存在";
		} else if("Bill Creating".equals(str)) {
			return "账单未生成";
		} else if("CompressGZip Error".equals(str)) {
			return "账单压缩失败";
		} else if("UnCompressGZip Error".equals(str)) {
			return "账单解压失败";
		} else {
			return "保存微信支付订单信息的时候发生了错误！";
		}
	}
	
	/**
	 * 转换状态码
	 * @param str
	 * @return
	 */
	public static int getTradestatus(String str){
		if("待买家支付".equals(str) || "SUCCESS".equals(str)){
			return 0;
		} else if("订单已关闭".equals(str)){
			return 1;
		} else if("买家已支付".equals(str)){
			return 2;
		} else if("交易结束".equals(str)){
			return 3;
		} else if("全额退款".equals(str)){
			return 4;
		} else {
			return 5;
		}
	}
	
	/**
	 * 获得对账日期的开始时间
	 * @return
	 */
	public static String getStart(){
		Calendar calendar = Calendar.getInstance();//获取日历实例
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String yesterday= sdf.format(calendar.getTime());//获得前一天
		return yesterday;
	}
	
	/**
	 * 获得对账日期的结束时间
	 * @return
	 */
	public static String getEnd(){
		Calendar calendar = Calendar.getInstance();//获取日历实例
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 1);  //设置为下一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String tomorrow= sdf.format(calendar.getTime());//获得前一天
		return tomorrow;
	}
}
