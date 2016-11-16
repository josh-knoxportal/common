package com.nemustech.sample.service.impl;

import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.Constants;
import com.nemustech.sample.model.Sample_Test;
import com.nemustech.sample.service.Sample_TestService;

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