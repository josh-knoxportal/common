package com.nemustech.common.util;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class SpringUtil {
	public static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
	public static final ParserContext PARSER_CONTEXT = new TemplateParserContext();
	public static final StandardEvaluationContext STANDARD_EVALUATION_CONTEXT = new StandardEvaluationContext();

	static {
		STANDARD_EVALUATION_CONTEXT.addPropertyAccessor(new BeanExpressionContextAccessor());
		STANDARD_EVALUATION_CONTEXT.addPropertyAccessor(new BeanFactoryAccessor());
		STANDARD_EVALUATION_CONTEXT.addPropertyAccessor(new MapAccessor());
		STANDARD_EVALUATION_CONTEXT.addPropertyAccessor(new EnvironmentAccessor());
	}

	public static Object getEvaluationResult(String exp) {
		return getEvaluationResult(exp, Object.class);
	}

	/**
	 * SPEL
	 * 
	 * @param exp
	 * @param desiredResultType
	 * @return
	 */
	public static <T> T getEvaluationResult(String exp, Class<T> desiredResultType) {
		return EXPRESSION_PARSER.parseExpression(exp, PARSER_CONTEXT).getValue(desiredResultType);
	}

	public static Object getEvaluationResult(String exp, ConfigurableBeanFactory beanFactory) {
		return getEvaluationResult(exp, beanFactory, Object.class);
	}

	/**
	 * SPEL (Bean references)
	 * 
	 * @param exp id
	 * @param beanFactory
	 * @param desiredResultType
	 * @return
	 */
	public static <T> T getEvaluationResult(String exp, ConfigurableBeanFactory beanFactory,
			Class<T> desiredResultType) {
		STANDARD_EVALUATION_CONTEXT.setBeanResolver(new BeanFactoryResolver(beanFactory));
		BeanExpressionContext rootObject = new BeanExpressionContext(beanFactory, null);

		return EXPRESSION_PARSER.parseExpression(exp, PARSER_CONTEXT).getValue(STANDARD_EVALUATION_CONTEXT, rootObject,
				desiredResultType);
	}

	protected static class TemplateParserContext implements ParserContext {
		@Override
		public boolean isTemplate() {
			return true;
		}

		@Override
		public String getExpressionPrefix() {
			return "#{";
		}

		@Override
		public String getExpressionSuffix() {
			return "}";
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getEvaluationResult("DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')"));
		System.out.println(getEvaluationResult("#{T(com.nemustech.common.util.Utils).getDate()}"));
	}
}
