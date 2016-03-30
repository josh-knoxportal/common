package org.oh.adapter.eai;

import org.oh.adapter.exception.AdapterException;

import COM.activesw.api.client.BrokerEvent;

/**
 * EAI 기본 매퍼
 */
public interface EAIMapper<T1, T2> {
	public void mappingRequestParam(BrokerEvent event, T1 params) throws AdapterException;

	public T2 mappingResponseParam(BrokerEvent event) throws AdapterException;
}
