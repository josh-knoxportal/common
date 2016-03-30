package org.oh.web.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.annotation.Fields;
import org.mybatisorm.annotation.Join;

@Join
public class SampleAndTest extends Default {
	@Fields("*")
	protected Sample sample;

	@Fields("*")
	protected Test test;

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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}