package com.nemustech.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

import com.nemustech.common.file.Files;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;

/**
 * 공통 파일 컨트롤러2
 * 
 * <pre>
 * - 공통 파일 생성
 * . [/model/insert_files.do],methods=[POST]
 * . [/model/insert_files_form.do],methods=[POST]
 * 
 * - 공통 파일 수정
 * . [/model/update_files.do],methods=[POST]
 * . [/model/update_files_form.do],methods=[POST]
 * 
 * - 공통 파일 삭제
 * . [/model/delete_files_json.do],methods=[POST]
 * . [/model/delete_files.do],methods=[DELETE]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 * @param <F>
 */
@Controller
public abstract class CommonFilesController3<T extends Default, F extends Files> extends CommonFilesController2<T, F> {
	@Override
	@RequestMapping(value = "insert_files" + CommonController3.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files_json(@Valid @RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		return super.insert_files_json(model, errors, request);
	}

	@Override
	@RequestMapping(value = "insert_files_form" + CommonController3.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files(@Valid T model, BindingResult errors,
			HttpServletRequest request) throws Exception {
		return super.insert_files(model, errors, request);
	}

	@Override
	@RequestMapping(value = "update_files" + CommonController3.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files_json(@RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		return super.update_files_json(model, errors, request);
	}

	@Override
	@RequestMapping(value = "update_files_form" + CommonController3.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files(T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		return super.update_files(model, errors, request);
	}

	@Override
	@RequestMapping(value = "delete_files_json" + CommonController3.POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> delete_files_json(@RequestBody T model, BindingResult errors)
			throws Exception {
		return super.delete_files_json(model, errors);
	}

	@Override
	@RequestMapping(value = "delete_files" + CommonController3.POSTFIX, method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete_files(T model, BindingResult errors) throws Exception {
		return super.delete_files(model, errors);
	}
}
