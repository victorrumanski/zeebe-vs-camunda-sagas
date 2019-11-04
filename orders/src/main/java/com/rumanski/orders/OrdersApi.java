package com.rumanski.orders;

import java.util.Random;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrdersApi {

	@PostMapping("/join")
	public ResponseEntity<Customer> createCustomer(@RequestBody @Valid JoinRequest r) {
		System.out.println();
		Customer c = new Customer(r.email, r.card, r.address);
		c.id = (Long.parseLong(new Random().nextInt(1000) + ""));
		return ResponseEntity.ok().body(c);
	}

	@PostMapping("/order")
	public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderRequest r) {
		System.out.println();
		Order order = new Order();
		order.id = (Long.parseLong(new Random().nextInt(1000) + ""));
		order.customer = r.customer;
		order.items = r.items;
		return ResponseEntity.ok().body(order);
	}

	@PostMapping("/pay")
	public ResponseEntity<Payment> pay(@RequestBody Order order) {
		System.out.println();
		Payment payment = new Payment();
		payment.id = (Long.parseLong(new Random().nextInt(1000) + ""));
		payment.orderid = (order.id);
		payment.amount = ((order.theTotal()));
		return ResponseEntity.ok().body(payment);
	}

	@PostMapping("/charge")
	public ResponseEntity<Charge> charge(@RequestBody Payment payment) {
		System.out.println();
		Charge c = new Charge();
		c.id = (Long.parseLong(new Random().nextInt(1000) + ""));
		c.paymentid = (payment.id);
		c.orderid = (payment.orderid);
		c.credits = ((long) (payment.amount.doubleValue() / 2));
		return ResponseEntity.ok().body(c);
	}

}
