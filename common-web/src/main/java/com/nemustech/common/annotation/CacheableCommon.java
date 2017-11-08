package com.nemustech.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

import com.nemustech.common.service.CacheService;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Cacheable(value = CacheService.CACHE_NAME, key = "#root.caches[0].name + '_' + #root.targetClass + '_' + #root.methodName + '_' + T(com.nemustech.common.util.StringUtil).toString(#root.args, 'conditionObj')")
public @interface CacheableCommon {
}