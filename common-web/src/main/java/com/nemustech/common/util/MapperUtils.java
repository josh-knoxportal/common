package com.nemustech.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nemustech.common.model.Default;

/**
 * 매퍼 유틸리티 클래스
 */
public abstract class MapperUtils {
	/**
	 * 모델 객체를 ORM 구조의 모델로 변경한다.
	 * 
	 * <pre>
	 * <주의>
	 * - T 객체에는 model(), joinModels() 메소드를 반드시 구현해야 한다.
	 * - model(), joinModels() 반환 객체는 getId() 메소드를 구현하고,
	 *   targetFieldNames 명으로 반드시 Set 필드들을 정의해야 한다.
	 * </pre>
	 * 
	 * @param sourceList 변환하고자 하는 Default 객체 리스트
	 * @param targetFieldNames Default 객체를 담을 Set 필드명들
	 * @return
	 */
	public static <T extends Default> List<Default> convertModels(List<T> sourceList, String... targetFieldNames) {
		if (!Utils.isValidate(sourceList))
			return null;

		Map<Object, Default> targetMap = new LinkedHashMap<Object, Default>();
		for (T source : sourceList) {
			if (source == null || source.model() == null)
				continue;

			// 기본 모델
			Default tempModel = source.model();

			// 중복 제거
			Default sourceModel = targetMap.get(tempModel.id());
			if (sourceModel == null)
				sourceModel = tempModel;

			// 조인 모델들
			for (int i = 0; i < targetFieldNames.length; i++) {
				Default sourceJoinModel = source.joinModels()[i];

				// 아이디가 null 이 아닌 것만 담는다.
				if (sourceJoinModel != null && sourceJoinModel.id() != null) {
					Set<Default> targetSet = (Set) ReflectionUtil.getValue(sourceModel, targetFieldNames[i]);
					targetSet.add(sourceJoinModel);
				}
			}

			targetMap.put(sourceModel.id(), sourceModel);
		}

		return new ArrayList<Default>(targetMap.values());
	}
}