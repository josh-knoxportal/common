package com.nemustech.sample.service;

import java.util.List;

import com.nemustech.common.service.CommonService;
import com.nemustech.sample.model.Sample;

/**
 * 샘플 서비스
 * 
 * @author skoh
 */
public interface SampleService extends CommonService<Sample> {
	/**
	 * 샘플 조회
	 * 
	 * @param sample 샘플
	 * @return Sample
	 */
	public Sample get2(Sample sample) throws Exception;

	/**
	 * 샘플 목록 조회
	 * 
	 * @param sample 샘플
	 * @return List<Sample>
	 */
	public List<Sample> list2(Sample sample) throws Exception;

	/**
	 * 샘플 건수 조회
	 * 
	 * @param sample 샘플
	 * @return 건수
	 * @throws Exception
	 */
	public int count2(Sample sample) throws Exception;

	/**
	 * 샘플 페이지 조회
	 * 
	 * @param sample 샘플
	 * @param paging 페이지
	 * @return List<Sample>
	 */
	public List<Sample> page(Sample sample) throws Exception;

	/**
	 * 샘플 등록
	 * 
	 * @param sample 샘플
	 * @return int
	 */
	public int insert2(Sample sample) throws Exception;

	/**
	 * 샘플 수정
	 * 
	 * @param sample 샘플
	 * @return int
	 */
	public int update2(Sample sample) throws Exception;

	/**
	 * 샘플 삭제
	 * 
	 * @param sample 샘플
	 * @return int
	 */
	public int delete2(Sample sample) throws Exception;

	/**
	 * 샘플 병합
	 * 
	 * @param sample 샘플
	 * @return int
	 */
	public int merge(Sample sample) throws Exception;
}