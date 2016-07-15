package org.oh.web.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.Condition;
import org.mybatisorm.Condition.Seperator;
import org.oh.common.util.Utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 기본 모델
 * 
 * @author skoh
 */
public abstract class Default implements Serializable {
	/**
	 * SQL 순번
	 * 
	 * <pre>
	 * - hint 나 fields 를 변경할 경우 반드시 순번 지정 (1 부터 가능)
	 * </pre>
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected Integer sql_seq;

	/**
	 * 힌트
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected String hint;

	/**
	 * 필드
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected String fields;

	/**
	 * 조회 조건 문자열
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected String condition;

	/**
	 * 조회 조건
	 */
	@JsonIgnore
	protected Condition condition2;

	/**
	 * 정렬 기준
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected String order_by;

	public Integer getSql_seq() {
		return sql_seq;
	}

	public void setSql_seq(Integer sql_seq) {
		this.sql_seq = sql_seq;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;

		addCondition(condition);
	}

	public void addCondition(String condition) {
		addCondition(null, condition);
	}

	public void addCondition(String seperator, String condition) {
		if (!Utils.isValidate(condition))
			return;

		getCondition(seperator).add(condition);
	}

	public void addCondition(String operator, Object... value) {
		addCondition(null, operator, value);
	}

	public void addCondition(String field, String operator, Object... value) {
		addCondition(null, field, operator, value);
	}

	public void addCondition(String seperator, String field, String operator, Object... value) {
		if (!Utils.isValidate(value))
			return;

		getCondition(seperator).add(field, operator, value);
	}

	public Condition getCondition2() {
		return condition2;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	protected Condition getCondition(String seperator) {
		if (condition2 == null) {
			if ("OR".equalsIgnoreCase(seperator)) {
				condition2 = new Condition(Seperator.OR);
			} else {
				condition2 = new Condition();
			}
		}

		return condition2;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
