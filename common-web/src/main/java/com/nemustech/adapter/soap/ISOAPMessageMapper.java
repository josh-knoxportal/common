package com.nemustech.adapter.soap;

import javax.xml.soap.SOAPMessage;

import com.nemustech.adapter.exception.AdapterException;

public interface ISOAPMessageMapper<T1, T2> {

	public SOAPMessage mappingRequestParamToSOAPMessage(T1 params) throws AdapterException;

	public T2 mappingSOAPMessageToResponse(SOAPMessage soapMessage) throws AdapterException;
}
