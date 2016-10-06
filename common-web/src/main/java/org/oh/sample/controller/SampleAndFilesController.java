package org.oh.sample.controller;

import org.oh.sample.model.SampleAndFiles;
import org.oh.sample.service.SampleAndFilesService;
import org.oh.web.controller.CommonController;
import org.oh.web.service.CommonService;
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
