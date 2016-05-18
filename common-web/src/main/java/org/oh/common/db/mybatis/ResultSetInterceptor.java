package org.oh.common.db.mybatis;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class ResultSetInterceptor implements Interceptor {
	private Logger log = LogManager.getLogger(getClass());

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
//		log.debug(invocation.getMethod());

		Object result = invocation.proceed();

		String title = "==>  Totalsize: ";
		if (result instanceof List) {
			log.debug(title + ((List) result).size());
		} else {
			log.debug(title + result);
		}

		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
