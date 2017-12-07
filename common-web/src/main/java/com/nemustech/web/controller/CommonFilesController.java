package com.nemustech.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
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

/**
 * 공통 파일 컨트롤러
 * 
 * <pre>
 * - 공통 파일 생성
 * . [/model/insert_files_json],methods=[POST]
 * . [/model/insert_files],methods=[POST]
 * 
 * - 공통 파일 수정
 * . [/model/update_files_json],methods=[POST]
 * . [/model/update_files],methods=[POST]
 * 
 * - 공통 파일 삭제
 * . [/model/delete_files_json],methods=[POST]
 * . [/model/delete_files],methods=[DELETE]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 * @param <F>
 */
@Controller
public abstract class CommonFilesController<T extends Default, F extends Files> extends CommonController2<T> {
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

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
//	@RequestParam("file") MultipartFile[] files
	@RequestMapping(value = "insert_files_json", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files_json(@Valid @RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		return insert_files(model, errors, request);
	}

	/**
	 * 단일 모델의 복수 파일 등록
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "insert_files", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files(@Valid T model, BindingResult errors,
			HttpServletRequest request) throws Exception {
		List<F> fileList = getFiles(model, request);

		Object result = getCommonFilesService().insert(model, fileList);
		Response<Object> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "update_files_json", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files_json(@RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		return update_files(model, errors, request);
	}

	/**
	 * 단일 모델의 복수 파일 수정
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "update_files", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files(T model, BindingResult errors, HttpServletRequest request)
			throws Exception {
		List<F> fileList = getFiles(model, request);

		int result = getCommonFilesService().update(model, fileList);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	/**
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_files_json", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> delete_files_json(@RequestBody T model, BindingResult errors)
			throws Exception {
		return delete_files(model, errors);
	}

	/**
	 * 단일 모델의 복수 파일 삭제
	 * Content-Type : application/x-www-form-urlencoded
	 * 
	 * @param model
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_files", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete_files(T model, BindingResult errors) throws Exception {
		int result = getCommonFilesService().delete(model, new ArrayList<>((Set<F>) model.getFiles()));
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	protected List<F> getFiles(T model, HttpServletRequest request) throws Exception {
		return getFiles(model, (Set<F>) model.getFiles(), request);
	}

	/**
	 * 요청에서 파일 리스트를 구한다.
	 * (파일 수정시 model 객체의 files 객체를 가져올 경우에 Override하여 사용)
	 * 
	 * @param model
	 * @param files
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected List<F> getFiles(T model, Set<F> files, HttpServletRequest request) throws Exception {
		List<F> filesList = new ArrayList<>();

		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = multipartRequest.getFileNames();

			List<MultipartFile> files_ = new ArrayList<MultipartFile>();
			while (fileNames.hasNext()) {
				files_.addAll(multipartRequest.getFiles((String) fileNames.next()));
			}

			Iterator<F> filesIter = files.iterator();
			for (MultipartFile file_ : files_) {
				if (!Utils.isValidate(file_.getOriginalFilename())
						|| MODEL_FILE_NAME.equals(file_.getOriginalFilename()))
					continue;

				F file = TypeUtil.newSuperclassGenericArgumentInstance(this, 1);
				file.setFiles((files == null) ? null : (filesIter.hasNext()) ? filesIter.next().getId() : null, null,
						file_.getOriginalFilename(), file_.getBytes());
				filesList.add(file);
			}
		}

		return filesList;
	}
}
