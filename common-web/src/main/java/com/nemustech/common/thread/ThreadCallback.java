package com.nemustech.common.thread;

import org.apache.commons.logging.Log;
import com.nemustech.common.FunctionCallback;

/**
 * 쓰레드 콜백
 * 
 * @author skoh
 *
 * @param <T1> 파라미터 타입
 * @param <T2> 응답 타입
 */
public class ThreadCallback<T1, T2> extends AbstractThread<T1, T2> {
	public ThreadCallback(FunctionCallback<T1, T2> callback, String title, T1 params) {
		super(callback, title, params);
	}
	
	public ThreadCallback(FunctionCallback<T1, T2> callback, String title, Log log, T1 params) {
		super(callback, title, log, params);
	}

	@Override
	public T2 excute() throws Exception {
		return ((FunctionCallback<T1, T2>) target).executeTemplate(params);
	}
}