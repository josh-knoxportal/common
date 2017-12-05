package com.nemustech.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.service.FilesService;
import com.nemustech.common.service.impl.CommonFilesServiceImpl;
import com.nemustech.sample.mapper.SampleMapper;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.service.SampleFilesService;

@Service
public class SampleFilesServiceImpl extends CommonFilesServiceImpl<Sample, Files2> implements SampleFilesService {
	/**
	 * 샘플 매퍼
	 */
	@Autowired
	protected SampleMapper mapper;

	/**
	 * 파일 서비스
	 */
	@Autowired
	protected FilesService<Files2> filesService;

	@Override
	public CommonMapper<Sample> getMapper() {
		return mapper;
	}

	@Override
	public FilesService<Files2> getFileService() {
		return filesService;
	}

	@Override
	protected void setFileInsert(Sample model, Files2 files) throws Exception {
		super.setFileInsert(model, files);

		files.setDoc_id(model.getId().toString());
	}
}