package com.nemustech.web;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nemustech.common.file.Files;
import com.nemustech.common.service.FilesService;
import com.nemustech.common.storage.FileStorage;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.service.SampleAndFilesService;
import com.nemustech.sample.service.SampleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleServiceTest {
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

	@Autowired
	protected SampleAndFilesService sampleAndFilesService;

	/**
	 * 파일 서비스
	 */
	@Autowired
	protected FilesService filesService;

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
	public void t11_insert() throws Exception {
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
	public void t12_inserts() throws Exception {
		Sample model = new Sample();
		model.setName("s1");
		model.setTest_id(3L);
		model.setReg_id("1");
		model.setMod_id("1");

		Object result = sampleService.insert(Arrays.asList(new Sample[] { model, model }));
		System.out.println("result: " + result);
	}

//	@Test
	public void t13_list() throws Exception {
		Sample model = new Sample();
//		model.setId(1L);
//		model.addCondition("name LIKE 's%'");
		model.addCondition(model.newCondition("OR").add("name", "IN", "s1", "s2").add("name", "IN", "s3"));
//		model.setOrder_by("id DESC");

		List<Sample> result = sampleService.list(model);
		System.out.println("result: " + result);
//		Sample sample = new Sample();
//		Files2 files = new Files2();
//		SampleAndFiles model = new SampleAndFiles(sample, files);
//		model.addCondition("sample_.name = 's1'");
//
//		List<SampleAndFiles> list = sampleAndFilesService.list(model);
//		List<Default> list2 = MapperUtils.convertModels(list, "filesSet");
//		System.out.println("list: " + JsonUtil2.toStringPretty(list2));
	}

//	@Test
	public void t14_page() throws Exception {
		Sample model = new Sample();
//		model.setName("s");
//		model.addCondition("name LIKE 's%'");
		model.setOrder_by("id");

		model.setPage_number(2);
		model.setRows_per_page(3);

		List<Sample> response = sampleService.page(model);
		System.out.println("result: " + JsonUtil2.toStringPretty(response));

//		Page<Sample> page = new Page<Sample>(1);
//
//		Page<Sample> list_page = sampleService.page(model, page);
//		System.out.println("result: " + JsonUtil2.toStringPretty(list_page));
	}

//	@Test
	public void t15_update() throws Exception {
		Sample model = new Sample();
		model.addCondition("name = 's1'");
		model.setMod_id("2");

		Files files = new Files("2.txt", "2".getBytes());
		files.setReg_id("1");
		files.setMod_id("1");

		Object result = sampleService.update(model, Arrays.asList(new Files[] { files }));
		System.out.println("result: " + result);
	}

//	@Test
	public void t16_delete() throws Exception {
		Sample model = new Sample();
		model.setName("s1");

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
		model.setName("2.txt");

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