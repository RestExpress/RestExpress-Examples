package org.restexpress.example.blogging;

import java.util.Map;

import org.restexpress.RestExpress;
import org.restexpress.example.blogging.domain.Blog;
import org.restexpress.example.blogging.domain.BlogEntry;
import org.restexpress.example.blogging.domain.Comment;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.RelTypes;

public abstract class Relationships
{
	public static void define(RestExpress server)
	{
		Map<String, String> routes = server.getRouteUrlsByName();

		HyperExpress.relationships()
		.forClass(Blog.class)
			.rel(RelTypes.SELF, routes.get(Constants.Routes.BLOG_READ_ROUTE))

		.forCollectionOf(BlogEntry.class)
			.rel(RelTypes.SELF, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE))
				.withQuery("filter={filter}")
				.withQuery("limit={limit}")
				.withQuery("offset={offset}")
			.rel(RelTypes.NEXT, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE) + "?offset={nextOffset}")
				.withQuery("filter={filter}")
				.withQuery("limit={limit}")
				.optional()
			.rel(RelTypes.PREV, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE) + "?offset={prevOffset}")
				.withQuery("filter={filter}")
				.withQuery("limit={limit}")
				.optional()
			.rel(RelTypes.UP, routes.get(Constants.Routes.BLOG_READ_ROUTE))

		.forClass(BlogEntry.class)
			.rel(RelTypes.SELF, routes.get(Constants.Routes.BLOG_ENTRY_READ_ROUTE))
			.rel(RelTypes.UP, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE))

		.forCollectionOf(Comment.class)
			.rel(RelTypes.SELF, routes.get(Constants.Routes.COMMENTS_READ_ROUTE))
				.withQuery("filter={filter}")
				.withQuery("limit={limit}")
				.withQuery("offset={offset}")
			.rel(RelTypes.NEXT, routes.get(Constants.Routes.COMMENTS_READ_ROUTE) + "?offset={nextOffset}")
				.withQuery("filter={filter}")
				.withQuery("limit={limit}")
				.optional()
			.rel(RelTypes.PREV, routes.get(Constants.Routes.COMMENTS_READ_ROUTE) + "?offset={prevOffset}")
				.withQuery("filter={filter}")
				.withQuery("limit={limit}")
				.optional()
			.rel(RelTypes.UP, routes.get(Constants.Routes.COMMENT_READ_ROUTE))

		.forClass(Comment.class)
			.rel(RelTypes.SELF, routes.get(Constants.Routes.COMMENT_READ_ROUTE))
			.rel(RelTypes.UP, routes.get(Constants.Routes.COMMENTS_READ_ROUTE));
	}
}
