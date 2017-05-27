package com.io.qp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

/**
 *
 * Created by qpzhang on 17/5/26.
 */
public class HTTPProxy extends AbstractVerticle {
    @Override
    public void  start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(routingContext -> {
            
        	System.out.println("===================================");
        	//check auth
        	HttpServerRequest r = routingContext.request();
        	MultiMap headerMap = r.headers();
        	MultiMap paramsMap = r.params();
        	String path = r.path();
            //split stream
            //upstream
            //response
        	System.out.println("headers:");
        	headerMap.forEach(header ->{
        		System.out.println("\t" + header.getKey() + "\t" + header.getValue());
        	});
        	
        	System.out.println("params:");
        	paramsMap.forEach(p ->{
        		System.out.println("\t" + p.getKey() + "\t" + p.getValue());
        	});
   
        	System.out.println("cookies:");
        	routingContext.cookies().forEach(cookie->{
        		//cookie.getDomain()
        		System.out.println("\t" + cookie.getName() + "\t" + cookie.getValue());
        	});
        	
        	System.out.println(path);
        	
        	routingContext.response().end("hello, proxy!");
        });
        //TODO: need read from config
        server.requestHandler(router::accept).listen(8081, "127.0.0.1");
    }
}
