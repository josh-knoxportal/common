package org.oh.web.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Condition;
import org.mybatisorm.EntityManager;
import org.mybatisorm.Page;
import org.mybatisorm.Query;
import org.oh.common.annotation.TransactionalException;
import org.oh.common.file.Files;
import org.oh.common.model.Common;
import org.oh.common.model.Default;
import org.oh.common.page.Paging;
import org.oh.common.storage.FileStorage;
import org.oh.common.util.ReflectionUtil;
import org.oh.common.util.StringUtil;
import org.oh.common.util.Utils;
import org.oh.web.service.CommonService;
import org.oh.web.service.FilesService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * https://github.com/skoh/common.git
 * 
 * @author skoh
 */
@Service("commonService")
public abstract class CommonServiceImpl<T extends Default> implements CommonService<T>, InitializingBean {
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

	@Autowired
	protected FileStorage fileStorage;

	@Lazy
	@Autowired
	protected FilesService fileService;

	/**
	 * 캐쉬명을 설정한다.
	 * 
	 * @return null 은 캐쉬 사용 안함
	 */
	@Override
	public String getCacheName() {
		return null;
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
			cacheKey = cacheKeyFormat.format(new Object[] { "list", StringUtil.toString(model, "conditionObj") });
			log.debug("cacheKey: " + cacheKey);

			list = cache.get(cacheKey, List.class);
			if (list != null) {
				return list;
			}
		}

		list = entityManager.list(model, model.getConditionObj(), model.getOrder_by(), model.getHint(),
				model.getFields(), model.getTable(), model.getGroup_by(), model.getHaving(), model.getSql_name());

		if (cache != null) {
			cache.put(cacheKey, list);
		}

		return list;
	}

	@Override
	public List<Map<String, Object>> select(Map<String, Object> model, Condition condition, String orderBy, String hint,
			String fields, String table, String groupBy, String having, String sqlName) throws Exception {
		return entityManager.list(model, condition, orderBy, hint, fields, table, groupBy, having, sqlName);
	}

	@Override
	public int count(T model) throws Exception {
		model = setModel(model);

		return entityManager.count(model, model.getConditionObj());
	}

	@Override
	public Page<T> page(T model, Page<T> page) throws Exception {
		model = setModel(model);

		if (model.getOrder_by() == null)
			model.setOrder_by(Paging.DEFAULT_ORDER_BY);

		return entityManager.page(model, model.getConditionObj(), model.getOrder_by(), page.getPageNumber(),
				page.getRows());
	}

	@Override
	@TransactionalException
//	@CacheEvictCommon
	public Object insert(T model) throws Exception {
		return insert(model, new ArrayList<Files>());
	}

	@Override
	@TransactionalException
	public Object insert(T model, List<Files> files) throws Exception {
		model = setDefaultModifyDate(setDefaultRegisterDate(model));

		Object result = String.valueOf(entityManager.insert(model, model.getTable(), model.getSql_name()));
		Object id = getId(model);
		if (Utils.isValidate(id))
			result = id;

		insertFile(model, files);

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
	public List<Object> insert(List<T> models) throws Exception {
		List<Object> result = new ArrayList<Object>();

		for (T model : models) {
			model = setDefaultModifyDate(setDefaultRegisterDate(model));

			Object result_ = entityManager.insert(model, model.getTable(), model.getSql_name());
			Object id = getId(model);
			if (Utils.isValidate(id))
				result.add(id);
			else
				result.add(result_);
		}

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
	public int update(T model) throws Exception {
		return update(model, new ArrayList<Files>());
	}

	@Override
	@TransactionalException
	public int update(T model, List<Files> files) throws Exception {
		model = setDefaultModifyDate(model);

		int result = entityManager.update(model, model.getConditionObj(), model.getTable(), model.getSql_name());

		updateFile(model, files);

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
	public int update(List<T> models) throws Exception {
		int result = 0;

		for (T model : models) {
			model = setDefaultModifyDate(model);

			result += entityManager.update(model, model.getConditionObj(), model.getTable(), model.getSql_name());
		}

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	@TransactionalException
	public int delete(T model) throws Exception {
		int result = entityManager.delete(model, model.getConditionObj(), model.getTable(), model.getSql_name());

		if (cache != null) {
			cache.clear();
		}

		deleteFile(model);

		return result;
	}

	@Override
	@TransactionalException
	public int delete(List<T> models) throws Exception {
		int result = 0;

		for (T model : models) {
			result += entityManager.delete(model, model.getConditionObj(), model.getTable(), model.getSql_name());
		}

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	/**
	 * id 필드의 값을 구한다.
	 * 
	 * @param model
	 * 
	 * @return id 필드가 없으면 null
	 */
	protected Object getId(T model) {
		Object id = null;
		try {
			id = ReflectionUtil.getValue(model, "id");
		} catch (Exception e) {
		}

		return id;
	}

	/**
	 * Model에 대한 쿼리를 가공할 경우에 Override하여 사용
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

	/**
	 * 파일 등록
	 * 
	 * @param model
	 * @param files
	 * 
	 * @throws Exception
	 */
	protected List<Object> insertFile(T model, List<Files> files) throws Exception {
		for (Files file : files) {
			file.setFile_path(fileStorage.save(file));
		}

		return fileService.insert(files);
	}

	/**
	 * 파일 수정
	 * 
	 * <pre>
	 * - 기존 파일은 삭제하지 않음
	 * - 파일 정보는 삭제하고 다시 생성함
	 * </pre>
	 * 
	 * @param model
	 * @param files
	 * 
	 * @throws Exception
	 */
	protected List<Object> updateFile(T model, List<Files> files) throws Exception {
		return insertFile(model, files);
	}

	/**
	 * 파일 삭제
	 * 
	 * <pre>
	 * - 기존 파일은 삭제하지 않음
	 * - 파일 정보는 삭제함
	 * </pre>
	 * 
	 * @param model
	 * 
	 * @throws Exception
	 */
	protected int deleteFile(T model) throws Exception {
		return 0;
	}
}