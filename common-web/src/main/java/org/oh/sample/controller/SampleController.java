package org.oh.sample.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.oh.common.util.Utils;
import org.oh.sample.model.Sample;
import org.oh.sample.service.SampleService;
import org.oh.web.common.Response;
import org.oh.web.controller.CommonController;
import org.oh.web.page.PageNavigator;
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

	// 주의) 로깅을 위해 Annotation 재정의
	@Override
	@RequestMapping(value = "list.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<Sample>>> list(Sample sample, BindingResult errors) throws Exception {
		log.info("message: " + messageSource.getMessage("NotEmpty.sample.name", null, Locale.KOREA));

		ResponseEntity<Response<List<Sample>>> responseEntity = super.list(sample, errors);

		return new ResponseEntity<Response<List<Sample>>>(responseEntity.getBody(), responseEntity.getStatusCode());
	}

	@RequestMapping(value = "/list2.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<Sample>>> list2(@Valid Sample sample, BindingResult errors,
			HttpServletRequest request, HttpSession session) throws Exception {
//		log.info(messageSource.getMessage("field.required.sample.name", null, null));
		log.info("errors: " + errors.hasErrors());
		log.info("errors: " + Utils.toString(errors.getAllErrors()));
		log.info("errors: " + errors.getFieldError());
		log.info("errors: " + Utils.toString(errors.getFieldErrors()));
//		log.info("errors: " + errors.getFieldErrors().iterator().next().getDefaultMessage());
//		log.info("errors: " + errors.getFieldErrors("NotNull.sample.name"));
		for (FieldError error : errors.getFieldErrors()) {
			log.info(String.format("%s.%s : %s (%s)", error.getObjectName(), error.getField(),
					error.getDefaultMessage(), error.getRejectedValue()));
		}
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotNull");
//		ValidationUtils.rejectIfEmpty(errors, "name", "field.required");
		List<Sample> list = service.list2(sample);
		Response<List<Sample>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<Sample>>>(response, HttpStatus.OK);
	}

	// 주의) RequestMethod 추가시 메소드명을 다르게 정의
	@RequestMapping(value = "/list3.do", method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Sample>>> list3(@RequestBody Sample sample, BindingResult errors,
			HttpServletRequest request, HttpSession session) throws Exception {
		return list(sample, errors);
	}

	@RequestMapping(value = "/list4.do", method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Sample>>> list4(@RequestBody Sample sample, BindingResult errors,
			HttpServletRequest request, HttpSession session) throws Exception {
		return list2(sample, errors, request, session);
	}

	@RequestMapping(value = "/get2.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<Sample>> get2(Sample sample) throws Exception {
		sample = service.get2(sample);
		Response<Sample> response = Response.getSuccessResponse(sample);

		return new ResponseEntity<Response<Sample>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/count2.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count2(Sample sample) throws Exception {
		int count = service.count2(sample);
		Response<Integer> response = Response.getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/page2.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<Sample>>> page(Sample sample) throws Exception {
		List<Sample> list = service.page(sample);

		int count = service.count2(sample);
		sample.setTotal_sise(count);

		Response<PageNavigator<Sample>> response = Response.getSuccessResponse(PageNavigator.getInstance(sample, list));

		return new ResponseEntity<Response<PageNavigator<Sample>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/insert2.do", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> insert2(@Valid Sample sample) throws Exception {
		int result = service.insert2(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update2.do", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> update2(Sample sample) throws Exception {
		int result = service.update2(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete2.do", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete2(Sample sample) throws Exception {
		int result = service.delete2(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/merge.do", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> merge(Sample sample) throws Exception {
		int result = service.merge(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}
}
