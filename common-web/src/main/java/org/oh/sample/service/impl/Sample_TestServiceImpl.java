package org.oh.sample.service.impl;

import org.oh.sample.Constants;
import org.oh.sample.model.Sample_Test;
import org.oh.sample.service.Sample_TestService;
import org.oh.web.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class Sample_TestServiceImpl extends CommonServiceImpl<Sample_Test> implements Sample_TestService {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}