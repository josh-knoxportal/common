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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.annotation.handler.HandlerFactory;
import org.mybatisorm.annotation.handler.TableHandler;

public class Query {

	private static final String PARAMETER_RE = "#\\{([0-9a-zA-Z_\\.]+)\\}";

	private static final Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_RE);
	
	public static final String PARAMETER_PREFIX = "parameter.";
	
	// 변수 바인딩 by skoh
	public static final String VARIABLE_PREFIX = "\b";

	private static final String CONDITION_REPLACEMENT = "#{" + PARAMETER_PREFIX + "$1}";
	
	private static Log logger = LogFactory.getLog(Query.class);

	// SQL명 추가 by skoh
	protected String sqlName;
	// 힌트 추가 by skoh
	protected String hint;
	// 필드 추가 by skoh
	protected String fields;
	// 테이블 추가 by skoh
	protected String table;
	// 그룹방식 추가 by skoh
	protected String groupBy;
	// HAVING 추가 by skoh
	protected String having;
	
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
		this(parameter, new Condition().add(condition), orderBy);
	}
	
	public Query(Object parameter, Condition condition, String orderBy) {
		this.parameter = parameter;
		this.orderBy = orderBy;
		this.condition = condition;
	}
	
	// 생성자 추가 by skoh
	public Query(Object parameter, Condition condition, String orderBy, String table, String sqlName) {
		this(parameter, condition, orderBy, null, null, table, null, null, sqlName);
	}
	
	// 힌트, 필드, 테이블, 그룹방식, HAVING, SQL명 추가 by skoh
	public Query(Object parameter, Condition condition, String orderBy,
			String hint, String fields, String table, String groupBy, String having, String sqlName) {
		this(parameter, condition, orderBy);
		this.parameter = parameter;
		this.condition = condition;
		this.orderBy = orderBy;
		this.hint = hint;
		this.fields = fields;
		this.table = table;
		this.groupBy = groupBy;
		this.having = having;
		this.sqlName = sqlName;
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
	
	// SQL명 추가 by skoh
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	
	// 힌트 추가 by skoh
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	// 필드 추가 by skoh
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	// 테이블 추가 by skoh
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	
	// 그룹방식 추가 by skoh
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	
	// HAVING 추가 by skoh
	public String getHaving() {
		return having;
	}
	public void setHaving(String having) {
		this.having = having;
	}
	
	public String getCondition() {
		return condition.build(this);
	}
	// 조건 추가 by skoh
	public void setCondition(Condition condition) {
		this.condition = condition;
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