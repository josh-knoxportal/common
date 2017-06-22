/*
 * DateUtil.java
 * 
 * Copyright (c) 2012, FIT S&C. All rights reserved.
 */
package com.nemustech.platform.lbs.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * DateUtil
 * 
 * @author Renman
 * @version 1.0
 *
 */
public class DateUtil {

	public static void isDate(String s, String s1) throws ParseException {
		if (s == null) {
			throw new NullPointerException("date string to check is null");
		}

		if (s1 == null) {
			throw new NullPointerException(
					"format string to check date is null");
		}

		SimpleDateFormat simpledateformat = new SimpleDateFormat(s1,
				Locale.KOREA);
		Date date = null;
		try {
			date = simpledateformat.parse(s);
		} catch (ParseException parseexception) {
			throw new ParseException(parseexception.getMessage()
					+ " with format \"" + s1 + "\"", 0);
		}

		if (!simpledateformat.format(date).equals(s)) {
			throw new ParseException("Out of bound date:\"" + s
					+ "\" with format \"" + s1 + "\"", 0);
		}
	}

	public static String getDateString() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		return simpledateformat.format(new Date());
	}

	public static String getFormatString(String s) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s, Locale.KOREA);
		String s1 = simpledateformat.format(new Date());
		return s1;
	}

	public static String getFormatString(Date d, String s) {
		if(d == null) return null;
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s, Locale.KOREA);
		String s1 = simpledateformat.format(d);
		return s1;
	}

	public static String getFormatString(String s, String s1) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s1, Locale.KOREA);
		String s2 = simpledateformat.format(s);
		return s2;
	}

	public static int getYear() {
		return getNumberByPattern("yyyy");
	}

	public static int getMonth() {
		return getNumberByPattern("MM");
	}

	public static int getDay() {
		return getNumberByPattern("dd");
	}

	private static int getNumberByPattern(String s) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s, Locale.KOREA);
		String s1 = simpledateformat.format(new Date());
		return Integer.parseInt(s1);
	}

	public static String getShortDateString() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		return simpledateformat.format(new Date());
	}

	public static String getShortTimeString() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("HHmmss", Locale.KOREA);
		return simpledateformat.format(new Date());
	}

	public static String getTimeStampString() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		return simpledateformat.format(new Date());
	}

	public static String getTimeString() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
		return simpledateformat.format(new Date());
	}

	public static int getFirstMonth(int i, int j, int k) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar = Calendar.getInstance();
		calendar.set(1, i);
		calendar.set(2, j - 1);
		calendar.set(5, 1);
		calendar1 = calendar;

		//		int l = calendar1.get(1);
		//		int i1 = calendar1.get(2) + 1;
		//		int j1 = calendar1.get(5);

		return calendar1.get(7) - 1;
	}

	public static int getDaysInMonth(int i, int j) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar = Calendar.getInstance();
		calendar.set(1, i);
		calendar.set(2, j);
		calendar.set(5, 0);
		calendar1 = calendar;

		//		int k = calendar1.get(1);
		//		int l = calendar1.get(2);
		int i1 = calendar1.get(5);

		return i1;
	}

	public static String addDateYear(String s, int i) throws ParseException {
		String s1 = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = simpledateformat.parse(s);
			calendar.setTime(date);
			calendar.add(1, i);
			date = calendar.getTime();
			s1 = simpledateformat.format(date);
		} catch (ParseException parseexception) {
			s1 = s;
		}
		return s1;
	}

	public static String addDateMonth(String s, int i) throws ParseException {
		String s1 = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = simpledateformat.parse(s);
			calendar.setTime(date);
			calendar.add(2, i);
			date = calendar.getTime();
			s1 = simpledateformat.format(date);
		} catch (ParseException parseexception) {
			s1 = s;
		}
		return s1;
	}

	public static String addDateDay(String s, int i) {
		String s1 = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = simpledateformat.parse(s);
			calendar.setTime(date);
			calendar.add(5, i);
			date = calendar.getTime();
			s1 = simpledateformat.format(date);
		} catch (Exception e) {
			s1 = s;
		}
		return s1;
	}

	public static String addDateTimeHour(String s, int i) throws ParseException {
		String s1 = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = simpledateformat.parse(s);
			calendar.setTime(date);
			calendar.add(10, i);
			date = calendar.getTime();
			s1 = simpledateformat.format(date);
		} catch (ParseException parseexception) {
			s1 = s;
		}

		return s1;
	}

	public static String addDateTimeMinute(String s, int i) throws ParseException {
		String s1 = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = simpledateformat.parse(s);
			calendar.setTime(date);
			calendar.add(12, i);
			date = calendar.getTime();
			s1 = simpledateformat.format(date);
		} catch (ParseException parseexception) {
			s1 = s;
		}

		return s1;
	}

	public static String addDateTimeSecond(String s, int i) throws ParseException {
		String s1 = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = simpledateformat.parse(s);
			calendar.setTime(date);
			calendar.add(13, i);
			date = calendar.getTime();
			s1 = simpledateformat.format(date);
		} catch (ParseException parseexception) {
			s1 = s;
		}
		return s1;
	}

	/**
	 * 날짜와 출력패턴을 입력받는다.<br>
	 * ex)	Date and Time Pattern  Result  <br>
			"yyyy.MM.dd G 'at' HH:mm:ss z"  2001.07.04 AD at 12:08:56 PDT  <br>
			"EEE, MMM d, ''yy"              Wed, Jul 4, '01  <br>
			"h:mm a"                        12:08 PM  
			"hh 'o''clock' a, zzzz"         12 o'clock PM, Pacific Daylight Time  <br>
			"K:mm a, z"                     0:08 PM, PDT  <br>
			"yyyyy.MMMMM.dd GGG hh:mm aaa"  02001.July.04 AD 12:08 PM  <br>
			"EEE, d MMM yyyy HH:mm:ss Z"    Wed, 4 Jul 2001 12:08:56 -0700  <br>
			"yyMMddHHmmssZ"                 010704120856-0700  <br>
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ"    2001-07-04T12:08:56.235-0700  <br>

	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date getPatternDate(String date, String pattern) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.KOREA);
		Date d = simpleDateFormat.parse(date);
		return d;
	}

	public static String convertDatePattern(String date, String srcPattern, String targetPattern) throws ParseException{
		Date d = getPatternDate(date, srcPattern);
		String sd = getFormatString(d, targetPattern);
		return sd;
	}

	public static int diffOfDate(String begin, String beginPattern, String end, String endPattern) throws Exception
	{
		SimpleDateFormat beginFormatter = new SimpleDateFormat(beginPattern);
		SimpleDateFormat endFormatter = new SimpleDateFormat(endPattern);

		Date beginDate = beginFormatter.parse(begin);
		DateTime bDt = new DateTime(beginDate);
		Date endDate = endFormatter.parse(end);
		DateTime eDt = new DateTime(endDate);

		int diffDays = Days.daysBetween(bDt, eDt).getDays();

		return diffDays;
	}

	/**
	 * 오늘날짜 리턴~!<br>
	 * 출력형식 : 2011-05-31
	 * @return
	 */
	public static java.sql.Date getToday(){
		Calendar cal = Calendar.getInstance();
		return new java.sql.Date(cal.getTimeInMillis());
	}

	public static java.sql.Date getDateFromCYMD(String s) {
		if(s == null || s.length() < 8) return null;

		int i = Integer.parseInt(s.substring(0, 4));
		int j = Integer.parseInt(s.substring(4, 6));
		int k = Integer.parseInt(s.substring(6, 8));

		Calendar calendar = Calendar.getInstance();
		calendar.set(i, j - 1, k);

		return new java.sql.Date(calendar.getTimeInMillis());
	}	
}