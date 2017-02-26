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
import com.nemustech.common.model.Response;
import com.nemustech.common.page.PageNavigator;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.sample.controller.GroupController;
import com.nemustech.sample.model.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupControllerTest {
	@Autowired
	protected GroupController groupController;

	@Test
	public void t02_list() throws Exception {
		Group group = new Group();
		group.setName("s");
//
		ResponseEntity<Response<List<Group>>> response = groupController.list(group,
				new BeanPropertyBindingResult(group, "group"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}

//	@Test
	public void t04_page() throws Exception {
		Group group = new Group();
		group.setName("s");
		group.addCondition("name LIKE 's%'");
		group.setOrder_by("id DESC");

		Page<Group> page = new Page<Group>(1);

		ResponseEntity<Response<PageNavigator<Group>>> response = groupController.page(group, page,
				new BeanPropertyBindingResult(group, "group"));
		System.out.println("response: " + JsonUtil2.toStringPretty(response));
		Assert.assertTrue("Fail", response.getBody().getHeader().getSuccess_yn());
	}
}