package com.nemustech.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nemustech.common.annotation.CacheEvictCommonClass;
import com.nemustech.common.annotation.TransactionalException;
import com.nemustech.common.file.Files;
import com.nemustech.common.model.Common;
import com.nemustech.common.model.Default;
import com.nemustech.common.service.CommonFilesService;
import com.nemustech.common.service.FilesService;

@Service
public abstract class CommonFilesServiceImpl<T extends Default, F extends Files> extends CommonServiceImpl<T>
		implements CommonFilesService<T, F> {
	/**
	 * 파일 서비스
	 * 
	 * @return
	 */
	public abstract FilesService<F> getFileService();

	@Override
	@TransactionalException
	@CacheEvictCommonClass
	public Object insert(T model, List<F> files) throws Exception {
		// 공통
		Object id = super.insert(model);

		// 파일
		for (F file : files) {
			setFileInsert(model, file);
		}
		getFileService().insert(files);

		return id;
	}

	@Override
	@TransactionalException
	@CacheEvictCommonClass
	public int update(T model, List<F> files) throws Exception {
		// 공통
		int id = super.update(model);

		// 파일
		for (F file : files) {
			setFielUpdate(model, file);
		}
		getFileService().update(files);

		return id;
	}

	@Override
	@TransactionalException
	@CacheEvictCommonClass
	public int delete(T model, List<F> files) throws Exception {
		// 공통
		int id = super.delete(model);

		// 파일
		getFileService().delete(files);

		return id;
	}

	/**
	 * 파일 생성시 model 객체를 files 객체에 바인딩할 경우에 Override하여 사용
	 * 
	 * @param model
	 * @param files
	 * @return
	 */
	protected void setFileInsert(T model, F files) throws Exception {
		if (model instanceof Common) {
			Common common = (Common) model;
			files.setReg_id(common.getReg_id());
			files.setMod_id(common.getMod_id());
		}
	}

	/**
	 * 파일 수정시 model 객체를 files 객체에 바인딩할 경우에 Override하여 사용
	 * 
	 * @param model
	 * @param files
	 * @return
	 */
	protected void setFielUpdate(T model, F files) throws Exception {
		if (model instanceof Common) {
			Common common = (Common) model;
			files.setMod_id(common.getMod_id());
		}
	}
}