package com.nemustech.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.mybatisorm.Condition;
import org.mybatisorm.EntityManager;
import org.mybatisorm.Page;
import org.mybatisorm.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.nemustech.common.annotation.TransactionalException;
import com.nemustech.common.file.Files;
import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.model.Common;
import com.nemustech.common.model.Default;
import com.nemustech.common.page.Paging;
import com.nemustech.common.service.CommonService;
import com.nemustech.common.service.FilesService;
import com.nemustech.common.storage.FileStorage;
import com.nemustech.common.util.ReflectionUtil;
import com.nemustech.common.util.StringUtil;
import com.nemustech.common.util.Utils;

/**
 * https://github.com/skoh/common.git
 * 
 * @author skoh
 * @see <a href="https://github.com/wolfkang/mybatis-orm">https://github.com/wolfkang/mybatis-orm</a>
 */
public abstract class CommonServiceImpl<T extends Default> extends CacheServiceImpl implements CommonService<T> {
	@Value("${spring.profiles.active:default}")
	protected String activeProfile;

	/**
	 * 매퍼
	 */
	protected CommonMapper<T> mapper;

	/**
	 * ORM 관리자
	 */
	@Autowired
	protected EntityManager entityManager;

	/**
	 * 파일 저장소
	 */
	@Autowired
	protected FileStorage fileStorage;

	/**
	 * 파일 서비스
	 */
	protected FilesService fileService;

	public static String getDefaultDate(String sourceType) {
		if (SOURCE_TYPE_MYSQL.equals(sourceType)) {
			return DEFAULT_DATE_MYSQL;
		} else if (SOURCE_TYPE_ORACLE.equals(sourceType)) {
			return DEFAULT_DATE_ORACLE;
		} else if (SOURCE_TYPE_SQLSERVER.equals(sourceType)) {
			return DEFAULT_DATE_SQLSERVER;
		}

		return null;
	}

	public static String getDefaultDateValue(String sourceType) {
		if (SOURCE_TYPE_MYSQL.equals(sourceType)) {
			return DEFAULT_DATE_CHAR_MYSQL;
		} else if (SOURCE_TYPE_ORACLE.equals(sourceType)) {
			return DEFAULT_DATE_CHAR_ORACLE;
		} else if (SOURCE_TYPE_SQLSERVER.equals(sourceType)) {
			return DEFAULT_DATE_CHAR_SQLSERVER;
		}

		return null;
	}

	/**
	 * 변수 바인딩
	 * 
	 * @param variable
	 * @return
	 */
	public static String makeVariable(String variable) {
		return Query.makeVariable(variable);
	}

	@PostConstruct
	public void initService_() throws Exception {
		mapper = getMapper();

		fileService = getFileService();
	}

	@Override
	public String getDefaultDate() {
		return getDefaultDate(getSourceType());
	}

	@Override
	public String getDefaultDateValue() {
		return getDefaultDateValue(getSourceType());
	}

	@Override
	public CommonMapper<T> getMapper() {
		return null;
	}

	@Override
	public FilesService getFileService() {
		return null;
	}

	@Override
	public String getActiveProfile() {
		return activeProfile;
	}

	@Override
	public String getSourceType() {
		return entityManager.getSourceType();
	}

	@Override
	public T get(T model) throws Exception {
		model = setModel(model);

		if (mapper == null) {
			return entityManager.get(model);
		} else {
			setCondition(model);
			List<T> list = mapper.list(model);
			return (list.size() > 0) ? list.get(0) : null;
		}
	}

	@Override
//	@CacheableCommon
	public List<T> list(T model) throws Exception {
		model = setModel(model);

		List<T> list = null;

		String cacheKey = null;
		if (cache != null) {
			if (mapper != null) {
				setCondition(model);
			}
			cacheKey = cacheKeyFormat.format(new Object[] { StringUtil.toString(model, "conditionObj") });
			log.debug("cacheKey: " + cacheKey);

			list = cache.get(cacheKey, List.class);
			if (list != null) {
				return list;
			}
		}

		if (mapper == null) {
			list = entityManager.list(model, model.getConditionObj(), model.getOrder_by(), model.getHint(),
					model.getFields(), model.getTable(), model.getGroup_by(), model.getHaving(), model.getSql_name());
		} else {
			list = mapper.list(model);
		}

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

		if (mapper == null) {
			return entityManager.count(model, model.getConditionObj());
		} else {
			setCondition(model);
			return mapper.count(model);
		}
	}

	@Override
	public List<T> page(Paging model) throws Exception {
		T model2 = (T) model;

		List<T> list = null;
		if (mapper == null) {
			page(model2, new Page<T>(model.getPage_number(), model.getRows_per_page())).getList();
		} else {
			if (model2.getOrder_by() == null)
				model2.setOrder_by(Paging.DEFAULT_ORDER_BY);

			setCondition(model2);
			list = mapper.list(model2);
		}

		return list;
	}

	@Deprecated
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

		Object result = null;
		if (mapper == null) {
			result = entityManager.insert(model, model.getTable(), model.getSql_name());
		} else {
			setCondition(model);
			result = mapper.insert(model);
		}

		result = String.valueOf(result);
		Object id = getId(model);
		if (Utils.isValidate(id))
			result = id;

		// 파일 생성
//		if (!(model instanceof Files))
//			insertFile(model, files);

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

			Object result_ = null;
			if (mapper == null) {
				result_ = entityManager.insert(model, model.getTable(), model.getSql_name());
			} else {
				setCondition(model);
				result_ = mapper.insert(model);
			}

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
		int result = 0;

		// 파일 수정
//		if (!(model instanceof Files)) {
//			String condition = model.getCondition();
//			model = (T) model.getClass().newInstance();
//			model.addCondition(condition);
//			updateFile(model, files);
//		}

		model = setDefaultModifyDate(model);

		if (mapper == null) {
			result = entityManager.update(model, model.getConditionObj(), model.getTable(), model.getSql_name());
		} else {
			setCondition(model);
			result = mapper.update(model);
		}

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

			if (mapper == null) {
				result += entityManager.update(model, model.getConditionObj(), model.getTable(), model.getSql_name());
			} else {
				setCondition(model);
				result += mapper.update(model);
			}
		}

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Override
	public int delete(T model) throws Exception {
		return delete(model, new ArrayList<Files>());
	}

	@TransactionalException
	public int delete(T model, List<Files> files) throws Exception {
		int result = 0;

		// 파일 삭제
//		if (!(model instanceof Files))
//			deleteFile(model, files);

		if (mapper == null) {
			result += entityManager.delete(model, model.getConditionObj(), model.getTable(), model.getSql_name());
		} else {
			setCondition(model);
			result += mapper.delete(model);
		}

		if (cache != null) {
			cache.clear();
		}

		return result;
	}

	@Deprecated
	@Override
	@TransactionalException
	public int delete(List<T> models) throws Exception {
		int result = 0;

		for (T model : models) {
			// 파일 삭제
//			if (!(model instanceof Files))
//				deleteFile(model);

			if (mapper == null) {
				result += entityManager.delete(model, model.getConditionObj(), model.getTable(), model.getSql_name());
			} else {
				setCondition(model);
				result += mapper.delete(model);
			}
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
	 * @return model
	 */
	protected T setModel(T model) throws Exception {
		return model;
	}

	/**
	 * 등록일자의 DB벤더별 기본값을 설정
	 * 
	 * @param model
	 * @return model
	 * @throws Exception
	 */
	protected T setDefaultRegisterDate(T model) throws Exception {
		if (model instanceof Common) {
			Common common = (Common) model;
			if (common.getReg_dt() == null) {
				if (SOURCE_TYPE_MYSQL.equals(getSourceType())) {
					common.setReg_dt(makeVariable(DEFAULT_DATE_CHAR_MYSQL));
				} else if (SOURCE_TYPE_ORACLE.equals(getSourceType())) {
					common.setReg_dt(makeVariable(DEFAULT_DATE_CHAR_ORACLE));
//					common.setReg_dt(new Date()); // to_timestamp('07/10/2016 21:21:31.915', 'mm/dd/yyyy hh24:mi:ss.ff3')
				} else if (SOURCE_TYPE_SQLSERVER.equals(getSourceType())) {
					common.setReg_dt(makeVariable(DEFAULT_DATE_CHAR_SQLSERVER));
				}
			}
		}

		return model;
	}

	/**
	 * 수정일자의 DB벤더별 기본값을 설정
	 * 
	 * @param model
	 * @return model
	 * @throws Exception
	 */
	protected T setDefaultModifyDate(T model) throws Exception {
		if (model instanceof Common) {
			Common common = (Common) model;
			if (common.getMod_dt() == null) {
				if (SOURCE_TYPE_MYSQL.equals(getSourceType())) {
					common.setMod_dt(makeVariable(DEFAULT_DATE_CHAR_MYSQL));
				} else if (SOURCE_TYPE_ORACLE.equals(getSourceType())) {
					common.setMod_dt(makeVariable(DEFAULT_DATE_CHAR_ORACLE));
				} else if (SOURCE_TYPE_SQLSERVER.equals(getSourceType())) {
					common.setMod_dt(makeVariable(DEFAULT_DATE_CHAR_SQLSERVER));
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
	 * @throws Exception
	 */
	protected List<Object> insertFile(T model, List<Files> files) throws Exception {
		for (Files file : files) {
			file.setPath(fileStorage.save(file));
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
	 * @throws Exception
	 */
	protected List<Object> updateFile(T model, List<Files> files) throws Exception {
		deleteFile(model, files);

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
	 * @param files
	 * @throws Exception
	 */
	protected int deleteFile(T model, List<Files> files) throws Exception {
		return 0;
	}

	/**
	 * 조건 설정
	 * 
	 * @param model
	 * @throws Exception
	 */
	protected void setCondition(T model) throws Exception {
		Query query = new Query(model);

		String condition = model.getConditionObj().build(query);
		if (Utils.isValidate(condition))
			model.setCondition2(condition);

		model.setProperties(query.getProperties());
	}
}