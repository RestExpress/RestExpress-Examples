package org.restexpress.example.blogging;

import io.netty.handler.codec.http.HttpMethod;

import org.restexpress.RestExpress;

public abstract class Routes
{
	public static void define(Configuration config, RestExpress server)
	{
		server.uri("/blogs.{format}", config.getBlogController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST);
	
		server.uri("/blogs/{blogId}.{format}", config.getBlogController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.Routes.BLOG_READ_ROUTE);
	
		server.uri("/blogs/{blogId}/entries.{format}", config.getBlogEntryController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name(Constants.Routes.BLOG_ENTRIES_READ_ROUTE);
	
		server.uri("/blogs/{blogId}/entries/{entryId}.{format}", config.getBlogEntryController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.Routes.BLOG_ENTRY_READ_ROUTE);
	
		server.uri("/blogs/{blogId}/entries/{entryId}/comments.{format}", config.getCommentController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name(Constants.Routes.COMMENTS_READ_ROUTE);
	
		server.uri("/blogs/{blogId}/entries/{entryId}/comments/{commentId}.{format}", config.getCommentController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.Routes.COMMENT_READ_ROUTE);
	}
}
