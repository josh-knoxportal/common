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
package org.mybatisorm.annotation.handler;

import java.lang.reflect.Field;

import org.mybatisorm.Query;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.TypeHandler;

/**
 * Mybatis 에서 사용되는 토큰 생성
 * 
 * @author yangkun7@gmail.com
 */
public class TokenMaker {
	// sb.append(columnPrefix).append(ColumnAnnotation.getName(field, column)).append(" = ").append(" #{").append(fieldPrefix).append(field.getName()).append("}");

	// 변수 바인딩 by skoh
//	public static String mybatisToken(String fieldName, TypeHandler typeHandlerAnnotation, String fieldPrefix) {
	public static String mybatisToken(String fieldName, TypeHandler typeHandlerAnnotation, String fieldPrefix, Object value) {
		StringBuilder token = new StringBuilder();
		// 변수 바인딩 by skoh
//		token.append("#{");
		if (value != null && value instanceof String && ((String)value).startsWith(Query.VARIABLE_PREFIX)) {
			token.append("${");
		} else {
			token.append("#{");
		}
		if (fieldPrefix != null && fieldPrefix.length() > 0) token.append(fieldPrefix);
		token.append(fieldName);
		if (typeHandlerAnnotation != null) {
			token.append(",typeHandler=" + typeHandlerAnnotation.value().getName());
			token.append(",jdbcType=" + typeHandlerAnnotation.jdbcType().toString());
		}
		token.append("}");
		return token.toString();
	}

	// 변수 바인딩 by skoh
//	public static String mybatisToken(Field field, String fieldPrefix) {
//		return mybatisToken(field.getName(), field.getAnnotation(TypeHandler.class), fieldPrefix);
	public static String mybatisToken(Field field, String fieldPrefix, Object value) {
		return mybatisToken(field.getName(), field.getAnnotation(TypeHandler.class), fieldPrefix, value);
	}

	// 변수 바인딩 by skoh
//	public static String fieldEqual(Field field, Column column, String fieldPrefix, String columnPrefix) {
	public static String fieldEqual(Field field, Column column, String fieldPrefix, String columnPrefix, Object value) {
		StringBuilder token = new StringBuilder();

		if (columnPrefix != null && columnPrefix.length() > 0) token.append(columnPrefix);
		if (column != null) token.append(ColumnAnnotation.getName(field, column));
		else token.append(ColumnAnnotation.getName(field));

		token.append(" = ");
		// 변수 바인딩 by skoh
//		token.append(mybatisToken(field, fieldPrefix));
		token.append(mybatisToken(field, fieldPrefix, value));
		return token.toString();
	}

	// 변수 바인딩 by skoh
//	public static String mybatisToken(Field field) {
//		return mybatisToken(field, null);
	public static String mybatisToken(Field field, Object value) {
		return mybatisToken(field, null, value);
	}

	// 변수 바인딩 by skoh
//	public static String fieldEqual(Field field, Column column) {
//		return fieldEqual(field, column, null, null);
	public static String fieldEqual(Field field, Column column, Object value) {
		return fieldEqual(field, column, null, null, value);
	}

	// 변수 바인딩 by skoh
//	public static String fieldEqual(Field field) {
//		return fieldEqual(field, null, null, null);
	public static String fieldEqual(Field field, Object value) {
		return fieldEqual(field, null, null, null, value);
	}

}
