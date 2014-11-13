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
		String message = request.getHeader("echo");
		return new DelayResponse("create", delayms, message);
	}

	public Object read(Request request, Response response)
	{
		long delayms = delay(request);
		String message = request.getHeader("echo");
		return new DelayResponse("read", delayms, message);
	}

	public Object update(Request request, Response response)
	{
		long delayms = delay(request);
		String message = request.getHeader("echo");
		return new DelayResponse("update", delayms, message);
	}

	public Object delete(Request request, Response response)
	{
		long delayms = delay(request);
		String message = request.getHeader("echo");
		return new DelayResponse("delete", delayms, message);
	}
}
