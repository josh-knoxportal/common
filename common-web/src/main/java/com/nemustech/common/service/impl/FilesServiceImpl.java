package com.nemustech.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.file.Files;
import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.mapper.FilesMapper;
import com.nemustech.common.service.FilesService;
import com.nemustech.common.storage.FileStorage;

/**
 * 파일 서비스
 * 
 * @author skoh
 */
@Service
public abstract class FilesServiceImpl<T extends Files> extends CommonServiceImpl<T> implements FilesService<T> {
	/**
	 * 파일 저장소
	 */
	@Autowired
	protected FileStorage fileStorage;

	@Autowired
	protected FilesMapper<T> mapper;

	@Override
	public CommonMapper<T> getMapper() {
		return mapper;
	}

//	@Override
//	public FilesService getFileService() {
//		return files2Service;
//	}

	void test(T model, List<T> models) {
//		List<Files> files = getFiles(model, request);
//		files = filesService.insertFile(files);
//
//		// 파일 생성
//		if (!(model instanceof Files))
//			insertFile(model, files);
//
//		// 파일 수정
//		if (!(model instanceof Files)) {
//			String condition = model.getCondition();
//			model = (T) model.getClass().newInstance();
//			model.addCondition(condition);
//			updateFile(model, files);
//		}
//
//		// 파일 삭제
//		if (!(model instanceof Files))
//			deleteFile(model, files);
//
//		for (T model : models) {
//			// 파일 삭제
//			if (!(model instanceof Files))
//				deleteFile(model);
//		}
	}

	/**
	 * 파일 등록
	 * 
	 * @param files
	 * @throws Exception
	 */
	@Override
	public List<T> insertFile(List<T> files) throws Exception {
		for (Files file : files) {
			file.setPath(fileStorage.save(file));
		}

		return files;
	}

	/**
	 * 파일 수정
	 * 
	 * <pre>
	 * - 기존 파일은 삭제하지 않음
	 * - 파일 정보는 삭제하고 다시 생성함
	 * </pre>
	 * 
	 * @param files
	 * @throws Exception
	 */
	@Override
	public List<T> updateFile(List<T> files) throws Exception {
		deleteFile(files);

		return insertFile(files);
	}

	/**
	 * 파일 삭제
	 * 
	 * <pre>
	 * - 기존 파일은 삭제하지 않음
	 * - 파일 정보는 삭제함
	 * </pre>
	 * 
	 * @param files
	 * @throws Exception
	 */
	@Override
	public List<T> deleteFile(List<T> files) throws Exception {
		return files;
	}
}