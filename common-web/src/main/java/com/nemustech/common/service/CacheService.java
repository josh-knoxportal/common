package com.nemustech.common.service;

/**
 * 캐시 서비스
 * 
 * @author skoh
 */
public interface CacheService {
	/**
	 * 캐쉬명 정의
	 * 
	 * @return 캐쉬명(null 은 캐쉬 사용 안함)
	 */
	public String getCacheName();
}