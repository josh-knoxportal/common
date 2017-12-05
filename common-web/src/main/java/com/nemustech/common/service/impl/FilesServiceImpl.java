package com.nemustech.common.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.annotation.CacheEvictCommonClass;
import com.nemustech.common.annotation.TransactionalException;
import com.nemustech.common.file.Files;
import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.mapper.FilesMapper;
import com.nemustech.common.service.FilesService;
import com.nemustech.common.storage.FileStorage;
import com.nemustech.common.storage.LocalFileStorage;

@Service
public abstract class FilesServiceImpl<T extends Files> extends CommonServiceImpl<T> implements FilesService<T> {
	/**
	 * 파일 저장소
	 */
	@Autowired
	protected FileStorage fileStorage;

	/**
	 * 파일 매퍼
	 */
	@Autowired
	protected FilesMapper<T> mapper;

	@Override
	public CommonMapper<T> getMapper() {
		return mapper;
	}

	@Override
	@TransactionalException
	@CacheEvictCommonClass
	public Object insert(T model) throws Exception {
		insertFile(model);

		return super.insert(model);
	}

	@Override
	@TransactionalException
	@CacheEvictCommonClass
	public int update(T model) throws Exception {
		updateFile(model);

		return super.update(model);
	}

	@Override
	@TransactionalException
	@CacheEvictCommonClass
	public int delete(T model) throws Exception {
		deleteFile(LocalFileStorage.getRealFilePath(model.getId()));

		return super.delete(model);
	}

	/**
	 * 파일 등록
	 * 
	 * @param model
	 * @throws Exception
	 */
	protected void insertFile(T model) throws Exception {
		model.setPath(fileStorage.save(model));
	}

	/**
	 * 파일 수정
	 * 
	 * @param model
	 * @throws Exception
	 */
	protected void updateFile(T model) throws Exception {
		insertFile(model);
	}

	/**
	 * 파일 삭제
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	protected boolean deleteFile(String path) throws Exception {
		File file = new File(path);

		return file.delete();
	}
}