package com.nemustech.platform.lbs.web.danger.controller;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.common.model.Common;
import com.nemustech.platform.lbs.common.aop.PageTitle;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.ServerConfig;
import com.nemustech.platform.lbs.wwms.service.ServerConfigService;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT, produces = MediaType.APPLICATION_JSON_VALUE
		+ ";charset=utf-8;")
public class ServerConfigController extends AbstractController<ServerConfig> {
	@Autowired
	protected ServletContext context;

	@Autowired
	protected ServerConfigService service;

	@PageTitle("서버 설정")
	@RequestMapping(value = "/sadmin/serverConfig.do", method = RequestMethod.GET)
	public String get(ServerConfig model, @Valid Common common, BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			checkValidate(errors);
		}

		ServerConfig serverConfig = service.get(model);
		context.setAttribute("g_server_config", serverConfig);

		return "danger/sadmin/config/serverConfig";
	}
}
