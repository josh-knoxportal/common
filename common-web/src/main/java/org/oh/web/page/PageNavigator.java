package org.oh.web.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.Page;

/**
 * 페이지 네비게이션
 */
public class PageNavigator<T> {
	public static final int ROWS_PER_PAGE = 1; // 기본 페이지당 행수
	public static final int PAGE_GROUP_COUNT = 10; // 기본 화면당 페이지수

	protected int currentPage; // 현재 페이지 번호
	protected int rowsPerPage; // 페이지당 행수
	protected int startRow; // 시작 행
	protected int endRow; // 마지막 행
	protected int totalSize; // 전체 목록 수
	protected int pageGroupCount; // 화면당 페이지 수

	protected int pageGroupStart = 1; // 페이지 그룹의 시작 페이지 번호
	protected int pageGroupEnd = 0; // 페이지 그룹의 마지막 페이지 번호
	protected int pageTotal = 1; // 전체 페이지 수 = totalRecord / pagingUnit;

	protected List<T> list = new ArrayList<T>(); // 데이타

	public static <T> PageNavigator<T> getInstance(Page<T> page) throws Exception {
		PageNavigator<T> pageNavi = new PageNavigator.Builder<T>(page).build();
		pageNavi.setList(page.getList());

		return pageNavi;
	}

	public static <T> PageNavigator<T> getInstance(Paging paging, List<T> list) throws Exception {
		PageNavigator<T> pageNavi = new PageNavigator.Builder<T>(paging).build();
		pageNavi.setList(list);

		return pageNavi;
	}

	public static class Builder<T> {
		// 필수 인자
		protected int currentPage = 1; // 현재 페이지 번호
		protected int totalSize = 0; // 전체 목록 수

		// 선택 인자 - 기본값으로 초기화
		protected int rowsPerPage = ROWS_PER_PAGE; // 페이지당 행수
		protected int pageGroupCount = PAGE_GROUP_COUNT; // 화면당 페이지 수

		public Builder(Paging paging) {
			this(paging.getPage_number(), paging.getTotal_sise(), paging.getRows_per_page(),
					paging.getPage_group_count());
		}

		public Builder(Page<T> page) {
			this(page.getPageNumber(), page.getCount());

			setRowsPerPage(page.getRows());
		}

		public Builder(int currentPage, int totalSize) {
			this.currentPage = currentPage;
			this.totalSize = totalSize;
		}

		public Builder(int currentPage, int totalSize, int rowsPerPage, int pageGroupCount) {
			this(currentPage, totalSize);

			this.rowsPerPage = rowsPerPage;
			this.pageGroupCount = pageGroupCount;
		}

		public Builder<T> setRowsPerPage(int rowsPerPage) {
			this.rowsPerPage = rowsPerPage;

			return this;
		};

		public Builder<T> setPageGroupCount(int pageGroupCount) {
			this.pageGroupCount = pageGroupCount;

			return this;
		};

		public PageNavigator<T> build() {
			return new PageNavigator<T>(this);
		}
	}

	protected PageNavigator(Builder<T> builder) {
		setPageGroupCount(builder.pageGroupCount);
		setCurrentPage(builder.currentPage);
		setRowsPerPage(builder.rowsPerPage);
		setTotalSize(builder.totalSize);
	}

	public int getCurrentPage() {
		if (pageTotal > 0 && currentPage > pageTotal)
			currentPage = pageTotal;

		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage < 1)
			currentPage = 1;

		this.currentPage = currentPage;

		calcStartEndRow(currentPage);
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;

		calcStartEndRow(currentPage);
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * 시작/종료/전체 페이지 계산
	 * 
	 * @param totalSize
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;

		if (endRow > totalSize)
			endRow = totalSize;

		setPageTotal();
		setPageGroupStart();
		setPageGroupEnd();
	}

	public int getPageGroupCount() {
		return pageGroupCount;
	}

	public void setPageGroupCount(int pageGroupCount) {
		this.pageGroupCount = pageGroupCount;
	}

	public int getPageGroupStart() {
		return pageGroupStart;
	}

	protected void setPageGroupStart() {
		this.pageGroupStart = ((currentPage - 1) / pageGroupCount) * pageGroupCount + 1;
	}

	public int getPageGroupEnd() {
		return pageGroupEnd;
	}

	protected void setPageGroupEnd() {
		int tempPageGroupEnd = pageGroupStart + pageGroupCount;

		if (tempPageGroupEnd > pageTotal)
			tempPageGroupEnd = pageTotal + 1;

		if (tempPageGroupEnd > 1)
			tempPageGroupEnd = tempPageGroupEnd - 1;

		this.pageGroupEnd = tempPageGroupEnd;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	protected void setPageTotal() {
		int tempPageTotal = (totalSize) / rowsPerPage;

		if (totalSize > tempPageTotal * rowsPerPage)
			tempPageTotal++;

		this.pageTotal = tempPageTotal;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 시작/종료 행수 계산
	 * 
	 * @param currentPage
	 */
	protected void calcStartEndRow(int currentPage) {
		startRow = (currentPage - 1) * rowsPerPage + 1;
		endRow = startRow + rowsPerPage - 1;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static void main(String[] args) {
		PageNavigator<Object> pageNavi = new PageNavigator.Builder<Object>(7, 77, 10, 5).build();
		System.out.println(pageNavi);
	}
}
