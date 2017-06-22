/**
 * 
 */
package com.nemustech.platform.lbs.web.danger.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.nemustech.platform.lbs.common.aop.PageTitle;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.web.vehicle.service.AdminAccountApiService;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountVo;

/**
 * Handles requests for the application home page.
 *
 */
@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT, produces = MediaType.APPLICATION_JSON_VALUE
		+ ";charset=utf-8;")
public class AccountDangerController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(AccountDangerController.class);

	@Autowired
	private AdminAccountApiService adminAccountApiService;

	@PageTitle("사용자 정보")
	@RequestMapping(value = "/admin/accountInfo.do", method = RequestMethod.GET)
	public String userAccountInfo(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		getAccountInfoByUid(request, model);
		return "danger/admin/account/accountInfo";
	}

	@PageTitle("사용자 정보")
	@RequestMapping(value = "/admin/accountInfoModify.do", method = RequestMethod.GET)
	public String userAccountInfoEdit(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/admin/account/accountEdit";
	}

	@PageTitle("사용자 계정관리")
	@RequestMapping(value = "/sadmin/userAccount.do", method = RequestMethod.GET)
	public String userAccountList(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/sadmin/user_account/userAccountList";
	}

	@PageTitle("관리자 정보")
	@RequestMapping(value = "/sadmin/accountInfo.do", method = RequestMethod.GET)
	public String superAdminAccountInfo(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		getAccountInfoByUid(request, model);
		return "danger/sadmin/account/accountInfo";
	}

	@PageTitle("관리자 정보")
	@RequestMapping(value = "/sadmin/accountInfoModify.do", method = RequestMethod.GET)
	public String superAdminAccountInfoEdit(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/sadmin/account/accountEdit";
	}

	private void getAccountInfoByUid(HttpServletRequest request, Model model) {
		ResponseLoginVo account = null;
		AdminAccountVo adminAccount = null;
		String account_uid = null;
		int system_type = 0;

		account = (ResponseLoginVo) WebUtils.getSessionAttribute(request, "account");
		if (account != null) {
			account_uid = account.getAccount_uid();
			system_type = Integer.parseInt(account.getSystem_type());
			try {
				logger.info("account_uid:" + account_uid);
				adminAccount = new AdminAccountVo();
				adminAccount.setAccount_uid(account_uid);
				adminAccount.setSystem_type(system_type);
				adminAccount = adminAccountApiService.selectAdminAccount(adminAccount);

			} catch (Exception e) {
			}
		}

		model.addAttribute("account", adminAccount);
	}
}