package org.oh.sample.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.oh.web.page.Paging;

/**
 * DDL : 샘플 테이블
 * 
 * <pre>
 * -- Mysql
 * DROP TABLE IF EXISTS sample;
 * CREATE TABLE sample
 * (
 *  id INT(20) NOT NULL AUTO_INCREMENT
 *  ,name VARCHAR(100)
 *  ,test_id INT(20) NOT NULL
 *  ,reg_id VARCHAR(100) NOT NULL
 *  ,reg_dt VARCHAR(14) DEFAULT DATE_FORMAT (NOW(), '%Y%m%d%H%i%s') NOT NULL
 *  ,mod_id VARCHAR(100) NOT NULL
 *  ,mod_dt VARCHAR(14) DEFAULT DATE_FORMAT (NOW(), '%Y%m%d%H%i%s') NOT NULL
 *  ,PRIMARY KEY
 * (
 *   id
 * )) CHARSET=utf8;
 * 
 * -- Oracle
 * DROP TABLE sample CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE sample
 * (
 *   id NUMBER(20) NOT NULL
 *  ,name VARCHAR2(100)
 *  ,test_id NUMBER(20) NOT NULL
 *  ,reg_id VARCHAR2(100) NOT NULL
 *  ,reg_dt VARCHAR2(14) DEFAULT TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') NOT NULL
 *  ,mod_id VARCHAR2(100) NOT NULL
 *  ,mod_dt VARCHAR2(14) DEFAULT TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') NOT NULL
 *  ,CONSTRAINT sample_pk PRIMARY KEY
 * (
 *   id
 * ));
 * DROP SEQUENCE sample_seq;
 * CREATE SEQUENCE sample_seq INCREMENT BY 1 START WITH 1;
 * 
 * SELECT 
 *     sample_.reg_id sample_reg_id,
 *     sample_.reg_dt sample_reg_dt,
 *     sample_.mod_id sample_mod_id,
 *     sample_.mod_dt sample_mod_dt,
 *     sample_.id sample_id,
 *     sample_.name sample_name,
 *     sample_.test_id sample_test_id,
 *     test_.reg_id test_reg_id,
 *     test_.reg_dt test_reg_dt,
 *     test_.mod_id test_mod_id,
 *     test_.mod_dt test_mod_dt,
 *     test_.id test_id,
 *     test_.name test_name
 * FROM
 *     Sample sample_
 *         INNER JOIN
 *     Test test_ ON (test_.id = sample_.test_id)
 * WHERE
 *     (test_.name LIKE 't%')
 * ORDER BY sample_.id DESC , test_.id DESC;
 * </pre>
 * 
 * @author skoh
 */
@Table("sample")
public class Sample extends Paging {
	/**
	 * 샘플 아이디(PK), 시퀀스
	 */
	@Column(primaryKey = true, autoIncrement = true) // mysql // , sequence = "sample_seq") // oracle
	protected Long id;

	/**
	 * 샘플명
	 */
	@Column
	protected String name;

	/**
	 * 테스트 아이디(FK)
	 */
	@Column(references = "Test.id")
	protected Long test_id;

//	@Null // 반드시 값이 있어야 합니다.
//	@NotNull(message = "반드시 값이 있어야 합니다.") // 반드시 값이 없어야 합니다.
//	@Min(1) // 반드시 1보다 같거나 커야 합니다.
//	@Max(Long.MAX_VALUE) // 반드시 9223372036854775807보다 같거나 작아야 합니다.
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty // 반드시 값이 존재하고 길이 혹은 크기가 0보다 커야 합니다.
//	@Size(min = 1, max = Integer.MAX_VALUE) // 반드시 최소값 1과(와) 최대값 2147483647 사이의 크기이어야 합니다.
///	@Length(min = 1, max = Integer.MAX_VALUE) // 반드시 최소값 1과(와) 최대값 2147483647 사이의 길이이어야 합니다.
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