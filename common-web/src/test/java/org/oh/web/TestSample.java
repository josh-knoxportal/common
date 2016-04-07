package org.oh.web;

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
		Sample model = new Sample();
		model.setId(1L);

		sampleService.get(model);
	}

	@Test
	public void t05_list() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setOrder_by("id DESC");

		sampleService.list(model);
	}

	@Test
	public void t06_page() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setOrder_by("id");

		Paging paging = new Paging();
		paging.setPage_number(1);
		paging.setRows_per_page(1);

		sampleService.page(model, paging);

//		paging.setPage_number(3);
		paging.setTotal_sise(1);
//		paging.setRows_per_page(10);
		paging.setPage_group_count(1);
		PageNavigator pageNavi = new PageNavigator.Builder(paging).build();
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

//	@Test
	public void t01_insert() throws Exception {
		Sample model = new Sample();
		model.setName("s");
		model.setTest_id(2L);
		model.setReg_id("1");
		model.setReg_dt("1");
		model.setMod_id("1");
		model.setReg_dt("1");

		sampleService.insert(model);
	}

//	@Test
	public void t02_update() throws Exception {
		Sample model = new Sample();
		model.setId(1L);
		model.setName("x");

		sampleService.update(model);
	}

//	@Test
	public void t03_delete() throws Exception {
		Sample model = new Sample();
		model.setId(1L);

		sampleService.delete(model);
	}
}