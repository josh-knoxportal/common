package org.oh.web.service;

import java.util.List;
import java.util.Map;

import org.mybatisorm.Condition;
import org.mybatisorm.Page;
import org.oh.common.file.Files;
import org.oh.common.model.Default;
import org.springframework.web.multipart.MultipartFile;

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
	 * @param model 모델
	 * 
	 * @return T
	 * 
	 * @throws Exception 1
	 */
	public T get(T model) throws Exception;

	/**
	 * 공통 목록 조회
	 * 
	 * @param model 모델
	 * 
	 * @return List<T>
	 * 
	 * @throws Exception
	 */
	public List<T> list(T model) throws Exception;

	/**
	 * 데이타 조회용
	 * 
	 * @param model 모델
	 * @param condition 조건
	 * @param orderBy 정렬방식
	 * @param hint 힌트
	 * @param fields 필드
	 * @param table 테이블
	 * @param sqlName SQL명
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public List<Map<String, Object>> select(Map<String, Object> model, Condition condition, String orderBy, String hint,
			String fields, String table, String sqlName) throws Exception;

	/**
	 * 공통 건수 조회
	 * 
	 * @param model 모델
	 * 
	 * @return 건수
	 * 
	 * @throws Exception
	 */
	public int count(T model) throws Exception;

	/**
	 * 공통 목록(페이지) 조회
	 * 
	 * @param model 모델
	 * @param page 페이지
	 * 
	 * @return Page<T>
	 * 
	 * @throws Exception
	 */
	public Page<T> page(T model, Page<T> page) throws Exception;

	/**
	 * 공통 등록
	 * 
	 * @param model 모델
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public Object insert(T model) throws Exception;

	/**
	 * 공통 등록
	 * 
	 * @param model 모델
	 * @param files 파일 리스트
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public Object insert(T model, List<Files> files) throws Exception;

	/**
	 * 공통 리스트 등록
	 * 
	 * @param models 모델 리스트
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public List<Object> insert(List<T> models) throws Exception;

	/**
	 * 공통 수정
	 * 
	 * @param model 모델
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public int update(T model) throws Exception;

	/**
	 * 공통 수정
	 * 
	 * @param model 모델
	 * @param files 파일 리스트
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public int update(T model, List<Files> files) throws Exception;

	/**
	 * 공통 리스트 수정
	 * 
	 * @param models 모델 리스트
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public int update(List<T> models) throws Exception;

	/**
	 * 공통 삭제
	 * 
	 * @param model 모델
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public int delete(T model) throws Exception;

	/**
	 * 공통 리스트 삭제
	 * 
	 * @param models 모델 리스트
	 * 
	 * @return 결과
	 * 
	 * @throws Exception
	 */
	public int delete(List<T> models) throws Exception;
}