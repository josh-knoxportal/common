package org.oh.adapter;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

/**
 * 데이터베이스 연동 Adapter
 * (MyBatis)
 */
public interface DBAdapter {

	/**
	 * 데이터베이스 Connection을 얻는다.
	 * 
	 * @param envName SqpMalConfig.xml의 environment 이름
	 * @return DataBase Connection Instance
	 */
	public Connection getConnection(String envName);

	/**
	 * 데이터베이스 연결 Session을 연다 (자동커밋 실행됨)
	 * 
	 * @param envName SqpMalConfig.xml의 environment 이름
	 * @return DataBase Sql Session
	 */
	public SqlSession openSession(String envName);

	/**
	 * 데이터베이스 연결 Session을 연다 (자동커밋 설정 필요)
	 * 
	 * @param envName SqpMalConfig.xml의 environment 이름
	 * @param autoCommit Sql transaction auto commit 설정
	 * @return DataBase Sql Session
	 */
	public SqlSession openSession(String envName, boolean autoCommit);

	/**
	 * SQL 쿼리를 실행하여 1개의 데이터 조회
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter map
	 * @return 조회결과 result type object
	 */
	public Object selectOne(String envName, String statement, Map<String, Object> parameters);

	/**
	 * SQL 쿼리를 실행하여 1개의 데이터 조회
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter object
	 * @return 조회결과 result type object
	 */
	public Object selectOne(String envName, String statement, Object parameters);

	/**
	 * SELECT 쿼리를 실행하여 1개의 데이터 조회
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter map
	 * @param resultType result class type
	 * @return 조회결과 result type object
	 */
	public <T> T selectOne(String envName, String statement, Object parameters, Class<T> resultType);

	/**
	 * SELECT 쿼리를 실행하여 1개의 데이터 조회
	 * 
	 * @param session Sql Session
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter map
	 * @param resultType result class type
	 * @return 조회결과 result type object
	 * 
	 * @since 3.0.0
	 */
	public <T> T selectOne(SqlSession session, String statement, Object parameters, Class<T> resultType);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter map
	 * @param resultListType result class type
	 * @return 조회결과 List object
	 */
	public <T> List<T> selectList(String envName, String statement, Map<String, Object> parameters,
			Class<T> resultListType);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter object
	 * @return 조회결과 List Object
	 */
	public List<?> selectList(String envName, String statement, Object parameters);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 목록 중 일부를 조회한다.
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter map
	 * @param start 목록의 조회 위치
	 * @param size 조회할 항목의 개수
	 * @return 조회결과 List Object
	 */
	public List<?> selectList(String envName, String statement, Map<String, Object> parameters, int start, int size);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 목록 중 일부를 조회한다.
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter object
	 * @param start 목록의 조회 위치
	 * @param size 조회할 항목의 개수
	 * @return 조회결과 List Object
	 */
	public List<?> selectList(String envName, String statement, Object parameters, int start, int size);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter object
	 * @param resultType result class type
	 * @return 조회결과 List Object
	 */
	public <T> List<T> selectList(String envName, String statement, Object parameters, Class<T> resultType);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 
	 * @param envName Sql Session
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter object
	 * @param resultType result class type
	 * @return 조회결과 List Object
	 */
	public <T> List<T> selectList(SqlSession session, String statement, Object parameters, Class<T> resultType);

	/**
	 * SELECT 쿼리를 실행하여 여러 개의 데이터를 조회
	 * 목록 중 일부를 조회한다.
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameters query parameter object
	 * @param start 목록의 조회 위치
	 * @param size 조회할 항목의 개수
	 * @param resultType result class type
	 * @return 조회결과 List Object
	 */
	public <T> List<T> selectList(String envName, String statement, Object parameters, int start, int size,
			Class<T> resultType);

	/**
	 * INSERT 쿼리를 실행하여 데이터를 삽입
	 * (1개의 쿼리를 실행할 때 사용, 새로운 세션을 사용)
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameter query parameter object
	 * @return 데이터를 삽입한 데이터 수
	 */
	public int insert(String envName, String statement, Object parameter);

	/**
	 * INSERT 쿼리를 실행하여 데이터를 삽입
	 * (주어진 세션의 트랜잭션을 사용)
	 * 
	 * @param session
	 * @param statement
	 * @param parameter
	 * @return
	 */
	public int insert(SqlSession session, String statement, Object parameter);

	/**
	 * DELETE 쿼리를 실행하여 데이터를 삭제
	 * (1개의 쿼리를 실행할 때 사용, 새로운 세션을 사용)
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameter query parameter object
	 * @return 삭제된 row 수
	 */
	public int delete(String envName, String statement, Object parameter);

	/**
	 * DELETE 쿼리를 실행하여 데이터를 삭제
	 * (주어진 세션의 트랜잭션을 사용)
	 * 
	 * @param session Database Sql Session
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameter query parameter object
	 * @return 삭제된 row 수
	 */
	public int delete(SqlSession session, String statement, Object parameter);

	/**
	 * UPDATE 쿼리를 실행하여 데이터를 수정
	 * (1개의 쿼리를 실행할 때 사용, 새로운 세션을 사용)
	 * 
	 * @param envName SqlMapConfig.xml의 environment 이름
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameter query parameter object
	 * @return 수정된 row 수
	 */
	public int update(String envName, String statement, Object parameter);

	/**
	 * UPDATE 쿼리를 실행하여 데이터를 수정
	 * (주어진 세션의 트랜잭션을 사용)
	 * 
	 * @param session Database Sql Session
	 * @param statement sqlmap.xml에 선언한 namespace.queryid 명
	 * @param parameter query parameter object
	 * @return 수정된 row 수
	 */
	public int update(SqlSession session, String statement, Object parameter);

	/**
	 * 세션의 트랜잭션을 커밋하고 세션을 닫는다.
	 * 
	 * @param session Database Sql Session
	 */
	public void commitAndClose(SqlSession session);

	/**
	 * 세션의 트랜잭션을 롤백하고 세션을 닫는다.
	 * 
	 * @param session Database Sql Session
	 */
	public void rollbackAndClose(SqlSession session);
}
