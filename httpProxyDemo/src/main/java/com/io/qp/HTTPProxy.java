package com.io.qp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
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
            //check auth
            //split stream
            //upstream
            //response
        });
        //TODO: need read from config
        server.requestHandler(router::accept).listen(8081, "127.0.0.1");
    }
}
