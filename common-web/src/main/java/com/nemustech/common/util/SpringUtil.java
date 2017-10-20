package com.nemustech.common.util;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.nemustech.web.util.WebApplicationContextUtil;

public abstract class SpringUtil {
	protected static WebApplicationContext webApplicationContext = WebApplicationContextUtil.getApplicationContext();

	protected static ExpressionParser expressionParser = new SpelExpressionParser();
	protected static StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
	protected static StandardBeanExpressionResolver beanExpressionResolver = new StandardBeanExpressionResolver();

	static {
		standardEvaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
		standardEvaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
		standardEvaluationContext.addPropertyAccessor(new MapAccessor());
		standardEvaluationContext.addPropertyAccessor(new EnvironmentAccessor());
	}

	///////////////////////////////////////////////////////////////////////////

	public static String getPlaceholderResult(String value) {
		if (WebApplicationContextUtil.isConfigurableWebApplicationContext(webApplicationContext)) {
			return getPlaceholderResult(value,
					((ConfigurableWebApplicationContext) webApplicationContext).getBeanFactory());
		} else {
			return value;
		}
	}

	/**
	 * Placeholder 결과를 구한다.
	 * 
	 * @param value
	 * @param beanFactory
	 * @return
	 */
	public static String getPlaceholderResult(String value, ConfigurableBeanFactory beanFactory) {
		Assert.notNull(beanFactory, "BeanFactory must not be null");

		return beanFactory.resolveEmbeddedValue(value);
	}

	///////////////////////////////////////////////////////////////////////////

	public static Object getSPELResolverResult(String exp) {
		if (WebApplicationContextUtil.isConfigurableWebApplicationContext(webApplicationContext)) {
			return getSPELResolverResult(exp,
					((ConfigurableWebApplicationContext) webApplicationContext).getBeanFactory());
		} else {
			return getSPELResult(exp);
		}
	}

	public static Object getSPELResult(String exp) {
		return getSPELResult(exp, Object.class);
	}

	public static <T> T getSPELResult(String exp, Class<T> desiredResultType) {
		if (WebApplicationContextUtil.isConfigurableWebApplicationContext(webApplicationContext)) {
			return getSPELResult(exp, ((ConfigurableWebApplicationContext) webApplicationContext).getBeanFactory(),
					desiredResultType);
		} else {
			return expressionParser.parseExpression(exp, ParserContext.TEMPLATE_EXPRESSION).getValue(desiredResultType);
		}
	}

	public static Object getSPELResult(String exp, ConfigurableBeanFactory beanFactory) {
		return getSPELResult(exp, beanFactory, Object.class);
	}

	/**
	 * SPEL 결과를 구한다.
	 * 
	 * @param exp id
	 * @param beanFactory
	 * @param desiredResultType
	 * @return
	 */
	public static <T> T getSPELResult(String exp, ConfigurableBeanFactory beanFactory, Class<T> desiredResultType) {
		standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		BeanExpressionContext rootObject = new BeanExpressionContext(beanFactory, null);

		return expressionParser.parseExpression(exp, ParserContext.TEMPLATE_EXPRESSION)
				.getValue(standardEvaluationContext, rootObject, desiredResultType);
	}

	/**
	 * StandardBeanExpressionResolver 사용
	 * 
	 * @param exp
	 * @param beanFactory
	 * @return
	 */
	public static Object getSPELResolverResult(String exp, ConfigurableBeanFactory beanFactory) {
		BeanExpressionContext rootObject = new BeanExpressionContext(beanFactory, null);

		return beanExpressionResolver.evaluate(exp, rootObject);
	}

	///////////////////////////////////////////////////////////////////////////

	public static Object getPlaceholderSPELResult(String exp) {
		return getPlaceholderSPELResult(exp, Object.class);
	}

	public static <T> T getPlaceholderSPELResult(String value, Class<T> desiredResultType) {
		if (WebApplicationContextUtil.isConfigurableWebApplicationContext(webApplicationContext)) {
			return getPlaceholderSPELResult(value,
					((ConfigurableWebApplicationContext) webApplicationContext).getBeanFactory(), desiredResultType);
		} else {
			return null;
		}
	}

	public static Object getPlaceholderSPELResult(String value, ConfigurableBeanFactory beanFactory) {
		return getPlaceholderSPELResult(value, beanFactory, Object.class);
	}

	/**
	 * Placeholder 와 SPEL 결과를 구한다.
	 * 
	 * @param result
	 * @param beanFactory
	 * @return
	 */
	public static <T> T getPlaceholderSPELResult(String value, ConfigurableBeanFactory beanFactory,
			Class<T> desiredResultType) {
		value = getPlaceholderResult(value, beanFactory);

		return getSPELResult(value, beanFactory, desiredResultType);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getSPELResult(
				"#{T(com.nemustech.common.util.Utils).getDate()} #{T(com.nemustech.common.util.Utils).getDate()}"));
		System.out.println(getSPELResolverResult("#{T(com.nemustech.common.util.Utils).getDate()}"));
	}
}
