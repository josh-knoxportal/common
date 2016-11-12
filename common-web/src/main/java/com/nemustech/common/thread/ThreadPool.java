package com.nemustech.common.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 쓰레드 풀
 * 
 * @author skoh
 *
 * @param <T> 쓰레드 응답 타입
 */
public class ThreadPool<T> {
	/**
	 * 최소 쓰레드 풀 갯수
	 */
	protected int minPoolSize = 0;

	/**
	 * 최대 쓰레드 풀 갯수
	 */
	protected int maximumPoolSize = 0;

	/**
	 * 쓰레드 반환 대기 시간(초)
	 */
	protected long keepAliveTime = 0;

	protected ExecutorService executorService = null;

	public ThreadPool() {
		this(10, 100, 60);
	}

	/**
	 * 쓰레드 풀을 생성한다.
	 * 
	 * @param minPoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 */
	public ThreadPool(int minPoolSize, int maximumPoolSize, long keepAliveTime) {
		this.minPoolSize = minPoolSize;
		this.maximumPoolSize = maximumPoolSize;
		this.keepAliveTime = keepAliveTime;

		executorService = new ThreadPoolExecutor(minPoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
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
}
