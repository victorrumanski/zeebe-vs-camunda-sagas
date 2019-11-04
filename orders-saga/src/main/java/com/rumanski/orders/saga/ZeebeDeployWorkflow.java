package com.rumanski.orders.saga;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;

public class ZeebeDeployWorkflow {
	public static void main(String[] args) {

		final ZeebeClient client = ZeebeClient.newClientBuilder()
				.brokerContactPoint("127.0.0.1:26500")
				.build();

		System.out.println("Connected.");

		final DeploymentEvent deployment = client.newDeployCommand()
				.addResourceFile("zeebe-saga.bpmn")
				.send()
				.join();

		final int version = deployment.getWorkflows().get(0).getVersion();
		System.out.println("Workflow deployed. Version: " + version);

		client.close();
		System.out.println("Closed.");
	}
}
