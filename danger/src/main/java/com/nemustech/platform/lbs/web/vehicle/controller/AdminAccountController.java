/**
 * 
 */
package com.nemustech.platform.lbs.web.vehicle.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.nemustech.platform.lbs.common.aop.PageTitle;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.util.SessionUtil;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;

/**
 * Handles requests for the application home page.
 *
 */
@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_VEHICLE_ROOT+"/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class AdminAccountController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(AdminAccountController.class);

	@PageTitle("계정관리")
	@RequestMapping(value = "sadmin/account.do", method = RequestMethod.GET)
	public String saccount(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		int isAdmin = 0;

		ResponseLoginVo account = SessionUtil.getAccountInfo();
		if (account != null) {
			isAdmin = account.getIs_admin();
		}

		if (isAdmin == 1) {
			return "vehicle/sadmin/account/accountList";
		}

		return "forward:/vehicle/sadmin/accountDetail.do";
	}
	
	@PageTitle("계정관리")
	@RequestMapping(value = "admin/account.do", method = RequestMethod.GET)
	public String account(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		int isAdmin = 0;

		ResponseLoginVo account = SessionUtil.getAccountInfo();
		if (account != null) {
			isAdmin = account.getIs_admin();
		}

		if (isAdmin == 1) {
			return "vehicle/admin/account/accountList";
		}

		return "forward:/vehicle/admin/accountDetail.do";
	}

	@PageTitle("계정관리")
	@RequestMapping(value = "admin/accountDetail.do", method = RequestMethod.GET)
	public String accountDetail(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		String account_uid = null;

		ResponseLoginVo account = (ResponseLoginVo) WebUtils.getSessionAttribute(request,
				DefaultController.SESSION_ATTR_KEY_ACCOUNT);
		if (account != null) {
			account_uid = account.getAccount_uid();
		}

		model.addAttribute("account_uid", account_uid);
		return "vehicle/admin/account/accountDetail";
	}
	
	@PageTitle("계정관리")
	@RequestMapping(value = "sadmin/accountDetail.do", method = RequestMethod.GET)
	public String saccountDetail(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		String account_uid = null;

		ResponseLoginVo account = (ResponseLoginVo) WebUtils.getSessionAttribute(request,
				DefaultController.SESSION_ATTR_KEY_ACCOUNT);
		if (account != null) {
			account_uid = account.getAccount_uid();
		}

		model.addAttribute("account_uid", account_uid);
		return "vehicle/sadmin/account/accountDetail";
	}

	@PageTitle("계정관리")
	@RequestMapping(value = "admin/accountModify.do", method = RequestMethod.GET)
	public String accountEdit(HttpServletRequest request, Model model,
			@RequestParam(value = "account_uid", required = true) String account_uid) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);
		logger.info("account_uid: " + account_uid);

		model.addAttribute("account_uid", account_uid);
		return "vehicle/admin/account/accountEdit";
	}
	
	@PageTitle("계정관리")
	@RequestMapping(value = "sadmin/accountModify.do", method = RequestMethod.GET)
	public String saccountEdit(HttpServletRequest request, Model model,
			@RequestParam(value = "account_uid", required = true) String account_uid) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);
		logger.info("account_uid: " + account_uid);

		model.addAttribute("account_uid", account_uid);
		return "vehicle/sadmin/account/accountEdit";
	}

	@PageTitle("계정관리")
	@RequestMapping(value = "sadmin/accountRegister.do", method = RequestMethod.GET)
	public String accountRegister(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		int isAdmin = 0;
		int system_type = 0;

		ResponseLoginVo account = (ResponseLoginVo) WebUtils.getSessionAttribute(request,
				DefaultController.SESSION_ATTR_KEY_ACCOUNT);
		if (account != null) {
			isAdmin = account.getIs_admin();
			system_type = Integer.parseInt(account.getSystem_type());
		}

		if (SystemType.VEHICLE.getValue() == system_type && isAdmin == 1) {
			return "vehicle/sadmin/account/accountReg";
		} else {
			return "redirect:/vehicle/admin/login.do";
		}
	}

}
