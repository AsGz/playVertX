package com.io.qp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;

/**
 * Created by qpzhang on 17/5/27.
 */
public class MyHttpProxy  extends AbstractVerticle {
    @Override
    public void  start() throws Exception {
        HttpClient client = vertx.createHttpClient(new HttpClientOptions());
        vertx.createHttpServer().requestHandler(req -> {
            System.out.println("Proxying request: " + req.uri());
            HttpClientRequest c_req = client.request(req.method(), 8080, "127.0.0.1", req.uri(), c_res -> {
                System.out.println("Proxying response: " + c_res.statusCode());
                req.response().setChunked(true);
                req.response().setStatusCode(c_res.statusCode());
                req.response().headers().setAll(c_res.headers());
                c_res.handler(data -> {
                    System.out.println("Proxying response body: " + data.toString("ISO-8859-1"));
                    req.response().write(data);
                });
                c_res.endHandler((v) -> req.response().end());
            });
            c_req.setChunked(true);
            c_req.headers().setAll(req.headers());
            req.handler(data -> {
                System.out.println("Proxying request body " + data.toString("ISO-8859-1"));
                c_req.write(data);
            });
            req.endHandler((v) -> c_req.end());
        }).listen(8081);
    }
}
