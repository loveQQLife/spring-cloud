package com.kreken.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYY_MM_dd_hh_mm = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YYYY_MM_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";

	public static String format(Date date) {
		return format(date, FORMAT_YYYY_MM_dd_hh_mm_ss);
	}

	public static String format(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static String format(long date) {
		return format(new Date(date));
	}

	public static String format(long date, String format) {
		return format(new Date(date), format);
	}

	/**
	 * 字符串转换日期对象, 默认格式：YYYY-MM-dd hh:mm:ss
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		return parseDate(dateString, FORMAT_YYYY_MM_dd_hh_mm_ss);
	}

	/**
	 * 字符串转换日期对象
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static Date parseDate(String dateString, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(dateString);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 计算两个时间段的天数
	 * */
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }
	
	/**
	 * 计算两个时间段的天数
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException{  
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }

	/**
	 * 计算从这个日期开始，n天后的日期
	 *
	 * @param startDate 开始日期
	 * @param days 天数
     * @return
     */
	public static   String  getDistanceByDays(String startDate,int days){
		DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH,days);
			Date endDate = calendar.getTime();
			return dateFormat.format(endDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 计算从这个日期开始，n月后的日期
	 *
	 * @param startDate 开始日期
	 * @param months 月数
	 * @return
	 */
	public static  Date  getDistanceByMonth(String startDate,int months){
		DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.MONTH,months);
			Date endDate = calendar.getTime();
			return endDate;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 比较两个日期大小
	 *
	 * @param sDate
	 * @param dDate
     * @return
     */
	public static int compare_date(String sDate, String dDate) {
	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  try {
				 Date sdt = df.parse(sDate);
				 Date ddt = df.parse(dDate);
				  if (sdt.getTime() > ddt.getTime()) {
						  return 1;
					  } else if (sdt.getTime() < ddt.getTime()) {
						  return -1;
					  } else {
						  return 0;
					  }
			  } catch (Exception exception) {
				  exception.printStackTrace();
			  }
		  return 0;
	  }

	/**
	 * 根据日期来判断日期是星期几
	 *
	 * @param dataString
	 * @return
     */
	public static  String getDateDayOfWeek(String dataString){
		String dayOfWeekString = null;

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(dataString));
			int day = cal.get(Calendar.DAY_OF_WEEK);
			switch (day){
				case 1:
					dayOfWeekString="星期日";
					break;
				case 2:
					dayOfWeekString="星期一";
					break;
				case 3:
					dayOfWeekString="星期二";
					break;
				case 4:
					dayOfWeekString="星期三";
					break;
				case 5:
					dayOfWeekString="星期四";
					break;
				case 6:
					dayOfWeekString="星期五";
					break;
				case 7:
					dayOfWeekString="星期六";
					break;
			}
		} catch (ParseException e) {
			return  null;
		}
		return dayOfWeekString;
	}
	
	/**
	 * 根据日期来判断日期是星期几
	 *
	 * @param dataString
	 * @return
     */
	public static  String getDateDayOfWeek(Date dataString){
		
		String dayOfWeekString = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataString);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		switch (day){
			case 1:
				dayOfWeekString="星期日";
				break;
			case 2:
				dayOfWeekString="星期一";
				break;
			case 3:
				dayOfWeekString="星期二";
				break;
			case 4:
				dayOfWeekString="星期三";
				break;
			case 5:
				dayOfWeekString="星期四";
				break;
			case 6:
				dayOfWeekString="星期五";
				break;
			case 7:
				dayOfWeekString="星期六";
				break;
		}
		return dayOfWeekString;
	}

	/**
	 * 获得结束月份
	 * 即下个月1号的时间
	 * @return
	 */
	public static Date getEndMon(String staTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date endMon = null;
		try {
			calendar.setTime(sdf.parse(staTime));
			calendar.add(Calendar.MONTH, 1);
			endMon = calendar.getTime();
		} catch (ParseException e) {
			return  null;
		}
		return endMon;
	}
}
