package com.nemustech.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

import com.nemustech.common.service.CacheService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

import com.nemustech.common.service.CacheService;

/**
 * 동기화 캐시에 등록하는 Annotation
 * 
 * <pre>
 * - 캐시에 등록, 삭제시 바로 서버간 동기화
 * - 주로 서버간의 실시간 동기화시 사용
 * - 서버간의 마치 하나의 캐시처럼 사용
 * - 서버간의 데이타 이동으로 작은 데이타 동기화시 적합
 * </pre>
 * 
 * @author skoh
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Cacheable(value = CacheService.CACHE_NAME_SYNC, key = CacheService.CACHEABLE_KEY)
public @interface CacheableSync {
}