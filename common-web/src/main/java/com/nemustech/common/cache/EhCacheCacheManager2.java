package com.nemustech.common.cache;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.util.Assert;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;

/**
 * EhCacheCache2 사용
 * 
 * @author skoh
 */
public class EhCacheCacheManager2 extends EhCacheCacheManager {
	@Override
	protected Collection<Cache> loadCaches() {
		net.sf.ehcache.CacheManager cacheManager = getCacheManager();
		Assert.state(cacheManager != null, "No CacheManager set");

		Status status = cacheManager.getStatus();
		if (!Status.STATUS_ALIVE.equals(status)) {
			throw new IllegalStateException(
					"An 'alive' EhCache CacheManager is required - current cache is " + status.toString());
		}

		String[] names = getCacheManager().getCacheNames();
		Collection<Cache> caches = new LinkedHashSet<>(names.length);
		for (String name : names) {
			caches.add(new EhCacheCache2(getCacheManager().getEhcache(name)));
		}
		return caches;
	}

	@Override
	protected Cache getMissingCache(String name) {
		net.sf.ehcache.CacheManager cacheManager = getCacheManager();
		Assert.state(cacheManager != null, "No CacheManager set");

		// Check the EhCache cache again (in case the cache was added at runtime)
		Ehcache ehcache = cacheManager.getEhcache(name);
		if (ehcache != null) {
			return new EhCacheCache2(ehcache);
		}
		return null;
	}
}