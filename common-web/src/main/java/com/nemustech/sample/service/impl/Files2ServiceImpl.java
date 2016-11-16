package com.nemustech.sample.service.impl;

import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.Constants;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.service.Files2Service;

import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class Files2ServiceImpl extends CommonServiceImpl<Files2> implements Files2Service {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}