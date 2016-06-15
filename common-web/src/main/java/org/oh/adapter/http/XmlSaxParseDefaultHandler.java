package org.oh.adapter.http;

import java.lang.reflect.Field;

import org.springframework.beans.DirectFieldAccessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSaxParseDefaultHandler<T> extends DefaultHandler {

	private String position = "";

	private T target;
	private Class<T> type;
	private Field[] fields;
	private DirectFieldAccessor fieldAccessor;

	public XmlSaxParseDefaultHandler(Class<T> type) {
		this.type = type;
	}

	public T getParsedData() {
		return target;
	}

	@Override
	public void startDocument() throws SAXException {

		try {
			target = type.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		init();
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		position = qName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = new String(ch, start, length);
		// LogUtil.writeLog("value: " + text, getClass());

		for (Field field : fields) {
			// LogUtil.writeLog("tagname: " + position + ", property: " + field.getName(), getClass());
			if (position.equals(field.getName())) {
				fieldAccessor.setPropertyValue(field.getName(), text);
				break;
			}
		}

		position = "";

	}

	private void init() {
		Class<?> clazz = target.getClass();
		fields = clazz.getDeclaredFields();
		fieldAccessor = new DirectFieldAccessor(target);
	}

}
