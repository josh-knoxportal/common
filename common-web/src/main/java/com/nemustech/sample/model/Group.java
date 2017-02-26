package com.nemustech.sample.model;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;

import com.nemustech.common.model.Default;

import lombok.Data;
import lombok.ToString;

/**
 * DDL : 샘플 테이블
 * 
 * <pre>
 * -- Mysql
 * DROP TABLE IF EXISTS V3_GROUP;
 * CREATE TABLE V3_GROUP (
 * id BIGINT NOT NULL AUTO_INCREMENT,
 * ,name VARCHAR(255) NOT NULL,
 * ,parent_group_id BIGINT NOT NULL DEFAULT 0,
 * PRIMARY KEY
 * (
 *   id
 * ))
 */
@Data
@ToString(callSuper = true)
@Table("V3_GROUP")
public class Group extends Default {
	@Column(primaryKey = true, autoIncrement = true)
	protected Long id;

	@Column
	protected String name;

	@Column
	protected Long parent_group_id;
}
