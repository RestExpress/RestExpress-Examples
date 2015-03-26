package org.restexpress.example.blogging.event;

import java.util.UUID;

import org.restexpress.example.blogging.domain.event.BlogEntryDeletedEvent;
import org.restexpress.example.blogging.persistence.CommentRepository;

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
		UUID blogEntryId = ((BlogEntryDeletedEvent) event).blogEntryId;
		
		// "Cascade-delete" the comments for this blog entry.
		comments.deleteByBlogEntryId(blogEntryId);
	}

	@Override
	public boolean handles(Class<?> type)
	{
		return BlogEntryDeletedEvent.class.isAssignableFrom(type);
	}
}
