package com.nemustech.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.nemustech.common.file.Files;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.service.CommonFilesService;
import com.nemustech.common.util.TypeUtil;
import com.nemustech.common.util.Utils;

@Controller
public abstract class CommonFilesController<T extends Default, F extends Files> extends CommonController<T> {
	/**
	 * 모델 파일명
	 */
	public static final String MODEL_FILE_NAME = "_model_";

	/**
	 * 공통 파일 서비스
	 * 
	 * @return
	 */
	public abstract CommonFilesService<T, F> getCommonFilesService();

	@RequestMapping(value = "insert_files_json" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files_json(@Valid @RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		return insert_files(model, errors, request);
	}

	@RequestMapping(value = "insert_files" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files(@Valid T model, BindingResult errors,
			HttpServletRequest request) throws Exception {
		List<F> fileList = getFiles(model, request);

		Object result = getCommonFilesService().insert(model, fileList);
		Response<Object> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "update_files_json" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files_json(@Valid @RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		return update_files(model, errors, request);
	}

	@RequestMapping(value = "update_files" + POSTFIX, method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files(@Valid T model, BindingResult errors,
			HttpServletRequest request) throws Exception {
		List<F> fileList = getFiles(model, request);

		int result = getCommonFilesService().update(model, fileList);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * 요청에서 파일 리스트를 구한다.
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected List<F> getFiles(T model, HttpServletRequest request) throws Exception {
		List<F> filesList = new ArrayList<>();

		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = multipartRequest.getFileNames();

			List<MultipartFile> files = new ArrayList<MultipartFile>();
			while (fileNames.hasNext()) {
				files.addAll(multipartRequest.getFiles((String) fileNames.next()));
			}

			for (MultipartFile file : files) {
				if (!Utils.isValidate(file.getOriginalFilename()) || MODEL_FILE_NAME.equals(file.getOriginalFilename()))
					continue;

				F files_ = TypeUtil.newSuperclassGenericArgumentInstance(this, 1);
				files_.setFiles(null, null, file.getOriginalFilename(), file.getBytes());
				filesList.add(files_);
			}
		}

		return filesList;
	}
}
