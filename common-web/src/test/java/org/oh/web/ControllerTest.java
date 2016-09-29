package org.oh.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.oh.WebApplication;
import org.oh.common.storage.LocalFileStorageAccessor;
import org.oh.common.storage.StorageAccessor;
import org.oh.common.util.JsonUtil2;
import org.oh.sample.controller.SampleAndTest2Controller;
import org.oh.sample.controller.SampleAndTestController;
import org.oh.sample.controller.SampleController;
import org.oh.sample.model.Sample;
import org.oh.sample.model.SampleAndTest;
import org.oh.sample.model.SampleAndTest2;
import org.oh.web.common.Response;
import org.oh.web.model.Common;
import org.oh.web.page.PageNavigator;
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
	private static Log log = LogFactory.getLog(ControllerTest.class);

	@Autowired
	protected StorageAccessor storageAccessor;

	/**
	 * 공통 서비스
	 */
//	@Resource(name = "commonService")
//	protected CommonService<Default> commonService;

	/**
	 * 샘플 컨트롤러
	 */
	@Autowired
	protected SampleController sampleController;

	@Autowired
	protected SampleAndTestController sampleAndTestController;

	@Autowired
	protected SampleAndTest2Controller sampleAndTest2Controller;

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
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.addCondition(sample.newCondition().add("name LIKE 's%'").add("name", "LIKE", "s%"));

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
		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
		test.setName("t");

		SampleAndTest sat = new SampleAndTest(sample, test);
		sat.addCondition("sample_.name LIKE 's%'");
		sat.addCondition("test_.name LIKE 't%'");
		sat.addCondition("sample_.name", "IN", "s");
		sat.setOrder_by("sample_.id DESC, test_.id DESC");

//		JoinHandler handler = new JoinHandler(SampleAndTest.class);
//		System.out.println(handler.getName());

		ResponseEntity<Response<List<SampleAndTest>>> response = sampleAndTestController.list(sat,
				new BeanPropertyBindingResult(sat, "sat"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t06_joinPage() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");

		org.oh.sample.model.Test test = new org.oh.sample.model.Test();
		test.setName("t");

		SampleAndTest sat = new SampleAndTest(sample, test);
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
		sample.setName("s1");
		sample.setTest_id(3L);
		sample.setReg_id("1");
		sample.setMod_id("1");

		ResponseEntity<Response<Long>> response = sampleController.insert(sample,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t09_update() throws Exception {
		Sample sample = new Sample();
//		sample.setId(1L);
		sample.setName("s2");
		sample.setMod_id("2");
		sample.addCondition("name LIKE 's1%'");

		ResponseEntity<Response<Integer>> response = sampleController.update(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t10_delete() throws Exception {
		Sample sample = new Sample();
//		sample.setId(1L);
		sample.setName("s2");
		sample.addCondition("name LIKE 's2%'");

		ResponseEntity<Response<Integer>> response = sampleController.delete(sample,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t11_select() throws Exception {
		Common common = new Common();
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
		log.info("================================================================================");
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

	@Test
	public void t90() throws Exception {
		log.info("================================================================================");
	}

	@Test
	public void t91() throws Exception {
		for (int i = 0; i < 1; i++) {
			String UID = LocalFileStorageAccessor.generateUID();

			storageAccessor.save(UID, "테스트.txt", "테스트".getBytes());
//			System.out.println("fileInfo: " + storageAccessor.getFileInfo(UID));

			byte[] file = storageAccessor.load(UID);
			System.out.println(new String(file, "UTF-8"));
		}
	}
}