package org.oh.sample.model;

import org.mybatisorm.annotation.Fields;
import org.mybatisorm.annotation.Join;
import org.oh.common.model.Default;

/**
 * Sample 테이블을 기준으로 Test 테이블을 조인
 * 
 * @author skoh
 */
@Join // Inner Join
//@Join("sample LEFT JOIN test") // Outer Join
//@Join("sample LEFT JOIN test LEFT JOIN files")
public class SampleAndTest extends Default {
	@Fields("*")
	protected Sample sample = new Sample(); // 인스턴스를 생성해야 기본 조건이 만들어짐.

	@Fields("id, name") // @Column 이 선언된 필드명 리스트 (, 로 구분하고 모든 필드는 *)
	protected Test test = new Test();

	@Fields("*")
	protected Files2 files = new Files2();

	public SampleAndTest() {
	}

	public SampleAndTest(Sample sample, Test test, Files2 files) {
		this.sample = sample;
		this.test = test;
		this.files = files;
	}

	@Override
	public Default getModel() {
		return sample;
	}

	@Override
	public Default[] getJoinModels() {
		return new Default[] { test, files };
	}

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

	public Files2 getFiles() {
		return files;
	}

	public void setFiles(Files2 files) {
		this.files = files;
	}
}