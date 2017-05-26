package com.io.qp;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Created by qpzhang on 17/5/26.
 */
public class Main {
    public static  void main(String []args){
        VertxOptions vo = new VertxOptions();
        //event loop size
        vo.setEventLoopPoolSize(8);
        Vertx vertx = Vertx.vertx(vo);
        //worker pool size
        DeploymentOptions options = new DeploymentOptions().setInstances(20);
        vertx.deployVerticle(HTTPProxy.class.getName(), options, e -> {
            System.out.println("start " + e.succeeded() + "," + e.cause());
        });
    }
}
