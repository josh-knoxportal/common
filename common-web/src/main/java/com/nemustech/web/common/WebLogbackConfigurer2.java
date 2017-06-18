package com.nemustech.web.common;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ServletContextPropertyUtils;
import org.springframework.web.util.WebUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.ext.spring.LogbackConfigurer;
import ch.qos.logback.ext.spring.web.WebLogbackConfigurer;

/**
 * tomcat zombie 예방
 * 
 * @author skoh
 * @see ch.qos.logback.ext.spring.web.WebLogbackConfigurer
 */
public class WebLogbackConfigurer2 {
	public static void initLogging(ServletContext servletContext) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.reset();

		String locationParam = servletContext.getInitParameter(WebLogbackConfigurer.CONFIG_LOCATION_PARAM);
		System.out.println("Initializing Logback from [" + locationParam + "]");

		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(loggerContext);
		try {
			configurator.doConfigure(locationParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Expose the web app root system property.
//		if (exposeWebAppRoot(servletContext)) {
//			WebUtils.setWebAppRootSystemProperty(servletContext);
//		}
//
//		// Only perform custom Logback initialization in case of a config file.
//		String locationParam = servletContext.getInitParameter(WebLogbackConfigurer.CONFIG_LOCATION_PARAM);
//		if (locationParam != null) {
//			// Perform Logback initialization; else rely on Logback's default initialization.
//			for (String location : StringUtils.tokenizeToStringArray(locationParam,
//					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS)) {
//				try {
//					// Resolve context property placeholders before potentially resolving real path.
//					location = ServletContextPropertyUtils.resolvePlaceholders(location, servletContext);
//					// Return a URL (e.g. "classpath:" or "file:") as-is;
//					// consider a plain file path as relative to the web application root directory.
//					if (!ResourceUtils.isUrl(location)) {
//						location = WebUtils.getRealPath(servletContext, location);
//					}
//
//					// Write log message to server log.
//					System.out.println("Initializing Logback from [" + location + "]");
//
//					// Initialize
//					LogbackConfigurer.initLogging(location);
//					break;
//				} catch (FileNotFoundException ex) {
//					System.out.println("No logback configuration file found at [" + location + "]");
//					ex.printStackTrace();
//					// throw new IllegalArgumentException("Invalid 'logbackConfigLocation' parameter: " + ex.getMessage());
//				} catch (JoranException e) {
//					e.printStackTrace();
//					throw new RuntimeException("Unexpected error while configuring logback", e);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		// If SLF4J's java.util.logging bridge is available in the classpath, install it. This will direct any messages
//		// from the Java Logging framework into SLF4J. When logging is terminated, the bridge will need to be uninstalled
//		try {
//			Class<?> julBridge = ClassUtils.forName("org.slf4j.bridge.SLF4JBridgeHandler",
//					ClassUtils.getDefaultClassLoader());
//
//			Method removeHandlers = ReflectionUtils.findMethod(julBridge, "removeHandlersForRootLogger");
//			if (removeHandlers != null) {
//				System.out.println("Removing all previous handlers for JUL to SLF4J bridge");
//				ReflectionUtils.invokeMethod(removeHandlers, null);
//			}
//
//			Method install = ReflectionUtils.findMethod(julBridge, "install");
//			if (install != null) {
//				System.out.println("Installing JUL to SLF4J bridge");
//				ReflectionUtils.invokeMethod(install, null);
//			}
//		} catch (ClassNotFoundException ignored) {
//			// Indicates the java.util.logging bridge is not in the classpath. This is not an indication of a problem.
//			System.out.println("JUL to SLF4J bridge is not available on the classpath");
//			ignored.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static void shutdownLogging(ServletContext servletContext) {
		// Uninstall the SLF4J java.util.logging bridge *before* shutting down the Logback framework.
//		try {
//			Class<?> julBridge = ClassUtils.forName("org.slf4j.bridge.SLF4JBridgeHandler",
//					ClassUtils.getDefaultClassLoader());
//			Method uninstall = ReflectionUtils.findMethod(julBridge, "uninstall");
//			if (uninstall != null) {
//				System.out.println("Uninstalling JUL to SLF4J bridge");
//				ReflectionUtils.invokeMethod(uninstall, null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			System.out.println("Shutting down Logback");
//			LogbackConfigurer.shutdownLogging();
//		} finally {
//			// Remove the web app root system property.
//			if (exposeWebAppRoot(servletContext)) {
//				WebUtils.removeWebAppRootSystemProperty(servletContext);
//			}
//		}
	}

	@SuppressWarnings({ "BooleanMethodNameMustStartWithQuestion" })
	private static boolean exposeWebAppRoot(ServletContext servletContext) {
		String exposeWebAppRootParam = servletContext.getInitParameter(WebLogbackConfigurer.EXPOSE_WEB_APP_ROOT_PARAM);
		return (exposeWebAppRootParam == null || Boolean.valueOf(exposeWebAppRootParam));
	}
}
