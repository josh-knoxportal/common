package org.oh.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.oh.WebApplication;
import org.oh.common.file.Files;
import org.oh.common.storage.FileStorage;
import org.oh.sample.model.Sample;
import org.oh.sample.service.Files2Service;
import org.oh.sample.service.SampleService;
import org.oh.web.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceTest {
	@Autowired
	protected FileStorage fileStorage;

	/**
	 * 공통 서비스
	 */
//	@Resource(name = "commonService")
//	protected CommonService<Default> commonService;

	/**
	 * 샘플 서비스
	 */
	@Autowired
	protected SampleService sampleService;

	/**
	 * 파일 서비스
	 */
	@Autowired
	protected FilesService filesService;

	/**
	 * 파일2 서비스
	 */
	@Autowired
	protected Files2Service files2Service;

//	@Test
	public void t01_save() throws Exception {
		FileUtils.writeByteArrayToFile(new File("/Users/skoh/git/skoh/common/common-web/storage/1.file"),
				"테스트".getBytes());
	}

//	@Test
	public void t02_save() throws Exception {
		for (int i = 0; i < 1; i++) {
			Files files = new Files("테스트.txt", "테스트".getBytes());

			fileStorage.save(files);

			byte[] file = fileStorage.load(files.getId());
			System.out.println(new String(file, "UTF-8"));
		}
	}

	@Test
	public void t10() throws Exception {
		System.out.println("================================================================================");
	}

//	@Test
	public void t11_list() throws Exception {
		Sample model = new Sample();
		model.setId(1L);

		List<Sample> result = sampleService.list(model);
		System.out.println("result: " + result);
	}

//	@Test
	public void t12_insert() throws Exception {
		Sample model = new Sample();
		model.setName("s1");
		model.setTest_id(3L);
		model.setReg_id("1");
		model.setMod_id("1");

		Files files = new Files("1.txt", "1".getBytes());
		files.setReg_id("1");
		files.setMod_id("1");

		Object result = sampleService.insert(model, Arrays.asList(new Files[] { files }));
		System.out.println("result: " + result);
	}

//	@Test
	public void t13_inserts() throws Exception {
		Sample model = new Sample();
		model.setName("s1");
		model.setTest_id(3L);
		model.setReg_id("1");
		model.setMod_id("1");

		Object result = sampleService.insert(Arrays.asList(new Sample[] { model, model }));
		System.out.println("result: " + result);
	}

//	@Test
	public void t14_update() throws Exception {
		Sample model = new Sample();
		model.setId(471L);
		model.setName("s1");
		model.setMod_id("2");

		Files files = new Files("2.txt", "2".getBytes());
		files.setReg_id("1");
		files.setMod_id("1");

		Object result = sampleService.update(model, Arrays.asList(new Files[] { files }));
		System.out.println("result: " + result);
	}

//	@Test
	public void t15_delete() throws Exception {
		Sample model = new Sample();
		model.setId(471L);

		Object result = sampleService.delete(model);
		System.out.println("result: " + result);
	}

	@Test
	public void t20() throws Exception {
		System.out.println("================================================================================");
	}

//	@Test
	public void t31_list() throws Exception {
		Files model = new Files();
		model.setId("201610061204378114BNFL");

		List<Files> result = filesService.list(model);
		System.out.println("result: " + result);
	}

//	@Test
	public void t32_insert() throws Exception {
		Files files = new Files("p1", "n1", "1".getBytes());
		files.setReg_id("1");
		files.setMod_id("1");

		Object result = filesService.insert(files);
		System.out.println("result: " + result);
	}
}