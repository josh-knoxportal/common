package org.oh.web.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.annotation.Column;

/**
 * DDL : 테스트 테이블
 * 
 * <pre>
 * -- ORACLE
 * DROP TABLE test CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE test
 * (
 *   test_id NUMBER(20) NOT NULL
 *  ,test_name VARCHAR2(100)
 *  ,reg_id VARCHAR2(100)
 *  ,reg_dt VARCHAR2(14)
 *  ,mod_id VARCHAR2(100)
 *  ,mod_dt VARCHAR2(14)
 *  ,CONSTRAINT test_pk PRIMARY KEY
 * (
 *   test_id
 * )
 * );
 * 
 * DROP SEQUENCE test_seq;
 * CREATE SEQUENCE test_seq INCREMENT BY 1 START WITH 1;
 * </pre>
 * 
 * @author skoh
 */
public class Test extends Common {
	@Column(primaryKey = true, sequence = "test_seq")
	protected Long test_id;

	@Column
	protected String test_name;

	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}

	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}