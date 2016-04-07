package org.oh.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.NameValuePair;
import org.oh.common.exception.CommonException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;

/**
 * JSON 관련 유틸리티 클래스 <br />
 * 기본적인 JSON Serialization/Deserialization 관련 사항은 {@link #getObjectMapper()}를 참고한다. <br />
 * 
 * <pre>
 * 예)
 * ObjectNode rootNode = JsonUtil2.objectNode();
 * ObjectNode headerNode = rootNode.putObject("header");
 * JsonUtil2.putValue(headerNode, "trcode", "TR0001");
 * JsonUtil2.putValue(headerNode, "result", true);
 * 
 * boolean result = (Boolean) JsonUtil2.getValue(headerNode, "result);
 * String trcode = JsonUtil2.find(rootNode, "header/trcode").getTextValue();
 * 
 * <br />
 * URL url = this.getClass().getResource("/JsonUtilSample.json");
 * File file = new File(url.getPath());
 * FileInputStream fin = new FileInputStream(file);
 * rootNode = JsonUtil2.getObjectMapper().readTree(fin);
 * </pre>
 * 
 * 
 * @version 2.5
 * @since 1.0
 */
public abstract class JsonUtil2 {
	protected static ObjectMapper objectMapper = null;

	/**
	 * {@link com.fasterxml.jackson.map.ObjectMapper} 인스턴스를 돌려 준다. <BR />
	 * 해당 인스턴스는 다음과 같은 Serialize/Deserialize 기능을 설정해 두었다. <br/>
	 * <UL>
	 * <LI>FAIL_ON_UNKNOWN_PROPERTIES : false</LI>
	 * <LI>WRITE_NULL_MAP_VALUES : false</LI>
	 * <LI>INDENT_OUTPUT : true</LI>
	 * </UL>
	 * 
	 * @return {@link com.fasterxml.jackson.map.ObjectMapper} 인스턴스
	 */
	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();

//			objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false); // v1.6.x 이상
//			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true); // v1.6.x 이상

			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // v1.6.x 이상
		}

		return objectMapper;
	}

	/**
	 * {@link com.fasterxml.jackson.node.JsonNodeFactory} 인스턴스를 돌려 준다. <BR />
	 * 해당 인스턴스로 ObjectNode와 같은 JsonNode를 만들 수 있다.
	 * 
	 * @return {@link com.fasterxml.jackson.node.JsonNodeFactory} 인스턴스
	 */
	public static JsonNodeFactory getNodeFactory() {
		return getObjectMapper().getNodeFactory();
	}

	/**
	 * Same as JsonNodeFactory._instance.objectNode();
	 * 
	 * @return {@link com.fasterxml.jackson.node.ObjectNode}
	 */
	public static ObjectNode objectNode() {
		return getNodeFactory().objectNode();
	}

	/**
	 * 주어진 맵을 json 문자열로 변환한다.
	 *
	 * <pre>
	 * 예)
	 *    Map<String, Object> userClassToMap = new HashMap<String, Object>();
	 * 
	 *    userClassToMap.put("name", "Kim Ga-Na");
	 *    userClassToMap.put("gender", "Man");
	 *    userClassToMap.put("age", "25");
	 *    userClassToMap.put("phone_num", "01012301230");
	 *    <br>
	 *    String json = JsonUtil2.toString(userClassToMap); // Json 변환
	 *    LogUtil.writeLog(json.toString()); // {"gender":"Man","age":"25","phone_num":"01012301230","name":"Kim Ga-Na"}
	 * </pre>
	 *
	 * @param map 맵
	 * @return json 문자열
	 */
	public static String toString(Map<String, Object> map) {
		StringWriter out = new StringWriter();
		try {
			getObjectMapper().writeValue(out, map);

			return out.toString();
		} catch (Exception e) {
			LogUtil.writeLog(e, JsonUtil2.class);
		} finally {
			HTTPUtil.closeQuietly(out);
		}

		return "{}";
	}

	public static String prettyPrint(String json) {
		return prettyPrint(readValue(json));
	}

	public static String prettyPrint(Object json) {
		return toString(json, true);
	}

	/**
	 * POJO를 Json 형식의 문자열로 바꾼다.(Serialization)
	 *
	 * 주어진 객체를 json 문자열로 변환한다.
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
	 *      User user = new User();
	 *      <br>
	 *      user.setName("Kim Ga-Na");
	 *      user.setAge(27);
	 *      user.setPhone_num("010-8888-9999");
	 *      user.setPhoneNum("010-7777-5555");
	 *      <br>
	 *      String json = JsonUtil2.toString(user); // User 객체를 Json으로 변환
	 *      <br>
	 *      LogUtil.writeLog(json.toString()); // {"gender":null,"age":27,"phoneNum":"010-7777-5555","address":null,"name":"Kim Ga-Na"}
	 * </pre>
	 * 
	 * @param pojo 객체
	 * @return Json 형식의 문자열. 변환에 실패하면, 빈 문자열("")을 반환한다.
	 */
	public static String toString(Object pojo) {
		return toString(pojo, false);
	}

	/**
	 * POJO를 Json 형식의 문자열로 바꾼다.(Serialization)
	 * 
	 * @param pojo 객체
	 * @param prettyPrint 문자열 formatting 유무. <code>true</code>이면 보기 좋게 들여 쓰기가 된 Json 형식의 문자열로 바꾼다.
	 * @return Json 형식의 문자열. 변환에 실패하면, 빈 문자열("")을 반환한다.
	 */
	public static String toString(Object pojo, boolean prettyPrint) {
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator jg = getObjectMapper().getJsonFactory().createJsonGenerator(sw);

			if (prettyPrint) {
				jg.useDefaultPrettyPrinter();
			}

			getObjectMapper().writeValue(jg, pojo);
		} catch (Exception e) {
			return "{}";
		} finally {
			HTTPUtil.closeQuietly(sw);
		}

		return sw.toString();
	}

	/**
	 * 주어진 json 문자열을 지정된 클래스 객체로 변환한다. <br>
	 * <br>
	 *
	 * <pre>
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      //      private String phone_num;
	 *      //      private String phoneNum;
	 *      <br>
	 *      String jsonStr = "{\"gender\":\"Man\",\"age\":27,\"phoneNum\":\"010-7777-5555\",\"address\":\"\",\"name\":\"Kim Ga-Na\"}";
	 *      <br>
	 *      User user = JsonUtil2.toObject(jsonStr, User.class); // Json String을 User 객체로 변환
	 *      <br>
	 *      LogUtil.writeLog(user.toString()); // User [address=, age=27, gender=Man, name=Kim Ga-Na, phoneNum=010-7777-5555, phone_num=null]
	 * </pre>
	 *
	 * @param json json 문자열
	 * @param clazz 변환될 클래스
	 * @return 클래스 객체
	 * 
	 * @deprecated Use {@link #readValue} instead
	 */
	@Deprecated
	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return getObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			LogUtil.writeLog(e, JsonUtil2.class);
		}

		return null;
	}

	/**
	 * 주어진 JsonNode를 지정한 클래스 객체로 변환한다.
	 *
	 * <br>
	 * <br>
	 *
	 * <pre>
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      //      private String phone_num;
	 *      //      private String phoneNum;
	 *      <br>
	 *      ObjectNode  reqNode = new ObjectNode(JsonNodeFactory.instance);
	 *      reqNode.put("name", "Kim Ga-Na");
	 *      reqNode.put("gender", "Man");
	 *      reqNode.put("age", "25");
	 *      reqNode.put("phone_num", "01012301230");
	 *      <br>
	 *      User user = JsonUtil2.toObject((JsonNode) reqNode, User.class); //JsonNode값을 User 객체로 변환 
	 *      <br>
	 *      LogUtil.writeLog(user.toString()); // User [address=null, age=25, gender=Man, name=Kim Ga-Na, phoneNum=null, phone_num=01012301230]
	 * </pre>
	 *
	 * @param jsonNode jsonNode
	 * @param clazz 변환할 클래스
	 * @return 클래스 객체
	 * 
	 * @deprecated Use {@link #readValue} instead
	 */
	@Deprecated
	public static <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
		try {
			return getObjectMapper().treeToValue(jsonNode, clazz);
		} catch (Exception e) {
			LogUtil.writeLog(e, JsonUtil2.class);
		}

		return null;
	}

	/**
	 * 객체를 ObjectNode로 변환한다.
	 * 
	 * <br>
	 * <br>
	 *
	 * <pre>
	 *      // [User Class]
	 *      //      public String name;
	 *      //      public String gender;
	 *      //      private int age;
	 *      //      private String address;
	 *      //      private String phone_num;
	 *      //      private String phoneNum;
	 *      <br>
	 *      User user = new User();
	 *      user.setName("Kim Ga-Na");
	 *      user.setAge(27);
	 *      user.setPhone_num("010-8888-9999");
	 *      user.setPhoneNum("010-7777-5555");
	 *      <br>
	 *      ObjectNode objectNode = JsonUtil2.toObjectNode(user); // User 객체를 ObjectNode로 변환
	 *      <br>
	 *      LogUtil.writeLog(objectNode.toString()); // {"gender":null,"age":27,"phoneNum":"010-7777-5555","address":null,"name":"Kim Ga-Na"}
	 * </pre>
	 * 
	 * @param object 변환할 객체
	 * @return ObjectNode
	 */
	public static ObjectNode toObjectNode(Object object) {
		return readValue(object, ObjectNode.class);
	}

	/**
	 * 하위 노드에 객체를 등록한다.
	 * 객체의 타입에 따라서 다음과 같은 형태로 등록한다.
	 * 객체의 타입이, <BR />
	 * <UL>
	 * <LI>문자열({@link java.lang.String})이면, textual 노드로 등록한다.</LI>
	 * <LI>부울 값(<code>Boolean</code>)이면, boolean 노드로 등록한다.</LI>
	 * <LI>숫자형이면, 숫자의 종류(정수, 소수 등)에 맞는 노드로 등록한다.</LI>
	 * <LI><code>BigDecimal</code>이면, BigDecimal 노드로 등록한다.</LI>
	 * <LI><code>byte[]</code>이면, binary 노드로 등록한다.</LI>
	 * <LI>{@link com.fasterxml.jackson.node.ArrayNode}이면, array 노드로 등록한다.</LI>
	 * <LI>{@link com.fasterxml.jackson.node.ObjectNode}이면, Object 노드로 등록한다.</LI>
	 * <LI>그 외에는, POJO 노드로 등록한다.</LI>
	 * </UL>
	 *
	 * @param node 하위 노드를 등록할 노드(parent)
	 * @param name 등록할 하위 노드의 이름
	 * @param value 등록할 객체
	 * @return 하위 노드를 등록한 노드(parent)를 돌려 준다.
	 * @see <a href="http://jackson.codehaus.org/1.6.3/javadoc/org/codehaus/jackson/node/BaseJsonNode.html">JsonNode의 하위 노드 종류</a>
	 */
	public static ObjectNode putValue(ObjectNode node, String name, Object value) {
		if (value instanceof String) {
			node.put(name, String.valueOf(value));
		}

		else if (value.getClass() == Boolean.class) {
			node.put(name, (Boolean) value);
		}

		else if (value instanceof Number) {
			Number v = (Number) value;

			if (v.getClass() == Integer.class) {
				node.put(name, v.intValue());
			}

			else if (v.getClass() == Long.class) {
				node.put(name, v.longValue());
			}

			else if (v.getClass() == Float.class) {
				node.put(name, v.floatValue());
			}

			else if (v.getClass() == Double.class) {
				node.put(name, v.doubleValue());
			}
		}

		else if (value instanceof BigDecimal) {
			node.put(name, (BigDecimal) value);
		}

		else if (value instanceof byte[]) {
			node.put(name, (byte[]) value);
		}

		else if (value instanceof ArrayNode) {
			node.putArray(name).addAll((ArrayNode) value);
		}

		else if (value instanceof ObjectNode) {
			node.putObject(name).putAll((ObjectNode) value);
		}

		else {
			node.putPOJO(name, value);
		}

		return node;
	}

	/**
	 * 하위 <code>ValueNode</code>의 값을 객체로 돌려 준다. <code>ContainerNode(ObjectNode, ArrayNode)</code>는 지원하지 않는다.
	 * 객체의 타입에 따라서 다음과 같이 돌려 준다. <code>ValueNode</code>가, <BR />
	 * <UL>
	 * <LI>binary 노드이면, <code>byte[]</code>로 돌려 준다.</LI>
	 * <LI>textual 노드이면, {@link java.lang.String} 객체로 돌려 준다.</LI>
	 * <LI>number 노드이면, 숫자의 종류(정수, 소수 등)에 맞는 객체로 돌려 준다.</LI>
	 * <LI>boolean 노드이면, {@link java.lang.Boolean} 객체로 돌려 준다.</LI>
	 * <LI>array 노드이면, {@link com.fasterxml.jackson.node.ArrayNode} 객체로 돌려 준다.</LI>
	 * <LI>Object 노드이면, {@link com.fasterxml.jackson.node.ObjectNode} 객체로 돌려 준다.</LI>
	 * <LI>POJO 노드이면, {@link java.lang.Object} 객체로 돌려 준다.</LI>
	 * <LI>그 외에는, {@code ""(빈 문자열)}을 돌려 준다.</LI>
	 * </UL>
	 * 
	 * @param node 하위에 ValueNode를 가진 노드(Parent)
	 * @param name 찾을 ValueNode의 이름
	 * @return 찾은 객체. <code>ValueNode</code>가 아니거나, 찾지 못 하거나, 값이 null인 경우에는 {@code ""(빈 문자열)}을 돌려 준다.
	 * @see <a href="http://jackson.codehaus.org/1.6.3/javadoc/org/codehaus/jackson/node/BaseJsonNode.html">JsonNode의 하위 노드 종류</a>
	 */
	public static Object getValue(JsonNode node, String name) {
		Object obj = null;
		JsonNode valueNode = node.path(name);

		if (valueNode == null || valueNode.isMissingNode() || valueNode.isNull()) {
			obj = "".toString();
		}

		else if (valueNode.isBinary()) {
			try {
				byte[] bytes = valueNode.binaryValue();
				obj = bytes;
			} catch (IOException e) {
				obj = "".toString();
			}
		}

		else if (valueNode.isTextual()) {
			obj = valueNode.textValue();
		}

		else if (valueNode.isNumber()) {
			if (valueNode.isInt()) {
				obj = new Integer(valueNode.intValue());
			}

			else if (valueNode.isLong()) {
				obj = new Long(valueNode.longValue());
			}

			else if (valueNode.isDouble()) {
				obj = new Double(valueNode.doubleValue());
			}
		}

		else if (valueNode.isBoolean()) {
			obj = new Boolean(valueNode.booleanValue());
		}

		else if (valueNode.isArray()) {
			obj = (ArrayNode) valueNode;
		}

		else if (valueNode.isObject()) {
			obj = (ObjectNode) valueNode;
		}

		else if (valueNode.isPojo()) {
			obj = ((POJONode) valueNode).getPojo();
		}

		return obj;
	}

	/**
	 * "/"로 구분된 경로에 해당하는 노드를 찾는다. <br/>
	 * "/header/trcode"로 path를 주게 되면, baseNode의 자식 노드를 header, trcode 순서대로 찾아 간다. <br/>
	 * 다음과 같이 사용할 수 있다. <br />
	 * <code>
	 * 		JsonNode node = JsonUtil2.find(rootNode, "/header/trcode");
	 * </code>
	 * 
	 * @param baseNode 경로를 탐색할 노드
	 * @param path "/"로 구분된 경로 정보
	 * @return 찾은 노드를 돌려 주며, 노드를 찾지 못 하면, {@link com.fasterxml.jackson.node.MissingNode}를 돌려 준다.
	 */
	public static JsonNode find(JsonNode baseNode, String path) {
		if (baseNode == null) {
			return MissingNode.getInstance();
		}

		else if (baseNode.isMissingNode()) {
			return baseNode;
		}

		else if (path == null) {
			return baseNode;
		}

		else {
			path.replaceAll("[/]+$", "");
			path.replaceAll("^[/]+", "");

			return find(baseNode, StringUtil.split(path, "/"));
		}
	}

	/**
	 * 경로에 해당하는 노드를 찾는다.
	 * 
	 * @param baseNode 경로를 탐색할 노드
	 * @param path 순차적인 경로 정보
	 * @return 찾은 노드를 돌려 주며, 노드를 찾지 못 하면, {@link com.fasterxml.jackson.node.MissingNode}를 돌려 준다.
	 */
	public static JsonNode find(JsonNode baseNode, String[] path) {
		if (baseNode == null) {
			return MissingNode.getInstance();
		}

		else if (path == null || StringUtil.isBlank(path[0]) || baseNode.isMissingNode()) {
			return baseNode;
		}

		else if (path.length < 2) {
			return baseNode.path(path[0]);
		}

		else {
			ArrayList<String> list = new ArrayList<String>(path.length);

			for (String p : path) {
				list.add(p);
			}

			return find(baseNode, list);
		}
	}

	/**
	 * 경로에 해당하는 노드를 찾는다.
	 * 
	 * @param baseNode 경로를 탐색할 노드
	 * @param path 순차적인 경로 정보
	 * @return 찾은 노드를 돌려 주며, 노드를 찾지 못 하면, {@link com.fasterxml.jackson.node.MissingNode}를 돌려 준다.
	 */
	public static JsonNode find(JsonNode baseNode, ArrayList<String> path) {
		try {
			if (baseNode == null) {
				return MissingNode.getInstance();
			}

			else if (path == null || path.isEmpty() || StringUtil.isBlank(path.get(0)) || baseNode.isMissingNode()) {
				return baseNode;
			}

			else if (path.size() < 2) {
				return baseNode.path(path.get(0));
			}

			else {
				return find(baseNode.path(path.remove(0)), path);
			}
		} catch (Exception e) {
			return MissingNode.getInstance();
		}
	}

	// 확장 ///

	public static ArrayNode arrayNode() {
		return getNodeFactory().arrayNode();
	}

	public static MissingNode missingNode() {
		return MissingNode.getInstance();
	}

	/**
	 * 유효성 검사
	 */
	public static boolean isValidate(JsonNode jsonNode) {
		if (jsonNode == null || jsonNode.isMissingNode() || jsonNode.isNull()) {
			return false;
		}
		return true;
	}

	/**
	 * JSON 복사
	 */
	public static <T extends JsonNode> T copy(T json) throws CommonException {
		if (!isValidate(json)) {
			return json;
		}

		try {
			return (T) getObjectMapper().readTree(json.traverse());
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Copy json data \"" + json + "\" error", e.getMessage()), e);
		}
	}

	/**
	 * value를 담아서 반환한다.
	 */
	public static JsonNode objectNode(String fieldName, JsonNode value) {
		ObjectNode objectNode = objectNode();
		objectNode.put(fieldName, value);
		return objectNode;
	}

	public static JsonNode readFile(String filePath) throws CommonException {
		return readValue(new File(filePath));
	}

	public static JsonNode readValue(Object src) throws CommonException {
		return readValue(src, JsonNode.class);
	}

	/**
	 * 객체를 지정한 클래스 타입으로 변환한다.
	 * 
	 * <pre>
	 * com.fasterxml.jackson.map.JsonMappingException
	 * : Can not deserialize instance of com.fasterxml.jackson.JsonNode out of VALUE_EMBEDDED_OBJECT token
	 * 발생함
	 * </pre>
	 */
	public static <T> T readValue(Object src, Class<T> valueType) throws CommonException {
		try {
			T result = null;
			if (src instanceof String)
				result = getObjectMapper().readValue((String) src, valueType);
			else if (src instanceof File)
				result = getObjectMapper().readValue((File) src, valueType);
			else if (src instanceof URL)
				result = getObjectMapper().readValue((URL) src, valueType);
			else if (src instanceof Reader)
				result = getObjectMapper().readValue((Reader) src, valueType);
			else if (src instanceof InputStream)
				result = getObjectMapper().readValue((InputStream) src, valueType);
			else if (src instanceof JsonNode)
				result = getObjectMapper().treeToValue((JsonNode) src, valueType);
			else if (src instanceof JsonParser)
				result = getObjectMapper().readValue((JsonParser) src, valueType);
			else
				result = getObjectMapper().convertValue(src, valueType);

			return result;
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Read json data \"" + src + "\" error", e.getMessage()), e);
		}
	}

	@Deprecated
	public static JsonNode readValue2(Object src) throws CommonException {
		if (!Utils.isValidate(src))
			return missingNode();

		return readValue2(src, JsonNode.class);
	}

	/**
	 * 객체를 JsonNode 타입으로 변환한다.
	 * 
	 * <pre>
	 * com.fasterxml.jackson.map.JsonMappingException
	 * : Can not deserialize instance of com.fasterxml.jackson.JsonNode out of VALUE_EMBEDDED_OBJECT token
	 * 발생 안함
	 * </pre>
	 */
	@Deprecated
	public static <T> T readValue2(Object src, Class<T> valueType) throws CommonException {
		try {
			String json = getObjectMapper().writeValueAsString(src);

			json = StringUtil.replace(json, "null", "\"\"");

			return readValue(json, valueType);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Read json data \"" + src + "\" error", e.getMessage()), e);
		}
	}

	/**
	 * 객체를 JsonNode 타입으로 변환한다.
	 * 
	 * <pre>
	 * com.fasterxml.jackson.map.JsonMappingException
	 * : Can not deserialize instance of com.fasterxml.jackson.JsonNode out of VALUE_EMBEDDED_OBJECT token
	 * 발생 안함
	 * </pre>
	 *
	 * @param src Collection 객체가 없는 1차원 POJO 객체
	 * @param excludes 제외할 필드명들
	 */
	public static JsonNode readValue3(Object src, String... excludes) {
		ObjectNode objectNode = objectNode();
		try {
			Class<?> reqClass = src.getClass();
			Field[] fields = reqClass.getDeclaredFields();
//			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(src);
			for (Field field : fields) {
				if (ArrayUtils.contains(excludes, field.getName()))
					continue;

				field.setAccessible(true);
				Object value = field.get(src);
//				Object value = reqClass.getDeclaredMethod(
//						"get" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(src);
//				Object value = fieldAccessor.getPropertyValue(name);

				putValue(objectNode, field.getName(), value);
			}
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Read json data \"" + src + "\" error", e.getMessage()), e);
		}

		return objectNode;
	}

	public static String writeText(String header, JsonNode body, String colDelim, String rowDelim) {
		if (!isValidate(body))
			return "";

		StringBuilder sb = new StringBuilder();
		String[] fieldNames = StringUtil.split(header, colDelim);

		if (body instanceof ObjectNode) {
			for (String fieldName : fieldNames) {
				sb.append(body.path(fieldName).asText() + colDelim);
			}
		} else if (body instanceof ArrayNode) {
			for (JsonNode node : body) {
				for (String fieldName : fieldNames) {
					sb.append(node.path(fieldName).asText() + colDelim);
				}
				sb.append(rowDelim);
			}
		}

		return sb.toString();
	}

	public static JsonNode readText(String header, String body, String colDelim, String rowDelim) {
		return readText("list", header, body, colDelim, rowDelim);
	}

	public static JsonNode readText(String fieldName, String header, String body, String colDelim, String rowDelim) {
		if (!Utils.isValidate(body))
			return missingNode();

		String[] fieldNames = StringUtil.split(header, colDelim);
		String[] rows = StringUtil.split(body, rowDelim);

		ObjectNode resBody = objectNode();
		ArrayNode resArray = arrayNode();
		resBody.put(fieldName, resArray);

		for (String row : rows) {
			resArray.add(readText(fieldNames, row, colDelim));
		}

		return resBody;
	}

	public static JsonNode readText(String header, String body, String colDelim) {
		return readText(StringUtil.split(header, colDelim), body, colDelim);
	}

	/**
	 * Plan text 파일을 읽어온다.
	 */
	public static JsonNode readText(String[] fieldNames, String body, String colDelim) {
		if (!Utils.isValidate(body))
			return missingNode();

		String[] values = StringUtil.split(body, colDelim);
		ObjectNode objectNode = objectNode();
		for (int i = 0; i < fieldNames.length; i++) {
			objectNode.put(fieldNames[i], values[i]);
		}

		return objectNode;
	}

	/**
	 * null, byte[] 타입 저장
	 */
	public static ObjectNode putValue2(ObjectNode node, String name, Object value) {
		if (value == null)
			node.put(name, getNodeFactory().nullNode());
//		else if (value instanceof byte[])
//			node.put(name, new String((byte[]) value).trim());
		else
			putValue(node, name, value);

		return node;
	}

	/**
	 * ArrayNode<TextNode> -> String[] 변환
	 */
	public static String[] convertIterableToArray(Iterable<JsonNode> iterable) {
		List<String> list = convertIterableToList(iterable);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * ArrayNode<TextNode> -> List<String> 변환
	 */
	public static List<String> convertIterableToList(Iterable<JsonNode> iterable) {
		List<String> list = new ArrayList<String>();
		if (iterable != null) {
			for (JsonNode i : iterable) {
				list.add(i.asText());
			}
		}

		return list;
	}

	/**
	 * JsonNode -> List<NameValuePair> 변환
	 */
	public static List<NameValuePair> convertJsonToList(JsonNode json) {
		Map<String, String> map = readValue(json, Map.class);

		return HTTPUtil.convertMapToList(map);
	}

	public static Map<String, Object> convertJsonToMap(JsonNode json, String... excludeKeys) {
		return convertJsonToMap(json, null, excludeKeys);
	}

	/**
	 * JsonNode -> Map<String, String> 변환
	 */
	public static Map<String, Object> convertJsonToMap(JsonNode json, Map<String, Object> includeMap,
			String... excludeKeys) {
		Map<String, Object> readMap = readValue(json, Map.class);

		return Utils.filter(readMap, includeMap, excludeKeys);
	}

	/**
	 * 문자열 변경
	 */
	public static JsonNode replace(JsonNode json, String search, String replacement) throws CommonException {
		return readValue(json.toString().replace(search, replacement));
	}

	/**
	 * 문자열 변경
	 */
	@Deprecated
	public static JsonNode replace(JsonNode json, String path, String search, String replacement)
			throws CommonException {
		JsonNode target = find(json, path);
		target = replace(target, search, replacement);

		JsonNode aaa = find(json, "/body/aaa");
		putValue((ObjectNode) aaa, "bbb", target);

		JsonNode body = find(json, "/body");
		putValue((ObjectNode) body, "aaa", aaa);
		putValue((ObjectNode) json, "body", body);

//		put(json, target, path);

		return json;
	}

	/**
	 * JSON 입력
	 */
	@Deprecated
	public static JsonNode put(JsonNode json, JsonNode target, String path) {
		String[] paths = StringUtil.split(path, "/");

		JsonNode aaa = find(json, "/body/aaa");
		putValue((ObjectNode) aaa, paths[paths.length], target);
		for (int i = paths.length - 1; i >= 0; i--) {
			JsonNode bbb = find(json, "/body/aaa");
			JsonNode first = find(json, path);
		}

		return null;
	}

	/**
	 * Base64로 암호화한다.
	 */
	public static JsonNode encodeBase64(JsonNode jsonNode, String fieldNames) {
		if (isValidate(jsonNode) && Utils.isValidate(fieldNames)) {
			String[] arrFieldName = StringUtil.split(fieldNames, "/");
			for (String fieldName : arrFieldName) {
				JsonNode contents = jsonNode.path(fieldName);
				if (!isValidate(contents))
					continue;

				((ObjectNode) jsonNode).put(fieldName, Base64Encoder.encode(contents.asText().getBytes()));
			}
		}

		return jsonNode;
	}

	/**
	 * CSS/HTML 본문 교정
	 */
	public static JsonNode correctHtml(JsonNode jsonNode, String fieldNames) {
		if (isValidate(jsonNode) && Utils.isValidate(fieldNames)) {
			String[] arrFieldName = StringUtil.split(fieldNames, "/");
			for (String fieldName : arrFieldName) {
				JsonNode contents = jsonNode.path(fieldName);
				if (!isValidate(contents))
					continue;

				((ObjectNode) jsonNode).put(fieldName, Html2Txt.correctHtml(contents.asText()));
			}
		}

		return jsonNode;
	}

	public static void main(String[] args) throws Exception {
//		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
//		JsonNode resRoot = readFile(StringUtil.getProperty(Smart.KEY_HOME_DIR, "/data/test/test01.json"));
//		LogUtil.writeLog(resRoot);

//		replace(resRoot, "/body/aaa/bbb", "${TEST2}", "skoh");
//		LogUtil.writeLog(resRoot);

//		ObjectNode objectNode = toObjectNode(null);
//		LogUtil.writeLog(objectNode);
//
//		objectNode = objectNode();
//		objectNode.putNull("fieldName");
//		LogUtil.writeLog(objectNode);
//
//		JsonNode jsonNode = objectNode.path("fieldName");
//		LogUtil.writeLog(jsonNode.isNull());
//
//		jsonNode = objectNode.path("fieldName1");
//		LogUtil.writeLog(jsonNode.isMissingNode());

//		JsonAdaptorObject obj = new JsonAdaptorObject();
//		LogUtil.writeLog(obj);
//		HashMap map = new HashMap();
//		map.put("arg0", null);
//		AdapterUtil2.setSessionValue(obj, map);
//		LogUtil.writeLog(obj);
//		HashMap map1 = new HashMap();
//		map1.put("arg00", "arg11");
//		AdapterUtil2.setSessionValue(obj, map1);
//		LogUtil.writeLog(obj);

//		ObjectNode objectNode = objectNode();
//		objectNode.put("fieldName", "a");
//		HashMap map = new HashMap();
//		for (Iterator<Entry<String, JsonNode>> fields = objectNode.getFields(); fields.hasNext();) {
//			Entry<String, JsonNode> entry = fields.next();
//			LogUtil.writeLog(entry.getKey() + ":" + entry.getValue().asText());
//		}
//		LogUtil.writeLog(readValue2(map));

//		DefaultHttpResponse resHttp = new DefaultHttpResponse();
//		resHttp.setMimeType("가");
//		resHttp.setContent("나".getBytes());
//		String message = "{\"message\":\"ab\",\"users\":[\"a\",\"b\"]}";
		String message = "{\"message\":\"ab\",\"users\":[{\"user\":\"null\"},{\"user\":null}]}";
//		JsonNode messageNode = readValue(resHttp);
//		LogUtil.writeLog(messageNode);
//		messageNode = readValue3(resHttp);
//		LogUtil.writeLog(messageNode);
//		BinaryNode node = (BinaryNode) messageNode.path("content");
//		LogUtil.writeLog(new String(node.getBinaryValue()));

//		String message1 = messageNode.path("message").asText();
//		LogUtil.writeLog(message1);

//		JsonNode usersNode = messageNode.path("users");
//		LogUtil.writeLog(usersNode);
//
//		int i = 1;
//		for (Iterator<JsonNode> iter = usersNode.iterator(); iter.hasNext();) {
//			ObjectNode userNode = (ObjectNode) iter.next();
//			String user = userNode.path("user").asText();
//			if (Utils.isValidate(user)) {
//				userNode.put("user", user + (i++));
//			}
//		}
//		LogUtil.writeLog(usersNode);
//
//		LogUtil.writeLog(messageNode);

//		List<JsonNode> list = Utils.convertList(usersNode);
//		Object[] users = list.toArray();
//		JsonNode[] users = list.toArray(new JsonNode[list.size()]);
//		List<String> list = convertList(usersNode);
//		String[] users = list.toArray(new String[list.size()]);
//
//		LogUtil.writeLog(Utils.getToString(users));

//		String s = "{\"sAttachFile\":[{\"FILE_NM\":\"http://gkowgwodv1.dongwha-dv.com/Storage/WF/Forms/PAYMENT DEFERRAL REQUEST/201207/20120716/D4000737-20120716025115_Calendar in Status bar_v2.0.5.apk\"},{\"FILE_NM\":\"FILE_NM02\"}]}";
//		JsonNode rootNode = readValue(s);
//		LogUtil.writeLog(rootNode);
//
//		ArrayNode sAttachFile = (ArrayNode) rootNode.path("sAttachFile");
//		for (int i = 0; i < sAttachFile.size(); i++) {
//			ObjectNode objectNode = (ObjectNode) sAttachFile.get(i);
//			objectNode.put("FILE_URL", "FILE_URL01");
//		}
//		LogUtil.writeLog(rootNode);

//		ObjectNode objectNode = objectNode();
//		objectNode.put("a", "1");
//		Map<String, Object> map = readValue(objectNode, Map.class);
//		LogUtil.writeLog(map);

//		String message = "{\"a\":\"1\", \"b\":1}";
//		JsonNode messageNode = readValue(message);
//		LogUtil.writeLog(messageNode.path("a").asText());
//		LogUtil.writeLog(messageNode.path("b").asText());

//		LogUtil.writeLog(readValue(new IOUtil2.Test01(), ObjectNode.class));

//		IOUtil2.Test01 test = new IOUtil2.Test01();
//		test.bytes = new byte[] {};
//		LogUtil.writeLog(test);

//		ObjectNode json = objectNode();
//		json.put("bytes", test.bytes);
//		JsonNode json = readValue(test);
//		JsonNode json = readValue2(test);
//		JsonNode json = readValue3(test);
//		LogUtil.writeLog(json);

//		IOUtil2.Test01 test2 = new IOUtil2.Test01();
//		test2.bytes = json.path("bytes").getBinaryValue();
//		LogUtil.writeLog(test2);

//		LogUtil.writeLog(isValidate(objectNode()));

//		ObjectNode json = objectNode();
//		json.put("b", "2");
//
//		json.putAll(objectNode);
//		ObjectNode json = copy(objectNode);
//
//		LogUtil.writeLog(objectNode);
//		LogUtil.writeLog(json);

		JsonNode json = readValue(message);
//		JsonNode json = readValue2(message);
		System.out.println(json.asText());
		System.out.println(json.toString());
	}
}
