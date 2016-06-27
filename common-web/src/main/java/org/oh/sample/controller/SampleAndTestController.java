package org.oh.sample.controller;

import org.oh.sample.model.SampleAndTest;
import org.oh.sample.service.SampleAndTestService;
import org.oh.web.controller.CommonController;
import org.oh.web.service.CommonService;
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
