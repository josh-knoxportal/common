package org.oh.adapter.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.oh.common.db.dao.MyBatisDao;
import org.springframework.stereotype.Component;

@Component
public class MyBatisGenericDao extends MyBatisDao implements GenericDBDao {
	public SqlSession openSession(String envName) {
		return openSession(envName, true);
	}

	public SqlSession openSession(String envName, boolean autoCommit) {
		SqlSessionFactory factory = getSqlFactory(envName);
		return factory.openSession(autoCommit);
	}
}
