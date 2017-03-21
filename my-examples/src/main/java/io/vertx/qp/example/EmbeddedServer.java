
//TODO: add unit test
//http://vertx.io/blog/my-first-vert-x-3-application/

package io.vertx.qp.example;

import io.vertx.core.Vertx;

/*
 *  * @author <a href="http://tfox.org">Tim Fox</a>
 *   */
public class EmbeddedServer {

	public static void main(String[] args) {
		// Create an HTTP server which simply returns "Hello World!" to each
		// request.
		System.out.println("server start...");
		Vertx.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello, vert.X!")).listen(8080);
		//Vertx.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
	}
}
