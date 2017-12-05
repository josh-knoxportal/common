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
import org.springframework.test.context.web.WebAppConfiguration;

import com.nemustech.common.CommonTest;
import com.nemustech.common.file.Files;
import com.nemustech.common.storage.FileStorage;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.service.Files2Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Files2ServiceTest extends CommonTest {
	@Autowired
	protected FileStorage fileStorage;

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
			log.info(new String(file, "UTF-8"));
		}
	}

	@Test
	public void t10() throws Exception {
		log.info("================================================================================");
	}

//	@Test
	public void t11_list() throws Exception {
		Files2 model = new Files2();
		model.setName("2.txt");

		List<Files2> result = files2Service.list(model);
		log.info("result: " + result);
	}

//	@Test
	public void t12_insert() throws Exception {
		Files files = new Files("1.txt", "테스트1".getBytes());
		files.setReg_id("1");
		files.setMod_id("1");
		Files2 files1 = new Files2(files, "1");

		files = new Files("2.txt", "테스트2".getBytes());
		files.setReg_id("1");
		files.setMod_id("1");
		Files2 files2 = new Files2(files, "1");

		List<Object> result = files2Service.insert(Arrays.asList(new Files2[] { files1, files2 }));
		log.info("result: " + result);
	}

//	@Test
	public void t13_update() throws Exception {
		Files files = new Files("11.txt", "테스트11".getBytes());
		files.setId("2017113021371405956POK");
		files.setMod_id("11");
		Files2 files1 = new Files2(files, "11");

		files = new Files("22.txt", "테스트22".getBytes());
		files.setId("20171130213714059G1PTZ");
		files.setMod_id("22");
		Files2 files2 = new Files2(files, "22");

		int result = files2Service.update(Arrays.asList(new Files2[] { files1, files2 }));
		log.info("result: " + result);
	}

//	@Test
	public void t14_delete() throws Exception {
		Files files = new Files("2017113021371405956POK");
		Files2 files1 = new Files2(files, null);

		files = new Files("20171130213714059G1PTZ");
		Files2 files2 = new Files2(files, null);

		int result = files2Service.delete(Arrays.asList(new Files2[] { files1, files2 }));
		log.info("result: " + result);
	}
}