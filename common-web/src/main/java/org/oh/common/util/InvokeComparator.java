package org.oh.common.util;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.oh.common.exception.SmartException;

/**
 * 메소드 실행결과를 비교해주는 클래스.
 * 
 * @version 2.5
 * @since 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class InvokeComparator implements Comparator {
	/** 메소드 배열 */
	private Method[] methodArray;

	/** 메소드수. */
	private int methodCount = 0;

	/**
	 * 생성자.
	 *
	 * @param invokeClass invoke 대상 클래스
	 * @param invokeMethodNames ,로 구분된 invoke 대상 메소드명 목록 연결문자열
	 */
	public InvokeComparator(Class invokeClass, String invokeMethodNames) throws SmartException {
		try {
			String[] invokeMethodNameArray = invokeMethodNames.split(",");
			int arraySize = (invokeMethodNameArray == null) ? 0 : invokeMethodNameArray.length;
			if (arraySize > 0) {
				this.methodArray = new Method[arraySize];
				String methodName = null;

				for (int i = 0; i < arraySize; i++) {
					methodName = StringUtil.trimToNull(invokeMethodNameArray[i]);
					this.methodArray[i] = (methodName == null) ? null : invokeClass.getMethod(methodName, (Class) null);
				}
			}

			int j = 0;
			for (; j < arraySize; j++) {
				if (this.methodArray[j] == null) {
					break;
				}
			}
			this.methodCount = j;
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * invoke 메소드에 파라메터 o1을 전달해서 실행한 결과와 o2를 전달해서 실행한 결과를 비교하여 리턴.<br/>
	 * - Comparator 인터페이스의 하나
	 *
	 * @param o1 메소드 실행 파라메터 1
	 * @param o2 메소드 실행 파라메터 2
	 * @return 메소드 실행 결과 비교값(string결과 비교)
	 * @throws ClassCastException - method result string cast 할때 발생할수 있음.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		int compareValue = 0;

		try {
			for (int i = 0; i < this.methodCount; i++) {
				String value1 = methodArray[i].invoke(o1, (Object) null).toString();
				String value2 = methodArray[i].invoke(o2, (Object) null).toString();
				compareValue = value1.compareTo(value2);

				if (compareValue != 0) {
					return compareValue;
				}
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, getClass());
		}

		return compareValue;
	}
}
