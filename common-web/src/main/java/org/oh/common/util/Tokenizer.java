package org.oh.common.util;

import java.util.LinkedList;

/**
 * 토큰분리자 클래스.
 */
public class Tokenizer {
	/** 토큰 목록. */
	private LinkedList<Token> tokenList = new LinkedList<Token>();

	/** 토큰 목록 현재인덱스. */
	private int currentIndex = 0;

	/**
	 * 생성자.
	 *
	 * @param source 토큰화(tokenization)시킬 문자열.
	 * @param beginDelim 토큰분리 시작구분자
	 * @param endDelim 토큰분리 종료구분자
	 */
	public Tokenizer(String source, String beginDelim, String endDelim) {
		int beginDelimLength = beginDelim.length();
		int endDelimLength = endDelim.length();

		String temp = "";
		for (int beginIndex = 0, endIndex = 0; true;) {
			beginIndex = source.indexOf(beginDelim, endIndex);
			if (beginIndex == -1) {
				temp = source.substring(endIndex, source.length());
				this.tokenList.add(new Token(false, source.substring(endIndex, source.length())));
				break;
			}

			temp = source.substring(endIndex, beginIndex);
			this.tokenList.add(new Token(false, temp));

			beginIndex += beginDelimLength;
			endIndex = source.indexOf(endDelim, beginIndex);
			if (endIndex == -1) {
				break;
			}

			temp = source.substring(beginIndex, endIndex);
			endIndex += endDelimLength;
			this.tokenList.add(new Token(true, temp));
		}

		rewind();
	}

	/**
	 * 다음 토큰 존재여부를 알아내는 메소드.
	 *
	 * @return 다음 토큰이 존재하면 true, 아니면 false.
	 */
	public final boolean hasMoreTokens() {
		return (this.currentIndex < this.tokenList.size()) ? true : false;
	}

	/**
	 * 다음 토큰 객체를 얻어내는 메소드.
	 *
	 * @return 다음 토큰객체.
	 */
	public final Token nextToken() {
		return (Token) this.tokenList.get(this.currentIndex++);
	}

	/**
	 * 토큰 목록 현재인덱스를 초기화시켜주는 메소드.
	 */
	protected final void rewind() {
		this.currentIndex = 0;
	}

	/**
	 * 토큰 POJO 클래스.
	 */
	public class Token {
		/** 토큰여부 */
		private boolean isToken;

		/** 토큰값 */
		private String value;

		/**
		 * 생성자
		 *
		 * @param isToken 토큰여부
		 * @param value 토큰값
		 */
		public Token(boolean isToken, String value) {
			this.isToken = isToken;
			this.value = value;
		}

		/**
		 * 토큰 여부를 알아내는 메소드
		 *
		 * @return 토큰이면 true, 아니면 false.
		 */
		public final boolean isToken() {
			return this.isToken;
		}

		/**
		 * 토큰값을 얻는 메소드
		 *
		 * @return 토큰값
		 */
		public final String getValue() {
			return this.value;
		}
	}
}
