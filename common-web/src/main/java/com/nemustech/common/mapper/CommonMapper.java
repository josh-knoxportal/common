package com.nemustech.common.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.common.model.Default;

/**
 * 공통 매퍼
 * 
 * @author skoh
 */
@Repository
public interface CommonMapper<T extends Default> {
	public List<T> list(T model);

	public int count(T model);

	public int insert(T model);

	public int update(T model);

	public int delete(T model);
}