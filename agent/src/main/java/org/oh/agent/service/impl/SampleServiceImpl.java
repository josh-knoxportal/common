package org.oh.agent.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.oh.agent.AgentException;
import org.oh.agent.Mybatis;
import org.oh.agent.dao.SampleDAO;
import org.oh.agent.domain.Sample;
import org.oh.agent.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService {
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected SampleDAO sampleDAO;

	public SampleServiceImpl() {
	}

	@Async
	public void task() throws AgentException {
		log.debug("----- START task -----");

		SqlSession session01 = null;
		SqlSession session02 = null;

		try {
//			session01 = Mybatis.getSqlSessionFactory("db01").openSession(false);
//			session02 = Mybatis.getSqlSessionFactory("db02").openSession(false);
//
//			List<Sample> samples = sampleDAO.getSampleList(session01);
//			log.debug("samples:" + samples);
//
//			Sample sample = new Sample("test", "테스트");
////			int n = sampleDAO.addSample(session02, sample);
////			log.debug("result:" + n);
//
//			sample = new Sample("test", null);
//			sample = sampleDAO.getSample(session02, sample);
//			log.debug("sample:" + sample);
//
//			session01.commit();
//			session02.commit();
			System.out.println(Thread.currentThread().getName());
			Thread.sleep(5000);
		} catch (Exception e) {
//			session01.rollback();
//			session02.rollback();

			throw new AgentException("SampleService_001", "샘플 서비스 실행 중 오류 발생", e);
//		} finally {
//			session01.close();
//			session02.close();
		}

		log.debug("----- END task -----");
	}
}
