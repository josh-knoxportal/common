package com.nemustech.common.service.impl;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.nemustech.common.annotation.CacheEvictCommon;
import com.nemustech.common.annotation.CacheEvictCommonClass;
import com.nemustech.common.annotation.CacheEvictCommonKey;
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
		return CACHE_NAME;
	}

	@Override
	@CacheEvictCommon
	public void clearCache() {
	}

	@Override
	@CacheEvictCommonKey
	public void clearCache(String key) {
	}

	@Override
	@CacheEvictCommonClass
	public void clearCacheClass() {
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