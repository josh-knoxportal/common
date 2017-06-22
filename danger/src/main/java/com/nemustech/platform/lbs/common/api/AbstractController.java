package com.nemustech.platform.lbs.common.api;

import org.springframework.http.HttpStatus;

import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.SimpleResponse2;
import com.nemustech.web.controller.CommonController;
import com.nemustech.web.util.ValidationUtil;

public abstract class AbstractController<T extends Default> extends CommonController<T> {
	@Override
	protected <U> Response<U> getSuccessResponse(U body) {
		return SimpleResponse2.getSuccessResponse(body);
	}

	@Override
	protected Response<T> getFailResponse(Exception e) {
		return SimpleResponse2.getFailResponse(
				ValidationUtil.getHttpErrorMaessage(HttpStatus.INTERNAL_SERVER_ERROR, StringUtil.getErrorMessage(e)));
	}
}
