package com.kickstart.persistence;

import com.github.jmkgreen.morphia.query.Query;
import com.kickstart.domain.Comment;
import com.mongodb.Mongo;

public class CommentRepository
extends BaseBloggingRepository<Comment>
{
	@SuppressWarnings("unchecked")
	public CommentRepository(Mongo mongo, String databaseName)
	{
		super(mongo, databaseName, Comment.class);
	}

	public void deleteByBlogEntryId(String blogEntryId)
	{
		Query<Comment> comments = getDataStore().createQuery(Comment.class).field("blogEntryId").equal(blogEntryId);
		getDataStore().delete(comments);
	}

	public void deleteByBlogEntryIds(Iterable<String> blogEntryIds)
	{
		if (blogEntryIds.iterator().hasNext())
		{
			Query<Comment> comments = getDataStore().createQuery(Comment.class).field("blogEntryId").in(blogEntryIds);
			getDataStore().delete(comments);
		}
	}
}
