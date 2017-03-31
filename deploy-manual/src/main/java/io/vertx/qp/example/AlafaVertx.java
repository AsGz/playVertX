package io.vertx.qp.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class AlafaVertx extends AbstractVerticle {
	@Override
	public void start(Future<Void> fut) {
		System.out.println("Main verticle has started, let's deploy some others...");
		

		// Deploy another instance and want for it to start
		vertx.deployVerticle("io.vertx.qp.example.BetaVertx", res -> {
			if (res.succeeded()) {
				String deploymentID = res.result();
				System.out.println("Other verticle deployed ok, deploymentID = " + deploymentID);

				// You can also explicitly undeploy a verticle deployment.
				// Note that this is usually unnecessary as any verticles
				// deployed by a verticle will be automatically
				// undeployed when the parent verticle is undeployed
				vertx.undeploy(deploymentID, res2 -> {
					if (res2.succeeded()) {
						System.out.println("Undeployed ok!");
					} else {
						res2.cause().printStackTrace();
					}
				});

			} else {
				res.cause().printStackTrace();
			}
		});

		// Send a message every second
		String myID = vertx.getOrCreateContext().deploymentID();
		EventBus eb = vertx.eventBus();
		vertx.setPeriodic(1000, v -> {
			eb.send("ping-address", "ping!", reply -> {
				if (reply.succeeded()) {
					System.out.println(myID + "Received reply " + reply.result().body() + "thread:"
							+ Thread.currentThread().getName());
				} else {
					System.out.println("No reply");
				}
			});
		});

		// Deploy specifying some config
		JsonObject config = new JsonObject().put("foo", "bar");
		vertx.deployVerticle("io.vertx.qp.example.BetaVertx", new DeploymentOptions().setConfig(config));

		// Deploy 10 instances
		// vertx.deployVerticle("io.vertx.qp.example.BetaVertx", new
		// DeploymentOptions().setInstances(10));

		// Deploy it as a worker verticle
		// vertx.deployVerticle("io.vertx.qp.example.BetaVertx", new
		// DeploymentOptions().setWorker(true));

		// Deploy it as a worker verticle
		/*
		 * vertx.deployVerticle(
		 * "io.vertx.example.core.verticle.deploy.OtherVerticle", new
		 * DeploymentOptions().setWorker(true), res -> { if (res.succeeded()) {
		 * System.out.println("can't happend."); } else {
		 * res.cause().printStackTrace(); } });
		 */

	}
}
