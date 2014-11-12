package com.kickstart.event;

import com.kickstart.domain.event.BlogEntryDeletedEvent;
import com.kickstart.persistence.CommentRepository;
import com.strategicgains.eventing.EventHandler;

public class BlogEntryCascadeDeleteHandler
implements EventHandler
{
	private CommentRepository comments;
	
	public BlogEntryCascadeDeleteHandler(CommentRepository service)
	{
		this.comments = service;
	}

	@Override
	public void handle(Object event)
	throws Exception
	{
		System.out.println("Cascade-deleting a blog entry...");
		String blogEntryId = ((BlogEntryDeletedEvent) event).blogEntryId;
		
		// "Cascade-delete" the comments for this blog entry.
		comments.deleteByBlogEntryId(blogEntryId);
	}

	@Override
	public boolean handles(Class<?> type)
	{
		return BlogEntryDeletedEvent.class.isAssignableFrom(type);
	}
}
