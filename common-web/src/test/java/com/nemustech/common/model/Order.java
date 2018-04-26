package com.nemustech.common.model;

import java.util.Date;

import lombok.Data;

/* 주문 */
@Data
public class Order {
	private Long id;
	private Date date;
	private Member member;
	// getters & setters
}