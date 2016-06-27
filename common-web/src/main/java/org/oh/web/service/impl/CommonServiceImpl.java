package org.oh.web.service.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.EntityManager;
import org.mybatisorm.Page;
import org.oh.common.util.ReflectionUtil;
import org.oh.web.model.Default;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author skoh
 */
//@Service("commonService")
public abstract class CommonServiceImpl<T extends Default> implements InitializingBean, CommonService<T> {
	protected Log log = LogFactory.getLog(getClass());

	protected MessageFormat cacheKeyFormat = new MessageFormat(getCacheName() + "_" + getClass().getName() + "{0}_{1}");

	/**
	 * 캐쉬명
	 */
	protected String cacheName;

	/**
	 * 캐쉬
	 */
	protected Cache cache;

	@Autowired
	protected CacheManager cacheManager;

	@Autowired
	protected EntityManager entityManager;

	@Override
	public String getCacheName() {
		return null;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		cacheName = getCacheName();
		if (cacheName != null) {
			cache = cacheManager.getCache(cacheName);
		}
	}

	@Override
	public T get(T model) throws Exception {
		return entityManager.get(model);
	}

	@Override
//	@CacheableCommon
	public List<T> list(T model) throws Exception {
		List<T> list;

		String cacheKey = null;
		if (cache != null) {
			cacheKey = cacheKeyFormat
					.format(new Object[] { "list", ReflectionUtil.toString(new Object[] { model }, "condition2") });
			log.debug("cacheKey: " + cacheKey);

			list = cache.get(cacheKey, List.class);
			if (list != null) {
				return list;
			}
		}

		list = entityManager.list(model, model.getCondition2(), model.getOrder_by(), model.getHint(), model.getFields(),
				model.getSql_seq());

		if (cache != null) {
			cache.put(cacheKey, list);
		}

		return list;
	}

	@Override
	public int count(T model) throws Exception {
		return entityManager.count(model, model.getCondition());
	}

	@Override
	public Page<T> page(T model, Page<T> page) throws Exception {
		return entityManager.page(model, model.getCondition(), model.getOrder_by(), page.getPageNumber(),
				page.getRows());
	}

	@Override
//	@CacheEvictCommon
	public int insert(T model) throws Exception {
		int result = entityManager.insert(model);

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
//	@CacheEvictCommon
	public int update(T model) throws Exception {
		int result = entityManager.update(model, model.getCondition());

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
//	@CacheEvictCommon
	public int delete(T model) throws Exception {
		int result = entityManager.delete(model, model.getCondition());

		if (cache != null) {
			cache.clear();
		}

		return result;
	}
}