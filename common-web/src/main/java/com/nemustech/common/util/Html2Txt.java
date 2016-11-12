package com.nemustech.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

/**
 * HTML 유틸
 */
public abstract class Html2Txt {
	public static final String MAIL_HTML_HEADER = "<html><head><meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\" /><title>Mail</title></head><body>";
	public static final String MAIL_HTML_FOOTER = "</body></html>";

	public static final Pattern HTML_TITLE_PATTERN = Pattern.compile("(<title.*?>)([\\s\\S]*?)(</title>)", 2);
	public static final Pattern TAG_PATTERN = Pattern.compile("(<)([\\s\\S]*?)(>)", 2);

	public static final Pattern AMP_PATTERN = Pattern.compile("(&amp;|&#38;)"); // &
	public static final Pattern CDATA_TAG_PATTERN = Pattern.compile("(&nbsp;)");
	public static final Pattern QUOTE_PATTERN = Pattern.compile("(&quot;|&#34;|&ldquo;|&rdquo;)"); // "
	public static final Pattern LAQUO_PATTERN = Pattern.compile("(&laquo;|&#171;)"); // <<
	public static final Pattern RAQUO_PATTERN = Pattern.compile("(&raquo;|&#187;)"); // >>
	public static final Pattern APOSTROPHE_PATTERN = Pattern.compile("(&apos;|&#39;|&lsquo;)"); // '
	public static final Pattern LT_PATTERN = Pattern.compile("(&lt;|&#60;)"); // <
	public static final Pattern GT_PATTERN = Pattern.compile("(&gt;|&#62;)"); // >
	public static final Pattern MIDDOT_PATTERN = Pattern.compile("(&middot;|&#183;)"); // \267
	public static final Pattern ELLIPSIS_PATTERN = Pattern.compile("(&hellip;|&#133;)"); // ...

	public static final Pattern PRE_TAG_PATTERN = Pattern.compile("(<pre.*?>)([\\s\\S]*?)(</pre>)", 2);
	public static final Pattern PRE_TAG_PATTERN1 = Pattern.compile("(<pre.*?>)", 2);
	public static final Pattern PRE_TAG_PATTERN2 = Pattern.compile("(</pre>)", 2);

	public static String convertHtmltoTxt2(String htmlStr) throws IOException {
		if (htmlStr == null)
			return htmlStr;

		htmlStr = replaceHtmlSymbols(htmlStr);
		htmlStr = replacePRE(htmlStr);
		htmlStr = HTML_TITLE_PATTERN.matcher(htmlStr).replaceAll("");
		htmlStr = htmlStr.replace("<BR>", "#####");
		htmlStr = htmlStr.replace("<br>", "#####");
		htmlStr = htmlStr.replace("<P ", "#####<P ");
		htmlStr = htmlStr.replace("<p ", "#####<p ");
		htmlStr = htmlStr.replace("<P>", "#####<P>");
		htmlStr = htmlStr.replace("<p>", "#####<p>");
		htmlStr = htmlStr.replace("<div>", "#####<div>");
		htmlStr = htmlStr.replace("<DIV>", "#####<div>");
		htmlStr = htmlStr.replace("<DIV ", "#####<div ");

		StringReader reader = new StringReader(htmlStr);

		Source source = new Source(reader);
		Segment seg = new Segment(source, source.getBegin(), source.getEnd());

		String result = seg.getTextExtractor().toString();
		result = TAG_PATTERN.matcher(result).replaceAll("");
		result = result.replace("#####", "\r\n");
		result = replaceHtmlSymbols(result);

		return result;
	}

	public static String replaceHtmlSymbols(String htmlStr) {
		htmlStr = AMP_PATTERN.matcher(htmlStr).replaceAll("&");
//		htmlStr = CDATA_TAG_PATTERN.matcher(htmlStr).replaceAll(" ");
		htmlStr = QUOTE_PATTERN.matcher(htmlStr).replaceAll("\"");
		htmlStr = LAQUO_PATTERN.matcher(htmlStr).replaceAll("<<");
		htmlStr = RAQUO_PATTERN.matcher(htmlStr).replaceAll(">>");
		htmlStr = APOSTROPHE_PATTERN.matcher(htmlStr).replaceAll("'");
		htmlStr = LT_PATTERN.matcher(htmlStr).replaceAll("<");
		htmlStr = GT_PATTERN.matcher(htmlStr).replaceAll(">");
		htmlStr = MIDDOT_PATTERN.matcher(htmlStr).replaceAll("\267");
		htmlStr = ELLIPSIS_PATTERN.matcher(htmlStr).replaceAll("...");

		return htmlStr;
	}

	public static String replacePRE(String htmlStr) {
		StringBuilder sb = new StringBuilder();
		int start = -1;
		int end = -1;
		String strInTag;
		for (Matcher m = PRE_TAG_PATTERN.matcher(htmlStr); m.find(); sb.append(strInTag)) {
			start = m.start();
			if (end == -1 && start > 0)
				sb.append(htmlStr.substring(0, start));
			if (end > -1 && start > end)
				sb.append(htmlStr.substring(end, start));
			end = m.end();
			String preStr = htmlStr.substring(start, end);
			Matcher m1 = PRE_TAG_PATTERN1.matcher(preStr);
			String pre1 = m1.replaceAll("");
			Matcher m2 = PRE_TAG_PATTERN2.matcher(pre1);
			strInTag = m2.replaceAll("");
			strInTag = StringUtil.replace(strInTag, "\r", "");
			strInTag = StringUtil.replace(strInTag, "\n", "<BR>");
		}

		if (end < htmlStr.length() - 1)
			if (end > -1)
				sb.append(htmlStr.substring(end));
			else
				sb.append(htmlStr);
		return sb.toString();
	}

	public static String reverseMailBody(String content) {
		content = content.replace("\r\n", "<br>").replace("\r", "<br>").replace("\n", "<br>");
//		String[] str = content.split("\t");
//		String temp = "".trim();
//
//		for (int i = 1; i < str.length; i++) {
//			temp += str[i];
//		}
//		String[] str2 = temp.split("\r\n");
//		String cleanBody = "".trim();
//		for (int j = 0; j < str2.length; j++) {
//			if (!str2[j].equals("")) {
//				cleanBody += str2[j];
//
//			} else {
//				cleanBody += "\r\n\r\n";
//			}
//		}
//
//		cleanBody = str[0] + cleanBody;
//		LogUtil.writeLog(cleanBody, Html2Txt.class);
//		return cleanBody;
		return content;
	}

	/**
	 * 띄어쓰기 제거
	 */
	public static String removeWhitespace(String htmlStr) {
		htmlStr = htmlStr.replaceAll("\r\n|\n|\t", "");
		htmlStr = htmlStr.replaceAll("\\\"", "\'");

		return htmlStr;
	}

	public static String correctHtml(String contents) {
		if (Utils.isValidate(contents)) {
			contents = replaceHtmlSymbols(contents);
			contents = removeWhitespace(contents);
		}

		return contents;
	}

	public static void main(String[] args) {
//		String htmlStr = "&lt;HTML&gt;&lt;HEAD&gt;\r\n&lt;META http-equiv=Content-Type content=\"text/html; charset=utf-8\"&gt;\r\n&lt;META content=A4 name=PaperType&gt;\r\n&lt;STYLE type=text/css&gt;BODY {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nP {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nTD {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nUL {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nOL {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nLI {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nP {\r\n\tMARGIN-TOP: 1px; MARGIN-BOTTOM: 1px\r\n}\r\nBLOCKQUOTE {\r\n\tMARGIN-TOP: 1px; MARGIN-BOTTOM: 1px\r\n}\r\n&lt;/STYLE&gt;\r\n\r\n&lt;META content=\"MSHTML 6.00.6000.21366\" name=GENERATOR&gt;\r\n&lt;STYLE&gt;v\\:* {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\no\\:* {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\nx\\:* {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\n.shape {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\n&lt;/STYLE&gt;\r\n&lt;/HEAD&gt;\r\n&lt;BODY&gt;\r\n&lt;P&gt;게시물 테스트&lt;/P&gt;\r\n&lt;P&gt;&amp;nbsp;&lt;/P&gt;\r\n&lt;P&gt;첨부파일 포함~&lt;/P&gt;\r\n&lt;P&gt;&amp;nbsp;&lt;/P&gt;\r\n&lt;P&gt;&lt;FONT style=\"FONT-SIZE: 11pt\" face=\"맑은 고딕\"&gt;&lt;FONT style=\"FONT-SIZE: 11pt\"&gt;&lt;STRONG&gt;&lt;FONT style=\"FONT-SIZE: 10pt\"&gt;&lt;IMG height=271 alt=\"\" hspace=0 src=\"http://gw.dongwha-mh.com/Storage/GW/FileStorage/Inline/15/3915_현충일 추가.JPG\" width=893 border=0&gt;&lt;/FONT&gt;&lt;/STRONG&gt;&lt;/FONT&gt;&lt;/FONT&gt;&lt;/P&gt;&lt;/BODY&gt;&lt;/HTML&gt;";
//		htmlStr = replaceHtmlSymbols(htmlStr);
//		System.out.println(htmlStr);

		String htmlStr = "<HTML><HEAD>\r\n<META http-equiv=Content-Type content=\"text/html; charset=utf-8\">\r\n<META content=A4 name=PaperType>\r\n<STYLE type=text/css>BODY {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nP {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nTD {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nUL {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nOL {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nLI {\r\n\tFONT-SIZE: 10pt; FONT-FAMILY: 굴림,Arial\r\n}\r\nP {\r\n\tMARGIN-TOP: 1px; MARGIN-BOTTOM: 1px\r\n}\r\nBLOCKQUOTE {\r\n\tMARGIN-TOP: 1px; MARGIN-BOTTOM: 1px\r\n}\r\n</STYLE>\r\n\r\n<META content=\"MSHTML 6.00.6000.21366\" name=GENERATOR>\r\n<STYLE>v\\:* {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\no\\:* {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\nx\\:* {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\n.shape {\r\n\tBEHAVIOR: url(#default#VML)\r\n}\r\n</STYLE>\r\n</HEAD>\r\n<BODY>\r\n<P>게시물 테스트</P>\r\n<P>&nbsp;</P>\r\n<P>첨부파일 포함~</P>\r\n<P>&nbsp;</P>\r\n<P><FONT style=\"FONT-SIZE: 11pt\" face=\"맑은 고딕\"><FONT style=\"FONT-SIZE: 11pt\"><STRONG><FONT style=\"FONT-SIZE: 10pt\"><IMG height=271 alt=\"\" hspace=0 src=\"http://gw.dongwha-mh.com/Storage/GW/FileStorage/Inline/15/3915_현충일 추가.JPG\" width=893 border=0></FONT></STRONG></FONT></FONT></P></BODY></HTML>";
		htmlStr = removeWhitespace(htmlStr);
		System.out.println(htmlStr);
	}
}