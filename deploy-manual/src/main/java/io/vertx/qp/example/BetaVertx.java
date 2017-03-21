package io.vertx.qp.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class BetaVertx extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		// The start method will be called when the verticle is deployed

		System.out.println("Beta Verticle start...");

		System.out.println("Config is " + config());

		String myID = vertx.getOrCreateContext().deploymentID();
		EventBus eb = vertx.eventBus();
		eb.consumer("ping-address", message -> {
			System.out.println(myID + "Received message: " + message.body());
			// Now send back reply
			message.reply("pong!");
		});
		System.out.println("Receiver ready!");
	}

	@Override
	public void stop() throws Exception {

		// You can optionally override the stop method too, if you have some
		// clean-up to do
		System.out.println("Beta Verticle stop...");

	}

}
