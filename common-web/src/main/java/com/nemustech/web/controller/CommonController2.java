package com.nemustech.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;

/**
 * 공통 컨트롤러2
 * 
 * <pre>
 * - 생성
 * . [/model/one],methods=[POST]
 * . [/model/form],methods=[POST]
 * 
 * - 수정
 * . [/model/one],methods=[PUT]
 * . [/model/form],methods=[PUT]
 * 
 * - 삭제
 * . [/model/one],methods=[POST]
 * . [/model/many],methods=[POST]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 */
@Controller
public abstract class CommonController2<T extends Default> extends CommonController<T> {
	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "one", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insertJson(@Valid @RequestBody T model, BindingResult errors)
			throws Exception {
		return insert(model, errors);
	}

	/**
	 * 단일 모델 등록
	 * Content-Type : application/x-www-form-urlencoded, multipart/form-data
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "form", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert(@Valid T model, BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		Object result = service.insert(model);
		Response<Object> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "one", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updateJson(@RequestBody T model, BindingResult errors) throws Exception {
		return update(model, errors);
	}

	/**
	 * 단일 모델 수정
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "form", method = { RequestMethod.PUT })
	public ResponseEntity<Response<Integer>> update(T model, BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.update(model);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "one", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> deleteJson(@RequestBody T model, BindingResult errors) throws Exception {
		return delete(model, errors);
	}

	/**
	 * 복수 모델 삭제
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "many", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> delete(@RequestBody List<T> models, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.delete(models);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}
}
