package com.nemustech.sample.model;

import org.mybatisorm.annotation.Fields;
import org.mybatisorm.annotation.Join;

import com.nemustech.common.model.Default;
import com.nemustech.common.page.Paging;

/**
 * Sample 테이블을 기준으로 Files 테이블을 조인
 * 
 * @author skoh
 */
@Join("sample LEFT JOIN files")
public class SampleAndFiles extends Paging {
	@Fields("*")
	protected Sample sample = new Sample();

	@Fields("*")
	protected Files2 files2 = new Files2();

	public SampleAndFiles() {
	}

	public SampleAndFiles(Sample sample, Files2 files2) {
		this.sample = sample;
		this.files2 = files2;
	}

	@Override
	public Default model() {
		return sample;
	}

	@Override
	public Default[] joinModels() {
		return new Default[] { files2 };
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public Files2 getFiles2() {
		return files2;
	}

	public void setFiles2(Files2 files2) {
		this.files2 = files2;
	}
}