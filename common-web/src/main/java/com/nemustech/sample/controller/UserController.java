package com.nemustech.sample.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.User;
import com.nemustech.web.controller.CommonController;

@Controller
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends CommonController<User> {
	@Override
	public CommonService<User> getService() {
		return service;
	}
}
