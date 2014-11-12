package com.kickstart.domain.event;

public class ObjectDeletedEvent extends StateChangeEvent
{
	public ObjectDeletedEvent(Object data)
	{
		super("deleted", data);
	}
}
