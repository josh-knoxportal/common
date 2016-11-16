package com.nemustech.sample.controller;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.SampleAndTest2;
import com.nemustech.sample.service.SampleAndTest2Service;
import com.nemustech.web.controller.CommonController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sample_and_test2", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleAndTest2Controller extends CommonController<SampleAndTest2> {
	@Autowired
	protected SampleAndTest2Service service;

	@Override
	public CommonService<SampleAndTest2> getService() {
		return service;
	}
}
