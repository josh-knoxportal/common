package com.nemustech.platform.lbs.common.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : CommonDao
 * 설   명 : DAO 공통 인터페이스 모음
 * 작 성 자 : 박진언(wwweojin@gmail.com)
 * 작성일자 : 2015-02-05
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2015-02-05             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */

public class CommonDao extends SqlSessionDaoSupport {
	/**
	 * insert
	 * @param statement
	 * @param paramMap
	 * SqlSessionDaoSupport
	 */
	public Object insert(String statement, Map<String, Object> paramMap) {
		return this.getSqlSession().insert(statement, paramMap);
	}
	/**
	 * update
	 * @param statement
	 * @param paramMap
	 */
	public int update(String statement, Map<String, Object> paramMap) {
		return this.getSqlSession().update(statement, paramMap);
	}
	/**
	 * delete
	 * @param statement
	 * @param paramMap
	 */
	public int delete(String statement, Map<String, Object> paramMap) {
		return this.getSqlSession().delete(statement, paramMap);
	}
	/**
	 * select to listMap
	 * @param statement
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectToListMap(String statement, Map<String, Object> paramMap) {
		return this.getSqlSession().selectList(statement, paramMap);
	}
	/**
	 * select to map
	 * @param statement
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectToMap(String statement, Map<String, Object> paramMap) {
		return this.getSqlSession().selectOne(statement, paramMap);
	}
	
	/**
	 * 
	 * @param string
	 * @param param
	 * @return
	 */
	public Object selectToObj(String statement, Map<String, Object> param) {
		return this.getSqlSession().selectOne(statement, param);
	}
	
	
	/**
	 * 
	 * @param string
	 * @param param
	 * @return
	 */
	public int selectToInt(String statement, Map<String, Object> param) {
		return this.getSqlSession().selectOne(statement, param);
	}
	
	/**
	 * select to obj list
	 * @param statement
	 * @param param
	 * @return
	 */
	public List<? extends Object> selectToListObj(String statement, Map<String, Object> param) {
		return this.getSqlSession().selectList(statement, param);
	}
	
	public static void main(String[] args) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tset", "test");
		SqlParameterSource params = new BeanPropertySqlParameterSource(paramMap);
		System.out.println(params);
	}
}