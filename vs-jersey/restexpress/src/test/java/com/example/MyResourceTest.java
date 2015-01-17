package com.example;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.post;
import static org.hamcrest.Matchers.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.RestExpress;

import com.example.Main;
import com.jayway.restassured.response.ValidatableResponse;

public class MyResourceTest
{
	private static RestExpress server;
	private static final String BASE_URL = "http://localhost:8080/myapp";

	@BeforeClass
	public static void beforeClass() throws Exception
	{
		String[] env =
		{
			"dev"
		};
		server = Main.startServer(env);
	}

	@AfterClass
	public static void afterClass()
	{
		server.shutdown();
	}

	@Test
	public void testRead()
	{
		ValidatableResponse response = get(BASE_URL + "/myresource").then();
		response.assertThat().statusCode(200);
		response.assertThat().body(equalTo("Got it!"));
	}

	@Test
	public void testCreate()
	{
		ValidatableResponse response = post(BASE_URL + "/myresource").then();
		response.statusCode(200);
		response.body("name", equalTo("todd"));
		response.body("href", equalTo("http://www.toddfredrich.com/"));
		response.body("id", not(equalTo(null)));
	}
}
