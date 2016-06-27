/*
 *    Copyright 2012 The MyBatisORM Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatisorm;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.TableHandler;

public class Query {

	private static final String PARAMETER_RE = "#\\{([0-9a-zA-Z_\\.]+)\\}";

	private static final Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_RE);
	
	public static final String PARAMETER_PREFIX = "parameter.";
	
	// 변수 바인딩 by skoh
	public static final String VARIABLE_PREFIX = "\b";

	private static final String CONDITION_REPLACEMENT = "#{" + PARAMETER_PREFIX + "$1}";
	
	private static Logger logger = Logger.getLogger(Query.class);

	// SQL 순번
	private Integer sqlSeq;
	// 힌트 추가 by skoh
	private String hint;
	// 필드 추가 by skoh
	private String fields;
	private String orderBy;
	private Condition condition;
	private Object parameter;
	private Map<String,Object> properties;
	
	private int pageNumber;
	private int rows;
	private int start;
	private int end;

	// 변수 바인딩 by skoh
	public static String makeVariable(String variable) {
		if (variable == null)
			return variable;

		return variable + VARIABLE_PREFIX;
	}
	
	public Query() {
	}
	
	public Query(Object parameter) {
		this.parameter = parameter;
	}
	
	public Query(Object parameter, String orderBy) {
		this.parameter = parameter;
		this.orderBy = orderBy;
	}
	
	public Query(Object parameter, String condition, String orderBy) {
		// 조건이 있을 경우만 추가 by skoh
//		this(parameter, new Condition().add(condition), orderBy);
		this(parameter, new Condition().add(condition), orderBy, null, null, 0);
	}
	
	public Query(Object parameter, Condition condition, String orderBy) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.condition = condition;
	}
	
	// 힌트, 필드 추가, SQL 순번 by skoh
	public Query(Object parameter, Condition condition, String orderBy, String hint, String fields, Integer sqlSeq) {
		this(parameter, condition, orderBy);
		this.hint = hint;
		this.fields = fields;
		this.sqlSeq = sqlSeq;
	}
	
	public Query(Object parameter, String orderBy, int pageNumber, int rows) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.pageNumber = pageNumber;
		this.rows = rows;
	}
	
	public Query(Object parameter, String condition, String orderBy, int pageNumber, int rows) {
		// 조건이 있을 경우만 추가 by skoh
//		this(parameter, new Condition().add(condition), orderBy, pageNumber, rows);
		this(parameter, (condition == null) ? new Condition() : new Condition().add(condition), orderBy, pageNumber, rows);
	}

	public Query(Object parameter, Condition condition, String orderBy, int pageNumber, int rows) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.pageNumber = pageNumber;
		this.rows = rows;
		this.condition = condition;
	}
	
	public String buildOrderBy() {
		if (orderBy == null)
			return null;
		
		return HandlerFactory.getHandler(parameter).buildOrderBy(orderBy);
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public boolean hasOrderBy() {
		return orderBy != null;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	// SQL 순번 추가 by skoh
	public Integer getSqlSeq() {
		return sqlSeq;
	}
	// 힌트 추가 by skoh
	public String getHint() {
		return hint;
	}
	// 필드 추가 by skoh
	public String getFields() {
		return fields;
	}
	public String getCondition() {
		return condition.build(this);
	}
	public boolean hasCondition() {
		// 조건 변경 by skoh
//		return condition != null;
		return condition != null && condition.getList().size() > 0;
	}
	private static String replaceParam(String value) {
		return value.replaceAll(PARAMETER_RE, CONDITION_REPLACEMENT);
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Map<String,Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String,Object> properties) {
		this.properties = properties;
	}
	
	public String addProperty(Object value) {
		if (value instanceof String) {
			Matcher m = PARAMETER_PATTERN.matcher((String)value);
			if (m.find())
				return replaceParam((String)value);
		}
		if (properties == null)
			properties = new HashMap<String,Object>();
		int index = properties.size();
		properties.put(String.valueOf(index),value);
		return "#{properties."+index+"}";
	}
	
	public String getNotNullColumnEqualFieldAndVia(TableHandler handler) {
		return parameter == null ? "" :
			handler.getNotNullColumnEqualFieldAnd(parameter,PARAMETER_PREFIX);
	}

	public String getPrimaryKeyNotNullColumnEqualFieldAnd1(TableHandler handler) {
		return parameter == null ? "" :
			handler.getNotNullPrimaryKeyEqualFieldAnd(parameter,PARAMETER_PREFIX);
	}

	public static void main(String[] args) {
		Matcher m = PARAMETER_PATTERN.matcher("'%' || #{board.messageId} || '%'");
//		Matcher m = PARAMETER_PATTERN.matcher("'%' || #{name} || '%'#{board.messageId}");
		System.out.println(m.find());
	}


}