package org.oh.adapter.sap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oh.adapter.exception.AdapterException;
import org.springframework.beans.DirectFieldAccessor;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecordField;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public abstract class AbstractSapMapper implements ISapMapper {

	protected <T> T covertSapStructureToObject(JCoStructure structure, Class<T> type) throws AdapterException {

		try {
			T model = type.newInstance();
			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(model);

			for (JCoFieldIterator iter = structure.getFieldIterator(); iter.hasNextField();) {
				JCoField field = iter.nextField();
				fieldAccessor.setPropertyValue(field.getName(), field.getValue());
			}
			return model;
		} catch (InstantiationException e) {
			throw new AdapterException("COV001", "", e);
		} catch (IllegalAccessException e) {
			throw new AdapterException("COV001", "", e);
		}

	}

	protected <T> List<T> convertSapTableToObjectList(JCoTable table, Class<T> type) throws AdapterException {

		try {
			List<T> list = new ArrayList<T>();

			if (table.getNumRows() > 0) {
				table.firstRow();
				do {
					T model = type.newInstance();
					DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(model);
					for (JCoRecordFieldIterator iter = table.getRecordFieldIterator(); iter.hasNextField();) {
						JCoRecordField recoredField = iter.nextRecordField();
						if (fieldAccessor.isReadableProperty(recoredField.getName())) {
							fieldAccessor.setPropertyValue(recoredField.getName(), recoredField.getValue());
						}
					}
					list.add(model);
				} while (table.nextRow());
			}

			return list;
		} catch (InstantiationException e) {
			throw new AdapterException("COV001", "", e);
		} catch (IllegalAccessException e) {
			throw new AdapterException("COV001", "", e);
		}

	}

	protected <T> JCoTable convertObjectListToSapTable(List<T> list, JCoTable table) {

		table.firstRow();
		for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
			T model = iter.next();
			table.appendRow();

			Class<?> clazz = model.getClass();
			Field[] fields = clazz.getDeclaredFields();
			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(model);
			for (Field field : fields) {
				Object value = fieldAccessor.getPropertyValue(field.getName());
				table.setValue(field.getName(), value);
			}
		}
		return table;

	}

	protected JCoFunction setParametersToSapImportParam(Object parameters, JCoFunction function, String... excludes) {

		Class<?> clazz = parameters.getClass();
		Field[] fields = clazz.getDeclaredFields();
		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(parameters);
		for (Field field : fields) {
			for (String exclude : excludes) {
				if (field.getName().equalsIgnoreCase(exclude))
					continue;
			}
			Object value = fieldAccessor.getPropertyValue(field.getName());
			function.getImportParameterList().setValue(field.getName(), value);
		}

		return function;
	}

}
