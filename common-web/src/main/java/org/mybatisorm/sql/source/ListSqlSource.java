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
package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.mybatisorm.Query;
import org.oh.common.util.StringUtil;
import org.oh.common.util.Utils;

public class ListSqlSource extends AbstractSelectSqlSource {

	private static Logger logger = Logger.getLogger(ListSqlSource.class);
	
	public ListSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
	}

	public BoundSql getBoundSql(final Object queryParam) {
		Query query = (Query)queryParam;
		// (필드, 힌트), 테이블 추가 by skoh
		staticSql = Utils.replaceLastString(staticSql, "SELECT", "FROM", query.getFields());
		staticSql = Utils.insertString(staticSql, "SELECT", query.getHint());
		staticSql = Utils.replaceLastString(staticSql, "FROM", query.getTable());
		if (query.getTable() != null && query.getTable().startsWith("TABLE ")) {
			staticSql = StringUtil.replace(staticSql, "SELECT", "");
			staticSql = StringUtil.replace(staticSql, "FROM", "");
		}
		// 주석 처리 by skoh
//		String where = null;
		StringBuilder sb = new StringBuilder(staticSql);
		// 모든 조건 적용 by skoh
//		where = query.hasCondition() ? query.getCondition() :
//			handler.getNotNullColumnEqualFieldAnd(query.getParameter(),Query.PARAMETER_PREFIX);
		String where = query.getNotNullColumnEqualFieldAndVia(handler);
		where = makeCondition(where, query);
		if (where != null && where.length() > 0) {
			sb.append(" WHERE ").append(where);
		}
		// 그룹방식 추가 by skoh
		if (query.hasGroupBy())
			sb.append(" GROUP BY ").append(query.buildGroupBy());
		// HAVING 추가 by skoh
		if (query.hasHaving())
			sb.append(" HAVING ").append(query.getHaving());
		if (query.hasOrderBy())
			sb.append(" ORDER BY ").append(query.buildOrderBy());
		return getBoundSql(sb.toString(),queryParam);
	}
}
