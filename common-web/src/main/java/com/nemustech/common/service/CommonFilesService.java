package com.nemustech.common.service;

import java.util.List;

import com.nemustech.common.file.Files;
import com.nemustech.common.model.Default;

/**
 * 공통 파일 서비스
 * 
 * @author skoh
 */
public interface CommonFilesService<T extends Default, F extends Files> extends CommonService<T> {
	/**
	 * 단일 모델의 복수 파일 등록
	 * 
	 * @param model
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public Object insert(T model, List<F> files) throws Exception;

	/**
	 * 단일 모델의 복수 파일 수정
	 * 
	 * @param model
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public int update(T model, List<F> files) throws Exception;

	/**
	 * 단일 모델의 복수 파일 삭제
	 * 
	 * @param model
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public int delete(T model, List<F> files) throws Exception;
}