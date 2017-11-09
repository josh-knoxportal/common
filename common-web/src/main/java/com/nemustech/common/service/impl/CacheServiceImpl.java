package com.nemustech.common.service.impl;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.nemustech.common.cache.EhCacheCache2;
import com.nemustech.common.service.CacheService;

public abstract class CacheServiceImpl implements CacheService {
	protected Log log = LogFactory.getLog(getClass());
	protected MessageFormat cacheKeyFormat = new MessageFormat(getCacheName() + "_" + getClass().getName() + "_{0}");

	/**
	 * 캐시
	 */
	protected Cache cache;

	/**
	 * 캐시 관리자
	 */
	@Autowired
	protected CacheManager cacheManager;

	/**
	 * 클래스에서 사용한 캐시 키를 생성한다.(삭제용)
	 * 
	 * @param cacheName
	 * @param clz
	 * @return
	 */
	public static String getCacheClassKey(String cacheName, Class<?> clz) {
		return EhCacheCache2.PREFIX_REGEX + cacheName + '_' + clz.toString() + ".*";
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
		cache.evict(getCacheClassKey(getCacheName(), clz));
	}

	/**
	 * 캐시 초기화
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initCache_() throws Exception {
		cache = cacheManager.getCache(getCacheName());
	}
}