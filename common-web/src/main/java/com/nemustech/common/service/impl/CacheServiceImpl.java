package com.nemustech.common.service.impl;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

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
		cache.evict("regex:" + getCacheName() + '_' + getClass() + ".*");
	}

	/**
	 * 캐시 초기화
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initCache_() throws Exception {
		String cacheName = getCacheName();
		cache = cacheManager.getCache(cacheName);
	}
}