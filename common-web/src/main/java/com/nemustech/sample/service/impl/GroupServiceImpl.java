package com.nemustech.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.Constants;
import com.nemustech.sample.mapper.GroupMapper;
import com.nemustech.sample.model.Group;
import com.nemustech.sample.service.GroupService;

/**
 * @author skoh
 */
@Service
public class GroupServiceImpl extends CommonServiceImpl<Group> implements GroupService {
	@Autowired
	protected GroupMapper mapper;

//	@Override
//	public CommonMapper<Group> getMapper() {
//		return mapper;
//	}

	@Override
	public String getCacheName() {
		return Constants.CACHE_NAME;
	}
}