package com.nemustech.web.option;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.nemustech.common.model.Response;
import com.nemustech.common.model.SimpleResponse;
import com.nemustech.common.util.StringUtil;
import com.nemustech.web.util.ValidationUtil;

/**
 * 컨트롤러 전역 예외 처리3
 * 
 * @author skoh
 */
@ControllerAdvice
public class ResponseEntityExceptionHandler3 extends ResponseEntityExceptionHandler2 {
	@Override
	protected Response<Object> getFailResponse(Exception ex, HttpStatus status) {
		return SimpleResponse
				.getFailResponse(ValidationUtil.getHttpErrorMaessage(status, StringUtil.getErrorMessage(ex)));
	}
}