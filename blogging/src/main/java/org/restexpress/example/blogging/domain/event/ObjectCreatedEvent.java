package org.restexpress.example.blogging.domain.event;

public class ObjectCreatedEvent extends StateChangeEvent
{
	public ObjectCreatedEvent(Object data)
	{
		super("created", data);
	}
}
