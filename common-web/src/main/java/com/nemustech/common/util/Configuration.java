package com.nemustech.common.util;

import java.util.Enumeration;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuration extends PropertiesConfiguration {
	private static final Log log = LogFactory.getLog(Configuration.class);

	/**
	 * The properties.
	 */
	protected Properties properties;

	/**
	 * Instantiates a new common configuration.
	 */
	public Configuration() {
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@PostConstruct
	public void init_() throws Exception {
		StringBuffer elements = new StringBuffer();

		if (this.properties != null) {
			Enumeration<?> keys = this.properties.propertyNames();
			elements.append("\n ============= dispay config data =============");

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = this.properties.getProperty(key);
				elements.append("\n").append("[" + key + "]: " + value);
				super.setProperty(key, value);
			}
		} else {
			elements.append("\n ============= no config data =============");
		}

		log.info(elements.toString());

		// 기존 데이터 삭제
		this.properties = null;
		elements = null;
	}
}
