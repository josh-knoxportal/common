package com.nemustech.common.model;

import lombok.Data;

/* 주소 */
@Data
public class Address {
	private String street;
	private String city;
	private String zipcode;
	// getters & setters
}