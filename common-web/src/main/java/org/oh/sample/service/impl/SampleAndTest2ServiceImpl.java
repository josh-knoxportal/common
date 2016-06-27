package org.oh.sample.service.impl;

import org.oh.sample.Constants;
import org.oh.sample.model.SampleAndTest2;
import org.oh.sample.service.SampleAndTest2Service;
import org.oh.web.service.impl.CommonServiceImpl;
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