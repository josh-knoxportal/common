package com.nemustech.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.nemustech.common.annotation.TransactionalException;
import com.nemustech.common.file.Files;
import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.service.FilesService;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.common.util.Utils;
import com.nemustech.sample.Constants;
import com.nemustech.sample.annotation.CacheEvictSample;
import com.nemustech.sample.mapper.SampleMapper;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.model.Sample.Sample2;
import com.nemustech.sample.service.Files2Service;
import com.nemustech.sample.service.SampleService;

/**
 * 샘플 서비스
 * 
 * @author skoh
 */
@Service
public class SampleServiceImpl extends CommonServiceImpl<Sample> implements SampleService {
	@Autowired
	protected SampleMapper mapper;

	@Lazy
	@Autowired
	protected Files2Service files2Service;

	@Override
	public CommonMapper<Sample> getMapper() {
		return mapper;
	}

	@Override
	public FilesService getFileService() {
		return files2Service;
	}

	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}

//	@Override
//	public Sample get2(Sample sample) throws Exception {
//		List<Sample> list = mapper.list(sample);
//		return (list.size() > 0) ? list.get(0) : null;
//	}
//
//	@Override
//	@CacheableSample
//	public List<Sample> list2(Sample sample) throws Exception {
//		return mapper.list(sample);
//	}
//
//	@Override
//	public int count2(Sample sample) throws Exception {
//		return mapper.count(sample);
//	}
//
//	@Override
//	public List<Sample> page2(Sample sample) throws Exception {
//		return mapper.list(sample);
//	}
//
//	@Override
//	@TransactionalException
//	@CacheEvictSample
//	public int insert2(Sample sample) throws Exception {
//		return mapper.insert(sample);
//	}
//
//	@Override
//	@TransactionalException
//	@CacheEvictSample
//	public int update2(Sample sample) throws Exception {
//		return mapper.update(sample);
//	}
//
//	@Override
//	@TransactionalException
//	@CacheEvictSample
//	public int delete2(Sample sample) throws Exception {
//		return mapper.delete(sample);
//	}

	@Override
	@TransactionalException
	@CacheEvictSample
	public int merge(Sample sample) throws Exception {
		return mapper.merge(sample);
	}

	@Override
	protected List<Object> insertFile(Sample model, List<Files> files) throws Exception {
		List<Files2> filesList = new ArrayList<Files2>();
		for (Files file : files) {
			file.setPath(fileStorage.save(file));

			filesList.add(new Files2(file, getId(model).toString()));
		}

		return files2Service.insert(filesList);
	}

	@Override
	protected List<Object> updateFile(Sample model, List<Files> files) throws Exception {
		if (Utils.isValidate(files)) {
			deleteFile(model, files);
		}

		return super.updateFile(model, files);
	}

	@Override
	protected int deleteFile(Sample model, List<Files> files) throws Exception {
		int result = 0;

		List<Sample> list = list(model);
		for (Sample sample : list) {
			Files2 files2 = new Files2();
			files2.addCondition("doc_id = '" + sample.getId() + "'");

			result += files2Service.delete(files2);
		}

		return result;
	}

	@Override
	public List<Sample> list3(Sample2 model) throws Exception {
		model.setSql_name("list3");
		model.setHint("DISTINCT");
		model.setFields("id, name");
		model.setOrder_by("id DESC");

		return list(model);
	}
}