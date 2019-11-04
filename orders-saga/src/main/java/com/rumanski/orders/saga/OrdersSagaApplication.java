package com.rumanski.orders.saga;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;

@EnableZeebeClient
@SpringBootApplication
@SuppressWarnings("unchecked")
public class OrdersSagaApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersSagaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(OrdersSagaApplication.class, args);
	}

	@ZeebeWorker(type = "create-customer")
	public void handleJoin(final JobClient client, final ActivatedJob job) {
		logJob(job);
		Map<String, Object> vars = job.getVariablesAsMap();
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> customer = restTemplate.postForObject("http://localhost:9991/api/orders/join", vars,
				Map.class);
		vars.put("customer", customer);
		client.newCompleteCommand(job.getKey()).variables(vars).send().join();
	}

	@ZeebeWorker(type = "create-order")
	public void handleOrder(final JobClient client, final ActivatedJob job) {
		logJob(job);
		Map<String, Object> vars = job.getVariablesAsMap();
		Map<String, Object> request = new HashMap<String, Object>();

		request.put("customer", vars.get("customer"));
		request.put("items", vars.get("items"));

		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> order = restTemplate.postForObject("http://localhost:9991/api/orders/order", request,
				Map.class);
		vars.put("order", order);
		client.newCompleteCommand(job.getKey()).variables(vars).send().join();
	}

	@ZeebeWorker(type = "process-payment")
	public void handlePayment(final JobClient client, final ActivatedJob job) {
		logJob(job);
		Map<String, Object> vars = job.getVariablesAsMap();
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> payment = restTemplate.postForObject("http://localhost:9991/api/orders/pay",
				vars.get("order"), Map.class);
		vars.put("payment", payment);
		client.newCompleteCommand(job.getKey()).variables(vars).send().join();
	}

	@ZeebeWorker(type = "charge-credits")
	public void handleCharge(final JobClient client, final ActivatedJob job) {
		logJob(job);
		Map<String, Object> vars = job.getVariablesAsMap();
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> charge = restTemplate.postForObject("http://localhost:9991/api/orders/charge",
				vars.get("payment"), Map.class);
		vars.put("charge", charge);
		client.newCompleteCommand(job.getKey()).variables(vars).send().join();
	}

	private static void logJob(final ActivatedJob job) {
		LOGGER.info(
				"complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
				job.getType(),
				job.getKey(),
				job.getElementId(),
				job.getWorkflowInstanceKey(),
				Instant.ofEpochMilli(job.getDeadline()),
				job.getCustomHeaders(),
				job.getVariables());
	}

}