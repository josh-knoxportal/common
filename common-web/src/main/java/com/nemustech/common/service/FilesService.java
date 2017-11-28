package com.nemustech.common.service;

import java.util.List;

import com.nemustech.common.file.Files;

/**
 * 파일 서비스
 * 
 * @author skoh
 */
public interface FilesService<T extends Files> extends CommonService<T> {
//	/**
//	 * 파일 서비스를 구한다.
//	 * 
//	 * @return
//	 */
//	public FilesService getFileService();

	/**
	 * 공통 파일 등록
	 * 
	 * @param files 파일 리스트
	 * @return 결과
	 * @throws Exception
	 */
	public List<T> insertFile(List<T> files) throws Exception;

	/**
	 * 공통 파일 수정
	 * 
	 * @param files 파일 리스트
	 * @return 결과
	 * @throws Exception
	 */
	public List<T> updateFile(List<T> files) throws Exception;

	/**
	 * 공통 파일 삭제
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public List<T> deleteFile(List<T> files) throws Exception;
}