package com.nemustech.sample.model;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import com.nemustech.common.file.Files;

@Table("files")
public class Files2 extends Files {
	/**
	 * 문서 아이디(FK)
	 */
	@Column(references = "Sample.id")
	protected String doc_id;

	public Files2() {
	}

	public Files2(Files files) {
		this(files, null);
	}

	public Files2(Files files, String doc_id) {
		super(files.getId(), files.getFile_path(), files.getFile_name(), files.getFile_size(), files.getFile_bytes());

		reg_id = files.getReg_id();
		mod_id = files.getMod_id();

		this.doc_id = doc_id;

	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
}
