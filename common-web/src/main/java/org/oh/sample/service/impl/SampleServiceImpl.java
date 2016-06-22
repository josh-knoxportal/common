package org.oh.sample.service.impl;

import java.util.List;

import org.oh.common.cache.CacheEvictCommon;
import org.oh.sample.cache.CacheEvictSample;
import org.oh.sample.cache.CacheableSample;
import org.oh.sample.mapper.SampleMapper;
import org.oh.sample.model.Sample;
import org.oh.sample.service.SampleService;
import org.oh.web.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class SampleServiceImpl extends CommonServiceImpl<Sample> implements SampleService {
	@Autowired
	protected SampleMapper sampleMapper;

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
	@CacheEvictSample
	public int insert2(Sample sample) throws Exception {
		return sampleMapper.insert(sample);
	}

	@Override
	@CacheEvictSample
	public int update2(Sample sample) throws Exception {
		return sampleMapper.update(sample);
	}

	@Override
	@CacheEvictSample
	public int delete2(Sample sample) throws Exception {
		return sampleMapper.delete(sample);
	}

	@Override
	@CacheEvictSample
	public int merge(Sample sample) throws Exception {
		return sampleMapper.merge(sample);
	}
}