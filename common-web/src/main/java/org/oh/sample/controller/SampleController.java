package org.oh.sample.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.oh.common.util.Utils;
import org.oh.common.util.ValidationUtil;
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

	@Override
	public ResponseEntity<Response<List<Sample>>> list(Sample sample, BindingResult errors) throws Exception {
		log.info("message: " + messageSource.getMessage("NotEmpty.sample.name", null, Locale.KOREA));
		if (errors.hasFieldErrors()) {
			log.error("fieldErrors: " + errors.getFieldErrors());
			Response<List<Sample>> response = ValidationUtil.getResponse(errors);

			return new ResponseEntity<Response<List<Sample>>>(response, HttpStatus.BAD_REQUEST);
		}

		ResponseEntity<Response<List<Sample>>> responseEntity = super.list(sample, errors);

		return new ResponseEntity<Response<List<Sample>>>(responseEntity.getBody(), responseEntity.getStatusCode());
	}

	// 주의) RequestMethod 추가시 메소드명과 SQL 순번을 다르게 정의
	@RequestMapping(value = "/list.do", method = { RequestMethod.POST })
	public ResponseEntity<Response<List<Sample>>> list3(@RequestBody @Valid Sample sample, BindingResult errors)
			throws Exception {
		sample.setSql_seq(1);

		return list(sample, errors);
	}

	@RequestMapping(value = "/get2", method = { RequestMethod.GET })
	public ResponseEntity<Response<Sample>> get2(Sample sample) throws Exception {
		sample = service.get2(sample);
		Response<Sample> response = Response.getSuccessResponse(sample);

		return new ResponseEntity<Response<Sample>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/list2", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<Sample>>> list2(@Valid Sample sample, BindingResult errors) throws Exception {
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
		List<Sample> list = service.list(sample);
		Response<List<Sample>> response = Response.getSuccessResponse(list);

		return new ResponseEntity<Response<List<Sample>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/count2", method = { RequestMethod.GET })
	public ResponseEntity<Response<Integer>> count2(Sample sample) throws Exception {
		int count = service.count2(sample);
		Response<Integer> response = Response.getSuccessResponse(count);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/page2", method = { RequestMethod.GET })
	public ResponseEntity<Response<PageNavigator<Sample>>> page(Sample sample) throws Exception {
		List<Sample> list = service.page(sample);

		int count = service.count2(sample);
		sample.setTotal_sise(count);

		Response<PageNavigator<Sample>> response = Response.getSuccessResponse(PageNavigator.getInstance(sample, list));

		return new ResponseEntity<Response<PageNavigator<Sample>>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/insert2", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> insert2(Sample sample) throws Exception {
		int result = service.insert2(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update2", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> update2(Sample sample) throws Exception {
		int result = service.update2(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete2", method = RequestMethod.DELETE)
	public ResponseEntity<Response<Integer>> delete2(Sample sample) throws Exception {
		int result = service.delete2(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public ResponseEntity<Response<Integer>> merge(Sample sample) throws Exception {
		int result = service.merge(sample);
		Response<Integer> response = Response.getSuccessResponse(result);

		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}
}
