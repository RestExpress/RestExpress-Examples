package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testGetIt() {
    	Response response = target.path("myresource").request().get();
    	assertEquals(200, response.getStatus());
        assertEquals("Got it!", response.readEntity(String.class));
    }

    @Test
    public void testPostIt() {
    	Response response = target.path("myresource").request().post(null);
    	assertEquals(200, response.getStatus());
        MyResource.Model model = response.readEntity(MyResource.Model.class);
        assertEquals("todd", model.getName());
        assertEquals("http://www.toddfredrich.com/", model.getHref());
        assertNotNull(model.getId());
    }
}
