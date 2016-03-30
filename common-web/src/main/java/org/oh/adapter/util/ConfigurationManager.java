package org.oh.adapter.util;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.oh.common.util.LogUtil;

/**
 * 
 * bizMOP 및 해당 고객사에서 사용하기 위한 Properties를 관리하기 위한 클래스
 */
public class ConfigurationManager {
	static ConfigurationManager configManager;
	private static HashMap<String, PropertiesConfiguration> configurations = new HashMap<String, PropertiesConfiguration>();

	public ConfigurationManager(String propertiesPath) {
		init(propertiesPath);
	}

	public static HashMap<String, PropertiesConfiguration> getConfigurations() {
		return configurations;
	}

	public static void setConfigurations(HashMap<String, PropertiesConfiguration> configurations) {
		ConfigurationManager.configurations = configurations;
	}

	public static ConfigurationManager getDefaultConfManager(String propertiesPath) {
		if (configManager == null) {
			configManager = new ConfigurationManager(propertiesPath);
		}

		return configManager;
	}

	protected static void init(String home) {
		try {

			File SMART2_HOME = new File(home);
			File[] propertiesList = SMART2_HOME.listFiles();
			PropertiesConfiguration propConfig = new PropertiesConfiguration();
			for (File item : propertiesList) {
				if (item.isFile()) {
					String fileName = item.getName();
					String fileExt = FilenameUtils.getExtension(fileName);

					if (fileExt.equalsIgnoreCase("properties")) {
						fileName = FilenameUtils.getBaseName(fileName);
						propConfig = new PropertiesConfiguration();
						propConfig.load(item.getAbsolutePath());
						propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
						propConfig.setDelimiterParsingDisabled(true);

						configurations.put(fileName, propConfig);
					}
				}
			}

		} catch (ConfigurationException e) {
			LogUtil.writeLog(e, ConfigurationManager.class);
		}
	}

	public static PropertiesConfiguration getConfiguration(String confID) {
		if (configurations.containsKey(confID)) {
			return configurations.get(confID);
		} else {
			return null;
		}
	}

	public void addConfiguration(String configID, String configurationURL) {
		try {
			PropertiesConfiguration propConfig = new PropertiesConfiguration(configurationURL);
			propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
			configurations.put(configID, propConfig);
		} catch (ConfigurationException e) {
			LogUtil.writeLog(e, getClass());
		}
	}

	public static String getString(String configID, String key) {
		if (configurations.containsKey(configID)) {
			Configuration config = configurations.get(configID);
			if (config.containsKey(key)) {
				// String newValue = new String(config.getString(key).getBytes("ISO-8859-1"), "UTF-8");
				// return newValue;
				return config.getString(key);
			}
		}
		return null;
	}

	public String[] getConfigurationsKey() {
		Set<String> keySet = configurations.keySet();
		Object[] result = keySet.toArray();
		String[] response = new String[result.length];
		for (int i = 0; i < result.length; i++) {
			response[i] = (String) result[i];
		}

		return response;

	}
}
