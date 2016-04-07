package org.oh.web.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.annotation.Fields;

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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}