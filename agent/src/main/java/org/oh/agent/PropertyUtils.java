package org.oh.agent;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * 프로퍼티 유틸
 */
public class PropertyUtils {
	protected static PropertyUtils instance = null;

	protected Configuration configuration = null;

	protected static PropertyUtils getInstance() {
		if (instance == null)
			instance = new PropertyUtils();
		return instance;
	}

	protected PropertyUtils() {
		PropertiesConfiguration pc = new PropertiesConfiguration();
		try {
			pc.setEncoding("UTF-8");
			pc.load("conf/agent.properties");
			pc.setReloadingStrategy(new FileChangedReloadingStrategy());
			configuration = pc;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Configuration getConfiguration() {
		return configuration;
	}

	public static String getString(String key) {
		return getInstance().getConfiguration().getString(key);
	}

	public static String getString(String key, String defaultValue) {
		return getInstance().getConfiguration().getString(key, defaultValue);
	}

	public static String[] getStringArray(String key) {
		return getInstance().getConfiguration().getStringArray(key);
	}

	public static int getInt(String key) {
		return getInstance().getConfiguration().getInt(key);
	}

	public static int getInt(String key, int defaultValue) {
		return getInstance().getConfiguration().getInt(key, defaultValue);
	}

	public static long getLong(String key) {
		return getInstance().getConfiguration().getLong(key);
	}

	public static long getLong(String key, long defaultValue) {
		return getInstance().getConfiguration().getLong(key, defaultValue);
	}

	public static double getDouble(String key) {
		return getInstance().getConfiguration().getDouble(key);
	}

	public static double getDouble(String key, double defaultValue) {
		return getInstance().getConfiguration().getDouble(key, defaultValue);
	}

	public static boolean getBoolean(String key) {
		return getInstance().getConfiguration().getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return getInstance().getConfiguration().getBoolean(key, defaultValue);
	}
}