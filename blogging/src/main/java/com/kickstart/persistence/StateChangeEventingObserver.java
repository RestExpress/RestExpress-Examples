package com.kickstart.persistence;

import com.kickstart.domain.Blog;
import com.kickstart.domain.BlogEntry;
import com.kickstart.domain.event.BlogDeletedEvent;
import com.kickstart.domain.event.BlogEntryDeletedEvent;
import com.kickstart.domain.event.ObjectCreatedEvent;
import com.kickstart.domain.event.ObjectDeletedEvent;
import com.kickstart.domain.event.ObjectUpdatedEvent;
import com.strategicgains.eventing.DomainEvents;
import com.strategicgains.repoexpress.Repository;
import com.strategicgains.repoexpress.domain.TimestampedIdentifiable;
import com.strategicgains.repoexpress.event.AbstractRepositoryObserver;

public class StateChangeEventingObserver<T extends TimestampedIdentifiable>
extends AbstractRepositoryObserver<T>
{
	private Repository<T> repo;

	public StateChangeEventingObserver(Repository<T> repo)
	{
		super();
		this.repo = repo;
	}

	@Override
	public void afterCreate(T object)
	{
		DomainEvents.publish(new ObjectCreatedEvent(object));
	}

	@Override
	public void beforeDelete(T object)
	{
		DomainEvents.publish(new ObjectDeletedEvent(object));
		
		if (Blog.class.isAssignableFrom(object.getClass()))
		{
			DomainEvents.publish(new BlogDeletedEvent((Blog) object));
		}
		else if (BlogEntry.class.isAssignableFrom(object.getClass()))
		{
			DomainEvents.publish(new BlogEntryDeletedEvent((BlogEntry) object));
		}
	}

	@Override
	public void beforeUpdate(T object)
	{
		T previous = repo.read(object.getId());
		DomainEvents.publish(new ObjectUpdatedEvent(previous, object));
	}
}