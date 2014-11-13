package com.kickstart.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.restexpress.exception.BadRequestException;
import org.restexpress.query.QueryFilters;
import org.restexpress.query.QueryOrders;
import org.restexpress.query.QueryRanges;

import com.kickstart.Constants;
import com.kickstart.domain.Blog;
import com.kickstart.persistence.BlogRepository;
import com.strategicgains.hyperexpress.RelTypes;
import com.strategicgains.hyperexpress.domain.Link;
import com.strategicgains.syntaxe.ValidationEngine;

public class BlogController
{
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
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		response.addLocationHeader(LinkUtils.formatUrl(locationUrl, Constants.BLOG_ID_PARAMETER, saved.getId()));

		// Return the newly-created item...
		return saved;
	}

	public Blog read(Request request, Response response)
	{
		String id = request.getHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		Blog result = blogs.read(id);

		// Add 'self' link
		String selfUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		String selfUrl = LinkUtils.formatUrl(selfUrlPattern, Constants.BLOG_ID_PARAMETER, result.getId());
		result.addLink(new Link(RelTypes.SELF, selfUrl));

		// Add 'entries' link
		String entriesUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRIES_READ_ROUTE);
		String entriesUrl = LinkUtils.formatUrl(entriesUrlPattern, Constants.BLOG_ID_PARAMETER, result.getId());
		result.addLink(new Link("http://www.pearson.com/pts/2012/blogging/entries", entriesUrl, "This Blog's Entries"));
		return result;
	}

	public List<Blog> readAll(Request request, Response response)
	{
		QueryFilter filter = QueryFilters.parseFrom(request);
		QueryOrder order = QueryOrders.parseFrom(request);
		QueryRange range = QueryRanges.parseFrom(request, 20);
		List<Blog> results = blogs.readAll(filter, range, order);
		response.setCollectionResponse(range, results.size(), blogs.count(filter));
		
		// Add 'self' links
		String urlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		String entriesUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRIES_READ_ROUTE);
		
		for (Blog blog : results)
		{
			String selfUrl = LinkUtils.formatUrl(urlPattern, Constants.BLOG_ID_PARAMETER, blog.getId());
			blog.addLink(new Link(RelTypes.SELF, selfUrl));

			// Add 'entries' link
			String entriesUrl = LinkUtils.formatUrl(entriesUrlPattern, Constants.BLOG_ID_PARAMETER, blog.getId());
			blog.addLink(new Link("http://www.pearson.com/pts/2012/blogging/entries", entriesUrl, "This Blog's Entries"));
		}

		return results;
	}

	public void update(Request request, Response response)
	{
		String id = request.getHeader(Constants.BLOG_ID_PARAMETER);
		Blog blog = request.getBodyAs(Blog.class, "Blog details not provided");
		
		if (!id.equals(blog.getId()))
		{
			throw new BadRequestException("ID in URL and ID in Blog must match");
		}
		
		ValidationEngine.validateAndThrow(blog);
		blogs.update(blog);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		blogs.delete(id);
		response.setResponseNoContent();
	}
}
