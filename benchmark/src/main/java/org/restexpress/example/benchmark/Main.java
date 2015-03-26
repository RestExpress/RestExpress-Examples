package org.restexpress.example.benchmark;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;

public class Main
{
    public static void main(String[] args)
    {
        RestExpress server = new RestExpress()
            .setName("Echo");

        server.uri("/echo", new Object()
        {
            @SuppressWarnings("unused")
            public String read(Request req, Response res)
            {
                String value = req.getHeader("echo");
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
