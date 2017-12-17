package com.nemustech.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.common.file.Files;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;

/**
 * 공통 파일 컨트롤러2
 * 
 * <pre>
 * - 공통 파일 생성
 * 18. [/model/files_form],methods=[POST]
 * 
 * - 공통 파일 수정
 * . [/model/update_files_form],methods=[POST]
 * 
 * - 공통 파일 삭제
 * 20. [/model/delete_files],methods=[POST]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 * @param <F>
 */
@Controller
public abstract class CommonFilesController2<T extends Default, F extends Files> extends CommonFilesController<T, F> {
	/**
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "files_form", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files(@Valid T model, BindingResult errors,
			HttpServletRequest request) throws Exception {
		return super.insert_files_json(model, errors, request);
	}

	/**
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "update_files_form", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files(T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		return super.update_files_json(model, errors, request);
	}

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_files", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> delete_files_json(@RequestBody T model, BindingResult errors)
			throws Exception {
		return super.delete_files(model, errors);
	}
}
