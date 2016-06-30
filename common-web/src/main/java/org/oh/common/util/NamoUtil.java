package org.oh.common.util;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;

/**
 * 나모에디터 유틸리티 클래스.
 */
public abstract class NamoUtil {
	/**
	 * ASCII(8859_1)로 되어있는 KSC5601 문자열을 로 인코딩한다.
	 *
	 * @param str asc Ascii로 인코딩 되어있는 문자열
	 * @return KSC5601로 변환된 문자열
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static String ascToKsc(String str) throws UnsupportedEncodingException {
		return StringUtil.isEmpty(str) ? "" : new String(str.getBytes("ISO8859_1"), "KSC5601");
	}

	/**
	 * 웹페이지를 복사해 붙여넣기 하는 경우 웹페이지 내 이미지 링크가 아래와 같이 alt 부분에 Content-ID를 포함하여<br/>
	 * (예 : <IMG border=0 alt=cid:image003.jpg@01CB281B.135D22D0 src="http://bbs.kbsec.co.kr/clip.jpg"/> )<br/>
	 * Util.mapping(strBodyText, mappingMap, "cid:", "@namo.co.kr", false); 코드 호출시 alt=이후 부분의 데이타가<br/>
	 * 모두 날라가는 문제를 해결하기 위해 alt내 cid 코드를 모두 제거하는 함수
	 *
	 * @param bodyText 본문
	 * @return 이미지 태그의 alt 부분에 Content-ID를 제거한 본문
	 */
	public static String removeAltCid(String bodyText) {
		StringBuffer result = new StringBuffer();
		int beginDelimLength = " alt=cid:".length();

		for (int beginIndex = 0, endIndex = 0;;) {
			beginIndex = bodyText.indexOf(" alt=cid:", endIndex);
			if (beginIndex == -1) {
				result.append(bodyText.substring(endIndex, bodyText.length()));
				break;
			}

			result.append(bodyText.substring(endIndex, beginIndex));
			endIndex = bodyText.indexOf(" ", beginIndex + beginDelimLength);
			if (endIndex == -1) {
				break;
			}
		}

		return result.toString();
	}

	/**
	 * 나모웹에디터 연동 모듈의 하나.<br/>
	 * 주어진 (Content-ID 이미지를 포함한) 마임타입의 본문을 파싱한 HTML 텍스트를 반환한다.<br/>
	 * - 본문에 Content-ID 이미지가 포함되어 있으면 지정된 디렉토리에 이미지 파일을 저장하고 Content-ID를 이미지 URL 경로로 변환한다.
	 *
	 * @param mimeBody (Content-ID 이미지를 포함한) 마임타입의 본문
	 * @param fileSaveDir 본문에 포함된 Content-ID 이미지 저장 경로
	 * @param fileBaseUrl Content-ID 이미지를 이미지 URL로 변환시 사용할 이미지 홈 URL
	 * @param bmpToPng BMP 이미지 파일을 PNG 이미지 파일로 변환 여부
	 * @return 파싱한 HTML 텍스트
	 */
	final public static String parseNamoMime(String mimeBody, String fileSaveDir, String fileBaseUrl,
			boolean bmpToPng) {
		try {
			MimeMessage mimeMessage = new MimeMessage(null,
					new ByteArrayInputStream(mimeBody.replaceAll(" \r\n", " ").getBytes()));
			StringBuffer bodyTextBuf = new StringBuffer();
			HashMap<String, String> cidAttachMap = new HashMap<String, String>(); // <Content-ID, 이미지 파일명> 맵

			parseNamoMime(mimeMessage, fileSaveDir, bodyTextBuf, cidAttachMap, bmpToPng);
			if (fileBaseUrl.endsWith("/") == false) {
				fileBaseUrl += "/";
			}

			HashMap<String, String> mappingMap = new HashMap<String, String>();
			String bodyText = removeAltCid(ascToKsc(bodyTextBuf.toString()));

			for (Iterator<String> it = cidAttachMap.keySet().iterator(); it.hasNext();) {
				String contentId = it.next();
				mappingMap.put(contentId.substring(0, contentId.indexOf("@")),
						fileBaseUrl + cidAttachMap.get(contentId));
			}

			bodyText = StringUtil.mapping(bodyText, mappingMap, "cid:", "@namo.co.kr");

			// <BODY></BODY> 태그내 컨텐츠만 추출
			int bodyTagBeginIndex = bodyText.indexOf("<BODY");
			if (bodyTagBeginIndex < 0) {
				return bodyText;
			} else {
				int bodyBeginIndex = bodyText.indexOf(">", bodyTagBeginIndex) + 1;
				int bodyEndIndex = bodyText.indexOf("</BODY>");

				return bodyText.substring(bodyBeginIndex, bodyEndIndex);
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, NamoUtil.class);
		}

		return "";
	}

	/**
	 * 나모웹에디터 연동 모듈의 하나.<br/>
	 * 주어진 마임 파트를 재귀적으로 호출하여 마임타입의 본문을 파싱한다.
	 *
	 * @param part 마임 파트
	 * @param fileSaveDir 본문에 포함된 Content-ID 이미지 저장 경로
	 * @param bodyTextBuf 파싱한 HTML 텍스트
	 * @param cidAttachMap <Content-ID, 이미지 파일명> 맵
	 * @param bmpToPng BMP 이미지 파일을 PNG 이미지 파일로 변환 여부
	 * @throws Exception 파일 저장오류시 발생
	 */
	final private static void parseNamoMime(Part part, String fileSaveDir, StringBuffer bodyTextBuf,
			HashMap<String, String> cidAttachMap, boolean bmpToPng) throws Exception {
		if (part.isMimeType("text/*")) {
			InputStream in = part.getInputStream();
			int ch = -1;
			while ((ch = in.read()) != -1) {
				bodyTextBuf.append((char) ch);
			}
		} else if (part.isMimeType("multipart/*")) {
			Multipart multiPart = (Multipart) part.getContent();
			int numOfPart = multiPart.getCount();
			for (int i = 0; i < numOfPart; i++) {
				parseNamoMime(multiPart.getBodyPart(i), fileSaveDir, bodyTextBuf, cidAttachMap, bmpToPng);
			}
		} else if (part.isMimeType("message/rfc822")) {
			parseNamoMime((Part) part.getContent(), fileSaveDir, bodyTextBuf, cidAttachMap, bmpToPng);
		} else if (part.isMimeType("image/*") || part.isMimeType("application/*")) {
			String contentId = null;

			try {
				String[] contentIdArray = (part.getHeader("Content-ID"));
				contentId = contentIdArray[0].trim().substring(1, contentIdArray[0].length() - 1);
				String attachName = part.getFileName();
				if (isEncoded(attachName)) {
					attachName = MimeUtility.decodeText(attachName);
				}

				String saveFilePrefix = DateUtil.getCurrentDateMillisTime().substring(0, 15);
				if (attachName.startsWith(saveFilePrefix.substring(0, 8)) == false) {
					String saveFileSuffix = contentId.substring(0, 8);
					String saveFileExt = FileUtil.getExtension(attachName);
					attachName = saveFilePrefix + "_" + saveFileSuffix + "." + saveFileExt;
				}

				if (attachName.endsWith("ukn")) {
					attachName = attachName.substring(0, attachName.length() - 3) + "gif";
				}

				String attachPath = attachName.substring(0, 8) + "/" + attachName;

				if (bmpToPng == true && attachPath.toLowerCase().endsWith(".bmp")) {
					cidAttachMap.put(contentId, attachPath.substring(0, attachPath.length() - 3) + "png");
				} else {
					cidAttachMap.put(contentId, attachPath);
				}
				saveMimeFile(part, fileSaveDir, attachName, bmpToPng);
			} catch (Exception e) {
				try {
					if (contentId != null) {
						cidAttachMap.remove(contentId);
					}
				} catch (Exception ex) {
				}
			}
		}
		// else if(part.getDisposition().equalsIgnoreCase("attachment")) // 첨부파일 처리
		// {
		// String strAttachName = part.getFileName();
		// }
	}

	/**
	 * 주어진 마임 파트에서 파일내용을 읽어와 지정된 파일경로에 지정된 파일명으로 저장한다.
	 *
	 * @param part 마임 파트
	 * @param fileSaveDir 저장할 파일 경로
	 * @param filename 저장할 파일명
	 * @param bmpToPng BMP 이미지 파일을 PNG 이미지 파일로 변환 여부
	 * @throws Exception 파일 저장오류시 발생
	 */
	final private static void saveMimeFile(Part part, String fileSaveDir, String filename, boolean bmpToPng)
			throws Exception {
		InputStream in = null;
		FileOutputStream out = null;
		String filePath = null;

		try {
			filePath = (fileSaveDir.endsWith("/")) ? fileSaveDir + filename.substring(0, 8)
					: fileSaveDir + "/" + filename.substring(0, 8);
			FileUtil.forceMkdir(filePath);

			filePath += "/" + filename;

			in = part.getInputStream();
			out = new FileOutputStream(filePath);
			HTTPUtil.copyLarge(in, out);
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}

		if (bmpToPng == true && filename.toLowerCase().endsWith(".bmp")) {
			String pngFilePath = filePath.substring(0, filePath.length() - 3) + "png";
			FileUtil.bmpToPng(filePath, pngFilePath);
		}
	}

	/**
	 * 소스라인이 인코딩되어있는 문자열인지 체크한다.<br/>
	 * Case 1> '=?' 이 꼭 맨 처음에 오지 않는 경우도 있다.<br/>
	 * 예를들면 헤더에 Subject: TEST =?EUC-KR?B?xde9usau?=
	 *
	 * @param src 입력 문자열.
	 * @return 결과 문자열.
	 */
	final private static boolean isEncoded(String src) {
		try {
			String temp = StringUtil.defaultString(src);

			if (temp.length() > 6) {
				int x1 = temp.indexOf("=?");
				int x2 = temp.indexOf("?=");
				if (x1 > -1 && x2 > -1 && x2 > x1) {
					return true;
				}
			}
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * 주어진 HTML 문자열내 이미지 태그(<img .../>)내 src 값이 http://나 https://로 시작하지 않는 것에 대해<br/>
	 * 지정된 이미지 Base URL을 추가해준다.<br/>
	 * 이미지 src에 한글이 포함되고 WAS가 Tomcat인 경우 server.xml 에서 Connector 부분에 URIEncoding="euc-kr" 을 추가한다.
	 *
	 * @param html HTML 문자열
	 * @param imageBaseUrl 이미지 Base URL
	 * @param imageSrcEnc 이미지 src 인코딩타입 (예: EUC-KR) - 이미지 src가 한글일때 문제를 해결
	 * @return 이미지 태그(<img .../>)내 src 값에 이미지 Base URL이 추가된 문자열
	 */
	public static String replaceImgByBaseUrl(String html, String imageBaseUrl, String imageSrcEnc) {
		int htmlLength = (html == null) ? 0 : html.length();
		if (htmlLength < 10 || StringUtil.isBlank(imageBaseUrl)) {
			return html;
		}

		boolean endsWithSlash = imageBaseUrl.trim().endsWith("/");

		Pattern imgPattern = Pattern.compile("(<img\\s*[^>]*?\\s+src\\s*=\\s*[\"'])([^\"']+?)([\"'])",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		Matcher imgMatcher = imgPattern.matcher(html);

		StringBuffer toHtmlBuf = new StringBuffer();
		for (int beginIndex = 0, endIndex = 0; beginIndex < htmlLength; beginIndex = endIndex) {
			if (endIndex != -1 && endIndex <= beginIndex) {
				endIndex = imgMatcher.find(beginIndex) ? imgMatcher.start() : -1;
			}

			if (endIndex == -1) {
				toHtmlBuf.append(html.substring(beginIndex));
				break;
			} else {
				toHtmlBuf.append(html.substring(beginIndex, endIndex));
				toHtmlBuf.append(imgMatcher.group(1));
				String src = imgMatcher.group(2);

				if (src != null && (src.startsWith("http://") == false && src.startsWith("https://") == false)) {
					if (imageSrcEnc != null) {
						try {
							src = URLEncoder.encode(src, imageSrcEnc);
						} catch (Exception e) {
						}
					}

					if (endsWithSlash && src.startsWith("/")) {
						toHtmlBuf.append(imageBaseUrl).append(src.substring(1));
					} else if (endsWithSlash == false && src.startsWith("/") == false) {
						toHtmlBuf.append(imageBaseUrl).append('/').append(src);
					} else {
						toHtmlBuf.append(imageBaseUrl).append(src);
					}
				} else {
					toHtmlBuf.append(src);
				}
				toHtmlBuf.append(imgMatcher.group(3));
				endIndex = imgMatcher.end();
			}
		}

		return toHtmlBuf.toString();
	}

	/**
	 * 주어진 HTML 문자열에서 지정된 HTML 태그에 감싸인 문자열(태그 포함)을 변환할 문자열로 변환하여 리턴.
	 *
	 * @param fromHtml HTML 문자열
	 * @param tag HTML 태그 (대소문자 구별안함) - 예) img, iframe
	 * @param replacement 변환할 문자열
	 * @return 주어진 태그에 포함된 문자열이 지정된 변환 문자열로 변환된 HTML 문자열
	 */
	public static String replaceHtmlTag(String fromHtml, String tag, String replacement) {
		int htmlLength = (fromHtml == null) ? 0 : fromHtml.length();
		if (htmlLength < tag.length() + 3) {
			return fromHtml;
		}

		Pattern tagPattern = Pattern.compile("<" + tag + "[^>]*?>(.*?)</" + tag + ">",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		Matcher tagMatcher = tagPattern.matcher(fromHtml);

		return tagMatcher.replaceAll(replacement);
	}

	/**
	 * 주어진 HTML 문자열에서 HTML 태그에 감싸인 컨텐츠 문자열(태그 비포함)을 문자열 변환하는 메소드.
	 *
	 * @param fromHtml HTML 문자열
	 * @param regex 변환대상 문자열
	 * @param replacement 변환할 문자열
	 * @return HTML 태그에 포함된 컨텐츠 문자열이 지정된 변환 문자열로 변환된 HTML 문자열
	 */
	public static String replaceHtmlContent(String fromHtml, String regex, String replacement) {
		int htmlLength = (fromHtml == null) ? 0 : fromHtml.length();
		if (htmlLength < 3) {
			return fromHtml;
		}

		Pattern tagPattern = Pattern.compile("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>");
		Matcher tagMatcher = tagPattern.matcher(fromHtml);
		StringBuffer toHtmlBuf = new StringBuffer();
		int tagIndex = 0;
		for (int startIndex = 0, endIndex = 0; startIndex < htmlLength; startIndex = endIndex) {
			tagIndex = (tagMatcher.find(startIndex)) ? tagMatcher.start() : -1;

			if (tagIndex == -1) {
				toHtmlBuf.append(fromHtml.substring(startIndex));
				break;
			} else {
				toHtmlBuf.append(fromHtml.substring(startIndex, tagIndex).replaceAll(regex, replacement));
				String tag = tagMatcher.group();
				toHtmlBuf.append(tag);
				endIndex = tagMatcher.end();
			}
		}

		return toHtmlBuf.toString();
	}

	/*
	 * public String getTagContent(String html, String tag) {
	 * String upperHtml = html.toUpperCase();
	 * String upperTag = tag.toUpperCase();
	 * 
	 * int tagBeginIndex = upperHtml.indexOf("<" + upperTag);
	 * if (tagBeginIndex < 0) {
	 * return html;
	 * } else {
	 * int beginIndex = upperHtml.indexOf(">", tagBeginIndex) + 1;
	 * int endIndex = upperHtml.indexOf("</" + upperTag + ">");
	 * 
	 * return html.substring(beginIndex, endIndex);
	 * }
	 * }
	 */
}
