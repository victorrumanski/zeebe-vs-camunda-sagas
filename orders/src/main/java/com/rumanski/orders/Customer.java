package com.rumanski.orders;

public class Customer {

	public Long id;

	public String email;

	public String card;

	public String address;

	public Customer(String email, String card, String address) {
		super();
		this.email = email;
		this.card = card;
		this.address = address;
	}

}
