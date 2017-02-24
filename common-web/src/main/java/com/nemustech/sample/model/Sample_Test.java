package com.nemustech.sample.model;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;

import com.nemustech.common.model.Default;

/**
 * DDL : 샘플 테스트 매핑테이블
 * 
 * <pre>
 * -- Mysql
 * DROP TABLE IF EXISTS sample_test;
 * CREATE TABLE sample_test
 * (
 *   sample_id INT(20) NOT NULL
 *  ,test_id INT(20) NOT NULL
 *  ,reg_id VARCHAR(100) NOT NULL
 *  ,reg_dt VARCHAR(14) NOT NULL
 *  ,mod_id VARCHAR(100) NOT NULL
 *  ,mod_dt VARCHAR(14) NOT NULL
 *  ,PRIMARY KEY
 * (
 *   sample_id
 *  ,test_id
 * )) CHARSET=utf8;
 * 
 * -- Oracle
 * DROP TABLE sample_test CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE sample_test
 * (
 *   sample_id NUMBER(20) NOT NULL
 *  ,test_id NUMBER(20) NOT NULL
 *  ,reg_id VARCHAR2(100) NOT NULL
 *  ,reg_dt VARCHAR2(14) DEFAULT TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') NOT NULL
 *  ,mod_id VARCHAR2(100) NOT NULL
 *  ,mod_dt VARCHAR2(14) DEFAULT TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') NOT NULL
 *  ,CONSTRAINT sample_test_pk PRIMARY KEY
 * (
 *   sample_id
 *  ,test_id
 * ));
 * </pre>
 * 
 * @author skoh
 */
@Table("sample_test")
public class Sample_Test extends Default {
	@Column(references = "Sample.id")
	protected Long sample_id;

	@Column(references = "Test.id")
	protected Long test_id;

	public Long getSample_id() {
		return sample_id;
	}

	public void setSample_id(Long sample_id) {
		this.sample_id = sample_id;
	}

	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}
}