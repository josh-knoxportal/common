package org.oh.web.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.EntityManager;
import org.mybatisorm.Page;
import org.oh.common.cache.CacheEvictCommon;
import org.oh.common.cache.CacheableCommon;
import org.oh.web.model.Default;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service("commonService")
public class CommonServiceImpl<T extends Default> implements CommonService<T> {
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected CacheManager cacheManager;
	protected Cache cache = null;

	@Autowired
	protected EntityManager entityManager;

	@Override
	public String getCacheName() {
		return "common";
	}

	@Override
	public T get(T model) throws Exception {
		return entityManager.get(model);
	}

	@Override
	@CacheableCommon
	public List<T> list(T model) throws Exception {
		List<T> list;

//		list = getCache().get(ReflectionUtil.toString(model), List.class);
//		if (list != null)
//			return list;

		list = entityManager.list(model, model.getCondition(), model.getOrder_by());

//		cache.put(model.toString(), list);

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
	@CacheEvictCommon
	public int insert(T model) throws Exception {
		return entityManager.insert(model);

//		getCache().clear();
	}

	@Override
	@CacheEvictCommon
	public int update(T model) throws Exception {
		return entityManager.update(model, model.getCondition());
	}

	@Override
	@CacheEvictCommon
	public int delete(T model) throws Exception {
		return entityManager.delete(model, model.getCondition());
	}

	/**
	 * 캐쉬 조회
	 * 
	 * @param Cache
	 * 
	 * @return
	 */
	protected Cache getCache() {
		if (cache != null)
			return cache;

		cache = cacheManager.getCache(getCacheName());

		return cache;
	}
}