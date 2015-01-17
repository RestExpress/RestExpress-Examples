package com.example;

import java.util.UUID;

import org.restexpress.Request;
import org.restexpress.Response;

public class MyResource
{
	public String read(Request request, Response response)
	{
//		throw new RuntimeException("message goes here");
		return "Got it!";
	}

	public Model create(Request request, Response response)
	{
		return new Model("todd", "http://www.toddfredrich.com/");
	}

	public class Model
	{
		private UUID id = UUID.randomUUID();
		private String name;
		private String href;

		public Model(String name, String href)
		{
			super();
			this.name = name;
			this.href = href;
		}
	}
}
