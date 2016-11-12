package com.nemustech.common.db.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.node.ObjectNode;

/**
 * DBDao 사용을 위한 HashMap형태의 Data Type.
 * Key값으로 <code>String</code> 타입을 가진다.
 * 
 * 
 * @version 1.0.0
 * @see java.util.HashMap
 *
 */
public class DBMap extends HashMap<String, Object> {
	protected static Log log = LogFactory.getLog(DBMap.class);
	private static final long serialVersionUID = 1L;

	/**
	 * 생성자
	 */
	public DBMap() {
		this(1);
	}

	/**
	 * 생성자
	 * 
	 * @param size Map의 기본 크기
	 * @see java.util.HashMap#HashMap(int)
	 */
	public DBMap(int size) {
		super(size);
	}

	/**
	 * 생성자
	 * 
	 * @param map 초기 생성할 Map
	 * @see java.util.HashMap#HashMap(Map)
	 */
	public DBMap(Map<String, Object> map) {
		super(map);
	}

	/**
	 * 생성자
	 * 
	 * @param key 초기 생성할 key 값
	 * @param value 초기 생성할 value 객체
	 */
	public DBMap(String key, Object value) {
		super(2);
		this.put(key, value);
	}

	/**
	 * 신규 객체를 추가한 다음에 해당 Map을 돌려 준다.
	 * 
	 * @param key 추가할 key 값
	 * @param value 추가할 value 객체
	 * @return 추가한 후의 Map
	 * @see java.util.HashMap#put(Object, Object)
	 */
	public DBMap put(String key, Object value) {
		super.put(key, value);

		return this;
	}

	/**
	 * <code>boolean</code> 타입의 값을 돌려 준다.
	 * 
	 * @param key 찾을 key값
	 * @param defaultVal 기본값
	 * @return key에 해당하는 값이 "TRUE", "Y", 1 이면 true, "FALSE", "N", 0 이면 false 값을 돌려 준다. (case-insensitive)
	 */
	public boolean getBoolean(String key, boolean defaultVal) {
		log.trace("Start::getBoolean()");
		log.trace("  > key: " + key);
		log.trace("  > defaultVal: " + defaultVal);

		try {
			Object obj = super.get(key);

			if (isTypeOf(obj, Boolean.class)) {
				return ((Boolean) obj).booleanValue();
			}

			else if (isTypeOf(obj, Integer.class)) {
				if (((Integer) obj).intValue() == 0) {
					return false;
				}

				else if (((Integer) obj).intValue() == 1) {
					return true;
				}
			}

			else {
				if ("TRUE".equalsIgnoreCase(obj.toString())) {
					return true;
				}

				else if ("Y".equalsIgnoreCase(obj.toString())) {
					return true;
				}

				else if ("1".equalsIgnoreCase(obj.toString())) {
					return true;
				}

				else if ("FALSE".equalsIgnoreCase(obj.toString())) {
					return false;
				}

				else if ("N".equalsIgnoreCase(obj.toString())) {
					return false;
				}

				else if ("0".equalsIgnoreCase(obj.toString())) {
					return false;
				}
			}

			return defaultVal;
		} finally {
			log.trace("End::getBoolean()");
		}
	}

	/**
	 * <code>boolean</code> 타입의 값을 돌려 준다.
	 * 
	 * @param key 찾을 key값
	 * @return key에 해당하는 값이 "TRUE", "Y", 1 이면 true, "FALSE", "N", 0 이면 false 값을 돌려 준다. (case-insensitive) 조건에 해당하는 값이 없으면 false
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/**
	 * DBMap 객체를 parentNode에 추가한다.
	 * 
	 * @param parentNode 객체를 저장할 <code>ObjectNode</code>
	 * @throws NullPointerException parentNode가 null
	 */
	public void toJsonNode(ObjectNode parentNode) throws NullPointerException {
		log.trace("Start::toJsonNode()");
		log.trace("  > parentNode: " + parentNode);

		for (Map.Entry<String, Object> entry : this.entrySet()) {
			addJsonNode(parentNode, entry.getKey(), entry.getValue());
		}

		log.trace("End::getBoolean()");
	}

	/**
	 * DBMap 객체를 parentNode에 추가한다.
	 * key 이름은 대/소문자를 무시하고 하나의 형태로 한다.
	 * 
	 * @param parentNode 객체를 저장할 <code>ObjectNode</code>
	 * @param upperCase true 면 key 이름을 일괄적으로 대문자로 변경하며, false 면 일괄적으로 소문자로 변경
	 * @throws NullPointerException parentNode가 null
	 */
	public void toJsonNode(ObjectNode parentNode, boolean upperCase) throws NullPointerException {
		log.trace("Start::toJsonNode()");
		log.trace("  > parentNode: " + parentNode);
		log.trace("  > upperCase: " + upperCase);

		for (Map.Entry<String, Object> entry : this.entrySet()) {
			String keyName = entry.getKey();

			keyName = (upperCase) ? keyName.toUpperCase(Locale.ENGLISH) : keyName.toLowerCase(Locale.ENGLISH);

			if (parentNode.findPath(keyName).isMissingNode()) {
				addJsonNode(parentNode, keyName, entry.getValue());
			}
		}

		log.trace("End::getBoolean()");
	}

	protected void addJsonNode(ObjectNode node, String filedName, Object value) {
		if (isTypeOf(value, Boolean.class)) {
			node.put(filedName, ((Boolean) value).booleanValue());
		}

		else if (isTypeOf(value, Integer.class)) {
			node.put(filedName, ((Integer) value).intValue());
		}

		else if (isTypeOf(value, Long.class)) {
			node.put(filedName, ((Long) value).longValue());
		}

		else if (isTypeOf(value, Float.class)) {
			node.put(filedName, ((Float) value).floatValue());
		}

		else if (isTypeOf(value, Double.class)) {
			node.put(filedName, ((Double) value).doubleValue());
		}

		else if (isTypeOf(value, String.class)) {
			node.put(filedName, (String) value);
		}

		else {
			node.putPOJO(filedName, value);
		}
	}

	/**
	 * POJO로 매핑되어 있을 경우 해당 POJO를 돌려 준다.
	 * 
	 * @param clazz
	 * @return POJO
	 */
	@SuppressWarnings("unchecked")
	public <T> T getMapperedPOJO(Class<T> clazz) {
		return (T) super.get(clazz.getSimpleName());
	}

	/**
	 * object가 특정 class의 instance인지 확인한다.
	 * 
	 * @param obj 확인할 object
	 * @param clazz 확인할 class type
	 * @return instance가 맞으면 true, otherwise false
	 */
	public static <T> boolean isTypeOf(Object obj, Class<T> clazz) {
		return (obj.getClass() == clazz);
	}
}
