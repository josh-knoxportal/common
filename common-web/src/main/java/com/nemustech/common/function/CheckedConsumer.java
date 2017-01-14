package com.nemustech.common.function;

/**
 * 예외 발생한 Consumer
 *
 * @param <T>
 */
public interface CheckedConsumer<T> {
	void accept(T t);
}