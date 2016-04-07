package org.oh.web;

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
	public void t01_get() throws Exception {
		Sample model = new Sample();
		model.setId(1L);

		Default result = commonService.get(model);
//		Assert.assertTrue("result == null", result != null);
	}

	@Test
	public void t02_list() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setCondition("name LIKE 's%'");
		model.setOrder_by("id DESC");

		commonService.list(model);
	}

	@Test
	public void t03_count() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setCondition("name LIKE 's%'");

		commonService.count(model);
	}

	@Test
	public void t04_page() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setCondition("name LIKE 's%'");
		model.setOrder_by("id DESC");

		Page<Sample> page = new Page<Sample>(1, 1);

		page = commonService.page(model, page);

//		page.setPageNumber(3);
//		page.setCount(75);
//		page.setRows(10);
		PageNavigator pageNavi = new PageNavigator.Builder(page).setPageGroupCount(1).build();
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

	@Test
	public void t05_joinList() throws Exception {
		Sample model = new Sample();
		model.setName("s");

		org.oh.web.model.Test model2 = new org.oh.web.model.Test();
		model2.setName("t");

		SampleAndTest model3 = new SampleAndTest();
		model3.setSample(model);
		model3.setTest(model2);
		model3.setCondition("sample_.name LIKE 's%'");
		model3.setOrder_by("sample_.id DESC, test_.id DESC");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		commonService.list(model3);
	}

	@Test
	public void t06_joinPage() throws Exception {
		Sample model = new Sample();
		model.setName("s");

		org.oh.web.model.Test model2 = new org.oh.web.model.Test();
		model2.setName("t");

		SampleAndTest model3 = new SampleAndTest();
		model3.setSample(model);
		model3.setTest(model2);
		model3.setCondition("sample_.name LIKE 's%'");
		model3.setOrder_by("sample_.id DESC, test_.id DESC");

		Page<SampleAndTest> page = new Page<SampleAndTest>(1, 1);

		page = commonService.page(model3, page);

		PageNavigator pageNavi = new PageNavigator.Builder(page).setPageGroupCount(1).build();
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

	@Test
	public void t07_joinList2() throws Exception {
		Sample model = new Sample();
		model.setName("s");

		org.oh.web.model.Test model2 = new org.oh.web.model.Test();
		model2.setName("t");

		SampleAndTest2 model3 = new SampleAndTest2();
		model3.setSample(model);
		model3.setTest(model2);
		model3.setCondition("sample_.name LIKE 's%'");
		model3.setOrder_by("sample_.id DESC, test_.id DESC");

		commonService.list(model3);
	}

//	@Test
	public void t08_insert() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setTest_id(2L);
		model.setReg_id("1");
		model.setReg_dt("1");
		model.setMod_id("1");
		model.setReg_dt("1");

		commonService.insert(model);
	}

//	@Test
	public void t09_update() throws Exception {
		Sample model = new Sample();
		model.setId(1L);
		model.setName("x");

		commonService.update(model);
	}

//	@Test
	public void t10_delete() throws Exception {
		Sample model = new Sample();
		model.setId(1L);
		model.setCondition("name LIKE 's%'");

		commonService.delete(model);
	}
}