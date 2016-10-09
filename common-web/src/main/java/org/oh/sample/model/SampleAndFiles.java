package org.oh.sample.model;

import org.mybatisorm.annotation.Fields;
import org.mybatisorm.annotation.Join;
import org.oh.common.model.Default;

/**
 * Sample 테이블을 기준으로 Files 테이블을 조인
 * 
 * @author skoh
 */
@Join("sample LEFT JOIN files")
public class SampleAndFiles extends Default {
	@Fields("*")
	protected Sample sample = new Sample();

	@Fields("*")
	protected Files2 files = new Files2();

	public SampleAndFiles() {
	}

	public SampleAndFiles(Sample sample, Files2 files) {
		this.sample = sample;
		this.files = files;
	}

	@Override
	public Default getModel() {
		return sample;
	}

	@Override
	public Default[] getJoinModels() {
		return new Default[] { files };
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public Files2 getFiles() {
		return files;
	}

	public void setFiles(Files2 files) {
		this.files = files;
	}
}