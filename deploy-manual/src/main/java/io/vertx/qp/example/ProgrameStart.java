package io.vertx.qp.example;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ProgrameStart {

	public static void main(String[] args) {
		VertxOptions vo = new VertxOptions();
		vo.setEventLoopPoolSize(8);
		Vertx vertx = Vertx.vertx(vo);

		DeploymentOptions options = new DeploymentOptions();
		options.setInstances(20);

		vertx.deployVerticle(SimpleWebVerticle.class.getName(), options, e -> {
			System.out.println(e.succeeded());
			System.out.println(e.failed());
			System.out.println(e.cause());
			System.out.println(e.result());
			
			//这里虽然有20个实例, 但是event loop size 是8，所以只会有8个处理线程来处理请求
			for (int i = 0; i < 100; i++) {
				vertx.createHttpClient().getNow(8080, "127.0.0.1", "/hello", response -> {
					//response.bodyHandler(System.out::println);
				});
			}
		});
	}
}
