package com.nemustech.adapter.sap;

import com.nemustech.adapter.SAPAdapter;
import com.nemustech.adapter.aspect.AuditRequired;
import com.nemustech.adapter.exception.AdapterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SAPAdapterImpl implements SAPAdapter {
	@Autowired
	private SapConnector sapConnector;

	public SAPAdapterImpl() {
	}

	@AuditRequired
	public Object execute(String functionName, Object parameter, ISapMapper mapper) throws AdapterException {

		return sapConnector.executeSapInterface(functionName, parameter, mapper);
	}
}
