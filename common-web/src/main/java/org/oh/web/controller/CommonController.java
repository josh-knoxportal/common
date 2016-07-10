package org.oh.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.oh.web.common.Response;
import org.oh.web.model.Default;
import org.oh.web.page.PageNavigator;
import org.oh.web.service.CommonService;
import org.oh.web.util.ValidationUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public abstract class CommonController<T extends Default> implements InitializingBean {
	protected Log log = LogFactory.getLog(getClass());

//	@Resource(name = "commonService")
	private CommonService<T> service;

	public abstract CommonService<T> getService();

	public void setService(CommonService<T> service) {
		this.service = service;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		service = getService();
	}

	@RequestMapping(value = "get.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<T>> get(T model, BindingResult errors) throws Exception {
		model = service.get(model);
		Response<T> response = Response.getSuccessResponse(model);

		return new ResponseEntity<Response<T>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "list.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<T>>> list(T model, BindingResult errors) throws Exception {
		List<T> list = service.list(model);
		Response<List<T>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<T>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "count.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count(T model, BindingResult errors) throws Exception {
		int count = service.count(model);
		Response<Integer> response = Response.getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "page.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, BindingResult errors)
			throws Exception {
		page = service.page(model, page);
		Response<PageNavigator<T>> response = Response.getSuccessResponse(PageNavigator.getInstance(page));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "insert_json.do", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> insert_json(@RequestBody @Valid T model, BindingResult errors)
			throws Exception {
		return insert(model, errors);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * 
	 * @return ResponseEntity
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "insert.do", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> insert(@Valid T model, BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return checkValidate(errors);
		}

		int result = service.insert(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "update_json.do", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updateJson(@RequestBody T model, BindingResult errors) throws Exception {
		return update(model, errors);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * 
	 * @return ResponseEntity
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "update.do", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> update(T model, BindingResult errors) throws Exception {
		int result = service.update(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * 
	 * @return ResponseEntity
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_json.do", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete_json(@RequestBody T model, BindingResult errors) throws Exception {
		return delete(model, errors);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "delete.do", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete(T model, BindingResult errors) throws Exception {
		int result = service.delete(model);
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

	protected ResponseEntity<Response<Integer>> checkValidate(BindingResult errors) throws Exception {
		log.error("field errors: " + errors.getFieldErrors());
		Response<Integer> response = ValidationUtil.getResponse(errors);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.BAD_REQUEST);
	}
}
