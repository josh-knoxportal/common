package com.nemustech.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

import com.nemustech.common.service.CacheService;

/**
 * 공통 캐시에 등록하는 Annotation
 * 
 * <pre>
 * - 캐시를 삭제시에만 서버간 동기화
 * - 주로 일반적인 캐시 용도(부하 감소)로 사용
 * - 서버별로 따로 캐시를 등록하여 사용
 * - 서버간의 데이타 이동이 없어 네트워크 트래픽 감소
 * </pre>
 * 
 * @author skoh
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Cacheable(value = CacheService.CACHE_NAME_COMMON, key = CacheService.CACHEABLE_KEY)
public @interface CacheableCommon {
}