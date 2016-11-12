package com.nemustech.adapter.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.nemustech.adapter.DBAdapter;
import com.nemustech.adapter.aspect.AuditRequired;
import com.nemustech.common.db.type.DBSqlParameter;
import com.nemustech.common.db.type.DBSqlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBAdapterImpl implements DBAdapter {
	@Autowired
	private GenericDBDao myBatisDao;

	private SqlSession session;

	public Connection getConnection(String envName) {
		session = myBatisDao.openSession(envName, false);
		return session.getConnection();
	}

	public SqlSession openSession(String envName) {
		return openSession(envName, true);
	}

	public SqlSession openSession(String envName, boolean autoCommit) {
		session = myBatisDao.openSession(envName, autoCommit);
		return session;
	}

	@AuditRequired
	public Object selectOne(String envName, String statement, Map<String, Object> parameters) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.selectOne(statement, parameters);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@AuditRequired
	public Object selectOne(String envName, String statement, Object parameters) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.selectOne(statement, parameters);
		} finally {
			if (session != null)
				session.close();
		}

	}

	@AuditRequired
	public <T> T selectOne(String envName, String statement, Object parameters, Class<T> resultType) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return resultType.cast(session.selectOne(statement, parameters));
		} finally {
			if (session != null)
				session.close();
		}

	}

	@AuditRequired
	public <T> T selectOne(SqlSession session, String statement, Object parameters, Class<T> resultType) {
		return resultType.cast(session.selectOne(statement, parameters));
	}

	@SuppressWarnings("unchecked")
	@AuditRequired
	public <T> List<T> selectList(String envName, String statement, Map<String, Object> parameters,
			Class<T> resultListType) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return (List<T>) session.selectList(statement, parameters);
		} finally {
			if (session != null)
				session.close();
		}

	}

	@AuditRequired
	public List<?> selectList(String envName, String statement, Map<String, Object> parameters, int start, int size) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.selectList(statement, parameters, new RowBounds(start, size));
		} finally {
			if (session != null)
				session.close();
		}

	}

	@AuditRequired
	public List<?> selectList(String envName, String statement, Object parameters) {
		SqlSession session = myBatisDao.openSession(envName);

		try {
			session.getConfiguration().setDefaultStatementTimeout(1);
			return session.selectList(statement, parameters);
		} finally {
			if (session != null)
				session.close();
		}

	}

	@SuppressWarnings("unchecked")
	@AuditRequired
	public <T> List<T> selectList(String envName, String statement, Object parameters, Class<T> resultType) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return (List<T>) session.selectList(statement, parameters);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@AuditRequired
	public <T> List<T> selectList(SqlSession session, String statement, Object parameters, Class<T> resultType) {
		return (List<T>) session.selectList(statement, parameters);
	}

	@SuppressWarnings("unchecked")
	@AuditRequired
	public <T> List<T> selectList(String envName, String statement, Object parameters, int start, int size,
			Class<T> resultType) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return (List<T>) session.selectList(statement, parameters, new RowBounds(start, size));
		} finally {
			if (session != null)
				session.close();
		}
	}

	@AuditRequired
	public List<?> selectList(String envName, String statement, Object parameters, int start, int size) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.selectList(statement, parameters, new RowBounds(start, size));
		} finally {
			if (session != null)
				session.close();
		}

	}

	public DBSqlResult select(DBSqlParameter parameter) throws SQLException {
		DBSqlResult sqlResult = myBatisDao.select(parameter);
		return sqlResult;
	}

	@AuditRequired
	public int insert(String envName, String statement, Object parameter) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.insert(statement, parameter);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@AuditRequired
	public int insert(SqlSession session, String statement, Object parameter) {
		return session.insert(statement, parameter);
	}

	@AuditRequired
	public DBSqlResult insert(DBSqlParameter parameter) throws SQLException {
		return myBatisDao.insert(parameter);
	}

	@AuditRequired
	public int delete(String envName, String statement, Object parameter) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.delete(statement, parameter);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@AuditRequired
	public int delete(SqlSession session, String statement, Object parameter) {
		return session.delete(statement, parameter);
	}

	public DBSqlResult delete(DBSqlParameter parameter) throws SQLException {
		return myBatisDao.delete(parameter);
	}

	@AuditRequired
	public int update(String envName, String statement, Object parameter) {
		SqlSession session = myBatisDao.openSession(envName);
		try {
			return session.update(statement, parameter);
		} finally {
			if (session != null)
				session.close();
		}

	}

	@AuditRequired
	public int update(SqlSession session, String statement, Object parameter) {
		return session.delete(statement, parameter);
	}

	public DBSqlResult update(DBSqlParameter parameter) throws SQLException {
		return myBatisDao.update(parameter);
	}

	public void commitAndClose(SqlSession session) {
		session.commit();
		session.close();
	}

	public void rollbackAndClose(SqlSession session) {
		session.rollback();
		session.close();
	}
}
