package com.rumanski.orders;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Item {
	
	@NotEmpty
	public String name;

	@NotNull
	private BigDecimal qty;

	public BigDecimal getQty() {
		return qty;
	}

}
