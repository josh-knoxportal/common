package org.oh.common.util;

import org.mybatisorm.annotation.Table;

public abstract class ORMUtil {
	public static <T> String getTableName(Class<T> cls) {
		return AnnotationUtil.getValue(cls.getAnnotation(Table.class)).toString();
	}
}
