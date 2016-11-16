package com.nemustech.sample.service.impl;

import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.Constants;
import com.nemustech.sample.model.SampleAndTest2;
import com.nemustech.sample.service.SampleAndTest2Service;

import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class SampleAndTest2ServiceImpl extends CommonServiceImpl<SampleAndTest2> implements SampleAndTest2Service {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}