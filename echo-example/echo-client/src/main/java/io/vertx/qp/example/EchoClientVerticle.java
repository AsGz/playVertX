package io.vertx.qp.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetSocket;


public class EchoClientVerticle extends AbstractVerticle {
	@Override
	public void start() throws Exception {
		vertx.createNetClient().connect(1234, "localhost", res->{
			if (res.succeeded()){
				//start在event loop主线程上面,这里只是示例,真实情况下收发包等操作,需要另起worker线程
				NetSocket socket = res.result();
				socket.handler(buf ->{
					System.out.println("Net client receiving: " + buf.toString("UTF-8"));
				});
				for(int i =0; i<10; i++){
					String str = "hello " + i + "\n";
					System.out.println("Net client sending: " + str);
					socket.write(str);
				}
			}else{
				 System.out.println("Failed to connect " + res.cause());
			}
		});
		
		EventBus eb = vertx.eventBus();
		
		//同一个instance中的eventbus通信
		vertx.setPeriodic(1000, v ->{
			eb.send("ping-address", "ping!", reply->{
				if(reply.succeeded()){
					System.out.println("Received reply "+ reply.result().body());
				}else{
					System.out.println("No reply");
				}
			});
		});
		/*
		vertx.setPeriodic(1000, v ->{
			eb.send("local-ping-address", "ping!", reply->{
				if(reply.succeeded()){
					System.out.println("Received reply "+ reply.result().body());
				}else{
					System.out.println("No reply");
				}
			});
		});
		eb.consumer("local-ping-address", message -> {
			System.out.println("Received message: " + message.body());
			message.reply("pong!");
		});
		*/
	}
}
