package org.oh.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.mybatisorm.Query;
import org.oh.common.util.LogUtil;
import org.oh.sample.model.Sample;
import org.oh.sample.model.SampleAndTest;
import org.oh.sample.model.SampleAndTest2;
import org.oh.sample.service.SampleService;
import org.oh.web.model.Default;
import org.oh.web.page.PageNavigator;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSample {
	private static Log log = LogFactory.getLog(TestSample.class);

	/**
	 * 공통 서비스
	 */
	@Resource(name = "commonService")
	protected CommonService<Default> commonService;

	/**
	 * 샘플 서비스
	 */
	@Autowired
	protected SampleService sampleService;

//	@Test
	public void t01_get() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);

		sample = sampleService.get(sample);
//		Assert.assertTrue("sample == null", sample != null);
	}

//	@Test
	public void t02_list() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.setCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		sampleService.list(sample);
	}

//	@Test
	public void t03_count() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.setCondition("name LIKE 's%'");

		sampleService.count(sample);
	}

//	@Test
	public void t04_page() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.setCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		Page<Sample> page = new Page<Sample>(1);

		page = sampleService.page(sample, page);

		PageNavigator<Sample> pageNavi = new PageNavigator.Builder<Sample>(page).build();
		pageNavi.setList(page.getList());
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

	@Test
	public void t05_joinList() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("t");

		SampleAndTest sat = new SampleAndTest();
		sat.setSample(sample);
//		sat.setTest(test);
		sat.setCondition("sample_.name LIKE 's%'");
		sat.setCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		commonService.list(sat);
	}

//	@Test
	public void t06_joinPage() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("t");

		SampleAndTest sat = new SampleAndTest();
		sat.setSample(sample);
		sat.setTest(test);
		sat.setCondition("sample_.name LIKE 's%'");
		sat.setCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

		int count = sampleService.count2(sample);

		Page<Default> page = new Page<Default>(1, count);
		PageNavigator<Default> pageNavi = new PageNavigator.Builder<Default>(page).build();

		page = commonService.page(sat, page);

		pageNavi.setList(page.getList());
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

//	@Test
	public void t07_joinList2() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("t");

		SampleAndTest2 sat = new SampleAndTest2();
		sat.setSample(sample);
		sat.setTest(test);
		sat.setCondition("sample_.name LIKE 's%'");
		sat.setCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

		commonService.list(sat);
	}

//	@Test
	public void t08_insert() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
//		sample.setTest_id(1L);
		sample.setReg_id("1");
		sample.setReg_dt(Query.makeVariable("TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')"));
		sample.setMod_id("1");
		sample.setMod_dt(Query.makeVariable("TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')"));

		sampleService.insert(sample);
	}

//	@Test
	public void t09_update() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
//		sample.setName("s");
		sample.setMod_id("1");
		sample.setMod_dt(Query.makeVariable("TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')"));

		sampleService.update(sample);
	}

//	@Test
	public void t10_delete() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
//		sample.setCondition("name LIKE 's%'");

		sampleService.delete(sample);
	}

	@Test
	public void t50() throws Exception {
		log.info("================================================================================");
	}

//	@Test
	public void t51_get() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);

		sampleService.get2(sample);
	}

//	@Test
	public void t52_list() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.setCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		sampleService.list2(sample);
	}

//	@Test
	public void t53_count2() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.setCondition("name LIKE 's%'");

		sampleService.count2(sample);
	}

//	@Test
	public void t54_page() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.setCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		int count = sampleService.count2(sample);

		sample.setPage_number(1);
		sample.setTotal_sise(count);
		PageNavigator<Sample> pageNavi = new PageNavigator.Builder<Sample>(sample).build();

		List<Sample> list = sampleService.page(sample);

		pageNavi.setList(list);
		LogUtil.writeLog("pageNavi:" + pageNavi);
	}

//	@Test
	public void t55_insert() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
//		sample.setTest_id(1L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		sampleService.insert2(sample);
	}

//	@Test
	public void t56_update() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
//		sample.setName("s");
		sample.setMod_id("1");

		sampleService.update2(sample);
	}

//	@Test
	public void t57_delete() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);

		sampleService.delete2(sample);
	}

//	@Test
	public void t58_merge() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
//		sample.setName("s");
//		sample.setTest_id(1L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		sampleService.merge(sample);
	}
}