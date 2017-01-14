package com.nemustech.common.function;

/**
 * 예외 발생한 Supplier
 *
 * @param <T>
 */
public interface CheckedSupplier<T> {
	T get() throws Exception;
}