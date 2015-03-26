package org.restexpress.example.blogging.domain.event;

import java.util.UUID;

import org.restexpress.example.blogging.domain.Blog;

public class BlogDeletedEvent
{
	public UUID blogId;

	public BlogDeletedEvent(Blog deleted)
	{
		this.blogId = deleted.getUuid();
	}
}
