package org.oh.sample.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.oh.common.file.Files;
import org.oh.common.page.Paging;

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
	 * 아이디(PK)
	 */
//	@Null(message = "반드시 값이 없어야 합니다.") // must be null : 반드시 값이 있어야 합니다.
//	@NotNull(message = "반드시 값이 있어야 합니다.") // may not be null : 반드시 값이 없어야 합니다.
//	@Min(1) // must be greater than or equal to 1 : 반드시 1보다 같거나 커야 합니다.
//	@Max(2) // must be less than or equal to 9223372036854775807 : 반드시 9223372036854775807보다 같거나 작아야 합니다.
	@Column(primaryKey = true, autoIncrement = true, sequence = "sample_seq")
	protected Long id;

	/**
	 * 명칭
	 */
//	@Size(max = 0) // Empty
	@NotEmpty // may not be empty : 반드시 값이 존재하고 길이 혹은 크기가 0보다 커야 합니다.
//	@Size(min = 1, max = 2) // size must be between 1 and 2147483647 : 반드시 최소값 1과(와) 최대값 2147483647 사이의 크기이어야 합니다.
///	@Length(min = 1, max = 2) // length must be between 1 and 2147483647 : 반드시 최소값 1과(와) 최대값 2147483647 사이의 길이이어야 합니다.
	@Column
	protected String name;

	/**
	 * 테스트 아이디(FK)
	 */
	@Column // (references = "Test.id")
	protected Long test_id;

	/**
	 * 갯수 필드
	 */
//	@Column
	protected Integer count;

	protected Set<Test> testSet = new LinkedHashSet<Test>();

	protected Set<Files> filesSet = new LinkedHashSet<Files>();

	public Sample() {
	}

	public Sample(Long id, String name, Long test_id) {
		this.id = id;
		this.name = name;
		this.test_id = test_id;
	}

	@Override
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Set<Test> getTestSet() {
		return testSet;
	}

	public void setTestSet(Set<Test> testSet) {
		this.testSet = testSet;
	}

	public Set<Files> getFilesSet() {
		return filesSet;
	}

	public void setFilesSet(Set<Files> filesSet) {
		this.filesSet = filesSet;
	}
}