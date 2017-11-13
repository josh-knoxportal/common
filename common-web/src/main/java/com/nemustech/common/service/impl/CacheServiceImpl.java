package com.nemustech.common.service.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

import com.nemustech.common.cache.EhCacheCache2;
import com.nemustech.common.service.CacheService;

public abstract class CacheServiceImpl implements CacheService {
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected ApplicationContext context;

	/**
	 * 캐시
	 */
	protected Cache cache;

	/**
	 * 캐시 관리자
	 */
	protected CacheManager cacheManager;

	/**
	 * 캐시키를 생성한다.
	 * 
	 * @param cacheName
	 * @param clz
	 * @param methodName
	 * @return
	 */
	public static String makeCacheKey(String cacheName, Class<?> clz, String methodName) {
		return cacheName + "_" + clz.toString() + ((methodName == null) ? "" : "_" + methodName);
	}

	/**
	 * 캐시 초기화
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initCache_() throws Exception {
		try {
			cacheManager = context.getBean(CacheManager.class);
		} catch (Exception e) {
			log.info(getClass().toString() + e.getMessage());
			return;
		}

		cache = cacheManager.getCache(getCacheName());
	}

	/**
	 * 캐시 활성화 여부
	 * 
	 * @return
	 */
	public boolean isActiveCache() {
		return (cache != null);
	}

	/**
	 * @return getCacheName()_getClass()
	 */
	public String makeClassCacheKey() {
		return makeClassCacheKey(getClass());
	}

	/**
	 * @param clz
	 * @return getCacheName()_clz
	 */
	public String makeClassCacheKey(Class<?> clz) {
		return makeMethodCacheKey(clz, null);
	}

	/**
	 * @return getCacheName()_getClass()_getMethodName()
	 */
	public String makeMethodCacheKey() {
		return makeMethodCacheKey(getClass());
	}

	/**
	 * @param clz
	 * @return getCacheName()_clz_getMethodName()
	 */
	public String makeMethodCacheKey(Class<?> clz) {
		return makeMethodCacheKey(clz, Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * @param methodName
	 * @return getCacheName()_getClass()_methodName
	 */
	public String makeMethodCacheKey(String methodName) {
		return makeMethodCacheKey(getClass(), methodName);
	}

	/**
	 * @param clz
	 * @param methodName
	 * @return getCacheName()_clz_methodName
	 */
	public String makeMethodCacheKey(Class<?> clz, String methodName) {
		String cacheKey = makeCacheKey(getCacheName(), clz, methodName);
		log.debug("cache key: " + cacheKey);

		return cacheKey;
	}

	@Override
	public String getCacheName() {
		return CACHE_NAME_COMMON;
	}

	@Override
	public void putCache(String key, Object value) {
		cache.put(key, value);
	}

	@Override
	public Object getCache(String key) {
		return ((ValueWrapper) cache.get(key)).get();
	}

	@Override
	public <T> T getCache(String key, Class<T> clz) {
		return cache.get(key, clz);
	}

	@Override
	public void clearCache() {
		cache.clear();
	}

	@Override
	public void clearCache(String key) {
		cache.evict(key);
	}

	@Override
	public void clearCacheClass() {
		clearCacheClass(getClass());
	}

	@Override
	public void clearCacheClass(Class<?> clz) {
		cache.evict(EhCacheCache2.PREFIX_REGEX + makeClassCacheKey(clz) + ".*");
	}
}