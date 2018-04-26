package com.nemustech.common.model;

import lombok.Data;

/* 회원 */
@Data
public class Member {
	private Long id;
	private String name;
	private Address address;
	// getters & setters
}