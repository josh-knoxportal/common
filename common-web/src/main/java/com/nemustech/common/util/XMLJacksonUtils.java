package com.nemustech.common.util;

import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nemustech.common.exception.CommonException;

/**
 * XML <-> JSON 변환 유틸
 * : XML의 array 와 attribute 불가
 */
public abstract class XMLJacksonUtils extends XMLUtils {
	protected static XmlMapper xmlMapper = null;

	/**
	 * @return XmlMapper
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
	 * JsonString -> XmlString 변환
	 */
	public static String convertJsonStringToXmlString(String json) throws CommonException {
		JsonNode jsonNode = convertJsonStringToJsonNode(json);

		return convertJsonNodeToXmlString(jsonNode);
	}

	/**
	 * JsonString -> JsonNode 변환
	 */
	public static JsonNode convertJsonStringToJsonNode(String json) throws CommonException {
		if (!Utils.isValidate(json))
			return MissingNode.getInstance();

		JsonNode jsonNode = null;
		try {
			jsonNode = JsonUtil2.getObjectMapper().readTree(json);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert json to jsonNode \"" + json + "\" error", e.getMessage()), e);
		}

		return jsonNode;
	}

	/**
	 * JsonNode -> XmlString 변환
	 */
	public static String convertJsonNodeToXmlString(JsonNode jsonNode) throws CommonException {
		String xml = null;
		try {
			xml = getXmlMapper().writeValueAsString(jsonNode);
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml;
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert jsonNode to xml \"" + jsonNode + "\" error", e.getMessage()), e);
		}

		return xml;
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * XmlString -> JsonNode 변환
	 */
	public static JsonNode convertXmlStringToJsonNode(String xml) throws CommonException {
		if (!Utils.isValidate(xml))
			return MissingNode.getInstance();

		String json = convertXmlStringToJsonString(xml);

		return convertJsonStringToJsonNode(json);
	}

	/**
	 * XmlString -> JsonString 변환
	 */
	public static String convertXmlStringToJsonString(String xml) {
		xml = StringUtil.replace(xml, "null", "NULL");

		String json = null;
		try {
			json = getXmlMapper().readTree(xml).toString();
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert xml to json \"" + xml + "\" error", e.getMessage()), e);
		}

		return StringUtil.replace(json, "null", "\"\"");
	}

	public static void main(String[] args) throws Exception {
		String xml = IOUtils.toString(new FileInputStream("src/test/resources/xml/mail.xml"),
				Charset.defaultCharset());
		System.out.println(xml);
		String json = convertXmlStringToJsonNode(xml).toString();
		System.out.println(JsonUtil2.toStringPretty(json));

		String xml_ = convertJsonStringToXmlString(json);
		System.out.println(xml_);
		String json_ = convertXmlStringToJsonNode(xml_).toString();
		System.out.println(JsonUtil2.toStringPretty(json_));
	}
}