package org.restexpress.example.blogging.controller;

import static com.strategicgains.repoexpress.adapter.Identifiers.UUID;

import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.restexpress.example.blogging.Constants;
import org.restexpress.example.blogging.domain.Blog;
import org.restexpress.example.blogging.persistence.BlogRepository;
import org.restexpress.query.QueryFilters;
import org.restexpress.query.QueryOrders;
import org.restexpress.query.QueryRanges;

import com.strategicgains.hyperexpress.builder.DefaultTokenResolver;
import com.strategicgains.hyperexpress.builder.DefaultUrlBuilder;
import com.strategicgains.hyperexpress.builder.UrlBuilder;
import com.strategicgains.syntaxe.ValidationEngine;

import io.netty.handler.codec.http.HttpMethod;

public class BlogController
{
	private static final UrlBuilder LOCATION_BUILDER = new DefaultUrlBuilder();
	private BlogRepository blogs;
	
	public BlogController(BlogRepository blogRepository)
	{
		super();
		this.blogs = blogRepository;
	}

	public Blog create(Request request, Response response)
	{
		Blog blog = request.getBodyAs(Blog.class, "Blog details not provided");
		ValidationEngine.validateAndThrow(blog);
		Blog saved = blogs.create(blog);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationPattern = request.getNamedUrl(HttpMethod.GET, Constants.Routes.BLOG_ENTRY_READ_ROUTE);
		response.addLocationHeader(LOCATION_BUILDER.build(locationPattern, new DefaultTokenResolver()));

		// Return the newly-created item...
		return saved;
	}

	public Blog read(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "No Blog ID supplied");
		Blog entity = blogs.read(UUID.parse(id));

		return entity;
	}

	public List<Blog> readAll(Request request, Response response)
	{
		QueryFilter filter = QueryFilters.parseFrom(request);
		QueryOrder order = QueryOrders.parseFrom(request);
		QueryRange range = QueryRanges.parseFrom(request, 20);
		List<Blog> entities = blogs.readAll(filter, range, order);
		response.setCollectionResponse(range, entities.size(), blogs.count(filter));

		return entities;
	}

	public void update(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.BLOG_ID_PARAMETER);
		Blog blog = request.getBodyAs(Blog.class, "Blog details not provided");

		// Can't change the blod ID via update.
		blog.setId(UUID.parse(id));

		ValidationEngine.validateAndThrow(blog);
		blogs.update(blog);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "No Blog ID supplied");
		blogs.delete(UUID.parse(id));
		response.setResponseNoContent();
	}
}
