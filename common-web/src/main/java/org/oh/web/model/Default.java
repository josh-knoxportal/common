package org.oh.web.model;

import java.io.Serializable;

import javax.validation.constraints.Null;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.Condition;
import org.mybatisorm.Condition.Seperator;
import org.oh.common.util.Utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 기본 모델
 * 
 * @author skoh
 */
public abstract class Default implements Serializable {
	/**
	 * SQL명
	 * 
	 * <pre>
	 * - hint 나 fields 를 지정하여 FROM 절 앞단을 변형할때 반드시 지정 (보통 호출하는 메소드명을 사용)
	 * </pre>
	 */
	protected String sql_name;

	/**
	 * 힌트
	 */
	protected String hint;

	/**
	 * 필드
	 */
	protected String fields;

	/**
	 * 테이블
	 */
	protected String table;

	/**
	 * 정렬 기준
	 */
	protected String order_by;

	/**
	 * 조회 조건(문자열)
	 */
	protected String condition;

	/**
	 * 조회 조건
	 */
	protected Condition condition2 = new Condition();

	@JsonIgnore
	@Null(message = "This parameter can not be used.")
	public String getSql_name() {
		return sql_name;
	}

	public void setSql_name(String sql_name) {
		this.sql_name = sql_name;
	}

	@JsonIgnore
	@Null(message = "This parameter can not be used.")
	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	@JsonIgnore
	@Null(message = "This parameter can not be used.")
	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@JsonIgnore
	@Null(message = "This parameter can not be used.")
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@JsonIgnore
	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	@JsonIgnore
	@Null(message = "This parameter can not be used.")
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		addCondition(condition);
	}

	@JsonIgnore
	public Condition getCondition2() {
		return condition2;
	}

	public void setCondition2(Condition condition2) {
		this.condition2 = condition2;
	}

	///////////////////////////////////////////////////////////////////////////

	public void addCondition(String condition) {
		if (!Utils.isValidate(condition))
			return;

		this.condition = condition;
		condition2.add(condition);
	}

	public void addCondition(String operator, Object... value) {
		addCondition(null, operator, value);
	}

	public void addCondition(String field, String operator, Object... value) {
		if (!Utils.isValidate(value))
			return;

		condition2.add(field, operator, value);
	}

	public Condition newCondition() {
		return newCondition(null);
	}

	public Condition newCondition(String seperator) {
		Condition condition = null;
		if ("OR".equalsIgnoreCase(seperator)) {
			condition = new Condition(Seperator.OR);
		} else {
			condition = new Condition();
		}

		return condition;
	}

	public void addCondition(Condition condition) {
		this.condition2.add(condition);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
