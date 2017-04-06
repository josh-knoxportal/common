package com.nemustech.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.model.SimpleResponse;
import com.nemustech.web.util.ValidationUtil;

/**
 * @author skoh
 */
@Controller
public abstract class CommonController2<T extends Default> extends CommonController<T> {
	@Override
	protected <T> Response<T> getSuccessResponse(T body) {
		return SimpleResponse.getSuccessResponse(body);
	}

	@Override
	protected Response<T> getFailResponse(Exception e) {
		return SimpleResponse.getFailResponse(ValidationUtil.getHttpErrorMaessage(HttpStatus.INTERNAL_SERVER_ERROR,
				StringUtils.substringBefore(e.getMessage().trim(), System.lineSeparator())));
	}
}
