package io.vertx.qp.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.streams.Pump;


public class EchoServerVerticle extends AbstractVerticle {

	public static void main(String[] args) {
		
	}

	@Override
	public void start() throws Exception {
		vertx.createNetServer().connectHandler(sock -> {
			Pump.pump(sock, sock).start();
		}).listen(1234);

		//如果在不同的进程或机器使用event bus传递消息, 那么需要配置cluster manager
		//默认采用hazelcast来管理，在resources里面使用cluster.xml来进行配置
		//启动命令需要带上其它参数
		//java -jar ./target/echo-client-3.4.0-fat.jar  -cluster -cluster-host 127.0.0.1
		//java -jar ./target/echo-server-3.4.0-fat.jar   -cluster -cluster-host 127.0.0.1
		EventBus eb = vertx.eventBus();
		eb.consumer("ping-address", message -> {
			System.out.println("Received message: " + message.body());
			message.reply("pong!");
		});
	}
}
