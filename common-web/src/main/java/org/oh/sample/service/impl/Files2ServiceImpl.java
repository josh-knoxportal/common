package org.oh.sample.service.impl;

import org.oh.sample.Constants;
import org.oh.sample.model.Files2;
import org.oh.sample.service.Files2Service;
import org.oh.web.service.impl.CommonServiceImpl;
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