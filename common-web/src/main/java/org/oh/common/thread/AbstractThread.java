package org.oh.common.thread;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.oh.common.util.LogUtil;
import org.oh.common.util.Utils;
import org.springframework.util.StopWatch;

/**
 * 추상 쓰레드
 * 
 * @author skoh
 *
 * @param <T1> 파라미터 타입
 * @param <T2> 응답 타입
 */
public abstract class AbstractThread<T1, T2> implements Callable<T2> {
	/**
	 * 실행대상
	 */
	protected Object target = null;

	/**
	 * 실행명
	 */
	protected String title = null;

	/**
	 * 로그(선택)
	 */
	protected Log log = null;

	/**
	 * 파라미터
	 */
	protected T1 params = null;

	public AbstractThread(Object target, String title, T1 params) {
		this.target = target;
		this.title = title;
		this.params = params;
	}

	public AbstractThread(Object target, String title, Log log, T1 params) {
		this(target, title, params);
		this.log = log;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public T1 getParams() {
		return params;
	}

	public void setParams(T1 params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	public T2 call() throws Exception {
		StopWatch sw = new StopWatch(title);
		sw.start();

		T2 result = excute();

		sw.stop();
		String message = sw.shortSummary();
		if (log == null)
			LogUtil.writeLog(message, getClass());
		else
			log.info(message);

		return result;
	}

	/**
	 * 쓰레드 실행
	 * 
	 * @return 응답결과
	 * 
	 * @throws Exception
	 */
	protected abstract T2 excute() throws Exception;
}