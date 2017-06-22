package com.nemustech.platform.lbs.common.vo;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class DefaultVo implements Serializable {

	private static final long serialVersionUID = -5598189735343246247L;
	private int page_index = 0;
	private int record_count_per_page = -1;
	private int tot_page_count = 0;
	private String sort_column = "id";
	private int order_by = 1;
	private String search_filter = "";
	@SuppressWarnings("unused")
	private String str_order_by = "asc";
	private String sort_column2 = "";
	
	public int getPage_index() {
		return page_index;
	}
	public void setPage_index(int page_index) {
		this.page_index = page_index;
	}
	public int getRecord_count_per_page() {
		return record_count_per_page;
	}
	public void setRecord_count_per_page(int record_count_per_page) {
		this.record_count_per_page = record_count_per_page;
	}
	public String getSort_column() {
		return sort_column;
	}
	public void setSort_column(String sort_column) {
		this.sort_column = sort_column;
	}
	public int getOrder_by() {
		return order_by;
	}
	public void setOrder_by(int order_by) {
		this.order_by = order_by;
		if( this.order_by == 1 ) {
			this.str_order_by = "asc";
		}
		else {
			this.str_order_by = "desc";
		}
	}
	public String getSearch_filter() {
		return search_filter;
	}
	public void setSearch_filter(String search_filter) {
		this.search_filter = search_filter;
	}
	public int getTot_page_count() {
		return tot_page_count;
	}
	public void setTot_page_count(int tot_page_count) {
		this.tot_page_count = tot_page_count;
	}
	public String getStr_order_by() {
		if( this.order_by == 1 ) {
			return "asc";
		}
		else {
			return "desc";
		}
	}
	public void setStr_order_by(String str_order_by) {
		if( str_order_by.toLowerCase().equals("asc") == true ) {
			this.order_by = 1;
		}
		else {
			this.order_by = 2;
		}
		this.str_order_by = str_order_by;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonExtraInfo() {
		JSONObject jobj = new JSONObject();
		
		jobj.put("page_index", this.getPage_index());
		jobj.put("record_count_per_page",  this.getRecord_count_per_page());
		jobj.put("tot_page_count", this.getTot_page_count());
		jobj.put("sort_column", this.getSort_column());
		jobj.put("order_by", this.getOrder_by());
		jobj.put("search_filter", this.getSearch_filter());
		
		return jobj;
	}
	public String getSort_column2() {
		return sort_column2;
	}
	public void setSort_column2(String sort_column2) {
		this.sort_column2 = sort_column2;
	}
}
