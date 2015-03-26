package org.restexpress.example.blogging.domain.event;

import java.util.UUID;

import org.restexpress.example.blogging.domain.BlogEntry;

public class BlogEntryDeletedEvent
{
	public UUID blogEntryId;

	public BlogEntryDeletedEvent(BlogEntry deleted)
	{
		this.blogEntryId = deleted.getUuid();
	}
}
