package com.nemustech.platform.lbs.web.danger.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindingResult;

import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.web.util.ValidationUtil;

public abstract class AbstractController<T extends Default> {
	protected Log log = LogFactory.getLog(getClass());

	protected void checkValidate(BindingResult errors) throws Exception {
		log.error("Validate errors: " + errors.getFieldErrors());

		Response<T> response = ValidationUtil.getFailResponse(errors);

		throw new Exception(response.getHeader().getError_message());
	}
}
