package com.kickstart.domain.event;

public class StateChangeEvent
{
	public Object data;
	public String changeType;
	
	public StateChangeEvent(String changeType, Object data)
	{
		this.data = data;
		this.changeType = changeType;
	}
}
