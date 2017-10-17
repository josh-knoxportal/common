package com.nemustech.common.service.impl;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.nemustech.common.service.CacheService;

public abstract class CacheServiceImpl implements CacheService {
	protected Log log = LogFactory.getLog(getClass());
	protected MessageFormat cacheKeyFormat = new MessageFormat(getCacheName() + "_" + getClass().getName() + "_{0}");

	/**
	 * 캐쉬명
	 */
	protected String cacheName;

	/**
	 * 캐쉬
	 */
	protected Cache cache;

	/**
	 * 캐쉬 관리자
	 */
	@Autowired
	protected CacheManager cacheManager;

	@Override
	public String getCacheName() {
		return null;
	}

	/**
	 * 캐시 초기화
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initCache_() throws Exception {
		cacheName = getCacheName();
		if (cacheName == null)
			return;

		cache = cacheManager.getCache(cacheName);
	}
}