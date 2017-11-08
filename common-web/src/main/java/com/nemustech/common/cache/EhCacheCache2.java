package com.nemustech.common.cache;

import org.springframework.cache.ehcache.EhCacheCache;

import net.sf.ehcache.Ehcache;

public class EhCacheCache2 extends EhCacheCache {
	public static final String REG_EX = "regex:";

	public EhCacheCache2(Ehcache ehcache) {
		super(ehcache);
	}

	/**
	 * 캐시 삭제시 키값에 정규표현식 사용 가능
	 */
	@Override
	public void evict(Object key) {
		Ehcache ehcache = getNativeCache();

		String keyString = key.toString();
		if (keyString.startsWith(REG_EX)) {
			String regex = keyString.substring(REG_EX.length());
			for (Object key_ : ehcache.getKeys()) {
				if (key_.toString().matches(regex)) {
					ehcache.remove(key_);
				}
			}
		} else {
			ehcache.remove(key);
		}
	}
}