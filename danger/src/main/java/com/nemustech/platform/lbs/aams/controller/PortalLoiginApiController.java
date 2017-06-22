package com.nemustech.platform.lbs.aams.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.platform.lbs.aams.service.PortalLoginApiService;
import com.nemustech.platform.lbs.common.vo.PortalLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;

@Controller
public class PortalLoiginApiController {

	@Autowired
	private PortalLoginApiService portalLoginApiService;

	@RequestMapping(value = "/login/portalLogin.do", method = RequestMethod.GET)
	public String dangerPortalLinkLogin(HttpServletRequest request, PortalLoginVo portalLoginVo) throws Exception {
		return portalLoginApiService.processPortalLogin(request, SystemType.DANGER.toString(), portalLoginVo);
	}

	@RequestMapping(value = "/portal/portalLogin.do", method = RequestMethod.GET)
	public String moveDangerPortalLoginErrorPage(HttpServletRequest request, Model model) throws Exception {
		return "danger/portal/loginError";
	}
}
