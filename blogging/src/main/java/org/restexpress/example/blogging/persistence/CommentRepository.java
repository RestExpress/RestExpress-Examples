package org.restexpress.example.blogging.persistence;

import java.util.UUID;

import org.mongodb.morphia.query.Query;
import org.restexpress.example.blogging.domain.Comment;

import com.mongodb.MongoClient;
import com.strategicgains.repoexpress.domain.Identifier;

public class CommentRepository
extends BaseBloggingRepository<Comment>
{
	@SuppressWarnings("unchecked")
	public CommentRepository(MongoClient mongo, String databaseName)
	{
		super(mongo, databaseName, Comment.class);
	}

	public void deleteByBlogEntryId(UUID blogEntryId)
	{
		Query<Comment> comments = getDataStore().createQuery(Comment.class).field("blogEntryId").equal(blogEntryId);
		getDataStore().delete(comments);
	}

	public void deleteByBlogEntryIds(Iterable<Identifier> blogEntryIds)
	{
		if (blogEntryIds.iterator().hasNext())
		{
			Query<Comment> comments = getDataStore().createQuery(Comment.class).field("blogEntryId").in(blogEntryIds);
			getDataStore().delete(comments);
		}
	}
}
