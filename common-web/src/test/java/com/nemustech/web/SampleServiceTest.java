package com.nemustech.web;

import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nemustech.common.CommonTest;
import com.nemustech.common.file.Files;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.service.SampleAndFilesService;
import com.nemustech.sample.service.SampleFilesService;
import com.nemustech.sample.service.SampleService;
import com.nemustech.sample.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleServiceTest extends CommonTest {
	@Autowired
	protected SampleService sampleService;

	@Autowired
	protected SampleFilesService sampleFilesService;

	@Autowired
	protected TestService testService;

	@Autowired
	protected SampleAndFilesService sampleAndFilesService;

	@Test
	public void t11_insert() throws Exception {
		Object result = "1";

		Sample model = new Sample();
		model.setName("s1");
		model.setTest_id(3L);
		model.setReg_id("1");
		model.setMod_id("1");

//		result = sampleService.insert(model);
//		log.info("result: " + result);
//		log.info("model: " + model);

		Files files = new Files("1.txt", "테스트1".getBytes());
		Files2 files1 = new Files2(files);

		files = new Files("2.txt", "테스트2".getBytes());
		Files2 files2 = new Files2(files);

		result = sampleFilesService.insert(model, Arrays.asList(new Files2[] { files1, files2 }));
		log.info("result: " + result);
	}

//	@Test
	public void t12_inserts() throws Exception {
		Sample model = new Sample();
		model.setName("s1");
		model.setTest_id(3L);
		model.setReg_id("1");
		model.setMod_id("1");

		Sample model2 = new Sample();
		model2.setName("s1");
		model2.setTest_id(3L);
		model2.setReg_id("1");
		model2.setMod_id("1");

		List<Sample> list = Arrays.asList(model, model2);
		Object result = sampleService.insert(list);
		Object result2 = sampleService.getIds(list);
		log.info("result: " + result);
		log.info("result2: " + result2);
		log.info("model: " + model);
		log.info("model2: " + model2);
	}

//	@Test
	public void t13_list() throws Exception {
		Sample model = new Sample();
//		model.setId(1L);
		model.addCondition("name LIKE 's%'");
//		model.addCondition(model.newCondition("OR").add("name", "IN", "s1", "s2").add("name", "IN", "s3"));
//		model.setOrder_by("id DESC");

		List<Sample> result = sampleService.list(model);
		log.info("result: " + result);

//		com.nemustech.sample.model.Test model2 = new com.nemustech.sample.model.Test();
//		model2.addCondition("name LIKE 't%'");
//		testService.list(model2);
//
//		sampleService.clearCache();
//		sampleService.clearCacheClass();
//		sampleService.clearCacheClass(SampleServiceImpl.class);
//		sampleService.clearCache(
//				"common_class com.nemustech.sample.service.impl.SampleServiceImpl_list_[com.nemustech.sample.model.Sample[id=<null>,name=<null>,test_id=<null>,count=<null>,testSet=[],filesSet=[],entityManager=<null>,reg_id=<null>,reg_dt=<null>,mod_id=<null>,mod_dt=<null>,page_number=0,rows_per_page=10,page_group_count=10,total_sise=0,sql_name=<null>,hint=<null>,fields=<null>,table=<null>,group_by=<null>,having=<null>,order_by=<null>,condition=name LIKE 's%',properties=<null>]]");
//
//		sampleService.list(model);
//		testService.list(model2);

//		Sample sample = new Sample();
//		Files2 files = new Files2();
//		SampleAndFiles model = new SampleAndFiles(sample, files);
//		model.addCondition("sample_.name = 's1'");
//
//		List<SampleAndFiles> list = sampleAndFilesService.list(model);
//		List<Default> list2 = MapperUtils.convertModels(list, "files");
//		log.info("list: " + JsonUtil2.toStringPretty(list2));
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
		log.info("result: " + JsonUtil2.toStringPretty(response));

//		Page<Sample> page = new Page<Sample>(1);
//
//		Page<Sample> list_page = sampleService.page(model, page);
//		log.info("result: " + JsonUtil2.toStringPretty(list_page));
	}

//	@Test
	public void t15_update() throws Exception {
		Sample model = new Sample();
		model.setId(34L); // sampleFilesService.update() 시 필요
		model.setName("s2");
//		model.addCondition("name LIKE 's1%'");
		model.setMod_id("2");

//		Object result = sampleService.update(model);
//		Object result = sampleService.update(Arrays.asList(model));
//		log.info("result: " + result);
//		log.info("model: " + model);

		Files files = new Files("11.txt", "테스트11".getBytes());
		files.setId("201712011543544113CZ8U");
		Files2 files1 = new Files2(files);

		files = new Files("22.txt", "테스트22".getBytes());
		files.setId("20171201154354411WOFKJ");
		Files2 files2 = new Files2(files);

		int result = sampleFilesService.update(model, Arrays.asList(new Files2[] { files1, files2 }));
		log.info("result: " + result);
	}

//	@Test
	public void t16_delete() throws Exception {
		Sample model = new Sample();
		model.setName("s2");

//		Object result = sampleService.delete(model);
//		log.info("result: " + result);

		Files files = new Files("201712011543544113CZ8U");
		Files2 files1 = new Files2(files, null);

		files = new Files("20171201154354411WOFKJ");
		Files2 files2 = new Files2(files, null);

		int result = sampleFilesService.delete(model, Arrays.asList(new Files2[] { files1, files2 }));
		log.info("result: " + result);
	}

	@Test
	public void t20() throws Exception {
		log.info("================================================================================");
	}

//	@Test
	public void t31_test() throws Exception {
		log.info(sampleService.getDefaultDateValue());
	}
}