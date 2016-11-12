package com.nemustech.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

public class ParserUtil {
	public static String parse(String string, Map<String, String> variables, String prefix, String postfix) {
		VariableTokenHandler handler = new VariableTokenHandler(variables);
		GenericTokenParser parser = new GenericTokenParser(prefix, postfix, handler);
		return parser.parse(string);
	}

	protected static class VariableTokenHandler implements TokenHandler {
		protected Map<String, String> variables;

		public VariableTokenHandler(Map<String, String> variables) {
			this.variables = variables;
		}

		public String handleToken(String content) {
			if (variables != null && variables.containsKey(content)) {
				return variables.get(content);
			}
			return content;
		}
	}

	public static void main(String[] args) {
		System.out.println(parse("a ${a} b ${a} c", new HashMap<String, String>() {
			{
				put("a", "aa");
			}
		}, "${", "}"));
	}
}
