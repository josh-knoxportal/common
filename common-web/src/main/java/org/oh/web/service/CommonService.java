package org.oh.web.service;

import java.util.List;

import org.mybatisorm.Page;
import org.oh.web.model.Default;

/**
 * 공통 서비스
 * 
 * @author skoh
 */
public interface CommonService<T extends Default> {
	/**
	 * 캐쉬명 정의
	 * 
	 * @return 캐쉬명
	 */
	public String getCacheName();

	/**
	 * 공통 조회
	 * 
	 * @param t 모델
	 * 
	 * @return T
	 * 
	 * @throws Exception 1
	 */
	public T get(T t) throws Exception;

	/**
	 * 공통 목록 조회
	 * 
	 * @param t 모델
	 * 
	 * @return List<T>
	 * 
	 * @throws Exception
	 */
	public List<T> list(T t) throws Exception;

	/**
	 * 공통 건수 조회
	 * 
	 * @param t 모델
	 * 
	 * @return 건수
	 * 
	 * @throws Exception
	 */
	public int count(T t) throws Exception;

	/**
	 * 공통 목록(페이지) 조회
	 * 
	 * @param t 모델
	 * @param page 페이지
	 * 
	 * @return Page<T>
	 * 
	 * @throws Exception
	 */
	public Page<T> page(T t, Page<T> page) throws Exception;

	/**
	 * 공통 등록
	 * 
	 * @param t 모델
	 * 
	 * @throws Exception
	 */
	public void insert(T t) throws Exception;

	/**
	 * 공통 수정
	 * 
	 * @param t 모델
	 * 
	 * @throws Exception
	 */
	public void update(T t) throws Exception;

	/**
	 * 공통 삭제
	 * 
	 * @param t 모델
	 * 
	 * @throws Exception
	 */
	public void delete(T t) throws Exception;
}