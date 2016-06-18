package com.liang.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

//	private static final Logger logger = Logger.getLogger(DateFormatUtil.class);
	private static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";
	private static final String FORMAT_DATE = "yyyy-MM-dd";

	/**
	 * 返回String类型时间精确到秒: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateToSeconds(Date date) {
		DateFormat df = new SimpleDateFormat(FORMAT_TIME);
		return df.format(date);
	}

	/**
	 * 返回String类型时间精确到天: yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateToDay(Date date) {
		DateFormat df = new SimpleDateFormat(FORMAT_DATE);
		return df.format(date);
	}

	/**
	 * 返回Date类型时间精确到秒
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date parseToSeconds(String dateString) throws ParseException {
		DateFormat df = new SimpleDateFormat(FORMAT_TIME);
		return df.parse(dateString);
	}

	/**
	 * 返回Date类型时间精确到天
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date parseToDay(String dateString) throws ParseException {
		DateFormat df = new SimpleDateFormat(FORMAT_DATE);
		return df.parse(dateString);
	}
}
