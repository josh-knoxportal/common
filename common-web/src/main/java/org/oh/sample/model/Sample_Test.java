package org.oh.sample.model;

import org.mybatisorm.annotation.Column;
import org.oh.web.page.Paging;

/**
 * DDL : 샘플 테스트 매핑테이블
 * 
 * <pre>
 * -- ORACLE
 * DROP TABLE sample_test CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE sample_test
 * (
 *   sample_id NUMBER(20) NOT NULL
 *  ,test_id NUMBER(20) NOT NULL
 *  ,reg_id VARCHAR2(100)
 *  ,reg_dt VARCHAR2(14)
 *  ,mod_id VARCHAR2(100)
 *  ,mod_dt VARCHAR2(14)
 *  ,CONSTRAINT sample_test_pk PRIMARY KEY
 * (
 *   sample_id
 *  ,test_id
 * ));
 * </pre>
 * 
 * @author skoh
 */
public class Sample_Test extends Paging {
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