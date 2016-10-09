package org.oh.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.oh.common.annotation.TransactionalException;
import org.oh.common.file.Files;
import org.oh.common.util.Utils;
import org.oh.sample.Constants;
import org.oh.sample.annotation.CacheEvictSample;
import org.oh.sample.annotation.CacheableSample;
import org.oh.sample.mapper.SampleMapper;
import org.oh.sample.model.Files2;
import org.oh.sample.model.Sample;
import org.oh.sample.service.Files2Service;
import org.oh.sample.service.SampleService;
import org.oh.web.service.impl.CommonServiceImpl;
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