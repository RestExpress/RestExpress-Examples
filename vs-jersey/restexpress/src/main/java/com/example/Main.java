package com.example;

import java.io.IOException;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;

public class Main
{
	public static RestExpress startServer(String[] args) throws IOException
	{
		RestExpress server = new RestExpress();
		MyResource r = new MyResource();

		server.uri("/myapp/myresource", r)
			.method(HttpMethod.GET)
			.noSerialization();

		server.uri("/myapp/myresource", r)
			.method(HttpMethod.POST);

		server.bind(8080);
		return server;
    }

	public static void main(String[] args) throws Exception
	{
		RestExpress server = startServer(args);
        System.out.println("Hit enter to stop it...");
        System.in.read();
		server.shutdown();
	}
}
