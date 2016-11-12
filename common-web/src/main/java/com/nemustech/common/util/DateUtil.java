package com.nemustech.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import com.nemustech.common.exception.SmartException;

/**
 * 날짜/시간 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.lang3.time.DateUtils 클래스를 상속받음.
 * 
 * @see <a
 *      href=http://commons.apache.org/lang/api-release/org/apache/commons/lang
 *      /time/DateUtils.html>org.apache.commons.lang3.time.DateUtils</a>
 */
public abstract class DateUtil extends DateUtils {
	/** 최소일자 - 00000000 */
	public static final String MIN_DATE = "00000000";
	/** 최대일자 - 99999999 */
	public static final String MAX_DATE = "99999999";
	/** 최소일시 - 00000000000000 */
	public static final String MIN_DATE_TIME = "00000000000000";
	/** 최대일시 - 99999999999999 */
	public static final String MAX_DATE_TIME = "99999999999999";

	public static final String PATTERN_yyyyMM = "yyyyMM";
	public static final String PATTERN_yyyyMM_DASH = "yyyy-MM";
	public static final String PATTERN_yyyyMMdd = "yyyyMMdd";
	public static final String PATTERN_yyyyMMdd_DASH = "yyyy-MM-dd";
	public static final String PATTERN_MMddHHmmss_DASH = "MM-dd HH:mm:ss";
	public static final String PATTERN_yyyyMMdd_SLASH = "yyyy/MM/dd";
	public static final String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String PATTERN_yyyyMMddHHmmss_DASH = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
	public static final String PATTERN_yyyyMMddHHmmssSSS_DASH = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String PATTERN_HHmmss = "HH:mm:ss";

	/**
	 * 주어진 일시에 지정된 일만큼 더한 일시를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120422";    //지정된 날짜
	 *      <br>
	 *      addDays(str, 1, "yyyyMMdd");    //지정된 날짜에 Day 더한 날짜 값
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param amount 일
	 * @param pattern DateFormat 패턴
	 * @return 지정된 일만큼 더한 일시
	 */
	public static String addDays(String dateString, int amount, String pattern) throws SmartException {
		return addDays(toDate(dateString, pattern), amount, pattern);
	}

	public static String addDays(Date date, int amount, String pattern) throws SmartException {
		return format(addDays(date, amount), pattern);
	}

	/**
	 * 주어진 일시에 지정된 달만큼 더한 일시를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120420";    //지정된 날짜
	 *      <br>
	 *      addMonths(str, 1, "yyyyMMdd");  //지정된 날짜에 월을 더한 날짜 값
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param amount 달
	 * @param pattern DateFormat 패턴
	 * @return 지정된 달만큼 더한 일시
	 */
	public static String addMonths(String dateString, int amount, String pattern) throws SmartException {
		return addMonths(toDate(dateString, pattern), amount, pattern);
	}

	public static String addMonths(Date date, int amount, String pattern) throws SmartException {
		return format(addMonths(date, amount), pattern);
	}

	/**
	 * 현재일시를 yyyyMMddHHmmssSSS형 문자열로 가져온다
	 *
	 * @return 현재일시 (yyyyMMddHHmmssSSS형 문자열)
	 */
	public static final String getCurrentDateMillisTime() {
		return format(new Date(), PATTERN_yyyyMMddHHmmssSSS);
	}

	/**
	 * 현재일시을 주어진 패턴의 문자열로 가져온다
	 *
	 * 주어진 일시에 지정된 년만큼 더한 일시를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120420";    //지정된 날짜
	 *      <br>
	 *      addYears(str, 1, "yyyyMMdd");   //지정된 날짜에 년을 더한 날짜 값
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param amount 년
	 * @param pattern DateFormat 패턴
	 * @return 지정된 년만큼 더한 일시
	 */
	public static String addYears(String dateString, int amount, String pattern) throws SmartException {
		return addYears(toDate(dateString, pattern), amount, pattern);
	}

	public static String addYears(Date date, int amount, String pattern) throws SmartException {
		return format(addYears(date, amount), pattern);
	}

	/**
	 * 주어진 두 일시 간의 시간차(단위: 일)를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120420";    //지정날짜
	 *      String str2 = "20120430";
	 *      <br>
	 *      differenceDays(str, str2, "yyyyMMdd");  //날짜 차이 계산된 값
	 * </pre>
	 * 
	 * @param fromDate 시작일시
	 * @param toDate 종료일시
	 * @param pattern DateFormat 패턴
	 * @return 두 일시의 시간차 (단위: 일)
	 * @throws SmartException 유효한 일시가 아닌 경우
	 */
	public static long differenceDays(String fromDate, String toDate, String pattern) throws SmartException {
		return differenceSeconds(fromDate, toDate, pattern) / 60 / 60 / 24;
	}

	/**
	 * 주어진 두 일시 간의 시간차(단위: 초)를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120419";        //지정날짜
	 *      String str2 = "20120430";
	 *      <br>
	 *      differenceSeconds(str, str2, "yyyyMMdd");   //두 날짜 사이의 초 차이 계산 값
	 * </pre>
	 * 
	 * @param fromDate 시작일시
	 * @param toDate 종료일시
	 * @param pattern DateFormat 패턴
	 * @return 두 일시의 시간차 (단위: 초)
	 * @throws SmartException 유효한 일시가 아닌 경우
	 */
	public static long differenceSeconds(String fromDate, String toDate, String pattern) throws SmartException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setTimeZone(TimeZone.getDefault());
			return (formatter.parse(fromDate).getTime() - formatter.parse(toDate).getTime()) / 1000;
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 오늘일자를 yyyyMMdd형 문자열로 가져온다.
	 * 
	 * <pre>
	 * 예)
	 * getCurrentDate(); // 현재 일자 생성
	 * </pre>
	 * 
	 * @return 오늘일자 (yyyyMMdd형 문자열)
	 */
	public static final String getCurrentDate() {
		return format(new Date(), PATTERN_yyyyMMdd);
	}

	/**
	 * 현재일시를 yyyyMMddHHmmss형 문자열로 가져온다.
	 * 
	 * <pre>
	 * 예)
	 * getCurrentDateTime(); // 현재 년 월 일 시간 생성
	 * </pre>
	 * 
	 * @return 현재일시 (yyyyMMddHHmmss형 문자열)
	 */
	public static final String getCurrentDateTime() {
		return format(new Date(), PATTERN_yyyyMMddHHmmss);
	}

	/**
	 * 주어진 일시 문자열을 Date 객체로 변환한다.
	 * 
	 * <pre>
	 * 예)
	 *      String dateStr = "20120419";        //지정날짜
	 *      String patternStr = "yyyyMMdd";     //지정패턴
	 *      <br>
	 *      toDate(dateStr, patternStr);
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param pattern DateFormat 패턴
	 * @return Date 객체로 변환된 일시
	 * @throws SmartException 유효한 일시가 아닌 경우
	 */
	public static Date toDate(String dateString, String pattern) throws SmartException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setTimeZone(TimeZone.getDefault());

			return formatter.parse(dateString);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 현재일시을 주어진 패턴의 문자열로 가져온다.
	 * 
	 * <pre>
	 * 예)
	 * getCurrentDateTime(&quot;yyyy-MM-dd HH:mm:ss:SSS&quot;) // 패턴을 정하여 현재 일시 리턴
	 * </pre>
	 * 
	 * @param pattern DateFormat 패턴
	 * @return 현재일시
	 */
	public static final String getCurrentDateTime(String pattern) {
		return format(new Date(), pattern);
	}

	/**
	 * 주어진 일시에 지정된 초만큼 더한 일시를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120420142030";
	 *      <br>
	 *      addSeconds(str, 10, "yyyyMMddHHmmss")  //지정된 초만큼 더한 값 리턴
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param amount 초
	 * @param pattern DateFormat 패턴
	 * @return 지정된 초만큼 더한 일시
	 */
	public static String addSeconds(String dateString, int amount, String pattern) throws SmartException {
		return addSeconds(toDate(dateString, pattern), amount, pattern);
	}

	public static String addSeconds(Date date, int amount, String pattern) throws SmartException {
		return format(addSeconds(date, amount), pattern);
	}

	/**
	 * 주어진 일시에 지정된 분만큼 더한 일시를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120420142030";
	 *      <br>
	 *      addMinutes(str, 10, "yyyyMMddHHmmss")  //지정된 분 만큼 더한 값 리턴
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param amount 분
	 * @param pattern DateFormat 패턴
	 * @return 지정된 분만큼 더한 일시
	 */
	public static String addMinutes(String dateString, int amount, String pattern) throws SmartException {
		return addMinutes(toDate(dateString, pattern), amount, pattern);
	}

	public static String addMinutes(Date date, int amount, String pattern) throws SmartException {
		return format(addMinutes(date, amount), pattern);
	}

	/**
	 * 주어진 일시에 지정된 시간만큼 더한 일시를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String str = "20120420142030";
	 *      <br>
	 *      addHours(str, 12, "yyyyMMddHHmmss")    //지정된 시간 만큼 더한 값 리턴
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param amount 시간
	 * @param pattern DateFormat 패턴
	 * @return 지정된 시간만큼 더한 일시
	 */
	public static String addHours(String dateString, int amount, String pattern) throws SmartException {
		return addHours(toDate(dateString, pattern), amount, pattern);
	}

	public static String addHours(Date date, int amount, String pattern) throws SmartException {
		return format(addHours(date, amount), pattern);
	}

	/**
	 * 주어진 기준일시가 시작일시와 종료일시 사이에 존재하는 일시인지를 알아낸다.
	 * 
	 * <pre>
	 * 예)
	 *      String fromdate = "20120401000000";             //비교시작일시
	 *      String todate = getCurrentDateTime();  //비교종료일시
	 *      String thedate = "20120420000000";              //비교대상 일시
	 *      <br>
	 *      isValidDateTime(thedate, fromdate, todate) //해당 값을 비교해서 Boolean값 리턴
	 * </pre>
	 * 
	 * @param theDateTime 기준일시 (yyyyMMddHHmmss)
	 * @param fromDateTime 시작일시 (yyyyMMddHHmmss)
	 * @param toDateTime 종료일시 (yyyyMMddHHmmss)
	 * @return 시작일시와 종료일시 사이에 존재하는 일시면 true, 아니면 false
	 */
	public static boolean isValidDateTime(String theDateTime, String fromDateTime, String toDateTime) {
		return (StringUtil.isLessThan(theDateTime, fromDateTime) || StringUtil.isGreaterThan(theDateTime, toDateTime))
				? false : true;
	}

	/**
	 * 주어진 기준일시가 시작일시와 종료일시 사이에 존재하지 않는 일시인지를 알아낸다.
	 * 
	 * <pre>
	 * 예)
	 *      String fromdate = "20120401000000";             //비교시작일시
	 *      String todate = getCurrentDateTime();  //비교종료일시
	 *      String thedate = "20120420000000";              //비교대상 일시
	 *      <br>
	 *      isValidDateTime(thedate, fromdate, todate) //해당 값을 비교해서 Boolean값 리턴
	 * </pre>
	 * 
	 * @param theDateTime 기준일시 (yyyyMMddHHmmss)
	 * @param fromDateTime 시작일시 (yyyyMMddHHmmss)
	 * @param toDateTime 종료일시 (yyyyMMddHHmmss)
	 * @return 시작일시와 종료일시 사이에 존재하지 않는 일시면 true, 아니면 false
	 */
	public static boolean isInvalidDateTime(String theDateTime, String fromDateTime, String toDateTime) {
		return (StringUtil.isLessThan(theDateTime, fromDateTime) || StringUtil.isGreaterThan(theDateTime, toDateTime))
				? true : false;
	}

	/**
	 * 주어진 두 일시 간의 시간차(단위: 분)를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String fromdate = "20120401000000";             //시작일시
	 *      String todate = getCurrentDateTime();  //종료일시
	 *      String dateformat = "yyyyMMddHHmmss";           //리턴될 패턴
	 *      <br>
	 *      differenceMinutes(fromdate, todate, dateformat)    //비교된 두 시간 사이의 분을 리턴한다.
	 * </pre>
	 * 
	 * @param fromDate 시작일시
	 * @param toDate 종료일시
	 * @param pattern DateFormat 패턴
	 * @return 두 일시의 시간차 (단위: 분)
	 * @throws SmartException 유효한 일시가 아닌 경우
	 */
	public static long differenceMinutes(String fromDate, String toDate, String pattern) throws SmartException {
		return differenceSeconds(fromDate, toDate, pattern) / 60;
	}

	/**
	 * 주어진 두 일시 간의 시간차(단위: 시간)를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      String fromdate = "20120401000000";             //시작일시
	 *      String todate = getCurrentDateTime();  //종료일시
	 *      String dateformat = "yyyyMMddHHmmss";           //리턴될 패턴
	 *      <br>
	 *      differenceHours(fromdate, todate, dateformat)    //비교된 두 시간 사이의 시간을 리턴한다.
	 * </pre>
	 * 
	 * @param fromDate 시작일시
	 * @param toDate 종료일시
	 * @param pattern DateFormat 패턴
	 * @return 두 일시의 시간차 (단위: 시간)
	 * @throws SmartException 유효한 일시가 아닌 경우
	 */
	public static long differenceHours(String fromDate, String toDate, String pattern) throws SmartException {
		return differenceSeconds(fromDate, toDate, pattern) / 60 / 60;
	}

	/**
	 * 주어진 일시 문자열의 패턴을 변환시킨다.
	 * 
	 * <pre>
	 * 예)
	 *      String date = "20120411000000";         //지정일시
	 *      String dateformat = "yyyyMMddHHmmss";   //현재일시패턴
	 *      String dateformat2 = "yyyyMMdd";        //바뀔일시패턴
	 *      <br>
	 *      convertDateFormat(date, dateformat, dateformat2)
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param fromPattern 변화전 패턴
	 * @param toPattern 변환후 패턴
	 * @return 패턴이 변환된 일시 문자열
	 * @throws SmartException 유효한 일시, 패턴이 아닌 경우
	 */
	public static String convertDateFormat(String dateString, String fromPattern, String toPattern)
			throws SmartException {
		try {
			return format(toDate(dateString, fromPattern), toPattern);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 일시 문자열의 패턴을 변환시킨다.
	 * 
	 * <pre>
	 * 예)
	 *      String date = "2012/04/11 00:00:00";        //지정된일시
	 *      String dateformat = "yyyy/MM/dd HH:mm:ss";  //현재일시패턴
	 *      String dateformat2 = "yyyy-MM-dd";          //바뀔일시패턴
	 *      <br>
	 *      format(date, dateformat, dateformat2)
	 * </pre>
	 * 
	 * @param dateString 일시 문자열
	 * @param fromPattern 변화전 패턴
	 * @param toPattern 변환후 패턴
	 * @return 패턴이 변환된 일시 문자열
	 * @throws SmartException 유효한 일시, 패턴이 아닌 경우
	 */
	public static String format(String dateString, String fromPattern, String toPattern) throws SmartException {
		try {
			return format(toDate(dateString, fromPattern), toPattern);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 millisecond 시간을 지정된 DateFormat 패턴 문자열로 변환한다.
	 * 
	 * <pre>
	 * 예)
	 *      long millis = 10000;                        //지정한밀리초
	 *      String dateformat = "yyyy/MM/dd HH:mm:ss";  //계산되어 나올 날짜의 패턴
	 *      <br>
	 *      format(millis, dateformat)
	 * </pre>
	 * 
	 * @param millis millisecond 시간
	 * @param pattern DateFormat 패턴
	 * @return DateFormat 패턴 문자열로 변환된 시간
	 */
	public static String format(long millis, String pattern) {
		return DateFormatUtils.format(millis, pattern);
	}

	/**
	 * 주어진 Date 객체를 지정된 DateFormat 패턴 문자열로 변환한다.
	 * 
	 * <pre>
	 * 예)
	 *      String date = "20120502123533";
	 *      String date2 = "20120502";
	 *      String dateformat = "yyyy/MM/dd HH:mm:ss";
	 *      String dateformat2 = "yyyy-MM-dd";
	 *      <br>
	 *      //date String을 Date 타입으로 변경해주어야 한다.
	 *      format(toDate(date, "yyyyMMddHHmmss"), dateformat)
	 *      format(toDate(date2, "yyyyMMdd"), dateformat2)
	 * </pre>
	 * 
	 * @param date Date 객체
	 * @param pattern DateFormat 패턴
	 * @return DateFormat 패턴 문자열로 변환된 시간
	 */
	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 주어진 일시 문자열의 요일을 얻는다.
	 * 
	 * <pre>
	 * 예)
	 *      String formatStr = "yyyyMMdd";      //패턴
	 *      String dateStr = "20120501";        //요일을 원하는 날짜
	 *      <br>
	 *      getDayOfWeek(dateStr, formatStr)
	 * </pre>
	 * 
	 * @param dateString 일시 문자열 (yyyyMMdd)
	 * @param pattern DateFormat 패턴
	 * @return 요일 (Calendar.SUNDAY=1, Calendar.MONDAY=2, ...,
	 *         Calendar.SATURDAY=7)
	 */
	public static int getDayOfWeek(String dateString, String pattern) throws SmartException {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDate(dateString, pattern));
			return calendar.get(Calendar.DAY_OF_WEEK);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 일시 문자열에 해당하는 주에서 원하는 요일의 날짜를 얻는다.
	 * 
	 * <pre>
	 * 예)
	 *      String dateStr = "20120501";    //해당하는 주에 포함되는 날짜
	 *      String formatStr = "yyyyMMdd";  //패턴
	 *      int dayOfweek = 2;              //원하는 요일
	 *      <br>
	 *      getDayOfThisWeek(dateStr, formatStr, dayOfweek)
	 * </pre>
	 * 
	 * @param dateString 일시 문자열 (yyyyMMdd)
	 * @param pattern DateFormat 패턴
	 * @param dayOfWeek 요일(Calendar.SUNDAY=1, Calendar.MONDAY=2, ...,
	 *            Calendar.SATURDAY=7)
	 */
	public static String getDayOfThisWeek(String dateString, String pattern, int dayOfWeek) throws SmartException {
		try {
			int theDay = getDayOfWeek(dateString, pattern);
			return addDays(dateString, dayOfWeek - theDay, pattern);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// 확장
	///////////////////////////////////////////////////////////////////////////

	public static String toYYYYMMDD(Date date) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

		if (date == null) {
			return "";
		}
		return yyyyMMdd.format(date);
	}

	public static String toHHmm(Date date) {
		SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
		if (date == null) {
			return "";
		}
		return HHmm.format(date);
	}

	/**
	 * 날짜를 형 변환한다.
	 *
	 * @param date yyyyMMddHHmmss 타입의 날짜 문자
	 * @return "yyyy-MM-dd" 변환된 날짜. 변환 실패시에는 date를 return
	 */
	public static String formatDate(String date) {
		return formatDate(date, null);
	}

	/**
	 * 날짜를 형 변환한다.
	 *
	 * @param date yyyyMMddHHmmss 타입의 날짜 문자
	 * @param delimiter 날짜 구분자. 기본값은 "-"
	 * @return 변환된 날짜. 변환 실패시에는 date를 return
	 */
	public static String formatDate(String date, String delimiter) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		if (delimiter == null) {
			delimiter = "-";
		}

		SimpleDateFormat tgdf = new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd");

		try {
			Date dt = sdf.parse(date);

			return tgdf.format(dt);
		} catch (ParseException e) {
			return date;
		}
	}

	public static String formatFullDate(String date) {
		return formatFullDate(date, null);
	}

	public static String formatFullDate(String date, String delimiter) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		if (delimiter == null) {
			delimiter = "-";
		}

		SimpleDateFormat tgdf = new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd");
		try {
			Date dt = sdf.parse(date);
			return tgdf.format(dt);
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * 시간을 형 변환한다.
	 *
	 * @param date yyyyMMddHHmmss 타입의 날짜 문자
	 * @return "HH:mm" 변환된 시간. 변환 실패시에는 date를 return
	 */
	public static String formatTime(String date) {
		return formatTime(date, null);
	}

	/**
	 * 시간을 형 변환한다.
	 *
	 * @param date yyyyMMddHHmmss 타입의 날짜 문자
	 * @param delimiter 시간 구분자. 기본값은 ":"
	 * @return 변환된 시간. 변환 실패시에는 date를 return
	 */
	public static String formatTime(String date, String delimiter) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

		if (delimiter == null) {
			delimiter = ":";
		}

		SimpleDateFormat tgdf = new SimpleDateFormat("HH" + delimiter + "mm");

		try {
			Date dt = sdf.parse(date);

			return tgdf.format(dt);
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * 시간을 형 변환한다.
	 *
	 * @param date yyyyMMddHHmmss 타입의 날짜 문자
	 * @return "HH:mm" 변환된 시간. 변환 실패시에는 date를 return
	 */
	public static String formatFullTime(String date) {
		return formatFullTime(date, null);
	}

	/**
	 * 시간을 형 변환한다.
	 *
	 * @param date yyyyMMddHHmmss 타입의 날짜 문자
	 * @param delimiter 시간 구분자. 기본값은 ":"
	 * @return 변환된 시간. 변환 실패시에는 date를 return
	 */
	public static String formatFullTime(String date, String delimiter) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		if (delimiter == null) {
			delimiter = ":";
		}

		SimpleDateFormat tgdf = new SimpleDateFormat("HH" + delimiter + "mm");

		try {
			Date dt = sdf.parse(date);

			return tgdf.format(dt);
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * 날짜 시간을 형 변환한다. yyyy-MM-dd HH:mm 으로 변환
	 *
	 * @param dateTime yyyyMMddHHmmss 타입의 날짜 문자
	 * @return 변환된 날짜 시간. 변환 실패시에는 dateTime을 return
	 */
	public static String formatDateTime(String dateTime) {
		return formatDate(dateTime) + " " + formatTime(dateTime);
	}

	public static Date toDate2(String yyyyMMdd, String HHmm) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmm");
		return dateFormat.parse(yyyyMMdd + " " + HHmm);
	}

	public static String dateConverter(String date, String outFormat) {
		SimpleDateFormat outDateFormat = new SimpleDateFormat(outFormat);
		Date frmTime = new Date(date);

		return outDateFormat.format(frmTime);
	}

	/**
	 * 'YYYYMMDD' 형태의 String형을 Date형으로 만들어 리턴
	 */
	public static Date stringToDate(String d) {

		int year = Integer.parseInt(d.substring(0, 4));
		int month = Integer.parseInt(d.substring(4, 6));
		int day = Integer.parseInt(d.substring(6));

		Calendar cdate = java.util.Calendar.getInstance();
		cdate.set(Calendar.YEAR, year);
		cdate.set(Calendar.MONTH, month - 1); // 0 이 1월, 1 은 2월, ....
		cdate.set(Calendar.DATE, day);

		Date ddate = cdate.getTime(); // java.sql.Date 가 아님..
		return ddate;

	}

	/**
	 * for example, convert "2012-01-25 20:14:25" to "2012-01-25"
	 *
	 * @return if draftDate is null or isn't 19 length, return blank, ""
	 */
	public static String toYYYY_MM_DD(String draftDate) {
		if (draftDate == null || draftDate.length() < 19) {
			return draftDate;
		}
		return draftDate.substring(0, 10);
	}

	/**
	 * for example, convert "2012-01-25 20:14:25" to "20:14"
	 *
	 * @return @return if draftDate is null or isn't 19 length, return blank, ""
	 */
	public static String toHH_MM(String draftDate) {
		if (draftDate == null || draftDate.length() < 19) {
			return draftDate;
		}
		return draftDate.substring(11, 16);
	}

	public static String getToday(String dateFormat) {
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		return date.format(new Date());
	}

	/**
	 * 요일을 계산
	 */
	public static String getWeekDay(String day) {
		Calendar cal = Calendar.getInstance();
		String date = StringUtil.replace(day, "-", "");

		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));

		String[] dayOfWeek = { "일", "월", "화", "수", "목", "금", "토" };

		String weekday = dayOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];

		return weekday;
	}

	public static String getFormatDateWithWeekDay(String day) {
		String date = "";

		if (day == null) {
			date = "";
		} else if (day.length() == 8) {
			// yyyymmdd
			date = formatDate(day) + "(" + getWeekDay(day) + ")";
		} else if (day.length() == 10) {
			// yyyy-mm-dd 양식
			date = day + "(" + getWeekDay(day) + ")";
		} else if (day.length() > 10 && day.length() < 19) {
			String tmp = day.substring(0, 10);
			date = tmp + "(" + getWeekDay(tmp) + ")";
		} else {
			date = day;
		}

		return date;
	}

	/**
	 * 기간 사이의 월을 구한다.
	 * 
	 * @param start_date YYYYMMDD
	 * @param end_date YYYYMMDD
	 * @return
	 * @throws SmartException
	 */
	public static List<String> getMonthList(String start_date, String end_date) throws SmartException {
		List<String> list = new ArrayList<String>();
		if (DateUtil.differenceDays(start_date, end_date, DateUtil.PATTERN_yyyyMMdd) > 0) {
			return list;
		}

		String startMonth = start_date.substring(0, 6);
		String endMonth = end_date.substring(0, 6);
		while (true) {
			list.add(startMonth);

			if (endMonth.equals(startMonth))
				break;
			startMonth = DateUtil.addMonths(startMonth, 1, DateUtil.PATTERN_yyyyMM);
		}

		return list;
	}

	/**
	 * 기간 사이의 일자를 구한다.
	 * 
	 * @param start_date YYYYMMDD
	 * @param end_date YYYYMMDD
	 * @return
	 * @throws SmartException
	 */
	public static List<String> getDateList(String start_date, String end_date) throws SmartException {
		List<String> list = new ArrayList<String>();
		if (DateUtil.differenceDays(start_date, end_date, DateUtil.PATTERN_yyyyMMdd) > 0) {
			return list;
		}

		while (true) {
			list.add(start_date);

			if (end_date.equals(start_date))
				break;
			start_date = DateUtil.addDays(start_date, 1, DateUtil.PATTERN_yyyyMMdd);
		}

		return list;
	}

	public static void main(String[] args) throws Exception {
//		Date date = new Date();
//		System.out.println(date);
//		System.out.println(addSeconds(date, 30));

//		System.out.println(addMonths("20161001", 1, PATTERN_yyyyMMdd));
//		System.out.println(addDays(new Date(), 1, PATTERN_yyyyMMdd));
//		System.out.println(toDate("20161001", PATTERN_yyyyMMdd));
//		System.out.println(format("20161001", PATTERN_yyyyMMdd, PATTERN_yyyyMM));
//		System.out.println(dateConverter("Sat, 12 Aug 1995 13:30:00 GMT+0430", PATTERN_yyyyMMddHHmmssSSS_DASH));
//		System.out.println(differenceDays("20161001", "20161002", PATTERN_yyyyMMdd));
		System.out.println(getMonthList("20161102", "20161101"));
//		System.out.println(getMonthList("20161001", "20161101"));
//		System.out.println(getDateList("20161001", "20161101"));
	}
}
