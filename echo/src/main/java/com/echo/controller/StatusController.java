package com.echo.controller;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class StatusController
extends AbstractDelayingController
{
	private static final String STATUS_RESPONSE_HEADER = "http_response_code";

	public Object create(Request request, Response response)
	{
		long delayms = delay(request);
		int status = Integer.valueOf(request.getHeader(STATUS_RESPONSE_HEADER));
		response.setResponseCode(status);
		return new DelayResponse("create", delayms);
	}

	public Object read(Request request, Response response)
	{
		long delayms = delay(request);
		int status = Integer.valueOf(request.getHeader(STATUS_RESPONSE_HEADER));
		response.setResponseCode(status);
		return new DelayResponse("read", delayms);
	}

	public Object update(Request request, Response response)
	{
		long delayms = delay(request);
		int status = Integer.valueOf(request.getHeader(STATUS_RESPONSE_HEADER));
		response.setResponseCode(status);
		return new DelayResponse("update", delayms);
	}

	public Object delete(Request request, Response response)
	{
		long delayms = delay(request);
		int status = Integer.valueOf(request.getHeader(STATUS_RESPONSE_HEADER));
		response.setResponseCode(status);
		return new DelayResponse("delete", delayms);
	}
}
