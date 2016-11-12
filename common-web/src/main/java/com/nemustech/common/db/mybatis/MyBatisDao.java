package com.nemustech.common.db.mybatis;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.nemustech.common.db.dao.DBDao;
import com.nemustech.common.db.type.DBMap;
import com.nemustech.common.db.type.DBMapList;
import com.nemustech.common.db.type.DBSqlParameter;
import com.nemustech.common.db.type.DBSqlResult;

/**
 * {@link com.nemustech.common.db.dao.DBDao} 인터페이스를 MyBatis 3.0 기반으로 구현한다. <br />
 * DBSqlParameter를 설정할 때는 dsName을 environment id와 동일하게 설정한다.
 * 
 * 
 * @version 1.0.0
 * @see {@link com.nemustech.common.db.dao.DBDao} <BR />
 *      {@link com.nemustech.common.db.type.DBSqlParameter}
 * 
 */
public class MyBatisDao implements DBDao {
	protected static Log log = LogFactory.getLog(MyBatisDao.class);

	/**
	 * 생성자
	 */
	public MyBatisDao() {
		super();
	}

	public DBSqlResult execute(DBSqlParameter sqlParam) throws SQLException {
		MappedStatement stmt = getSqlFactory(sqlParam).getConfiguration().getMappedStatement(sqlParam.getSqlID());

		log.trace("SQL Command Type: " + stmt.getSqlCommandType().name());

		if (stmt.getSqlCommandType() == SqlCommandType.SELECT) {
			return select(sqlParam);
		}

		else if (stmt.getSqlCommandType() == SqlCommandType.INSERT) {
			return insert(sqlParam);
		}

		else if (stmt.getSqlCommandType() == SqlCommandType.DELETE) {
			return delete(sqlParam);
		}

		else if (stmt.getSqlCommandType() == SqlCommandType.UPDATE) {
			return update(sqlParam);
		}

		else {
			return new DBSqlResult(0);
		}
	}

	/**
	 * {@inheritDoc}
	 * ResultType이 일반 POJO일 경우에는 해당 POJO는 {@link com.nemustech.common.db.type.DBMap}에 POJO 이름으로 저장한다.
	 */
	public DBSqlResult select(DBSqlParameter sqlParam) throws SQLException {
		SqlSessionFactory factory = getSqlFactory(sqlParam);
		MappedStatement stmt = factory.getConfiguration().getMappedStatement(sqlParam.getSqlID());

		log.trace("  === " + sqlParam.getSqlID() + " ======");
		log.trace(stmt.getBoundSql(sqlParam.getParameters()).getSql());

		if (stmt.getStatementType() == StatementType.CALLABLE) {
			return selectFromProcedure(factory, sqlParam);
		}

		else {
			return selectFromSQL(factory, sqlParam);
		}
	}

	/**
	 * SELECT 쿼리를 수행한다.
	 * 
	 * @param factory {@link org.apache.ibatis.session.SqlSessionFactory} 객체
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected DBSqlResult selectFromSQL(SqlSessionFactory factory, DBSqlParameter sqlParam) throws SQLException {
		List list = selectList(factory, sqlParam);
		DBMap map = new DBMap();
		DBMapList mapList = appendRowsToList(list);
		map.put(String.format("%s%d", DBSqlResult._RESULTSET_PREFIX, 1), mapList);

		return new DBSqlResult(map, list.size());
	}

	/**
	 * SELECT 프로시져를 수행한다.
	 * 
	 * @param factory {@link org.apache.ibatis.session.SqlSessionFactory} 객체
	 * @param sqlParam SQL을 수행하기 위한 정보
	 * @return SQL 수행 결과
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected DBSqlResult selectFromProcedure(SqlSessionFactory factory, DBSqlParameter sqlParam) throws SQLException {
		List list = selectList(factory, sqlParam);
		int rowCount = 0;
		DBMap result = new DBMap();
		MappedStatement stmt = factory.getConfiguration().getMappedStatement(sqlParam.getSqlID());

		if (stmt.getResultMaps().size() == 1) {
			DBMapList mapList = appendRowsToList(list);
			result.put(String.format("%s%d", DBSqlResult._RESULTSET_PREFIX, 1), mapList);
			rowCount += mapList.size();
		}

		else {
			int size = list.size();

			for (int i = 0; i < size; ++i) {
				DBMapList mapList = appendRowsToList((List) list.get(i));
				result.put(String.format("%s%d", DBSqlResult._RESULTSET_PREFIX, i + 1), mapList);
				rowCount += mapList.size();
			}
		}

		result.put(String.format("%s%d", DBSqlResult._RESULTSET_PREFIX, 0), sqlParam.getParameters());

		return new DBSqlResult(result, rowCount);
	}

	/**
	 * {@link java.util.List}객체를 {@link com.nemustech.common.db.type.DBMapList} 객체로 바꾼다. {@link java.util.List}에 {@link com.nemustech.common.db.type.DBMap}이 있다면, {@link com.nemustech.common.db.type.DBMapList}로 바로 변경하며,
	 * 그 외 객체가 있다면, {@link com.nemustech.common.db.type.DBMap}으로 변경 후에 {@link com.nemustech.common.db.type.DBMapList}로 변경한다.
	 * 
	 * @param list {@link java.util.List}객체
	 * @return {@link com.nemustech.common.db.type.DBMapList} 객체
	 */
	@SuppressWarnings("rawtypes")
	protected DBMapList appendRowsToList(List list) {
		DBMapList mapList = new DBMapList(0);

		for (Object obj : list.toArray()) {
			if (obj == null) {
				continue;
			}

			else if (obj instanceof Map) {
				mapList.add((DBMap) obj);
			}

			else {
				mapList.add(new DBMap().put(obj.getClass().getSimpleName(), obj));
			}
		}

		return mapList;
	}

	@SuppressWarnings("rawtypes")
	private List selectList(SqlSessionFactory factory, DBSqlParameter sqlParam) throws SQLException {
		SqlSession session = factory.openSession();

		try {
			return session.selectList(sqlParam.getSqlID(), sqlParam.getParameters());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @return 성공하면 RowCount를 통해서 MyBatis의 insert 반환값(DB 및 쿼리 설정에 따라 다름), 실패시에는 -1
	 */
	public DBSqlResult insert(DBSqlParameter sqlParam) {
		log.trace("Start::insert()");
		log.trace("  > SQL ID: " + sqlParam.getSqlID());

		SqlSession session = getSqlFactory(sqlParam).openSession(false);
		int count = -1;

		try {
			count = session.insert(sqlParam.getSqlID(), sqlParam.getParameters());
		} catch (Exception e) {
			log.debug(String.format("Insert failed! > Param [ %s ] Cause: %s", sqlParam.getParameters().toString(),
					e.getMessage()));
			session.rollback();
			count = -1;
		} finally {
			session.commit();

			if (session != null) {
				session.close();
			}
		}

		log.trace("  > RV(count): " + count);
		log.trace("End::insert()");

		return new DBSqlResult(count);
	}

	/**
	 * @return 성공하면 RowCount를 통해서 영향받은 row의 개수(insert, update, delete), 실패시에는 0
	 */
	public DBSqlResult executeTransaction(DBSqlParameter[] sqlParams, String dsName) {
		log.trace("Start::executeTransaction()");
		log.trace("  > sqlParams count: " + sqlParams.length);

		DBSqlParameter dsParam = new DBSqlParameter();
		dsParam.setDsName(dsName);
		SqlSession session = getSqlFactory(dsParam).openSession(false);
		int uncommitRows = 0;

		for (DBSqlParameter param : sqlParams) {
			try {
				param.setDsName(dsName);
				uncommitRows += session.update(param.getSqlID(), param.getParameters());
			} catch (Exception e) {
				log.trace(String.format("Param [ %s ] > Failed! Cause: %s", param.getParameters().toString(),
						e.getMessage()));
				uncommitRows = 0;
				break;
			}
		}

		try {
			if (uncommitRows > 0) {
				session.commit(true);
				log.trace(String.format("Success to insert multiple SQL as one transaction! > Commit rows : %d",
						uncommitRows));
			}

			else {
				session.rollback(true);
				log.trace(String.format("Failed to insert multiple SQL as one transaction!"));
			}

			return new DBSqlResult(uncommitRows);
		} finally {
			if (session != null) {
				session.close();
			}

			log.info("  > RV(Transaction commit count): " + uncommitRows);
			log.trace("End::executeTransaction()");
		}
	}

	/**
	 * @param sqlParam SQL 실행을 위한 파라미터. 각 SQL에 맞틑 파라미터를 반드시 <code>List</code>로 전달한다.
	 * @param commitSize 한번에 commit할 개수. 최소 2개 이상
	 * @return 성공하면 RowCount를 통해서 commit 한 row의 개수
	 */
	@SuppressWarnings("rawtypes")
	public DBSqlResult insertBatch(DBSqlParameter sqlParam, int commitSize) {
		log.trace("Start::insertBatch()");
		log.trace("  > sqlParam: " + sqlParam);
		log.trace("  > commitSize: " + commitSize);

		SqlSession session = getSqlFactory(sqlParam).openSession(ExecutorType.BATCH, false);
		List parameters = (List) sqlParam.getParameters();
		Iterator iter = parameters.iterator();
		int uncommitRows = 0;
		int commitRows = 0;
		Object obj = null;
		commitSize = Math.max(commitSize, 2);

		while (iter.hasNext()) {
			try {
				obj = iter.next();
				session.insert(sqlParam.getSqlID(), obj);
				++uncommitRows;
				log.debug(String.format("Param [ %s ] > Inserted!", obj.toString()));

				if (uncommitRows >= commitSize) {
					session.commit(true);
					commitRows += uncommitRows;
					uncommitRows = 0;
				}
			} catch (Exception e) {
				log.warn(String.format("Param [ %s ] > Failed! Cause: %s", obj.toString(), e.getMessage()));
				continue;
			}
		}

		try {
			if (uncommitRows > 0) {
				session.commit(true);
				commitRows += uncommitRows;
			}

			else if (commitRows == 0) {
				session.rollback(true);
			}

			return new DBSqlResult(commitRows);
		} finally {
			if (session != null) {
				session.close();
			}

			log.info(String.format("SQL [%s] batch result > Total params : %d, Commit rows : %d", sqlParam.getSqlID(),
					parameters.size(), commitRows));
			log.trace("End::insertBatch()");
		}
	}

	public DBSqlResult delete(DBSqlParameter sqlParam) throws SQLException {
		log.trace("Start::delete()");
		log.trace("  > sqlParam: " + sqlParam);

		SqlSession session = getSqlFactory(sqlParam).openSession(false);
		int count = -1;

		try {
			count = session.delete(sqlParam.getSqlID(), sqlParam.getParameters());
		} finally {
			if (count >= 0) {
				session.commit();
			}

			else {
				session.rollback();
			}

			if (session != null) {
				session.close();
			}
		}

		log.trace("  > RV(count): " + count);
		log.trace("Start::delete()");

		return new DBSqlResult(count);
	}

	public DBSqlResult update(DBSqlParameter sqlParam) throws SQLException {
		log.trace("Start::update()");
		log.trace("  > sqlParam: " + sqlParam);

		SqlSession session = getSqlFactory(sqlParam).openSession();
		int count = -1;

		try {
			count = session.update(sqlParam.getSqlID(), sqlParam.getParameters());
		} finally {
			if (count >= 0) {
				session.commit();
			}

			else {
				session.rollback();
			}

			if (session != null) {
				session.close();
			}
		}

		log.trace("  > RV(count): " + count);
		log.trace("Start::update()");

		return new DBSqlResult(count);
	}

	/**
	 * SQL을 수행할 {@link org.apache.ibatis.session.SqlSessionFactory}을 돌려준다.
	 * 
	 * @param sqlParam environment id를 가지고 있는 <code>DBSqlParameter</code>.
	 * @return <code>SqlSessionFactory</code>
	 */
	public SqlSessionFactory getSqlFactory(DBSqlParameter sqlParam) {
		return MyBatisConfig.getSqlSessionFactory(sqlParam.getDsName());
	}

	/**
	 * SQL을 수행할 {@link org.apache.ibatis.session.SqlSessionFactory}을 돌려준다.
	 * 
	 * @param envName environment id
	 * @return <code>SqlSessionFactory</code>
	 */
	public SqlSessionFactory getSqlFactory(String envName) {
		return MyBatisConfig.getSqlSessionFactory(envName);
	}
}
