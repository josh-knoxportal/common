package com.nemustech.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.file.Files;
import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.mapper.FilesMapper;
import com.nemustech.common.service.FilesService;
import com.nemustech.sample.Constants;

/**
 * 파일 서비스
 * 
 * @author skoh
 */
@Service
public abstract class FilesServiceImpl<T extends Files> extends CommonServiceImpl<T> implements FilesService<T> {
	@Autowired
	protected FilesMapper<T> mapper;

	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}

	@Override
	public CommonMapper<T> getMapper() {
		return mapper;
	}
}