package org.oh.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oh.common.util.ReflectionUtil;
import org.oh.web.cache.CacheEvictSample;
import org.oh.web.cache.CacheableSample;
import org.oh.web.mapper.SampleMapper;
import org.oh.web.model.Sample;
import org.oh.web.page.Paging;
import org.oh.web.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class SampleServiceImpl implements SampleService {
	/**
	 * 샘플 매퍼
	 */
	@Autowired
	protected SampleMapper sampleMapper;

	@Override
	public Sample get(Sample sample) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(ReflectionUtil.convertObjectToMap(sample));
		List<Sample> list = sampleMapper.list(params);
		return (list.size() > 0) ? list.get(0) : null;
	}

	@Override
	@CacheableSample
	public List<Sample> list(Sample sample) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(ReflectionUtil.convertObjectToMap(sample));
		return sampleMapper.list(params);
	}

	@Override
	public List<Sample> page(Sample sample, Paging paging) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(ReflectionUtil.convertObjectToMap(sample));
		params.putAll(ReflectionUtil.convertObjectToMap(paging));
		return sampleMapper.list(params);
	}

	@Override
	@CacheEvictSample
	public int insert(Sample sample) throws Exception {
		return sampleMapper.insert(sample);
	}

	@Override
	@CacheEvictSample
	public int update(Sample sample) throws Exception {
		return sampleMapper.update(sample);
	}

	@Override
	@CacheEvictSample
	public int delete(Sample sample) throws Exception {
		return sampleMapper.delete(sample);
	}

	@Override
	@CacheEvictSample
	public int merge(Sample sample) throws Exception {
		return sampleMapper.merge(sample);
	}
}