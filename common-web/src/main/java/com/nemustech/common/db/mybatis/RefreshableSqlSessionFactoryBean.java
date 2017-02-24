package com.nemustech.common.db.mybatis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * mybatis mapper 자동 감지 후 자동으로 서버 재시작이 필요 없이 반영
 *
 * <pre>
 * 참고) <a href="http://bryan7.tistory.com/117">http://bryan7.tistory.com/117</a>
 * </pre>
 */
public class RefreshableSqlSessionFactoryBean extends SqlSessionFactoryBean implements DisposableBean {
	protected static final Log log = LogFactory.getLog(RefreshableSqlSessionFactoryBean.class);

	protected SqlSessionFactory proxy;
	protected int interval = 3000; // 3초
	protected Timer timer;
	protected TimerTask task;
	protected Resource configLocation;
	protected Resource[] mapperLocations;
	protected Properties configurationProperties;

	/**
	 * 파일 감시 쓰레드가 실행중인지 여부.
	 */
	protected boolean running = false;
	protected final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	protected final Lock r = rwl.readLock();
	protected final Lock w = rwl.writeLock();

	@Override
	public SqlSessionFactory getObject() {
		return this.proxy;
	}

	@Override
	public Class<? extends SqlSessionFactory> getObjectType() {
		return (this.proxy != null ? this.proxy.getClass() : SqlSessionFactory.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * 싱글톤 멤버로 SqlSessionFactory 원본 대신 프록시로 설정하도록 오버라이드.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		setRefreshable();
	}

	@Override
	public void destroy() throws Exception {
		timer.cancel();
	}

	@Override
	public void setConfigLocation(Resource configLocation) {
		super.setConfigLocation(configLocation);

		this.configLocation = configLocation;
	}

	@Override
	public void setMapperLocations(Resource[] mapperLocations) {
		super.setMapperLocations(mapperLocations);

		this.mapperLocations = mapperLocations;
	}

	@Override
	public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
		super.setConfigurationProperties(sqlSessionFactoryProperties);

		this.configurationProperties = sqlSessionFactoryProperties;
	}

	public void setCheckInterval(int ms) {
		interval = ms;

		if (timer != null) {
			resetInterval();
		}
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void refresh() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("refreshing SqlSessionFactory.");
		}

		w.lock();

		try {
			super.afterPropertiesSet();

		} finally {
			w.unlock();
		}
	}

	protected void resetInterval() {
		if (running) {
			timer.cancel();
			running = false;
		}

		if (interval > 0) {
			timer.schedule(task, 0, interval);
			running = true;
		}
	}

	protected void setRefreshable() {
		proxy = (SqlSessionFactory) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(),
				new Class[] { SqlSessionFactory.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// log.debug("method.getName() : " + method.getName());
						return method.invoke(getParentObject(), args);
					}
				});

		task = new TimerTask() {
			protected Map<Resource, Long> map = new HashMap<Resource, Long>();

			public void run() {
				if (isModified()) {
					try {
						refresh();

					} catch (Exception e) {
						log.error("caught exception", e);
					}
				}
			}

			protected boolean isModified() {
				boolean retVal = false;

				if (mapperLocations != null) {
					for (int i = 0; i < mapperLocations.length; i++) {
						Resource mappingLocation = mapperLocations[i];
						retVal |= findModifiedResource(mappingLocation);
						if (retVal)
							break;
					}
				} else if (configLocation != null) {
					Configuration configuration = null;

					XMLConfigBuilder xmlConfigBuilder = null;
					try {
						xmlConfigBuilder = new XMLConfigBuilder(configLocation.getInputStream(), null,
								configurationProperties);
						configuration = xmlConfigBuilder.getConfiguration();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (xmlConfigBuilder != null) {
						try {
							xmlConfigBuilder.parse();

							// Configuration 클래스의 protected member field 인 loadedResources 를 얻기 위해 reflection 을 사용함.
							Field loadedResourcesField = Configuration.class.getDeclaredField("loadedResources");
							loadedResourcesField.setAccessible(true);

							@SuppressWarnings("unchecked")
							Set<String> loadedResources = (Set<String>) loadedResourcesField.get(configuration);

							for (Iterator< String>iterator = loadedResources.iterator(); iterator.hasNext();) {
								String resourceStr = (String) iterator.next();
								if (resourceStr.endsWith(".xml")) {
									Resource mappingLocation = new ClassPathResource(resourceStr);
									retVal |= findModifiedResource(mappingLocation);
									if (retVal) {
										break;
									}
								}
							}
						} catch (Exception ex) {
							throw new RuntimeException("Failed to parse config resource: " + configLocation, ex);
						} finally {
							ErrorContext.instance().reset();
						}
					}
				}

				return retVal;
			}

			protected boolean findModifiedResource(Resource resource) {
				boolean retVal = false;
				List<String> modifiedResources = new ArrayList<String>();

				try {
					long modified = resource.lastModified();

					if (map.containsKey(resource)) {
						long lastModified = ((Long) map.get(resource)).longValue();

						if (lastModified != modified) {
							map.put(resource, new Long(modified));

							modifiedResources.add(resource.getDescription());

							retVal = true;
						}
					} else {
						map.put(resource, new Long(modified));
					}
				} catch (IOException e) {
					log.error("caught exception", e);
				}

				if (retVal) {
					if (log.isInfoEnabled()) {
						log.info("modified files : " + modifiedResources);
					}
				}

				return retVal;
			}
		};

		timer = new Timer(true);
		resetInterval();
	}

	protected Object getParentObject() throws Exception {
		r.lock();

		try {
			return super.getObject();

		} finally {
			r.unlock();
		}
	}
}