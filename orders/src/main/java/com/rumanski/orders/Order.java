package com.rumanski.orders;

import java.math.BigDecimal;
import java.util.List;

public class Order {

	public Long id;

	public Customer customer;

	public List<Item> items;

	public Payment payment;
	
	public Charge charge;

	public BigDecimal theTotal() {
		BigDecimal result = items == null ? BigDecimal.ZERO
				: items.stream()
						.map(Item::getQty)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
		return result;
	}

}
