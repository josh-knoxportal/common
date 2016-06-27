/*
 *    Copyright 2012 The MyBatisORM Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatisorm;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.oh.web.page.PageNavigator;

public class Page<T> {

	private int pageNumber = 1; // 초기값 부여 by skoh
	private int rows = PageNavigator.ROWS_PER_PAGE; // 초기값 부여 by skoh
	private int count; // 전체 건수
	private List<T> list;

	public Page() {
	}

	// 생성자 추가 by skoh
	public Page(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public Page(int pageNumber, int rows) {
		this(pageNumber,rows,0);
	}
	
	public Page(int pageNumber, int rows, int totalCount) {
		this.pageNumber = pageNumber;
		this.rows = rows;
		this.count = totalCount;
		this.list = Collections.emptyList();
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int totalCount) {
		this.count = totalCount;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getLast() {
		return count == 0 ? 1 : ((int)(count-1)/rows+1);
	}

	// toString 추가 by skoh
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
