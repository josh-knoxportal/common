package com.nemustech.web;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

import com.nemustech.WebApplication;
import com.nemustech.common.model.Response;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.sample.controller.TestController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestControllerTest {
	@Autowired
	protected TestController controller;

	@Test
	public void t01_get() throws Exception {
		com.nemustech.sample.model.Test model = new com.nemustech.sample.model.Test();
		model.setName("t");

		ResponseEntity<Response<List<com.nemustech.sample.model.Test>>> response = controller.list(model, model,
				new BeanPropertyBindingResult(model, "model"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}
}