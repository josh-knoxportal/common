package com.nemustech.web;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

import com.nemustech.WebApplication;
import com.nemustech.common.model.Default;
import com.nemustech.common.model.Response;
import com.nemustech.common.page.PageNavigator;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.MapperUtils;
import com.nemustech.sample.controller.SampleAndFilesController;
import com.nemustech.sample.controller.SampleAndTest2Controller;
import com.nemustech.sample.controller.SampleAndTestController;
import com.nemustech.sample.controller.Sample_TestController;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.model.SampleAndTest;
import com.nemustech.sample.model.SampleAndTest2;
import com.nemustech.sample.model.Sample_Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTest {
	@Autowired
	protected Sample_TestController sample_TestController;

	@Autowired
	protected SampleAndTestController sampleAndTestController;

	@Autowired
	protected SampleAndTest2Controller sampleAndTest2Controller;

	@Autowired
	protected SampleAndFilesController sampleAndFilesController;

//	@Test
	public void t01_list() throws Exception {
		Sample_Test sample_Test = new Sample_Test();

		ResponseEntity<Response<List<Sample_Test>>> response2 = sample_TestController.list(sample_Test,
				new BeanPropertyBindingResult(sample_Test, "sample_test"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response2));
	}

//	@Test
	public void t02_joinList() throws Exception {
		Sample sample = new Sample();
//		sample.setName("ss");

		com.nemustech.sample.model.Test test = new com.nemustech.sample.model.Test();
//		test.setName("tt");

		Files2 files = new Files2();

		SampleAndTest sat = new SampleAndTest(sample, test, files);
		sat.addCondition("sample_.name LIKE 's%'");
//		sat.addCondition("test_.name LIKE 't%'");
//		sat.addCondition("sample_.name", "IN", "ss");
//		sat.setOrder_by("sample_.id DESC, test_.id DESC");

//		sample.setSql_name("t05_joinList");
//		sat.setFields("sample_.name sample_name, COUNT(1) sample_count"); // skoh
//		sat.setGroup_by("sample_.name");
//		sat.setHaving("COUNT(1) > 1");
//		sat.setOrder_by("sample_.name");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		ResponseEntity<Response<List<SampleAndTest>>> response = sampleAndTestController.list(sat,
				new BeanPropertyBindingResult(sat, "sat"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());

		List<Default> list = MapperUtils.convertModels(response.getBody().getBody(), "testSet", "filesSet");
		System.out.println("list: " + JsonUtil2.toStringPretty(list));

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
	public void t03_joinPage() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");

		com.nemustech.sample.model.Test test = new com.nemustech.sample.model.Test();
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

	@Test
	public void t04_joinList2() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");

		com.nemustech.sample.model.Test test = new com.nemustech.sample.model.Test();
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
}