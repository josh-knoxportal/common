package com.nemustech.common.service.impl;

import com.nemustech.common.file.Files;
import com.nemustech.common.service.FilesService;
import com.nemustech.sample.Constants;

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