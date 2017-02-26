package com.nemustech.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.Group;
import com.nemustech.sample.service.GroupService;
import com.nemustech.web.controller.CommonController;

@Controller
@RequestMapping(value = "group", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController extends CommonController<Group> {
	@Autowired
	protected GroupService service;

	@Override
	public CommonService<Group> getService() {
		return service;
	}
}
