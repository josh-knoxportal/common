package org.oh.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.oh.Application;
import org.oh.common.util.JsonUtil2;
import org.oh.common.util.LogUtil;
import org.oh.sample.controller.SampleController;
import org.oh.sample.model.Sample;
import org.oh.sample.model.SampleAndTest;
import org.oh.sample.model.SampleAndTest2;
import org.oh.sample.service.SampleAndTest2Service;
import org.oh.sample.service.SampleAndTestService;
import org.oh.sample.service.SampleService;
import org.oh.web.common.Response;
import org.oh.web.page.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSample {
	private static Log log = LogFactory.getLog(TestSample.class);

//	/**
//	 * 공통 서비스
//	 */
//	@Resource(name = "commonService")
//	protected CommonService<Default> commonService;

	/**
	 * 샘플 컨트롤러
	 */
	@Autowired
	protected SampleController sampleController;

	/**
	 * 샘플 서비스
	 */
	@Autowired
	protected SampleService sampleService;

	@Autowired
	protected SampleAndTestService sampleAndTestService;

	@Autowired
	protected SampleAndTest2Service sampleAndTest2Service;

//	@Test
	public void t01_get() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);

		sample = sampleService.get(sample);
		LogUtil.writeLog("sample: " + JsonUtil2.toStringPretty(sample));
//		Assert.assertTrue("sample == null", sample != null);
	}

	@Test
	public void t02_list() throws Exception {
		Sample sample = new Sample();
		sample.setSql_name("list");
		sample.setHint("DISTINCT");
		sample.setFields("id, name");
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.addCondition(sample.newCondition().add("name LIKE 'ss%'").add("name", "LIKE", "sss%"));
		sample.setOrder_by("id DESC");
//		System.out.println(ReflectionUtil.toStringRecursive(sample, "condition2"));

//		List<Sample> list = sampleService.list(sample);
		ResponseEntity<Response<List<Sample>>> list = sampleController.list3(sample,
				new BeanPropertyBindingResult(sample, ""), new MockHttpServletRequest(), new MockHttpSession());
		LogUtil.writeLog("list: " + JsonUtil2.toStringPretty(list));
	}

//	@Test
	public void t03_count() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.addCondition("name LIKE 's%'");

		sampleService.count(sample);
	}

//	@Test
	public void t04_page() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		Page<Sample> page = new Page<Sample>(1);

		page = sampleService.page(sample, page);

		PageNavigator<Sample> pageNavi = new PageNavigator.Builder<Sample>(page).build();
		pageNavi.setList(page.getList());
		LogUtil.writeLog("pageNavi: " + JsonUtil2.toStringPretty(pageNavi));
	}

//	@Test
	public void t05_joinList() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("t");

		SampleAndTest sat = new SampleAndTest(sample, test);
		sat.addCondition("sample_.name LIKE 's%'");
		sat.addCondition("test_.name LIKE 't%'");
		sat.addCondition("sample_.name", "IN", "s");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		List<SampleAndTest> list = sampleAndTestService.list(sat);
		LogUtil.writeLog("list: " + JsonUtil2.toStringPretty(list));
	}

//	@Test
	public void t06_joinPage() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("t");

		SampleAndTest sat = new SampleAndTest(sample, test);
		sat.addCondition("sample_.name LIKE 's%'");
		sat.addCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

		Page<SampleAndTest> page = new Page<SampleAndTest>(1);

		page = sampleAndTestService.page(sat, page);

		PageNavigator<SampleAndTest> pageNavi = new PageNavigator.Builder<SampleAndTest>(page).build();
		pageNavi.setList(page.getList());
		LogUtil.writeLog("pageNavi: " + JsonUtil2.toStringPretty(pageNavi));
	}

//	@Test
	public void t07_joinList2() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("t");

		SampleAndTest2 sat = new SampleAndTest2(sample, test);
		sat.addCondition("sample_.name LIKE 's%'");
		sat.addCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

		List<SampleAndTest2> list = sampleAndTest2Service.list(sat);
		LogUtil.writeLog("list: " + JsonUtil2.toStringPretty(list));
	}

//	@Test
	public void t08_insert() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.setTest_id(3L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		sampleService.insert(sample);
	}

//	@Test
	public void t09_update() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
		sample.setName("s1");
		sample.setMod_id("11");
//		sample.addCondition("name LIKE 's%'");

		sampleService.update(sample);
	}

//	@Test
	public void t10_delete() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
//		sample.addCondition("name LIKE 's%'");

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

		sample = sampleService.get2(sample);
		LogUtil.writeLog("sample: " + JsonUtil2.toStringPretty(sample));
	}

//	@Test
	public void t52_list() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		List<Sample> list = sampleService.list2(sample);
		LogUtil.writeLog("sample: " + JsonUtil2.toStringPretty(list));
	}

//	@Test
	public void t53_count2() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.addCondition("name LIKE 's%'");

		sampleService.count2(sample);
	}

//	@Test
	public void t54_page() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		int count = sampleService.count2(sample);

		sample.setPage_number(1);
		sample.setTotal_sise(count);

		List<Sample> list = sampleService.page(sample);

		PageNavigator<Sample> pageNavi = new PageNavigator.Builder<Sample>(sample).build();
		pageNavi.setList(list);
		LogUtil.writeLog("pageNavi: " + JsonUtil2.toStringPretty(pageNavi));
	}

//	@Test
	public void t55_insert() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.setTest_id(3L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		sampleService.insert2(sample);
	}

//	@Test
	public void t56_update() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);
		sample.setName("s1");
		sample.setMod_id("11");

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