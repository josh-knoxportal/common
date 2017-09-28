package com.nemustech.common.service.impl;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.nemustech.common.model.Default;
import com.nemustech.common.service.CacheService;

@Service("cacheService")
public abstract class CacheServiceImpl<T extends Default> implements CacheService<T> {

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

	@PostConstruct
	public void initCache_() throws Exception {
		cacheName = getCacheName();
		if (cacheName != null) {
			cache = cacheManager.getCache(cacheName);
		}
	}

	@Override
	public String getCacheName() {
		return null;
	}
}