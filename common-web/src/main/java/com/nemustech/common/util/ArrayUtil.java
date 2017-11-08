package com.nemustech.common.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 배열 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.lang3.ArrayUtils 클래스를 상속받음.
 *
 * @see <a
 *      href=https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/ArrayUtils.html>org.apache.commons.lang3.ArrayUtils
 *      </a>
 */
public abstract class ArrayUtil extends ArrayUtils {
	/**
	 * Collection(Iterable, Iterator, Enumeration, Map) 객체를 배열 형태로 변환한다.
	 * 
	 * @param obj
	 * @return
	 */
	protected static Object[] toArray(Object obj) {
		if (obj instanceof Iterable) {
			if (obj instanceof Collection) {
				Collection<Object> col = (Collection) obj;
				return col.toArray();
			} else
				return IteratorUtils.toArray(((Iterable) obj).iterator());
		} else if (obj instanceof Iterator) {
			return IteratorUtils.toArray((Iterator) obj);
		} else if (obj instanceof Enumeration) {
			return IteratorUtils.toArray(IteratorUtils.asIterator((Enumeration) obj));
		} else if (obj instanceof Map) {
			return ((Map) obj).values().toArray();
		} else {
			return new Object[0];
		}
	}
}
