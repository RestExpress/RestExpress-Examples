/*
    Copyright 2011, Strategic Gains, Inc.

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
package org.restexpress.example.echo.controller;

/**
 * @author toddf
 * @since Dec 20, 2011
 */
public class DelayResponse
{
	@SuppressWarnings("unused")
    private String action;
	@SuppressWarnings("unused")
    private long delayMs;
	@SuppressWarnings("unused")
	private String message;

	public DelayResponse(String action, long delayMs, String message)
    {
	    super();
	    this.action = action;
	    this.delayMs = delayMs;
	    this.message = message;
    }
}
