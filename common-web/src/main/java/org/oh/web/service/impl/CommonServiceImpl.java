package org.oh.web.service.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.EntityManager;
import org.mybatisorm.Page;
import org.mybatisorm.Query;
import org.oh.common.annotation.TransactionalException;
import org.oh.common.util.ReflectionUtil;
import org.oh.web.model.Common;
import org.oh.web.model.Default;
import org.oh.web.service.CommonService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author skoh
 */
@Service("commonService")
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
		model = setModel(model);

		return entityManager.get(model);
	}

	@Override
//	@CacheableCommon
	public List<T> list(T model) throws Exception {
		model = setModel(model);

		List<T> list = null;

		String cacheKey = null;
		if (cache != null) {
			cacheKey = cacheKeyFormat.format(new Object[] { "list", ReflectionUtil.toString(model, "condition2") });
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
		model = setModel(model);

		return entityManager.count(model, model.getCondition2());
	}

	@Override
	public Page<T> page(T model, Page<T> page) throws Exception {
		model = setModel(model);

		return entityManager.page(model, model.getCondition2(), model.getOrder_by(), page.getPageNumber(),
				page.getRows());
	}

	@Override
	@TransactionalException
//	@CacheEvictCommon
	public int insert(T model) throws Exception {
		model = setDefaultModifyDate(setDefaultRegisterDate(model));

		int result = entityManager.insert(model);

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
//	@CacheEvictCommon
	public int insert(List<T> models) throws Exception {
		int result = 0;
		for (T model : models) {
			model = setDefaultModifyDate(setDefaultRegisterDate(model));

			result += entityManager.insert(model);
		}

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
//	@CacheEvictCommon
	public int update(T model) throws Exception {
		model = setDefaultModifyDate(model);

		int result = entityManager.update(model, model.getCondition2());

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
//	@CacheEvictCommon
	public int delete(T model) throws Exception {
		int result = entityManager.delete(model, model.getCondition2());

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	/**
	 * Model에 대한 쿼리를 가공할 경우에 사용
	 * 
	 * @param model
	 * 
	 * @return model
	 */
	protected T setModel(T model) throws Exception {
		return model;
	}

	/**
	 * 등록일자의 DB벤더별 기본값을 설정
	 * 
	 * @param model
	 * 
	 * @return model
	 * 
	 * @throws Exception
	 */
	protected T setDefaultRegisterDate(T model) throws Exception {
		if (model instanceof Common) {
			Common common = (Common) model;
			if (common.getReg_dt() == null) {
				if ("mysql".equals(entityManager.getSourceType())) {
					common.setReg_dt(Query.makeVariable(Common.DEFAULT_DATE_MYSQL));
				} else if ("oracle".equals(entityManager.getSourceType())) {
					common.setReg_dt(Query.makeVariable(Common.DEFAULT_DATE_ORACLE));
//					common.setReg_dt(new Date()); // to_timestamp('07/10/2016 21:21:31.915', 'mm/dd/yyyy hh24:mi:ss.ff3')
				} else if ("sqlserver".equals(entityManager.getSourceType())) {
				}
			}
		}

		return model;
	}

	/**
	 * 수정일자의 DB벤더별 기본값을 설정
	 * 
	 * @param model
	 * 
	 * @return model
	 * 
	 * @throws Exception
	 */
	protected T setDefaultModifyDate(T model) throws Exception {
		if (model instanceof Common) {
			Common common = (Common) model;
			if (common.getMod_dt() == null) {
				if ("mysql".equals(entityManager.getSourceType())) {
					common.setMod_dt(Query.makeVariable(Common.DEFAULT_DATE_MYSQL));
				} else if ("oracle".equals(entityManager.getSourceType())) {
					common.setMod_dt(Query.makeVariable(Common.DEFAULT_DATE_ORACLE));
				} else if ("sqlserver".equals(entityManager.getSourceType())) {
				}
			}
		}

		return model;
	}
}