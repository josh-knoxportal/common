package org.oh.web;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.oh.common.util.LogUtil;
import org.oh.web.model.Sample;
import org.oh.web.page.PageNavigator;
import org.oh.web.page.Paging;
import org.oh.web.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSample {
	/**
	 * 샘플 서비스
	 */
	@Autowired
	protected SampleService sampleService;

	@Test
	public void t04_get() throws Exception {
		LogUtil.writeLog("========== SELECT ==============================================================");

		Sample sample = new Sample();
		sample.setSample_id(1L);

		Sample sampleResult = sampleService.get(sample);
		LogUtil.writeLog("sampleResult:" + sampleResult);
	}

	@Test
	public void t05_list() throws Exception {
		LogUtil.writeLog("========== SELECT LIST =========================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");
		sample.setOrder_by("sample_id DESC");

		List<Sample> sampleList = sampleService.list(sample);
		LogUtil.writeLog("sampleList:" + sampleList);
	}

	@Test
	public void t06_page() throws Exception {
		LogUtil.writeLog("========== SELECT PAGE =========================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");
		sample.setOrder_by("sample_id");

		Paging paging = new Paging();
		paging.setPage_number(1);
		paging.setRows_per_page(1);

		List<Sample> sampleList = sampleService.page(sample, paging);
		LogUtil.writeLog("samplePage:" + sampleList);

		LogUtil.writeLog("---------- Page Navigator ------------------------------------------------------");

//		paging.setPage_number(3);
		paging.setTotal_sise(1);
//		paging.setRows_per_page(10);
		paging.setPage_group_count(1);
		PageNavigator pageNavi = new PageNavigator.Builder(paging).build();
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

//	@Test
	public void t01_insert() throws Exception {
		LogUtil.writeLog("========== INSERT ==============================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");
		sample.setTest_id(2L);
		sample.setReg_id("1");
		sample.setReg_dt("1");
		sample.setMod_id("1");
		sample.setReg_dt("1");

		int result = sampleService.insert(sample);
		LogUtil.writeLog("sample:" + sample);
		LogUtil.writeLog("result:" + result);
	}

//	@Test
	public void t02_update() throws Exception {
		LogUtil.writeLog("========== UPDATE ==============================================================");

		Sample sample = new Sample();
		sample.setSample_id(1L);
		sample.setSample_name("x");

		int result = sampleService.update(sample);
		LogUtil.writeLog("result:" + result);
	}

//	@Test
	public void t03_delete() throws Exception {
		LogUtil.writeLog("========== DELETE ==============================================================");

		Sample sample = new Sample();
		sample.setSample_id(1L);

		int result = sampleService.delete(sample);
		LogUtil.writeLog("result:" + result);
	}
}