package org.oh.sample.model;

import org.mybatisorm.annotation.Fields;
import org.mybatisorm.annotation.Join;
import org.oh.web.model.Default;

/**
 * Sample 테이블을 기준으로 Test 테이블을 조인
 * 
 * @author skoh
 */
@Join
public class SampleAndTest extends Default {
	@Fields("*")
	protected Sample sample;

	@Fields("*")
	protected Test test;
//	protected List<Test> test;

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

//	public List<Test> getTest() {
//		return test;
//	}
//
//	public void setTest(List<Test> test) {
//		this.test = test;
//	}
}