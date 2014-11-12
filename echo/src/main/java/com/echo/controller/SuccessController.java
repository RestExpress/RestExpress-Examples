package com.echo.controller;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class SuccessController
extends AbstractDelayingController
{
	public Object create(Request request, Response response)
	{
		long delayms = delay(request);
		response.setResponseCreated();
		return new DelayResponse("create", delayms);
	}

	public Object read(Request request, Response response)
	{
		long delayms = delay(request);
		return new DelayResponse("read", delayms);
	}

	public Object update(Request request, Response response)
	{
		long delayms = delay(request);
		return new DelayResponse("udpate", delayms);
	}

	public Object delete(Request request, Response response)
	{
		long delayms = delay(request);
		return new DelayResponse("delete", delayms);
	}
}
