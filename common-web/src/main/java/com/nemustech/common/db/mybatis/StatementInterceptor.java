package com.nemustech.common.db.mybatis;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Intercepts({ @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
		@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }) })
public class StatementInterceptor implements Interceptor {
	private Logger log = LogManager.getLogger(getClass());

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler handler = (StatementHandler) invocation.getTarget();
		Object param = handler.getParameterHandler().getParameterObject();

		log.debug("==>  Preparing: " + handler.getBoundSql().getSql());
		log.debug("==>  Parameter: " + param != null ? param.toString() : "");
//		log.debug(invocation.getMethod());

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
