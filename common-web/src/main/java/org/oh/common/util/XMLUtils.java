package org.oh.common.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.oh.common.exception.CommonException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XML 유틸
 */
public abstract class XMLUtils {
	protected static DocumentBuilderFactory dbf = null;
	protected static DocumentBuilder db = null;
	protected static XPath xpath = null;

	// org.w3c.dom //////////

	static {
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);

		try {
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			LogUtil.writeLog(e, XMLUtils.class);
		}
	}

	/**
	 * JSON에 포함된 XML 데이타를 교정한다.
	 * 
	 * @param xml
	 * 
	 * @return
	 */
	public static String correctXmlInJson(String xml) {
		return StringUtil.replace(xml, "\\\"", "\"");
	}

	/**
	 * Get first value by tag name
	 */
	public static String getFirstValueByTagName(String xml, String tagName) {
		return getFirstValueByTagName(convertXmlStringToDocument(xml), tagName);
	}

	/**
	 * Get first value by tag name
	 */
	public static String getFirstValueByTagName(Document doc, String tagName) {
		if (doc == null)
			return "";

		NodeList nodes = doc.getElementsByTagName(tagName);
		Node node = nodes.item(0);
		if (node == null || node.getFirstChild() == null)
			return "";

		return node.getFirstChild().getNodeValue();
	}

	/**
	 * Get value by id
	 */
	@Deprecated
	public static String getValueById(String xml, String idValue) {
		return getValueById(convertXmlStringToDocument(xml), idValue);
	}

	/**
	 * Get value by id
	 */
	@Deprecated
	public static String getValueById(Document doc, String idValue) {
		if (doc == null)
			return "";

		Element element = doc.getElementById(idValue);
		if (element == null || element.getFirstChild() == null)
			return "";

		return element.getFirstChild().getNodeValue();
	}

	/**
	 * XMLString -> Map 변환
	 */
	public static Map<String, String> convertXmlStringToMap(String xml) {
		return convertXmlStringToMap(convertXmlStringToDocument(xml));
	}

	/**
	 * Document -> Map 변환
	 */
	public static Map<String, String> convertXmlStringToMap(Document doc) {
		Map<String, String> map = new HashMap<String, String>();
		if (doc == null)
			return map;

		Element element = doc.getDocumentElement(); // 1 dept (root)
		NodeList nlRoot = element.getChildNodes();
		Node nRoot = null;
		NodeList nodeList = null;
		Node node = null;
		NamedNodeMap nnm = null;
		Node nID = null;
		for (int i = 0; i < nlRoot.getLength(); i++) {
			nRoot = nlRoot.item(i);
			if ("#text".equals(nRoot.getNodeName()))
				continue;
			nodeList = nRoot.getChildNodes(); // 2 dept
			for (int j = 0; j < nodeList.getLength(); j++) {
				node = nodeList.item(j); // 3 dept
				if ("#text".equals(node.getNodeName()))
					continue;
//				map.put(node.getNodeName(), node.getFirstChild().getNodeValue());
				nnm = node.getAttributes();
				nID = nnm.getNamedItem("id");
				if (nID != null)
					map.put(nID.getNodeValue(), node.getFirstChild().getNodeValue());
			}
		}

		return map;
	}

	/**
	 * XMLString -> Document 변환
	 */
	public static Document convertXmlStringToDocument(String xml) throws CommonException {
		Document doc = null;
		try {
			doc = db.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, "Parse xml data \"" + xml + "\" error", e);
		}

		return doc;
	}

	// javax.xml.xpath //////////

	/**
	 * @return javax.xml.xpath.XPath
	 */
	public static XPath getXPath() {
		if (xpath == null) {
			xpath = XPathFactory.newInstance().newXPath();
		}

		return xpath;
	}

	/**
	 * Get first value by tag name
	 */
	public static String getFirstValueByTagName2(String xml, String tagName) {
		return getValueByXPath(convertXmlStringToDocument(xml), "//*/" + tagName);
	}

	/**
	 * Get first value by id
	 */
	public static String getFirstValueByAttrId(String xml, String idValue) {
		return getFirstValueByAttr(xml, "id", idValue);
	}

	/**
	 * Get first value by name
	 */
	public static String getFirstValueByAttr(String xml, String attrName, String attrValue) {
		return getValueByXPath(convertXmlStringToDocument(xml), "//*[@" + attrName + "='" + attrValue + "']");
	}

	/**
	 * Get value by expression
	 *
	 * @param expression <a href="http://www.w3.org/TR/xpath">XML Path Language (XPath) Version 1.0</a>
	 */
	public static String getValueByXPath(Document doc, String expression) throws CommonException {
		if (doc == null)
			return "";

		String value = null;
		try {
			value = (String) getXPath().evaluate(expression, doc, XPathConstants.STRING);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, "Evaluate xml expression \"" + expression + "\" error", e);
		}

		return value;
	}

	public static void main(String[] args) throws Exception {
//		String json = IOUtils.toString((new FileInputStream("test/test02.json")));
//
//		JsonNode rootNode = JsonUtil2.readValue(json);
//		ObjectNode objectNode = (ObjectNode) JsonUtil2.find(rootNode, "object/attrs");
//		LogUtil.writeLog(objectNode);
//
//		ObjectNode headerNode = objectNode.putObject("header");
//		JsonUtil2.putValue2(headerNode, "trcode", "TR0001");
//		LogUtil.writeLog(objectNode);
//
//		objectNode = (ObjectNode) JsonUtil2.getValue(rootNode, "object");
//		LogUtil.writeLog(objectNode);
//
//		String xml = IOUtils.toString((new FileInputStream("test/test02.xml")));
//
//		LogUtil.writeLog(convertXmlStringToMap(xml));
//
//		LogUtil.writeLog(getFirstValueByTagName(xml, "RESULT"));
//		LogUtil.writeLog(getValueById(xml, "RESULT2"));
//
//		LogUtil.writeLog(getFirstValueByTagName2(xml, "RESULT"));
//		LogUtil.writeLog(getFirstValueByAttrId(xml, "RESULT2"));
	}
}