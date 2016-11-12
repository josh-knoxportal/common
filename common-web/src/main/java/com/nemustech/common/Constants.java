package com.nemustech.common;

import com.nemustech.common.util.Utils;

/**
 * 상수 정의
 */
public abstract class Constants {
	/**
	 * {@value}
	 */
	public static final String KEY_HOME_DIR = "HOME";

	/**
	 * <code>HOME</code>에 대한 값을 확인하고 없을 경우 현재 폴더(<code>"."</code>)를 사용한다.
	 */
	public static final String HOME_DIR = System.getProperty(KEY_HOME_DIR) != null ? System.getProperty(KEY_HOME_DIR)
			: ".";

	// 프로퍼티 키
	public static final String PROPERTY_DOWNLOAD_PATH = "com.nemustech.commnon.download_path";

	/**
	 * {@value}
	 */
	public static final String CONF_DIR_NAME = "conf";

	static {
		StringBuilder messages = new StringBuilder();
//		messages.append(" (").append(System.currentTimeMillis()).append(") ");
//		messages.append("[thread-").append(Thread.currentThread().getName()).append("-")
//				.append(Thread.currentThread().getId()).append("] ");
//		messages.append(Constants.class.getSimpleName()).append(" - ");

		if (HOME_DIR == null || ".".equalsIgnoreCase(HOME_DIR)) {
//			messages.insert(0, "[WARN]");
			messages.append("Cannot read HOME configuration.");
			System.out.println(Utils.toString(messages, null, 2));
		} else {
//			messages.insert(0, "[INFO] ");
			messages.append("HOME location: ").append(HOME_DIR);
			System.out.println(Utils.toString(messages, null, 2));
		}
	}
}