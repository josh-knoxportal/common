package org.oh.adapter.sap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.oh.adapter.exception.AdapterException;
import org.springframework.stereotype.Component;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRepository;

@Component
public class SapConnector {
	private final static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

	private JCoDestination jcoDestination;

	public SapConnector() {
	}

	public SapConnector(Properties connProp) throws AdapterException {
		FileOutputStream fos = null;
		try {
			File conInfoFile = new File(ABAP_AS_POOLED + ".jcoDestination");

			removeJcoDestinationFile(conInfoFile);

			if (!conInfoFile.isFile()) {
				fos = new FileOutputStream(conInfoFile, false);
				connProp.store(fos, ABAP_AS_POOLED);
			}

		} catch (IOException e) {
			throw new AdapterException("JCO0000", "SAP Interface is non-initialized.", e);
		} finally {
			try {
				if (connProp != null)
					connProp.clear();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
		}

		initJcoConnectionManager(ABAP_AS_POOLED);
	}

	private void initJcoConnectionManager(String poolName) throws AdapterException {
		try {
			jcoDestination = JCoDestinationManager.getDestination(poolName);
		} catch (JCoException e) {
			throw new AdapterException("JCO0001", poolName + " not found in JCO", e);
		}
	}

	public JCoDestination getJcoDestination() throws AdapterException {
		if (jcoDestination == null) {
			initJcoConnectionManager(ABAP_AS_POOLED);
		}

		return this.jcoDestination;
	}

	public Object executeSapInterface(String procedure, Object param, ISapMapper mapper) throws AdapterException {
		try {
			JCoDestination destination = getJcoDestination();
			JCoRepository repository = destination.getRepository();
			JCoFunctionTemplate template = repository.getFunctionTemplate(procedure);

			if (template == null) {
				throw new AdapterException("JCO0001", procedure + " not found in SAP.");
			}

			JCoFunction function = template.getFunction();

			if (function == null) {
				throw new AdapterException("JCO0001", procedure + " not found in SAP.");
			}

			function = mapper.mappingRequestObjectToSapData(function, param);

			function.execute(destination);

			mapper.verifySapResult(function);

			return mapper.mappingResponseSapDataToObject(function);

		} catch (JCoException e) {
			throw new AdapterException("JCO0002", "JCo Function execute error.", e);
		}
	}

	protected boolean removeJcoDestinationFile(File destinationFile) throws AdapterException {
		if (destinationFile.isFile()) {
			return destinationFile.delete();
		} else {
			return true;
		}
	}

	protected boolean removeJcoDestinationFile(String jcoDestinationFullPath) throws AdapterException {
		return removeJcoDestinationFile(new File(jcoDestinationFullPath));
	}
}
