package org.oh.web;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.oh.WebApplication;
import org.oh.common.model.Common;
import org.oh.common.page.PageNavigator;
import org.oh.common.util.JsonUtil2;
import org.oh.sample.controller.SampleAndFilesController;
import org.oh.sample.controller.SampleAndTest2Controller;
import org.oh.sample.controller.SampleAndTestController;
import org.oh.sample.controller.SampleController;
import org.oh.sample.model.Files2;
import org.oh.sample.model.Sample;
import org.oh.sample.model.SampleAndTest;
import org.oh.sample.model.SampleAndTest2;
import org.oh.web.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTest {
	/**
	 * 샘플 컨트롤러
	 */
	@Autowired
	protected SampleController sampleController;

	@Autowired
	protected SampleAndTestController sampleAndTestController;

	@Autowired
	protected SampleAndTest2Controller sampleAndTest2Controller;

	@Autowired
	protected SampleAndFilesController sampleAndFilesController;

//	@Test
	public void t01_get() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);

		ResponseEntity<Response<Sample>> response = sampleController.get(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t02_list() throws Exception {
		Sample sample = new Sample();
//		sample.setName("s");
//		sample.addCondition("name LIKE 's%'");
//		sample.addCondition("name", "LIKE", "s%");
//		sample.addCondition("name", "IN", "s1", "s2");
//		sample.addCondition("name", "BETWEEN", "s1", "s2");
//		sample.addCondition(sample.newCondition("OR").add("name LIKE 's%'").add("name", "LIKE", "s%"));
		sample.setSql_name("t02_list");
		sample.setHint("DISTINCT");
		sample.setFields("name, COUNT(1) AS count");
		sample.setTable("sample");
		sample.setGroup_by("name");
		sample.setHaving("COUNT(1) > 0");
		sample.setOrder_by("name");

		ResponseEntity<Response<List<Sample>>> response = sampleController.list(sample,
				new BeanPropertyBindingResult(sample, "sample"));
//		ResponseEntity<Response<List<Sample>>> response = sampleController.list3(sample,
//				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest(), new MockHttpSession());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t03_count() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");

		ResponseEntity<Response<Integer>> response = sampleController.count(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t04_page() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		Page<Sample> page = new Page<Sample>(1);

		ResponseEntity<Response<PageNavigator<Sample>>> response = sampleController.page(sample, page,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t05_joinList() throws Exception {
		Sample sample = new Sample();
//		sample.setName("ss");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
//		test.setName("tt");

		Files2 files = new Files2();

		SampleAndTest sat = new SampleAndTest(sample, test, files);
		sample.setSql_name("t05_joinList");
		sat.setFields("sample_.name sample_name, COUNT(1) sample_count"); // skoh
//		sat.addCondition("sample_.name LIKE 's%'");
//		sat.addCondition("test_.name LIKE 't%'");
//		sat.addCondition("sample_.name", "IN", "ss");
		sat.setGroup_by("sample_.name");
		sat.setHaving("COUNT(1) > 1");
		sat.setOrder_by("sample_.name");
//		sat.setOrder_by("sample_.id DESC, test_.id DESC");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		ResponseEntity<Response<List<SampleAndTest>>> response = sampleAndTestController.list(sat,
				new BeanPropertyBindingResult(sat, "sat"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());

//		List<Default> list = MapperUtils.convertModels(response.getBody().getBody(), "testSet", "filesSet");
//		System.out.println("list: " + JsonUtil2.toStringPretty(list));

//		Sample sample = new Sample();
//		sample.setName("s1");
//
//		SampleAndFiles saf = new SampleAndFiles(sample, files);
//
//		ResponseEntity<Response<List<SampleAndFiles>>> response2 = sampleAndFilesController.list(saf,
//				new BeanPropertyBindingResult(saf, "saf"));
//		System.out.println("response: " + JsonUtil2.toStringPretty(response2));
//		Assert.assertTrue("Fail", response2.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t06_joinPage() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
		test.setName("t");

		Files2 files = new Files2();

		SampleAndTest sat = new SampleAndTest(sample, test, files);
		sat.addCondition("sample_.name LIKE 's%'");
		sat.addCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

		Page<SampleAndTest> page = new Page<SampleAndTest>(1);

		ResponseEntity<Response<PageNavigator<SampleAndTest>>> response = sampleAndTestController.page(sat, page,
				new BeanPropertyBindingResult(sat, "sat"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t07_joinList2() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
		test.setName("t");

		SampleAndTest2 sat = new SampleAndTest2(sample, test);
		sat.addCondition("sample_.name LIKE 's%'");
		sat.addCondition("test_.name LIKE 't%'");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

		ResponseEntity<Response<List<SampleAndTest2>>> response = sampleAndTest2Controller.list(sat,
				new BeanPropertyBindingResult(sat, "sat"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t08_insert() throws Exception {
		Sample sample = new Sample();
//		sample.setSql_name("t08_insert");
//		sample.setTable("sample");
		sample.setName("s1");
		sample.setTest_id(3L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		ResponseEntity<Response<Object>> response = sampleController.insert(sample,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t09_update() throws Exception {
		Sample sample = new Sample();
//		sample.setSql_name("t09_update");
//		sample.setTable("sample");
		sample.setId(1L);
		sample.setName("s2");
		sample.setMod_id("2");
		sample.addCondition("name LIKE 's1%'");

		ResponseEntity<Response<Integer>> response = sampleController.update(sample,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t10_delete() throws Exception {
		Sample sample = new Sample();
//		sample.setSql_name("t10_delete");
//		sample.setTable("sample");
		sample.setId(541L);
		sample.setName("s1");
		sample.addCondition("name LIKE 's1%'");

		ResponseEntity<Response<Integer>> response = sampleController.delete(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t11_select() throws Exception {
		Common common = new Common();
		common.setSql_name("t11_select");
		common.setHint("DISTINCT");
		common.setFields("*");
		common.setTable("sample");
		common.setCondition("name LIKE 's%'");
		common.setOrder_by("id DESC");

		ResponseEntity<Response<List<Map<String, Object>>>> response = sampleController.select(common,
				new BeanPropertyBindingResult(common, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

	@Test
	public void t50() throws Exception {
		System.out.println("================================================================================");
	}

//	@Test
	public void t51_get() throws Exception {
		Sample sample = new Sample();
		sample.setId(1L);

		ResponseEntity<Response<Sample>> response = sampleController.get2(sample, null,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t52_list() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");

		ResponseEntity<Response<List<Sample>>> response = sampleController.list2(sample, null,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest(), new MockHttpSession());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t53_count2() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");

		ResponseEntity<Response<Integer>> response = sampleController.count2(sample, null,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t54_page() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setOrder_by("id DESC");
		sample.setPage_number(1);

		ResponseEntity<Response<PageNavigator<Sample>>> response = sampleController.page(sample, (Common) null,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t55_insert() throws Exception {
		Sample sample = new Sample();
		sample.setName("s1");
		sample.setTest_id(3L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		ResponseEntity<Response<Long>> response = sampleController.insert2(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t56_update() throws Exception {
		Sample sample = new Sample();
//		sample.setId(1L);
		sample.setName("s2");
		sample.setMod_id("2");
		sample.addCondition("name LIKE 's1%'");

		ResponseEntity<Response<Integer>> response = sampleController.update2(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t57_delete() throws Exception {
		Sample sample = new Sample();
//		sample.setId(1L);
		sample.setName("s2");
		sample.addCondition("name LIKE 's2%'");

		ResponseEntity<Response<Integer>> response = sampleController.delete2(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t58_merge() throws Exception {
		Sample sample = new Sample();
//		sample.setId(1L);
		sample.setName("s1");
		sample.setTest_id(3L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		ResponseEntity<Response<Integer>> response = sampleController.merge(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}
}