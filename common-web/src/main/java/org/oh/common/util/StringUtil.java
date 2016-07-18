package org.oh.common.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.oh.common.util.Tokenizer.Token;

/**
 * 문자열 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.lang3.StringUtils 클래스를 상속받음.
 *
 * @see <a
 *      href=http://commons.apache.org/lang/api-2.4/org/apache/commons/lang/StringUtils.html>org.apache.commons.lang3.StringUtils
 *      </a>
 */
public abstract class StringUtil extends StringUtils {
	public static final String DEFAULT_ELLIPSIS = "...";
	public static final String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
	public static final String FORMAT_PATTERN = "###,###,###,###,##0.###";

	protected static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat.getInstance();
	protected static final GregorianCalendar CALENDAR = new GregorianCalendar();

	private static final Pattern PATTERN_HTML_TAG = Pattern.compile("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>");
	private static final Pattern PATTERN_HTML_CHAR = Pattern.compile("&[^;]+;");

	/**
	 * 주어진 UTF-8 문자열을 지정된 byte 길이 만큼 잘라서 List로 반환한다.
	 *
	 * <pre>
	 * 예)
	 * String str = &quot;ABCDEFGHIJKLMN&quot;;
	 * String korStr = &quot;가나다라마바사아자&quot; &lt; br &gt;
	 * 
	 * StringUtil.splitUtf8(new String(str.getBytes(&quot;utf-8&quot;)), 3); // &quot;[ABC, DEF, GHI, JKL, MN]&quot;
	 * StringUtil.splitUtf8(new String(korStr.getBytes(&quot;utf-8&quot;)), 3); // &quot;[가, 나, 다, 라, 마, 바, 사, 아, 자]&quot;
	 * </pre>
	 * 
	 * 
	 * @param utf8String UTF-8 문자열
	 * @param cutByteSize 문자열을 자를 byte수
	 * @return 지정된 길이 만큼 잘라진 문자열의 List
	 */
	public static List<String> splitUtf8(String utf8String, int cutByteSize) {
		List<String> splitList = new LinkedList<String>();

		if (utf8String == null || cutByteSize < 1) {
			return splitList;
		}

		int sourceLength = utf8String.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, tempByteSize = 0; i < sourceLength; i++) {
			char ch = utf8String.charAt(i);
			int charByte = ((int) ch < 128) ? 1 : 3; // UTF-8 에서 한글은 3byte,
														// ASCII 문자는 1byte
			if (tempByteSize + charByte <= cutByteSize) {
				buffer.append(ch);
				tempByteSize += charByte;
			} else {
				splitList.add(buffer.toString());
				buffer.setLength(0);
				buffer.append(ch);
				tempByteSize = charByte;
			}
		}

		if (buffer.length() > 0) {
			splitList.add(buffer.toString());
		}

		return splitList;
	}

	/**
	 * 주어진 문자열을 지정된 길이 만큼 잘라서 List로 반환한다.
	 *
	 * <pre>
	 * 예)
	 *    String  str         = "ABCDEFGHIJKLMN";
	 *    <br>
	 *    
	 *    StringUtil.splitByLength(str, 2); // "[AB, CD, EF, GH, IJ, KL, MN]"
	 *    StringUtil.splitByLength(str, 3); // "[ABC, DEF, GHI, JKL, MN]"
	 * </pre>
	 *
	 * @param str 문자열
	 * @param cutLength 문자열을 자를 길이
	 * @return 지정된 길이 만큼 잘라진 문자열의 List
	 */
	public static List<String> splitByLength(String str, int cutLength) {
		List<String> splitList = new LinkedList<String>();

		if (str == null || cutLength < 1) {
			return splitList;
		}

		int sourceLength = str.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, tempLength = 0; i < sourceLength; i++) {
			char ch = str.charAt(i);

			if (tempLength + 1 <= cutLength) {
				buffer.append(ch);
				tempLength++;
			} else {
				splitList.add(buffer.toString());
				buffer.setLength(0);
				buffer.append(ch);
				tempLength = 1;
			}
		}

		if (buffer.length() > 0) {
			splitList.add(buffer.toString());
		}

		return splitList;
	}

	/**
	 * 첫째 문자열이 둘째 문자열보다 작은지 여부를 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String str1 = "ABCDEF";
	 *    String str2 = "ABCDEF";
	 *    String str3 = "ABCDEFGHI";
	 *    String str4 = "ABC";
	 *    <br>
	 *    
	 *    StringUtil.isLessThan(str1, str2); // 같음 "false"
	 *    StringUtil.isLessThan(str1, str3); // 작음 "true";
	 *    StringUtil.isLessThan(str1, str4); // 큼 "false"
	 * </pre>
	 *
	 * @param str1 첫째 문자열
	 * @param str2 둘째 문자열
	 * @return 작으면 true, 아니면 false
	 */
	public static boolean isLessThan(String str1, String str2) {
		if (str1 == null) {
			return str2 != null;
		} else if (str2 == null) {
			return false;
		}
		return str1.compareTo(str2) < 0;
	}

	/**
	 * 첫째 문자열이 둘째 문자열보다 작거나 같은지 여부를 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String str1 = "ABCDEF";
	 *    String str2 = "ABCDEF";
	 *    String str3 = "ABCDEFGHI";
	 *    String str4 = "ABC";
	 *    <br>
	 *    
	 *    StringUtil.isLessEqual(str1, str2); // 같음 "true"
	 *    StringUtil.isLessEqual(str1, str3); // 작음 "true";
	 *    StringUtil.isLessEqual(str1, str4); // 큼 "false"
	 * </pre>
	 *
	 * @param str1 첫째 문자열
	 * @param str2 둘째 문자열
	 * @return 작거나 같으면 true, 아니면 false
	 */
	public static boolean isLessEqual(String str1, String str2) {
		if (str1 == null) {
			return true;
		} else if (str2 == null) {
			return false;
		}
		return str1.compareTo(str2) <= 0;
	}

	/**
	 * 첫째 문자열이 둘째 문자열보다 큰지 여부를 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String str1 = "ABCDEF";
	 *    String str2 = "ABCDEF";
	 *    String str3 = "ABCDEFGHI";
	 *    String str4 = "ABC";
	 *    <br>
	 *    
	 *    StringUtil.isGreaterThan(str1, str2); // 같음 "false"
	 *    StringUtil.isGreaterThan(str1, str3); // 작음 "false";
	 *    StringUtil.isGreaterThan(str1, str4); // 큼 "true"
	 * </pre>
	 * 
	 * @param str1 첫째 문자열
	 * @param str2 둘째 문자열
	 * @return 크면 true, 아니면 false
	 */
	public static boolean isGreaterThan(String str1, String str2) {
		if (str1 == null) {
			return false;
		} else if (str2 == null) {
			return true;
		}
		return str1.compareTo(str2) > 0;
	}

	/**
	 * 첫째 문자열이 둘째 문자열보다 크거나 같은지 여부를 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String str1 = "ABCDEF";
	 *    String str2 = "ABCDEF";
	 *    String str3 = "ABCDEFGHI";
	 *    String str4 = "ABC";
	 *    <br>
	 *    
	 *    StringUtil.isGreaterEqual(str1, str2); // 같음 "true"
	 *    StringUtil.isGreaterEqual(str1, str3); // 작음 "false";
	 *    StringUtil.isGreaterEqual(str1, str4); // 큼 "true"
	 * </pre>
	 * 
	 * @param str1 첫째 문자열
	 * @param str2 둘째 문자열
	 * @return 크거나 같으면 true, 아니면 false
	 */
	public static boolean isGreaterEqual(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		} else if (str2 == null) {
			return true;
		}
		return str1.compareTo(str2) >= 0;
	}

	/**
	 * 주어진 두 문자열값이 다른지 여부를 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String str1 = "ABCDEF";
	 *    String str2 = "ABCDEF";
	 *    String str3 = "ABCDEFEFG";
	 *    <br>
	 *    
	 *    StringUtil.notEquals(str1, str2); // 같음 "false"
	 *    StringUtil.notEquals(str1, str3); // 다름 "true";
	 * </pre>
	 * 
	 * @param str1 첫째 문자열
	 * @param str2 둘째 문자열
	 * @return 다르면 true, 같으면 false
	 */
	public static boolean notEquals(String str1, String str2) {
		return equals(str1, str2) ? false : true;
	}

	/**
	 * 주어진 문자열이 비교 대상문자열 배열에서 같은 문자열이 없는지 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String      str         = "ABC";
	 *    String[]    targetArray1 = {"ABC", "ACD", "ABN", "ABB"};
	 *    String[]    targetArray2 = {"ACD", "ABN", "ABB", "DBF"};
	 *    <br>
	 *    
	 *    StringUtil.notEquals(str, targetArray1); // 같은 문자열 존재 "false"
	 *    StringUtil.notEquals(str, targetArray2); // 같은 문자열 미존재 "true";
	 * </pre>
	 * 
	 * @param str 문자열
	 * @param targetArray 비교 대상문자열 배열
	 * @return 같은 문자열이 없으면 true, 아니면 false
	 */
	public static boolean notEquals(String str, String[] targetArray) {
		int targetSize = (targetArray == null) ? 0 : targetArray.length;

		for (int i = 0; i < targetSize; i++) {
			if (equals(str, targetArray[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 주어진 스트링 배열에 지정된 검색 문자열이 존재하는지 여부를 알아낸다.
	 *
	 * <pre>
	 * 예)
	 *    String[] str = {"abc","abb","acc"};
	 *    <br>
	 *    
	 *    StringUtil.contains(str, "abc"); // 문자열 포함 "true"
	 *    StringUtil.contains(str, "add"); // 문자열 미포함 "false";
	 * </pre>
	 * 
	 * @param sourceArray 스트링 배열
	 * @param searchString 검색 문자열
	 * @return 존재시 true, 존재하지 않으면 false
	 */
	public static boolean contains(String[] sourceArray, String searchString) {
		if (searchString != null) {
			int arrayLength = sourceArray.length;
			for (int i = 0; i < arrayLength; i++) {
				if (searchString.equals(sourceArray[i])) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 주어진 문자열이 지정된 최대길이를 초과할 경우 말줄임 처리를 한다.
	 *
	 * <pre>
	 * 예)
	 *    String str = "ABCDEFGHIJKLMN";
	 *    <br>
	 *    
	 *    StringUtil.ellipsis(str, 5, "..."); // "AB..." 
	 *    StringUtil.ellipsis(str, 5, "***"); // "AB***"
	 * </pre>
	 *
	 * @param str 문자렬
	 * @param maxLength 최대길이
	 * @param ellipsis 말줄임
	 * @return 말줄임 처리된 문자열
	 */
	public static String ellipsis(String str, int maxLength, String ellipsis) {
		int length = (str == null) ? 0 : str.length();

		if (length <= maxLength) {
			return str;
		}

		int ellipsisLength = (ellipsis == null) ? 0 : ellipsis.length();
		return str.substring(0, maxLength - ellipsisLength) + ellipsis;
	}

	/**
	 * 주어진 문자열이 지정된 최대길이를 초과할 경우 말줄임(...) 처리를 한다.
	 *
	 * <pre>
	 * 예)
	 *    String str = "ABCDEFGHIJKLMN";
	 *    <br>
	 *    
	 *    StringUtil.ellipsis(str, 5); // "AB..."
	 *    StringUtil.ellipsis(str, 14); // "ABCDEFGHIJKLMN"
	 * </pre>
	 * 
	 * @param str 문자렬
	 * @param maxLength 최대길이
	 * @return 말줄임 처리된 문자열
	 */
	public static String ellipsis(String str, int maxLength) {
		return ellipsis(str, maxLength, DEFAULT_ELLIPSIS);
	}

	/**
	 * 주어진 문자가 null이면 defaultValue를 리턴함.
	 *
	 * <pre>
	 * 예)
	 * StringUtil.defaultIfBlank(&quot; &quot;, &quot;abc&quot;); // &quot;abc&quot; 출력 (디폴트값)
	 * StringUtil.defaultIfBlank(&quot;123&quot;, &quot;abc&quot;); // &quot;123&quot; 출력
	 * </pre>
	 *
	 * @param str 문자열
	 * @param defaultStr str이 null일때 리턴할 문자.
	 *
	 * @return 주어진 문자가 null이면 defaultValue를 리턴. 아니면 주어진 문자열을 리턴.
	 */
	public static String defaultIfBlank(String str, String defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	/**
	 * 주어진 객체가 null이면 defaultValue를 리턴함.
	 *
	 * @param obj 객체
	 * @param defaultStr str이 null일때 리턴할 문자.
	 *
	 * @return 주어진 문자가 null이면 defaultValue를 리턴. 아니면 defaultIfBlank(obj.toString(), defaultStr)을 리턴.
	 */
	public static String defaultIfBlank(Object obj, String defaultStr) {
		return (obj == null) ? defaultStr : defaultIfBlank(obj.toString(), defaultStr);
	}

	/**
	 * 주어진 객체가 null이면 defaultStr을 리턴하고, 그렇지 않으면 toString() 메소드로 리턴함.
	 * 
	 * @param str 문자
	 * @param defaultStr 객체가 null일 때 리턴할 문자.
	 * @return 주어진 문자가 null이면 defaultValue를 리턴. 아니면 defaultIfEmpty(obj.toString(), defaultStr)을 리턴.
	 */
	public static String defaultIfEmpty(String str, String defaultStr) {
		return (str == null) ? defaultStr : defaultIfEmpty(str, defaultStr);
	}

	/**
	 * 주어진 객체가 null이면 defaultStr을 리턴하고, 그렇지 않으면 toString() 메소드로 리턴함.
	 * 
	 * @param obj 객체
	 * @param defaultStr 객체가 null일 때 리턴할 문자.
	 * @return 주어진 객체가 null이면 defaultStr, 아니면 defaultString(obj.toString(), defaultStr)을 리턴.
	 */
	public static String defaultString(Object obj, String defaultStr) {
		return (obj == null) ? defaultStr : defaultString(obj.toString(), defaultStr);
	}

	/**
	 * underscore(_) 양식의 문자열을 camel 양식으로 변환
	 * 
	 * underscore(_) 양식의 문자열을 camel 양식으로 변환.
	 *
	 * <pre>
	 * 예)
	 *    String str1 = "_abc_defg_hijk";
	 *    String str2 = "abc_defg_hijk";
	 *    <br>
	 *    
	 *    StringUtil.underscoreToCamel(str1); // "AbcDefgHijk"
	 *    StringUtil.underscoreToCamel(str2); // "abcDefgHijk"
	 * </pre>
	 *
	 * @param str underscore(_) 양식의 문자열
	 * @return camel 양식의 문자열
	 */
	public static String underscoreToCamel(String str) {
		if (isBlank(str)) {
			return str;
		}

		StringBuffer buffer = new StringBuffer();
		boolean upperNextChar = false;

		for (char ch : str.toCharArray()) {
			if (ch == '_') {
				upperNextChar = true;
				continue;
			}
			if (upperNextChar) {
				buffer.append(Character.toUpperCase(ch));
			} else {
				buffer.append(ch);
			}
			upperNextChar = false;
		}

		return buffer.toString();
	}

	/**
	 * camel 양식의 문자열을 underscore(_) 양식으로 변환.
	 *
	 * <pre>
	 * 예)
	 *    String str = "camelToUnderscore_test";
	 *    <br>
	 *    
	 *    StringUtil.camelToUnderscore(str); // "camel_to_underscore_test"
	 * </pre>
	 *
	 * @param str camel 양식의 문자열
	 * @return underscore(_) 양식의 문자열
	 */
	public static String camelToUnderscore(String str) {
		if (isBlank(str)) {
			return str;
		}

		StringBuffer buffer = new StringBuffer();

		for (char ch : str.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				buffer.append('_');
				ch = Character.toLowerCase(ch);
			}
			buffer.append(ch);
		}

		return buffer.toString();
	}

	/**
	 * 주어진 문자열에 대해 XSS 인코딩 처리를 하는 메소드.<br/>
	 * - < 문자는 &lt;로 변환, > 문자는 &gt;로 변환<br/>
	 * - ",“, ', ‘ 문자는 제거
	 *
	 * @param str 문자열
	 * @return XSS 인코딩 처리된 문자열.
	 */
	public static String encodeXss(String str) {

		int length = (str == null) ? 0 : str.length();
		if (length == 0) {
			return str;
		}

		StringBuffer encodedValueBuffer = new StringBuffer();

		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);

			switch (ch) {
				case '<':
					encodedValueBuffer.append("&lt;");
					break;
				case '>':
					encodedValueBuffer.append("&gt;");
					break;
				case '"':
				case '“':
				case '\'':
				case '‘':
					continue;
				default:
					encodedValueBuffer.append(ch);
					break;
			}
		}

		return encodedValueBuffer.toString();
	}

	/**
	 * 주어진 문자열에 대해 XSS 디코딩 처리를 하는 메소드.<br/>
	 * - &gt; 문자열을 > 문자로 변환<br/>
	 * - (주의) &lt; 문자열을 < 문자로 변환 안함
	 *
	 * @param str 문자열
	 * @return XSS 디코딩 처리된 문자열.
	 */
	public static String decodeXss(String str) {
		try {
			if (isNotBlank(str)) {
				return str.replaceAll("&gt;", ">");
			}
		} catch (Exception e) {
		}

		return str;
	}

	/**
	 * 주어진 문자열에 대해 XSS 디코딩 처리를 하는 메소드.<br/>
	 * - &lt; 문자열을 < 문자로 변환, &gt; 문자열을 > 문자로 변환
	 *
	 * @param str 문자열
	 * @return XSS 디코딩 처리된 문자열.
	 */
	public static String decodeXssWithLt(String str) {
		try {
			if (isNotBlank(str)) {
				return str.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
			}
		} catch (Exception e) {
		}

		return str;
	}

	/**
	 * 주어진 객체의 지정된 빈프로퍼티에 대해 XSS 디코딩 처리를 하는 메소드.<br/>
	 * - &gt; 문자열을 > 문자로 변환<br/>
	 * - (주의) &lt; 문자열을 < 문자로 변환 안함
	 *
	 * @param obj 객체
	 * @param beanPropertyNames 빈프로터티명
	 */
	public static void decodeXss(Object obj, String beanPropertyNames) {
		try {
			if (obj != null && beanPropertyNames != null) {
				String[] beanPropertyNameArray = beanPropertyNames.split(",");
				int arraySize = (beanPropertyNameArray == null) ? 0 : beanPropertyNameArray.length;
				for (int i = 0; i < arraySize; i++) {
					String str = BeanUtils.getSimpleProperty(obj, beanPropertyNameArray[i]);
					if (isNotBlank(str)) {
						BeanUtils.setProperty(obj, beanPropertyNameArray[i], decodeXss(str));
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 주어진 문자열에서 토큰변환맵<토큰, 변환값>에 저장된 토큰에 해당하는 문자열을 변환값으로 맵핑시키는 메소드.
	 *
	 * @param str 문자열
	 * @param map 토큰변환맵<토큰, 변환값>
	 * @param beginDelim 토큰분리 시작구분자
	 * @param endDelim 토큰분리 종료구분자
	 * @return 토큰이 변환값으로 맵핑된 문자열.
	 */
	public static String mapping(String str, Map<String, String> map, String beginDelim, String endDelim) {
		Tokenizer tokenizer = new Tokenizer(str, beginDelim, endDelim);

		StringWriter out = new StringWriter();

		String lineBuf = null;
		Token token = null;

		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			lineBuf = token.isToken() ? defaultString(map.get(token.getValue().trim())) : token.getValue();
			out.write(lineBuf);
		}

		return out.toString();
	}

	/**
	 * 주어진 HTML 문자열에서 HTML 태그를 제거하는 메소드.
	 *
	 * @param html HTML 문자열
	 * @return HTML 태그가 제거된 문자열
	 */
	public static String removeHtmlTag(String html) {
		try {
			Matcher htmlMatcher = PATTERN_HTML_TAG.matcher(html);
			Matcher charMatcher = PATTERN_HTML_CHAR.matcher(htmlMatcher.replaceAll("").trim());
			return charMatcher.replaceAll("").trim();
		} catch (Exception e) {
			return html;
		}
	}

	/**
	 * 주어진 문자열 배열에서 엘리먼트 값이 blank(null, whitespace)인 엘리먼트를 제거하는 메소드.
	 *
	 * @param strArray 문자열 배열
	 * @return blank 엘리먼트가 제거된 문자열 배열
	 */
	public static String[] makeNoBlankArray(String[] strArray) {
		if (strArray == null || strArray.length == 0) {
			return null;
		}

		LinkedList<String> strList = new LinkedList<String>();
		for (String str : strArray) {
			if (isBlank(str) == false) {
				strList.add(str);
			}
		}

		int listSize = strList.size();

		if (listSize == 0) {
			return null;
		}

		String[] noBlankArray = new String[listSize];
		strList.toArray(noBlankArray);
		return noBlankArray;
	}

	// 확장 ///

	public static String subString(String str, int offset, int leng) {
		return new String(str.getBytes(), offset - 1, leng);
	}

	public static String subString(String str, int offset) {
		byte bytes[] = str.getBytes();
		int size = bytes.length - (offset - 1);
		return new String(bytes, offset - 1, size);
	}

	public static String fitString(String str, int size) {
		return fitString(str, size, 2);
	}

	public static String fitString(String str, int size, int align) {
		byte bts[] = str.getBytes();
		int len = bts.length;
		if (len == size)
			return str;
		if (len > size) {
			String s = new String(bts, 0, size);
			if (s.length() == 0) {
				StringBuilder sb = new StringBuilder(size);
				for (int idx = 0; idx < size; idx++)
					sb.append("?");

				s = sb.toString();
			}
			return s;
		}
		if (len < size) {
			int cnt = size - len;
			char values[] = new char[cnt];
			for (int idx = 0; idx < cnt; idx++)
				values[idx] = ' ';

			if (align == 1)
				return String.copyValueOf(values) + str;
			else
				return str + String.copyValueOf(values);
		} else {
			return str;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] toStringArray(String str) {
		List list = new ArrayList();
		for (StringTokenizer st = new StringTokenizer(str); st.hasMoreTokens(); list.add(st.nextToken()))
			;
		return toStringArray(list);
	}

	@SuppressWarnings("rawtypes")
	public static String[] toStringArray(List list) {
		String strings[] = new String[list.size()];
		for (int idx = 0; idx < list.size(); idx++)
			strings[idx] = list.get(idx).toString();

		return strings;
	}

	public static String replace2(String src, String from, String to) {
		if (src == null)
			return null;
		if (from == null)
			return src;
		if (to == null)
			to = "";
		StringBuilder buf = new StringBuilder();
		int pos;
		while ((pos = src.indexOf(from)) >= 0) {
			buf.append(src.substring(0, pos));
			buf.append(to);
			src = src.substring(pos + from.length());
		}
		buf.append(src);
		return buf.toString();
	}

	public static String cutString(String str, int limit) {
		if (str == null || limit < 4)
			return str;
		int len = str.length();
		int cnt = 0;
		int index;
		for (index = 0; index < len && cnt < limit;)
			if (str.charAt(index++) < '\u0100')
				cnt++;
			else
				cnt += 2;

		if (index < len)
			str = str.substring(0, index - 1) + "...";
		return str;
	}

	public static String cutEndString(String src, String end) {
		if (src == null)
			return null;
		if (end == null)
			return src;
		int pos = src.indexOf(end);
		if (pos >= 0)
			src = src.substring(0, pos);
		return src;
	}

	public static char[] makeCharArray(char c, int cnt) {
		char a[] = new char[cnt];
		Arrays.fill(a, c);
		return a;
	}

	public static String getString(char c, int cnt) {
		return new String(makeCharArray(c, cnt));
	}

	public static String getLeftTrim(String lstr) {
		if (!lstr.equals("")) {
			int strlen = 0;
			int cptr = 0;
			boolean lpflag = true;
			strlen = lstr.length();
			cptr = 0;
			lpflag = true;
			do {
				char chk = lstr.charAt(cptr);
				if (chk != ' ')
					lpflag = false;
				else if (cptr == strlen)
					lpflag = false;
				else
					cptr++;
			} while (lpflag);
			if (cptr > 0)
				lstr = lstr.substring(cptr, strlen);
		}
		return lstr;
	}

	public static String getRightTrim(String lstr) {
		if (!lstr.equals("")) {
			int strlen = 0;
			int cptr = 0;
			boolean lpflag = true;
			strlen = lstr.length();
			cptr = strlen;
			lpflag = true;
			do {
				char chk = lstr.charAt(cptr - 1);
				if (chk != ' ')
					lpflag = false;
				else if (cptr == 0)
					lpflag = false;
				else
					cptr--;
			} while (lpflag);
			if (cptr < strlen)
				lstr = lstr.substring(0, cptr);
		}
		return lstr;
	}

	public static String getLeft(String str, int len) {
		if (str == null)
			return "";
		else
			return str.substring(0, len);
	}

	public static String getRight(String str, int len) {
		if (str == null)
			return "";
		String dest = "";
		for (int i = str.length() - 1; i >= 0; i--)
			dest = dest + str.charAt(i);

		str = dest;
		str = str.substring(0, len);
		dest = "";
		for (int i = str.length() - 1; i >= 0; i--)
			dest = dest + str.charAt(i);

		return dest;
	}

	public static String nvl(String str, String replace) {
		if (str == null)
			return replace;
		else
			return str;
	}

	public static String checkEmpty(String str, String replace) {
		if (isEmpty(str))
			return replace;
		else
			return str;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.equals(""))
			return true;
		else
			return false;
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return str;
		else
			return (new StringBuilder(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
					.toString();
	}

	public static String escapeXml(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '&')
				sb.append("&amp;");
			else
				sb.append(c);
		}

		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getTokenList(String s, String token) {
		if (isEmpty(s))
			return null;
		List tokenList = new ArrayList();
		for (StringTokenizer st = new StringTokenizer(s, token); st.hasMoreTokens(); tokenList
				.add(st.nextToken().trim()))
			;
		return tokenList;
	}

	public static int getTokenLength(String s, String token) {
		if (s == null)
			return 0;
		int len = 0;
		for (StringTokenizer st = new StringTokenizer(s, token); st.hasMoreTokens();)
			len++;

		return len;
	}

	public static String getToken(int index, String s, String token) {
		if (s == null)
			return "";
		StringTokenizer st = new StringTokenizer(s, token);
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		do {
			if (!st.hasMoreTokens())
				break;
			if (index == i) {
				sb.append(st.nextToken());
				break;
			}
			st.nextToken();
			i++;
		} while (true);
		if (sb.toString().length() > 0)
			return sb.toString().trim();
		else
			return "";
	}

	public static String getToken(int index, String s, String token, String nvl) {
		if (s == null)
			return nvl;
		StringTokenizer st = new StringTokenizer(s, token);
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		do {
			if (!st.hasMoreTokens())
				break;
			if (index == i) {
				sb.append(st.nextToken());
				break;
			}
			st.nextToken();
			i++;
		} while (true);
		if (sb.toString().length() > 0)
			return sb.toString().trim();
		else
			return nvl;
	}

	public static void deleteStringBuilder(StringBuilder strBuf) {
		strBuf.delete(0, strBuf.length());
	}

	public static boolean isset(String str) {
		return str != null && str.length() > 0;
	}

	public static String collapse(String str, String characters, String replacement) {
		if (str == null)
			return null;
		StringBuilder newStr = new StringBuilder();
		boolean prevCharMatched = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (characters.indexOf(c) != -1) {
				if (!prevCharMatched) {
					prevCharMatched = true;
					newStr.append(replacement);
				}
			} else {
				prevCharMatched = false;
				newStr.append(c);
			}
		}

		return newStr.toString();
	}

	public static String getString(String str, int max) {
		byte temp[] = str.getBytes();
		int count = 0;
		int str_count;
		for (str_count = 0; max > str_count && str_count != temp.length; str_count++)
			if (temp[str_count] < 0)
				count++;

		if (count % 2 != 0)
			str = new String(temp, 0, str_count - 1);
		else
			str = new String(temp, 0, str_count);
		return str;
	}

	public static boolean checkUndefined(Object obj) {
		boolean result = false;
		if (obj.toString().equals("Undefined"))
			result = true;
		else
			result = false;
		return result;
	}

	public static String dashedPdaNo(String pdaNo) {
		String firstPdaNo = "";
		String secondPdaNo = "";
		String thirdPdaNo = "";
		if (null == pdaNo || 0 == pdaNo.trim().length())
			return "";
		if (pdaNo.trim().length() < 10 || 11 < pdaNo.trim().length()) {
			return pdaNo;
		} else {
			firstPdaNo = pdaNo.substring(0, 3) + "-";
			secondPdaNo = pdaNo.substring(3, pdaNo.length() - 4) + "-";
			thirdPdaNo = pdaNo.substring(pdaNo.length() - 4);
			return firstPdaNo + secondPdaNo + thirdPdaNo;
		}
	}

	public static String makeLikeValue(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append('%');
		if (value != null)
			sb.append(value);
		sb.append('%');
		return sb.toString();
	}

	public static boolean existsNonAscii(String src) {
		byte b[] = src.getBytes();
		for (int i = 0; i < b.length; i++)
			if (b[i] < 0)
				return true;

		return false;
	}

	public static String[] parseGuid(String input, String separator, int count) {
		StringTokenizer token = new StringTokenizer(input, separator);
		if (token.countTokens() != count)
			return null;
		String outputs[] = new String[token.countTokens()];
		int i = 0;
		while (token.hasMoreElements())
			outputs[i++] = token.nextToken();
		return outputs;
	}

	public static String encodeURL(String s, String enc) throws UnsupportedEncodingException {
		return URLEncoder.encode(s, enc).replaceAll("\\+", "%20");
	}

	public static String getEncode(String boxName) {
		try {
			return encodeURL(boxName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeLog(e, StringUtil.class);
		}
		return null;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public static Boolean getTestDecode(String password, byte[] p1) {
		p1 = "kisatest".getBytes();

		return true;
	}

	public static String convertFormat(long value) {
		return convertFormat((double) value);
	}

	public static String convertFormat(double value) {
		return convertFormat(value, FORMAT_PATTERN);
	}

	public static String convertFormat(long value, String pattern) {
		return convertFormat((double) value, pattern);
	}

	public static String convertFormat(double value, String pattern) {
		DECIMAL_FORMAT.applyPattern(pattern);
		return DECIMAL_FORMAT.format(value);
	}

	/**
	 * ID를 생성한다.(20자리)
	 *
	 * @return 생성한 Unique ID
	 */
	public static String generateUID() {
		long c = System.nanoTime();
		Random random = new Random();
		int len = chars.length;

		// 603032925024778IXNTD
		return String.format("%015d%s%s%s%s%s", c, chars[random.nextInt(len)], chars[random.nextInt(len)],
				chars[random.nextInt(len)], chars[random.nextInt(len)], chars[random.nextInt(len)]);
	}

	public static String getDateTime() {
		return getDateTime(System.currentTimeMillis());
	}

	public static String getDateTime(long timeMillis) {
		return getDate(timeMillis) + getTime(timeMillis);
	}

	public static String getDateTimeHypen() {
		return getDateTimeHypen(System.currentTimeMillis());
	}

	public static String getDateTimeHypen(long timeMillis) {
		return getDate(timeMillis) + "_" + getTime(timeMillis);
	}

	public static String getDate() {
		return getDate(System.currentTimeMillis());
	}

	/**
	 * 일자를 구한다.
	 */
	public static String getDate(long timeMillis) {
		CALENDAR.setTimeInMillis(timeMillis);
		return "" + CALENDAR.get(Calendar.YEAR) + convertFormat(CALENDAR.get(Calendar.MONTH) + 1, "00")
				+ convertFormat(CALENDAR.get(Calendar.DAY_OF_MONTH), "00");
	}

	public static String getTime() {
		return getTime(System.currentTimeMillis());
	}

	/**
	 * 시간을 구한다.
	 */
	public static String getTime(long timeMillis) {
		CALENDAR.setTimeInMillis(timeMillis);
		return "" + convertFormat(CALENDAR.get(Calendar.HOUR_OF_DAY), "00")
				+ convertFormat(CALENDAR.get(Calendar.MINUTE), "00")
				+ convertFormat(CALENDAR.get(Calendar.SECOND), "00");
	}

	public static String getProperty(String key, String addValue) {
		return System.getProperty(key, "") + addValue;
	}

	public static String getMessage(String pattern, String... arguments) {
		return new MessageFormat(pattern).format(arguments).toString();
	}

	/**
	 * 주민등록번호 유효성 체크
	 */
	public static boolean isValid_juminChk(String jumin) {
		if (!Utils.isValidate(jumin) || jumin.length() != 13) {
			return false;
		}

		int a = Integer.parseInt(jumin.substring(0, 1));
		int b = Integer.parseInt(jumin.substring(1, 2));
		int c = Integer.parseInt(jumin.substring(2, 3));
		int d = Integer.parseInt(jumin.substring(3, 4));
		int e = Integer.parseInt(jumin.substring(4, 5));
		int f = Integer.parseInt(jumin.substring(5, 6));
		int g = Integer.parseInt(jumin.substring(6, 7));
		int h = Integer.parseInt(jumin.substring(7, 8));
		int i = Integer.parseInt(jumin.substring(8, 9));
		int j = Integer.parseInt(jumin.substring(9, 10));
		int k = Integer.parseInt(jumin.substring(10, 11));
		int l = Integer.parseInt(jumin.substring(11, 12));
		int m = Integer.parseInt(jumin.substring(12, 13));
		int month = Integer.parseInt(jumin.substring(2, 4));
		int day = Integer.parseInt(jumin.substring(4, 6));

		// valid check
		if (month <= 0 || month > 12)
			return false;
		if (day <= 0 || day > 31)
			return false;

		if (g > 4 || g == 0)
			return false;

		int temp = a * 2 + b * 3 + c * 4 + d * 5 + e * 6 + f * 7 + g * 8 + h * 9 + i * 2 + j * 3 + k * 4 + l * 5;
		temp = temp % 11;
		temp = 11 - temp;
		temp = temp % 10;

		if (temp == m)
			return true;
		else
			return false;
	}

	/**
	 * 주민등록번호 만 나이 리턴
	 */
	public static int getAge(String juminNo) {
		String juminBirth = juminNo.substring(0, 6);
		String juminSex = juminNo.substring(6, 7);

		String toDate = Utils.formatCurrentDate();
		String toYear = toDate.substring(0, 4);
		String toMonth = toDate.substring(4, 6);
		String toDay = toDate.substring(6, 8);

		String juminYear = (("1".equals(juminSex) || "2".equals(juminSex) || "5".equals(juminSex)
				|| "6".equals(juminSex)) ? "19" : "20") + juminBirth.substring(0, 2);
		String juminMonth = juminBirth.substring(2, 4);
		String juminDay = juminBirth.substring(4, 6);

		int ageYear = Integer.parseInt(toYear) - Integer.parseInt(juminYear);
		if (Integer.parseInt(toMonth) < Integer.parseInt(juminMonth))
			ageYear -= 1;
		else if (Integer.parseInt(toMonth) == Integer.parseInt(juminMonth)) {
			if (Integer.parseInt(toDay) < Integer.parseInt(juminDay))
				ageYear -= 1;
		}

		return ageYear;
	}

	public static String convertDisplay(String juminNo) {
		if (Utils.isValidate(juminNo) && juminNo.length() != 13) {
			return juminNo;
		}

		return juminNo.substring(0, 7) + "*****" + juminNo.substring(12, 13);
	}

	/**
	 * 객체배열을 문자열로 변환한다.
	 * 
	 * @param objs
	 * 
	 * @return
	 */
	public static String toString(Object... objs) {
		if (objs == null || objs.length == 0)
			return "[]";

		StandardToStringStyle style = new StandardToStringStyle();
		style.setUseIdentityHashCode(false);

		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < objs.length; i++) {
			sb.append(ReflectionToStringBuilder.toString(objs[i], style));
			if (i == objs.length - 1) {
				sb.append("]");
			} else {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	/**
	 * 시작하는 문자열에 대한 정규표현식을 만든다. 예) [ "11", "12" ] -> "^11|^12"
	 * 
	 * @param list
	 * 
	 * @return
	 */
	public static String getStartWithRegExp(List<String> list) {
		if (list == null)
			return "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append("^" + list.get(i));
			if (i < list.size() - 1)
				sb.append("|");
		}

		return sb.toString();
	}

	public static void main(String[] args) {
//		System.out.println(convertFormat(1L, "00"));
//		System.out.println(encodeMD5("1234")); // 05841730f0329c9ad0c80ff268da9dfe
//		System.out.println(generateUID());
//		String uid = LocalFileStorageAccessor.generateUID().substring(6);
//		String uid = "00000001371200140695";
//		System.out.println(getDateTime(Long.parseLong(uid)));

//		System.out.println(convertDisplay("7208031234567").substring(6, 7));

//		System.out.println(String.format("%1$-8s", "2121003"));
//		System.out.println(leftPad("2121003", 8, "0"));

//		System.out.println(defaultIfBlank("1", "abc"));
	}
}
