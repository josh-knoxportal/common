package com.nemustech.sample.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.sample.model.SampleAndTest;

/**
 * 샘플 테스트 매퍼
 * 
 * @author skoh
 */
@Repository
public interface SampleAndTestMapper extends CommonMapper<SampleAndTest> {
}