package org.oh.adapter;

import org.oh.adapter.exception.AdapterException;
import org.oh.adapter.soap.ISOAPMessageMapper;

public abstract interface SOAPAdapter {
	public abstract <T1, T2> T2 sendAndReceive(String paramString, T1 paramT1,
			ISOAPMessageMapper<T1, T2> paramISOAPMessageMapper) throws AdapterException;
}
