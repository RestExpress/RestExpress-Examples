package com.kickstart.persistence;

import com.mongodb.Mongo;
import com.strategicgains.repoexpress.mongodb.AbstractMongodbEntity;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;

public class BaseBloggingRepository<T extends AbstractMongodbEntity>
extends MongodbEntityRepository<T>
{
	public BaseBloggingRepository(Mongo mongo, String databaseName, Class<T>... types)
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
