package com.nemustech.common.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.common.model.Default;

/**
 * 페이징 처리용 모델
 * 
 * <pre>
 * <상속시 주의사항>
 * - 정렬 조건이 필수 (기본 정렬조건 : id DESC)
 * </pre>
 * 
 * @author skoh
 */
public class Paging extends Default {
	/**
	 * 기본 정렬 방식
	 */
	public static final String DEFAULT_ORDER_BY = "id DESC";

	/**
	 * 페이지 번호
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected int page_number;

	/**
	 * 페이지당 건수
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected int rows_per_page = PageNavigator.ROWS_PER_PAGE;

	/**
	 * 화면당 페이지 수
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	protected int page_group_count = PageNavigator.PAGE_GROUP_COUNT;

	/**
	 * 전체 건수
	 */
	@JsonIgnore
	protected int total_sise;

	public Paging() {
		order_by = DEFAULT_ORDER_BY;
	}

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}

	public int getRows_per_page() {
		return rows_per_page;
	}

	public void setRows_per_page(int rows_per_page) {
		this.rows_per_page = rows_per_page;
	}

	public int getPage_group_count() {
		return page_group_count;
	}

	public void setPage_group_count(int page_group_count) {
		this.page_group_count = page_group_count;
	}

	public int getTotal_sise() {
		return total_sise;
	}

	public void setTotal_sise(int total_sise) {
		this.total_sise = total_sise;
	}
}