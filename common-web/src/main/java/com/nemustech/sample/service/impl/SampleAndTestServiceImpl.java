package com.nemustech.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.Constants;
import com.nemustech.sample.mapper.SampleAndTestMapper;
import com.nemustech.sample.model.SampleAndTest;
import com.nemustech.sample.service.SampleAndTestService;

/**
 * @author skoh
 */
@Service
public class SampleAndTestServiceImpl extends CommonServiceImpl<SampleAndTest> implements SampleAndTestService {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}

	@Autowired
	protected SampleAndTestMapper mapper;

	@Override
	public CommonMapper<SampleAndTest> getMapper() {
		return mapper;
	}
}