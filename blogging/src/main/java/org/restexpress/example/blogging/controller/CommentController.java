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
import org.restexpress.example.blogging.domain.Comment;
import org.restexpress.example.blogging.persistence.BlogEntryRepository;
import org.restexpress.example.blogging.persistence.BlogRepository;
import org.restexpress.example.blogging.persistence.CommentRepository;
import org.restexpress.query.QueryFilters;
import org.restexpress.query.QueryOrders;
import org.restexpress.query.QueryRanges;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.builder.TokenBinder;
import com.strategicgains.hyperexpress.builder.TokenResolver;
import com.strategicgains.hyperexpress.builder.UrlBuilder;
import com.strategicgains.repoexpress.util.UuidConverter;
import com.strategicgains.syntaxe.ValidationEngine;

public class CommentController
{
	private static final UrlBuilder LOCATION_BUILDER = new UrlBuilder();
	private CommentRepository comments;
	private BlogEntryRepository entries;
	private BlogRepository blogs;
	
	public CommentController(CommentRepository commentRepository, BlogEntryRepository blogEntryRepository, BlogRepository blogRepository)
	{
		super();
		this.comments = commentRepository;
		this.entries = blogEntryRepository;
		this.blogs = blogRepository;
	}

	public Comment create(Request request, Response response)
	{
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		String blogId = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "Blog ID not provided");
		String blogEntryId = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER, "Blog Entry ID not provided");
		Blog blog = blogs.read(UUID.parse(blogId));
		BlogEntry entry = entries.read(UUID.parse(blogEntryId));
		comment.setBlogEntryId(entry.getUuid());
		ValidationEngine.validateAndThrow(comment);
		Comment saved = comments.create(comment);

		// Construct the response for create...
		response.setResponseCreated();

		// Bind the resource with link URL tokens, etc. here...
		TokenResolver resolver = HyperExpress.bind(Constants.Url.COMMENT_ID_PARAMETER, UUID.format(saved.getUuid()))
			.bind(Constants.Url.BLOG_ID_PARAMETER, UUID.format(blog.getUuid()))
			.bind(Constants.Url.BLOG_ENTRY_ID_PARAMETER, UUID.format(saved.getBlogEntryId()));

		// Include the Location header...
		String locationPattern = request.getNamedUrl(HttpMethod.GET, Constants.Routes.COMMENT_READ_ROUTE);
		response.addLocationHeader(LOCATION_BUILDER.build(locationPattern, resolver));

		// Return the newly-created item...
		return saved;
	}

	public Comment read(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		String blogId = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "Blog ID not provided");
		String blogEntryId = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER, "Blog Entry ID not provided");
		Blog blog = blogs.read(UUID.parse(blogId));
		entries.read(UUID.parse(blogEntryId));
		Comment entity = comments.read(UUID.parse(id));

		// Bind the resource with link URL tokens, etc. here...
		HyperExpress.bind(Constants.Url.COMMENT_ID_PARAMETER, UUID.format(entity.getUuid()))
			.bind(Constants.Url.BLOG_ID_PARAMETER, UUID.format(blog.getUuid()))
			.bind(Constants.Url.BLOG_ENTRY_ID_PARAMETER, UUID.format(entity.getBlogEntryId()));

		return entity;
	}

	public List<Comment> readAll(Request request, Response response)
	{
		String blogId = request.getHeader(Constants.Url.BLOG_ID_PARAMETER, "No Blog ID supplied");
		String blogEntryId = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER, "No Blog Entry ID supplied");
		final Blog blog = blogs.read(UUID.parse(blogId));
		entries.read(UUID.parse(blogEntryId));

		QueryFilter filter = QueryFilters.parseFrom(request);
		QueryOrder order = QueryOrders.parseFrom(request);
		QueryRange range = QueryRanges.parseFrom(request, 20);
		
		filter.addCriteria("blogEntryId", FilterOperator.EQUALS, UuidConverter.parse(blogEntryId));
		List<Comment> entities = comments.readAll(filter, range, order);
		response.setCollectionResponse(range, entities.size(), comments.count(filter));

		// Bind the resources in the collection with link URL tokens, etc. here...
		HyperExpress.tokenBinder(new TokenBinder<Comment>()
		{
			@Override
			public void bind(Comment entity, TokenResolver resolver)
			{
				resolver.bind(Constants.Url.COMMENT_ID_PARAMETER, UUID.format(entity.getUuid()))
					.bind(Constants.Url.BLOG_ID_PARAMETER, UUID.format(blog.getUuid()))
					.bind(Constants.Url.BLOG_ENTRY_ID_PARAMETER, UUID.format(entity.getBlogEntryId()));
			}
		});

		return entities;
	}

	public void update(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		String blogEntryId = request.getHeader(Constants.Url.BLOG_ENTRY_ID_PARAMETER, "Blog Entry ID not provided");
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		BlogEntry entry = entries.read(UUID.parse(blogEntryId));

		// Cannot change entry, comment IDs on update.
		comment.setId(UUID.parse(id));
		comment.setBlogEntryId(entry.getUuid());
		ValidationEngine.validateAndThrow(comment);
		comments.update(comment);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getHeader(Constants.Url.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		comments.delete(UUID.parse(id));
		response.setResponseNoContent();
	}
}
