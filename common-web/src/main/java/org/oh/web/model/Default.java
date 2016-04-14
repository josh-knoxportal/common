package org.oh.web.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 기본 모델
 * 
 * @author skoh
 */
public class Default implements Serializable {
	/**
	 * 조회 조건
	 */
	protected String condition;

	/**
	 * 정렬 기준
	 */
	protected String order_by;

	@JsonIgnore
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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
