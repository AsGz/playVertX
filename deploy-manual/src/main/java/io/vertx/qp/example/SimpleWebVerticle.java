package io.vertx.qp.example;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.LongAdder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class SimpleWebVerticle extends AbstractVerticle {
	private static final LongAdder adder = new LongAdder();
	@Override
	public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(request->{
        	adder.increment();
        	long seq = adder.longValue();
        	if (seq % 2 == 0){
        		//模拟慢请求
        		try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        	HttpServerResponse response = request.response();
        	  response.putHeader("content-type", "text/plain");
        	  response.end("Hello World! handle: " + Thread.currentThread().getName());
        	  System.out.println(LocalDateTime.now() + "seq:" + seq + " " + Thread.currentThread().getName());
        });
        server.listen(8080);
	}
}
