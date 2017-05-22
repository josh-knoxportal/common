package com.nemustech.common.util;

import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.nemustech.common.exception.CommonException;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * XML <-> JSON 변환 유틸 (com.fasterxml.jackson.databind.JsonNode 사용)
 */
public class XMLJsonUtils2 {
	protected XMLSerializer xmlSerializer = null;

	public XMLJsonUtils2(String rootName, String objectName, String elementName) {
		xmlSerializer = new XMLSerializer();
//		xmlSerializer.setTrimSpaces(true);
//		xmlSerializer.setTypeHintsEnabled(false);
		if (rootName != null)
			xmlSerializer.setRootName(rootName);
		if (objectName != null)
			xmlSerializer.setObjectName(objectName);
		if (elementName != null)
			xmlSerializer.setElementName(elementName);
	}

	/**
	 * JsonNode -> XmlString 변환
	 */
	public String convertJsonNodeToXmlString(JsonNode jsonNode) {
		return convertJsonStringToXmlString(jsonNode.toString());
	}

	/**
	 * JsonString -> XmlString 변환
	 */
	public String convertJsonStringToXmlString(String json) {
		JSON JSON = convertJsonStringToJSON(json);

		return convertJSONXmlString(JSON);
	}

	/**
	 * JsonString -> JSON 변환
	 */
	public JSON convertJsonStringToJSON(String json) {
		return JSONObject.fromObject(json);
	}

	/**
	 * JSON -> XmlString 변환
	 */
	public String convertJSONXmlString(JSON json) {
		return xmlSerializer.write(json);
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * XmlString -> JsonString 변환
	 */
	public String convertXmlStringToJsonString(String xml) throws CommonException {
		return convertXmlStringToJsonNode(xml).toString();
	}

	/**
	 * XmlString -> JsonNode 변환
	 */
	public JsonNode convertXmlStringToJsonNode(String xml) throws CommonException {
		if (!Utils.isValidate(xml))
			return MissingNode.getInstance();

		xml = StringUtil.replace(xml, "null", "NULL");
		String json = convertXmlStringToJSON(xml).toString();
		json = StringUtil.replace(json, "null", "\"\"");

		JsonNode jsonNode = null;
		try {
			jsonNode = JsonUtil2.readValue(json);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert json to jsonNode \"" + json + "\" error", e.getMessage()), e);
		}

		return jsonNode;
	}

	/**
	 * XmlString -> JSON 변환
	 */
	public JSON convertXmlStringToJSON(String xml) {
		return xmlSerializer.read(xml);
	}

	public static void main(String[] args) throws Exception {
		XMLJsonUtils2 xmlJsonUtils = new XMLJsonUtils2("smartplant-safety", "object", "element");

		String xml = IOUtils.toString(new FileInputStream("src/test/resources/xml/safety-worker-conf.xml"),
				Charset.defaultCharset());
		System.out.println(xml);

		String json = xmlJsonUtils.convertXmlStringToJsonString(xml);
		System.out.println(JsonUtil2.toStringPretty(json));

//		json = IOUtils.toString(new FileInputStream("src/test/resources/json/mail.json"),
//				Charset.defaultCharset());
		String xml_ = xmlJsonUtils.convertJsonStringToXmlString(json);
		System.out.println(xml_);

		String json_ = xmlJsonUtils.convertXmlStringToJsonString(xml_);
		System.out.println(JsonUtil2.toStringPretty(json_));
	}
}