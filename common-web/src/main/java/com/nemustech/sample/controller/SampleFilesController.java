package com.nemustech.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nemustech.common.service.CommonFilesService;
import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.service.SampleFilesService;
import com.nemustech.sample.service.SampleService;
import com.nemustech.web.controller.CommonFilesController2;

@Controller
@RequestMapping(value = "sample2", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleFilesController extends CommonFilesController2<Sample, Files2> {
	@Autowired
	protected SampleService service;

	@Autowired
	protected SampleFilesService filesService;

	@Override
	public CommonService<Sample> getService() {
		return service;
	}

	@Override
	public CommonFilesService<Sample, Files2> getCommonFilesService() {
		return filesService;
	}
}
