package com.nemustech.platform.lbs.common.vo;

import io.swagger.annotations.ApiModel;

/**
 * 검색 VO
 **/
@ApiModel(description = "검색 VO 정보")
public class SearchVo {

	private String sort_column = "id";
	private String search_filter = "";

	private int limit_offset;
	private int limit_count;

	private String str_order_by = "asc";

	public String getSort_column() {
		return sort_column;
	}

	public void setSort_column(String sort_column) {
		this.sort_column = sort_column;
	}

	public String getSearch_filter() {
		return search_filter;
	}

	public void setSearch_filter(String search_filter) {
		this.search_filter = search_filter;
	}

	public int getLimit_offset() {
		return limit_offset;
	}

	public void setLimit_offset(int limit_offset) {
		this.limit_offset = limit_offset;
	}

	public int getLimit_count() {
		return limit_count;
	}

	public void setLimit_count(int limit_count) {
		this.limit_count = limit_count;
	}

	public String getStr_order_by() {
		return str_order_by;
	}

	public void setStr_order_by(String str_order_by) {
		this.str_order_by = str_order_by;
	}

}
