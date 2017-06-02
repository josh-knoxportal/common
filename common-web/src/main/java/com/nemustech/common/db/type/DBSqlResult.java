package com.nemustech.common.db.type;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SQL을 수행한 후 ResultSet 과 Out parameter 등을 가지고 있는 객체. <br />
 * {@link com.nemustech.common.db.dao.DBDao}의 구현 class의 구현 방법에 따라 다르게 동작할 수도 있다.
 * 
 * 
 * @version 1.0.0
 *
 */
public class DBSqlResult {
	private static final Log log = LogFactory.getLog(DBSqlResult.class);

	private LinkedHashMap<String, Object> resultMap = null;
	private int rowCount = 0;
	/**
	 * ResultSet의 prefix 이름 - {@value}
	 */
	public static final String _RESULTSET_PREFIX = "#resultset_list_";

	/**
	 * 생성자. Map의 data를 바탕으로 생성한다.
	 * 
	 * @param map data가 있는 Map
	 */
	public DBSqlResult(Map<String, Object> map) {
		this(map, 0);
	}

	/**
	 * 생성자. Map의 data를 바탕으로 생성한다.
	 * 
	 * @param map data가 있는 Map
	 * @param rowCount SQL에 영향받은 Row 개수
	 */
	public DBSqlResult(Map<String, Object> map, int rowCount) {
		this.resultMap = new LinkedHashMap<String, Object>(map);
		this.rowCount = rowCount;
	}

	/**
	 * 생성자
	 */
	public DBSqlResult() {
		this(new LinkedHashMap<String, Object>(1), 0);
	}

	/**
	 * 생성자
	 * 
	 * @param rows SQL에 영향받은 Row 개수
	 */
	public DBSqlResult(int rows) {
		this(new LinkedHashMap<String, Object>(1), rows);
	}

	/**
	 * SQL에 영향받은 Row 개수를 돌려 준다.
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * SQL에 영향받은 Row 개수를 설정한다.
	 * 
	 * @param rowCount 설정할 개수
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * ResultSet에서 key에 해당하는 값을 돌려 준다.
	 * 단일 ResultSet일 경우에 유효하다. (DBMapList가 아닌 DBMap일 경우)
	 * 
	 * @param key 값을 찾을 key
	 * @return value 객체
	 */
	public Object get(String key) {
		return resultMap.get(key);
	}

	/**
	 * 현재와 동일한 <code>DBSqlResult</code> 객체를 돌려 준다.
	 * 
	 * @return 새로 생성한 객체
	 */
	public DBSqlResult getResult() {
		return new DBSqlResult(resultMap, rowCount);
	}

	/**
	 * 첫번째 ResultSet을 <code>DBMapList</code>로 돌려 준다. <code>getList(0)</code>와 동일한다.
	 * 
	 * @return 첫번째 ResultSet. 첫번째 객체가 <code>DBMapList</code>가 아닐 경우 Runtime Exception이 발생할 수 있다.
	 */
	public DBMapList getList() {
		return getList(0);
	}

	/**
	 * ResultSet을 <code>DBMapList</code>로 돌려 준다.
	 * 
	 * @param index 해당 객체의 위치(index)
	 * @return ResultSet. 객체가 <code>DBMapList</code>가 아닐 경우 Runtime Exception이 발생할 수 있다.
	 */
	public DBMapList getList(int index) {
		return get(index, DBMapList.class);
	}

	/**
	 * 첫번째 ResultSet을 <code>List&lt;E&gt;</code>로 돌려 준다. <code>get(0, List.class)</code>와 동일하다.
	 * 
	 * @param <T> 객체의 타입
	 * @param clazz 객체의 Class
	 * @return 첫번째 ResultSet. 첫번째 객체가 <code>List</code>가 아닐 경우 Runtime Exception이 발생할 수 있다.
	 */
	public <T> List<T> getList(Class<T> clazz) {
		return getList(0, clazz);
	}

	/**
	 * ResultSet을 <code>List&lt;E&gt;</code>로 돌려 준다. <code>get(index, List.class)</code>와 동일하다.
	 * 
	 * @param <T> 객체의 타입
	 * @param index 해당 객체의 위치(index)
	 * @param clazz 객체의 Class
	 * @return ResultSet. 객체가 <code>List</code>가 아닐 경우 Runtime Exception이 발생할 수 있다.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(int index, Class<T> clazz) {
		return get(index, List.class);
	}

	/**
	 * 첫번째 ResultSet을 <code>DBMap</code>로 돌려 준다. <code>get(0)</code>와 동일하다.
	 * 
	 * @return 첫번째 ResultSet
	 */
	public DBMap get() {
		return get(0);
	}

	/**
	 * 첫번째 ResultSet을 객체로 돌려 준다.
	 * 
	 * @param <T> 객체의 타입
	 * @param clazz 객체의 Class
	 * @return 첫번째 ResultSet
	 */
	public <T> T get(Class<T> clazz) {
		return get(0, clazz);
	}

	/**
	 * ResultSet을 <code>DBMap</code>객체로 돌려 준다.
	 * 
	 * @param index 해당 객체의 위치(index)
	 * @return 첫번째 ResultSet
	 */
	public DBMap get(int index) {
		return get(index, DBMap.class);
	}

	/**
	 * ResultSet을 돌려 준다.
	 * 
	 * @param <T> 객체의 타입
	 * @param index 해당 ResultSet의 위치(index)
	 * @param clazz 객체의 Class
	 * @return 해당 ResultSet이 <code>List</code>이면 <code>List&lt;E&gt;</code>형태로, 그 외에는 해당 ResultSet의 첫번째 객체를 요청한 타입으로 돌려 준다.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T get(int index, Class<T> clazz) {
		log.trace("Start::get()");
		log.trace("  > index: " + index);
		log.trace("  > clazz: " + clazz);

		T obj;

		if (List.class == clazz || DBMapList.class == clazz) {
			obj = (T) resultMap.get(String.format("%s%d", _RESULTSET_PREFIX, index + 1));
		}

		else {
			List list = (List) resultMap.get(String.format("%s%d", _RESULTSET_PREFIX, index + 1));

			if (list != null && list.size() > 0) {
				obj = (T) list.get(0);
			}

			else {
				obj = null;
			}
		}

		log.trace("  > RV(<T> T): " + obj);
		log.trace("End::get()");

		return obj;
	}

	/**
	 * Out 파라미터 값을 돌려 준다. <code>getOutParamerterValue(parameterName, Object.class)</code>와 동일하다.
	 * 
	 * @param parameterName 파라미터 이름
	 * @return 파라미터 객체
	 */
	public Object getOutParamerterValue(String parameterName) {
		return getOutParamerterValue(parameterName, Object.class);
	}

	/**
	 * Out 파라미터 값을 돌려 준다.
	 * 
	 * @param parameterName 파라미터 이름
	 * @param clazz 파라미터의 클래스
	 * @return 파라미터 객체. parameterName에 대한 값이 없을 경우에는 null을 돌려 준다.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getOutParamerterValue(String parameterName, Class<T> clazz) {
		log.trace("Start::getOutParamerterValue()");
		log.trace("  > parameterName: " + parameterName);
		log.trace("  > clazz: " + clazz);

		T obj;
		String key = String.format("%s%d", _RESULTSET_PREFIX, 0);

		if (resultMap.isEmpty() || !resultMap.containsKey(key)) {
			obj = null;
		}

		else {
			DBMap map = (DBMap) resultMap.get(key);

			if (map.containsKey(parameterName)) {
				obj = (T) map.get(parameterName);
			}

			else {
				obj = null;
			}
		}

		log.trace("  > RV(<T> T): " + obj);
		log.trace("End::getOutParamerterValue()");

		return obj;
	}

	/**
	 * 내부 Map의 size를 돌려 준다.
	 * 
	 * @return 해당 size - 1 이 ResultSet의 개수가 된다.
	 */
	public int size() {
		return resultMap.size();
	}
}
