package com.nemustech.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.annotation.handler.TableHandler;

public abstract class ORMUtil {
	/**
	 * 테이블
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> String getTable(Class<T> clazz) {
//		Annotation anno = clazz.getAnnotation(Table.class);
//		if (anno == null)
//			return null;
//
//		return AnnotationUtil.getValue(anno).toString();
		Table table = clazz.getAnnotation(Table.class);
		if (table == null)
			return null;

		String value = table.value();
		if ("".equals(value))
			return clazz.getSimpleName();

		return value;
	}

	/**
	 * Primary key <컬럼명, 필드명>
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> KeyValue getPrimaryKey(Class<T> clazz) {
		Field field = getPrimaryKeyField(clazz);

		return getPrimaryKey(field);
	}

	/**
	 * 자동 증가 <컬럼명>
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> String getAutoIncrement(Class<T> clazz) {
		Field field = getAutoIncrementField(clazz);

		return getAutoIncrement(field);
	}

	/**
	 * 시퀀스 <테이블명, 컬럼명>
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> KeyValue getSequence(Class<T> clazz) {
		Field field = getSequenceField(clazz);

		return getSequence(field);
	}
	

	public static <T> List<KeyValue> getCreateColumnList(Class<T> clazz) {
		return getCreateColumnList(clazz, false);
	}

	/**
	 * 신규 컬럼 리스트 <컬럼명, (#{필드명} | 기본값)>
	 * 
	 * @param clazz
	 * @param primaryKey
	 * @return
	 */
	public static <T> List<KeyValue> getCreateColumnList(Class<T> clazz, boolean excludePrimaryKey) {
		List<KeyValue> list = new ArrayList<KeyValue>();

		List<Field> fieldList = getColumnFieldList(clazz);
		for (Field field : fieldList) {
			Column column = field.getAnnotation(Column.class);
			if (excludePrimaryKey && column.primaryKey()) {
				continue;
			}

			String columnName = ("".equals(column.value())) ? field.getName() : column.value();
			String defaultValue = column.defaultValue();

			if ("".equals(defaultValue)) {
				list.add(new DefaultKeyValue(columnName, "#{" + field.getName() + "}"));
			} else {
				list.add(new DefaultKeyValue(columnName, defaultValue));
			}
		}

		return list;
	}

	/**
	 * 수정 컬럼 리스트 <컬럼명, (#{필드명} | 기본값)>
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> List<KeyValue> getUpdateColumnList(Class<T> clazz) {
		List<KeyValue> list = new ArrayList<KeyValue>();

		List<Field> fieldList = getColumnFieldList(clazz);
		for (Field field : fieldList) {
			Column column = field.getAnnotation(Column.class);
			if (column.primaryKey()) {
				continue;
			}

			String columnName = ("".equals(column.value())) ? field.getName() : column.value();
			String defaultValue = column.defaultValue();

			if ("".equals(defaultValue)) {
				list.add(new DefaultKeyValue(columnName, "#{" + field.getName() + "}"));
			} else {
				if (column.defaultUpdate()) {
					list.add(new DefaultKeyValue(columnName, defaultValue));
				}
			}
		}

		return list;
	}

	/**
	 * 컬럼 필드 리스트 <필드>
	 * 
	 * @param clazz
	 * @return
	 */
	protected static <T> List<Field> getColumnFieldList(Class<T> clazz) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : TableHandler.getFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				list.add(field);
			}
		}

		return list;
	}

	protected static <T> Field getPrimaryKeyField(Class<T> clazz) {
		List<Field> list = getColumnFieldList(clazz);

		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			if (column.primaryKey()) {
				return field;
			}
		}

		return null;
	}

	protected static KeyValue getPrimaryKey(Field field) {
		if (field == null)
			return null;

		Column column = field.getAnnotation(Column.class);
		return new DefaultKeyValue(("".equals(column.value())) ? field.getName() : column.value(), field.getName());
	}

	protected static <T> Field getAutoIncrementField(Class<T> clazz) {
		List<Field> list = getColumnFieldList(clazz);

		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			if (column.autoIncrement()) {
				return field;
			}
		}

		return null;
	}

	protected static String getAutoIncrement(Field field) {
		if (field == null)
			return null;

		Column column = field.getAnnotation(Column.class);
		return ("".equals(column.value())) ? field.getName() : column.value();
	}

	protected static <T> Field getSequenceField(Class<T> clazz) {
		List<Field> list = getColumnFieldList(clazz);

		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			if (!"".equals(column.sequence())) {
				return field;
			}
		}

		return null;
	}

	protected static KeyValue getSequence(Field field) {
		if (field == null)
			return null;

		Column column = field.getAnnotation(Column.class);
		return new DefaultKeyValue(column.sequence(), ("".equals(column.value())) ? field.getName() : column.value());
	}

	public static void main(String[] args) {
//		System.out.println("table:\t\t\t" + getTable(Default.class));
//		System.out.println("primaryKey:\t\t" + getPrimaryKey(Default.class));
//		System.out.println("autoIncrement:\t\t" + getAutoIncrement(Default.class));
//		System.out.println("sequence:\t\t" + getSequence(Default.class));
//
//		System.out.println("columnFieldList:\t" + JsonUtil2.toStringPretty(getColumnFieldList(Default.class)));
//		System.out.println("createColumnList:\t" + JsonUtil2.toStringPretty(getCreateColumnList(Default.class)));
//		System.out.println("updateColumnList:\t" + JsonUtil2.toStringPretty(getUpdateColumnList(Default.class)));
//
//		System.out.println("--------------------------------------------------------------------------------");
//
//		System.out.println("table:\t\t\t" + getTable(Common.class));
//		System.out.println("primaryKey:\t\t" + getPrimaryKey(Common.class));
//		System.out.println("autoIncrement:\t\t" + getAutoIncrement(Common.class));
//		System.out.println("sequence:\t\t" + getSequence(Common.class));
//
//		System.out.println("columnFieldList:\t" + JsonUtil2.toStringPretty(getColumnFieldList(Common.class)));
//		System.out.println("createColumnList:\t" + JsonUtil2.toStringPretty(getCreateColumnList(Common.class)));
//		System.out.println("updateColumnList:\t" + JsonUtil2.toStringPretty(getUpdateColumnList(Common.class)));
//
//		TableHandler handler = HandlerFactory.getHandler(Common.class);
//		System.out.println("columnAsFieldComma:\t" + handler.getColumnAsFieldComma());
//
//		System.out.println("--------------------------------------------------------------------------------");
//
//		System.out.println("table:\t\t\t" + getTable(Sample.class));
//		System.out.println("primaryKey:\t\t" + getPrimaryKey(Sample.class));
//		System.out.println("autoIncrement:\t\t" + getAutoIncrement(Sample.class));
//		System.out.println("sequence:\t\t" + getSequence(Sample.class));
//
//		System.out.println("columnFieldList:\t" + JsonUtil2.toStringPretty(getColumnFieldList(Sample.class)));
//		System.out.println("createColumnList:\t" + JsonUtil2.toStringPretty(getCreateColumnList(Sample.class)));
//		System.out.println("updateColumnList:\t" + JsonUtil2.toStringPretty(getUpdateColumnList(Sample.class)));
//
//		handler = HandlerFactory.getHandler(Sample.class);
//		System.out.println("columnAsFieldComma:\t" + handler.getColumnAsFieldComma());
	}
}
