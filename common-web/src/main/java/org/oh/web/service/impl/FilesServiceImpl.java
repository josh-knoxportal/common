package org.oh.web.service.impl;

import org.oh.common.file.Files;
import org.oh.sample.Constants;
import org.oh.web.service.FilesService;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service
public class FilesServiceImpl extends CommonServiceImpl<Files> implements FilesService {
	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}