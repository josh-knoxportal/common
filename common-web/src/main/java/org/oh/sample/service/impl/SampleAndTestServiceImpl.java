package org.oh.sample.service.impl;

import org.oh.sample.Constants;
import org.oh.sample.model.SampleAndTest;
import org.oh.sample.service.SampleAndTestService;
import org.oh.web.service.impl.CommonServiceImpl;
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