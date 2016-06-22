package org.oh.sample.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Cacheable(value = "sample", key = "#root.caches[0].name + '_' + #root.targetClass + '_' + #root.methodName + '_' + T(org.oh.common.util.ReflectionUtil).toString(#root.args)")
public @interface CacheableSample {
}