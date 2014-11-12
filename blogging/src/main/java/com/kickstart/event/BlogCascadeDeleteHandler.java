package com.kickstart.event;

import com.kickstart.domain.event.BlogDeletedEvent;
import com.kickstart.persistence.BlogEntryRepository;
import com.kickstart.persistence.CommentRepository;
import com.strategicgains.eventing.EventHandler;

public class BlogCascadeDeleteHandler
implements EventHandler
{
	private BlogEntryRepository blogEntries;
	private CommentRepository comments;

	public BlogCascadeDeleteHandler(BlogEntryRepository blogEntryRepo, CommentRepository commentRepo)
	{
		this.blogEntries = blogEntryRepo;
		this.comments = commentRepo;
	}

	@Override
	public void handle(Object event)
	throws Exception
	{
		System.out.println("Cascade-deleting blog...");
		String blogId = ((BlogDeletedEvent) event).blogId;

		// Delete the comments for every blog entry within this blog.
		comments.deleteByBlogEntryIds(blogEntries.findIdsByBlogId(blogId));

		// Now delete all the blog entries in this blog.
		blogEntries.deleteByBlogId(blogId);
	}

	@Override
	public boolean handles(Class<?> type)
	{
		return BlogDeletedEvent.class.isAssignableFrom(type);
	}
}
