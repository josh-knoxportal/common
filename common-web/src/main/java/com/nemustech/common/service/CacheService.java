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
	public static final String CACHE_NAME_COMMON = "common";

	/**
	 * 동기화 캐시명
	 */
	public static final String CACHE_NAME_SYNC = "sync";

	/**
	 * 캐시 등록 키
	 */
	public static final String CACHEABLE_KEY = "#root.caches[0].name + '_' + #root.targetClass + '_' + #root.methodName + '_' + T(com.nemustech.common.util.StringUtil).toString(#root.args, 'conditionObj')";

	/**
	 * 캐시 삭제 키
	 */
	public static final String CACHE_EVICT_KEY = "'regex:' + #root.caches[0].name + '_' + #root.targetClass + '.*'";

	/**
	 * 캐시명 정의
	 * 
	 * <pre>
	 * - 기본값은 공통 캐시명(common)
	 * </pre>
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