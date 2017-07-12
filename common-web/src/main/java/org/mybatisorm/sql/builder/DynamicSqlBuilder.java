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
package org.mybatisorm.sql.builder;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.parsing.PropertyParser;
import org.mybatisorm.Query;

import com.nemustech.common.util.Utils;

public abstract class DynamicSqlBuilder extends SqlBuilder {

	private static Log logger = LogFactory.getLog(DynamicSqlBuilder.class);

	protected String staticSql;

	public DynamicSqlBuilder(SqlSourceBuilder sqlSourceParser, Class<?> targetClass) {
		super(sqlSourceParser, targetClass);
	}

	// 주석 처리 by skoh
//	public abstract BoundSql getBoundSql(Object parameterObject);

	protected BoundSql getBoundSql(String sql, Object parameterObject) {
		// 타이틀 추가 by skoh
		logger.debug("==>  Preparing: " + sql);
		// 변수 바인딩 by skoh
		sql = parserVariable(sql, parameterObject);
//		return getSqlSourceParser().parse(sql, parameterObject.getClass()).getBoundSql(parameterObject); // null 추가 by skoh
		return getSqlSourceParser().parse(sql, parameterObject.getClass(), null).getBoundSql(parameterObject); // mybatis ver 3.2.0 이상
	}

	protected BoundSql makeWhere(String where, Object parameter) {
		return getBoundSql(where != null && where.length() > 0 ? staticSql + " WHERE " + where : staticSql, parameter);
	}

	// 모든 조건 적용 by skoh
	protected String makeCondition(String where, Query query) {
		String and = (where.length() > 0) ? " AND " : "";
		return where + (query.hasCondition() ? and + query.getCondition() : "");
	}

	// 바인딩 변수 by skoh
	protected String parserVariable(String sql, Object parameterObject) {
		Properties variables = new Properties();
		Map<String, Object> map = null;
		if (parameterObject instanceof Query) {
			map = Utils.convertObjectToMap(((Query) parameterObject).getParameter(), Query.VARIABLE_PREFIX,
					Query.PARAMETER_PREFIX, false);
		} else {
			map = Utils.convertObjectToMap(parameterObject, Query.VARIABLE_PREFIX, false);
		}
		variables.putAll(map);

		return PropertyParser.parse(sql, variables);
	}
}
