package com.io.qp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.*;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;

/**
 *
 * Created by qpzhang on 17/5/26.
 */
public class HTTPProxy extends AbstractVerticle {
    @Override
    public void  start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        //init to handler cookie
        router.route().handler(CookieHandler.create());

        Route ping = router.route().path("/ping");
        ping.handler(context -> {
            Cookie my = Cookie.cookie("my" , "value");
        	my.setDomain("127.0.0.1");
        	my.setPath("/");
        	my.setMaxAge(3600);
            context.addCookie(my);
        	context.response().end("set cookie.");
        });

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
        		System.out.println("\t" + cookie.getName() + "\t" + cookie.getValue());
        	});

        	Cookie my = Cookie.cookie("other" , "hook");
        	my.setDomain("127.0.0.1");
        	my.setPath("/");
        	my.setMaxAge(3600);
            routingContext.addCookie(my);

            HttpClient client = vertx.createHttpClient(new HttpClientOptions());
            int upstreamPort = 8080;
            String upstreamHost = "127.0.0.1";
            System.out.println("fuck proxy to:" + r.method() + "\t" + r.scheme() + "://" + upstreamHost + ":" + upstreamPort + r.uri());

            HttpClientRequest currReq = client.request(r.method(), upstreamPort, upstreamHost, r.uri(), resp->{
			    System.out.println("it's ok.....");
			    r.response().setChunked(true);
                r.response().setStatusCode(resp.statusCode());
                //server set cookie will be in the response headers
                r.response().headers().setAll(resp.headers());
                resp.handler(data -> {
                    System.out.println("proxy:" + resp.statusCode());
                    r.response().write(data);
                });
                resp.exceptionHandler(e->{
                   System.out.println("proxy exception:" + e);
                   r.response().end("proxy failed.");
                });
                resp.endHandler((v) -> r.response().end());
            });
			currReq.setTimeout(1000*3);
            currReq.setChunked(true);
            //cookie will be send to upstream in the heanders
            currReq.headers().setAll(r.headers());
            currReq.exceptionHandler(e->{
                System.out.println("connect proxy exception:" + e);
                r.response().end("proxy failed.");
            });
            r.handler(data -> {
                System.out.println("body is:" + data);
                currReq.write(data);
            });
            r.response().endHandler((v)->currReq.end());
        });
        //TODO: need read from config
        server.requestHandler(router::accept).listen(8081, "127.0.0.1");
    }
}
