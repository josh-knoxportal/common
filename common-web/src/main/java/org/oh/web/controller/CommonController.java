package org.oh.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.oh.web.common.Response;
import org.oh.web.model.Default;
import org.oh.web.page.PageNavigator;
import org.oh.web.service.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommonController<T extends Default> {
	protected Log log = LogFactory.getLog(getClass());

	@Resource(name = "commonService")
	protected CommonService<T> commonService;

	@RequestMapping(value = "get.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<T>> get(T model, BindingResult bindingResult) throws Exception {
		model = commonService.get(model);
		Response<T> response = Response.getSuccessResponse(model);

		return new ResponseEntity<Response<T>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "list.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<T>>> list(T model, BindingResult bindingResult) throws Exception {
		List<T> list = commonService.list(model);
		Response<List<T>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<T>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "count.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count(T model, BindingResult bindingResult) throws Exception {
		int count = commonService.count(model);
		Response<Integer> response = Response.getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "page.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, BindingResult bindingResult)
			throws Exception {
		page = commonService.page(model, page);
		Response<PageNavigator<T>> response = Response.getSuccessResponse(PageNavigator.getInstance(page));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "insert.do", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> insert(T model, BindingResult bindingResult) throws Exception {
		int result = commonService.insert(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "update.do", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> update(T model, BindingResult bindingResult) throws Exception {
		int result = commonService.update(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "delete.do", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete(T model, BindingResult bindingResult) throws Exception {
		int result = commonService.delete(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<T>> handleException(Exception e) {
		String error_code = HttpStatus.INTERNAL_SERVER_ERROR + " " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		log.error(error_code, e);
		Response<T> response = Response.getFailResponse(error_code, e.getMessage());

		return new ResponseEntity<Response<T>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
