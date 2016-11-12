package com.nemustech.web.service.impl;

import com.nemustech.common.file.Files;
import com.nemustech.sample.Constants;
import com.nemustech.web.service.FilesService;
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