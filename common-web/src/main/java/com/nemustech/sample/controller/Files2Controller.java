package com.nemustech.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.service.Files2Service;
import com.nemustech.web.controller.CommonController;

@Controller
@RequestMapping(value = "files2", produces = MediaType.APPLICATION_JSON_VALUE)
public class Files2Controller extends CommonController<Files2> {
	@Autowired
	protected Files2Service service;

	@Override
	public CommonService<Files2> getService() {
		return service;
	}
}
