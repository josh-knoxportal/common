package com.nemustech.platform.lbs.common.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenHandlerManager implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenHandlerManager.class);

	@Autowired
	ApplicationContext applicationContext;

	private Map<String, AccessTokenHandler> handlerMap = new HashMap<String, AccessTokenHandler>();

	@Override
	public void afterPropertiesSet() throws Exception {

		Map<String, AccessTokenHandler> beansMap = applicationContext.getBeansOfType(AccessTokenHandler.class);
		for (AccessTokenHandler handler : beansMap.values()) {
			handlerMap.put(handler.getRequestType(), handler);
			logger.info("register AccessToken handler req_type :" + handler.getRequestType());
		}
	}

	/**
	 * Handler 가져오기
	 * 
	 * @param requestType
	 * @return
	 */
	public AccessTokenHandler get(String requestType) {
		if (requestType == null)
			requestType = "2";
		return handlerMap.get(requestType);
	}

}
