package com.nemustech.sample.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.sample.model.Sample;

/**
 * 샘플 매퍼
 * 
 * @author skoh
 */
@Repository
public interface SampleMapper extends CommonMapper<Sample> {
	public int merge(Sample model);
}