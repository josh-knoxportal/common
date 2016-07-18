package org.oh.common.util;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.oh.common.exception.CommonException;

import com.fasterxml.jackson.databind.JsonNode;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * XML <-> JSON 변환 유틸(net.sf.json 사용)
 */
public abstract class XMLJsonUtils2 extends XMLUtils {
	protected static XMLSerializer xmlSerializer = null;

	/**
	 * @return XMLSerializer
	 */
	public static XMLSerializer getXMLSerializer() {
		if (xmlSerializer == null) {
			xmlSerializer = new XMLSerializer();
//			xmlSerializer.setTrimSpaces(true);
//			xmlSerializer.setTypeHintsEnabled(false);
		}

		return xmlSerializer;
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * org.codehaus.jackson.JsonNode -> XmlString 변환
	 */
	public static String convertJsonNodeToXmlString(JsonNode jsonNode) {
		return convertJsonStringToXmlString(jsonNode.toString());
	}

	/**
	 * JsonString -> XmlString 변환
	 */
	public static String convertJsonStringToXmlString(String json) {
		JSON JSON = convertJsonStringToJSON(json);

		return convertJSONXmlString(JSON);
	}

	/**
	 * JsonString -> JSON : JSONObject.fromObject()
	 */
	public static JSON convertJsonStringToJSON(String json) {
		return JSONObject.fromObject(json);
	}

	/**
	 * JSON -> XmlString 변환
	 */
	public static String convertJSONXmlString(JSON json) {
		return getXMLSerializer().write(json);
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * XmlString -> JsonString 변환
	 */
	public static String convertXmlStringToJsonString(String xml) throws CommonException {
		return convertXmlStringToJsonNode(xml).toString();
	}

	public static JsonNode convertXmlStringToJsonNode(String xml) throws CommonException {
		return convertXmlStringToJsonNode(xml, false);
	}

	/**
	 * XmlString -> org.codehaus.jackson.JsonNode 변환
	 */
	public static JsonNode convertXmlStringToJsonNode(String xml, boolean log) throws CommonException {
		if (!Utils.isValidate(xml))
			return JsonUtil2.missingNode();

		if (log)
			LogUtil.writeLog(xml, XMLJsonUtils2.class);

		xml = StringUtil.replace(xml, "null", "NULL");
		String json = convertXmlStringToJSON(xml).toString();
		json = StringUtil.replace(json, "null", "\"\"");

		if (log)
			LogUtil.writeLog(json, XMLJsonUtils2.class);

		JsonNode jsonNode = null;
		try {
			jsonNode = JsonUtil2.readValue(json);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Read json data \"" + json + "\" error", e.getMessage()), e);
		}

		return jsonNode;
	}

	/**
	 * XmlString -> JSON 변환
	 */
	public static JSON convertXmlStringToJSON(String xml) {
		return getXMLSerializer().read(xml);
	}

	public static void main(String[] args) throws Exception {
//		String json = IOUtils.toString((new FileInputStream("test/test02.json")));
//		String xml = convertJsonStringToXmlString(json);
//		System.out.println(xml);
//		System.out.println(convertXmlStringToJsonString(xml));
//
//		System.out.println();

		String xml2 = IOUtils.toString((new FileInputStream("test/test02.xml")));
		String json2 = convertXmlStringToJsonNode(xml2, true).toString();
//		System.out.println(convertJsonStringToXmlString(json2));
	}
}