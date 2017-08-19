package com.nemustech.common.util;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public abstract class SpringUtil {
	public static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
	public static final ParserContext PARSER_CONTEXT = new TemplateParserContext();

	/**
	 * SpringEL
	 * 
	 * @param exp
	 * @return
	 */
	public static Object getEvaluationResult(String exp) {
		return EXPRESSION_PARSER.parseExpression(exp, PARSER_CONTEXT).getValue();
	}

	protected static class TemplateParserContext implements ParserContext {
		public String getExpressionPrefix() {
			return "#{";
		}

		public String getExpressionSuffix() {
			return "}";
		}

		public boolean isTemplate() {
			return true;
		}
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(getEvaluationResult("#{DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')}"));
		System.out.println(getEvaluationResult("#{T(com.nemustech.common.util.Utils).getDate()}"));
	}
}
