package org.oh.web.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.Condition;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 기본 모델
 * 
 * @author skoh
 */
public abstract class Default implements Serializable {
	/**
	 * Statement 순번
	 */
	protected Integer stmtSeq;

	/**
	 * 힌트
	 */
	protected String hint;

	/**
	 * 필드
	 */
	protected String fields;

	/**
	 * 조회 조건 문자열
	 */
	protected String condition;

	/**
	 * 조회 조건
	 */
	protected Condition condition2;

	/**
	 * 정렬 기준
	 */
	protected String order_by;

	@JsonIgnore
	public Integer getStmtSeq() {
		return stmtSeq;
	}

	public void setStmtSeq(Integer stmtSeq) {
		this.stmtSeq = stmtSeq;
	}

	@JsonIgnore
	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	@JsonIgnore
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;

		addCondition(condition);
	}

	public void addCondition(String condition) {
		addCondition(new Condition().add(condition));
	}

	@JsonIgnore
	public Condition getCondition2() {
		return condition2;
	}

	public void setCondition(Condition condition2) {
		this.condition2 = condition2;
	}

	public void addCondition(Condition condition2) {
		if (this.condition2 == null) {
			this.condition2 = new Condition();
		}

		this.condition2.add(condition2);
	}

	@JsonIgnore
	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@JsonIgnore
	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
