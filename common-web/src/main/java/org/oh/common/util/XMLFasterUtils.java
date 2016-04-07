package org.oh.common.util;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.oh.common.exception.CommonException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * XML <-> JSON 변환 유틸(com.fasterxml.jackson.dataformat.xml 사용)
 * : XML의 array 와 attribute 불가
 */
public abstract class XMLFasterUtils extends XMLUtils {
	protected static ObjectMapper objectMapper = null;
	protected static XmlMapper xmlMapper = null;

	/**
	 * @return com.fasterxml.jackson.databind.ObjectMapper
	 *
	 */
	public static ObjectMapper getFasterxmlObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		return objectMapper;
	}

	/**
	 * @return com.fasterxml.jackson.dataformat.xml.XmlMapper
	 */
	public static XmlMapper getXmlMapper() {
		if (xmlMapper == null) {
			JacksonXmlModule module = new JacksonXmlModule();
//			module.setDefaultUseWrapper(false);
			xmlMapper = new XmlMapper(module);
		}

		return xmlMapper;
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * org.codehaus.jackson.JsonNode -> XmlString 변환
	 */
	public static String convertJsonNodeToXmlString(org.codehaus.jackson.JsonNode jsonNode) throws CommonException {
		return convertJsonStringToXmlString(jsonNode.toString());
	}

	/**
	 * JsonString -> XmlString 변환
	 */
	public static String convertJsonStringToXmlString(String json) throws CommonException {
		JsonNode jsonNode = convertJsonStringToJsonNode(json);

		return convertJsonNodeToXmlString(jsonNode);
	}

	/**
	 * JsonString -> com.fasterxml.jackson.databind.JsonNode 변환
	 */
	public static JsonNode convertJsonStringToJsonNode(String json) throws CommonException {
		JsonNode jsonNode = null;
		try {
			jsonNode = getFasterxmlObjectMapper().readTree(json);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert string to json \"" + json + "\" error", e.getMessage()), e);
		}

		return jsonNode;
	}

	/**
	 * com.fasterxml.jackson.databind.JsonNode -> XmlString 변환
	 */
	public static String convertJsonNodeToXmlString(JsonNode json) throws CommonException {
		String xml = null;
		try {
			xml = getXmlMapper().writeValueAsString(json);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert json to xml \"" + json + "\" error", e.getMessage()), e);
		}

		return xml;
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * XmlString -> JsonString 변환
	 */
	public static String convertXmlStringToJsonString(String xml) throws CommonException {
		return convertXmlStringToJsonNode(xml).toString();
	}

	public static org.codehaus.jackson.JsonNode convertXmlStringToJsonNode(String xml) {
		return convertXmlStringToJsonNode(xml, false);
	}

	/**
	 * XmlString -> org.codehaus.jackson.JsonNode 변환
	 */
	public static org.codehaus.jackson.JsonNode convertXmlStringToJsonNode(String xml, boolean log)
			throws CommonException {
		if (!Utils.isValidate(xml))
			return JsonUtil.missingNode();

		if (log)
			LogUtil.writeLog(xml, XMLFasterUtils.class);

		xml = StringUtil.replace(xml, "null", "NULL");
		String json = convertXmlStringToJsonNode2(xml).toString();
		json = StringUtil.replace(json, "null", "\"\"");

		if (log)
			LogUtil.writeLog(json, XMLFasterUtils.class);

		org.codehaus.jackson.JsonNode jsonNode = null;
		try {
			jsonNode = JsonUtil.readValue(json);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Read json data \"" + json + "\" error", e.getMessage()), e);
		}

		return jsonNode;
	}

	/**
	 * XmlString -> com.fasterxml.jackson.databind.JsonNode 변환
	 */
	public static JsonNode convertXmlStringToJsonNode2(String xml) throws CommonException {
		if (!Utils.isValidate(xml))
			return com.fasterxml.jackson.databind.node.MissingNode.getInstance();

		JsonNode jsonNode = null;
		try {
			jsonNode = getXmlMapper().readValue(xml, JsonNode.class);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert xml to json \"" + xml + "\" error", e.getMessage()), e);
		}

		return jsonNode;
	}

	public static void main(String[] args) throws Exception {
//		String json = IOUtils.toString((new FileInputStream("test/test02.json")));
//		String xml = convertJsonStringToXmlString(json);
//		LogUtil.writeLog(xml);
//		LogUtil.writeLog(convertXmlStringToJsonString(xml));
//
//		LogUtil.writeLog();

		String xml2 = IOUtils.toString((new FileInputStream("test/test02.xml")));
		String json2 = convertXmlStringToJsonNode(xml2, true).toString();
//		LogUtil.writeLog(convertJsonStringToXmlString(json2));
	}
}