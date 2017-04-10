package com.nemustech.sample.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.sample.model.Test;

/**
 * @author skoh
 */
@Repository
public interface TestMapper extends CommonMapper<Test> {
}