package com.nemustech.common.service;

/**
 * 캐시 서비스
 * 
 * @author skoh
 */
public interface CacheService {
	/**
	 * 공통 캐시명
	 */
	public static final String CACHE_NAME = "common";

	/**
	 * 캐시명 정의
	 * 
	 * @return 캐시명
	 */
	public String getCacheName();

	/**
	 * 캐시를 모두 지운다.
	 */
	public void clearCache();

	/**
	 * 키에 해당하는 캐시를 지운다.
	 * 
	 * @param key
	 */
	public void clearCache(String key);

	/**
	 * 클래스에서 사용한 캐시를 모두 지운다.
	 */
	public void clearCacheClass();
}