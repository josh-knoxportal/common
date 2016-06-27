package org.oh.sample.service.impl;

import org.oh.sample.Constants;
import org.oh.sample.model.Test;
import org.oh.sample.service.TestService;
import org.oh.web.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class TestServiceImpl extends CommonServiceImpl<Test> implements TestService {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}