package com.nemustech.adapter.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.nemustech.adapter.exception.AdapterException;
import com.nemustech.adapter.util.AdapterUtil;
import com.nemustech.common.util.JsonUtil2;
import org.springframework.beans.DirectFieldAccessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/**
 * SAP 기본 매퍼
 * : SAP 서비스와 연계
 */
public class SapMapper extends AbstractSapMapper {
	public static final String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

	private static final Log log = LogFactory.getLog(SapMapper.class);

	public SapMapper() {
	}

	/**
	 * 공통 로그 출력
	 */
	protected void loggingCommon(JCoFunction function) {
		if (function == null || function.getTableParameterList() == null)
			return;

		JCoFieldIterator jCoFieldIterator = function.getTableParameterList().getFieldIterator();
		while (jCoFieldIterator.hasNextField()) {
			log.trace(function.getTableParameterList().getTable(jCoFieldIterator.nextField().getName()).toString());
		}
	}

	/**
	 * 입력 로그 출력
	 */
	protected void loggingInput(JCoFunction function) throws AdapterException {
		if (function == null || function.getImportParameterList() == null)
			return;

		try {
			log.debug(JCoDestinationManager.getDestination(ABAP_AS_POOLED).toString());
		} catch (Exception e) {
			throw new AdapterException("", "Load " + ABAP_AS_POOLED + " destination error", e);
		}

		log.trace("========== SAP INPUT START ==========");
		log.trace(function.getImportParameterList().toString());
		loggingCommon(function);
		log.trace("========== SAP INPUT END ==========");
	}

	/**
	 * 출력 로그 출력
	 */
	protected void loggingOutput(JCoFunction function, String trCode) throws AdapterException {
		if (function == null || function.getExportParameterList() == null)
			return;

		log.trace("========== SAP OUTPUT START==========");
		log.trace(function.getExportParameterList().toString());
		loggingCommon(function);
		log.trace("========== SAP OUTPUT END ==========");
	}

	/**
	 * Import String 값 추출
	 */
	protected String getStringImport(JCoFunction function, String paramString) {
		return function.getImportParameterList().getString(paramString);
	}

	/**
	 * Import int 값 추출
	 */
	protected int getIntImport(JCoFunction function, String paramString) {
		return function.getImportParameterList().getInt(paramString);
	}

	/**
	 * Export String 값 추출
	 */
	protected String getStringExport(JCoFunction function, String paramString) {
		return function.getExportParameterList().getString(paramString);
	}

	/**
	 * Export byte[] 값 추출
	 */
	protected byte[] getBytesExport(JCoFunction function, String paramString) {
		return function.getExportParameterList().getByteArray(paramString);
	}

	/**
	 * Export int 값 추출
	 */
	protected int getIntExport(JCoFunction function, String paramString) {
		return function.getExportParameterList().getInt(paramString);
	}

	/**
	 * Table 추출
	 */
	protected JCoTable getTable(JCoFunction function, String paramString) {
		return function.getTableParameterList().getTable(paramString);
	}

	/**
	 * JCoParameterList -> Object 변환
	 */
	protected <T> T covertSapParameterToObject(JCoParameterList parameters, Class<T> type, String... excludes)
			throws AdapterException {
		try {
			Object obj = type.newInstance();
			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(obj);

			JCoFieldIterator fields = parameters.getFieldIterator();
			while (fields.hasNextField()) {
				JCoField field = fields.nextField();
				if (ArrayUtils.contains(excludes, field.getName()))
					continue;

				fieldAccessor.setPropertyValue(field.getName(), field.getValue());
			}

			return (T) obj;
		} catch (Exception e) {
			throw new AdapterException("", "Mapping response date error", e);
		}
	}

	@Override
	public JCoFunction mappingRequestObjectToSapData(JCoFunction function, Object params) throws AdapterException {
		JsonNode bodyNode = AdapterUtil.ConvertJsonNode(params);

		// import data
		JCoParameterList importParam = function.getImportParameterList();
		if (importParam != null) {
			JCoFieldIterator importIter = importParam.getFieldIterator();
			if (importIter != null) {
				while (importIter.hasNextField() == true) {
					JCoField field = importIter.nextField();
					String fieldName = field.getName();

					if (field.getType() == JCoMetaData.TYPE_STRUCTURE) {
						JsonNode objectNode = bodyNode.get(fieldName);
						JCoStructure structure = field.getStructure();
						JCoFieldIterator structIter = structure.getFieldIterator();

						while (structIter.hasNextField() == true) {
							JCoField structField = structIter.nextField();
							String structFieldName = structField.getName();
							if (objectNode.get(structFieldName) == null) {
//								throw new AdapterException("SAP" + PointiException.PREFIX_USER + "00",
//										"입력 데이터(Structure) 오류: " + structFieldName + " 데이터를 확인해 주세요.");
								continue;
							}

							Object structFieldValue = JsonUtil2.getValue(objectNode, structFieldName);
							structure.setValue(structFieldName, structFieldValue);
						}
					} else {
						if (bodyNode.get(fieldName) == null) {
//							throw new AdapterException("SAP" + PointiException.PREFIX_USER + "01",
//									"입력 데이터(Object) 오류: " + fieldName + " 데이터를 확인해 주세요.");
							continue;
						}

						Object value = JsonUtil2.getValue(bodyNode, fieldName);
						importParam.setValue(fieldName, value);
					}
				}
			}
		}

		// table data
		JCoParameterList tableParam = function.getTableParameterList();
		if (tableParam != null) {
			JCoFieldIterator tableIter = tableParam.getFieldIterator();
			if (tableIter != null) {
				while (tableIter.hasNextField() == true) {
					JCoField field = tableIter.nextField();
					String tableName = field.getName();
					// import table의 경우, "I"_ 로 시작함
					if (tableName.indexOf("I") == 0) {
						JCoTable table = field.getTable();
						table.firstRow();

						ArrayNode arrayNode = (ArrayNode) bodyNode.get(field.getName());
						if (arrayNode == null || arrayNode.equals("") == true) {
//							throw new AdapterException("SAP" + PointiException.PREFIX_USER + "02",
//									"입력 데이터(Table) 오류: " + field.getName() + " 데이터를 확인해 주세요.");
							continue;
						}

						for (JsonNode node : arrayNode) {
							table.appendRow();
							JCoFieldIterator tableFieldIter = table.getFieldIterator();

							while (tableFieldIter.hasNextField() == true) {
								JCoField tableField = tableFieldIter.nextField();
								String tableFieldName = tableField.getName();

								if (node.get(tableFieldName) == null) {
//									throw new AdapterException("SAP" + PointiException.PREFIX_USER + "03",
//											"입력 데이터(Table Field) 오류: " + tableFieldName + "데이터를 확인해 주세요.");
									continue;
								}

								Object tableFieldValue = JsonUtil2.getValue(node, tableFieldName);
								table.setValue(tableFieldName, tableFieldValue);
							}
						}
					}
				}
			}
		}
		loggingInput(function);

		return function;
	}

	@Override
	public Object mappingResponseSapDataToObject(JCoFunction function) throws AdapterException {
		Map<String, Object> resMap = new HashMap<String, Object>();

		// export data
		JCoParameterList exportParam = function.getExportParameterList();
		if (exportParam != null) {
			JCoFieldIterator exportIter = exportParam.getFieldIterator();
			if (exportIter != null) {
				while (exportIter.hasNextField() == true) {
					JCoField field = exportIter.nextField();

					if (field.getType() == JCoMetaData.TYPE_STRUCTURE) {
						Map<String, Object> objectMap = new HashMap<String, Object>();
						JCoStructure structure = field.getStructure();
						JCoFieldIterator structIter = structure.getFieldIterator();

						while (structIter.hasNextField() == true) {
							JCoField structField = structIter.nextField();
							objectMap.put(structField.getName(), structField.getValue());
						}

						resMap.put(field.getName(), objectMap);
					} else {
						resMap.put(field.getName(), field.getValue());
					}
				}
			}
		}

		// table data
		JCoParameterList tableParam = function.getTableParameterList();
		if (tableParam != null) {
			JCoFieldIterator tableIter = function.getTableParameterList().getFieldIterator();
			if (tableIter != null) {
				while (tableIter.hasNextField() == true) {
					JCoField field = tableIter.nextField();
					String tableName = field.getName();

					// export table의 경우, "E" 로 시작함
					if (tableName.indexOf("E") == 0) {
						JCoTable table = field.getTable();
						int rowCount = table.getNumRows();
						List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

						for (int i = 0; i < rowCount; i++) {
							table.setRow(i);
							Map<String, Object> rowMap = new HashMap<String, Object>();

							for (JCoField tableField : table) {
								rowMap.put(tableField.getName(), tableField.getValue());
							}

							listMap.add(rowMap);
						}

						resMap.put(field.getName(), listMap);
					}
				}
			}
		}

		return resMap;
	}

	@Override
	public void verifySapResult(JCoFunction function) throws AdapterException {
		// TODO SAP 호출 결과에 대한 검증 로직 추가
	}
}