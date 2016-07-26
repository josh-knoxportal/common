package org.oh.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.oh.common.exception.CommonException;
import org.springframework.util.ReflectionUtils;

/**
 * ReflectionUtils 클래스 확장
 */
public abstract class ReflectionUtil extends ReflectionUtils {
	public static <T> T newInstance(Class<T> target, Object[] initargs) throws CommonException {
		return newInstance(target, convertArray(initargs), initargs);
	}

	public static Class<Object>[] convertArray(Object[] arrObj) {
		Class<Object>[] arrClass = new Class[0];
		if (arrObj == null)
			return arrClass;

		arrClass = new Class[arrObj.length];
		for (int i = 0; i < arrObj.length; i++)
			arrClass[i] = (arrObj[i] == null) ? null : (Class<Object>) arrObj[i].getClass();

		return arrClass;
	}

	public static <T> T newInstance(Class<T> target, Class<Object>[] parameterTypes, Object[] initargs)
			throws CommonException {
		try {
			// Declare the constructor.
			Constructor<T> csrt = target.getConstructor(parameterTypes);

			// Invoke the constructor.
			return csrt.newInstance(initargs);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Instantiate class \"" + target.getName() + "\" error", e.getMessage()), t);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Instantiate class \"" + target.getName() + "\" error", e.getMessage()), e);
		}
	}

	/**
	 * 객체 생성
	 */
	public static Object invoke(String className, String methodName, Object[] args) throws CommonException {
		Object target = null;
		try {
			target = Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Instantiate class \"" + className + "\" error", e.getMessage()), e);
		}

		return invoke(target, methodName, args);
	}

	public static Object invoke(Object target, String methodName, Object[] args) throws CommonException {
		return invoke(target, methodName, convertArray(args), args);
	}

	/**
	 * 메소드 실행
	 */
	public static Object invoke(Object target, String methodName, Class<Object>[] parameterTypes, Object[] args)
			throws CommonException {
		try {
			// Declare the method.
			Method method = target.getClass().getMethod(methodName, parameterTypes);

			// Invoke the method.
			return method.invoke(target, args);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Invoke method \"" + target + "." + methodName + "\" error", e.getMessage()),
					t);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Invoke method \"" + target + "." + methodName + "\" error", e.getMessage()),
					e);
		}
	}

	public static Map<String, Field> getFields(Object target) {
		if (!Utils.isValidate(target))
			return null;

		FieldCallbackImpl fieldCallback = new FieldCallbackImpl();
		doWithFields(target.getClass(), fieldCallback);

		return fieldCallback.getFieldMap();
	}

	/**
	 * 객체 필드 조회(이름 조건)
	 */
	public static Object getValue(Object target, String fieldName) throws CommonException {
		Map<String, Field> fieldMap = getFields(target);
		Field field = fieldMap.get(fieldName);

		return getValue(target, field);
	}

	/**
	 * 객체 필드 조회(타입 조건)
	 */
	public static <T> T getValue(Object target, Class<T> fieldType) throws CommonException {
		Map<String, Field> fieldMap = getFields(target);

		Field field = null;
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			if (entry == null || entry.getValue() == null)
				continue;

//			Object obj = getValue(target, entry.getValue());
//			if (fieldType.isInstance(obj)) {
			if (entry.getValue().getType().equals(fieldType)) {
				field = entry.getValue();
			}
		}

		if (field == null)
			return null;

		return (T) getValue(target, field);
	}

	/**
	 * 객체 내부에 필드가 있으면 객체를, 없으면 값을 반환한다.
	 * (필드가 없는 모델 객체에 toJsonNode() 메소드 호출시 문제점 해결)
	 */
	public static <T> T getValue(T target, T value) {
		Map<String, Field> fieldMap = getFields(target);
		if (fieldMap.size() > 0)
			return target;
		else
			return value;
	}

	public static Object getValue(Object target, Field field) throws CommonException {
		try {
			field.setAccessible(true);

			return getField(field, target);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(
					"Get field value \"" + target + "." + ((field == null) ? "null" : field.getName()) + "\" error",
					e.getMessage()), e);
		}
	}

	public static void setValue(Object target, String fieldName, Object value) throws CommonException {
		Map<String, Field> fieldMap = getFields(target);
		Field field = fieldMap.get(fieldName);

		setValue(target, field, value);
	}

	/**
	 * 객체 필드 저장
	 */
	public static void setValue(Object target, Field field, Object value) throws CommonException {
		field.setAccessible(true);

		try {
			field.set(target, value);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage(
							"Set field value \"" + value + "\" => \"" + target + "." + field.getName() + "\" error",
							e.getMessage()),
					e);
		}
	}

	public static boolean existField(Object target, String fieldName) throws CommonException {
		Field field = findField(target.getClass(), fieldName);

		return (field != null);
	}

	/**
	 * Object -> Map<String, Object> 변환
	 */
	public static Map<String, Object> convertObjectToMap(Object obj) {
		return convertMapToMap(obj, getFields(obj));
	}

	/**
	 * Map<String, Field> -> Map<String, Object> 변환
	 */
	public static Map<String, Object> convertMapToMap(Object target, Map<String, Field> map) {
		Map<String, Object> map2 = new HashMap<String, Object>();
		if (map != null) {
			for (Map.Entry<String, Field> entry : map.entrySet()) {
				if (entry == null || entry.getValue() == null)
					continue;

				map2.put(entry.getKey(), getValue(target, entry.getValue()));
			}
		}

		return map2;
	}

	/**
	 * 객체를 문자열로 변환한다.
	 * 
	 * @param object
	 * @param excludeFieldNamesParam
	 * 
	 * @return
	 */
	public static String toString(Object object, String... excludeFieldNamesParam) {
		String str = new ReflectionToStringBuilder(object, new JsonRecursiveToStringStyle())
				.setExcludeFieldNames(excludeFieldNamesParam).toString();
		str = StringUtil.replace(str, "{[", "[");
		str = StringUtil.replace(str, "]}", "]");

		return str;
	}

	// Inner class ------------------------------------------------------------

	/**
	 * 필드 콜백
	 */
	protected static class FieldCallbackImpl implements FieldCallback {
		protected Map<String, Field> fieldMap = new HashMap<String, Field>();

		@Override
		public void doWith(Field field) {
			fieldMap.put(field.getName(), field);
		}

		protected Map<String, Field> getFieldMap() {
			return fieldMap;
		}
	}

	// Test -------------------------------------------------------------------

	protected static class Person {
		String name;
		int age = 2;
		boolean smoker = false;
		Job job = new Job();
	}

	protected static class Job {
		String title = "3";
		Job2 job = new Job2();
	}

	protected static class Job2 {
		String title = "4";
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(toString(new Person()));
//		System.out.println(toString(new Person[] { new Person(), new Person() }));
//
//		Common common = new Common();
//		common.setCondition2(new Condition());
//		System.out.println(toString(common, "condition2"));

		System.out.println(ReflectionUtil.getValue(new Person(), "name1"));
	}
}