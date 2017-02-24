package com.nemustech.common.file;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.mybatisorm.annotation.Column;

import com.nemustech.common.model.Common;
import com.nemustech.common.storage.LocalFileStorage;
import com.nemustech.common.util.Utils;

/**
 * DDL :파일 테이블
 * 
 * <pre>
 * -- Mysql
 * -- DEFAULT DATE_FORMAT (NOW(), '%Y%m%d%H%i%s')
 * DROP TABLE IF EXISTS files;
 * CREATE TABLE files
 * (
 *  id VARCHAR(30) NOT NULL
 *  ,name VARCHAR(100)
 *  ,file_size INT(27) NOT NULL
 *  ,path VARCHAR(100)
 *  ,reg_id VARCHAR(100) NOT NULL
 *  ,reg_dt VARCHAR(14) NOT NULL
 *  ,mod_id VARCHAR(100) NOT NULL
 *  ,mod_dt VARCHAR(14) NOT NULL
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
 *  ,name VARCHAR2(100)
 *  ,file_size NUMBER(27) NOT NULL
 *  ,path VARCHAR(100)
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
public class Files extends Common {
	/**
	 * 아이디(PK)
	 */
	@Column(primaryKey = true)
	protected String id;

	/**
	 * 파일 경로
	 */
	@Column
	protected String path;

	/**
	 * 파일 이름
	 */
	@Column
	protected String name;

	/**
	 * 파일 크기
	 */
	@Column
	protected Long file_size;

	/**
	 * 파일 데이타
	 */
	protected byte[] bytes;

	public Files() {
	}

	public Files(String name, byte[] bytes) {
		this(null, name, bytes);
	}

	public Files(String path, String name, byte[] bytes) {
		this(null, path, name, bytes);
	}

	public Files(String id, String path, String name, byte[] bytes) {
		this(id, path, name, Utils.isValidate(bytes) ? (long) bytes.length : 0, bytes);
	}

	public Files(String id, String path, String name, Long file_size, byte[] bytes) {
		this.id = Utils.isValidate(id) ? id : LocalFileStorage.generateUID();
		this.path = path;
		this.name = name;
		this.file_size = file_size;
		this.bytes = bytes;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFile_size() {
		return file_size;
	}

	public void setFile_size(Long file_size) {
		this.file_size = file_size;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "bytes");
	}
}
