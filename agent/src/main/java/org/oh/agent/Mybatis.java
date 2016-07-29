package org.oh.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mybatis {
	protected static Log log = LogFactory.getLog(Mybatis.class);
	protected static Map<String, SqlSessionFactory> factoryMap = new HashMap<String, SqlSessionFactory>();

	public static SqlSessionFactory getSqlSessionFactory(String db) {
		return factoryMap.get(db);
	}

	public Mybatis() throws AgentException {
		String fileName = "mybatis-config.xml";

		init(fileName);

		try {
			for (String id : factoryMap.keySet()) {
				InputStream inputStream = new FileInputStream("conf/" + fileName);
//				InputStream inputStream = Resources.getResourceAsStream(fileName);

				factoryMap.put(id, new SqlSessionFactoryBuilder().build(inputStream, id));
			}
		} catch (Exception e) {
			throw new AgentException("Mybatis_001", "Mybatis 설정파일 로드 중 오류 발생", e);
		}
	}

	protected void init(String fileName) throws AgentException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new File("conf/" + fileName));
//			doc = db.parse(getClass().getClassLoader().getResourceAsStream(fileName));
		} catch (Exception e) {
			throw new AgentException("Mybatis_002", "Mybatis DB정보 로드 중 오류 발생", e);
		}
		if (doc == null)
			return;

		Element element = doc.getDocumentElement();
		NodeList nlRoot = element.getChildNodes();
		if (!"configuration".equals(element.getTagName()))
			return;

		NodeList nodeList = null;
		Node node = null;
		NamedNodeMap nnm = null;
		Node nID = null;
		for (int i = 0; i < nlRoot.getLength(); i++) {
			Node nRoot = nlRoot.item(i);
			if ("environments".equals(nRoot.getNodeName())) {
				nodeList = nRoot.getChildNodes();
				for (int j = 0; j < nodeList.getLength(); j++) {
					node = nodeList.item(j);
					if ("environment".equals(node.getNodeName())) {
						nnm = node.getAttributes();
						nID = nnm.getNamedItem("id");
						if (nID != null)
							factoryMap.put(nID.getNodeValue(), null);
					}
				}
			}
		}
	}
}
