package com.nemustech.platform.lbs.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/", produces = "application/json;charset=utf8;")
public class AuthErrorController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthErrorController.class);
	
	@RequestMapping(value = "/auth_error.do", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> authError() throws NotFoundException {
		logger.info("GET Auth Error!!!!!!!");
		
		HttpStatus statusCode = HttpStatus.UNAUTHORIZED;
		return new ResponseEntity<String>(statusCode);
	}
	
	
	@RequestMapping(value = "/auth_403.do", method = RequestMethod.GET)
	public String error403() {
		return "err/403";
	}
	@RequestMapping(value = "/auth_503.do", method = RequestMethod.GET)
	public String error503() {
		return "err/503";
	}
	
	
}
