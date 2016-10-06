package org.oh.sample.service.impl;

import org.oh.sample.Constants;
import org.oh.sample.model.SampleAndFiles;
import org.oh.sample.service.SampleAndFilesService;
import org.oh.web.service.impl.CommonServiceImpl;
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