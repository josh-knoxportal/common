package com.nemustech.common.model;

import java.io.Serializable;

import javax.validation.constraints.Null;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.Condition;
import org.mybatisorm.Condition.Seperator;
import com.nemustech.common.util.Utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 기본 모델
 * 
 * @author skoh
 */
public abstract class Default implements Serializable {
	public static final String PARAMETER_ERROR_MESSAGE = "This parameter can not be used.";

	/**
	 * SQL명
	 * 
	 * <pre>
	 * - hint, fields, table, group_by, having 를 지정하여 쿼리를 변형할때 반드시 지정
	 * - 보통 호출하는 메소드명을 사용
	 * </pre>
	 */
	@JsonIgnore // 클라이언트가 사용하지 않는 필드
	@Null(message = PARAMETER_ERROR_MESSAGE) // 파라미터로 사용하지 않는 필드
	protected String sql_name;

	/**
	 * 힌트
	 */
	@JsonIgnore
	@Null(message = PARAMETER_ERROR_MESSAGE)
	protected String hint;

	/**
	 * 필드
	 */
	@JsonIgnore
	@Null(message = PARAMETER_ERROR_MESSAGE)
	protected String fields;

	/**
	 * 테이블
	 */
	@JsonIgnore
	@Null(message = PARAMETER_ERROR_MESSAGE)
	protected String table;

	/**
	 * 그룹 방식
	 */
	@JsonIgnore
	@Null(message = PARAMETER_ERROR_MESSAGE)
	protected String group_by;

	/**
	 * HAVING
	 */
	@JsonIgnore
	@Null(message = PARAMETER_ERROR_MESSAGE)
	protected String having;

	/**
	 * 정렬 방식
	 */
	@JsonIgnore
	protected String order_by;

	/**
	 * 조회 조건(문자열)
	 */
	@JsonIgnore
	@Null(message = PARAMETER_ERROR_MESSAGE)
	protected String condition;

	/**
	 * 조회 조건
	 */
	@JsonIgnore
	protected Condition conditionObj = new Condition();

	public Default() {
	}

	public Default(String sql_name, String hint, String fields, String table, String order_by, String condition,
			Condition conditionObj) {
		this.sql_name = sql_name;
		this.hint = hint;
		this.fields = fields;
		this.table = table;
		this.order_by = order_by;
		this.condition = condition;
		this.conditionObj = conditionObj;
	}

	/**
	 * 데이타 중복을 제거하기 위해
	 */
	@Override
	public int hashCode() {
		if (getId() == null)
			return super.hashCode();

		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getId() == null)
			return super.equals(obj);

		if (obj instanceof Default) {
			if (getId().equals(((Default) obj).getId()))
				return true;
		}

		return false;
	}

	/**
	 * 아이디를 구한다.
	 * 
	 * @return
	 */
	public Object getId() {
		return null;
	}

	/**
	 * 기본 모델을 구한다.
	 * 
	 * @return
	 */
	@JsonIgnore
	public Default getModel() {
		return null;
	}

	/**
	 * 조인 모델을 구한다.
	 * 
	 * @return
	 */
	@JsonIgnore
	public Default[] getJoinModels() {
		return new Default[] { null };
	}

	public String getSql_name() {
		return sql_name;
	}

	public void setSql_name(String sql_name) {
		this.sql_name = sql_name;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getGroup_by() {
		return group_by;
	}

	public void setGroup_by(String group_by) {
		this.group_by = group_by;
	}

	public String getHaving() {
		return having;
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		addCondition(condition);
	}

	public Condition getConditionObj() {
		return conditionObj;
	}

	public void setConditionObj(Condition conditionObj) {
		this.conditionObj = conditionObj;
	}

	public void addCondition(String condition) {
		if (!Utils.isValidate(condition))
			return;

		this.condition = condition;
		conditionObj.add(condition);
	}

	public void addCondition(String operator, Object... value) {
		addCondition(null, operator, value);
	}

	public void addCondition(String field, String operator, Object... value) {
		if (!Utils.isValidate(value))
			return;

		conditionObj.add(field, operator, value);
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
		this.conditionObj.add(condition);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
