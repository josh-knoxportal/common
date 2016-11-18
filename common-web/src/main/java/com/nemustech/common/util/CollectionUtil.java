package com.nemustech.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import com.nemustech.common.exception.SmartException;

/**
 * Collection 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.collections.CollectionUtils 클래스를 상속받음.
 *
 * @version 2.5
 * @since 1.0.0
 * @see <a href=http://commons.apache.org/proper/commons-collections/javadocs/api-release/org/apache/commons/collections4/CollectionUtils.html>org.apache.commons.collections.CollectionUtils</a>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class CollectionUtil extends CollectionUtils {
	/**
	 * Collection 객체의 item 개수를 리턴.
	 * 
	 * <pre>
	 * 예)
	 *      ArrayList<String> arrayStr = new ArrayList<String>();
	 *      <br>
	 *      CollectionUtil.size(arrayStr);       // 0 리턴 (item 개수 0)
	 *      
	 *      arrayStr.add("A");
	 *      arrayStr.add("B");
	 *      CollectionUtil.size(arrayStr);       // 2 리턴 (item 개수 2)
	 * </pre>
	 *
	 * @param object Collection 객체
	 * @return Collection 객체의 item 개수
	 */
	public static int size(Object object) {
		return (object == null) ? 0 : CollectionUtils.size(object);
	}

	/**
	 * 주어진 List 객체의 size를 가져온다.
	 * 
	 * <pre>
	 * 예)
	 *      List<String> list =  new ArrayList<String>();
	 *      <br>
	 *      CollectionUtil.size(list);       // 0 리턴 (item 개수 0)
	 *      
	 *      list.add("A");
	 *      list.add("B");
	 *      CollectionUtil.size(list);       // 2 리턴 (item 개수 2)
	 * </pre>
	 * 
	 * @param list List 객체
	 * @return List size
	 */
	public static int size(List list) {
		return (list == null) ? 0 : list.size();
	}

	/**
	 * 주어진 클래스를 <멤버변수, 멤버변수값>맵으로 변환환다.
	 * ( public static 으로 정의 되어 있는 멤버변수와 값을 변환 )
	 * 
	 * <pre>
	 * 예)
	 *      // [Sample Class 멤버변수]
	 *      //   public static final String staticStringData = "Sample Static Data";
	 *      //   public static final int staticIntData = 1000;
	 *      //   private static final int staticIntData2 = 1000;
	 *      
	 *      Sample data =  new Sample();  
	 *      <br>
	 *      // Sample 클래스의 멤버변수(public static)의 변수명과 변수값을 Map으로 리턴
	 *      Map<String, Object> classToMap = CollectionUtil.classToMap(data.getClass());
	 *      <br> 
	 *     LogUtil.writeLog(classToMap.toString()); // {staticIntData=1000, staticStringData=Sample Static Data}
	 * </pre>
	 *
	 * @param clazz 클래스
	 * @return <멤버변수, 멤버변수값>맵
	 */
	public static Map<String, Object> classToMap(Class clazz) {
		Map<String, Object> fieldMap = new HashMap<String, Object>();

		try {
			Field[] arrFields = clazz.getFields();
			for (int i = 0; i < arrFields.length; i++) {
				fieldMap.put(arrFields[i].getName(), arrFields[i].get(new Object()));
			}

		} catch (Exception e) {
			LogUtil.writeLog(e, CollectionUtil.class);
		}
		return fieldMap;
	}

	/**
	 * 주어진 데이타맵을 지정된 클래스의 객체로 변환한다.
	 * 
	 * <pre>
	 * 예)
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      
	 *      <br>
	 *      Map<String, Object> userClassToMap = new HashMap<String, Object>();
	 *      <br>
	 *      userClassToMap.put("name", "Kim Ga-Na");
	 *      userClassToMap.put("gender", "Man");
	 *      userClassToMap.put("age", "25");  
	 *      <br>
	 *      // Map의 데이터를 User 클래스로 변환하여 리턴
	 *      User userData = (User) CollectionUtil.mapToObject(userClassToMap, User.class);
	 *      <br> 
	 *      LogUtil.writeLog(userData.toString()); // User [address=null, age=25, gender=Man, name=Kim Ga-Na]
	 * </pre>
	 *
	 * @param map 데이타 맵
	 * @param clazz 변환될 형식의 클래스
	 * @return 변환된 인스턴스 객체
	 */
	public static <T> T mapToObject(Map<String, ?> map, Class<T> clazz) throws SmartException {
		return mapToObject(map, clazz, false);
	}

	/**
	 * 주어진 데이타맵을 지정된 클래스의 객체로 변환한다.
	 * 
	 * <pre>
	 * 예)
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      //      private String phone_num;
	 *      //      private String phoneNum;
	 *      <br>
	 *      Map<String, Object> userClassToMap = new HashMap<String, Object>();
	 *      <br>
	 *      userClassToMap.put("name", "Kim Ga-Na");
	 *      userClassToMap.put("gender", "Man");
	 *      userClassToMap.put("age", "25");
	 *      userClassToMap.put("phone_num", "01012301230");  
	 *      <br>
	 *      // Map의 데이터를 User 클래스로 변환하여 리턴 (camel 양식으로 강제 변환 적용)
	 *      User userDataTrue = (User) CollectionUtil.mapToObject(userClassToMap, User.class, true);
	 *      <br>
	 *      // Map의 데이터를 User 클래스로 변환하여 리턴 (camel 양식 적용안함)
	 *      User userDataFalse = (User) CollectionUtil.mapToObject(userClassToMap, User.class, false);
	 *      <br> 
	 *      LogUtil.writeLog(userDataTrue.toString()); // User [address=null, age=25, gender=Man, name=Kim Ga-Na, phoneNum=null, phone_num=01012301230]
	 *      LogUtil.writeLog(userDataFalse.toString()); // User [address=null, age=25, gender=Man, name=Kim Ga-Na, phoneNum=01012301230, phone_num=null]
	 * </pre>
	 *
	 * @param map 데이타맵
	 * @param clazz 변환될 형식의 클래스
	 * @param useCamelMemberName 데이타맵의 키를 객체 멤버로 변환시 멤버명을 camel 양식으로 강제할지 여부.
	 * @return 변환된 인스턴스 객체
	 */
	public static <T> T mapToObject(Map<String, ?> map, Class<T> clazz, boolean useCamelMemberName)
			throws SmartException {
		Object obj = null;

		try {
			obj = clazz.newInstance();
			for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
				String name = it.next();
				Object value = map.get(name);
				if (useCamelMemberName) {
					name = StringUtil.underscoreToCamel(name);
				}
				BeanUtils.setProperty(obj, name, value);
			}
		} catch (Exception e) {
			throw new SmartException(e);
		}

		return (T) obj;
	}

	/**
	 * 주어진 데이타 객체에서 getter 메소드가 있는 프로퍼티를 Map에 넣어 반환.
	 * 
	 * <pre>
	 * 예)
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      //      private String phone_num;  // getter 메소드 미존재
	 *      //      private String phoneNum;   // getter 메소드 존재
	 *      <br>
	 *      User user = new User();
	 *      user.setName("Kim Ga-Na");
	 *      user.setAge(27);
	 *      user.setPhone_num("010-8888-9999");
	 *      user.setPhoneNum("010-7777-5555");
	 *      <br>
	 *      Map<String, Object> userClassToMap = CollectionUtil.objectToMap(user); // phone_num 변수 제외
	 *      <br>
	 *      LogUtil.writeLog(userClassToMap.toString()); // {gender=null, age=27, class=class data.User, address=null, phoneNum=010-7777-5555, name=Kim Ga-Na}
	 * </pre>
	 *
	 * @param obj 데이타 객체
	 * @return 데이타 객체의 프로퍼티 Map
	 */
	public static Map<String, String> objectToMap(Object obj) throws SmartException {
		try {
			return BeanUtils.describe(obj);
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 데이타 객체를 Map 객체로 변환한다.
	 * 
	 * <pre>
	 * 예)
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      //      private String phone_num;  // getter 메소드 미존재
	 *      //      private String phoneNum;   // getter 메소드 존재
	 *      <br>
	 *      User user = new User();
	 *      user.setName("Kim Ga-Na");
	 *      user.setAge(27);
	 *      user.setPhone_num("010-8888-9999");
	 *      user.setPhoneNum("010-7777-5555");
	 *      <br>
	 *      Map<String, Object> userClassToMap1 = CollectionUtil.objectToMap(user, true); // phoneNum 변수를 phone_num 으로 변환
	 *      Map<String, Object> userClassToMap2 = CollectionUtil.objectToMap(user, false); // 변경없이 출력
	 *      <br>
	 *      LogUtil.writeLog(userClassToMap1.toString()); // {gender=null, age=27, phone_num=010-7777-5555, address=null, name=Kim Ga-Na}
	 *      LogUtil.writeLog(userClassToMap2.toString()); // {gender=null, age=27, address=null, phoneNum=010-7777-5555, name=Kim Ga-Na}
	 * </pre>
	 * 
	 * @param obj 데이타 객체
	 * @param useUnderscoreKey 데이타 객체 멤버를 맵의 키로 변환시 키를 underscore(_) 양식으로 강제할지 여부.
	 * @return 변환된 인스턴스 Map 객체
	 */
	public static Map<String, String> objectToMap(Object obj, boolean useUnderscoreKey) throws SmartException {
		Map<String, String> toMap = new HashMap<String, String>();

		try {
			Map<String, String> fromMap = BeanUtils.describe(obj);

			if (useUnderscoreKey) {
				for (Iterator<String> it = fromMap.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					// BeanUtils.describe() 메소드에서 "class" 라는 키=값을 자동으로 입력해서 변환될 객체에 포함시키지 않음
					if (key.equals("class") == false) {
						toMap.put(StringUtil.camelToUnderscore(key), fromMap.get(key));
					}
				}
			} else {
				toMap = objectToMap(obj);
				// BeanUtils.describe() 메소드에서 "class" 라는 키=값을 자동으로 입력해서 변환될 객체에 포함시키지 않음
				toMap.remove("class");
			}
		} catch (Exception e) {
			throw new SmartException(e);
		}

		return toMap;
	}

	/**
	 * 리스트객체와 lst객체 안의 T객체에서 Key에 해당하는 값을 가져오는 getter메쏘드명을 method String변수로 준다.
	 * 
	 * <pre>
	 * 예)
	 *      // [User Class]
	 *      //      public String name;
	 *      //      private int age; 
	 *      //      ....
	 *      //     
	 *      //       public String getName() {
	 *      //           return name;
	 *      //       }     
	 *      //      
	 *      //      public int getAge() {
	 *      //          return age;
	 *      //      }
	 *      
	 *      <br>
	 *      User user = new User();
	 *      user.setName("Kim Ga-Na");
	 *      user.setAge(27);
	 *      user.setPhone_num("010-8888-9999");
	 *      user.setPhoneNum("010-7777-5555");
	 *      <br>
	 *      User user1 = new User();
	 *      user1.setName("Kim Ma-Ba");
	 *      user1.setAge(30);
	 *      user1.setPhone_num("010-7890-0987");
	 *      user1.setPhoneNum("010-8520-0258");
	 *      <br>
	 *      User user2 = new User();
	 *      user2.setName("Kim Ah-Ja");
	 *      user2.setAge(56);
	 *      user2.setPhone_num("010-9630-0369");
	 *      user2.setPhoneNum("010-7410-0147");
	 *      <br>
	 *      List<User> userList = new ArrayList<User>();
	 *      userList.add(user);
	 *      userList.add(user1);
	 *      userList.add(user2);
	 *      
	 *      <br>
	 *      // "getName" 메소드가 Key, "getAge" 메소드가 value
	 *      HashMap<String, Object> listToMap = CollectionUtil.listToMap(userList, "getName", "getAge");
	 *      <br>
	 *      LogUtil.writeLog(listToMap.toString()); //{Kim Ga-Na=27, Kim Ma-Ba=30, Kim Ah-Ja=56}
	 * </pre>
	 * 
	 * @param fromList T 객체 리스트
	 * @param keyMethodName key가 될 메소드 이름
	 * @param valueMethodName value가 될 메소드 이름
	 * @return 변환된 인스턴스 Map 객체
	 */
	public static <T> HashMap<String, Object> listToMap(List<T> fromList, String keyMethodName, String valueMethodName)
			throws SmartException {
		// 리턴할 해쉬맵 생성
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			for (T t : fromList) {
				Object key = CommonUtil.invokeMothod(t, keyMethodName, null);
				Object value = CommonUtil.invokeMothod(t, valueMethodName, null);
				resultMap.put((String) key, value);
			}
		} catch (Exception e) {
			throw new SmartException(e);
		}
		return resultMap;
	}

	/**
	 * 주어진 컬렉션을 지정된 빈프로퍼티 값을 Key로 갖는 맵으로 변환.
	 * 
	 * <pre>
	 * 예)
	 *      // [User Class]
	 *      //      public String name;
	 *      //      private int age; 
	 *      //      ....
	 *      //     
	 *      
	 *      <br>
	 *      User user = new User();
	 *      user.setName("Kim Ga-Na");
	 *      user.setAge(27);
	 *      user.setPhone_num("010-8888-9999");
	 *      user.setPhoneNum("010-7777-5555");
	 *      <br>
	 *      User user1 = new User();
	 *      user1.setName("Kim Ma-Ba");
	 *      user1.setAge(30);
	 *      user1.setPhone_num("010-7890-0987");
	 *      user1.setPhoneNum("010-8520-0258");
	 *      <br>
	 *      List<User> userList = new ArrayList<User>();
	 *      userList.add(user);
	 *      userList.add(user1);
	 *      <br>
	 *      // "name" 변수값이 Key가 됨
	 *      Map<String, User>   listToMap = CollectionUtil.listToMap(userList, "name");
	 *      <br>
	 *      //  {
	 *      //    Kim Ga-Na=User [address=null, age=27, gender=null, name=Kim Ga-Na, phoneNum=010-7777-5555, phone_num=010-8888-9999], 
	 *      //    Kim Ma-Ba=User [address=null, age=30, gender=null, name=Kim Ma-Ba, phoneNum=010-8520-0258, phone_num=010-7890-0987]
	 *      //  }
	 *      LogUtil.writeLog(listToMap.toString());
	 * </pre>
	 *
	 * @param fromCollection <T>컬렉션
	 * @param beanPropertyName 빈프로퍼티명
	 * @return <빈프로퍼티값, <T>> 맵
	 * @throws SmartException
	 */
	public static <T> Map<String, T> listToMap(Collection<T> fromCollection, String beanPropertyName)
			throws SmartException {
		Map<String, T> resultMap = new HashMap<String, T>();
		for (T t : fromCollection) {
			try {
				resultMap.put(BeanUtils.getSimpleProperty(t, beanPropertyName), t);
			} catch (Exception e) {
				throw new SmartException(e);
			}
		}
		return resultMap;
	}

	/**
	 * 쿼리시 SUM()을 사용할 경우 적용
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<T> getSumList(List<T> list) {
		if (list.size() > 0 && list.get(0) == null)
			return new ArrayList<T>();

		return list;
	}
}
