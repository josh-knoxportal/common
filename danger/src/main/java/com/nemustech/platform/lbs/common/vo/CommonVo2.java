package com.nemustech.platform.lbs.common.vo;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;

/**
 * 공통 모델2
 */
@Table
public class CommonVo2 extends CommonVo {
	@Column
	protected String creator_id;

	@Column
	protected String editor_id;

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public String getEditor_id() {
		return editor_id;
	}

	public void setEditor_id(String editor_id) {
		this.editor_id = editor_id;
	}
}
