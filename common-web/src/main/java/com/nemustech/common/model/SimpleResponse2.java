package com.nemustech.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nemustech.common.model.SimpleResponse;

/**
 * 응답 결과2
 * 
 * <pre>
 * - 샘플
 * {
 *   "msg" : "",
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
 */
public class SimpleResponse2<T> extends SimpleResponse<T> {
	/**
	 * 에러 메시지
	 */
	protected String msg;

	/**
	 * 성공 응답을 반환한다.
	 * 
	 * @param body
	 * @return
	 */
	public static <T> SimpleResponse2<T> getSuccessResponse(T body) {
		return new SimpleResponse2<T>(body);
	}

	/**
	 * 실패 응답을 반환한다.
	 * 
	 * @param error_code
	 * @param error_message
	 * @return
	 */
	public static <T> SimpleResponse2<T> getFailResponse(String msg) {
		return new SimpleResponse2<T>(msg);
	}

	public SimpleResponse2() {
	}

	public SimpleResponse2(String msg) {
		this(msg, null);
	}

	public SimpleResponse2(T body) {
		this("", body);
	}

	public SimpleResponse2(String msg, T body) {
		setMsg(msg);
		setBody(body);
	}

	@Override
	@JsonIgnore
	public String getMessage() {
		return message;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}