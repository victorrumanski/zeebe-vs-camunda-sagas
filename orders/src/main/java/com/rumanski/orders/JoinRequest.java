package com.rumanski.orders;

import javax.validation.constraints.NotEmpty;

public class JoinRequest {
	@NotEmpty
	public String email, card, address;
}
