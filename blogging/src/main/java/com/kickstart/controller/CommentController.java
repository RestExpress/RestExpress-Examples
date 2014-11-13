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
import com.kickstart.domain.Comment;
import com.kickstart.persistence.CommentRepository;
import com.strategicgains.hyperexpress.RelTypes;
import com.strategicgains.hyperexpress.domain.Link;
import com.strategicgains.syntaxe.ValidationEngine;

public class CommentController
{
	private CommentRepository comments;
	
	public CommentController(CommentRepository commentRepository)
	{
		super();
		this.comments = commentRepository;
	}

	public Comment create(Request request, Response response)
	{
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		String blogId = request.getHeader(Constants.BLOG_ID_PARAMETER, "Blog ID not provided");
		String blogEntryId = request.getHeader(Constants.BLOG_ENTRY_ID_PARAMETER, "Blog Entry ID not provided");
		comment.setBlogEntryId(blogEntryId);
		ValidationEngine.validateAndThrow(comment);
		Comment saved = comments.create(comment);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		response.addLocationHeader(LinkUtils.formatUrl(locationUrl,
				Constants.COMMENT_ID_PARAMETER, saved.getId(),
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, blogEntryId));

		// Return the newly-created item...
		return saved;
	}

	public Comment read(Request request, Response response)
	{
		String id = request.getHeader(Constants.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		String blogId = request.getHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		Comment result = comments.read(id);

		// Add 'self' link
		String selfUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		String selfUrl = LinkUtils.formatUrl(selfUrlPattern,
				Constants.COMMENT_ID_PARAMETER, result.getId(),
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, result.getBlogEntryId());
		result.addLink(new Link(RelTypes.SELF, selfUrl));

		String parentUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRY_READ_ROUTE);
		String parentUrl = LinkUtils.formatUrl(parentUrlPattern,
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, result.getBlogEntryId());
		result.addLink(new Link(RelTypes.UP, parentUrl, "The Parent Blog-Entry"));

		return result;
	}

	public List<Comment> readAll(Request request, Response response)
	{
		String blogId = request.getHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		String blogEntryId = request.getHeader(Constants.BLOG_ENTRY_ID_PARAMETER, "No Blog Entry ID supplied");
		QueryFilter filter = QueryFilters.parseFrom(request);
		QueryOrder order = QueryOrders.parseFrom(request);
		QueryRange range = QueryRanges.parseFrom(request, 20);
		
		filter.addCriteria("blogEntryId", blogEntryId);
		List<Comment> results = comments.readAll(filter, range, order);
		response.setCollectionResponse(range, results.size(), comments.count(filter));
		
		// Add 'self' and 'parent' links
		String selfUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		
		for (Comment comment : results)
		{
			String selfUrl = LinkUtils.formatUrl(selfUrlPattern,
				Constants.COMMENT_ID_PARAMETER, comment.getId(),
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, comment.getBlogEntryId());
			comment.addLink(new Link(RelTypes.SELF, selfUrl));
		}

		// Add 'parent' link to the collection
		LinkableCollection<Comment> wrapper = new LinkableCollection<Comment>(results);
		String parentUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRY_READ_ROUTE);
		String parentUrl = LinkUtils.formatUrl(parentUrlPattern,
			Constants.BLOG_ID_PARAMETER, blogId,
			Constants.BLOG_ENTRY_ID_PARAMETER, blogEntryId);
		wrapper.addLink(new Link(RelTypes.UP, parentUrl, "The Parent Blog-Entry"));
		return wrapper;
	}

	public void update(Request request, Response response)
	{
		String id = request.getHeader(Constants.COMMENT_ID_PARAMETER);
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		
		if (!id.equals(comment.getId()))
		{
			throw new BadRequestException("ID in URL and ID in Comment must match");
		}
		
		ValidationEngine.validateAndThrow(comment);
		comments.update(comment);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getHeader(Constants.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		comments.delete(id);
		response.setResponseNoContent();
	}
}
