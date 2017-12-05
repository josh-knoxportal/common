package com.nemustech.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nemustech.common.model.Common;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.model.SimpleResponse;
import com.nemustech.common.model.ValidList;
import com.nemustech.common.page.PageNavigator;
import com.nemustech.common.util.StringUtil;
import com.nemustech.web.util.ValidationUtil;

/**
 * 공통2 Controller
 * 
 * @author skoh
 * @param <T>
 */
@Controller
public abstract class CommonController2<T extends Default> extends CommonController<T> {
	@Override
	protected <U> Response<U> getSuccessResponse(U body) {
		return SimpleResponse.getSuccessResponse(body);
	}

	@Override
	protected Response<T> getFailResponse(Exception e) {
		return SimpleResponse.getFailResponse(
				ValidationUtil.getHttpErrorMaessage(HttpStatus.INTERNAL_SERVER_ERROR, StringUtil.getErrorMessage(e)));
	}

	@Override
	@RequestMapping(method = { RequestMethod.GET })
	public ResponseEntity<Response<List<T>>> list(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		return super.list(model, common, errors);
	}

	@Override
	@RequestMapping(value = "count", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		return super.count(model, common, errors);
	}

	@Override
	@RequestMapping(value = "page", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		return super.page(model, common, errors);
	}

	@Override
	@RequestMapping(value = "file", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert(@Valid T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		return super.insert(model, errors, request);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Response<List<Object>>> inserts(@Valid @RequestBody ValidList<T> models, BindingResult errors,
			HttpServletRequest request) throws Exception {
		return super.inserts(models, errors, request);
	}

	@Override
	@RequestMapping(value = "update", method = { RequestMethod.POST })
	public ResponseEntity<Response<Integer>> update(T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		return update(model, errors, request);
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updates(@RequestBody List<T> models, BindingResult errors,
			HttpServletRequest request) throws Exception {
		return super.updates(models, errors, request);
	}

	@Override
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete(T model, BindingResult errors) throws Exception {
		return super.delete(model, errors);
	}

	@Override
	@RequestMapping(value = "mapper", method = { RequestMethod.GET })
	public ModelAndView mapper(T model, ModelAndView mav) throws Exception {
		return super.mapper(model, mav);
	}

	@Override
	@RequestMapping(value = "select", method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Map<String, Object>>>> select(Common model, BindingResult errors)
			throws Exception {
		return super.select(model, errors);
	}
}
