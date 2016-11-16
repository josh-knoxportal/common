package com.nemustech.sample.controller;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.SampleAndTest;
import com.nemustech.sample.service.SampleAndTestService;
import com.nemustech.web.controller.CommonController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sample_and_test", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleAndTestController extends CommonController<SampleAndTest> {
	@Autowired
	protected SampleAndTestService service;

	@Override
	public CommonService<SampleAndTest> getService() {
		return service;
	}
}
