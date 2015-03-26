package org.restexpress.example.blogging.persistence;

import org.restexpress.example.blogging.domain.Blog;

import com.mongodb.MongoClient;

public class BlogRepository
extends BaseBloggingRepository<Blog>
{
	@SuppressWarnings("unchecked")
	public BlogRepository(MongoClient mongo, String databaseName)
	{
		super(mongo, databaseName, Blog.class);
	}
}
