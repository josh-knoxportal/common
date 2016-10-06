package org.oh.web.controller;

import org.oh.common.file.Files;
import org.oh.web.service.CommonService;
import org.oh.web.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "files", produces = MediaType.APPLICATION_JSON_VALUE)
public class FilesController extends CommonController<Files> {
	@Autowired
	protected FilesService service;

	@Override
	public CommonService<Files> getService() {
		return service;
	}
}
