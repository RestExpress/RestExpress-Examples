package com.benchmark;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.RestExpress;

public class Main
{
    public static void main(String[] args)
    {
        RestExpress server = new RestExpress()
            .setName("Echo");

        server.uri("/echo", new Object()
        {
            public String read(Request req, Response res)
            {
                String value = req.getRawHeader("echo");
                res.setContentType("text/xml");

                if (value == null)
                {
                    return "<http_test><error>no value specified</error></http_test>";
                }
                else
                {
                    return String.format("<http_test><value>%s</value></http_test>", value);
                }
            }
        })
        .method(HttpMethod.GET)
        .noSerialization();

        server.bind(8000);
        server.awaitShutdown();
    }}
