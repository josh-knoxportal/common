package com.nemustech.common.function;

/**
 * 예외 발생한 Function
 * 
 * @param <T>
 * @param <R>
 */
public interface CheckedFunction<T, R> {
	R apply(T t) throws Exception;
}