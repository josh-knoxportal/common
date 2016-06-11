package org.oh.sample.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.mybatisorm.annotation.Column;
import org.oh.web.page.Paging;

/**
 * DDL : 샘플 테이블
 * 
 * <pre>
 * -- ORACLE
 * DROP TABLE sample CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE sample
 * (
 *   id NUMBER(20) NOT NULL
 *  ,name VARCHAR2(100)
 *  ,test_id NUMBER(20) NOT NULL
 *  ,reg_id VARCHAR2(100)
 *  ,reg_dt VARCHAR2(14)
 *  ,mod_id VARCHAR2(100)
 *  ,mod_dt VARCHAR2(14)
 *  ,CONSTRAINT sample_pk PRIMARY KEY
 * (
 *   id
 * ));
 * 
 * DROP SEQUENCE sample_seq;
 * CREATE SEQUENCE sample_seq INCREMENT BY 1 START WITH 1;
 * </pre>
 * 
 * @author skoh
 */
public class Sample extends Paging {
	/**
	 * 샘플 아이디(PK), 시퀀스
	 */
	@Min(0)
	@Max(Long.MAX_VALUE)
	@Column(primaryKey = true, sequence = "sample_seq")
	protected Long id;

	/**
	 * 샘플명
	 */
	@NotNull
	@NotEmpty
	@Column
	protected String name;

	/**
	 * 테스트 아이디(FK)
	 */
//	@Column(references = "Test.id")
	protected Long test_id;

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

	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}
}