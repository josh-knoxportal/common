package org.oh.adapter.soap;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oh.adapter.exception.AdapterException;
import org.oh.common.util.AESEncrypter;
import org.oh.common.util.PropertyUtils;
import org.oh.common.util.Utils;
import org.springframework.beans.DirectFieldAccessor;
import org.xml.sax.InputSource;

public abstract class AbstractSOAPMessageMapper<T1, T2> implements ISOAPMessageMapper<T1, T2> {
	protected static Log log = LogFactory.getLog(AbstractSOAPMessageMapper.class);

	protected static final AESEncrypter aesEncrypter = new AESEncrypter();
	protected static MessageFactory messageFactory = null;
	protected static TransformerFactory transformerFactory = null;

	static {
		try {
			messageFactory = MessageFactory.newInstance();
			transformerFactory = TransformerFactory.newInstance();
		} catch (Exception e) {
			log.error("Create soap service error", e);
		}
	}

	protected String trcode = null;
	protected String charset = null;

	public AbstractSOAPMessageMapper(String trcode) {
		this(trcode, "UTF-8");
	}

	public AbstractSOAPMessageMapper(String trcode, String charset) {
		this.trcode = trcode;
		this.charset = charset;
	}

	protected String getSOAPMessageAsString(SOAPMessage soapMessage) throws AdapterException {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			Source sourceContent = soapMessage.getSOAPPart().getContent();
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(sourceContent, result);

			StringBuffer buffer = writer.getBuffer();

			return buffer.toString();
		} catch (Exception e) {
			throw new AdapterException("SOAP002", "SOAP Message Parse Exception.");
		}

	}

	protected String getSOAPBodyAsString(SOAPMessage soapMessage) throws AdapterException {

		String xml = getSOAPMessageAsString(soapMessage);

		String value = null;

		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new InputSource(new StringReader(xml)));

			Element root = document.getRootElement();

			Element e = (Element) root.getChildren().get(0);

			Element e1 = (Element) e.getChildren().get(0);

			value = e1.getValue();

			return value;
		} catch (Exception e) {
			throw new AdapterException("SOAP002", "SOAP Message Parse Exception.", e);
		}
	}

	protected String convertObjectToXmlString(Object obj, String... excludes) {

		StringBuffer sb = new StringBuffer();

		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(obj);

		boolean isExclude = false;
		for (Field field : fields) {
			isExclude = false;
			for (String exclude : excludes) {
				if (field.getName().equalsIgnoreCase(exclude)) {
					isExclude = true;
					break;
				}
			}
			if (isExclude)
				continue;

			Object value = fieldAccessor.getPropertyValue(field.getName());
			sb.append("<" + field.getName() + ">");
			sb.append(value);
			sb.append("</" + field.getName() + ">");
		}

		return sb.toString();
	}

	protected String convertObjectToXmlString(Object obj, String parentName, String... excludes) {

		StringBuffer sb = new StringBuffer("<" + parentName + ">");

		sb.append(convertObjectToXmlString(obj, excludes));

		sb.append("</" + parentName + ">");

		return sb.toString();
	}

	// 확장 ///

	protected SOAPMessage mappingRequestParamToSOAPMessage(String actionUrl, String requestName, String params)
			throws AdapterException {
		return mappingRequestParamToSOAPMessage(actionUrl, requestName, params, false);
	}

	protected SOAPMessage mappingRequestParamToSOAPMessage(String actionUrl, String requestName, String params,
			boolean encrypt) throws AdapterException {
		String serviceName = PropertyUtils.getInstance().getString(trcode);
		String soapAction = actionUrl + serviceName;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			SOAPMessage soapMessage = messageFactory.createMessage();
			soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, charset);

			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
			soapEnvelope.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/soapEnvelope/");
			soapEnvelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
			soapEnvelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");

			if (Utils.isValidate(soapAction)) {
				MimeHeaders mimes = soapMessage.getMimeHeaders();
				mimes.addHeader("SOAPAction", soapAction);
				for (Iterator iter = mimes.getAllHeaders(); iter.hasNext();) {
					MimeHeader mime = (MimeHeader) iter.next();
					log.trace("header: " + mime.getName() + " = " + mime.getValue());
				}
			}

			SOAPBody soapBody = soapEnvelope.getBody();
			SOAPElement bodyElement = soapBody
					.addChildElement(soapEnvelope.createName(serviceName, "", "http://tempuri.org/"));

			params = (encrypt) ? aesEncrypter.encrypt(params) : params;
			bodyElement.addChildElement(requestName).addTextNode(params);
			soapMessage.writeTo(baos);
			log.trace("body: " + baos.toString());

			return soapMessage;
		} catch (Exception e) {
			throw new AdapterException(trcode + AdapterException.PREFIX_SYSTEM + "90", "Mapping soap request data error",
					e);
		} finally {
			IOUtils.closeQuietly(baos);
		}
	}

	protected String mappingSOAPMessageToResponse2(SOAPMessage soapMessage) throws AdapterException {
		return mappingSOAPMessageToResponse2(soapMessage, false);
	}

	protected String mappingSOAPMessageToResponse2(SOAPMessage soapMessage, boolean encrypt) throws AdapterException {
		try {
			Source sourceContent = soapMessage.getSOAPPart().getContent();
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(sourceContent, result);
			String xml = writer.getBuffer().toString();
			log.trace("body: " + xml);

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new InputSource(new StringReader(xml)));
			Element root = document.getRootElement();
			Element e = (Element) root.getChildren().get(0);
			Element e1 = (Element) e.getChildren().get(0);
			String value = e1.getValue();

			if ("Fault".equals(e1.getName())) {
				throw new AdapterException(trcode + AdapterException.PREFIX_USER + "91", value);
			} else {
				return (encrypt) ? aesEncrypter.decrypt(value) : value;
			}
		} catch (Exception e) {
			throw new AdapterException(trcode + AdapterException.PREFIX_SYSTEM + "91",
					"Mapping soap response data error", e);
		}
	}
}
