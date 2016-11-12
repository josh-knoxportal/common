package com.nemustech.sample.service.impl;

import com.nemustech.sample.Constants;
import com.nemustech.sample.model.SampleAndTest;
import com.nemustech.sample.service.SampleAndTestService;
import com.nemustech.web.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class SampleAndTestServiceImpl extends CommonServiceImpl<SampleAndTest> implements SampleAndTestService {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}