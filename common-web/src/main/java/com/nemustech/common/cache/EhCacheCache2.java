package com.nemustech.common.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.ehcache.EhCacheCache;

import net.sf.ehcache.Ehcache;

public class EhCacheCache2 extends EhCacheCache {
	public static final String PREFIX_REGEX = "regex:";

	protected Log log = LogFactory.getLog(getClass());

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
		log.debug("cache Key: " + keyString);
		if (keyString.startsWith(PREFIX_REGEX)) {
			String regexKey = keyString.substring(PREFIX_REGEX.length());
			for (Object key_ : ehcache.getKeys()) {
				if (key_.toString().matches(regexKey)) {
					log.debug("remove cache key: " + key_);
					ehcache.remove(key_);
				}
			}
		} else {
			ehcache.remove(key);
		}
	}
}