package com.nemustech.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.mapper.TestMapper;
import com.nemustech.sample.model.Test;
import com.nemustech.sample.service.TestService;

/**
 * @author skoh
 */
@Service
public class TestServiceImpl extends CommonServiceImpl<Test> implements TestService {
	@Autowired
	protected TestMapper mapper;

	@Override
	public CommonMapper<Test> getMapper() {
		return mapper;
	}
}