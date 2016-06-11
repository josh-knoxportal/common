package org.oh.sample.model;

import org.mybatisorm.annotation.Fields;

/**
 * Sample_Test 메핑 테이블을 기준으로 Sample, Test 테이블을 조인
 * 
 * @author skoh
 */
public class SampleAndTest2 extends SampleAndTest {
	@Fields("*")
	protected Sample sample;

	/**
	 * 필드 순서에 주의
	 */
	protected Sample_Test sample_test;

	@Fields("id, name")
	protected Test test;

	public Sample_Test getSample_test() {
		return sample_test;
	}

	public void setSample_test(Sample_Test sample_test) {
		this.sample_test = sample_test;
	}
}