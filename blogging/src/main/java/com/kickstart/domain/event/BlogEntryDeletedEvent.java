package com.kickstart.domain.event;

import com.kickstart.domain.BlogEntry;

public class BlogEntryDeletedEvent
{
	public String blogEntryId;

	public BlogEntryDeletedEvent(BlogEntry deleted)
	{
		this.blogEntryId = deleted.getId();
	}
}
