package org.oh.sample.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.oh.common.model.Common;
import org.oh.common.page.PageNavigator;
import org.oh.common.util.Utils;
import org.oh.sample.model.Sample;
import org.oh.sample.service.SampleService;
import org.oh.web.Constants;
import org.oh.web.common.Response;
import org.oh.web.controller.CommonController;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// produces 는 "Accept":"application/json" 생략하기 위해 기술 (생략시 application/xml)
@Controller
@RequestMapping(value = "sample", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleController extends CommonController<Sample> {
	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected SampleService service;

	@Override
	public CommonService<Sample> getService() {
		return service;
	}

	@RequestMapping(value = "/list3", method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Sample>>> list3(@RequestBody Sample model, BindingResult errors,
			HttpServletRequest request, HttpSession session) throws Exception {
		model.setSql_name("list3");
		model.setHint("DISTINCT");
		model.setFields("id, name");
		model.setOrder_by("id DESC");

		return list(model, null, errors);
	}

	// 주의) 로깅을 위해 Annotation 재정의
	@Override
	@RequestMapping(value = "list" + Constants.POSTFIX, method = { RequestMethod.GET })
	public ResponseEntity<Response<List<Sample>>> list(Sample model, @Valid Common common, BindingResult errors)
			throws Exception {
//		return ValidationUtil.getResponseEntity(HttpStatus.NOT_FOUND); // 사용하지 않을 경우

		// 수기로 유효성 체크
//		if (!Utils.isValidate(model.getName())) {
//			return ValidationUtil.getFailResponseEntity(HttpStatus.BAD_REQUEST, "에러 메세지");
//		}

		// 다른 타입으로 반환
//		ResponseEntity<Response<List<Sample>>> responseEntity = list(model, errors);
//		Response<Integer> response = Response.getResponse(responseEntity.getBody(),
//				(responseEntity.getBody().getBody() == null) ? null : responseEntity.getBody().getBody().size());
//		return new ResponseEntity<Response<Integer>>(response, responseEntity.getStatusCode());

		log.info("message: " + messageSource.getMessage("NotEmpty.model.name", null, Locale.getDefault()));
		log.info("message: " + messageSource.getMessage("NotEmpty.model.name", null, Locale.KOREA));
		log.info("message: " + messageSource.getMessage("NotEmpty.model.name", null, Locale.ENGLISH));

		ResponseEntity<Response<List<Sample>>> responseEntity = super.list(model, common, errors);

		return new ResponseEntity<Response<List<Sample>>>(responseEntity.getBody(), responseEntity.getStatusCode());
	}

	@RequestMapping(value = "/list4", method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Sample>>> list4(@RequestBody Sample model, BindingResult errors,
			HttpServletRequest request, HttpSession session) throws Exception {
		return list2(model, null, errors, request, session);
	}

	// 주의) RequestMethod 추가시 메소드명을 다르게 정의
	@RequestMapping(value = "/list2", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<Sample>>> list2(Sample model, @Valid Common common, BindingResult errors,
			HttpServletRequest request, HttpSession session) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

//		log.info(messageSource.getMessage("field.required.model.name", null, null));
		log.info("errors: " + errors.hasErrors());
		log.info("errors: " + Utils.toString(errors.getAllErrors()));
		log.info("errors: " + errors.getFieldError());
		log.info("errors: " + Utils.toString(errors.getFieldErrors()));
//		log.info("errors: " + errors.getFieldErrors().iterator().next().getDefaultMessage());
//		log.info("errors: " + errors.getFieldErrors("NotNull.model.name"));
		for (FieldError error : errors.getFieldErrors()) {
			log.info(String.format("%s.%s : %s (%s)", error.getObjectName(), error.getField(),
					error.getDefaultMessage(), error.getRejectedValue()));
		}
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotNull");
//		ValidationUtils.rejectIfEmpty(errors, "name", "field.required");

		List<Sample> list = service.list2(model);
		Response<List<Sample>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<Sample>>>(response, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "update" + Constants.POSTFIX, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Response<Integer>> update(@Valid Sample model, BindingResult errors,
			HttpServletRequest request) throws Exception {
		return super.update(model, errors, request);
	}

	@RequestMapping(value = "/get2", method = { RequestMethod.GET })
	public ResponseEntity<Response<Sample>> get2(Sample model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		model = service.get2(model);
		Response<Sample> response = Response.getSuccessResponse(model);

		return new ResponseEntity<Response<Sample>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/count2", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count2(Sample model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		int count = service.count2(model);
		Response<Integer> response = Response.getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/page2", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<Sample>>> page(Sample model, @Valid Common common,
			BindingResult errors) throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		List<Sample> list = service.page(model);

		int count = service.count2(model);
		model.setTotal_sise(count);

		Response<PageNavigator<Sample>> response = Response.getSuccessResponse(PageNavigator.getInstance(model, list));

		return new ResponseEntity<Response<PageNavigator<Sample>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/insert2", method = RequestMethod.POST)
	public ResponseEntity<Response<Long>> insert2(@Valid Sample model, BindingResult errors) throws Exception {
		long result = service.insert2(model);
		Response<Long> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Long>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update2", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> update2(Sample model, BindingResult errors) throws Exception {
		int result = service.update2(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete2", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete2(Sample model, BindingResult errors) throws Exception {
		int result = service.delete2(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> merge(Sample model, BindingResult errors) throws Exception {
		int result = service.merge(model);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}
}
