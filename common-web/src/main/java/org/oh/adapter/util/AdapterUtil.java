package org.oh.adapter.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.PropertyConfigurator;
import org.oh.adapter.DBAdapter;
import org.oh.adapter.exception.AdapterException;
import org.oh.adapter.http.DefaultHttpResponse;
import org.oh.common.Constants;
import org.oh.common.FunctionCallback;
import org.oh.common.util.FileUtil;
import org.oh.common.util.HTTPUtil;
import org.oh.common.util.JsonUtil2;
import org.oh.common.util.LogUtil;
import org.oh.common.util.PropertyUtils;
import org.oh.common.util.ReflectionUtil;
import org.oh.common.util.StringUtil;
import org.oh.common.util.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AdapterUtil {
	protected static Log log = LogFactory.getLog(AdapterUtil.class);

	// 세션 키
	public static final String META_KEY_ID = "id"; // "test"
	public static final String META_KEY_TRANSACTION_ID = "TRANSACTION_ID"; // 4391094320000000000
	public static final String META_KEY_DEVICE_ID = "device_id"; // "1"
	public static final String META_KEY_OS_TYPE = "os_type"; // "Android"
	public static final String META_KEY_MOBILE_NUMBER = "mobile_number"; // "010-1234-5678"
	public static final String META_KEY_ATTACHMENT_DOWNLOAD = "attachment_download"; // "true"
	public static final String META_KEY_LASTACCESSEDTIME = "lastAccessedTime"; // 1360745510611

	// 데이터 구분
	public static final String REQUEST_DATA_PATH = "/data/request/";
	public static final String RESPONSE_DATA_PATH = "/data/response/";

	// 프로퍼티 키
	public static final String PROPERTY_IS_REAL = "org.oh.adapter.isOnline";
	public static final String PROPERTY_POSTFIX = "org.oh.adapter.postfix";
	public static final String PROPERTY_SUPER_PASSWORD = "org.oh.adapter.super_password";
	public static final String PROPERTY_SERVER_TYPE = "org.oh.adapter.server_type";
	public static final String PROPERTY_SERVER_URL = "org.oh.adapter.server_url";
	public static final String PROPERTY_SERVER_PORT = "org.oh.adapter.server_port";
	public static final String PROPERTY_SERVER_CONTEXT = "org.oh.adapter.server_context";
	public static final String PROPERTY_PUSH_URL = "org.oh.adapter.push_url";
	public static final String PROPERTY_PUSH_PORT = "org.oh.adapter.push_port";
	public static final String PROPERTY_PUSH_CONTEXT = "org.oh.adapter.push_context";

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return result[0]-페이지 번호, result[1]-페이지당 개수
	 */
	public static int[] CalcToPage(int startIndex, int endIndex) {
		if (startIndex == -1) {
			int[] result = { -1, -1 };
			return result;
		}

		int tempEndIndex = endIndex + 1;
		long point = tempEndIndex % 10;

		if (point == 0) {
			endIndex = endIndex + 1;
		}

		int curPerPage = tempEndIndex - startIndex;
		int curPageNo = tempEndIndex / curPerPage;
		int[] result = { curPageNo, curPerPage };

		return result;
	}

	private static HashMap<String, Object> reqMessageToHashMap(JsonNode reqNode) {
		Iterator<String> iter = reqNode.fieldNames();
		HashMap<String, Object> reqItems = new HashMap<String, Object>();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object value = JsonUtil2.getValue(reqNode, key);

			if (value instanceof String) {
				reqItems.put(key, (String) value);
			} else if (value instanceof Integer) {
				reqItems.put(key, (Integer) value);
			} else if (value instanceof Boolean) {
				reqItems.put(key, (Boolean) value);
			} else if (value instanceof Long) {
				reqItems.put(key, (Long) value);
			}
		}
		return reqItems;
	}

	@SuppressWarnings({ "rawtypes" })
	private static Map<String, Object> reqMessageToSimpleMap(Object reqObject) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>();

		Class reqClass = reqObject.getClass();
		Field[] fields = reqClass.getDeclaredFields();
		Class[] paramTypes = {};
		for (Field field : fields) {
			String key = field.getName();
			String methodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
			Object value = reqClass.getDeclaredMethod(methodName, paramTypes).invoke(reqObject);
			map.put(key, value);
		}

		return map;
	}

//    private List< HashMap< String, Object >> ConvertComplexMap(ArrayNode reqNode) {
//    	List< HashMap< String, Object >> reqListItems = new ArrayList< HashMap< String, Object >>();
//    	
//    	for(JsonNode item : reqNode) {
//    		Iterator<String>			iter 		= item.getFieldNames();
//    		HashMap<String, Object> 	reqMap		= new HashMap<String, Object>(); 
//    		while(iter.hasNext()) {
//    			String 		key 		= (String)iter.next();
//    			Object 		value 		= JsonUtil2.getValue(reqNode, key);
//    			
//    			if (value instanceof String) {
//    				reqMap.put(key, (String)value);
//    			} else if (value instanceof Integer) {
//    				reqMap.put(key, (Integer)value);
//    			} else if (value instanceof Boolean) {
//    				reqMap.put(key, (Boolean)value);
//    			} else if (value instanceof Long) {
//    				reqMap.put(key, (Long)value);
//    			}
//    		}
//    		
//    		reqListItems.add(reqMap);
//    	}
//        return reqListItems;
//    }

	public static List<Map<String, Object>> ConvertComplexMap(Object reqArrayObj) throws AdapterException {
		JsonNode reqNode = ConvertJsonNode(reqArrayObj);
		List<Map<String, Object>> reqListItems = new ArrayList<Map<String, Object>>();

		for (JsonNode item : reqNode) {
			Iterator<String> iter = item.fieldNames();
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object value = JsonUtil2.getValue(reqNode, key);

				if (value instanceof String) {
					reqMap.put(key, (String) value);
				} else if (value instanceof Integer) {
					reqMap.put(key, (Integer) value);
				} else if (value instanceof Boolean) {
					reqMap.put(key, (Boolean) value);
				} else if (value instanceof Long) {
					reqMap.put(key, (Long) value);
				}
			}

			reqListItems.add(reqMap);
		}
		return reqListItems;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> ConvertMap(Object reqObj) throws AdapterException {
		try {
			Map<String, Object> reqMap = new HashMap<String, Object>();
			if (reqObj instanceof Map) {
				reqMap = (Map<String, Object>) reqObj;
			} else if (reqObj instanceof JsonNode) {
				reqMap = reqMessageToHashMap((JsonNode) reqObj);
			} else {
				reqMap = reqMessageToSimpleMap(reqObj);
			}
			return reqMap;
		} catch (Exception e) {
			throw new AdapterException("데이터 초기화에 실패하였습니다.", "HttpInitException", e);
		}
	}

	public static JsonNode ConvertJsonNode(Object resBody) throws AdapterException {

		if (resBody instanceof JsonNode) {
			return (JsonNode) resBody;
		} else {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.convertValue(resBody, JsonNode.class);
				return jsonNode;
			} catch (Exception e) {
				throw new AdapterException("PARSE", "결과값을 변환하던 중 에러가 발생하였습니다.", e);
			}
		}
	}

	public static boolean isOnline() {
		return Boolean.parseBoolean(PropertyUtils.getInstance().getString(PROPERTY_IS_REAL, "true"));
	}

	public static String getPostfix() {
		return PropertyUtils.getInstance().getString(PROPERTY_POSTFIX, "A");
	}

	public static String getMessage(AdapterException e) {
		return getMessage(e.getErrorCode(), e.getMessage());
	}

	public static String getErrorMessageDetail(AdapterException e) {
		return getMessage(e.getErrorCode(), e.getMessage(), (e.getCause() == null) ? null : e.getCause().getMessage());
	}

	public static String getMessage(String errorCode, Throwable t) {
		return getMessage(errorCode, t.getMessage());
	}

	public static String getErrorMessageDetail(String errorCode, Throwable t) {
		return getMessage(errorCode, t.getMessage(), (t.getCause() == null) ? null : t.getCause().getMessage());
	}

	public static String getMessage(String errorMessage) {
		return getMessage(null, errorMessage);
	}

	public static String getMessage(String errorCode, String errorMessage) {
		return getMessage(errorCode, errorMessage, null);
	}

	public static String getErrorMessageCause(String errorMessage, String errorMessageCause) {
		return getMessage(null, errorMessage, errorMessageCause);
	}

	/**
	 * 에러메세지를 구성한다.
	 */
	public static String getMessage(String errorCode, String errorMessage, String errorMessageCause) {
		return (Utils.isValidate(errorCode) ? "[" + errorCode + "]" : "") + errorMessage
				+ (Utils.isValidate(errorMessageCause) ? "-" + errorMessageCause : "");
	}

	public static String getSessionId(HttpServletRequest request) throws AdapterException {
		return (String) getSessionValue(request, META_KEY_ID);
	}

	public static Object getSessionValue(HttpServletRequest request, String key) throws AdapterException {
		return getSessionValue(request.getSession(false), key);
	}

	public static Object getSessionValue(HttpSession session, String key) throws AdapterException {
		try {
			return session.getAttribute(key);
		} catch (Exception e) {
			throw new AdapterException(AdapterException.ERROR, "Invalid http session error", e);
		}
	}

	public static <T> JsonNode getResponse(String trcode, FunctionCallback<T, JsonNode> callback, T request)
			throws AdapterException {
		return getResponse(true, trcode, callback, request);
	}

	// 확장 ///

	public static <T> JsonNode getResponse(boolean isOnline, String trcode, FunctionCallback<T, JsonNode> callback,
			T request) throws AdapterException {
		return getResponse(isOnline, null, trcode, callback, request);
	}

	public static <T> JsonNode getResponse(boolean isOnline, String postfix, String trcode,
			FunctionCallback<T, JsonNode> callback, T request) throws AdapterException {
		JsonNode resNode = getResBody(isOnline, postfix, trcode, callback, request, JsonNode.class);

		return resNode;
	}

	public static <T> JsonNode getResBody(String trcode, FunctionCallback<T, JsonNode> callback, T request)
			throws AdapterException {
		return getResBody(true, trcode, callback, request);
	}

	public static <T> JsonNode getResBody(boolean isOnline, String trcode, FunctionCallback<T, JsonNode> callback,
			T request) throws AdapterException {
		return getResBody(isOnline, trcode, callback, request, JsonNode.class);
	}

	public static <T> JsonNode getResBody(boolean isOnline, String postfix, String trcode,
			FunctionCallback<T, JsonNode> callback, T request) throws AdapterException {
		return getResBody(isOnline, postfix, trcode, callback, request, JsonNode.class);
	}

	public static <T1, T2> T2 getResBody(String trcode, FunctionCallback<T1, T2> callback, T1 request,
			Class<T2> returnType) throws AdapterException {
		return getResBody(true, trcode, callback, request, returnType);
	}

	public static <T1, T2> T2 getResBody(boolean isOnline, String trcode, FunctionCallback<T1, T2> callback, T1 request,
			Class<T2> returnType) throws AdapterException {
		return getResBody(isOnline, null, trcode, callback, request, returnType);
	}

	/**
	 * 실제 또는 테스트 응답데이터를 구한다.
	 *
	 * @param isOnline true 일경우 서버 응답데이터를, false 일경우 테스트 응답데이터를 사용한다.(기본값: true)
	 * @param postfix 테스트 응답데이터 구분자(기본값: "A")
	 * @param trcode 전문코드
	 * @param callback 레거시 콜백
	 * @param request 요청데이터
	 * @param returnType 응답타입
	 * @param params 파라미터
	 */
	public static <T1, T2> T2 getResBody(boolean isOnline, String postfix, String trcode,
			FunctionCallback<T1, T2> callback, T1 request, Class<T2> returnType) throws AdapterException {
//		checkSession(obj); // skoh test

		try {
			Object resBody = null;
			if (isOnline() && isOnline) {
				// 응답 템플릿 파일 로드
//				JsonNode resTemp = null;
//				try {
//					resTemp = JsonUtil2.readFile(StringUtil.getProperty(Smart.KEY_HOME_DIR, RESPONSE_DATA_PATH
//							+ trcode + ".json"));
//				} catch (PointiException e) {
//					if (!FileNotFoundException.class.isInstance(e.getCause())) {
//						throw e;
//					}
//				}

				// 콜백 함수 호출
				resBody = callback.executeTemplate(request);
//				if (ObjectNode.class.isInstance(resBody)) {
//					if (resTemp != null)
//						resBody = ((ObjectNode) resTemp).putAll((ObjectNode) resBody);
//				} else if (ArrayNode.class.isInstance(resBody)) {
//					if (resTemp != null)
//						resBody = ((ArrayNode) resTemp).addAll((ArrayNode) resBody);
//				}
			} else {
				// 응답 테스트 파일 로드
				postfix = (Utils.isValidate(postfix)) ? postfix : getPostfix();
				if (returnType == JsonNode.class) {
					resBody = JsonUtil2.readFile(StringUtil.getProperty(Constants.KEY_HOME_DIR,
							RESPONSE_DATA_PATH + trcode + postfix + ".json"));
				} else if (returnType == DefaultHttpResponse.class) {
					resBody = new DefaultHttpResponse(FileUtil.readFileToByteArray(new File(StringUtil
							.getProperty(Constants.KEY_HOME_DIR, RESPONSE_DATA_PATH + trcode + postfix + ".txt"))));
				} else {
					resBody = FileUtil.readFileToString(new File(StringUtil.getProperty(Constants.KEY_HOME_DIR,
							RESPONSE_DATA_PATH + trcode + postfix + ".txt")));
					if (returnType == Integer.class) {
						resBody = Integer.parseInt((String) resBody);
					}
				}
				log.debug(resBody.toString());
			}

			return (T2) resBody;
		} catch (AdapterException e) {
			String errorCode = (Utils.isValidate(e.getErrorCode())) ? e.getErrorCode()
					: trcode + AdapterException.PREFIX_SYSTEM;
			String errorMessage = (Utils.isValidate(e.getMessage())) ? e.getMessage() : e.getCause().getMessage();
			throw new AdapterException(errorCode, errorMessage, e);
		} catch (Exception e) {
			throw new AdapterException(trcode + AdapterException.PREFIX_SYSTEM,
					"Call legacy trcode \"" + trcode + "\" error", e);
		}
	}

	/**
	 * HttpSession 을 JsonNode 형태로 돌려준다.
	 */
	public static JsonNode convertJSON(HttpSession session) {
		ObjectNode objectNode = JsonUtil2.objectNode().objectNode();
		Enumeration<String> attrs = session.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String name = (String) attrs.nextElement();
			objectNode.putPOJO(name, session.getAttribute(name));
		}

		return objectNode;
	}

	public static String convertObjectToURI(String url, Object formParams, String... excludes) throws AdapterException {
		return convertObjectToURI(url, formParams, null, excludes);
	}

	/**
	 * 객체의 필드를 파라미터로 URI를 만든다.
	 */
	public static String convertObjectToURI(String url, Object formParams, List<NameValuePair> includes,
			String... excludes) throws AdapterException {
		return HTTPUtil.convertObjectToURI(url, convertObjectToList(formParams, includes, excludes));
	}

	public static List<NameValuePair> convertObjectToList(Object obj, String... excludes) throws AdapterException {
		return convertObjectToList(obj, null, excludes);
	}

	/**
	 * Object -> List<NameValuePair> 으로 변환한다.
	 */
	public static List<NameValuePair> convertObjectToList(Object param, List<NameValuePair> includes,
			String... excludes) throws AdapterException {
		Map<String, Object> convertMap = convertObjectToMap(param);
		Map<String, String> convertMap2 = Utils.convertMapToMap(convertMap);
		Map<String, String> includeMap = HTTPUtil.convertListToMap(includes);
		Map<String, String> filterMap = Utils.filter(convertMap2, includeMap, excludes);

		return HTTPUtil.convertMapToList(filterMap);
	}

	/**
	 * Object -> Map<String, Object> 으로 변환한다.
	 */
	public static Map<String, Object> convertObjectToMap(Object obj) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		if (obj instanceof Map)
			reqMap = (Map) obj;
		else if (obj instanceof JsonNode)
			reqMap = JsonUtil2.readValue((JsonNode) obj, Map.class);
		else {
			reqMap = ReflectionUtil.convertObjectToMap(obj);
		}

		return reqMap;
	}

	public static JsonNode executeSqlToJson(DBAdapter dbAdapter, SqlSession sqlSession, String nameSpace, String sqlId,
			Object request) throws AdapterException {
		Object result = executeSql(dbAdapter, sqlSession, nameSpace, sqlId, request);

		JsonNode json = JsonUtil2.readValue2(result);
		if (result instanceof Collection) {
			ObjectNode list = JsonUtil2.objectNode();
			list.put("list", json);

			return list;
		} else {
			return json;
		}
	}

	public static Object executeSql(DBAdapter dbAdapter, SqlSession sqlSession, String nameSpace, String sqlId,
			Object request) throws AdapterException {
		Class<?> resultType = Integer.class;
		if (sqlId.startsWith("select")) {
			resultType = Map.class;
		}

		return executeSql(dbAdapter, sqlSession, nameSpace, sqlId, request, resultType);
	}

	/**
	 * 쿼리를 실행한다.
	 */
	public static Object executeSql(DBAdapter dbAdapter, SqlSession sqlSession, String nameSpace, String sqlId,
			Object request, Class<?> resultType) throws AdapterException {
		Object result = null;
		try {
			if (sqlId.startsWith("selectOne")) {
				result = dbAdapter.selectOne(sqlSession, nameSpace + "." + sqlId,
						JsonUtil2.readValue(request, Map.class), resultType);
			} else if (sqlId.startsWith("select")) {
				result = dbAdapter.selectList(sqlSession, nameSpace + "." + sqlId,
						JsonUtil2.readValue(request, Map.class), resultType);
			} else if (sqlId.startsWith("insert")) {
				result = dbAdapter.insert(sqlSession, nameSpace + "." + sqlId, JsonUtil2.readValue(request, Map.class));
			} else if (sqlId.startsWith("update")) {
				result = dbAdapter.update(sqlSession, nameSpace + "." + sqlId, JsonUtil2.readValue(request, Map.class));
			} else if (sqlId.startsWith("delete")) {
				result = dbAdapter.delete(sqlSession, nameSpace + "." + sqlId, JsonUtil2.readValue(request, Map.class));
			}
		} catch (Exception e) {
			if (e.getCause() != null && SQLException.class.isInstance(e.getCause())) {
				throw new AdapterException(sqlId,
						"Execute sql \"" + sqlSession.getConfiguration().getEnvironment().getId() + ":" + nameSpace
								+ "." + sqlId + "\" error - " + e.getCause().getMessage());
			}
		}

		return result;
	}

	/**
	 * 다운로드 URL
	 */
	public static String getDownloadUrl(String trcode, String file_name, List<NameValuePair> params) {
		String url = PropertyUtils.getInstance().getString("org.oh.adapter.server_url") + ":"
				+ PropertyUtils.getInstance().getString("org.oh.adapter.server_port") + "/"
				+ PropertyUtils.getInstance().getString("org.oh.adapter.server_context")
				+ PropertyUtils.getInstance().getString(trcode + "_DOWNLOAD_PATH");

		params.add(new BasicNameValuePair("mode", "1"));
		params.add(new BasicNameValuePair("file_name", file_name));

		return HTTPUtil.convertObjectToURI(url, params);
	}

	/**
	 * 서버명 반환
	 */
	public static String geServerName(String trcode) {
		if (trcode.startsWith("NSG1")) {
			return "";
		} else if (trcode.startsWith("NSG2")) {
			return "";
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

//		DefaultHttpResponse resHttp = new DefaultHttpResponse();
//		resHttp.setMimeType("skoh");
//		JsonNode resHttp = JsonUtil2.readValue("{\"mimeType\":\"skoh\"}");
//		LogUtil.writeLog(ConvertMap(resHttp));

//		LogUtil.writeLog(ArrayUtils.contains(null, null));

//		Map<String, Integer> map = new HashMap<String, Integer>();
//		map.put("a", 1);
//		map.put("b", 2);
//		Map<String, String> map2 = Utils.convertMapToMap((Map) map);
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("c", "3"));
//		LogUtil.writeLog(convertObjectToList(map, list, "a"));

		LogUtil.writeLog(Integer.getInteger("0"));
		LogUtil.writeLog(Integer.parseInt("0"));
	}
}
