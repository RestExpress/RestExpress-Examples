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
package org.restexpress.example.echo;

import java.util.Properties;

import org.restexpress.Format;
import org.restexpress.RestExpress;
import org.restexpress.example.echo.controller.EchoController;
import org.restexpress.example.echo.controller.StatusController;
import org.restexpress.example.echo.controller.SuccessController;
import org.restexpress.util.Environment;

/**
 * @author toddf
 * @since Feb 10, 2011
 */
public class Configuration
extends Environment
{
	private static final String NAME_PROPERTY = "name";
	private static final String PORT_PROPERTY = "port";
	private static final String DEFAULT_FORMAT_PROPERTY = "defaultFormat";
	private static final String WORKER_COUNT_PROPERTY = "workerCount";
	private static final String EXECUTOR_THREAD_COUNT_PROPERTY = "executorThreadCount";

	private static final int DEFAULT_WORKER_COUNT = 0;
	private static final int DEFAULT_EXECUTOR_THREAD_COUNT = 0;

	private int port;
	private String name;
	private String defaultFormat;
	private int workerCount;
	private int executorThreadCount;
	
	private EchoController echoController = new EchoController();
	private SuccessController successController = new SuccessController();
	private StatusController statusController = new StatusController();

	@Override
	protected void fillValues(Properties p)
	{
		this.name = p.getProperty(NAME_PROPERTY, RestExpress.DEFAULT_NAME);
		this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
		this.defaultFormat = p.getProperty(DEFAULT_FORMAT_PROPERTY, Format.JSON);
		this.workerCount = Integer.parseInt(p.getProperty(WORKER_COUNT_PROPERTY, String.valueOf(DEFAULT_WORKER_COUNT)));
		this.executorThreadCount = Integer.parseInt(p.getProperty(EXECUTOR_THREAD_COUNT_PROPERTY, String.valueOf(DEFAULT_EXECUTOR_THREAD_COUNT)));
	}

	public String getDefaultFormat()
	{
		return defaultFormat;
	}

	public int getPort()
	{
		return port;
	}

	public String getName()
	{
		return name;
	}
	
	public int getWorkerCount()
	{
		return workerCount;
	}
	
	public int getExecutorThreadCount()
	{
		return executorThreadCount;
	}
	
	public EchoController getEchoController()
	{
		return echoController;
	}

	public SuccessController getSuccessController()
    {
    	return successController;
    }

	public StatusController getStatusController()
    {
    	return statusController;
    }
}
