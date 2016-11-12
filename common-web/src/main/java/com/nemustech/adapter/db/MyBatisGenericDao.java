package com.nemustech.adapter.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.nemustech.common.db.mybatis.MyBatisDao;
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
