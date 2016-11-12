package com.nemustech.common.config;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class Configuration extends PropertiesConfiguration implements InitializingBean {
	protected static Log log = LogFactory.getLog(Configuration.class);

	/**
	 * The properties.
	 * 
	 * @uml.property name="properties"
	 */
	private Properties properties;

	/**
	 * Instantiates a new common configuration.
	 */
	public Configuration() {
	}

	/**
	 * @return the properties
	 * @uml.property name="properties"
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 * @uml.property name="properties"
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
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
