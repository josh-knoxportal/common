package org.oh.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Log 유틸
 */
public abstract class LogUtil {
	public static void writeLog(Object message) {
		writeLog2(message, null, null);
	}

	public static <T> void writeLog(Object message, Class<T> cls) {
		writeLog2(message, null, cls);
	}

	public static void writeLog(Throwable e) {
		writeLog2(null, e, null);
	}

	public static <T> void writeLog(Throwable e, Class<T> cls) {
		writeLog2(null, e, cls);
	}

	public static void writeLog(Object message, Throwable e) {
		writeLog2(message, e, null);
	}

	public static <T> void writeLog(Object message, Throwable e, Class<T> cls) {
		writeLog2(message, e, cls);
	}

	protected static <T> void writeLog2(Object message, Throwable e, Class<T> cls) {
		Log log = null;
		if (PropertyUtils.getInstance().getBoolean("log.file.enable", false) && cls != null) {
			log = LogFactory.getLog(cls);
		}

		if (log == null) {
			String str = Utils.toString(message, e, 4); // 2
			System.out.println(str);
		} else {
			String str = Utils.toString(null, message, null);
			if (e == null) {
				log.info(str);
			} else {
				log.error(str, e);
			}
		}
	}

	public static void main(String[] args) {
		writeLog("test");
		writeLog("test", LogUtil.class);
		writeLog(new Exception("error"));
		writeLog(new Exception("error"), LogUtil.class);
		writeLog("test", new Exception("error"));
		writeLog("test", new Exception("error"), LogUtil.class);
	}
}