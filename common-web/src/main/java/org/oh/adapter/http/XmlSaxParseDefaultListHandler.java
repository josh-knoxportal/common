package org.oh.adapter.http;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.DirectFieldAccessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSaxParseDefaultListHandler<T> extends DefaultHandler {

	private String position = "";

	private T item;

	private List<T> list;

	private Class<T> type;

	private String parentTag;
	private String listTagName;
	private boolean flag = false;

	private Field[] fields;
	private DirectFieldAccessor fieldAccessor;

	public XmlSaxParseDefaultListHandler(Class<T> type, String listTagName) {
		this.type = type;
		this.listTagName = listTagName;
	}

	public XmlSaxParseDefaultListHandler(Class<T> type, String parentTag, String listTagName) {
		this.type = type;
		this.parentTag = parentTag;
		this.listTagName = listTagName;
	}

	public List<T> getParsedData() {
		return list;
	}

	@Override
	public void startDocument() throws SAXException {
		this.list = new ArrayList<T>();
	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// LogUtil.writeLog("before: "+ qName + ", ="+flag, getClass());
		if (parentTag == null || flag) {
			if (qName.equals(this.listTagName)) {
				try {
					item = type.newInstance();
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
				init();
				flag = true;
			} else {
				position = qName;
			}
		} else if (parentTag != null || (!flag)) {
			if (qName.equals(this.parentTag))
				flag = true;
		} else {
			position = qName;
			flag = false;
		}
		// LogUtil.writeLog("after: "+ qName + ", ="+flag, getClass());

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(this.listTagName) && flag) {
			list.add(item);
		} else if (qName.equals(this.parentTag)) {
			flag = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = new String(ch, start, length);
		// LogUtil.writeLog("characters:" + position +", =" + this.flag, getClass());
		if (fields == null) {
			position = "";
			return;
		}
		for (Field field : fields) {
			if (position.equals(field.getName())) {
				fieldAccessor.setPropertyValue(field.getName(), text);
				break;
			}
		}

		position = "";
	}

	private void init() {
		Class<?> clazz = item.getClass();
		fields = clazz.getDeclaredFields();
		fieldAccessor = new DirectFieldAccessor(item);
	}

}
