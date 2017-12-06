package com.nemustech.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nemustech.common.util.Utils;

/**
 * 응답 결과
 * 
 * <pre>
 * - 샘플
 * {
 *   "message" : "",
 *   "body" : {
 *     "currentPage" : 1,
 *     "rowsPerPage" : 10,
 *     "startRow" : 1,
 *     "endRow" : 1,
 *     "totalSize" : 1,
 *     "pageGroupCount" : 10,
 *     "pageGroupStart" : 1,
 *     "pageGroupEnd" : 1,
 *     "pageTotal" : 1,
 *     "list" : [ {
 *       "reg_id" : "1",
 *       "reg_dt" : "20160728162640",
 *       "mod_id" : "1",
 *       "mod_dt" : "20161007154501",
 *       "id" : 1,
 *       "name" : "s",
 *       "test_id" : 2,
 *       "count" : null,
 *       "tests" : [ ],
 *       "files" : [ ]
 *     } ]
 *   }
 * }
 * </pre>
 * 
 * @author skoh
 */
public class SimpleResponse<T> extends Response<T> {
	/**
	 * 에러 메시지
	 */
	protected String message;

	/**
	 * 바디
	 */
	protected T body;

	/**
	 * 성공 응답을 반환한다.
	 * 
	 * @param body
	 * @return
	 */
	public static <T> SimpleResponse<T> getSuccessResponse(T body) {
		return new SimpleResponse<T>(body);
	}

	/**
	 * 실패 응답을 반환한다.
	 * 
	 * @param error_code
	 * @param error_message
	 * @return
	 */
	public static <T> SimpleResponse<T> getFailResponse(String message) {
		return new SimpleResponse<T>(message);
	}

	/**
	 * 응답 결과를 변경한다.
	 * 
	 * @param response
	 * @param body
	 * @return
	 */
	public static <T1, T2> SimpleResponse<T2> getResponse(SimpleResponse<T1> response, T2 body) {
		SimpleResponse<T2> result = null;

		String message = response.getMessage();
		if (Utils.isValidate(message)) {
			result = getFailResponse(message);
		} else {
			result = getSuccessResponse(body);
		}

		return result;
	}

	public SimpleResponse() {
	}

	public SimpleResponse(String message) {
		this(message, null);
	}

	public SimpleResponse(T body) {
		this("", body);
	}

	public SimpleResponse(String message, T body) {
		setMessage(message);
		setBody(body);
	}

	@Override
	@JsonIgnore
	public Header getHeader() {
		return header;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}