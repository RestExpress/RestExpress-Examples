package org.restexpress.example.blogging.persistence;

import java.util.UUID;

import org.mongodb.morphia.query.Query;
import org.restexpress.example.blogging.domain.BlogEntry;

import com.mongodb.MongoClient;
import com.strategicgains.repoexpress.domain.Identifier;
import com.strategicgains.repoexpress.util.IdentifiableIterable;

public class BlogEntryRepository
extends BaseBloggingRepository<BlogEntry>
{
	@SuppressWarnings("unchecked")
	public BlogEntryRepository(MongoClient mongo, String databaseName)
	{
		super(mongo, databaseName, BlogEntry.class);
	}

	public Iterable<Identifier> findIdsByBlogId(UUID blogId)
	{
		Query<BlogEntry> blogEntries = getDataStore().createQuery(BlogEntry.class).field("blogId").equal(blogId).retrievedFields(true, "_id");
		return new IdentifiableIterable(blogEntries.fetch());
	}

	public void deleteByBlogId(UUID blogId)
	{
		Query<BlogEntry> blogEntries = getDataStore().createQuery(BlogEntry.class).field("blogId").equal(blogId);
		getDataStore().delete(blogEntries);
	}
}
