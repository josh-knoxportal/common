package org.oh.common.db.dao;

import java.sql.SQLException;

import org.oh.common.db.type.DBSqlParameter;
import org.oh.common.db.type.DBSqlResult;

/**
 * Database에 접근할 수 있는 객체의 interface
 * 해당 interface를 구현할 때는 {@link org.oh.common.db.type.DBSqlResult}의 내용을 참고한다.
 * 
 * 
 * @version 1.0.0
 * @see org.oh.common.db.type.DBSqlResult
 *
 */
public interface DBDao {
	/**
	 * SQL을 수행한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	abstract public DBSqlResult execute(DBSqlParameter sqlParam) throws SQLException;

	/**
	 * select SQL을 수행한다.
	 * 수행 결과 하나의 ResultSet은 {@link org.oh.common.db.type.DBMapList} 또는 {@link org.oh.common.db.type.DBMap} 형태로 저장하기를 권장한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	public DBSqlResult select(DBSqlParameter sqlParam) throws SQLException;

	/**
	 * insert SQL을 수행한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	public DBSqlResult insert(DBSqlParameter sqlParam) throws SQLException;

	/**
	 * 배치 형태로 insert SQL을 수행한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @param commitSize 한번에 commit할 SQL의 개수
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	public DBSqlResult insertBatch(DBSqlParameter sqlParam, int commitSize) throws SQLException;

	/**
	 * delete SQL을 수행한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	public DBSqlResult delete(DBSqlParameter sqlParam) throws SQLException;

	/**
	 * update SQL을 수행한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	public DBSqlResult update(DBSqlParameter sqlParam) throws SQLException;

	/**
	 * 여러 SQL을 하나의 transaction으로 처리한다.
	 * 
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @param dsName <code>DataSource</code> 이름
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	public DBSqlResult executeTransaction(DBSqlParameter[] sqlParam, String dsName) throws SQLException;
}
