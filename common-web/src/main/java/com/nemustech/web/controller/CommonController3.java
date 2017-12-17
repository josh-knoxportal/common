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
 * 공통 컨트롤러3
 * 
 * <pre>
 * - 조회
 * . [/model/get.do],methods=[GET]
 * . [/model/list.do],methods=[GET]
 * . [/model/count.do],methods=[GET]
 * . [/model/page.do],methods=[GET]
 * . [/model/mapper.do],methods=[GET]
 * 
 * - 생성
 * . [/model/insert.do],methods=[POST]
 * . [/model/insert_form.do],methods=[POST]
 * . [/model/inserts.do],methods=[POST]
 * 
 * - 수정
 * . [/model/update.do],methods=[PUT]
 * . [/model/update_form.do],methods=[PUT]
 * . [/model/updates.do],methods=[PUT]
 * 
 * - 삭제
 * . [/model/delete_json.do],methods=[POST]
 * . [/model/delete.do],methods=[DELETE]
 * . [/model/deletes.do],methods=[POST]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 */
@Controller
public abstract class CommonController3<T extends Default> extends CommonController2<T> {
	/**
	 * @RequestMapping value 의 접미어
	 */
	public static final String POSTFIX = ".do";

	@Override
	protected <U> Response<U> getSuccessResponse(U body) {
		return SimpleResponse.getSuccessResponse(body);
	}

	@Override
	protected Response<Object> getFailResponse(Exception ex) {
		return SimpleResponse.getFailResponse(
				ValidationUtil.getHttpErrorMaessage(HttpStatus.INTERNAL_SERVER_ERROR, StringUtil.getErrorMessage(ex)));
	}

	@Override
	@RequestMapping(value = "get" + POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<T>> get(T model, @Valid Common common, BindingResult errors) throws Exception {
		return super.get(model, common, errors);
	}

	@Override
	@RequestMapping(value = "list" + POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<List<T>>> list(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		return super.list(model, common, errors);
	}

	@Override
	@RequestMapping(value = "count" + POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		return super.count(model, common, errors);
	}

	@Override
	@RequestMapping(value = "page" + POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		return super.page(model, common, errors);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "insert" + POSTFIX, method = RequestMethod.POST)
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
	@RequestMapping(value = "insert_form" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert(@Valid T model, BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		Object result = service.insert(model);
		Response<Object> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "inserts" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<List<Object>>> inserts(@Valid @RequestBody ValidList<T> models, BindingResult errors)
			throws Exception {
		return super.inserts(models, errors);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "update" + POSTFIX, method = RequestMethod.PUT)
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
	@RequestMapping(value = "update_form" + POSTFIX, method = { RequestMethod.PUT })
	public ResponseEntity<Response<Integer>> update(T model, BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.update(model);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "updates" + POSTFIX, method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updates(@RequestBody List<T> models, BindingResult errors)
			throws Exception {
		return super.updates(models, errors);
	}

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_json" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> deleteJson(@RequestBody T model, BindingResult errors) throws Exception {
		return delete(model, errors);
	}

	@Override
	@RequestMapping(value = "delete" + POSTFIX, method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete(T model, BindingResult errors) throws Exception {
		return super.delete(model, errors);
	}

	/**
	 * 복수 모델 삭제
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "deletes" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> delete(@RequestBody List<T> models, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.delete(models);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "mapper" + POSTFIX, method = { RequestMethod.GET })
	public ModelAndView mapper(T model, ModelAndView mav) throws Exception {
		return super.mapper(model, mav);
	}
}
