package org.restexpress.example.blogging.controller;

import static com.strategicgains.repoexpress.adapter.Identifiers.UUID;
import io.netty.handler.codec.http.HttpMethod;

import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.query.FilterOperator;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.restexpress.example.blogging.Constants;
import org.restexpress.example.blogging.domain.Blog;
import org.restexpress.example.blogging.domain.BlogEntry;
import org.restexpress.example.blogging.persistence.BlogEntryRepository;
import org.restexpress.example.blogging.persistence.BlogRepository;
import org.restexpress.query.QueryFilters;
import org.restexpress.query.QueryOrders;
import org.restexpress.query.QueryRanges;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.builder.TokenBinder;
import com.strategicgains.hyperexpress.builder.TokenResolver;
import com.strategicgains.hyperexpress.builder.UrlBuilder;
import com.strategicgains.repoexpress.util.UuidConverter;
import com.strategicgains.syntaxe.ValidationEngine;

public class BlogEntryController
{
	private static final UrlBuilder LOCATION_BUILDER = new UrlBuilder();
	private BlogEntryRepository blogEntries;
	private BlogRepository blogs;
	
	public BlogEntryController(BlogEntryRepository blogEntryRepository, BlogRepository blogRepository)
	{
		super();
		this.blogEntries = blogEntryRepository;
		this.blogs = blogRepository;
	}

	public BlogEntry create(Request request, Response response)
	{
		String blogId = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "No Blog ID provided");
		BlogEntry blogEntry = request.getBodyAs(BlogEntry.class, "BlogEntry details not provided");
		Blog blog = blogs.read(UUID.parse(blogId));
		blogEntry.setBlogId(blog.getUuid());
		ValidationEngine.validateAndThrow(blogEntry);
		BlogEntry saved = blogEntries.create(blogEntry);

		// Construct the response for create...
		response.setResponseCreated();

		// Bind the resource with link URL tokens, etc. here...
		TokenResolver resolver = HyperExpress.bind(Constants.Url.BLOG_ENTRY_ID_PARAMETER, UUID.format(saved.getUuid()))
			.bind(Constants.Url.BLOG_ID_PARAMETER, UUID.format(saved.getBlogId()));

		// Include the Location header...
		String locationPattern = request.getNamedUrl(HttpMethod.GET, Constants.Routes.BLOG_ENTRY_READ_ROUTE);
		response.addLocationHeader(LOCATION_BUILDER.build(locationPattern, resolver));

		// Return the newly-created item...
		return saved;
	}

	public BlogEntry read(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER, "No BlogEntry ID supplied");
		BlogEntry entity = blogEntries.read(UUID.parse(id));

		// enrich the resource with links, etc. here...
		HyperExpress.bind(Constants.Url.BLOG_ENTRY_ID_PARAMETER, UUID.format(entity.getUuid()))
			.bind(Constants.Url.BLOG_ID_PARAMETER, UUID.format(entity.getBlogId()));

		return entity;
	}

	public List<BlogEntry> readAll(Request request, Response response)
	{
		String blogId = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "Blog ID not provided");
		QueryFilter filter = QueryFilters.parseFrom(request);
		QueryOrder order = QueryOrders.parseFrom(request);
		QueryRange range = QueryRanges.parseFrom(request, 20);

		filter.addCriteria("blogId", FilterOperator.EQUALS, UuidConverter.parse(blogId));
		List<BlogEntry> results = blogEntries.readAll(filter, range, order);
		response.setCollectionResponse(range, results.size(), blogEntries.count(filter));

		// Bind the resources in the collection with link URL tokens, etc. here...
		HyperExpress.tokenBinder(new TokenBinder<BlogEntry>()
		{
			@Override
			public void bind(BlogEntry entity, TokenResolver resolver)
			{
				resolver.bind(Constants.Url.BLOG_ENTRY_ID_PARAMETER, UUID.format(entity.getUuid()))
				.bind(Constants.Url.BLOG_ID_PARAMETER, UUID.format(entity.getBlogId()));
			}
		});

		return results;
	}

	public void update(Request request, Response response)
	{
		String blogId = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "Blog ID not provided");
		String id = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER);
		BlogEntry blogEntry = request.getBodyAs(BlogEntry.class, "BlogEntry details not provided");

		// Cannot change the blog, blog entry IDs via update.
		Blog blog = blogs.read(UUID.parse(blogId));
		blogEntry.setBlogId(blog.getUuid());
		blogEntry.setId(UUID.parse(id));

		ValidationEngine.validateAndThrow(blogEntry);
		blogEntries.update(blogEntry);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER, "No BlogEntry ID supplied");
		blogEntries.delete(UUID.parse(id));
		response.setResponseNoContent();
	}
}
