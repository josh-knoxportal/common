package com.nemustech.adapter.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.io.IOUtils;
import com.nemustech.adapter.exception.AdapterException;
import com.nemustech.common.util.LogUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;

public class DefaultDomParser {

	private byte[] bytes;

	public DefaultDomParser(byte[] bytes) {
		this.bytes = bytes;
	}

	public DefaultDomParser(InputStream is) {
		try {
			this.bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			LogUtil.writeLog(e, getClass());
		}
	}

	public <T> T fullParsing(Class<T> resClass) throws Exception {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document doc = parser.parse(inputStream);

		NodeList nodes = doc.getChildNodes();
		JsonNode resJsonNode = resultParse(nodes);

		@SuppressWarnings("unchecked")
		T response = (T) ConstructorUtils.invokeConstructor(resClass, resJsonNode);
		return response;
	}

	public <T> T elementParsing(Class<T> resClass, String findElement) throws Exception {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document doc = parser.parse(inputStream);

		NodeList nodes = doc.getElementsByTagName(findElement);
		JsonNode resJsonNode = resultParse(nodes);

		@SuppressWarnings("unchecked")
		T response = (T) ConstructorUtils.invokeConstructor(resClass, resJsonNode);
		return response;
	}

	public JsonNode fullParsing() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document doc = parser.parse(inputStream);

		NodeList nodes = doc.getChildNodes();
		JsonNode resJsonNode = resultParse(nodes);
		return resJsonNode;
	}

	public JsonNode elementParsing(String findElement) throws Exception {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document doc = parser.parse(inputStream);

		NodeList nodes = doc.getElementsByTagName(findElement);
		JsonNode resJsonNode = resultParse(nodes);
		return resJsonNode;
	}

	private JsonNode resultParse(NodeList nodes) throws AdapterException {

		Map<String, Object> resMap = new HashMap<String, Object>();
		JsonNode resNode = null;
		if (nodes != null) {

			int listCount = nodes.getLength();

			for (int i = 0; i < listCount; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Node node = nodes.item(i);

				if (isParsing(node)) {
					map = parse(node);
					resMap.putAll(map);
				}
			}
		}

		resNode = AdapterUtil.ConvertJsonNode(resMap);
		return resNode;
	}

	private Map<String, Object> parse(Node node) {
		boolean hasChildNode = node.hasChildNodes();
		String nodeName = node.getNodeName();
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (hasChildNode) {
			NodeList nodeList = node.getChildNodes();
			int listCount = nodeList.getLength();

			for (int index = 0; index < listCount; index++) {

				Node subNode = nodeList.item(index);
				boolean isParsing = isParsing(subNode);
				if (isParsing) {

					boolean hasSubNodeChild = subNode.hasChildNodes();

					if (hasSubNodeChild) {
						Map<String, Object> dataPasing = nextParsing(subNode);

						if (listCount == 1) {
							resultMap = dataPasing;
						} else {
							listData.add(dataPasing);
							resultMap.put(nodeName, listData);
						}
					} else {

						String subNodeName = subNode.getNodeName();
						String subNodeValue = nullCheck(subNode.getNodeValue());

						resultMap.put(subNodeName, subNodeValue);
					}
//					LogUtil.writeLog("parentNode: " + subNode.getParentNode().getNodeName() + ", NodeName: " + subNode.getNodeName() + " - " + subNode.getChildNodes().getLength() + ", " + subNode.hasChildNodes(), getClass());
				}
			}
		}
		return resultMap;
	}

	private Map<String, Object> nextParsing(Node node) {
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String nodeName = node.getNodeName();
		NodeList nodeList = node.getChildNodes();
		int listCount = nodeList.getLength();

		for (int index = 0; index < listCount; index++) {

			Node subNode = nodeList.item(index);
			boolean hasChildNode = subNode.hasChildNodes();
			if (hasChildNode) {
				String parentNodeName = subNode.getParentNode().getNodeName();

				listData = childNodeParsing(subNode, listData);
				resultMap.put(parentNodeName, listData);
			} else {
				String subNodeValue = subNode.getNodeValue();
				boolean isSubnodeParsing = isParsing(subNode);
				if (listCount == 1) {
					resultMap.put(nodeName, subNodeValue);
				} else if (isSubnodeParsing) {
					resultMap.put(nodeName, subNodeValue);
				}
			}

		}
		return resultMap;
	}

	private List<Map<String, Object>> childNodeParsing(Node subNode, List<Map<String, Object>> listData) {

		List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String subNodeName = subNode.getNodeName();
		NodeList nodeList = subNode.getChildNodes();
		int listCount = nodeList.getLength();

		for (int index = 0; index < listCount; index++) {
			Node childNode = nodeList.item(index);
			String childNodeName = childNode.getNodeName();
			String childNodeValue = nullCheck(childNode.getNodeValue());
			boolean hasChildNode = childNode.hasChildNodes();
			boolean isParsing = isParsing(childNode);
			if (hasChildNode) {
				subList.add(nextParsing(childNode));
				dataMap.put(subNodeName, subList);
			} else {
				if (listCount == 1) {
					dataMap.put(subNodeName, childNodeValue);
				} else if (isParsing) {
					dataMap.put(childNodeName, childNodeValue);
				}
			}
		}

		if (dataMap.size() != 0) {
			listData.add(dataMap);
		}

		return listData;
	}

	private String nullCheck(String nodeValue) {
		if (nodeValue == null) {
			return "";
		}

		return nodeValue;
	}

	private boolean isParsing(Node node) {
		short nodeType = node.getNodeType();

		if (nodeType == Node.ELEMENT_NODE) {
			return true;
		}

		return false;
	}
}
