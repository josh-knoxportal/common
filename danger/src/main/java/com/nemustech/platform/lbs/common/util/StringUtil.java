/*
 * StringUtil.java
 * 
 * Copyright (c) 2012, FIT S&C. All rights reserved.
 */
package com.nemustech.platform.lbs.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * StringUtil
 * 
 * @author Renman
 * @version 1.0
 *
 */
public class StringUtil {

	private static final int HANGUL_BYTE = 3;

	public static boolean isEmpty(String str) {
		//System.out.println("========"+str);
		return (str == null || "".equals(str));
		//return (str == null || str.trim().length() == 0);
	}

	public static boolean isNumber(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch(Exception e) {}
		return false;
	}

	public static String toMobileFotmat(String str) {
		try {
			str = str.replaceAll("-", "");
			StringBuilder sb = new StringBuilder();
			if(str.length() == 11) {
				sb.append(str.substring(0, 3)).append("-");
				sb.append(str.substring(3, 7)).append("-");
				sb.append(str.substring(7));
			} else if(str.length() == 10) {
				sb.append(str.substring(0, 3)).append("-");
				sb.append(str.substring(3, 6)).append("-");
				sb.append(str.substring(6));
			}
			return sb.toString();
		} catch(Exception e) {}
		return str;
	}

	public static int byteCheck( String data ){
		int len = 0;

		if( data != null ){
			String str = data.substring(0);
			for(int i=0; i<str.length(); i++){
				String ch = escape(str.substring(i, i+1));
				if(ch.length() == 1) len++;
				else if(ch.indexOf("%u") != -1) len += HANGUL_BYTE;
				else if(ch.indexOf("%") != -1) len += ch.length()/3;
			}
		}

		return len;
	}

	public static boolean isHangul(String str) {
		if(str == null || str.length() == 0) return false;
		char ch;
		for(int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if(ch < '가' || ch > '힣') return false;
		}
		return true;
	}

	public static boolean validEmailFormat(String str) {
		if(str == null || str.length() == 0) return false;
		try {
			int off = str.indexOf("@");
			if(off == -1) return false;
			String dom = str.substring(off+1);
			String[] arr = dom.split("\\.");
			return (arr.length > 1);
		} catch(Exception e) {
			return false;
		}
	}

	public static String fillRight(int no, char c, int len) {
		return fillRight(String.valueOf(no), c, len);
	}
	public static String fillRight(String str, char c, int len) {
		if(str == null) return null;

		if(str.length() >= len) return str;

		int cnt = len - str.length();
		String add = "";
		for(int i = 0; i < cnt; i++) {
			add += c;
		}

		return str+add;
	}

	public static String fillLeft(int no, char c, int len) {
		return fillLeft(String.valueOf(no), c, len);
	}
	public static String fillLeft(String str, char c, int len) {
		if(str == null) return null;

		if(str.length() >= len) return str;

		int cnt = len - str.length();
		String add = "";
		for(int i = 0; i < cnt; i++) {
			add += c;
		}

		return add+str;
	}

	public static String escape(String str)
	{
		StringBuffer sb = new StringBuffer();
		String ncStr = "*+_./0123456789@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		char c;

		for(int i=0; i<str.length(); i++)
		{
			c = str.charAt(i);
			if(c > 0x7f)
				sb.append("%u" + Integer.toHexString((int)c).toUpperCase());
			else if(ncStr.indexOf((int)c) == -1){
				sb.append('%');
				if(c <= 0xf)
					sb.append('0');
				sb.append(Integer.toHexString((int)c).toUpperCase());
			}
			else
				sb.append(c);
		}

		return sb.toString();
	}

	public static String replaceContent(String content) {
		if(content == null || content.length() == 0) return content;
		final String regEx = "(https?|ftp)://[a-zA-Z0-9\\./\\-_\\?=&;#]+";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regEx,
				java.util.regex.Pattern.CASE_INSENSITIVE | 
				java.util.regex.Pattern.UNICODE_CASE |
				java.util.regex.Pattern.DOTALL |
				java.util.regex.Pattern.MULTILINE);
		java.util.regex.Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			String s = matcher.group();
			matcher.appendReplacement(sb, "<a href=\""+s+"\" target==\"_blank\">"+s+"</a>");
		}
		matcher.appendTail(sb);
		//		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * @param str
	 * @param cls
	 * @return
	 */
	public static String toParagraphTagStr(String str, String cls) {
		if(str == null) return str;
		if(str.toLowerCase().indexOf("<p>") != -1) {
			if(cls != null) { 
				String regEx = "<(p)>";
				java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regEx,
						java.util.regex.Pattern.CASE_INSENSITIVE | 
						java.util.regex.Pattern.DOTALL |
						java.util.regex.Pattern.MULTILINE);
				java.util.regex.Matcher matcher = pattern.matcher(str);
				str = matcher.replaceAll("<p class=\""+cls+"\">");
			}
			return str;
		}

		String p = "<p>";
		if(cls != null) { 
			p = "<p class=\""+cls+"\">";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(p);	
		int st = 0, ed = 0;
		String s;
		while(ed != -1) {
			ed = str.indexOf("\n", st);
			if(ed != -1) {
				s = str.substring(st, ed).trim();
				if(s.length() == 0) s = "&nbsp;";
				sb.append(s);
				sb.append("</p>").append(p);
			} else {
				s = str.substring(st).trim();
				if(s.length() == 0) s = "&nbsp;";
				sb.append(s);
				sb.append("</p>").append(p);
				break;
			}
			st = ed+1;
		}

		String retStr = sb.toString().trim();
		if(retStr.endsWith(p)) {
			retStr = retStr.substring(0, retStr.length()-p.length());
		}
		return retStr;
	}

	public static boolean isClearValue(String value) {
		return !(value.indexOf("\r") != -1 || value.indexOf("\n") != -1 
				|| value.indexOf("%0a") != -1 || value.indexOf("%0d") != -1
				|| value.indexOf(":") != -1 || value.indexOf(";") != -1);
	}

	/**
	 * get file extension.
	 */
	static public String getFileExtension(String filename) {
		if (filename==null || filename.length()==0) {
			return "";
		}

		int idx = filename.lastIndexOf('.');
		if (idx != -1) {
			return filename.substring(idx+1);
		}
		return "";
	}

	public static String htmlToXml(String str) {
		if(str == null)
			return str;

		String xml_str = str;
		//		xml_str = xml_str.replaceAll("&", "&amp;");
		xml_str = xml_str.replaceAll("<", "&lt;");
		xml_str = xml_str.replaceAll(">", "&gt;");
		xml_str = xml_str.replaceAll("\"", "&#034;");
		xml_str = xml_str.replaceAll("'", "&#039;");

		return xml_str;
	}

	public static String addStyle2Tag(String content, String tag, String css) {
		//		String str = "<p style=\"text-align: center;\">aaaaa</p><p>adsfdsf</p>";
		final String regEx = "<"+tag+"(\\s+style=\"([^\"]*)\")?([^>]*)>";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regEx,
				java.util.regex.Pattern.CASE_INSENSITIVE | 
				java.util.regex.Pattern.UNICODE_CASE |
				java.util.regex.Pattern.DOTALL |
				java.util.regex.Pattern.MULTILINE);
		java.util.regex.Matcher matcher = pattern.matcher(content);

		if(css == null) css = "";

		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			String style=" style=\"";
			String s = matcher.group(2);
			//			System.out.println("s="+s);
			if(s != null) {
				style += s;
				if(!s.endsWith(";")) style += ";";
			}
			style += css;
			matcher.appendReplacement(sb, "<"+tag+style+"\">");
		}
		matcher.appendTail(sb);
		//		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static String html2Text(String str) {
		if(str == null) return null;
		return str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}

	public static String toJsonText(String str) {
		if(str == null) return null;
		String txt = html2Text(str);
		return txt.replaceAll("\"", "\\\\\"").replaceAll("\r", "").replaceAll("\n", "\\\\n");
	}

	public static int byte2Int(String s) {
		try {
			String t = s.toLowerCase();
			if(t.endsWith("mb")) {
				String s1 = t.substring(0, t.length()-2);
				return Integer.parseInt(s1) * 1024 * 1024;
			} else if(t.endsWith("m")) {
				String s1 = t.substring(0, t.length()-1);
				return Integer.parseInt(s1) * 1024 * 1024;
			} else if(t.endsWith("kb")) {
				String s1 = t.substring(0, t.length()-2);
				return Integer.parseInt(s1) * 1024;
			} else if(t.endsWith("k")) {
				String s1 = t.substring(0, t.length()-1);
				return Integer.parseInt(s1) * 1024;
			} else if(t.endsWith("b")) {
				String s1 = t.substring(0, t.length()-1);
				return Integer.parseInt(s1);
			} else if(t.endsWith("byte")) {
				String s1 = t.substring(0, t.length()-4);
				return Integer.parseInt(s1);
			} else {
				return Integer.parseInt(s);
			}
		} catch(Exception e) {
			return 0;
		}
	}

	public static int time2Mills(String s) {
		if(StringUtil.isEmpty(s)) return 0;

		String t = s.toLowerCase();
		if(t.endsWith("ms")) {
			String s1 = t.substring(0, t.length()-2);
			return Integer.parseInt(s1);
		} else if(t.endsWith("sec")) {
			String s1 = t.substring(0, t.length()-3);
			return Integer.parseInt(s1) * 1000;
		} else if(t.endsWith("min")) {
			String s1 = t.substring(0, t.length()-3);
			return Integer.parseInt(s1) * 1000 * 60;
		} else if(t.endsWith("m")) {
			String s1 = t.substring(0, t.length()-1);
			return Integer.parseInt(s1) * 1000 * 60;
		} else if(t.endsWith("hour")) {
			String s1 = t.substring(0, t.length()-4);
			return Integer.parseInt(s1) * 1000 * 60 * 24;
		} else if(t.endsWith("h")) {
			String s1 = t.substring(0, t.length()-1);
			return Integer.parseInt(s1) * 1000 * 60 * 24;
		} else {
			return Integer.parseInt(s);
		}
	}

	public static String toMD5(String str) {
		String MD5 = null; 
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();

		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace(); 
			MD5 = null; 
		}
		return MD5;
	}

	public static String toSHA256(String str) {
		String SHA = null; 
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();

		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace(); 
			SHA = null; 
		}
		return SHA;
	}
	
	public static String [] split_nemus(String origin, String delimiter) {
		return origin.split(delimiter);
	}
	
	
	public static String decodeHTML(String s) {
		return ReplaceTag(s, "decode");
	}
	
	public static String encodeHTML(String s) {
		return ReplaceTag(s, "encode");
	}
	
	private static String ReplaceTag(String Expression, String type){
        String result = "";
        if (Expression==null || Expression.equals("")) return "";

        if (type == "encode") {
            result = ReplaceString(Expression, "&", "&amp;");
            result = ReplaceString(result, "#", "&#35");
            result = ReplaceString(result, "<", "&lt;");
            result = ReplaceString(result, ">", "&gt;");
            result = ReplaceString(result, "(", "&#40;");
            result = ReplaceString(result, ")", "&#41;");
        }
        else if (type == "decode") {
            result = ReplaceString(Expression, "&amp;", "&");
            result = ReplaceString(result, "&#35", "#");
            result = ReplaceString(result, "&lt;", "<");
            result = ReplaceString(result, "&gt;", ">");
            result = ReplaceString(result, "&#40;", "(");
            result = ReplaceString(result, "&#41;", ")");
        }
        
        return result;  
    }

    private static String ReplaceString(String Expression, String Pattern, String Rep)
    {
        if (Expression==null || Expression.equals("")) return "";

        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = Expression.indexOf(Pattern, s)) >= 0) {
            result.append(Expression.substring(s, e));
            result.append(Rep);
            s = e + Pattern.length();
        }
        result.append(Expression.substring(s));
        return result.toString();
    }
}
