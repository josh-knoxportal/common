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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.mybatisorm.Query;

public class UpdateSqlSource extends AbstractUpdateSqlSource {

	private static Log logger = LogFactory.getLog(UpdateSqlSource.class);
	
	public UpdateSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser,clazz);
	}

	public BoundSql getBoundSql(final Object parameter) {
		// 모든 조건 적용 by skoh
//		return makeSet(
//				handler.getNotNullNonPrimaryKeyEqualFieldComma(parameter),
//				parameter);
		String set = "";
		if (parameter instanceof Query) {
			set = handler.getNotNullNonPrimaryKeyEqualFieldComma(((Query) parameter).getParameter(),
					Query.PARAMETER_PREFIX);
		} else {
			set = handler.getNotNullNonPrimaryKeyEqualFieldComma(parameter);
		}
		return makeSet(set, parameter);
	}
}
