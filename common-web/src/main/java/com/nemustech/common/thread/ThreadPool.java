package com.nemustech.common.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import com.nemustech.common.util.StringUtil;

/**
 * 쓰레드 풀
 * 
 * @author skoh
 *
 * @param <T> 쓰레드 응답 타입
 */
public class ThreadPool<T> {
	protected ExecutorService executorService = null;

	public ThreadPool() {
		this(10, 100, 60);
	}

	/**
	 * 쓰레드 풀을 생성한다.
	 * 
	 * @param minPoolSize 최소 쓰레드 풀 갯수
	 * @param maximumPoolSize 최대 쓰레드 풀 갯수
	 * @param keepAliveTime 쓰레드 반환 대기 시간(초)
	 */
	public ThreadPool(int minPoolSize, int maximumPoolSize, long keepAliveTime) {
		executorService = new ThreadPoolExecutor(minPoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * 쓰레드를 실행한다.
	 * 
	 * @param task
	 * @return
	 */
	public Future<T> submit(Callable<T> task) {
		return executorService.submit(task);
	}

	/**
	 * 쓰레드 풀을 종료한다.
	 */
	@PreDestroy
	public void shutdown() {
		if (executorService != null)
			executorService.shutdown();
	}

	@Override
	public String toString() {
		return StringUtil.toString(this);
	}
}
