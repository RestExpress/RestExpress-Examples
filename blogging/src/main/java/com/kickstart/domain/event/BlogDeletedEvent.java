package com.kickstart.domain.event;

import com.kickstart.domain.Blog;

public class BlogDeletedEvent
{
	public String blogId;

	public BlogDeletedEvent(Blog deleted)
	{
		this.blogId = deleted.getId();
	}
}
