package com.nemustech.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.Test;
import com.nemustech.sample.service.TestService;
import com.nemustech.web.controller.CommonController;

@Controller
@RequestMapping(value = "test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController extends CommonController<Test> {
	@Autowired
	protected TestService service;

	@Override
	public CommonService<Test> getService() {
		return service;
	}
}
