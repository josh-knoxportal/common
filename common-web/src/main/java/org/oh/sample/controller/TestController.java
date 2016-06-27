package org.oh.sample.controller;

import org.oh.sample.model.Test;
import org.oh.sample.service.TestService;
import org.oh.web.controller.CommonController;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "test")
public class TestController extends CommonController<Test> {
	@Autowired
	protected TestService service;

	@Override
	public CommonService<Test> getService() {
		return service;
	}
}
