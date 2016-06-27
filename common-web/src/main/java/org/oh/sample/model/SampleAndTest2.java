package org.oh.sample.model;

import org.mybatisorm.annotation.Fields;

/**
 * Sample_Test 메핑 테이블을 기준으로 Sample, Test 테이블을 조인
 * 
 * <pre>
 * SELECT 
 *     sample_.reg_id sample_reg_id,
 *     sample_.reg_dt sample_reg_dt,
 *     sample_.mod_id sample_mod_id,
 *     sample_.mod_dt sample_mod_dt,
 *     sample_.id sample_id,
 *     sample_.name sample_name,
 *     sample_.test_id sample_test_id,
 *     test_.id test_id,
 *     test_.name test_name
 * FROM
 *     Sample sample_
 *         INNER JOIN
 *     Sample_Test sample_test_ ON (sample_.id = sample_test_.sample_id)
 *         INNER JOIN
 *     Test test_ ON (test_.id = sample_test_.test_id)
 * WHERE
 *     (test_.name LIKE 't%')
 * ORDER BY sample_.id DESC , test_.id DESC;
 * </pre>
 * 
 * @author skoh
 */
public class SampleAndTest2 extends SampleAndTest {
	@Fields("*")
	protected Sample sample = new Sample();

	/**
	 * 필드 순서에 주의
	 */
	protected Sample_Test sample_test = new Sample_Test();

	@Fields("id, name")
	protected Test test = new Test();

	public SampleAndTest2() {
	}

	public SampleAndTest2(Sample sample, Test test) {
		this.sample = sample;
		this.test = test;
	}

	public Sample_Test getSample_test() {
		return sample_test;
	}

	public void setSample_test(Sample_Test sample_test) {
		this.sample_test = sample_test;
	}
}