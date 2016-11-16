package com.nemustech.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.nemustech.common.annotation.TransactionalException;
import com.nemustech.common.file.Files;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.common.util.Utils;
import com.nemustech.sample.Constants;
import com.nemustech.sample.annotation.CacheEvictSample;
import com.nemustech.sample.annotation.CacheableSample;
import com.nemustech.sample.mapper.SampleMapper;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.service.Files2Service;
import com.nemustech.sample.service.SampleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class SampleServiceImpl extends CommonServiceImpl<Sample> implements SampleService {
	@Autowired
	protected SampleMapper sampleMapper;

	@Lazy
	@Autowired
	protected Files2Service files2Service;

	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}

	@Override
	public Sample get2(Sample sample) throws Exception {
		List<Sample> list = sampleMapper.list(sample);
		return (list.size() > 0) ? list.get(0) : null;
	}

	@Override
	@CacheableSample
	public List<Sample> list2(Sample sample) throws Exception {
		return sampleMapper.list(sample);
	}

	@Override
	public int count2(Sample sample) throws Exception {
		return sampleMapper.count(sample);
	}

	@Override
	public List<Sample> page(Sample sample) throws Exception {
		return sampleMapper.list(sample);
	}

	@Override
	@TransactionalException
	@CacheEvictSample
	public int insert2(Sample sample) throws Exception {
		return sampleMapper.insert(sample);
	}

	@Override
	@TransactionalException
	@CacheEvictSample
	public int update2(Sample sample) throws Exception {
		return sampleMapper.update(sample);
	}

	@Override
	@TransactionalException
	@CacheEvictSample
	public int delete2(Sample sample) throws Exception {
		return sampleMapper.delete(sample);
	}

	@Override
	@TransactionalException
	@CacheEvictSample
	public int merge(Sample sample) throws Exception {
		return sampleMapper.merge(sample);
	}

	@Override
	protected List<Object> insertFile(Sample model, List<Files> files) throws Exception {
		List<Files2> filesList = new ArrayList<Files2>();
		for (Files file : files) {
			file.setFile_path(fileStorage.save(file));

			filesList.add(new Files2(file, getId(model).toString()));
		}

		return files2Service.insert(filesList);
	}

	@Override
	protected List<Object> updateFile(Sample model, List<Files> files) throws Exception {
		if (Utils.isValidate(files)) {
			deleteFile(model);
		}

		return super.updateFile(model, files);
	}

	@Override
	protected int deleteFile(Sample model) throws Exception {
		Files2 files2 = new Files2();
		files2.addCondition("doc_id = '" + getId(model) + "'");

		return files2Service.delete(files2);
	}
}