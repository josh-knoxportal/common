package com.nemustech.common.util;

import java.awt.Container;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

/**
 * 각종 유틸(Pure JDK)
 */
public abstract class Utils {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat.getInstance();
	public static final String DEFAULT_THUMBNAIL_FILE_TYPE = "jpg";

	/**
	 * 백스페이스 문자(사용자가 입력할수 없는 제어문자)
	 */
	public static final char CHAR_DELIMITER = '\b';

	/**
	 * 파일 종류
	 */
	protected static final String[] FILE_TYPES = { "asx", "avi", "bat", "bmp", "class", "dat", "dll", "doc", "drv",
			"gif", "gul", "htm", "html", "hwp", "inf", "ini", "jar", "jpg", "jsp", "mht", "mp3", "multi", "pdf", "ppt",
			"rar", "smi", "txt", "war", "wav", "xls", "zip" };

	public static final char INITIAL_SOUND_BEGIN_UNICODE = 12593; // 초성 유니코드 시작 값(ㄱ)
	public static final char INITIAL_SOUND_LAST_UNICODE = 12622; // 초성 유니코드 마지막 값(ㅎ)

	public static final char HANGUL_BEGIN_UNICODE = 44032; // 한글 유니코드 시작 값(가)
	public static final char HANGUL_LAST_UNICODE = 55203; // 한글 유니코드 마지막 값(힣)

	public static final char HANGUL_BASE_UNIT = 588; // 자음마다 가지는 글자수(= 21 * 28)
	public static final char[] CHO_SUNG_CHAR = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ',
			'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' }; // 19개

	// String ------------------------------------------------------------------

	public static String[][] toKSC5601(String[][] arrStr) {
		if (arrStr == null)
			return new String[0][0];

		for (String[] strings : arrStr) {
			strings = toKSC5601(strings);
		}

		return arrStr;
	}

	public static String[] toKSC5601(String[] arrStr) {
		if (arrStr == null)
			return new String[0];

		for (String string : arrStr) {
			string = toKSC5601(string);
		}

		return arrStr;
	}

	/**
	 * 문자열의 code("8859_1->KSC5601")를 변환한다.
	 *
	 * @param str 변환할 String
	 *
	 * @return String 변환된 String; 비어 있으면 변환되지 않은 String.
	 */
	public static String toKSC5601(String str) {
		if (isValidate(str)) {
			try {
				return new String(str.getBytes("ISO_8859-1"), "KSC5601");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
//				LogUtil.writeLog(e, Utils.class);
			}
		}

		return str;
	}

	/**
	 * 문자열의 code("8859_1->UTF-8")를 변환한다.
	 *
	 * @param str 변환할 String
	 *
	 * @return String 변환된 String; 비어 있으면 변환되지 않은 String.
	 */
	public static String toUTF_8(String str) {
		if (isValidate(str)) {
			try {
				return new String(str.getBytes("ISO_8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
//				LogUtil.writeLog(e, Utils.class);
			}
		}

		return str;
	}

	/**
	 * 문자열의 code ("KSC5601->8859_1")를 변환한다.
	 *
	 * @param str 변환할 String
	 *
	 * @return String 변환된 String; 비어 있으면 변환되지 않은 String.
	 */
	public static String to8859_1(String str) {
		if (isValidate(str)) {
			try {
				return new String(str.getBytes("KSC5601"), "ISO_8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
//				LogUtil.writeLog(e, Utils.class);
			}
		}

		return str;
	}

	public static boolean isValidateWithTrim(String str) {
		str = str.trim();
		return isValidate(str);
	}

	/**
	 * 입력받은 T 값이 유효한 값인지를 검사한다.
	 *
	 * @param T 유효성을 검사할 T
	 * @return 유효한 값이면 true; 유효하지 않으면 false.
	 */
	public static <T> boolean isValidate(T obj) {
		if (obj == null)
			return false;

		if (obj instanceof String) {
			String str = (String) obj;
			if (str.length() > 0)
				return true;
		} else if (obj instanceof Number) {
			return ((Number) obj).doubleValue() >= 0;
		} else if (obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();

		} else if (obj instanceof Iterable) {
			Iterator iter = ((Iterable) obj).iterator();
			if (iter.hasNext())
				return true;
		} else if (obj instanceof Iterator) {
			Iterator iter = (Iterator) obj;
			if (iter.hasNext())
				return true;
		} else if (obj instanceof Enumeration) {
			Enumeration enu = (Enumeration) obj;
			if (enu.hasMoreElements())
				return true;
		} else if (obj instanceof Map) {
			Map map = (Map) obj;
			if (map.size() > 0)
				return true;

		} else if (obj instanceof Object[]) {
			T[] objs = (T[]) obj;
			if (objs.length > 0)
				return true;
		} else if (obj instanceof byte[]) {
			byte[] bytes = (byte[]) obj;
			if (bytes.length > 0)
				return true;

		} else {
			return true;
		}

		return false;
	}

	/**
	 * 널인 경우 체크
	 */
	public static String getValidate(String str) {
		return getValidate(str, "");
	}

	/**
	 * 널인 경우 체크, 널대체값이 있는 경우
	 */
	public static String getValidate(String str, String sNullToStr) {
		return (isValidate(str)) ? str.trim() : sNullToStr;
	}

	/**
	 * 널인 경우 체크
	 */
	public static String getValidateNoTrim(String str) {
		return getValidateNoTrim(str, "");
	}

	/**
	 * 널인 경우 체크, 널대체값이 있는 경우
	 */
	public static String getValidateNoTrim(String str, String sNullToStr) {
		return (isValidate(str)) ? str : sNullToStr;
	}

	public static int getValidate(String str, int iNullToInt) {
		try {
			return (isValidate(str)) ? Integer.parseInt(str.trim()) : iNullToInt;
		} catch (NumberFormatException e) {
			return iNullToInt;
		}
	}

	/**
	 * szOriginal에서 szOld를 모두 szNew로 바꾼다.
	 *
	 * @param szOriginal 원래의 문자열.
	 * @param szOld 바꾸고자하는 문자열.
	 * @param szNew 새로운 문자열.
	 * @return szOriginal 문자열에서 모든 szOld 문자열을 szNew 문자열로 대치한 문자열을 넘긴다.
	 */
	public static String replace(String szOriginal, String szOld, String szNew) {
		return replace(szOriginal, szOld, szNew, 0);
	}

	/**
	 * sszOriginal에서 처음부터 nReplaceCount개만큼 szOld를 szNew로 바꾼다. 만약 nReplaceCount가 0이면 szOld를 모두 szNew로 바꾼다.
	 *
	 * @param szOriginal 원래의 문자열.
	 * @param szOld 바꾸고자하는 문자열.
	 * @param szNew 새로운 문자열.
	 * @param nReplaceCount szOriginal의 처음부터 몇개까지의 szOld를 szNew로 바꿀지를 결정한다. 1이면 처음 나타나는 szOld 문자열만을 szNew로 바꾼다. 0이면 모든
	 *            szOld 문자열을 szNew로 바꾼다.
	 *
	 * @return szOriginal 문자열에서 szOld 문자열을 찾아 nReplaceCount 갯수만큼 szNew 문자열로 대치한 문자열을 넘긴다.
	 */
	public static String replace(String szOriginal, String szOld, String szNew, int nReplaceCount) {
		if (szOriginal == null || szOld == null || szNew == null)
			return szOriginal;

		StringBuilder sbResult = new StringBuilder();
		int nFromIndex = 0, nToIndex = 0;
		int nOldLength = szOld.length();
		int i = 0;

		while ((nToIndex = szOriginal.indexOf(szOld, nFromIndex)) >= 0) {
			sbResult.append(szOriginal.substring(nFromIndex, nToIndex)).append(szNew);
			nFromIndex = nToIndex + nOldLength;

			if (nReplaceCount != 0 && ++i == nReplaceCount)
				return sbResult.append(szOriginal.substring(nFromIndex)).toString();
		}

		return sbResult.append(szOriginal.substring(nFromIndex)).toString();
	}

	/**
	 * 주어진 String의 왼쪽 Space를 모두 없앤다.<BR>
	 * 예) szSource: "  123 " -> Return: "123  " szSource: "  " -> Return: ""
	 */
	public static String leftTrim(String szSource) {
		if (szSource == null)
			return szSource;
		if (szSource.length() == 0)
			return szSource;

		String szResult = szSource;
		int nFirstindex = 0;
		int nLastIndex = szSource.length() - 1;
		while (nLastIndex >= 0 && szResult.charAt(nFirstindex) == ' ') {
			szResult = (szResult.length() == 1 ? "" : szResult.substring(nFirstindex, nLastIndex));
			nFirstindex++;
		}
		return szResult;
	}

	/**
	 * 주어진 String의 오른쪽 Space를 모두 없앤다.<BR>
	 *
	 * <PRE>
	 *      예) szSource: "  123 " -> Return: "   123"
	 *          szSource: "  " -> Return: ""
	 * </PRE>
	 */
	public static String rightTrim(String szSource) {
		if (szSource == null)
			return szSource;
		if (szSource.length() == 0)
			return szSource;

		String szResult = szSource;
		int nLastIndex = szSource.length() - 1;

		while (nLastIndex >= 0 && szResult.charAt(nLastIndex) == ' ') {
			szResult = (szResult.length() == 1 ? "" : szResult.substring(0, nLastIndex));
			nLastIndex--;
		}

		return szResult;
	}

	public static String[] split(String sData, String szDelimiter) {
		return split(sData, szDelimiter, true);
	}

	/**
	 * Delimiter 로 구분된 하나의 Source String 을 Delimiter 단위로 분해하여 Array 타입의 Strings 결과를 Return한다.<BR>
	 * 
	 * <pre>
	 * if true : Null String도 Array의 element도 잡는다.
	 *      예) sData: "|123|456||789|", szDelimiter: "|"
	 *              -> Return: ArrayList {"", "123", "456", "", "789", ""}
	 * if false
	 *      예) sData: "  123 |  456  |  789", szDelimiter: "|"
	 *              -> Return: ArrayList {"123", "456", "789"}
	 * </pre>
	 *
	 * @param sData source str
	 * @param szDelimiter delimiter 로 사용할 str
	 */
	public static String[] split(String sData, String szDelimiter, boolean bIncludeNull) {
		String[] arrStr = new String[0];
		if (!isValidate(sData) || !isValidate(szDelimiter))
			return arrStr;

		StringTokenizer st = new StringTokenizer(sData, szDelimiter, bIncludeNull);
		if (bIncludeNull) {
			List<Object> al = new ArrayList<Object>();
			String szTemp = "";
			boolean bNextDelimiter = false;
			int nCount = 0;

			while (st.hasMoreTokens()) {
				szTemp = st.nextToken();

				if (szTemp.equals(szDelimiter)) {
					if (!bNextDelimiter) {
						al.add("");
					}

					bNextDelimiter = false;
				} else {
					al.add(szTemp);
					bNextDelimiter = true;
				}
			}
			if (!bNextDelimiter)
				al.add("");

			nCount = al.size();
			arrStr = new String[nCount];

			for (int i = 0; i < nCount; i++)
				arrStr[i] = (String) al.get(i);
		} else {
			int nTokenNumber = st.countTokens();
			if (nTokenNumber == 0)
				return null;

			int i = 0;
			arrStr = new String[nTokenNumber];

			while (st.hasMoreTokens())
				arrStr[i++] = st.nextToken().trim();
		}

		return arrStr;
	}

	/**
	 * 주어진 String이 Delimiter로 분리되어 있는 String일 때, Start와 End가 주어지면 Start부터 End까지의 Token을 취해서 Return한다.<BR>
	 *
	 * <PRE>
	 *      예) sData: "123|456|789", szDelimiter: "|", nStartToken: 0, nEndToken: 1
	 *              -> Return: "123|456"
	 * </PRE>
	 */
	public static String splitString(String sData, String szDelimiter, int nStartToken, int nEndToken) {
		if (sData == null || szDelimiter == null || !(nStartToken >= 0 && nEndToken >= nStartToken))
			return sData;

		int i = 0;
		String szResult = "";
		String szNextToken = "";
		StringTokenizer st = new StringTokenizer(sData, szDelimiter, true);

		while (st.hasMoreTokens() && i <= nEndToken) {
			szNextToken = st.nextToken();

			if (szNextToken.equals(szDelimiter))
				szNextToken = "";
			// 이것이 Delimiter.
			else if (st.hasMoreTokens())
				szDelimiter = st.nextToken();

			if (i == nStartToken)
				szResult += szNextToken;
			else if (i > nStartToken)
				szResult += szDelimiter + szNextToken;

			i++;
		}

		return szResult;
	}

	/**
	 * T[]을 중복되지 않게 담는다.
	 */
	public static <T> T[] getDistinct(T[] arrStr) {
		return convertListToArrayDistinct(convertArrayToList(arrStr));
	}

	public static <T> T[] convertListToArrayDistinct(List<T> list) {
		return convertListToArray(distinct(list));
	}

	/**
	 * List<T> 형태를 T[] 형태로 돌려준다.
	 */
	public static <T> T[] convertListToArray(List<T> list) {
		return convertListToArray(list, null);
	}

	/**
	 * List<T> 형태를 원하는 타입의 배열 형태로 돌려준다.
	 */
	public static <T> T[] convertListToArray(List<T> list, Class<T> cls) {
//		T[] arrS = (T[]) new Object[0];
//		if (list != null) {
//			arrS = (T[]) new Object[list.size()];
//			for (int i = 0; i < list.size(); i++) {
//				arrS[i] = list.get(i);
//			}
//		}
//		return arrS;
		T[] arrObj = (T[]) ((cls == null) ? new Object[0] : Array.newInstance(cls, list.size()));
		return list.toArray(arrObj);
	}

	/**
	 * List<T>를 List<T>에 중복되지 않게 담는다.
	 */
	public static <T> List<T> distinct(List<T> list) {
		List<T> al = new ArrayList<T>();
		if (list != null) {
			for (T t : al) {
				if (!al.contains(t))
					al.add(t);
			}
		}

		return al;
	}

	public static String getStringBlank(int iLength) {
		return getStringChar(iLength, " ");
	}

	public static String getStringChar(int iLength, String sChar) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < iLength; i++)
			sb.append(sChar);

		return sb.toString();
	}

	public static boolean isExist(byte[] bytes, int length, String searchVal) {
		return isExist(bytes, 0, length, searchVal, "UTF-8");
	}

	public static boolean isExist(byte[] bytes, int fromIndex, int length, String searchVal, String enc) {
		if (bytes.length < length)
			return false;

		byte[] _temp = new byte[length];
		System.arraycopy(bytes, fromIndex, _temp, 0, length);
		String temp = "";
		try {
			temp = new String(_temp, enc);
		} catch (Exception e) {
			e.printStackTrace();
//			LogUtil.writeLog(e, Utils.class);
		}

		return (temp.indexOf(searchVal) >= 0);
	}

	/**
	 * startStr 뒤에 insertStr 를 삽입한다.
	 * 
	 * @param str
	 * @param startStr
	 * @param insertStr
	 * @return
	 */
	public static String insertString(String str, String startStr, String insertStr) {
		if (!(Utils.isValidate(str) && Utils.isValidate(startStr) && Utils.isValidate(insertStr))) {
			return str;
		}

		int startIdx = str.indexOf(startStr);

		return str.substring(0, startIdx) + str.substring(startIdx, startIdx + startStr.length()) + " " + insertStr
				+ " " + str.substring(startIdx + startStr.length());
	}

	/**
	 * startStr 와 endStr 사이를 replaceStr 로 대체한다.
	 * 
	 * @param str
	 * @param startStr
	 * @param endStr
	 * @param replaceStr
	 * @return
	 */
	public static String replaceString(String str, String startStr, String endStr, String replaceStr) {
		if (!Utils.isValidate(str) || !Utils.isValidate(startStr) || !Utils.isValidate(endStr)) {
			return str;
		}

		int startIdx = str.indexOf(startStr);
		int endIdx = str.indexOf(endStr);

		return str.substring(0, startIdx) + str.substring(startIdx, startIdx + startStr.length()) + " " + replaceStr
				+ " " + str.substring(endIdx);
	}

	/**
	 * 마지막 startStr 뒤에 replaceStr 를 대체한다.
	 * 
	 * @param str
	 * @param startStr
	 * @param replaceStr
	 * @return
	 */
	public static String replaceLastString(String str, String startStr, String replaceStr) {
		if (!Utils.isValidate(str) || !Utils.isValidate(startStr)) {
			return str;
		}

		int startIdx = str.lastIndexOf(startStr);

		return str.substring(0, startIdx) + str.substring(startIdx, startIdx + startStr.length()) + " " + replaceStr;
	}

	/**
	 * startStr 와 마지막 endStr 사이를 replaceStr 로 대체한다.
	 * 
	 * @param str
	 * @param startStr
	 * @param endStr
	 * @param replaceStr
	 * @return
	 */
	public static String replaceLastString(String str, String startStr, String endStr, String replaceStr) {
		if (!Utils.isValidate(str) || !Utils.isValidate(startStr) || !Utils.isValidate(endStr)) {
			return str;
		}

		int startIdx = str.indexOf(startStr);
		int endIdx = str.lastIndexOf(endStr);

		return str.substring(0, startIdx) + str.substring(startIdx, startIdx + startStr.length()) + " " + replaceStr
				+ " " + str.substring(endIdx);
	}

	// Date --------------------------------------------------------------------

	// ex) 2006
	public static String SDF_YEAR = "yyyy";
	// ex) 12
	public static String SDF_MONTH = "MM";
	// ex) 11
	public static String SDF_DAY = "dd";
	// ex) 오후
	public static String SDF_APM = "a";
	// ex) 13
	public static String SDF_HOUR = "HH";
	// ex) 01
	public static String SDF_HOUR2 = "hh";
	// ex) 1
	public static String SDF_HOUR3 = "h";
	// ex) 10
	public static String SDF_MINUTE = "mm";
	// ex) 20
	public static String SDF_SECOND = "ss";
	// ex) 999
	public static String SDF_MILLI_SECOND = "SSS";

	// ex) 20061211
	public static String SDF_DATE = SDF_YEAR + SDF_MONTH + SDF_DAY;

	// ex) 131020
	public static String SDF_TIME = SDF_HOUR + SDF_MINUTE + SDF_SECOND;

	// ex) 131020999
	public static String SDF_MILLI_TIME = SDF_TIME + SDF_MILLI_SECOND;

	// ex) 13:10:20
	public static String SDF_TIME1 = SDF_HOUR + ":" + SDF_MINUTE + ":" + SDF_SECOND;
	// ex) 오후 01:10:20
	public static String SDF_TIME2 = SDF_APM + " " + SDF_HOUR2 + ":" + SDF_MINUTE + ":" + SDF_SECOND;
	// ex) 오후 1:10:20
	public static String SDF_TIME3 = SDF_APM + " " + SDF_HOUR3 + ":" + SDF_MINUTE + ":" + SDF_SECOND;

	// ex) 13:10:20:999
	public static String SDF_MILLI_TIME1 = SDF_TIME1 + ":" + SDF_MILLI_SECOND;

	// ex) 20061211_131020
	public static String SDF_DATE_TIME = SDF_DATE + "_" + SDF_TIME;
	// ex) 20061211 13:10:20
	public static String SDF_DATE_TIME1 = SDF_DATE + " " + SDF_TIME1;
	// ex) 20061211 오후 01:10:20
	public static String SDF_DATE_TIME2 = SDF_DATE + " " + SDF_TIME2;
	// ex) 20061211 오후 1:10:20
	public static String SDF_DATE_TIME3 = SDF_DATE + " " + SDF_TIME3;

	// ex) 20061211_131020999
	public static String SDF_DATE_MILLI_TIME = SDF_DATE + "_" + SDF_MILLI_TIME;

	// ex) 2006-12-11
	public static String SDF_DATE_HYPEN = SDF_YEAR + "-" + SDF_MONTH + "-" + SDF_DAY;
	// ex) 2006-12-11 13:10:20
	public static String SDF_DATE_HYPEN_TIME1 = SDF_DATE_HYPEN + " " + SDF_TIME1;
	// ex) 2006-12-11 오후 01:10:20
	public static String SDF_DATE_HYPEN_TIME2 = SDF_DATE_HYPEN + " " + SDF_TIME2;
	// ex) 2006-12-11 오후 1:10:20
	public static String SDF_DATE_HYPEN_TIME3 = SDF_DATE_HYPEN + " " + SDF_TIME3; // Oracle Default Format

	// ex) 2006-12-11 13:10:20:999
	public static String SDF_DATE_HYPEN_MILLI_TIME1 = SDF_DATE_HYPEN + " " + SDF_MILLI_TIME1;

	// ex) 2006.12.11
	public static String SDF_DATE_DOT = SDF_YEAR + "." + SDF_MONTH + "." + SDF_DAY;
	// ex) 2006.12.11 13:10:20
	public static String SDF_DATE_DOT_TIMES1 = SDF_DATE_DOT + " " + SDF_TIME1;
	// ex) 2006.12.11 오후 01:10:20
	public static String SDF_DATE_DOT_TIME2 = SDF_DATE_DOT + " " + SDF_TIME2;
	// ex) 2006.12.11 오후 1:10:20
	public static String SDF_DATE_DOT_TIME3 = SDF_DATE_DOT + " " + SDF_TIME3;

	/**
	 * 시스템의 현재 날짜를 구한다.
	 */
	public static String formatCurrentDate() {
		return convertDateToString(new Date());
	}

	public static String formatCurrentTime() {
		return convertTimeToString(new Date());
	}

	public static String formatCurrentDateTime() {
		return convertDateTimeToString(new Date());
	}

	public static String formatCurrentDate(String datePattern) {
		return formatCurrentDate(new SimpleDateFormat(datePattern));
	}

	public static String formatCurrentDate(SimpleDateFormat simpleDateFormat) {
		return convertDateToString(new Date(), simpleDateFormat);
	}

	public static String convertDateToString(long date) {
		return convertDateToString(new Date(date));
	}

	public static String convertDateToString(java.util.Date date) {
		return convertDateToString(date, new SimpleDateFormat(SDF_DATE));
	}

	public static String convertTimeToString(java.util.Date date) {
		return convertDateToString(date, new SimpleDateFormat(SDF_TIME));
	}

	public static String convertDateTimeToString(java.util.Date date) {
		return convertDateToString(date, new SimpleDateFormat(SDF_DATE_TIME));
	}

	public static String convertDateToString(long date, SimpleDateFormat simpleDateFormat) {
		return convertDateToString(new Date(date), simpleDateFormat);
	}

	public static String convertDateToString(java.util.Date date, SimpleDateFormat simpleDateFormat) {
		String szResult = "";
		if (date != null && simpleDateFormat != null)
			szResult = simpleDateFormat.format(date);

		return szResult;
	}

	public static int formatIntervalHour(Calendar cStart, Calendar cEnd) {
		cStart.set(Calendar.MINUTE, 0);
		cStart.set(Calendar.SECOND, 0);
		cStart.set(Calendar.MILLISECOND, 0);

		cEnd.set(Calendar.MINUTE, 0);
		cEnd.set(Calendar.SECOND, 0);
		cEnd.set(Calendar.MILLISECOND, 0);

		return (int) ((cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / 1000 / 60 / 60);
	}

	public static int getIntervalMinute(Calendar cStart, Calendar cEnd) {
		cStart.set(Calendar.SECOND, 0);
		cStart.set(Calendar.MILLISECOND, 0);

		cEnd.set(Calendar.SECOND, 0);
		cEnd.set(Calendar.MILLISECOND, 0);

		return (int) ((cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / 1000 / 60);
	}

	public static int getIntervalSecond(Calendar cStart, Calendar cEnd) {
		cStart.set(Calendar.MILLISECOND, 0);

		cEnd.set(Calendar.MILLISECOND, 0);

		return (int) ((cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / 1000);
	}

	public static int getIntervalSecond(Date dStart, Date dEnd) {
		return (int) ((dEnd.getTime() - dStart.getTime()) / 1000);
	}

	/**
	 * 시분초로 변경한다.
	 * 
	 * @param sec
	 * @return 시:분:초(00:00:00)
	 */
	public static String convertSecToString(int seconds) {
		return String.format("%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 3600 % 60);
	}

	// File --------------------------------------------------------------------

	/**
	 * 파일명을 구한다.
	 */
	public static String getFileName(String szFileName) {
		String szName = szFileName;
		if (isValidate(szFileName)) {
			int index = szFileName.lastIndexOf(File.separatorChar);
			if (index < (szFileName.length() - 1) && index != -1)
				szName = szFileName.substring(index + 1);
		}

		return szName;
	}

	/**
	 * 파일명을 구한다.
	 */
	public static String getFileName2(String filePath) {
		int lastUnixPos = filePath.lastIndexOf("/");
		int lastWindowsPos = filePath.lastIndexOf("\\");

		return filePath.substring(Math.max(lastUnixPos, lastWindowsPos) + 1);
	}

	/**
	 * 파일의 파일명을 구한다.
	 */
	public static String getFileNameOnly(String szFileName) {
		if (szFileName == null)
			return szFileName;

		szFileName = getFileName(szFileName);
		if (szFileName == null)
			return szFileName;

		int index = szFileName.lastIndexOf(".");
		if (index < (szFileName.length() - 1) && index != -1)
			szFileName = szFileName.substring(0, index);

		return szFileName;
	}

	/**
	 * 파일의 확장자를 구한다.
	 */
	public static String getFileType(String szFileName) {
		if (!isValidate(szFileName))
			return "";

		int index = szFileName.lastIndexOf(".");
		if (index < (szFileName.length() - 1) && index != -1)
			return szFileName.substring(index + 1);
		else
			return "";
	}

	/**
	 * 확장자에 해당하는 이미지파일명을 구한다.
	 */
	public static String getImageFileName(String extension) {
		String imageFileName = "";
		if (extension != null) {
			String fileName = "ico_" + extension + ".gif";
			if (isExistFileType(extension))
				imageFileName = fileName;
			else
				imageFileName = "ico_txt.gif";
		}

		return imageFileName;
	}

	/**
	 * 확장자에 해당하는 이미지파일의 존재여부를 구한다.
	 */
	protected static boolean isExistFileType(String extension) {
		boolean existFileType = false;
		if (isValidate(extension)) {
			for (String fileType : FILE_TYPES) {
				if (extension.equalsIgnoreCase(fileType))
					existFileType = true;
			}
		}

		return existFileType;
	}

	// Properties --------------------------------------------------------------

	/**
	 * Load the properties file as system properties.
	 *
	 * @param szPropertiesFile properties file path.
	 * @param bSystemProperties if true, System properties is used, otherwise arbitary properties object is created.
	 * @return Properties
	 */
	public static Properties loadProperties(String szPropertiesFile, boolean bSystemProperties) {
		Properties p = new Properties();
		if (!isValidate(szPropertiesFile))
			return p;

		// install properties.
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(szPropertiesFile);
			p = (bSystemProperties) ? System.getProperties() : new Properties();
			load(p, fis);
		} catch (Exception e) {
			new Exception("PropertyFile [" + szPropertiesFile + "] Not Found.", e).printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
//					LogUtil.writeLog(e, Utils.class);
				}
			}
		}

		return p;
	}

	// 다음은 변수 처리 부분이다. 변수는 ${String} 의 형태이다.
	public static String processPropertyVariables(Properties p, String szLine) {
		int nVarStart;
		int nVarEnd;
		while ((nVarStart = szLine.indexOf("${")) != -1) {
			nVarEnd = szLine.indexOf("}", nVarStart);
			if (nVarEnd == -1)
				throw new RuntimeException("} Not found Properties Variable in Line: " + szLine);

			String szVariable = szLine.substring(nVarStart + 2, nVarEnd);
			String szReplace = null;
			if (p != null)
				szReplace = p.getProperty(szVariable);

			// 원래의 properties에 없다면, 시스템 properties에서 가져오도록 하자.
			if (szReplace == null) {
				szReplace = System.getProperty(szVariable);
				throw new RuntimeException("Undefined Properties Variable : ${" + szVariable + "}");
			}

			// '/' 코드가 convert 되므로 처리해 줘야 한다.
			szReplace = replace(szReplace, "\\", "\\\\");
			szLine = replace(szLine, "${" + szVariable + "}", szReplace);
		}

		return szLine;
	}

	protected static synchronized void load(Properties p, InputStream inStream) throws IOException {
		String keyValueSeparators = "=: \t\r\n\f";
		String strictKeyValueSeparators = "=:";
		String whiteSpaceChars = " \t\r\n\f";
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "ksc5601"));

		while (true) {
			// Get next line
			String line = in.readLine();
			if (line == null)
				return;

			if (isValidate(line)) {
				// Continue lines that end in slashes if they are not comments
				char firstChar = line.charAt(0);
				if ((firstChar != '#') && (firstChar != '!')) {
					while (continueLine(line)) {
						String nextLine = in.readLine();
						if (nextLine == null)
							nextLine = new String("");
						String loppedLine = line.substring(0, line.length() - 1);

						// Advance beyond whitespace on new line
						int startIndex = 0;
						for (startIndex = 0; startIndex < nextLine.length(); startIndex++) {
							if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1)
								break;
						}
						nextLine = nextLine.substring(startIndex, nextLine.length());
						line = new String(loppedLine + nextLine);
					}

					// 변수를 처리한다.
					line = processPropertyVariables(p, line);

					// Find start of key
					int len = line.length();
					int keyStart;
					for (keyStart = 0; keyStart < len; keyStart++) {
						if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
							break;
					}

					// Blank lines are ignored
					if (keyStart == len)
						continue;

					// Find separation between key and value
					int separatorIndex;
					for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
						char currentChar = line.charAt(separatorIndex);
						if (currentChar == '\\')
							separatorIndex++;
						else if (keyValueSeparators.indexOf(currentChar) != -1)
							break;
					}

					// Skip over whitespace after key if any
					int valueIndex;
					for (valueIndex = separatorIndex; valueIndex < len; valueIndex++) {
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
							break;
					}

					// Skip over one non whitespace key value separators if any
					if (valueIndex < len) {
						if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1)
							valueIndex++;
					}

					// Skip over white space after other separators if any
					while (valueIndex < len) {
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
							break;
						valueIndex++;
					}
					String key = line.substring(keyStart, separatorIndex);
					String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

					// Convert then store key and value
					key = loadConvert(key);
					value = loadConvert(value);
					p.put(key, value);
				}
			}
		}
	}

	/*
	 * Returns true if the given line is a line that must be appended to the next line copy from Properties Source
	 */
	protected static boolean continueLine(String line) {
		int slashCount = 0;
		int index = line.length() - 1;
		while ((index >= 0) && (line.charAt(index--) == '\\')) {
			slashCount++;
		}

		return (slashCount % 2 == 1);
	}

	/*
	 * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their original forms copy from
	 * Properties Source
	 */
	protected static String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuilder outBuffer = new StringBuilder(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';

					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}

		return outBuffer.toString();
	}

	// Format ------------------------------------------------------------------

	public static final int SIZE_FULL = 0x0001;
	public static final int SIZE_KILO = 0x0002;

	public static String getFileSizeByString(long lValue, int nType) {
		String szResult = "";
		switch (nType) {
			case SIZE_FULL:
				if (1048576.0 < lValue) { // 1024 * 1024
					DECIMAL_FORMAT.applyPattern("#,##0.0#");
					szResult = DECIMAL_FORMAT.format(lValue / 1048576.0) + " MB";
				} else {
					DECIMAL_FORMAT.applyPattern("#,##0");
					szResult = DECIMAL_FORMAT.format((lValue + 999) / 1024) + " KB";
				}
				DECIMAL_FORMAT.applyPattern("#,##0");
				szResult = szResult + " ( " + DECIMAL_FORMAT.format(lValue) + " Byte(s))";
				break;
			case SIZE_KILO:
				DECIMAL_FORMAT.applyPattern("#,##0");
				szResult = DECIMAL_FORMAT.format((lValue + 999) / 1024) + " KB";
				break;
		}

		return szResult;
	}

	public static String changeCipher(String sValue) {
		if (!isValidate(sValue))
			return "0";

		return changeCipher(Long.parseLong(sValue));
	}

	public static String changeCipher(long lValue) {
		DECIMAL_FORMAT.applyPattern("#,##0");
		return DECIMAL_FORMAT.format(lValue);
	}

	public static String changeCipher(double dValue) {
		DECIMAL_FORMAT.applyPattern("#,##0.0");
		return DECIMAL_FORMAT.format(dValue);
	}

	/**
	 * This method is used to change to leave cipher of point number.
	 *
	 * @param num change str number
	 * @param point leave cipher
	 * @return point number
	 */
	public static String changeCipher(String num, int point) {
		String result = num;
		if (!isValidate(num))
			return result;

		return changeCipher(Double.parseDouble(num), point);
	}

	/**
	 * This method is used to change to leave cipher of point number.
	 *
	 * @param dNum change double number
	 * @param point leave cipher
	 * @return point number
	 */
	public static String changeCipher(double dNum, int point) {
		String result = "";
		if (dNum < 0 || point < 0)
			return result;

		switch (point) {
			case 0:
				DECIMAL_FORMAT.applyPattern("0");
				result = DECIMAL_FORMAT.format(dNum);
				break;
			case 1:
				DECIMAL_FORMAT.applyPattern("0.0");
				result = DECIMAL_FORMAT.format(dNum);
				break;
			case 2:
				DECIMAL_FORMAT.applyPattern("0.00");
				result = DECIMAL_FORMAT.format(dNum);
				break;
			case 3:
				DECIMAL_FORMAT.applyPattern("0.000");
				result = DECIMAL_FORMAT.format(dNum);
		}

		return result;
	}

	// Convert -----------------------------------------------------------------

	/**
	 * 문자를 숫자로 바꾸어 준다.
	 */
	public static int convertInt(String str) {
		return (int) convertLong(str);
	}

	/**
	 * 문자를 숫자로 바꾸어 준다.
	 */
	public static long convertLong(String str) {
		return convertLong(str, "0");
	}

	/**
	 * 문자를 숫자로 바꾸어 준다.
	 */
	public static long convertLong(String str, String sNullToStr) {
		return Long.parseLong((isValidate(str)) ? str : sNullToStr);
	}

	/**
	 * 1 이면 "true", 1 이 아니면 "false" 를 반환한다.
	 */
	public static String convertBooleanString(int i) {
		return (i == 1) ? "true" : "false";
	}

	/**
	 * boolean값을 얻어온다.
	 *
	 * @return boolean 상태값 ("TRUE" or "true": true, "FALSE" or "false": false)
	 */
	public static boolean convertStringToBoolean(String str) {
		return (str != null && str.equalsIgnoreCase("true")) ? true : false;
	}

	/**
	 * boolean값을 얻어온다.
	 *
	 * @return boolean 상태값 ("Y": true, "N": false)
	 */
	public static boolean convertStringToBoolean2(String str) {
		return (isValidate(str)) ? convertCharToBoolean(str.charAt(0)) : false;
	}

	/**
	 * boolean값을 얻어온다.
	 *
	 * @return boolean 상태값 (‘Y’ or 'y': true, ‘N’ or 'n': false)
	 */
	public static boolean convertCharToBoolean(char ch) {
		return (ch == 'Y' || ch == 'y') ? true : false;
	}

	public static char convertStringToChar(String str) {
		return convertStringToChar(str, ' ');
	}

	/**
	 * 문자열의 첫번째 문자를 얻어온다.
	 */
	public static char convertStringToChar(String str, char ch) {
		return (isValidate(str) && str.length() > 0) ? str.charAt(0) : ch;
	}

	/**
	 * char값을 얻어온다.
	 *
	 * @return char 상태값 (true: ‘Y’, false: ‘N’)
	 */
	public static char convertBooleanToChar(boolean b) {
		return (b) ? 'Y' : 'N';
	}

	/**
	 * Format) "2007-06-21 13:01:10"
	 */
	public static Timestamp convertStringToTimestamp(String sDateTime) {
		if (!isValidate(sDateTime) || sDateTime.length() < 19)
			return null;

//		return new Timestamp(Integer.parseInt(sDateTime.substring(0, 4)), Integer.parseInt(sDateTime.substring(5, 7)),
//				Integer.parseInt(sDateTime.substring(8, 10)), Integer.parseInt(sDateTime.substring(11, 13)),
//				Integer.parseInt(sDateTime.substring(14, 16)), Integer.parseInt(sDateTime.substring(17, 19)), 0);
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.parseInt(sDateTime.substring(0, 4)), Integer.parseInt(sDateTime.substring(5, 7)) - 1,
				Integer.parseInt(sDateTime.substring(8, 10)), Integer.parseInt(sDateTime.substring(11, 13)),
				Integer.parseInt(sDateTime.substring(14, 16)), Integer.parseInt(sDateTime.substring(17, 19)));
		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * List<List<T>> 형태를 Map<T, T> 형태로 변환한다.
	 */
	public static <T> Map<T, T> convertListToMap(List<List<T>> list) {
		Map<T, T> m = new LinkedHashMap<T, T>();
		if (list != null && list.size() > 0 && list.get(0) != null && ((List<T>) list.get(0)).size() == 2) {
			for (List<T> list2 : list) {
				m.put(list2.get(0), list2.get(1));
			}
		}

		return m;
	}

	/**
	 * T[] 형태를 List<T> 형태로 변환한다.
	 */
	public static <T> List<T> convertArrayToList(T[] arrObj) {
//		List<T> al = new ArrayList<T>();
//		if (arrObj != null) {
//			for (T t : arrObj) {
//				al.add(t);
//			}
//		}
//
//		return al;
		return Arrays.asList(arrObj);
	}

	/**
	 * List<List<T>> 형태를 T[][] 형태로 변환한다.
	 */
	public static <T> T[][] convertStringToArray(List<List<T>> list) {
		T[][] arrS = (T[][]) new Object[0][0];
		if (list != null && list.get(0) != null) {
			arrS = (T[][]) new Object[list.size()][list.get(0).size()];
			List<T> list2 = null;
			for (int i = 0; i < list.size(); i++) {
				list2 = list.get(i);
				for (int j = 0; j < list2.size(); j++) {
					arrS[i][j] = list2.get(j);
				}
			}
		}

		return arrS;
	}

	/**
	 * List<List<String>> 형태를 String 형태의 구분자로 변환한다.
	 */
	public static String convertListToString(List<List<String>> list, String delimiter) {
		String s = "";
		if (list != null) {
			StringBuilder sbResult = new StringBuilder();
			for (List<String> list2 : list) {
				for (int i = 0; i < list2.size(); i++) {
					sbResult.append((String) list2.get(i) + delimiter);
				}
			}
			s = sbResult.toString();
		}

		return s;
	}

	/**
	 * String[] 형태를 int[] 형태로 변환한다.
	 */
	public static int[] convertArrayToInt(String[] arrStr) {
		int[] arrInt = new int[0];
		if (arrStr != null) {
			arrInt = new int[arrStr.length];
			for (int i = 0; i < arrStr.length; i++)
				arrInt[i] = Integer.parseInt(arrStr[i]);
		}

		return arrInt;
	}

	/**
	 * List<Integer> 형태를 int[] 형태로 변환한다.
	 */
	public static int[] convertListToInt(List<Integer> list) {
		int[] arrInt = new int[0];
		if (list != null) {
			arrInt = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof Integer)
					arrInt[i] = ((Integer) list.get(i)).intValue();
			}
		}

		return arrInt;
	}

	/**
	 * Map<T1, T2> 형태의 values를 T2[] 형태로 돌려준다.
	 */
	public static <T1, T2> T2[] convertMapValueToArray(Map<T1, T2> map) {
		return convertMapValueToArray(map, null);
	}

	/**
	 * Map<T1, T2> 형태의 values를 원하는 타입의 배열 형태로 돌려준다.
	 */
	public static <T1, T2> T2[] convertMapValueToArray(Map<T1, T2> map, Class<T2> cls) {
		T2[] arrObj = (T2[]) ((cls == null) ? new Object[0] : Array.newInstance(cls, map.size()));

		return map.values().toArray(arrObj);
	}

	/**
	 * Map<T1, T2> 형태의 keys를 T1[] 형태로 돌려준다.
	 */
	public static <T1, T2> T1[] convertMapKeyToArray(Map<T1, T2> map) {
		return convertMapKeyToArray(map, null);
	}

	/**
	 * Map<T1, T2> 형태의 keys를 원하는 타입의 배열 형태로 돌려준다.
	 */
	public static <T1, T2> T1[] convertMapKeyToArray(Map<T1, T2> map, Class<T1> cls) {
		T1[] arrObj = (T1[]) ((cls == null) ? new Object[0] : Array.newInstance(cls, map.size()));

		return map.keySet().toArray(arrObj);
	}

	/**
	 * T[] 형태의 String[] 형태로 돌려준다. (JDK ver 1.6 이상에서 동작)
	 */
	public static <T> String[] convertArrayToArray(T[] objs) {
		return Arrays.copyOf(objs, objs.length, String[].class);
	}

	/**
	 * Map<T1, T2> 형태의 Set을 Map.Entry<T1, T2>[] 형태로 돌려준다.
	 */
	public static <T1, T2> Map.Entry<T1, T2>[] convertMapToEntryArray(Map<T1, T2> map) {
		return map.entrySet().toArray(new Map.Entry[map.size()]);
	}

	/**
	 * char 문자열을 16진수 문자열로 변환한다.(char(1) list -> hex(2) list)
	 */
	public static String convertCharToHex(String sChars) {
		return convertCharToHex(sChars, false);
	}

	/**
	 * char 문자열을 16진수 문자열로 변환한다.(char(1) list -> hex(2) list)
	 */
	public static String convertCharToHex(byte[] arrbChar) {
		return convertCharToHex(arrbChar, false);
	}

	/**
	 * char 문자열을 16진수 문자열로 변환한다.(char(1) list -> hex(2) list) char 문자열을 XOR 한 체크 문자를 검증한다.
	 */
	public static String convertCharToHex(String sChars, boolean bCheck) {
		return convertCharToHex(sChars.getBytes(), bCheck);
	}

	/**
	 * char 문자 배열을 16진수 문자열로 변환한다.(char(1) list -> hex(2) list) char 문자열을 XOR 한 체크 문자를 검증한다.
	 */
	public static String convertCharToHex(byte[] arrbChar, boolean bCheck) {
		String sResult = null;
		if (arrbChar == null || arrbChar.length < 1)
			return sResult;

		// check(XOR)
		byte[] arrbCharCheck = new byte[arrbChar.length - 1];
		System.arraycopy(arrbChar, 0, arrbCharCheck, 0, arrbChar.length - 1);
		// String sHexCheck = getCheckXORCharToHex(arrbCharCheck);

		sResult = "";
		String sHex = null;
		for (int i = 0; i < arrbChar.length; i++) {
			sHex = Integer.toHexString(arrbChar[i]).toUpperCase();
			sHex = (sHex.length() == 1) ? "0" + sHex : sHex;
			sResult += sHex;

			// check(XOR)
			// if (bCheck && i == arrbChar.length - 1 && !sHexCheck.equals(sHex))
			// sResult = null;
		}

		return sResult;
	}

	/**
	 * char 문자열을 XOR 한 체크 문자를 구한다.
	 */
	public static String getCheckXORCharToHex(String sChars) {
		return getCheckXORCharToHex(sChars.getBytes());
	}

	/**
	 * char 문자 배열을 XOR 한 체크 문자를 구한다.
	 */
	public static String getCheckXORCharToHex(byte[] arrbChar) {
		String sResult = null;
		if (arrbChar == null || arrbChar.length < 1)
			return sResult;

		int iByte = 0;
		int iByte2 = 0;
		for (int i = 0; i < arrbChar.length; i++) {
			iByte = (i == 0) ? arrbChar[i] : arrbChar[i] ^ iByte2;
			iByte2 = iByte;
		}
		sResult = Integer.toHexString(iByte).toUpperCase();
		sResult = (sResult.length() == 1) ? "0" + sResult : sResult;

		return sResult;
	}

	/**
	 * 16진수 문자열을 char 문자열로 변환한다.(hex(2) list -> char(1) list)
	 */
	public static String convertHexToChar(String sHexs) {
		String sResult = null;
		if (!isValidate(sHexs) || sHexs.length() < 2)
			return sResult;

		sResult = "";
		for (int i = 0; i < sHexs.length(); i = i + 2)
			sResult += (char) Integer.parseInt(sHexs.substring(i, i + 2), 16);

		return sResult;
	}

	/**
	 * 16진수 문자열을 XOR 한 체크 문자를 구한다.
	 */
	public static String getCheckXORHexToChar(String sHexs) {
		return getCheckXORHexToChar(sHexs.getBytes());
	}

	/**
	 * 16진수 문자 배열을 XOR 한 체크 문자를 구한다.
	 */
	public static String getCheckXORHexToChar(byte[] arrbHex) {
		String sResult = null;
		if (arrbHex == null || arrbHex.length < 2)
			return sResult;

		int iByte = 0;
		int iByte2 = 0;
		for (int i = 0; i < arrbHex.length; i = i + 2) {
			iByte = Integer.parseInt(new String(new byte[] { arrbHex[i], arrbHex[i + 1] }), 16);
			iByte = (i == 0) ? iByte : iByte ^ iByte2;
			iByte2 = iByte;
		}
		sResult = Integer.toHexString(iByte).toUpperCase();
		sResult = (sResult.length() == 1) ? "0" + sResult : sResult;

		return sResult;
	}

	public static <E> List<E> convertList(Iterable<E> iterable) {
		if (iterable instanceof List) {
			return (List<E>) iterable;
		}

		List<E> list = new ArrayList<E>();
		if (iterable != null) {
			for (E e : iterable) {
				list.add(e);
			}
		}

		return list;
	}

	public static Map<String, String> convertMapToMap(Map<String, Object> map) {
		Map<String, String> map2 = new HashMap<String, String>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry == null || entry.getValue() == null)
					continue;

				map2.put(toString(entry.getKey()), toString(entry.getValue()));
			}
		}

		return map2;
	}

	public static <T> T convertMapToObject(Map<String, ?> map, Class<T> resultType) throws RuntimeException {
		T obj = null;
		try {
			obj = resultType.newInstance();
			for (Map.Entry<String, ?> entry : map.entrySet()) {
				if (entry == null || entry.getValue() == null)
					continue;

				// java.lang.IllegalArgumentException: Can not set java.lang.Long field com.nemustech.common.model.Sample.id to java.lang.String
				Field field = null;
				try {
					field = resultType.getDeclaredField(entry.getKey());
				} catch (NoSuchFieldException e) {
					continue;
				}
				field.setAccessible(true);
				field.set(obj, entry.getValue());
			}
		} catch (Exception e) {
			throw new RuntimeException("Read map to object \"" + map + "\" error", e);
		}

		return obj;
	}

	public static Map<String, Object> convertObjectToMap(Object obj, String... excludeKeys) throws RuntimeException {
		return convertObjectToMap(obj, null, excludeKeys);
	}

	public static Map<String, Object> convertObjectToMap(Object obj, Map<String, Object> includeMap,
			String... excludeKeys) throws RuntimeException {
		Map<String, Object> convertMap = convertObjectToMap(obj);

		return filter(convertMap, includeMap, excludeKeys);
	}

	public static Map<String, Object> convertObjectToMap(Object obj) throws RuntimeException {
		return convertObjectToMap(obj, true);
	}

	public static Map<String, Object> convertObjectToMap(Object obj, boolean isNullValue) throws RuntimeException {
		return convertObjectToMap(obj, null, isNullValue);
	}

	public static Map<String, Object> convertObjectToMap(Object obj, String deleteValue, boolean isNullValue)
			throws RuntimeException {
		return convertObjectToMap(obj, deleteValue, "", isNullValue);
	}

	public static Map<String, Object> convertObjectToMap(Object obj, String deleteValue, String keyPrefix,
			boolean isNullValue) throws RuntimeException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Class<?> cls = obj.getClass();
			while (cls != null) {
				for (Field field : cls.getDeclaredFields()) {
					field.setAccessible(true);
					Object value = field.get(obj);
					if (isNullValue || value != null) {
						if (deleteValue != null && value instanceof String) {
							value = replace((String) value, deleteValue, "");
						}
						map.put(keyPrefix + field.getName(), value);
					}
				}
				cls = cls.getSuperclass();
			}
		} catch (Exception e) {
			throw new RuntimeException("Convert object to map \"" + obj + "\" error", e);
		}

		return map;
	}

	// ToString ----------------------------------------------------------------

	public static <T> String toString(T obj) {
		return toString(null, obj);
	}

	public static <T> String toString(String message, T obj) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (obj instanceof Object[])
			sb.append(toString((Object[]) obj));
		else if (obj instanceof Object[][])
			sb.append(toString((Object[][]) obj));
		else {
			if (isValidate(obj)) {
				if (obj instanceof Iterable) {
					if (obj instanceof Collection) {
						Collection<Object> col = (Collection) obj;
						sb.append(toString(col.iterator(), col.size()));
					} else
						sb.append(toString(((Iterable) obj).iterator()));
				} else if (obj instanceof Iterator) {
					sb.append(toString((Iterator) obj));
				} else if (obj instanceof Enumeration) {
					sb.append(toString((Enumeration) obj));
				} else if (obj instanceof Map) {
					sb.append(toString((Map) obj));
				} else if (obj instanceof Throwable) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					((Throwable) obj).printStackTrace(pw);
					sb.append(sw);
				} else {
					sb.append(obj);
				}
			}

		}

		return sb.toString();
	}

	public static <T> String toString(T[] arrObj) {
		StringBuilder sb = new StringBuilder();
		if (arrObj != null) {
			sb.append(arrObj.length);
//			for (int i = 0; i < arrObj.length; i++) {
//				if (i == 0)
//					sb.append("[" + toString(arrObj[i]));
//				else
//					sb.append(", " + toString(arrObj[i]));
//			}
//			sb.append("]");
		}
		sb.append(Arrays.toString(arrObj));

		return sb.toString();
	}

	public static <T> String toString(T[][] arrObj) {
		StringBuilder sb = new StringBuilder();
		if (arrObj != null) {
			sb.append(arrObj.length);
			for (int i = 0; i < arrObj.length; i++) {
				if (i == 0)
					sb.append("[" + toString(arrObj[i]));
				else
					sb.append(", " + toString(arrObj[i]));
			}
			sb.append("]");
		}

		return sb.toString();
	}

	public static String toString(int[][] arrInt) {
		return toString(null, arrInt);
	}

	public static String toString(String message, int[][] arrInt) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrInt != null) {
			sb.append(arrInt.length);
			for (int i = 0; i < arrInt.length; i++) {
				if (i == 0)
					sb.append("[" + toString(arrInt[i]));
				else
					sb.append(", " + toString(arrInt[i]));
			}
			sb.append("]");
		}

		return sb.toString();
	}

	public static String toString(int[] arrInt) {
		return toString(null, arrInt);
	}

	public static String toString(String message, int[] arrInt) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrInt != null) {
			sb.append(arrInt.length);
//			for (int i = 0; i < arrInt.length; i++) {
//				if (i == 0)
//					sb.append("[" + arrInt[i]);
//				else
//					sb.append(", " + arrInt[i]);
//			}
//			sb.append("]");
		}
		sb.append(Arrays.toString(arrInt));

		return sb.toString();
	}

	public static String toString(float[][] arrFloat) {
		return toString(null, arrFloat);
	}

	public static String toString(String message, float[][] arrFloat) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrFloat != null) {
			sb.append(arrFloat.length);
			for (int i = 0; i < arrFloat.length; i++) {
				if (i == 0)
					sb.append("[" + toString(arrFloat[i]));
				else
					sb.append(", " + toString(arrFloat[i]));
			}
			sb.append("]");
		}

		return sb.toString();
	}

	public static String toString(float[] arrFloat) {
		return toString(null, arrFloat);
	}

	public static String toString(String message, float[] arrFloat) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrFloat != null) {
			sb.append(arrFloat.length);
//			for (int i = 0; i < arrFloat.length; i++) {
//				if (i == 0)
//					sb.append("[" + arrFloat[i]);
//				else
//					sb.append(", " + arrFloat[i]);
//			}
//			sb.append("]");
		}
		sb.append(Arrays.toString(arrFloat));

		return sb.toString();
	}

	public static String toString(double[][] arrDouble) {
		return toString(null, arrDouble);
	}

	public static String toString(String message, double[][] arrDouble) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrDouble != null) {
			sb.append(arrDouble.length);
			for (int i = 0; i < arrDouble.length; i++) {
				if (i == 0)
					sb.append("[" + toString(arrDouble[i]));
				else
					sb.append(", " + toString(arrDouble[i]));
			}
			sb.append("]");
		}

		return sb.toString();
	}

	public static String toString(double[] arrDouble) {
		return toString(null, arrDouble);
	}

	public static String toString(String message, double[] arrDouble) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrDouble != null) {
			sb.append(arrDouble.length);
//			for (int i = 0; i < arrDouble.length; i++) {
//				if (i == 0)
//					sb.append("[" + arrDouble[i]);
//				else
//					sb.append(", " + arrDouble[i]);
//			}
//			sb.append("]");
		}
		sb.append(Arrays.toString(arrDouble));

		return sb.toString();
	}

	public static String toString(byte[] arrbChar) {
		return toString(null, arrbChar);
	}

	public static String toString(String message, byte[] arrbChar) {
		StringBuilder sb = new StringBuilder();
		if (isValidate(message))
			sb.append(message + " ");

		if (arrbChar != null) {
			sb.append(arrbChar.length);
//			for (int i = 0; i < arrbChar.length; i++) {
//				if (i == 0)
//					sb.append("[" + (char) arrbChar[i]);
//				else
//					sb.append(", " + (char) arrbChar[i]);
//			}
//			sb.append("]");
		}
		sb.append(Arrays.toString(arrbChar));

		return sb.toString();
	}

	public static String toString(Object message, Throwable e, int stackTraceDepth) {
//		System.out.println("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
//		System.out.println(toString(Thread.currentThread().getStackTrace()));
		String log = String.format("%s %-12.12s %s %-60s", formatCurrentDate(SDF_MILLI_TIME1),
				"[" + Thread.currentThread().getName() + "]", (e == null) ? " INFO" : "ERROR",
				Thread.currentThread().getStackTrace()[stackTraceDepth]); // 1

		return toString(log, message, e);
	}

	public static String toString(String log, Object message, Throwable e) {
		StringBuilder sb = new StringBuilder();

		if (log != null) {
			sb.append(log);
		}

		if (message != null) {
			sb.append(((log == null) ? "" : " ") + toString(message));
		}

		if (e != null) {
			sb.append(LINE_SEPARATOR + toString(e));
		}

		return sb.toString();
	}

	protected static <T> String toString(Iterator<T> iter) {
		return toString(iter, -1);
	}

	protected static <T> String toString(Iterator<T> iter, int size) {
		StringBuilder sb = new StringBuilder();
		if (iter != null) {
			if (size > -1)
				sb.append(size);
			int i = 0;
			while (iter.hasNext()) {
				if (i++ == 0)
					sb.append("[" + toString(iter.next()));
				else
					sb.append(", " + toString(iter.next()));
			}
			sb.append("]");
		}

		return sb.toString();
	}

	protected static <T> String toString(Enumeration<T> enu) {
		StringBuilder sb = new StringBuilder();
		if (enu != null) {
			int i = 0;
			while (enu.hasMoreElements()) {
				if (i++ == 0)
					sb.append("[" + toString(enu.nextElement()));
				else
					sb.append(", " + toString(enu.nextElement()));
			}
			sb.append("]");
		}

		return sb.toString();
	}

	protected static <K, V> String toString(Map<K, V> map) {
		StringBuilder sb = new StringBuilder();
		if (map != null && map.size() > 0) {
			sb.append(map.size());
			int i = 0;
			for (Map.Entry<K, V> entry : map.entrySet()) {
				if (i++ == 0)
					sb.append("{" + toString(entry.getKey()) + "=" + toString(entry.getValue()));
				else
					sb.append(", " + toString(entry.getKey()) + "=" + toString(entry.getValue()));
			}
			sb.append("}");
		}

		return sb.toString();
	}

	// Image -------------------------------------------------------------------

	public static void createThumbPercent(String sFilePathIn, String sFilePathOut, int iScalePercent) {
		createThumbPercent(sFilePathIn, sFilePathOut, DEFAULT_THUMBNAIL_FILE_TYPE, iScalePercent);
	}

	public static void createThumbPercent(String sFilePathIn, String sFilePathOut, String sFileType,
			int iScalePercent) {
		BufferedImage biIn = getBufferedImage(sFilePathIn);
		if (biIn == null)
			return;

		ImageObserver io = new Container();
		createThumb(biIn, sFilePathOut, sFileType, (int) (biIn.getWidth(io) * ((float) iScalePercent / 100)));
	}

	public static void createThumbWidth(String sFilePathIn, String sFilePathOut, int iScaleWidth) {
		createThumbWidth(sFilePathIn, sFilePathOut, DEFAULT_THUMBNAIL_FILE_TYPE, iScaleWidth);
	}

	public static void createThumbWidth(String sFilePathIn, String sFilePathOut, String sFileType, int iScaleWidth) {
		BufferedImage biIn = getBufferedImage(sFilePathIn);
		if (biIn == null)
			return;

		createThumb(biIn, sFilePathOut, sFileType, iScaleWidth);
	}

	protected static BufferedImage getBufferedImage(String sFilePath) {
		try {
			return ImageIO.read(new File(sFilePath));
		} catch (Exception e) {
			e.printStackTrace();
//			LogUtil.writeLog(e, Utils.class);
		}

		return null;
	}

	protected static void createThumb(BufferedImage biIn, String sFilePathOut, String sFileType, int iWidth) {
		ImageObserver io = new Container();
		// SCALE_AREA_AVERAGING, SCALE_SMOOTH
		Image img = biIn.getScaledInstance(iWidth, -1, Image.SCALE_AREA_AVERAGING);
		BufferedImage biOut = new BufferedImage(img.getWidth(io), img.getHeight(io), BufferedImage.TYPE_INT_RGB);
		biOut.getGraphics().drawImage(img, 0, 0, io);

		try {
			// jpg, png
			ImageIO.write(biOut, sFileType, new File(sFilePathOut));
		} catch (Exception e) {
			e.printStackTrace();
//			LogUtil.writeLog(e, Utils.class);
		}
	}

	// 한글 ---------------------------------------------------------------------

	/**
	 * 초성이 포함되어 있는지 확인
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIncludeCho(String str) {
		for (char c : str.toCharArray()) {
			if (INITIAL_SOUND_BEGIN_UNICODE <= c && c <= INITIAL_SOUND_LAST_UNICODE) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 해당 문자열의 초성을 추출
	 * 
	 * @param str 문자열
	 * @return 초성 문자열
	 */
	public static String getChoSung(String str) {
		StringBuilder sb = new StringBuilder();
		for (char c : str.toCharArray()) {
			sb.append(getChoSung(c));
		}

		return sb.toString();
	}

	/**
	 * 해당 문자의 초성을 추출
	 * 
	 * @param c 문자
	 * @return 초성 문자
	 */
	public static char getChoSung(char c) {
		if (!isHangul(c))
			return c;

		int hanBegin = (c - HANGUL_BEGIN_UNICODE);
		int index = hanBegin / HANGUL_BASE_UNIT;

		return CHO_SUNG_CHAR[index];
	}

	/**
	 * 해당 문자가 한글인지 검사
	 * 
	 * @param c 문자
	 * @return 한글 : true, 아니면 : false
	 */
	public static boolean isHangul(char c) {
		return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
	}

	// Util --------------------------------------------------------------------

	/**
	 * Map<T1, T2>을 필터링한다.
	 */
	public static <T1, T2> Map<T1, T2> filter(Map<T1, T2> map, Map<T1, T2> includeMap, String... excludeKeys) {
		if (!isValidate(map))
			return map;

		if (isValidate(excludeKeys)) {
			for (String exclude : excludeKeys) {
				map.remove(exclude);
			}
		}

		if (isValidate(includeMap)) {
			map.putAll(includeMap);
		}

		return map;
	}

	public static <T> int getSize(Collection<T> c) {
		return (c == null) ? 0 : c.size();
	}

	public static <T1, T2> int getSize(Map<T1, T2> m) {
		return (m == null) ? 0 : m.size();
	}

	public static String[][] sumArray(String[][] arrObj1, String[][] arrObj2) {
		if (arrObj1 == null || arrObj2 == null)
			return new String[0][0];

		int iColumns = 0;
		if (arrObj1.length > 0)
			iColumns = arrObj1[0].length;
		else if (arrObj2.length > 0)
			iColumns = arrObj2[0].length;

		String[][] arrObj = new String[arrObj1.length + arrObj2.length][iColumns];
		System.arraycopy(arrObj1, 0, arrObj, 0, arrObj1.length);
		System.arraycopy(arrObj2, 0, arrObj, arrObj1.length, arrObj2.length);

		return arrObj;
	}

	public static boolean isExistClassName(Throwable trb, String sClsName) {
		if (trb == null)
			return false;

		StackTraceElement[] traces = trb.getStackTrace();
		for (StackTraceElement trace : traces) {
			if (trace.getClassName().indexOf(sClsName) > 0)
				return true;
		}

		return false;
	}

	public static <T> int indexOf(List<T> lObject, T object) {
		return indexOf((lObject == null) ? null : lObject.toArray(), object);
	}

	public static <T1, T2> int indexOf(Map<T1, T2> mObject, T2 object) {
		return indexOf((mObject == null) ? null : mObject.values().toArray(), object);
	}

	public static <T> int indexOf(T[] arrObject, T object) {
		if (arrObject != null && arrObject.length > 0) {
			if (object == null) {
				for (int i = 0; i < arrObject.length; i++) {
					if (arrObject[i] == null)
						return i;
				}
			} else {
				for (int i = 0; i < arrObject.length; i++) {
					if (arrObject[i].equals(object))
						return i;
				}
			}
		}

		return -1;
	}

	public static <K, V> K getKey(Map<K, V> map, Object V) {
		if (map != null) {
			for (Map.Entry<K, V> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					if (entry.getValue() == V) {
						return entry.getKey();
					}
				} else {
					if (entry.getValue().equals(V)) {
						return entry.getKey();
					}
				}
			}
		}

		return null;
	}

	/**
	 * 쿼리시 SUM()을 사용할 경우 적용
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<T> getSumList(List<T> list) {
		if (list.size() > 0 && list.get(0) == null)
			return new ArrayList<T>();

		return list;
	}

	public static void main(String[] args) {
//		System.out.println(new Timestamp(System.currentTimeMillis()));
//		System.out.println(isValid_juminChk(""));
//
//		List<Object> list = new ArrayList<Object>();
//		list.add("aaa");
//		System.out.println(toString(convertValue(list)));
//
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		map.put("a", "1");
//		map.put("b", "2");
//		map.put("b", null);
//		map.put(null, 3);
//		map.put(1, null);
//		System.out.println(map);
//		System.out.println(getKey(map, null));
//		Map<String, String> map2 = new HashMap<String, String>();
//		map.put("c", "3");
//		System.out.println(toString(convertKey(map)));
//		System.out.println(filter(map, map2, "a"));

//		Map<String, Object> map = new HashMap<String, Object>();
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("c", new String[] { "1", "2" });
//		map2.put("b", map3);
//		map.put("a", map2);
//		System.out.println(toString(map));
//		System.out.println(map);

//		String s = "http://127.0.0.1:8080/keb_mgw_client/contents/CM00/html/CM030101.html?message=테 스트";
//		System.out.println(s);
//		System.out.println(encodeURL(s));
//		System.out.println(decodeURL(s));

		// 0123
//		String s = "1a가";
//		System.out.println(s);
//		byte[] b = s.getBytes();
//		System.out.println(b.length + "");
//		System.out.println(isExist(b, 0, 5, "가", "EUC-KR"));
//		System.out.println(isExist(b, 0, 3, "가", "EUC-KR"));
//		System.out.println(isExist(b, 0, 4, "가", "EUC-KR"));
//		System.out.println(isExist(b, 2, 2, "가", "EUC-KR"));

//		List<String> usersNode = new ArrayList<String>();
//		usersNode.add("e");
//		Object[] arrs = usersNode.toArray();
//		String[] arrs = usersNode.toArray(new String[usersNode.size()]);
//		System.out.println(toString(arrs));

//		System.out.println(isValidate(new Integer(-1)));
//		System.out.println(isValidate(new Integer(0)));
//		System.out.println(isValidate(new Integer(1)));
//		System.out.println(isValidate(new Long(-1)));
//		System.out.println(isValidate(new Long(0)));
//		System.out.println(isValidate(new Long(1)));
//		System.out.println(isValidate(new Float(-0.1)));
//		System.out.println(isValidate(new Float(0)));
//		System.out.println(isValidate(new Float(0.1)));
//		System.out.println(isValidate(new Double(-0.1)));
//		System.out.println(isValidate(new Double(0)));
//		System.out.println(isValidate(new Double(0.1)));
//		System.out.println(isValidate(new String[][] { {} }));

//		System.out.println(convertList(new String[] { "1", "2" }));

//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("moduleName1", "1");
//		HTTPUtil.Test01 test = convertMapToObject(map, HTTPUtil.Test01.class);
//		System.out.println(test);

//		HTTPUtil.Test01 test = new HTTPUtil.Test01();
//		Map<String, Object> map = convertObjectToMap(test, "skoh.", false);
//		System.out.println(map);

//		System.out.println(toString("test", new byte[] {1, 2}));
//		System.out.println(Arrays.toString(new byte[] {1, 2}));

//		DECIMAL_FORMAT.applyPattern("00000000");
//		System.out.println(DECIMAL_FORMAT.format(2121003));

//		map.put(null, null);
//		System.out.println(toString(map.get("a")));
//		System.out.println(isValidate(map.keySet().toArray()[0]));

//		writeLog("skoh");

//		String input = "1 fish 2 fish red fish blue fish";
//		Scanner s = new Scanner(input).useDelimiter("\\s*fish\\s*");
//		System.out.println(toString(s));

//		String str = "네ㄱ무b스A텍";
//		System.out.println(getChoSung(str));
//		System.out.println(isIncludeCho(str));

//		System.out.println(replace("a\bc", null, ""));

//		int seconds = 60 * 60 * 2 + 60 * 3 + 13;
//		System.out.println(convertSecToString(seconds));

//		StringBuilder sb = new StringBuilder("1234567890");
//		System.out.println(sb.replace(4, 4, " ab "));
//		System.out.println(insertString("1234561234", "34", "ab"));
//		System.out.println(replaceString("1234561234", "34", "12", "ab"));
//		System.out.println(replaceLastString("1234561234", "12", "ab"));
//		System.out.println(replaceLastString("1234561234", "34", "12", "ab"));
	}
}