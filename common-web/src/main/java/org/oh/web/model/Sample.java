package org.oh.web.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.annotation.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DDL : 샘플 테이블
 * 
 * <pre>
 * -- ORACLE
 * DROP TABLE sample CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE sample
 * (
 *   sample_id NUMBER(20) NOT NULL
 *  ,sample_name VARCHAR2(100)
 *  ,test_id NUMBER(20) NOT NULL
 *  ,reg_id VARCHAR2(100)
 *  ,reg_dt VARCHAR2(14)
 *  ,mod_id VARCHAR2(100)
 *  ,mod_dt VARCHAR2(14)
 *  ,CONSTRAINT sample_pk PRIMARY KEY
 * (
 *   sample_id
 * )
 * );
 * 
 * DROP SEQUENCE sample_seq;
 * CREATE SEQUENCE sample_seq INCREMENT BY 1 START WITH 1;
 * </pre>
 * 
 * @author skoh
 */
public class Sample extends Common {
	/**
	 * 샘플 아이디(PK), 시퀀스
	 */
	@Column(primaryKey = true, sequence = "sample_seq")
	@Min(0)
	@Max(Long.MAX_VALUE)
	@JsonProperty("sample_id")
	protected Long sample_id;

	/**
	 * 샘플명
	 */
	@Column
	protected String sample_name;

	/**
	 * 테스트 아이디(FK)
	 */
	@Column(references = "Test.test_id")
	protected Long test_id;

	public Long getSample_id() {
		return sample_id;
	}

	public void setSample_id(Long sample_id) {
		this.sample_id = sample_id;
	}

	public String getSample_name() {
		return sample_name;
	}

	public void setSample_name(String sample_name) {
		this.sample_name = sample_name;
	}

	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}