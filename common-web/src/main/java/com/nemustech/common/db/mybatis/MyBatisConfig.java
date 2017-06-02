package com.nemustech.common.db.mybatis;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.nemustech.common.Constants;
import com.nemustech.common.exception.CommonException;
import com.nemustech.common.util.LogUtil;

/**
 * <code>{HOME}/SqlMapConfig.xml</code> 파일을 읽어서 mybatis를 사용할 수 있도록 구성한다.
 * 설정 파일의 구성 등은 <a href="http://www.mybatis.org/">mybatis</a>를 참고한다.
 * 
 * @version 1.0.0
 */
public class MyBatisConfig {
	protected static final Charset CHARSET = Charset.forName("UTF-8");

	private static final Log log = LogFactory.getLog(MyBatisConfig.class);

	private static HashMap<String, SqlSessionFactory> factoryMap = new HashMap<String, SqlSessionFactory>(2);
	private static final String configFile = Constants.HOME_DIR + File.separator + Constants.CONF_DIR_NAME
			+ File.separator + "SqlMapConfig.xml";

	static {
		try {
			Resources.setCharset(CHARSET);
			String resource = null;
			Reader reader = null;
			SqlSessionFactory sqlMap = null;

			try {
				resource = configFile;
//				reader = new InputStreamReader(new FileInputStream(configFile), CHARSET);
//				reader = new InputStreamReader(
//						MyBatisConfig.class.getClassLoader().getResourceAsStream(FileUtil.getName(configFile)),
//						CHARSET);
				reader = new InputStreamReader(Resources.getResourceAsStream(FilenameUtils.getName(configFile)));
			} catch (Exception e) {
				resource = "./SqlMapConfig.xml";
				log.error("Failed to load " + configFile);
				log.error("Instead, try to load " + resource);

				try {
					reader = Resources.getResourceAsReader(resource);
				} catch (IOException ioe) {
					log.fatal("Failed to load " + resource);
					throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(
							"Error initializing mybatis. Cause: failed to load config file", ioe.getMessage()), ioe);
				}
			}

			log.info("Success to load " + resource);
			sqlMap = new SqlSessionFactoryBuilder().build(reader);
			reader.close();

			factoryMap.put(sqlMap.getConfiguration().getEnvironment().getId(), sqlMap);
			factoryMap.put("default", sqlMap);
		} catch (Exception e) {
			log.error("Error initializing mybatis. Cause: ", e);
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Error initializing mybatis. Cause:", e.getMessage()), e);
		}
	}

	/**
	 * default로 지정한 environment 정보를 바탕으로 한 {@link org.apache.ibatis.session.SqlSessionFactory}을 돌려 준다.
	 * 
	 * @return <code>SqlSessionFactory</code>
	 */
	public static SqlSessionFactory getSqlSessionFactory() {
		return factoryMap.get("default");
	}

	/**
	 * 지정한 environment 정보를 바탕으로 한 {@link org.apache.ibatis.session.SqlSessionFactory}을 돌려 준다.
	 * 
	 * @param envName environment id
	 * @return <code>SqlSessionFactory</code>
	 */
	public static SqlSessionFactory getSqlSessionFactory(String envName) {
		log.info("Start::getSqlSessionFactory()");
		log.debug("  > evnName: " + envName);

		if (factoryMap.containsKey(envName)) {
			return factoryMap.get(envName);
		}

		else {
			try {
				Resources.setCharset(CHARSET);
				String resource = null;
				Reader reader = null;
				SqlSessionFactory sqlMap = null;

				try {
					resource = configFile;
//					reader = new InputStreamReader(new FileInputStream(configFile), CHARSET);
//					reader = new InputStreamReader(
//							MyBatisConfig.class.getClassLoader().getResourceAsStream(FileUtil.getName(configFile)),
//							CHARSET);
					reader = new InputStreamReader(Resources.getResourceAsStream(FilenameUtils.getName(configFile)));
				} catch (Exception e) {
					resource = "./SqlMapConfig.xml";
					log.error("Failed to load " + configFile);
					log.error("Instead, try to load " + resource);

					try {
						reader = Resources.getResourceAsReader(resource);
					} catch (IOException ioe) {
						log.fatal("Failed to load " + resource);
						throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(
								"Failed to get mybatis session! Cause: failed to load config file", e.getMessage()), e);
					}
				}

				log.info("Success to load " + resource);
				sqlMap = new SqlSessionFactoryBuilder().build(reader, envName);
				reader.close();
				factoryMap.put(envName, sqlMap);

				return sqlMap;
			} catch (Exception e) {
				log.fatal("Failed to get mybatis session! Cause: ", e);
				throw new CommonException(CommonException.ERROR,
						LogUtil.buildMessage("Failed to get mybatis session! Cause:", e.getMessage()), e);
			} finally {
				log.info("End::getSqlSessionFactory()");
			}
		}
	}
}
