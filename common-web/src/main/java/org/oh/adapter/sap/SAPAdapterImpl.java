package org.oh.adapter.sap;

import org.oh.adapter.SAPAdapter;
import org.oh.adapter.aspect.AuditRequired;
import org.oh.adapter.exception.AdapterException;
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
