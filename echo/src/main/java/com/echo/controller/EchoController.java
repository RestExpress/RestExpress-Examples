package com.echo.controller;

import io.netty.buffer.ByteBuf;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * @author toddf
 * @since Aug 31, 2010
 */
public class EchoController
extends AbstractDelayingController
{
    private static final String ECHO_PARAMETER_NOT_FOUND = "'echo' header or query-string parameter not found";
	private static final String ECHO_HEADER = "echo";

	public ByteBuf create(Request request, Response response)
	{
		delay(request);
		response.setResponseCreated();
		return request.getBody();
	}
	
	public String delete(Request request, Response response)
	{
		delay(request);
		return request.getHeader(ECHO_HEADER, ECHO_PARAMETER_NOT_FOUND);
	}
	
	public String read(Request request, Response response)
	{
		delay(request);
		String echo = request.getHeader(ECHO_HEADER);
		
		if (echo == null)
		{
			return "Please set query-string parameter 'echo' (e.g. ?echo=value)";
		}
		
		return echo;
	}

	public ByteBuf update(Request request, Response response)
	{
		delay(request);
		return request.getBody();
	}
}
