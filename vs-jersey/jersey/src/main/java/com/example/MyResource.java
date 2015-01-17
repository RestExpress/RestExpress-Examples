package com.example;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("myresource")
public class MyResource
{
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt()
	{
//		throw new RuntimeException("message goes here");
		return "Got it!";
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Model postIt()
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

		public UUID getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public String getHref()
		{
			return href;
		}
	}
}
