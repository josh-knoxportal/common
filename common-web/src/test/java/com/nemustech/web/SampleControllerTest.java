package com.nemustech.web;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mybatisorm.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.nemustech.WebApplication;
import com.nemustech.common.model.Common;
import com.nemustech.common.model.Response;
import com.nemustech.common.page.PageNavigator;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.sample.controller.SampleController;
import com.nemustech.sample.model.Sample;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleControllerTest {
	/**
	 * 샘플 컨트롤러
	 */
	@Autowired
	protected SampleController sampleController;

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
		System.out.println("sourceType: " + sampleController.getService().getSourceType());
		System.out.println("activeProfile" + sampleController.getService().getActiveProfile());
		Sample sample = new Sample();
		sample.setName("s");
////		sample.addCondition("name LIKE 's%'");
////		sample.addCondition("name", "LIKE", "s%");
////		sample.addCondition("name", "IN", "s1", "s2");
////		sample.addCondition("name", "BETWEEN", "s1", "s2");
////		sample.addCondition(sample.newCondition("OR").add("name LIKE 's%'").add("name", "LIKE", "s%"));
//		sample.setPage_number(1);
//
////		sample.setSql_name("t02_list");
////		sample.setHint("DISTINCT");
////		sample.setFields("name, COUNT(1) AS count");
////		sample.setTable("sample");
////		sample.setGroup_by("name");
////		sample.setHaving("COUNT(1) > 0");
////		sample.setOrder_by("name");
//
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
	public void t05_insert() throws Exception {
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
	public void t06_update() throws Exception {
		Sample sample = new Sample();
//		sample.setSql_name("t09_update");
//		sample.setTable("sample");
//		sample.setId(1L);
		sample.setName("s1");
		sample.setMod_id("2");
		sample.addCondition("name LIKE 's1%'");

		ResponseEntity<Response<Integer>> response = sampleController.update(sample,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t07_delete() throws Exception {
		Sample sample = new Sample();
//		sample.setSql_name("t10_delete");
//		sample.setTable("sample");
//		sample.setId(541L);
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

//	@Test
	public void t11_mapper() throws Exception {
		Sample sample = new Sample();

		sampleController.mapper(sample, new ModelAndView());
	}

	@Test
	public void t50() throws Exception {
		System.out.println("================================================================================");
	}

//	@Test
	public void t51_get() throws Exception {
//		while (true) {
//			try {
		Sample sample = new Sample();
		sample.setId(1L);

		ResponseEntity<Response<Sample>> response = sampleController.get(sample, null,
				new BeanPropertyBindingResult(sample, "sample"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Thread.currentThread().sleep(3000);
//		}
	}

//	@Test
	public void t52_list() throws Exception {
		Sample sample = new Sample();
		sample.setHint("DISTINCT");
		sample.setFields("id, COUNT(1) count");
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");
		sample.setGroup_by("id");
		sample.setHaving("COUNT(1) > 0");
		sample.setOrder_by("id DESC");

		ResponseEntity<Response<List<Sample>>> response = sampleController.list2(sample, null,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest(), new MockHttpSession());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t53_count() throws Exception {
		Sample sample = new Sample();
		sample.setName("s");
		sample.addCondition("name LIKE 's%'");

		ResponseEntity<Response<Integer>> response = sampleController.count(sample, null,
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

		ResponseEntity<Response<Object>> response = sampleController.insert(sample,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest());
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

		ResponseEntity<Response<Integer>> response = sampleController.update(sample,
				new BeanPropertyBindingResult(sample, "sample"), new MockHttpServletRequest());
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t57_delete() throws Exception {
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