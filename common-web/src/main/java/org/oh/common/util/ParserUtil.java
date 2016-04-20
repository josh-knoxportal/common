package org.oh.common.util;

import java.util.Map;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

public class ParserUtil {
	public static String parse(String string, Map<String, String> variables) {
		VariableTokenHandler handler = new VariableTokenHandler(variables);
		GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
		return parser.parse(string);
	}

	protected static class VariableTokenHandler implements TokenHandler {
		private Map<String, String> variables;

		public VariableTokenHandler(Map<String, String> variables) {
			this.variables = variables;
		}

		public String handleToken(String content) {
			if (variables != null && variables.containsKey(content)) {
				return variables.get(content);
			}
			return "${" + content + "}";
		}
	}
}
