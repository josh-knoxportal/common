/* Copyright 2012 The MyBatisORM Team
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package org.mybatisorm.sql.source;

import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;
import org.oh.common.util.StringUtil;

@SqlCommand(value = SqlCommandType.SELECT)
public abstract class AbstractSelectSqlSource extends DynamicSqlBuilder {

	public AbstractSelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser, clazz);
		// staticSql = "SELECT " + handler.getColumnAsFieldComma + " FROM " + handler.getName();
		// 2013-03-23 yangkun7@gmail.com (컬럼명 뒤에 필드명 Alias 적용되면 Inline-ResultMap 과 맵핑이 불완전함)
//		staticSql = "SELECT " + handler.getColumnComma() + " FROM " + handler.getName(); // getColumnAsFieldComma으로 변경 by skoh
		staticSql = "SELECT " + handler.getColumnAsFieldComma() + " FROM " + handler.getName();
	}

	public AbstractSelectSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz, String staticSql) {
		super(sqlSourceParser, clazz);
		this.staticSql = staticSql;
	}

	public List<ResultMapping> getResultMappingList() {
		return handler.getResultMappingList(sqlSourceBuilder.getConfiguration());
	}

	public Class<?> getResultType() {
		return handler.getTargetClass();
	}

	// 힌트 추가 by skoh
	protected void makeHint(Query query) {
		int index = staticSql.indexOf("SELECT");
		if (query.getHint() != null && index >= 0) {
			staticSql = staticSql.substring(0, index) + staticSql.substring(index, index + 6) + " " + query.getHint()
					+ " " + staticSql.substring(index + 7);
		}
	}

	// 필드 추가 by skoh
	protected void makeFields(Query query) {
		int index = staticSql.indexOf("SELECT");
		int index2 = staticSql.lastIndexOf("FROM");
		if (query.getFields() != null && index >= 0 && index2 >= 0) {
			staticSql = staticSql.substring(0, index) + staticSql.substring(index, index + 6) + " " + query.getFields()
					+ " " + staticSql.substring(index2);
		}
	}

	// 테이블 추가 by skoh
	protected void makeTable(Query query) {
		int index = staticSql.lastIndexOf("FROM");
		if (query.getTable() != null && index >= 0) {
			staticSql = staticSql.substring(0, index) + staticSql.substring(index, index + 4) + " " + query.getTable();

			if (query.getTable().startsWith("TABLE ")) {
				staticSql = StringUtil.replace(staticSql, "SELECT", "");
				staticSql = StringUtil.replace(staticSql, "FROM", "");
			}
		}
	}
}
