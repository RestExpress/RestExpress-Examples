package com.kickstart.persistence;

import com.github.jmkgreen.morphia.query.Query;
import com.kickstart.domain.BlogEntry;
import com.mongodb.Mongo;
import com.strategicgains.repoexpress.util.IdentifiableIterable;

public class BlogEntryRepository
extends BaseBloggingRepository<BlogEntry>
{
	@SuppressWarnings("unchecked")
	public BlogEntryRepository(Mongo mongo, String databaseName)
	{
		super(mongo, databaseName, BlogEntry.class);
	}

	public Iterable<String> findIdsByBlogId(String blogId)
	{
		Query<BlogEntry> blogEntries = getDataStore().createQuery(BlogEntry.class).field("blogId").equal(blogId).retrievedFields(true, "_id");
		return new IdentifiableIterable(blogEntries.fetch());		
	}

	public void deleteByBlogId(String blogId)
	{
		Query<BlogEntry> blogEntries = getDataStore().createQuery(BlogEntry.class).field("blogId").equal(blogId);
		getDataStore().delete(blogEntries);
	}
}
