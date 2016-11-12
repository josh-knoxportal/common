package com.nemustech.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import com.nemustech.common.Constants;

/**
 * 프로퍼티 유틸
 */
public class PropertyUtils {
	public static final String CONFIG_FILEPATH = Constants.HOME_DIR + File.separator + Constants.CONF_DIR_NAME
			+ File.separator + "common.properties";

	protected static PropertyUtils propertyUtils = null;

	protected Configuration configuration = null;

	public static PropertyUtils getInstance() {
		if (propertyUtils == null) {
			System.out.println("Create Property Utils");
			propertyUtils = new PropertyUtils();
		}

		return propertyUtils;
	}

	protected static InputStream getInputStream() {
		String fileName = FileUtil.getName(CONFIG_FILEPATH);
		InputStream is = PropertyUtils.class.getClassLoader().getResourceAsStream(fileName);

		if (is == null) {
			System.out.println("Not exists the \"classpath: " + fileName + "\" file.");
			return null;
		} else {
			System.out.println("Load the \"classpath: " + fileName + "\" file.");
			return is;
		}
	}

	public PropertyUtils() {
		this(getInputStream());
	}

	public PropertyUtils(String filePath) throws FileNotFoundException {
		this(new FileInputStream(filePath));
	}

	public PropertyUtils(InputStream is) {
		if (is == null) {
			return;
		}

		try {
			PropertiesConfiguration pc = new PropertiesConfiguration();
			pc.setEncoding("UTF-8");
			pc.setReloadingStrategy(new FileChangedReloadingStrategy());
			pc.load(is);

			configuration = pc;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public String getString(String key) {
		return configuration.getString(key);
	}

	public String getString(String key, String defaultValue) {
		return configuration.getString(key, defaultValue);
	}

	public String[] getStringArray(String key) {
		return configuration.getStringArray(key);
	}

	public int getInt(String key) {
		return configuration.getInt(key);
	}

	public int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	public long getLong(String key) {
		return configuration.getLong(key);
	}

	public long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public double getDouble(String key) {
		return configuration.getDouble(key);
	}

	public double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getInstance().getString("http.connection.timeout"));
//		while (true) {
//			System.out.println(getString("com.nemustech.common.postfix"));
//			Thread.sleep(2000);
//		}
	}
}