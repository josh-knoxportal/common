package org.oh.sample.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.oh.common.util.MapUtil;
import org.oh.common.util.Utils;
import org.oh.sample.model.Sample;
import org.oh.sample.service.SampleService;
import org.oh.web.controller.CommonController;
import org.oh.web.page.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "sample")
public class SampleController extends CommonController<Sample> {
	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected SampleService sampleService;

	@RequestMapping(value = "/get2", method = { RequestMethod.GET })
	public ResponseEntity<Sample> get2(Sample sample) throws Exception {
		sample = sampleService.get2(sample);

		return new ResponseEntity<Sample>(sample, HttpStatus.OK);
	}

	@RequestMapping(value = "/list2.do", method = { RequestMethod.GET })
	public ResponseEntity<List<?>> list(@Valid Sample sample, BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			log.error("fieldErrors: " + errors.getFieldErrors());

			List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
			for (FieldError fieldError : errors.getFieldErrors()) {
				errorList.add(MapUtil.convertArrayToMap(
						new String[] { "message", fieldError.getField() + " = " + fieldError.getDefaultMessage() }));
			}

			return new ResponseEntity<List<?>>(errorList, HttpStatus.BAD_REQUEST);
		}

		List<Sample> list = list(sample).getBody();

		return new ResponseEntity<List<?>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/list2", method = { RequestMethod.GET })
//	public ResponseEntity<List<Sample>> list2(Sample sample) throws Exception {
	public ResponseEntity<List<Sample>> list2(@Valid Sample sample, BindingResult errors) throws Exception {
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
		List<Sample> list = sampleService.list(sample);

		return new ResponseEntity<List<Sample>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/count2", method = { RequestMethod.GET })
	public ResponseEntity<Integer> count2(Sample sample) throws Exception {
		int count = sampleService.count2(sample);

		return new ResponseEntity<Integer>(count, HttpStatus.OK);
	}

	@RequestMapping(value = "/page2", method = { RequestMethod.GET })
	public ResponseEntity<PageNavigator<Sample>> page(Sample sample) throws Exception {
		int count = sampleService.count2(sample);

		List<Sample> list = sampleService.page(sample);

		sample.setTotal_sise(count);
		return new ResponseEntity<PageNavigator<Sample>>(PageNavigator.getInstance(sample, list), HttpStatus.OK);
	}

	@RequestMapping(value = "/insert2", method = RequestMethod.POST)
	public ResponseEntity<Integer> insert2(Sample sample) throws Exception {
		int result = sampleService.insert2(sample);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/update2", method = RequestMethod.PUT)
	public ResponseEntity<Integer> update2(Sample sample) throws Exception {
		int result = sampleService.update2(sample);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete2", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> delete2(Sample sample) throws Exception {
		int result = sampleService.delete2(sample);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public ResponseEntity<Integer> merge(Sample sample) throws Exception {
		int result = sampleService.merge(sample);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
}
