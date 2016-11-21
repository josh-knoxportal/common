package com.nemustech.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.annotation.handler.TableHandler;

public abstract class ORMUtil {
	public static <T> String getTableName(Class<T> clazz) {
		Annotation anno = clazz.getAnnotation(Table.class);
		if (anno == null)
			return null;

		return AnnotationUtil.getValue(anno).toString();
	}

	public static <T> List<Field> getColumnFields(Class<T> clazz) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : TableHandler.getFields(clazz)) {
			if (field.isAnnotationPresent(Column.class)) {
				list.add(field);
			}
		}

		return list;
	}

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

	public static <T> Field getAutoIncrement(Class<T> clazz) {
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

	public static <T> String getSequence(Class<T> clazz) {
		Field field = getSequenceField(clazz);

		return getSequence(field);
	}

	public static <T> String getSequence(Field field) {
		if (field == null)
			return null;

		return field.getAnnotation(Column.class).sequence();
	}

	public static void main(String[] args) {
//		System.out.println(getTableName(Default.class));
//		System.out.println(getColumnFields(Default.class));
//		System.out.println(getPrimaryKeyField(Default.class));
//		System.out.println(getAutoIncrement(Default.class));
//		System.out.println(getSequence(Default.class));
//		System.out.println(getSequenceField(Default.class));
//		System.out.println();
//
//		System.out.println(getTableName(Sample.class));
//		System.out.println(getColumnFields(Sample.class));
//		System.out.println(getPrimaryKeyField(Sample.class));
//		System.out.println(getAutoIncrement(Sample.class));
//		System.out.println(getSequence(Sample.class));
//		System.out.println(getSequenceField(Sample.class));
	}
}
