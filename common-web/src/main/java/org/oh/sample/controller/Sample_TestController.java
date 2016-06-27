package org.oh.sample.controller;

import org.oh.sample.model.Sample_Test;
import org.oh.sample.service.Sample_TestService;
import org.oh.web.controller.CommonController;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sample_test", produces = MediaType.APPLICATION_JSON_VALUE)
public class Sample_TestController extends CommonController<Sample_Test> {
	@Autowired
	protected Sample_TestService service;

	@Override
	public CommonService<Sample_Test> getService() {
		return service;
	}
}
