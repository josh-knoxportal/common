package com.nemustech.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.JoinHandler;
import org.mybatisorm.annotation.handler.TableHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nemustech.common.exception.CommonException;
import com.nemustech.common.model.Common;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.model.ValidList;
import com.nemustech.common.page.PageNavigator;
import com.nemustech.common.page.Paging;
import com.nemustech.common.service.CommonService;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.ORMUtil;
import com.nemustech.common.util.StringUtil;
import com.nemustech.web.util.ValidationUtil;

/**
 * 공통 컨트롤러
 * 
 * <pre>
 * - 조회
 * 1. [/model/{id}],methods=[GET]
 * . [/model],methods=[GET]
 * . [/model/count],methods=[GET]
 * . [/model/page],methods=[GET]
 * . [/model/mapper],methods=[GET]
 * 
 * - 생성
 * . [/model],methods=[POST]
 * 
 * - 수정
 * . [/model],methods=[PUT]
 * 
 * - 삭제
 * 8. [/model],methods=[DELETE]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 */
@Controller
public abstract class CommonController<T extends Default> {
	protected Log log = LogFactory.getLog(getClass());

	protected CommonService<T> service;

	/**
	 * 서비스
	 * 
	 * @return
	 */
	public abstract CommonService<T> getService();

	@PostConstruct
	public void initCommon_() throws Exception {
		service = getService();
	}

	public ResponseEntity<Response<T>> get(T model) throws Exception {
		return get(model, null);
	}

	public ResponseEntity<Response<T>> get(T model, BindingResult errors) throws Exception {
		return get(model, null, errors);
	}

	/**
	 * 단일 모델 조회
	 * 
	 * @param model
	 * @param common
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = { RequestMethod.GET })
	// 1. Common 파라미터는 GET 방식의 보안을 위해 사용
	// 2. BindingResult 인자는 반드시 @Valid 로 선언한 인자의 바로 뒤에 와야 함
	public ResponseEntity<Response<T>> get(T model, @Valid Common common, BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		model = service.get(model);
		Response<T> response = getSuccessResponse(model);

		return new ResponseEntity<Response<T>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<List<T>>> list(T model) throws Exception {
		return list(model, null);
	}

	public ResponseEntity<Response<List<T>>> list(T model, BindingResult errors) throws Exception {
		return list(model, null, errors);
	}

	/**
	 * 복수 모델 조회
	 * 
	 * @param model
	 * @param common
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.GET })
	public ResponseEntity<Response<List<T>>> list(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<T> list = service.list(model);
		Response<List<T>> response = getSuccessResponse(list);

		return new ResponseEntity<Response<List<T>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<Integer>> count(T model) throws Exception {
		return count(model, null);
	}

	public ResponseEntity<Response<Integer>> count(T model, BindingResult errors) throws Exception {
		return count(model, null, errors);
	}

	/**
	 * 모델 갯수 조회
	 * 
	 * @param model
	 * @param common
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "count", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int count = service.count(model);
		Response<Integer> response = getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<PageNavigator<T>>> page(T model) throws Exception {
		return page(model, (BindingResult) null);
	}

	public ResponseEntity<Response<PageNavigator<T>>> page(T model, BindingResult errors) throws Exception {
		return page(model, (Common) null, errors);
	}

	/**
	 * 모델 페이징 조회
	 * 
	 * @param model
	 * @param common
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "page", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		if (!(model instanceof Paging))
			throw new CommonException("Model type [" + model.getClass().getName() + "] not instance of Paging class.");

		Paging paging = (Paging) model;
		List<T> list = service.page(paging);
		int count = service.count((T) model);
		paging.setTotal_sise(count);
		Response<PageNavigator<T>> response = getSuccessResponse(PageNavigator.getInstance(paging, list));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	@Deprecated
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page) throws Exception {
		return page(model, page, null);
	}

	@Deprecated
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, BindingResult errors)
			throws Exception {
		return page(model, page, null, errors);
	}

	/**
	 * 모델 페이징 조회2
	 * 
	 * @param model
	 * @param page
	 * @param common
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@Deprecated
//	@RequestMapping(value = "page2" + POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, @Valid Common common,
			BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		page = service.page(model, page);
		Response<PageNavigator<T>> response = getSuccessResponse(PageNavigator.getInstance(page));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<List<Object>>> inserts(List<T> models) throws Exception {
		return inserts(models, null);
	}

	public ResponseEntity<Response<List<Object>>> inserts(List<T> models, BindingResult errors) throws Exception {
		return inserts(new ValidList<T>(models), errors);
	}

	/**
	 * 복수 모델 등록
	 * Content-Type : application/json
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Response<List<Object>>> inserts(@Valid @RequestBody ValidList<T> models, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Object> result = service.insert(models);
		Response<List<Object>> response = getSuccessResponse(result);

		return new ResponseEntity<Response<List<Object>>>(response, HttpStatus.OK);
	}

	/**
	 * 복수 모델 수정
	 * Content-Type : application/json
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updates(@RequestBody List<T> models, BindingResult errors)
			throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.update(models);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * 단일 모델 삭제
	 * Content-Type : application/x-www-form-urlencoded
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete(T model, BindingResult errors) throws Exception {
		if (errors != null && errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.delete(model);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * CRUD 쿼리 생성
	 * 
	 * @param model
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "mapper", method = { RequestMethod.GET })
	public ModelAndView mapper(T model, ModelAndView mav) throws Exception {
		Class<? extends Default> clazz = model.getClass();
		TableHandler handler = HandlerFactory.getHandler(clazz);

		// 클래스
		String package_ = StringUtil.replace(clazz.getPackage().getName(), ".model", ".mapper");
		String className = clazz.getSimpleName();
		if (className.endsWith("Vo"))
			className = className.substring(0, className.length() - 2);
		mav.addObject("className", className);
//		mav.addObject("namespace",
//				package_.substring(0, package_.lastIndexOf('.')) + ".mapper." + className + "Mapper");
		mav.addObject("namespace", package_ + "." + className + "Mapper");

		// 필드
		String fields = handler.getColumnAsFieldComma();
		mav.addObject("fields", fields);

		// 테이블
		String table = handler.getName();
		mav.addObject("table", table);

		// 컬럼 리스트 (WHERE)
		List<KeyValue> columnList = new ArrayList<KeyValue>();
		String[] columnArray = StringUtil.split(fields, ',');
		for (String column : columnArray) {
			String[] field = StringUtil.split(column, ' ');
			if (handler instanceof JoinHandler) {
				int index = field[1].indexOf("_");
				field[1] = field[1].substring(0, index) + "." + field[1].substring(index + 1);
			}
			columnList.add(new DefaultKeyValue(field[0], field[1]));
		}
		mav.addObject("columnList", columnList);

		// Sequence
		boolean isOracle = CommonService.SOURCE_TYPE_ORACLE.equalsIgnoreCase(service.getSourceType());
		KeyValue sequence = (isOracle) ? ORMUtil.getSequence(clazz) : null;
		mav.addObject("sequence", sequence);

		// AutoIncrement
		String autoIncrement = ORMUtil.getAutoIncrement(clazz);
		mav.addObject("autoIncrement", autoIncrement);

		// 신규 컬럼 리스트 (INSERT)
		List<KeyValue> createColumnList = (sequence == null) ? ORMUtil.getCreateColumnList(clazz, true)
				: ORMUtil.getCreateColumnList(clazz);
		mav.addObject("createColumnList", createColumnList);

		// 수정 컬럼 리스트 (UPDATE)
		List<KeyValue> updatgeColumnList = columnList = ORMUtil.getUpdateColumnList(clazz);
		mav.addObject("updatgeColumnList", updatgeColumnList);

		// PrimaryKey
		KeyValue primaryKey = ORMUtil.getPrimaryKey(clazz);
		mav.addObject("primaryKey", primaryKey);

		// 템플릿 매퍼
		mav.setViewName("templateMapper");
		log.debug("model: " + JsonUtil2.toStringPretty(mav.getModel()));

		return mav;
	}

	/**
	 * 유효성 체크
	 * 
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	protected ResponseEntity<Response<T>> checkValidate(BindingResult errors) throws Exception {
		log.error("Validate errors: " + errors.getFieldErrors());

		Response<T> response = ValidationUtil.getFailResponse(errors);

		return new ResponseEntity<Response<T>>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 예외 처리
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<Object>> handleException(Exception ex) {
		log.error(ValidationUtil.getHttpErrorCodeMaessage(HttpStatus.INTERNAL_SERVER_ERROR), ex);

		Response<Object> response = getFailResponse(ex);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 성공 응답
	 * 
	 * @param body
	 * @return
	 */
	protected <U> Response<U> getSuccessResponse(U body) {
		return Response.getSuccessResponse(body);
	}

	/**
	 * 실패 응답
	 * 
	 * @param ex
	 * @return
	 */
	protected Response<Object> getFailResponse(Exception ex) {
		return Response.getFailResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
