package com.nemustech.common.config;

import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ConfigurationFactory implements FactoryBean<Object> {
	protected static Log log = LogFactory.getLog(ConfigurationFactory.class);

	/**
	 * The configuration.
	 * 
	 * @uml.property name="configuration"
	 */
	private CompositeConfiguration configuration;

	/**
	 * The configurations.
	 * 
	 * @uml.property name="configurations"
	 */
	private Configuration[] configurations;

	/**
	 * The locations.
	 * 
	 * @uml.property name="locations"
	 */
	private Resource[] locations;

	/**
	 * The throw exception on missing.
	 * 
	 * @uml.property name="throwExceptionOnMissing"
	 */
	private boolean throwExceptionOnMissing = true;

	public ConfigurationFactory() {
		log.info("ConfigurationFactory Construct");
	}

	public ConfigurationFactory(Configuration configuration) {
		log.info("ConfigurationFactory Construct With Configuration");
		Assert.notNull(configuration);
		this.configuration = new CompositeConfiguration(configuration);
	}

	/**
	 * Gets the object.
	 * 
	 * @return the object
	 * @throws Exception the exception
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return (configuration != null) ? ConfigurationConverter.getProperties(configuration) : null;
	}

	/**
	 * Gets the object type.
	 * 
	 * @return the object type
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType() {
		return java.util.Properties.class;
	}

	/**
	 * Gets the configurations.
	 * 
	 * @return Returns the configurations.
	 * @uml.property name="configurations"
	 */
	public Configuration[] getConfigurations() {
		return configurations;
	}

	/**
	 * Set the commons configurations objects which will be used as properties.
	 * 
	 * @param configurations the configurations
	 * @uml.property name="configurations"
	 */
	public void setConfigurations(Configuration[] configurations) {
		this.configurations = configurations;
	}

	/**
	 * Gets the locations.
	 * 
	 * @return the locations
	 * @uml.property name="locations"
	 */
	public Resource[] getLocations() {
		return locations;
	}

	/**
	 * Shortcut for loading configuration from Spring resources. It will internally create a PropertiesConfiguration object based on the URL retrieved from the given Resources.
	 * 
	 * @param locations the locations
	 * @uml.property name="locations"
	 */
	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	/**
	 * Checks if is throw exception on missing.
	 * 
	 * @return true, if is throw exception on missing
	 * @uml.property name="throwExceptionOnMissing"
	 */
	public boolean isThrowExceptionOnMissing() {
		return throwExceptionOnMissing;
	}

	/**
	 * Set the underlying Commons CompositeConfiguration throwExceptionOnMissing flag.
	 * 
	 * @param throwExceptionOnMissing the throw exception on missing
	 * @uml.property name="throwExceptionOnMissing"
	 */
	public void setThrowExceptionOnMissing(boolean throwExceptionOnMissing) {
		this.throwExceptionOnMissing = throwExceptionOnMissing;
	}

	/**
	 * Getter for the underlying CompositeConfiguration object.
	 * 
	 * @return the configuration
	 * @uml.property name="configuration"
	 */
	public CompositeConfiguration getConfiguration() {
		return configuration;
	}

	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * After properties set.
	 * 
	 * @throws Exception the exception
	 */
	@PostConstruct
	public void init() throws Exception {
		if (configuration == null && (configurations == null || configurations.length == 0)
				&& (locations == null || locations.length == 0))
			throw new IllegalArgumentException("no configuration object or location specified");

		if (configuration == null)
			configuration = new CompositeConfiguration();

		configuration.setThrowExceptionOnMissing(throwExceptionOnMissing);

		if (configurations != null) {
			for (int i = 0; i < configurations.length; i++) {
				configuration.addConfiguration(configurations[i]);
			}
		}

		if (locations != null) {
			for (int i = 0; i < locations.length; i++) {
				URL url = locations[i].getURL();
				Configuration props = new PropertiesConfiguration(url);
				configuration.addConfiguration(props);
			}
		}
	}
}
