package com.rumanski.orders.saga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.spring.client.ZeebeClientLifecycle;

@RestController
@RequestMapping("/api/order-saga")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StartSagaAPI {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartSagaAPI.class);

	@Autowired
	private ZeebeClientLifecycle zeebeClient;

	@PostMapping("/start")
	public ResponseEntity<WorkflowInstanceEvent> start() {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("email", "luis@celfocus.com");
		variables.put("card", "123456789876543");
		variables.put("address", "Lake Towers Arrabida, 1223 - GAIA");
		List items = new ArrayList<>();
		Map item = new HashMap<>();
		item.put("name", "Maga Hat");
		item.put("qty", 222);
		items.add(item);
		Map item1 = new HashMap<>();
		item1.put("name", "Milk 1LT");
		item1.put("qty", 2);
		items.add(item1);
		variables.put("items", items);

		final WorkflowInstanceEvent event = zeebeClient
				.newCreateInstanceCommand()
				.bpmnProcessId("order-saga")
				.latestVersion()
				.variables(variables)
				.send()
				.join();

		LOGGER.info(
				"started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
				event.getWorkflowKey(), event.getBpmnProcessId(), event.getVersion(), event.getWorkflowInstanceKey());
		return ResponseEntity.ok().body(event);
	}

}
