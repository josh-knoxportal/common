package com.nemustech.common.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;

import com.nemustech.common.function.CallbackFunction;
import com.nemustech.common.thread.ThreadCallback;
import com.nemustech.common.thread.ThreadPool;

/**
 * 쓰레드 유틸
 * 
 * @author skoh
 */
public abstract class ThreadUtils {
	/**
	 * 쓰레드 풀
	 */
	protected static ThreadPool<Object> threadPool = null;

	public static void createThreadPool() {
		String key = "thread.pool.min_size";
		int minSize = PropertyUtils.getInstance().getInt(key, 10);
		LogUtil.writeLog("properties:[" + key + "=" + minSize + "]", ThreadUtils.class);

		key = "thread.pool.max_size";
		int maxSize = PropertyUtils.getInstance().getInt(key, 100);
		LogUtil.writeLog("properties:[" + key + "=" + maxSize + "]", ThreadUtils.class);

		key = "thread.pool.keep_alive_time";
		int keepAliveTime = PropertyUtils.getInstance().getInt(key, 60);
		LogUtil.writeLog("properties:[" + key + "=" + keepAliveTime + "]", ThreadUtils.class);

		createThreadPool(minSize, maxSize, keepAliveTime);
	}

	public static void createThreadPool(int minimumPoolSize, int maximumPoolSize, long keepAliveTime) {
		createThreadPool(new ThreadPool<Object>(minimumPoolSize, maximumPoolSize, keepAliveTime));
	}

	/**
	 * 쓰레드 풀 생성
	 * 
	 * @param threadPool 쓰레드 풀
	 */
	public static void createThreadPool(ThreadPool<Object> threadPool) {
		ThreadUtils.threadPool = threadPool;
	}

	public static List<Future<Object>> executeThread(List<Future<Object>> futureList,
			CallbackFunction<Object[], Object> callback, String title, Object... params) {
		return executeThread(futureList, callback, title, null, params);
	}

	/**
	 * 쓰레드 실행
	 * 
	 * @param futureList 응답결과 리스트
	 * @param callback 펑션 콜백
	 * @param title 실행명
	 * @param log 로그
	 * @param params 파라미터
	 * @return 응답결과 리스트
	 */
	public static List<Future<Object>> executeThread(List<Future<Object>> futureList,
			CallbackFunction<Object[], Object> callback, String title, Log log, Object... params) {
		Callable<Object> callable = new ThreadCallback<Object[], Object>(callback, title, log, params);

		return executeThread(futureList, callable);
	}

	/**
	 * 쓰레드 실행
	 * 
	 * @param futureList 응답결과 리스트
	 * @param callable 쓰레드
	 * @return 응답결과 리스트
	 */
	public static List<Future<Object>> executeThread(List<Future<Object>> futureList, Callable<Object> callable) {
		if (threadPool == null)
			createThreadPool();

		Future<Object> future = threadPool.submit(callable);

		futureList.add(future);

		return futureList;
	}

	/**
	 * 쓰레드 결과
	 * 
	 * @param futureList 응답결과 리스트
	 * @return 쓰레드 결과
	 */
	public static Map<String, Object> resultThread(List<Future<Object>> futureList) throws Exception {
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		for (Future<Object> future : futureList) {
			Map<String, Object> map = (Map<String, Object>) future.get();

			result.putAll(map);
		}

		return result;
	}

	public static void resultThreadVoid(List<Future<Void>> futureList) throws Exception {
		for (Future<Void> future : futureList) {
			future.get();
		}
	}

	public static void shutdownThreadPool() {
		shutdownThreadPool(null);
	}

	/**
	 * 쓰레드풀 해제
	 * 
	 * @param log
	 */
	public static void shutdownThreadPool(Log log) {
		if (threadPool == null)
			return;

		String message = "Shutdown the thread pool.";
		if (log == null)
			LogUtil.writeLog(message, ThreadUtils.class);
		else
			log.info(message);

		threadPool.shutdown();
	}
}
