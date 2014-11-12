package com.kickstart.domain.event;

public class ObjectUpdatedEvent extends StateChangeEvent
{
	public Object after;

	public ObjectUpdatedEvent(Object before, Object after)
	{
		super("updated", before);
		this.after = after;
	}
}
