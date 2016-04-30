package org.oh.sample.controller;

import java.util.List;

import javax.validation.Valid;

import org.oh.sample.model.Sample;
import org.oh.sample.service.SampleService;
import org.oh.web.controller.CommonController;
import org.oh.web.page.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/sample")
public class SampleController extends CommonController<Sample> {
	@Autowired
	protected SampleService sampleService;

	@RequestMapping(value = "/get2", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Sample> get2(Sample sample) throws Exception {
		sample = sampleService.get2(sample);

		return new ResponseEntity<Sample>(sample, HttpStatus.OK);
	}

	@RequestMapping(value = "/list2", method = { RequestMethod.GET, RequestMethod.POST })
//	public ResponseEntity<List<Sample>> list2(Sample sample) throws Exception {
	public ResponseEntity<List<Sample>> list(@Valid Sample sample, BindingResult errors) throws Exception {
		log.info(errors.hasErrors());
		ValidationUtils.rejectIfEmpty(errors, "name", "field.required");
		List<Sample> list = sampleService.list2(sample);

		return new ResponseEntity<List<Sample>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/count2", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Integer> count2(Sample sample) throws Exception {
		int count = sampleService.count2(sample);

		return new ResponseEntity<Integer>(count, HttpStatus.OK);
	}

	@RequestMapping(value = "/page2", method = { RequestMethod.GET, RequestMethod.POST })
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
