package org.oh.common.json;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/**
 * 간단한 Json 전문 처리 결과를 저장하는 객체 클래스
 * 
 * 
 * @version 1.0.0
 * 
 */
public class SimpleJsonResponse {
	protected static Log log = LogFactory.getLog(SimpleJsonResponse.class);

	/**
	 * {@value com.mcnc.smart.hybrid.common.code.Codes#_JSON_MESSAGE_RESULT}
	 */
	boolean result;
	/**
	 * {@value com.mcnc.smart.hybrid.common.code.Codes#_JSON_MESSAGE_ERRORCODE}
	 */
	String errorCode;
	/**
	 * {@value com.mcnc.smart.hybrid.common.code.Codes#_JSON_MESSAGE_ERRORTEXT}
	 */
	String errorText;
	/**
	 * {@value com.mcnc.smart.hybrid.common.code.Codes#_JSON_MESSAGE_INFOTEXT}
	 */
	String infoText;

	/**
	 * 전문을 성공적으로 처리한 결과 객체를돌려 준다.
	 * 
	 * @return 전문 처리 객체
	 */
	public static SimpleJsonResponse getSimpleSuccessResponse() {
		return new SimpleJsonResponse(true, "", "", "");
	}

	/**
	 * 전문을 처리한 결과 객체를 돌려 준다.
	 * 
	 * @param result 설정할 처리 결과 <code>true</code>이면 성공, <code>false</code>이면 실패
	 * @param errorCode 설정할 error_code
	 * @param errorText 설정할 error_text
	 * @param infoText 설정할 info_text
	 */
	public SimpleJsonResponse(boolean result, String errorCode, String errorText, String infoText) {
		if (errorCode == null) {
			errorCode = "";
		}

		if (errorText == null) {
			errorText = "";
		}

		if (infoText == null) {
			infoText = "";
		}

		this.result = result;
		this.errorCode = errorCode;
		this.errorText = errorText;
		this.infoText = infoText;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getResultCode() {
		return errorCode;
	}

	public void setResultCode(String resultCode) {
		this.errorCode = resultCode;
	}

	public String getResultText() {
		return errorText;
	}

	public void setResultText(String errorText) {
		this.errorText = errorText;
	}

	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	/**
	 * Json 형식의 문자열로 바꾼다.
	 * 
	 * @return Json 형식의 문자열. 변환에 실패하면, 빈 문자열("")을 반환한다.
	 */
	public String toString() {
		log.trace("Start::toString");

		String text = "";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;

		try {
			g = f.createJsonGenerator(bout, JsonEncoding.UTF8);
			g.writeStartObject();
			g.writeBooleanField("result", result);
			g.writeStringField("error_code", errorCode);
			g.writeStringField("error_text", errorText);
			g.writeStringField("info_text", infoText);
			g.writeEndObject();
			g.flush();
			text = bout.toString();
		} catch (Exception e) {
			try {
				text = "{ \"result\" : \"" + result + "\", \"error_code\" : \"" + errorCode + "\", \"error_text\" : \""
						+ URLEncoder.encode(errorText, "utf8") + "\", \"info_text\" : \""
						+ URLEncoder.encode(infoText, "utf8") + "\" }";
			} catch (UnsupportedEncodingException e1) {
				log.warn("Exception : ", e1);
			}
		}

		try {
			IOUtils.closeQuietly(g);
			IOUtils.closeQuietly(bout);
		} finally {
		}

		log.trace("  > RV(text): " + text);
		log.trace("End::toString()");

		return text;
	}

	/**
	 * <code>JsonNode</code> 객체로 변환
	 * 
	 * @return 변환된 객체
	 */
	public JsonNode toJson() {
		log.trace("Start::toJson");

		JsonNodeFactory f = null;
		ObjectNode rootNode = null;

		try {
			f = JsonNodeFactory.instance;
			rootNode = f.objectNode();
			rootNode.put("result", result);
			rootNode.put("error_code", errorCode);
			rootNode.put("error_text", errorText);
			rootNode.put("info_text", infoText);

		} catch (Exception e) {
			log.warn("The node will be null due to exception : ", e);
			rootNode = null;
		}

		log.trace("  > RV(rootNode): " + rootNode);
		log.trace("End::toJson");

		return rootNode;
	}
}
