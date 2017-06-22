package com.nemustech.platform.lbs.common.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.ExtendedProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class PropertyService implements ApplicationContextAware, InitializingBean, DisposableBean, ResourceLoaderAware {

	private ExtendedProperties extProperties = null;
	private ResourceLoader resourceLoader = null;

	@SuppressWarnings("unused")
	private MessageSource messageSource; 
	private Set<String> extFileName;

	@SuppressWarnings("rawtypes")
	private Map properties;

	public boolean getBoolean(String name) {
		return getConfiguration().getBoolean(name);
	}

	public boolean getBoolean(String name, boolean def) {
		return getConfiguration().getBoolean(name, def);
	}

	public double getDouble(String name) {
		return getConfiguration().getDouble(name);
	}

	public double getDouble(String name, double def) {
		return getConfiguration().getDouble(name, def);
	}

	public float getFloat(String name) {
		return getConfiguration().getFloat(name);
	}

	public float getFloat(String name, float def) {
		return getConfiguration().getFloat(name, def);
	}

	public int getInt(String name) {
		return getConfiguration().getInt(name);
	}

	public int getInt(String name, int def) {
		return getConfiguration().getInt(name, def);
	}

	@SuppressWarnings("rawtypes")
	public Iterator getKeys() {
		return getConfiguration().getKeys();
	}

	@SuppressWarnings("rawtypes")
	public Iterator getKeys(String prefix) {
		return getConfiguration().getKeys(prefix);
	}

	public long getLong(String name) {
		return getConfiguration().getLong(name);
	}

	public long getLong(String name, long def) {
		return getConfiguration().getLong(name, def);
	}

	public String getString(String name) {
		return getConfiguration().getString(name);
	}

	public String getString(String name, String def) {
		return getConfiguration().getString(name, def);
	}

	public String[] getStringArray(String name) {
		return getConfiguration().getStringArray(name);
	}

	@SuppressWarnings("rawtypes")
	public Vector getVector(String name) {
		return getConfiguration().getVector(name);
	}

	@SuppressWarnings("rawtypes")
	public Vector getVector(String name, Vector def)
	{
		return getConfiguration().getVector(name, def);
	}

	@SuppressWarnings("rawtypes")
	public Collection getAllKeyValue() {
		return getConfiguration().values();
	}

	private ExtendedProperties getConfiguration() {
		return this.extProperties;
	}

	@SuppressWarnings("rawtypes")
	public void refreshPropertyFiles() throws Exception {
		String fileName = null;
		try {
			Iterator it = this.extFileName.iterator();
			do {
				if (it == null) break; 
				Object element = it.next();
				String enc = null;

				if ((element instanceof Map)) {
					Map ele = (Map)element;
					enc = (String)ele.get("encoding");
					fileName = (String)ele.get("filename");
				} else {
					fileName = (String)element;
				}
				loadPropertyResources(fileName, enc);

			} while (it.hasNext());
		} catch (Exception e) {
			throw new Exception("error.properties.refresh.files; " + fileName, e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void afterPropertiesSet() throws Exception {
		try {
			this.extProperties = new ExtendedProperties();

			if (this.extFileName != null) {
				refreshPropertyFiles();
			}

			Iterator it = this.properties.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();

				if ((key == null) || (key.equals(""))) {
					throw new Exception("Key is null.");
				}

				this.extProperties.put(key, value);
			}
		} catch (Exception e) {
		}
	}

	public void setExtFileName(Set<String> extFileName) {
		this.extFileName = extFileName;
	}

	@SuppressWarnings("rawtypes")
	public void setProperties(Map properties) {
		this.properties = properties;
	}

	public void destroy() {
		this.extProperties = null;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.messageSource = ((MessageSource)applicationContext.getBean("messageSource"));
	}

	private void loadPropertyResources(String location, String encoding) throws Exception {
		if ((this.resourceLoader instanceof ResourcePatternResolver)) {
			try {
				Resource[] resources = ((ResourcePatternResolver)this.resourceLoader)
						.getResources(location);

				loadPropertyLoop(resources, encoding);
			} catch (IOException ex) {
				throw new BeanDefinitionStoreException(
						"Could not resolve Properties resource pattern [" + 
								location + "]", ex);
			}
		} else {
			Resource resource = this.resourceLoader.getResource(location);
			loadPropertyRes(resource, encoding);
		}
	}

	private void loadPropertyLoop(Resource[] resources, String encoding) throws Exception {
		Assert.notNull(resources, "Resource array must not be null");
		for (int i = 0; i < resources.length; i++)
			loadPropertyRes(resources[i], encoding);
	}

	private void loadPropertyRes(Resource resource, String encoding) throws Exception {
		ExtendedProperties egovProperty = new ExtendedProperties();
		egovProperty.load(resource.getInputStream(), encoding);
		this.extProperties.combine(egovProperty);
	}

}
