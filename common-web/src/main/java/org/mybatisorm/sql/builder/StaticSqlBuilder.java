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

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.log4j.Logger;

public class StaticSqlBuilder extends SqlBuilder {

	private static Logger logger = Logger.getLogger(StaticSqlBuilder.class);
	
	private SqlSource sqlSource;
	
	public StaticSqlBuilder(SqlSourceBuilder sqlSourceParser, Class<?> targetClass) {
		super(sqlSourceParser,targetClass);
	}

	public BoundSql getBoundSql(Object parameterObject) {
		return sqlSource.getBoundSql(parameterObject);
	}

	protected void parse(String sql, Class<?> clazz) {
		sqlSource = getSqlSourceParser().parse(sql, clazz); // null 추가 by skoh
//		sqlSource = getSqlSourceParser().parse(sql, clazz, null); // mybatis ver 3.2.0 이상
	}
}
