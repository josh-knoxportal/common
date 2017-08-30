package com.nemustech.common.model;

import java.io.Serializable;

import com.nemustech.common.util.StringUtil;

/**
 * 응답 결과
 * 
 * <pre>
 * - 샘플
 * {
 *   "header" : {
 *     "success_yn" : true,
 *     "error_code" : "",
 *     "error_message" : ""
 *   },
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
 *       "testSet" : [ ],
 *       "filesSet" : [ ]
 *     } ]
 *   }
 * }
 * </pre>
 * 
 * @author skoh
 */
public class Response<T> implements Serializable {
	/**
	 * 헤더
	 */
	protected Header header;

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
	public static <T> Response<T> getSuccessResponse(T body) {
		return new Response<T>(new Header(true, "", ""), body);
	}

	/**
	 * 실패 응답을 반환한다.
	 * 
	 * @param error_code
	 * @param error_message
	 * @return
	 */
	public static <T> Response<T> getFailResponse(String error_code, String error_message) {
		return new Response<T>(new Header(false, error_code, error_message));
	}

	/**
	 * 응답 결과를 변경한다.
	 * 
	 * @param response
	 * @param body
	 * @return
	 */
	public static <T1, T2> Response<T2> getResponse(Response<T1> response, T2 body) {
		Response<T2> result = null;

		Header header = response.getHeader();
		if (header.getSuccess_yn() == true) {
			result = getSuccessResponse(body);
		} else {
			result = getFailResponse(header.getError_code(), header.getError_message());
		}

		return result;
	}

	public Response() {
	}

	public Response(Header header) {
		this(header, null);
	}

	public Response(T body) {
		this(null, body);
	}

	public Response(Header header, T body) {
		setHeader(header);
		setBody(body);
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this);
	}
}