package org.oh.web.service.impl;

import java.util.List;

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
	public T get(T t) throws Exception {
		return entityManager.get(t);
	}

	@Override
	@CacheableCommon
	public List<T> list(T t) throws Exception {
		List<T> list;

//		list = getCache().get(ReflectionUtil.toString(t), List.class);
//		if (list != null)
//			return list;

		list = entityManager.list(t, t.getCondition(), t.getOrder_by());

//		cache.put(t.toString(), list);

		return list;
	}

	@Override
	public int count(T t) throws Exception {
		return entityManager.count(t, t.getCondition());
	}

	@Override
	public Page<T> page(T t, Page<T> page) throws Exception {
		return entityManager.page(t, t.getCondition(), t.getOrder_by(), page.getPageNumber(), page.getRows());
	}

	@Override
	@CacheEvictCommon
	public void insert(T t) throws Exception {
		entityManager.insert(t);

//		getCache().clear();
	}

	@Override
	@CacheEvictCommon
	public void update(T t) throws Exception {
		entityManager.update(t);
	}

	@Override
	@CacheEvictCommon
	public void delete(T t) throws Exception {
		entityManager.delete(t, t.getCondition());
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