package org.oh.adapter.sap;

import org.oh.adapter.exception.AdapterException;

import com.sap.conn.jco.JCoFunction;

public interface ISapMapper {

	JCoFunction mappingRequestObjectToSapData(JCoFunction function, Object param) throws AdapterException;

	Object mappingResponseSapDataToObject(JCoFunction function) throws AdapterException;

	void verifySapResult(JCoFunction function) throws AdapterException;
}
