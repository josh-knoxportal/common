package com.nemustech.web.option;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nemustech.common.model.Response;
import com.nemustech.web.util.ValidationUtil;

/**
 * 컨트롤러 전역 예외 처리2
 * 
 * <pre>
 * - Spring
 * {
 *   "timestamp" : 1512969460007,
 *   "status" : 405,
 *   "error" : "Method Not Allowed",
 *   "exception" : "org.springframework.web.HttpRequestMethodNotSupportedException",
 *   "message" : "Request method 'POST' not supported",
 *   "path" : "/common-web/sample/one"
 * }
 * </pre>
 * 
 * @author skoh
 */
@ControllerAdvice
public class ResponseEntityExceptionHandler2 extends ResponseEntityExceptionHandler {
	protected Log log = LogFactory.getLog(getClass());

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.warn(ValidationUtil.getHttpErrorCodeMaessage(status), ex);

		Response<Object> response = getFailResponse(ex, status);

		return new ResponseEntity<Object>(response, status);
	}

	/**
	 * 실패 응답
	 * 
	 * @param ex
	 * @param status
	 * @return
	 */
	protected Response<Object> getFailResponse(Exception ex, HttpStatus status) {
		return Response.getFailResponse(ex, status);
	}
}