package com.nemustech.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.TableHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.nemustech.common.file.Files;
import com.nemustech.common.model.Common;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.model.ValidList;
import com.nemustech.common.page.PageNavigator;
import com.nemustech.common.page.Paging;
import com.nemustech.common.service.CommonService;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.ORMUtil;
import com.nemustech.common.util.Utils;
import com.nemustech.web.Constants;
import com.nemustech.web.util.ValidationUtil;

/**
 * https://github.com/skoh/common.git
 * 
 * @author skoh
 * @see <a href="https://github.com/wolfkang/mybatis-orm">https://github.com/wolfkang/mybatis-orm</a>
 */
@Controller
public abstract class CommonController<T extends Default> implements InitializingBean {
	protected Log log = LogFactory.getLog(getClass());

	protected CommonService<T> service;

	public abstract CommonService<T> getService();

	@Override
	public void afterPropertiesSet() throws Exception {
		service = getService();
	}

	public ResponseEntity<Response<T>> get(T model, BindingResult errors) throws Exception {
		return get(model, null, errors);
	}

	// 1. Common 파라미터는 GET 방식의 보안을 위해 사용
	// 2. BindingResult 인자는 반드시 @Valid 로 선언한 인자의 바로 뒤에 와야 함
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

	@RequestMapping(value = "/page" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(Paging model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<T> list = service.page(model);

		int count = service.count((T) model);
		model.setTotal_sise(count);

		Response<PageNavigator<T>> response = Response.getSuccessResponse(PageNavigator.getInstance(model, list));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, BindingResult errors)
			throws Exception {
		return page(model, page, null, errors);
	}

	@RequestMapping(value = "page2" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<T>>> page(T model, Page<T> page, @Valid Common common,
			BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		page = service.page(model, page);
		Response<PageNavigator<T>> response = Response.getSuccessResponse(PageNavigator.getInstance(page));

		return new ResponseEntity<Response<PageNavigator<T>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "select" + Constants.POSTFIX, method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Map<String, Object>>>> select(Common model, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Map<String, Object>> list = service.select(new ModelMap(), model.newCondition().add(model.getCondition()),
				model.getOrder_by(), model.getHint(), model.getFields(), model.getTable(), model.getGroup_by(),
				model.getHaving(), "select_");
		Response<List<Map<String, Object>>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<Map<String, Object>>>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "insert_json" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insertJson(@Valid @RequestBody T model, BindingResult errors)
			throws Exception {
		return insert(model, errors, null);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded, multipart/form-data
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return ResponseEntity
	 * @throws Exception
	 */
	// @RequestParam("file") MultipartFile[] files
	@RequestMapping(value = "insert" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert(@Valid T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Files> files = getFiles(model, request);
		Object result = service.insert(model, files);
		Response<Object> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response<List<Object>>> inserts(List<T> models, BindingResult errors) throws Exception {
		return inserts(new ValidList<T>(models), null);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "inserts_json" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<List<Object>>> inserts(@Valid @RequestBody ValidList<T> models, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Object> result = service.insert(models);
		Response<List<Object>> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<List<Object>>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "update_json" + Constants.POSTFIX, method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updateJson(@RequestBody T model, BindingResult errors) throws Exception {
		return update(model, errors, null);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded (PUT), multipart/form-data (POST)
	 * 
	 * @param model
	 * @param errors
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "update" + Constants.POSTFIX, method = { RequestMethod.PUT, RequestMethod.POST })
	public ResponseEntity<Response<Integer>> update(T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Files> files = getFiles(model, request);
		int result = service.update(model, files);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "updates_json" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> updates(@RequestBody List<T> models, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.update(models);
		Response<Integer> response = Response.getSuccessResponse(result);

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
	@RequestMapping(value = "delete_json" + Constants.POSTFIX, method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> deleteJson(@RequestBody T model, BindingResult errors) throws Exception {
		return delete(model, errors);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded
	 */
	@RequestMapping(value = "delete" + Constants.POSTFIX, method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete(T model, BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.delete(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 */
	@RequestMapping(value = "deletes_json" + Constants.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> delete(@RequestBody List<T> models, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int result = service.delete(models);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "mapper" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ModelAndView mapper(T model, ModelAndView mav) throws Exception {
		Class<? extends Default> clazz = model.getClass();
		TableHandler handler = HandlerFactory.getHandler(clazz);

		// 클래스
		String package_ = clazz.getPackage().getName();
		String className = clazz.getSimpleName();
		if (className.endsWith("Vo"))
			className = className.substring(0, className.length() - 2);
		mav.addObject("namespace",
				package_.substring(0, package_.lastIndexOf('.')) + ".mapper." + className + "Mapper");

		// 필드
		String fields = handler.getColumnAsFieldComma();
		mav.addObject("fields", fields);

		// 테이블
		String table = handler.getName();
		mav.addObject("table", table);

		// 컬럼 리스트 (WHERE)
		List<KeyValue> columnList = new ArrayList<KeyValue>();
		String[] columnArray = StringUtils.split(fields, ',');
		for (String column : columnArray) {
			String[] field = StringUtils.split(column, ' ');
			columnList.add(new DefaultKeyValue(field[0], field[1]));
		}
		mav.addObject("columnList", columnList);

		// Sequence
		KeyValue sequence = ORMUtil.getSequence(clazz);
		mav.addObject("sequence", sequence);

		// AutoIncrement
		String autoIncrement = ORMUtil.getAutoIncrement(clazz);
		mav.addObject("autoIncrement", autoIncrement);

		// 신규 컬럼 리스트 (INSERT)
		List<KeyValue> createColumnList = (sequence != null || autoIncrement == null)
				? ORMUtil.getCreateColumnList(clazz) : ORMUtil.getCreateColumnList(clazz, true);
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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<T>> handleException(Exception e) {
		log.error(ValidationUtil.getHttpErrorCodeMaessage(HttpStatus.INTERNAL_SERVER_ERROR), e);

		Response<T> response = Response.getFailResponse(
				ValidationUtil.getHttpErrorCode(HttpStatus.INTERNAL_SERVER_ERROR),
				ValidationUtil.getHttpErrorMaessage(HttpStatus.INTERNAL_SERVER_ERROR,
						StringUtils.substringBefore(e.getMessage().trim(), System.lineSeparator())));

		return new ResponseEntity<Response<T>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected ResponseEntity<Response<T>> checkValidate(BindingResult errors) throws Exception {
		log.error("Validate errors: " + errors.getFieldErrors());

		Response<T> response = ValidationUtil.getFailResponse(errors);

		return new ResponseEntity<Response<T>>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 요청에서 파일 리스트를 구한다.
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected List<Files> getFiles(T model, HttpServletRequest request) throws Exception {
		List<Files> filesList = new ArrayList<Files>();

		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = multipartRequest.getFileNames();

			List<MultipartFile> files = new ArrayList<MultipartFile>();
			while (fileNames.hasNext()) {
				files.addAll(multipartRequest.getFiles((String) fileNames.next()));
			}

			for (MultipartFile file : files) {
				if (!Utils.isValidate(file.getOriginalFilename()))
					continue;

				Files files_ = new Files(file.getOriginalFilename(), file.getBytes());
				if (model instanceof Common) {
					Common common = (Common) model;
					files_.setReg_id(common.getMod_id());
					files_.setMod_id(common.getMod_id());
				}

				filesList.add(files_);
			}
		}

		return filesList;
	}

	@Table
	protected static class ModelMap extends LinkedHashMap<String, Object> {
	}
}
