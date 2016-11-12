package com.nemustech.common.util;

/**
 * 페이징 관련 유틸리티 클래스.
 */
public abstract class PageUtil {
	/**
	 * 전체 페이지수를 가져온다.
	 *
	 * <pre>
	 * 예)
	 * PageUtil.getTotalPage(100, 20); // &quot;5&quot; 리턴 (5페이지)
	 * PageUtil.getTotalPage(100, 10); // &quot;10&quot; 리턴 (10페이지)
	 * </pre>
	 *
	 * @param totalRow 전체 로우수
	 * @param rowsPerPage 페이지당 로우수
	 * @return 전체 페이지수
	 */
	public static int getTotalPage(int totalRow, int rowsPerPage) {
		return (totalRow % rowsPerPage == 0) ? totalRow / rowsPerPage : totalRow / rowsPerPage + 1;
	}

	/**
	 * 주어진 페이지 번호의 마지막 로우 인덱스를 가져온다.
	 *
	 * <pre>
	 * 예)
	 * PageUtil.getEndIndex(100, 20, 2); // &quot;80&quot; 리턴 (마지막 로우 80)
	 * PageUtil.getEndIndex(100, 10, 5); // &quot;60&quot; 리턴 (마지막 로우 60)
	 * </pre>
	 *
	 * @param totalRow 전체 로우수
	 * @param rowsPerPage 페이지당 로우수
	 * @param pageNo 페이지 번호
	 * @return 주어진 페이지 번호의 마지막 로우 인덱스
	 */
	public static int getEndIndex(int totalRow, int rowsPerPage, int pageNo) {
		return totalRow - (pageNo - 1) * rowsPerPage;
	}
}
