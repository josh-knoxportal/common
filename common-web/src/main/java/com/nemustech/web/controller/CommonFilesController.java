package com.nemustech.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.nemustech.common.file.Files;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.service.CommonFilesService;
import com.nemustech.common.util.TypeUtil;
import com.nemustech.common.util.Utils;
import com.nemustech.web.exception.WebException;
import com.nemustech.web.file.Downloader;

/**
 * 공통 파일 컨트롤러
 * 
 * <pre>
 * - 공통 파일 생성
 * 15. [/model/files],methods=[POST]
 * 
 * - 공통 파일 수정
 * . [/model/update_files],methods=[POST]
 * 
 * - 공통 파일 삭제
 * 17. [/model/files],methods=[DELETE]
 * </pre>
 * 
 * @author skoh
 * @param <T>
 * @param <F>
 */
@Controller
public abstract class CommonFilesController<T extends Default, F extends Files> extends CommonController<T> {
	/**
	 * 모델 파일명
	 */
	public static final String MODEL_FILE_NAME = "_model_";

	/**
	 * 처음부터 받기
	 */
	public static final int MODE_STREAM = 1;

	/**
	 * 나누어 받기
	 */
	public static final int MODE_PARTIAL = 2;

	/**
	 * 이어 받기
	 */
	public static final int MODE_RESUME = 3;

	/**
	 * 파일 다운로드 버퍼 크기
	 */
	public static final int BUFFER_SIZE = 8192; // 8kb

	protected CommonFilesService<T, F> filesService;

	/**
	 * 파일 서비스
	 * 
	 * @return
	 */
	public abstract CommonFilesService<T, F> getCommonFilesService();

	@PostConstruct
	public void initCommonFiles_() throws Exception {
		filesService = getCommonFilesService();
	}

	/**
	 * 단일 모델의 복수 파일 등록
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
//	@RequestParam("file") MultipartFile[] files
	@RequestMapping(value = "files", method = RequestMethod.POST)
	public ResponseEntity<Response<Object>> insert_files_json(@Valid @RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		List<F> fileList = getFiles(model, request);

		Object result = filesService.insert(model, fileList);
		Response<Object> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}

	/**
	 * 단일 모델의 복수 파일 수정
	 * Content-Type : application/json
	 * 
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "update_files", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> update_files_json(@RequestPart(MODEL_FILE_NAME) T model,
			BindingResult errors, HttpServletRequest request) throws Exception {
		List<F> fileList = getFiles(model, request);

		int result = filesService.update(model, fileList);
		Response<Integer> response = getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
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
	@RequestMapping(value = "files", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete_files(T model, BindingResult errors) throws Exception {
		int result = filesService.delete(model, new ArrayList<>((Set<F>) model.getFiles()));
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

	/**
	 * URI 요청 정보를 바탕으로 해당 downloader 서비스를 통해 URI 요청을 처리한다.
	 * 상세한 사항은 BizMOB 서버의 개발 가이드를 참고한다.
	 * 파일은 HTTP 프로토콜로 전송한다.
	 * 
	 * @param request 요청한 HttpRequest
	 * @param response 전송할 HttpResponse
	 * @param target URI에서 다운로드 target으로 지정한 정보
	 * @param file_name 파일 이름
	 * @param mode 받기 모드
	 * @throws Exception
	 */
	@RequestMapping(value = "/down/{target}/**", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	public void download(@PathVariable final String target, @RequestParam final String file_name,
			@RequestParam final int mode, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int fileStartPos = 0;
		String uid = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

//		log.info(String.format("===== Start::download() of %s : uid : %s, file : %s", target, uid, file_name));
//		if (log.isTraceEnabled()) {
//			log.trace("  > target : " + target);
//			log.trace("  > uid : " + uid);
//			log.trace("  > mode : " + mode);
//			log.trace("  > file name : " + file_name);
//		}

		switch (mode) {
			case MODE_STREAM:
				break;

			case MODE_RESUME:
				String pos = request.getParameter("index");
				if (pos == null || pos.length() == 0) {
					throw new WebException(HttpStatus.BAD_REQUEST, "No index");
				}

				fileStartPos = Integer.parseInt(pos);

				break;

			default:
				log.error("BAD_REQUEST due to invalid \"mode\".");

				throw new WebException(HttpStatus.BAD_REQUEST, "No mode");
		}

		ApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
		HashMap<String, Object> params = new HashMap<String, Object>(5);
		params.put("file_name", file_name);
		params.put("mode", mode);
		params.put("index", fileStartPos);
		params.put("HttpServletRequest", request);
		params.put("HttpServletResponse", response);

		Downloader downloader = context.getBean(target + "Downloader", Downloader.class);
		downloader.download(target, uid, params);

//		log.info("End::download() of " + target);
	}
}
