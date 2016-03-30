package org.oh.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.oh.common.exception.SmartException;

/**
 * 기타 공통 유틸리티 클래스.
 * 
 * @version 2.5
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public abstract class CommonUtil {
	/**
	 * Unique ID를 생성한다.
	 * 
	 * <pre>
	 * 예)
	 * CommonUtil.generateUniqueID() // =&gt; &quot;25c43396-c4f2-4cb1-8c74-78395b738443&quot; (Unique ID 생성)
	 * </pre>
	 *
	 * @return Unique ID
	 */
	final public static String generateUniqueID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 주어진 정수 객체를 정수로 변환한다. 정수변환 실패시 지정된 기본값을 반환한다.
	 * 
	 * <pre>
	 * 예)
	 *   int returnNumber = 0;
	 *   <br>
	 *   String strNumber = "120";
	 *   returnNumber = CommonUtil.parseInt(strNumber, 100); // 120
	 *   <br>
	 *   Integer intNumber = 120;
	 *   returnNumber = CommonUtil.parseInt(intNumber, 100); // 120
	 *   <br>
	 *   BigDecimal bigDecNumber = new BigDecimal("120");
	 *   returnNumber = CommonUtil.parseInt(bigDecNumber, 100); // 120
	 *   <br>
	 *   BigInteger bigIntNumber = new BigInteger("120");
	 *   returnNumber = CommonUtil.parseInt(bigIntNumber, 100); // 120
	 *   <br>
	 *   returnNumber = CommonUtil.parseInt(null, 100); // 100 (변환실패-기본값 리턴)
	 * </pre>
	 *
	 * @param intObject 정수 객체
	 * @param defaultValue 정수변환 실패시 반환값
	 * @return 변환된 정수
	 */
	final public static int parseInt(Object intObject, int defaultValue) {
		try {
			if (intObject instanceof String) {
				return Integer.parseInt((String) intObject);
			} else if (intObject instanceof Integer) {
				return ((Integer) intObject).intValue();
			} else if (intObject instanceof BigDecimal) {
				return ((BigDecimal) intObject).intValue();
			} else if (intObject instanceof BigInteger) {
				return ((BigInteger) intObject).intValue();
			}
		} catch (Exception e) {
		}

		return defaultValue;
	}

	/**
	 * 주어진 정수 객체를 Integer 객체로 변환한다. Integer 변환 실패시 지정된 기본값을 반환한다.
	 *
	 * <pre>
	 * 예)
	 *   Integer returnNumber = 0;
	 *   <br>
	 *   String strNumber = "120";
	 *   returnNumber = CommonUtil.parseInteger(strNumber, 100); // 120
	 *   <br>
	 *   int intNumber = 120;
	 *   returnNumber = CommonUtil.parseInteger(intNumber, 100); // 120
	 *   <br>
	 *   BigDecimal bigDecNumber = new BigDecimal("120");
	 *   returnNumber = CommonUtil.parseInteger(bigDecNumber, 100); // 120
	 *   <br>
	 *   BigInteger bigIntNumber = new BigInteger("120");
	 *   returnNumber = CommonUtil.pparseInteger(bigIntNumber, 100); // 120
	 *   <br>
	 *   returnNumber = CommonUtil.parseInteger(null, 100); // 100 (변환실패-기본값 리턴)
	 * </pre>
	 *
	 * @param intObject 정수 객체
	 * @param defaultValue 정수변환 실패시 반환값
	 * @return 변환된 Integer
	 */
	final public static Integer parseInteger(Object intObject, int defaultValue) {
		try {
			if (intObject instanceof String) {
				return new Integer((String) intObject);
			} else if (intObject instanceof Integer) {
				return (Integer) intObject;
			} else if (intObject instanceof BigDecimal) {
				return new Integer(((BigDecimal) intObject).intValue());
			} else if (intObject instanceof BigInteger) {
				return new Integer(((BigInteger) intObject).intValue());
			}
		} catch (Exception e) {
		}

		return new Integer(defaultValue);
	}

	/**
	 * 원본 객체에서 복사 객체에 동일한 이름의 프로퍼티를 복사.
	 * 
	 * <pre>
	 * 예)
	 *     [class define] 
	 *     User 클래스 =>  private String name;
	 *                    private String gender;
	 *                    private int age;
	 *                    private String address;
	 *                    
	 *     Student 클래스 =>  private String name;
	 *                      private int age;
	 *                      private String student_class;
	 *                      private String reg_date;   
	 *    <br><br>                                 
	 *    << example code>>        
	 *    User       user      = new User("Hong Gil-Dong", "Man", 25, "Seoul");
	 *    User       user2     = new User();   
	 *    Student    student   = new Student();
	 *    <br>
	 *    // => user의 내용을 user2로 복사
	 *    CommonUtil.copyObject(user, user2);
	 *    <br>
	 *    // => user의 내용 중 동일 필드(name,age)의 값이 student 필드(name,age)로 복사   
	 *    CommonUtil.copyObject(user, student);
	 * </pre>
	 *
	 * @param fromObject 원본 객체
	 * @param toObject 복사 객체
	 * @throws SmartException
	 */
	final public static void copyObject(Object fromObject, Object toObject) throws SmartException {
		try {
			BeanUtils.copyProperties(toObject, fromObject);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 bean 객체에 대해 지정된 프로퍼티값을 가져온다.
	 *
	 * @param bean bean 객체
	 * @param propertyName bean의 프로퍼티명
	 * @return bean 객체의 지정된 프로퍼티 값
	 * @throws SmartException
	 */
	public static String getBeanProperty(Object bean, String propertyName) throws SmartException {
		try {
			return BeanUtils.getProperty(bean, propertyName);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 bean 객체에 대해 지정된 프로퍼티에 지정된 프로퍼티값으로 설정한다.
	 *
	 * @param bean bean 객체
	 * @param propertyName bean의 프로퍼티명
	 * @param propertyValue 프로퍼티 값
	 * @throws SmartException
	 */
	public static void setBeanProperty(Object bean, String propertyName, Object propertyValue) throws SmartException {
		try {
			BeanUtils.setProperty(bean, propertyName, propertyValue);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 클래스경로에서 패키지 경로를 제거한 순수 클래스명을 가져온다
	 *
	 * <pre>
	 * 예)
	 * CommonUtil.getShortClassName(this.getClass()); // =&gt; &quot;CommonUtil&quot;
	 * </pre>
	 *
	 * @param classPath 클래스명을 포함한 클래스경로
	 * @return 패키지 경로가 제거된 클래스명
	 */
	public static String getShortClassName(String classPath) {
		return ClassUtils.getShortClassName(classPath);
	}

	/**
	 * 주어진 클래스 객체에서 패키지 경로를 제거한 순수 클래스명을 가져온다.
	 *
	 * <pre>
	 * 예)
	 * CommonUtil.getShortClassName(&quot;org.oh.test.TestAdapter&quot;); // =&gt; &quot;TestAdapter&quot;
	 * </pre>
	 *
	 * @param clazz 클래스 객체
	 * @return 패키지 경로가 제거된 클래스명
	 */
	public static String getShortClassName(Class clazz) {
		return ClassUtils.getShortClassName(clazz.getName());
	}

	/**
	 * 주어진 객체에 대해 지정된 메소드를 invoke 실행한다.
	 * 
	 * <pre>
	 * 예)
	 *    User       user      = new User("Hong Gil-Dong", "Man", 25, "Seoul");
	 *    User       user2     = new User(); 
	 *    <br>
	 *    CommonUtil.invokeMothod(user, "getName", null); // => getName 메소드 호출 "Hong Gil-Dong" 리턴
	 *    <br>
	 *    String[]   pram = {"Lee Soon-Sin"};
	 *    CommonUtil.invokeMothod(user2, "setName", pram); // setName 메소스 호출 리턴 null 
	 *    LogUtil.writeLog(user2.getName());            // invoke 수행된 값 리턴 ("Lee Soon-Sin")
	 * </pre>
	 *
	 * @param invokeObject invoke 대상 객체
	 * @param methodName invoke 대상 메소드명
	 * @param paramArray invoke 대상 메소드에 넘겨줄 파라미터 배열
	 * @return invoke 실행 반환값
	 * @throws SmartException
	 */
	public static Object invokeMothod(Object invokeObject, String methodName, Object[] paramArray)
			throws SmartException {
		try {
			Class[] paramClassArray = null;

			if (paramArray != null) {
				int paramLength = paramArray.length;

				paramClassArray = new Class[paramLength];
				for (int i = 0; i < paramLength; i++) {
					paramClassArray[i] = paramArray[i].getClass();
				}
			}

			return invokeObject.getClass().getMethod(methodName, paramClassArray).invoke(invokeObject, paramArray);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}
}
