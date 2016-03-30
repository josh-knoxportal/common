package org.oh.web;

import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.oh.common.util.LogUtil;
import org.oh.web.model.Default;
import org.oh.web.model.Sample;
import org.oh.web.model.SampleAndTest;
import org.oh.web.model.SampleAndTest2;
import org.oh.web.page.PageNavigator;
import org.oh.web.service.CommonService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCommon {
	/**
	 * 공통 서비스
	 */
	@Resource(name = "commonService")
	protected CommonService commonService;

	@Test
	public void t04_get() throws Exception {
		LogUtil.writeLog("========== SELECT ==============================================================");

		Sample sample = new Sample();
		sample.setSample_id(1L);

		Default sampleResult = commonService.get(sample);
		LogUtil.writeLog("sampleResult:" + sampleResult);
//		Assert.assertTrue("sampleResult == null", sampleResult != null);
	}

	@Test
	public void t05_list() throws Exception {
		LogUtil.writeLog("========== SELECT LIST =========================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");
		sample.setOrder_by("sample_id DESC");

		List<Sample> sampleList = commonService.list(sample);
		LogUtil.writeLog("sampleList:" + sampleList);
	}

	@Test
	public void t06_page() throws Exception {
		LogUtil.writeLog("========== SELECT PAGE =========================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");
		sample.setOrder_by("sample_id DESC");

		Page<Sample> page = new Page<Sample>(1, 1);

		page = commonService.page(sample, page);
		LogUtil.writeLog("sampleList:" + page.getList());
		LogUtil.writeLog("sampleCount:" + page.getCount());

		LogUtil.writeLog("---------- Page Navigator ------------------------------------------------------");

//		page.setPageNumber(3);
//		page.setCount(75);
//		page.setRows(10);
		PageNavigator pageNavi = new PageNavigator.Builder(page).setPageGroupCount(1).build();
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

	@Test
	public void t07_joinList() throws Exception {
		LogUtil.writeLog("========== SELECT JOIN LIST ====================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");

		org.oh.web.model.Test test = new org.oh.web.model.Test();
		test.setTest_name("t");

		SampleAndTest st = new SampleAndTest();
		st.setSample(sample);
		st.setTest(test);
		st.setOrder_by("sample_.sample_id DESC, test_.test_id DESC");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		List<SampleAndTest> list = commonService.list(st);
		LogUtil.writeLog("sampleAndTestList:" + list);
	}

	@Test
	public void t08_joinPage() throws Exception {
		LogUtil.writeLog("========== SELECT JOIN PAGE ====================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");

		org.oh.web.model.Test test = new org.oh.web.model.Test();
		test.setTest_name("t");

		SampleAndTest st = new SampleAndTest();
		st.setSample(sample);
		st.setTest(test);
		st.setOrder_by("sample_.sample_id DESC, test_.test_id DESC");

		Page<SampleAndTest> page = new Page<SampleAndTest>(1, 1);

		page = commonService.page(st, page);
		LogUtil.writeLog("sampleAndTestPage:" + page.getList());
		LogUtil.writeLog("sampleAndTestCount:" + page.getCount());

		LogUtil.writeLog("---------- Page Navigator ------------------------------------------------------");

		PageNavigator pageNavi = new PageNavigator.Builder(page).setPageGroupCount(1).build();
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

	@Test
	public void t09_joinList2() throws Exception {
		LogUtil.writeLog("========== SELECT JOIN LIST 2 ==================================================");

		Sample sample = new Sample();
		sample.setSample_name("s");

		org.oh.web.model.Test test = new org.oh.web.model.Test();
		test.setTest_name("t");

		SampleAndTest2 st = new SampleAndTest2();
		st.setSample(sample);
		st.setTest(test);
		st.setOrder_by("sample_.sample_id DESC, test_.test_id DESC");

		List<SampleAndTest2> list = commonService.list(st);
		LogUtil.writeLog("sampleAndTestList2:" + list);
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

		commonService.insert(sample);
		LogUtil.writeLog("sample:" + sample);
	}

//	@Test
	public void t02_update() throws Exception {
		LogUtil.writeLog("========== UPDATE ==============================================================");

		Sample sample = new Sample();
		sample.setSample_id(1L);
		sample.setSample_name("x");

		commonService.update(sample);
	}

//	@Test
	public void t03_delete() throws Exception {
		LogUtil.writeLog("========== DELETE ==============================================================");

		Sample sample = new Sample();
		sample.setSample_id(1L);

		commonService.delete(sample);
	}
}