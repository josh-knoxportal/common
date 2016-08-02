package org.oh.agent.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.oh.agent.model.Sample;
import org.springframework.stereotype.Repository;

@Repository
public class SampleDAO {
	protected static Log log = LogFactory.getLog(SampleDAO.class);

	public SampleDAO() {
	}

	public Sample getSample(SqlSession session, Sample sample) {
		return session.selectOne("sample.getSample", sample);
	}

	public List<Sample> getSampleList(SqlSession session) {
		return session.selectList("sample.getSampleList");
	}

	public int addSample(SqlSession session, Sample sample) {
		return session.insert("sample.addSample", sample);
	}
}
