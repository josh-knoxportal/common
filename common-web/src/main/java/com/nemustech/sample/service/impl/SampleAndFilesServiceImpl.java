package com.nemustech.sample.service.impl;

import com.nemustech.sample.Constants;
import com.nemustech.sample.model.SampleAndFiles;
import com.nemustech.sample.service.SampleAndFilesService;
import com.nemustech.web.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class SampleAndFilesServiceImpl extends CommonServiceImpl<SampleAndFiles> implements SampleAndFilesService {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}