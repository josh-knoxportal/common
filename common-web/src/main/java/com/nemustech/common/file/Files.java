package com.nemustech.common.file;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.annotation.Column;
import com.nemustech.common.page.Paging;
import com.nemustech.common.storage.LocalFileStorage;
import com.nemustech.common.util.Utils;

/**
 * DDL :파일 테이블
 * 
 * <pre>
 * -- Mysql
 * DROP TABLE IF EXISTS files;
 * CREATE TABLE files
 * (
 *  id VARCHAR(30) NOT NULL-- AUTO_INCREMENT
 *  ,file_name VARCHAR(100)
 *  ,file_size INT(27) NOT NULL
 *  ,file_path VARCHAR(100)
 *  ,reg_id VARCHAR(100) NOT NULL
 *  ,reg_dt VARCHAR(14) DEFAULT DATE_FORMAT (NOW(), '%Y%m%d%H%i%s') NOT NULL
 *  ,mod_id VARCHAR(100) NOT NULL
 *  ,mod_dt VARCHAR(14) DEFAULT DATE_FORMAT (NOW(), '%Y%m%d%H%i%s') NOT NULL
 *  ,PRIMARY KEY
 * (
 *   id
 * )) CHARSET=utf8;
 * 
 * -- Oracle
 * DROP TABLE files CASCADE CONSTRAINTS PURGE;
 * CREATE TABLE files
 * (
 *   id VARCHAR2(30) NOT NULL
 *  ,file_name VARCHAR2(100)
 *  ,file_size NUMBER(27) NOT NULL
 *  ,file_path VARCHAR(100)
 *  ,doc_id VARCHAR2(30)
 *  ,reg_id VARCHAR2(100) NOT NULL
 *  ,reg_dt VARCHAR2(14) DEFAULT TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') NOT NULL
 *  ,mod_id VARCHAR2(100) NOT NULL
 *  ,mod_dt VARCHAR2(14) DEFAULT TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') NOT NULL
 *  ,CONSTRAINT files_pk PRIMARY KEY
 * (
 *   id
 * ));
 * --DROP SEQUENCE files_seq;
 * --CREATE SEQUENCE files_seq INCREMENT BY 1 START WITH 1;
 * </pre>
 * 
 * @author skoh
 */
public class Files extends Paging {
	/**
	 * 아이디(PK)
	 */
	@Column(primaryKey = true, autoIncrement = true, sequence = "files_seq")
	protected String id;

	/**
	 * 파일 경로
	 */
	@Column
	protected String file_path;

	/**
	 * 파일 이름
	 */
	@Column
	protected String file_name;

	/**
	 * 파일 크기
	 */
	@Column
	protected Long file_size;

	/**
	 * 파일 데이타
	 */
	protected byte[] file_bytes;

	public Files() {
	}

	public Files(String file_name, byte[] file_bytes) {
		this(null, file_name, file_bytes);
	}

	public Files(String file_path, String file_name, byte[] file_bytes) {
		this(null, file_path, file_name, file_bytes);
	}

	public Files(String id, String file_path, String file_name, byte[] file_bytes) {
		this(id, file_path, file_name, Utils.isValidate(file_bytes) ? (long) file_bytes.length : 0, file_bytes);
	}

	public Files(String id, String file_path, String file_name, Long file_size, byte[] file_bytes) {
		this.id = Utils.isValidate(id) ? id : LocalFileStorage.generateUID();
		this.file_path = file_path;
		this.file_name = file_name;
		this.file_size = file_size;
		this.file_bytes = file_bytes;
	}

	@Override
	public String id() {
		return id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public Long getFile_size() {
		return file_size;
	}

	public void setFile_size(Long file_size) {
		this.file_size = file_size;
	}

	public byte[] getFile_bytes() {
		return file_bytes;
	}

	public void setFile_bytes(byte[] file_bytes) {
		this.file_bytes = file_bytes;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "file_bytes");
	}
}
