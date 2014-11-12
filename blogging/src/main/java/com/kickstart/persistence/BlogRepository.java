package com.kickstart.persistence;

import com.kickstart.domain.Blog;
import com.mongodb.Mongo;

public class BlogRepository
extends BaseBloggingRepository<Blog>
{
	@SuppressWarnings("unchecked")
	public BlogRepository(Mongo mongo, String databaseName)
	{
		super(mongo, databaseName, Blog.class);
	}
}
