package com.nemustech.platform.lbs.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nemustech.platform.lbs.common.service.MailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/root-context.xml")
@WebAppConfiguration("/src/test/resources")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapServiceTest {
	@Autowired
	protected MailService service;

	@Test
	public void test01() throws Exception {
		service.sendMail("from_mail", "to_mail", "title", "content");
	}
}
