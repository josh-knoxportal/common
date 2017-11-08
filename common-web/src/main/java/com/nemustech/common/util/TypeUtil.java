package com.nemustech.common.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.reflect.TypeUtils;

/**
 * 타입 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.lang3.reflect.TypeUtils 클래스를 상속받음.
 *
 * @see <a
 *      href=https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/reflect/TypeUtils.html>org.apache.commons.lang3.reflect.TypeUtils
 *      </a>
 */
public abstract class TypeUtil extends TypeUtils {
	/**
	 * Collection 타입인지 확인한다.
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isCollection(Object obj) {
		return isInstanceOf(obj, Iterable.class, Iterator.class, Enumeration.class, Map.class);
	}

	/**
	 * 타입이 일치하는지 확인한다.
	 * 
	 * @param obj
	 * @param clzs
	 * @return
	 */
	public static boolean isInstanceOf(Object obj, Class<?>... clzs) {
		for (Class<?> clz : clzs) {
			if (clz.isInstance(obj)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) {
		System.out.println(isInstanceOf(1L, String.class, Integer.class, Long.class));
	}
}
