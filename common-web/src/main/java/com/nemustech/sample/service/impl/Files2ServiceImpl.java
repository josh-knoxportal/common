package com.nemustech.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.service.impl.FilesServiceImpl;
import com.nemustech.sample.mapper.Files2Mapper;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.service.Files2Service;

/**
 * 파일2 서비스
 * 
 * @author skoh
 */
@Service
public class Files2ServiceImpl extends FilesServiceImpl<Files2> implements Files2Service {
	@Autowired
	protected Files2Mapper mapper;

	@Override
	public CommonMapper<Files2> getMapper() {
		return mapper;
	}
}