package org.oh.sample.model;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.oh.common.file.Files;

@Table("files")
public class Files2 extends Files {
	/**
	 * 문서 아이디(FK)
	 */
	@Column(references = "Sample.id")
	protected String doc_id;

	public Files2() {
	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
}
