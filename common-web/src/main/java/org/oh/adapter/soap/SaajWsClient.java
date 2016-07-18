package org.oh.adapter.soap;

import java.io.StringWriter;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.oh.common.util.LogUtil;
import org.springframework.stereotype.Component;

@Component
public class SaajWsClient {
	public SOAPMessage sendAndReceive(String url, SOAPMessage message) throws SOAPException {
		SOAPConnectionFactory soapConnFactory;
		SOAPConnection connection = null;
		SOAPMessage response;

		try {
			soapConnFactory = SOAPConnectionFactory.newInstance();
			connection = soapConnFactory.createConnection();

			response = connection.call(message, url);

			return response;
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	public static void main(String[] args) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage message = messageFactory.createMessage();

		message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

		SOAPPart soapPart = message.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		SOAPBody body = envelope.getBody();

		SOAPElement bodyElement = body.addChildElement(envelope.createName("MobileApproval", "",
				"http://confwfdev.lotte.net/"));
		bodyElement
				.addChildElement("mobileXml")
				.addTextNode(
						"<?xml version='1.0' encoding='UTF-8'?><MOBILE_REQUEST MODE='APPROVALTYPE_LIST' USERID='confadmin'></MOBILE_REQUEST>");

		String url = "http://confwfdev.lotte.net/um/MobileWS/Approval.asmx";
		SaajWsClient client = new SaajWsClient();
		SOAPMessage soapMessage = client.sendAndReceive(url, message);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		Source sourceContent = soapMessage.getSOAPPart().getContent();
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(sourceContent, result);

		StringBuffer buffer = writer.getBuffer();
		System.out.println(buffer.toString());
	}
}
