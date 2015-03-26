package org.restexpress.example.blogging.persistence;

import com.mongodb.MongoClient;
import com.strategicgains.repoexpress.mongodb.AbstractUuidMongodbEntity;
import com.strategicgains.repoexpress.mongodb.MongodbUuidEntityRepository;

public class BaseBloggingRepository<T extends AbstractUuidMongodbEntity>
extends MongodbUuidEntityRepository<T>
{
	public BaseBloggingRepository(MongoClient mongo, String databaseName, Class<T>... types)
	{
		super(mongo, databaseName, types);
	}
	
	@Override
	protected void initializeObservers()
	{
		super.initializeObservers();
		addObserver(new StateChangeEventingObserver<T>(this));
	}
}
