package org.oh.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * <pre>
 * Map 유틸리티 클래스.
 * - org.apache.commons.collections.MapUtils 클래스를 상속받음.
 * </pre>
 * 
 * @see <a href=http://commons.apache.org/proper/commons-collections/javadocs/api-release/org/apache/commons/collections4/MapUtils.html>org.apache.commons.collections.MapUtils</a>
 */
public abstract class MapUtil extends MapUtils {
	/**
	 * 파라미터를 Map<String, Object>으로 반환한다.
	 * 
	 * @param params 파라미터
	 * 
	 * @return
	 */
	public static <T> Map<String, T> convertArrayToMap(T[]... params) {
		Map<String, T> map = new LinkedHashMap<String, T>();
		for (T[] param : params) {
			if (param.length < 2)
				continue;

			map.put((String) param[0], param[1]);
		}

		return map;
	}

	public static void main(String[] args) {
		System.out.println(convertArrayToMap(new String[] { "a", "1" }, new String[] { "b", "2" }));
	}
}
