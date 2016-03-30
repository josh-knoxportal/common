package org.oh.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.oh.web.model.Default;
import org.oh.web.model.Sample;
import org.oh.web.page.Paging;
import org.oh.web.service.CommonService;
import org.oh.web.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {
	private static Log log = LogFactory.getLog(SampleController.class);

	/**
	 * 공통 서비스
	 */
	@Resource(name = "commonService")
	protected CommonService commonService;

	/**
	 * 샘플 서비스
	 */
	@Autowired
	protected SampleService sampleService;

	@RequestMapping("/get")
	public Default get(Sample sample) throws Exception {
		return commonService.get(sample);
	}

	@RequestMapping("/list")
	public List<Sample> list(@Valid Sample sample, BindingResult bindingResult) throws Exception {
		return commonService.list(sample);
	}

	@RequestMapping("/page")
	public Page<Sample> page(Sample sample, Page<Sample> page) throws Exception {
		return commonService.page(sample, page);
	}

	@RequestMapping("/insert")
	public void insert(Sample sample) throws Exception {
		commonService.insert(sample);
	}

	@RequestMapping("/update")
	public void update(Sample sample) throws Exception {
		commonService.update(sample);
	}

	@RequestMapping("/delete")
	public void delete(Sample sample) throws Exception {
		commonService.delete(sample);
	}

	///////////////////////////////////////////////////////////////////////////

	@RequestMapping("/get2")
	public Sample get2(Sample sample) throws Exception {
		return sampleService.get(sample);
	}

	@RequestMapping("/list2")
	public List<Sample> list2(Sample sample) throws Exception {
		return sampleService.list(sample);
	}

	@RequestMapping("/page2")
	public List<Sample> page(Sample sample, Paging paging) throws Exception {
		return sampleService.page(sample, paging);
	}

	@RequestMapping("/insert2")
	public int insert2(Sample sample) throws Exception {
		return sampleService.insert(sample);
	}

	@RequestMapping("/update2")
	public int update2(Sample sample) throws Exception {
		return sampleService.update(sample);
	}

	@RequestMapping("/delete2")
	public int delete2(Sample sample) throws Exception {
		return sampleService.delete(sample);
	}

	@RequestMapping("/merge")
	public int merge(Sample sample) throws Exception {
		return sampleService.merge(sample);
	}
}
