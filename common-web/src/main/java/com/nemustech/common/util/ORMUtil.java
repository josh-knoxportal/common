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
	 * Table
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
	 * Column
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> List<Field> getColumnFields(Class<T> clazz) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : TableHandler.getFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				list.add(field);
			}
		}

		return list;
	}

	/**
	 * PrimaryKey
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Field getPrimaryKeyField(Class<T> clazz) {
		List<Field> list = getColumnFields(clazz);

		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			boolean primaryKey = column.primaryKey();
			if (primaryKey) {
				return field;
			}
		}

		return null;
	}

	/**
	 * AutoIncrement
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Field getAutoIncrementField(Class<T> clazz) {
		List<Field> list = getColumnFields(clazz);

		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			boolean autoIncrement = column.autoIncrement();
			if (autoIncrement) {
				return field;
			}
		}

		return null;
	}

	/**
	 * Sequence
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> String getSequence(Class<T> clazz) {
		Field field = getSequenceField(clazz);

		return getSequence(field);
	}

	public static <T> Field getSequenceField(Class<T> clazz) {
		List<Field> list = getColumnFields(clazz);

		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			String sequence = column.sequence();
			if (!"".equals(sequence)) {
				return field;
			}
		}

		return null;
	}

	public static <T> String getSequence(Field field) {
		if (field == null)
			return null;

		return field.getAnnotation(Column.class).sequence();
	}

	/**
	 * DefaultValue
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> List<KeyValue> getDefaultValueList(Class<T> clazz) {
		List<Field> fieldList = getDefaultValueFields(clazz);

		return getDefaultValueList(fieldList);
	}

	public static <T> List<Field> getDefaultValueFields(Class<T> clazz) {
		List<Field> resultList = new ArrayList<Field>();

		List<Field> list = getColumnFields(clazz);
		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			String defaultValue = column.defaultValue();
			if (!"".equals(defaultValue)) {
				resultList.add(field);
			}
		}

		return resultList;
	}

	public static <T> List<KeyValue> getDefaultValueList(List<Field> fieldList) {
		List<KeyValue> list = new ArrayList<KeyValue>();
		for (Field field : fieldList) {
			Column column = field.getAnnotation(Column.class);
			list.add(new DefaultKeyValue("".equals(column.value()) ? field.getName() : column.value(),
					field.getAnnotation(Column.class).defaultValue()));
		}

		return list;
	}

	/**
	 * NoneDefaultValue
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> List<KeyValue> getNoneDefaultValueList(Class<T> clazz) {
		List<Field> fieldList = getNoneDefaultValueFields(clazz);

		return getNoneDefaultValueList(fieldList);
	}

	public static <T> List<Field> getNoneDefaultValueFields(Class<T> clazz) {
		List<Field> resultList = new ArrayList<Field>();

		List<Field> list = getColumnFields(clazz);
		for (Field field : list) {
			Column column = field.getAnnotation(Column.class);
			String defaultValue = column.defaultValue();
			if ("".equals(defaultValue)) {
				resultList.add(field);
			}
		}

		return resultList;
	}

	public static <T> List<KeyValue> getNoneDefaultValueList(List<Field> fieldList) {
		List<KeyValue> list = new ArrayList<KeyValue>();
		for (Field field : fieldList) {
			Column column = field.getAnnotation(Column.class);
			list.add(
					new DefaultKeyValue("".equals(column.value()) ? field.getName() : column.value(), field.getName()));
		}

		return list;
	}

	public static <T> List<KeyValue> getColumnList(Class<T> clazz) {
		List<KeyValue> list = new ArrayList<KeyValue>();

		List<Field> fieldList = getColumnFields(clazz);
		for (Field field : fieldList) {
			Column column = field.getAnnotation(Column.class);
			String columnName = ("".equals(column.value())) ? field.getName() : column.value();
			String defaultValue = column.defaultValue();
			if ("".equals(defaultValue)) {
				list.add(new DefaultKeyValue(columnName, field.getName()));
			} else {
				list.add(new DefaultKeyValue(columnName, defaultValue));
			}
		}

		return list;
	}

	public static void main(String[] args) {
//		System.out.println("table:\t\t" + getTable(Default.class));
//		System.out.println("columnFields:\t" + getColumnFields(Default.class));
//		System.out.println("primaryKeyField:" + getPrimaryKeyField(Default.class));
//		System.out.println("autoIncrement:\t" + getAutoIncrementField(Default.class));
//		System.out.println("sequence:\t" + getSequence(Default.class));
//		System.out.println("defaultValue:\t" + getDefaultValueList(Default.class));
//		System.out.println("noneDefaultValue:" + getNoneDefaultValueList(Default.class));
//		System.out.println("defaultValue2:" + getColumnList(Default.class));
//		System.out.println();
//
//		System.out.println("table:\t\t" + getTable(Common.class));
//		System.out.println("columnFields:\t" + getColumnFields(Common.class));
//		System.out.println("primaryKeyField:" + getPrimaryKeyField(Common.class));
//		System.out.println("autoIncrement:\t" + getAutoIncrementField(Common.class));
//		System.out.println("sequence:\t" + getSequence(Common.class));
//		System.out.println("defaultValue:\t" + getDefaultValueList(Common.class));
//		System.out.println("noneDefaultValue:" + getNoneDefaultValueList(Common.class));
//		System.out.println("defaultValue2:" + getColumnList(Common.class));
//		System.out.println();
//
//		System.out.println("table:\t\t" + getTable(Sample.class));
//		System.out.println("columnFields:\t" + getColumnFields(Sample.class));
//		System.out.println("primaryKeyField: " + getPrimaryKeyField(Sample.class));
//		System.out.println("autoIncrement:\t" + getAutoIncrementField(Sample.class));
//		System.out.println("sequence:\t" + getSequence(Sample.class));
//		System.out.println("defaultValue:\t" + getDefaultValueList(Sample.class));
//		System.out.println("noneDefaultValue:" + getNoneDefaultValueList(Sample.class));
//		System.out.println("defaultValue2:" + getColumnList(Sample.class));
	}
}
