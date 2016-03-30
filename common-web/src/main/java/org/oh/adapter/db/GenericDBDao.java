package org.oh.adapter.db;

import org.apache.ibatis.session.SqlSession;
import org.oh.common.db.dao.DBDao;

public interface GenericDBDao extends DBDao {
	public SqlSession openSession(String envName);

	public SqlSession openSession(String envName, boolean autoCommit);
}
