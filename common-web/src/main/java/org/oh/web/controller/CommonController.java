package org.oh.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.mybatisorm.annotation.Table;
import org.oh.web.Constants;
import org.oh.web.common.Response;
import org.oh.web.model.Common;
import org.oh.web.model.Default;
import org.oh.web.model.ValidList;
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

	public ResponseEntity<Response<T>> get(T model, BindingResult errors) throws Exception {
		return get(model, null, errors);
	}

	// Common 파라미터는 GET 방식의 보안을 위해 사용
	@RequestMapping(value = "get" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<T>> get(T model, @Valid Common common, BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		model = service.get(model);
		Response<T> response = Response.getSuccessResponse(model);

		return new ResponseEntity<Response<T>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<List<T>>> list(T model, BindingResult errors) throws Exception {
		return list(model, null, errors);
	}

	@RequestMapping(value = "list" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<List<T>>> list(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<T> list = service.list(model);
		Response<List<T>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<T>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<Integer>> count(T model, BindingResult errors) throws Exception {
		return count(model, null, errors);
	}

	@RequestMapping(value = "count" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int count = service.count(model);
		Response<Integer> response = Response.getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, BindingResult errors)
			throws Exception {
		return page(model, page, null, errors);
	}

	@RequestMapping(value = "page" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, @Valid Common common,
			BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		page = service.page(model, page);
		Response<PageNavigator<T>> response = Response.getSuccessResponse(PageNavigator.getInstance(page));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "select" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<List<Map<String, Object>>>> select(Common model) throws Exception {
		List<Map<String, Object>> list = service.select(new ModelMap(), model.newCondition().add(model.getCondition()),
				model.getOrder_by(), model.getHint(), model.getFields(), model.getTable(), "select_");
		Response<List<Map<String, Object>>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<Map<String, Object>>>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "insert_json" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Long>> insertJson(@Valid @RequestBody T model, BindingResult errors)
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
	@RequestMapping(value = "insert" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Long>> insert(@Valid T model, BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		long result = service.insert(model);
		Response<Long> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Long>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<List<Long>>> inserts(List<T> models, BindingResult errors) throws Exception {
		return inserts(new ValidList<T>(models), null);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "inserts_json" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<List<Long>>> inserts(@Valid @RequestBody ValidList<T> models, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Long> result = service.insert(models);
		Response<List<Long>> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<List<Long>>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "update_json" + Constants.POSTFIX, method = RequestMethod.PUT)
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
	@RequestMapping(value = "update" + Constants.POSTFIX, method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> update(T model, BindingResult errors) throws Exception {
		int result = service.update(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * 
	 * @return ResponseEntity
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_json" + Constants.POSTFIX, method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> deleteJson(@RequestBody T model, BindingResult errors) throws Exception {
		return delete(model, errors);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded
	 */
	@RequestMapping(value = "delete" + Constants.POSTFIX, method = RequestMethod.DELETE)
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

	protected ResponseEntity<Response<T>> checkValidate(BindingResult errors) throws Exception {
		log.error("field errors: " + errors.getFieldErrors());
		Response<T> response = ValidationUtil.getResponse(errors);

		return new ResponseEntity<Response<T>>(response, HttpStatus.BAD_REQUEST);
	}

	@Table
	protected static class ModelMap extends LinkedHashMap<String, Object> {
	}
}
