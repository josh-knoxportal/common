package com.nemustech.sample.controller;

import com.nemustech.sample.model.SampleAndFiles;
import com.nemustech.sample.service.SampleAndFilesService;
import com.nemustech.web.controller.CommonController;
import com.nemustech.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sample_and_files", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleAndFilesController extends CommonController<SampleAndFiles> {
	@Autowired
	protected SampleAndFilesService service;

	@Override
	public CommonService<SampleAndFiles> getService() {
		return service;
	}
}
