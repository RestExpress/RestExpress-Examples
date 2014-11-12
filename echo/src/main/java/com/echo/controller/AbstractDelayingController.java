/*
    Copyright 2012, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.echo.controller;

import org.restexpress.Request;
import org.restexpress.exception.BadRequestException;

/**
 * @author toddf
 * @since Jan 11, 2012
 */
public abstract class AbstractDelayingController
{
	private static final String TIMEOUT_MILLIS_HEADER = "delay_ms";

	protected long delay(Request request)
	{
		long millis = 0l;
		
		try
		{
			millis = Long.valueOf(request.getHeader(TIMEOUT_MILLIS_HEADER));
		}
		catch (NumberFormatException e)
		{
			throw new BadRequestException(e.getMessage());
		}

		if (millis == 0l) return 0l;

		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return millis;
	}

}
