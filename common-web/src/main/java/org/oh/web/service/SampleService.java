package org.oh.web.service;

import java.util.List;

import org.oh.web.model.Sample;
import org.oh.web.page.Paging;

/**
 * 샘플 서비스
 * 
 * @author skoh
 */
public interface SampleService {
	/**
	 * 샘플 조회
	 * 
	 * @param sample 샘플
	 * 
	 * @return Sample
	 */
	public Sample get(Sample sample) throws Exception;

	/**
	 * 샘플 목록 조회
	 * 
	 * @param sample 샘플
	 * 
	 * @return List<Sample>
	 */
	public List<Sample> list(Sample sample) throws Exception;

	/**
	 * 샘플 페이지 조회
	 * 
	 * @param sample 샘플
	 * @param paging 페이지
	 * 
	 * @return List<Sample>
	 */
	public List<Sample> page(Sample sample, Paging paging) throws Exception;

	/**
	 * 샘플 등록
	 * 
	 * @param sample 샘플
	 * 
	 * @return int
	 */
	public int insert(Sample sample) throws Exception;

	/**
	 * 샘플 수정
	 * 
	 * @param sample 샘플
	 * 
	 * @return int
	 */
	public int update(Sample sample) throws Exception;

	/**
	 * 샘플 삭제
	 * 
	 * @param sample 샘플
	 * 
	 * @return int
	 */
	public int delete(Sample sample) throws Exception;

	/**
	 * 샘플 병합
	 * 
	 * @param sample 샘플
	 * 
	 * @return int
	 */
	public int merge(Sample sample) throws Exception;
}