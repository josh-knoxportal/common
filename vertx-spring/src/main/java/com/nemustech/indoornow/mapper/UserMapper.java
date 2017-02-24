package com.nemustech.indoornow.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.indoornow.user.User;

@Repository
public interface UserMapper extends CommonMapper<User> {
}