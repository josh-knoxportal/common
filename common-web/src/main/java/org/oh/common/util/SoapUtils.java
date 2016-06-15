package org.oh.common.util;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.exception.CommonException;

/**
 * SOAP 유틸
 */
public abstract class SoapUtils {
	protected static Log log = LogFactory.getLog(SoapUtils.class);
	protected static SOAPConnectionFactory soapConnFactory = null;

	static {
		try {
			soapConnFactory = SOAPConnectionFactory.newInstance();
		} catch (Exception e) {
			log.error("Create soap connection error", e);
		}
	}

	/**
	 * SOAPMessage를 보낸다.
	 */
	public static SOAPMessage sendMessage(SOAPMessage soapMessage, String url) throws CommonException {
		LogUtil.writeLog("url: " + url, SoapUtils.class);

		SOAPConnection connection = null;
		try {
			connection = soapConnFactory.createConnection();

			return connection.call(soapMessage, url);
		} catch (SOAPException e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Connect soap service error", e.getMessage()), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					throw new CommonException(CommonException.ERROR,
							LogUtil.buildMessage("Close soap connection error", e.getMessage()), e);
				}
			}
		}
	}
}
