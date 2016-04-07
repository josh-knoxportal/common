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
 *   id NUMBER(20) NOT NULL
 *  ,name VARCHAR2(100)
 *  ,reg_id VARCHAR2(100)
 *  ,reg_dt VARCHAR2(14)
 *  ,mod_id VARCHAR2(100)
 *  ,mod_dt VARCHAR2(14)
 *  ,CONSTRAINT test_pk PRIMARY KEY
 * (
 *   id
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
	protected Long id;

	@Column
	protected String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}