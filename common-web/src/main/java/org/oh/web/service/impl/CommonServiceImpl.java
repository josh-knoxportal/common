package org.oh.web.service.impl;

import java.util.List;

import org.mybatisorm.EntityManager;
import org.mybatisorm.Page;
import org.oh.web.cache.CacheEvictSample;
import org.oh.web.model.Default;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service("commonService")
public class CommonServiceImpl<T extends Default> implements CommonService<T> {
	/**
	 * 공통 매퍼
	 */
	@Autowired
	protected EntityManager entityManager;

	@Override
	public T get(T t) throws Exception {
		return entityManager.get(t);
	}

	@Override
	@CacheEvictSample
	public List<T> list(T t) throws Exception {
		return entityManager.list(t, t.getCondition(), t.getOrder_by());
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
	@CacheEvictSample
	public void insert(T t) throws Exception {
		entityManager.insert(t);
	}

	@Override
	@CacheEvictSample
	public void update(T t) throws Exception {
		entityManager.update(t);
	}

	@Override
	@CacheEvictSample
	public void delete(T t) throws Exception {
		entityManager.delete(t, t.getCondition());
	}
}