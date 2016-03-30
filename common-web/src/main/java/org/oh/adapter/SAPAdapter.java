package org.oh.adapter;

import org.oh.adapter.exception.AdapterException;
import org.oh.adapter.sap.ISapMapper;

/**
 * SAP 레거시 연동 Adapter
 *
 * @since 1.0.2
 */
public interface SAPAdapter {
	/**
	 * Jco function 실행
	 * 
	 * @param functionName 실행한 function 이름
	 * @param parameter input 파라미터
	 * @param mapper Jco function의 I/O 데이터를 매핑하는 매퍼 클래스 인스턴스
	 * @return Jco function 실행 결과
	 * 
	 * @throws AdapterException
	 */
	public Object execute(String functionName, Object parameter, ISapMapper mapper) throws AdapterException;
}
