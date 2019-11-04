package com.rumanski.orders;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateOrderRequest {
	
	@NotNull
	public Customer customer;

	@NotEmpty
	public List<Item> items;
}
