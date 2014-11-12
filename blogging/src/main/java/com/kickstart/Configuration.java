package com.kickstart;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kickstart.controller.BlogController;
import com.kickstart.controller.BlogEntryController;
import com.kickstart.controller.CommentController;
import com.kickstart.persistence.BlogEntryRepository;
import com.kickstart.persistence.BlogRepository;
import com.kickstart.persistence.CommentRepository;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.exception.ConfigurationException;
import com.strategicgains.restexpress.util.Environment;

public class Configuration
extends Environment
{
	private static final String PORT_PROPERTY = "port";
	private static final String DEFAULT_FORMAT_PROPERTY = "default.Format";
	private static final String MONGODB_BOOTSTRAPS_PROPERTY = "mongodb.bootstraps";
	private static final String MONGODB_DATABASE_PROPERTY = "mongodb.database";
	private static final String MONGODB_USERNAME_PROPERTY = "mongodb.user";
	private static final String MONGODB_PASSWORD_PROPERTY = "mongodb.password";
	private static final String MONGODB_CONNECTIONS_PER_HOST_PROPERTY = "mongodb.connectionsPerHost";
	private static final String BASE_URL_PROPERTY = "base.url";
	private static final String EXECUTOR_THREAD_POOL_PROPERTY = "thread.pool.size";

	private int port;
	private String defaultFormat;
	private String baseUrl;
	private int executorThreadPoolSize;

	private BlogRepository blogRepository;
	private BlogEntryRepository blogEntryRepository;
	private CommentRepository commentRepository;

	private BlogController blogController;
	private BlogEntryController blogEntryController;
	private CommentController commentController;

	@Override
	protected void fillValues(Properties p)
	{
		this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
		this.defaultFormat = p.getProperty(DEFAULT_FORMAT_PROPERTY, Format.JSON);
		this.baseUrl = p.getProperty(BASE_URL_PROPERTY, "http://localhost:" + String.valueOf(port));
		this.executorThreadPoolSize = Integer.parseInt(p.getProperty(EXECUTOR_THREAD_POOL_PROPERTY, "100"));
		String dbName = p.getProperty(MONGODB_DATABASE_PROPERTY);

		if (dbName == null)
		{
			throw new ConfigurationException("Please define a database name for property: " + MONGODB_DATABASE_PROPERTY);
		}

		String dbUser = p.getProperty(MONGODB_USERNAME_PROPERTY);
		String dbPassword = p.getProperty(MONGODB_PASSWORD_PROPERTY);
		int connectionsPerHost = Integer.parseInt(p.getProperty(MONGODB_CONNECTIONS_PER_HOST_PROPERTY, "100"));
		List<ServerAddress> bootstraps = null;

		try
		{
			String bootstrapString = p.getProperty(MONGODB_BOOTSTRAPS_PROPERTY);
			bootstraps = parseBootstrapString(bootstrapString);
		}
		catch (Exception e)
		{
			throw new ConfigurationException(e);
		}

		MongoClientOptions options = new MongoClientOptions.Builder()
			.connectionsPerHost(connectionsPerHost)
			.socketKeepAlive(true)
			.build();
		MongoClient mongo = new MongoClient(bootstraps, options);
		initialize(mongo, dbName, dbUser, dbPassword);
	}

	private void initialize(Mongo mongo, String dbName, String dbUser, String dbPassword)
	{
		if (dbUser != null && dbPassword != null && dbPassword.length() > 0)
		{
			if (!mongo.getDB(dbName).authenticate(dbUser, dbPassword.toCharArray()))
			{
				throw new ConfigurationException("User not authenticated against database: " + dbName);
			}
		}

		blogRepository = new BlogRepository(mongo, dbName);
		blogEntryRepository = new BlogEntryRepository(mongo, dbName);
		commentRepository = new CommentRepository(mongo, dbName);

		blogController = new BlogController(blogRepository);
		blogEntryController = new BlogEntryController(blogEntryRepository);
		commentController = new CommentController(commentRepository);
	}

	/**
	 * @param bootstrapString
	 * @return
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 */
	private List<ServerAddress> parseBootstrapString(String bootstrapString)
	throws NumberFormatException,
	    UnknownHostException
	{
		if (bootstrapString == null || bootstrapString.trim().isEmpty())
		{
			throw new ConfigurationException("Please set MongoDB bootstrap servers in property: " + MONGODB_BOOTSTRAPS_PROPERTY);
		}

		String[] serverAndPorts = bootstrapString.split(",");
		List<ServerAddress> results = new ArrayList<ServerAddress>(serverAndPorts.length);

		for (String sp : serverAndPorts)
		{
			String[] server = sp.split(":");

			if (server.length == 2)
			{
				results.add(new ServerAddress(server[0], Integer.parseInt(server[1])));
			}
			else if (server.length == 1)
			{
				results.add(new ServerAddress(server[0]));
			}
		}

		return results;
	}
	
	
	// SECTION: ACCESSORS - PUBLIC

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public String getDefaultFormat()
	{
		return defaultFormat;
	}

	public int getPort()
	{
		return port;
	}

	public BlogController getBlogController()
	{
		return blogController;
	}
	
	public BlogEntryController getBlogEntryController()
	{
		return blogEntryController;
	}
	
	public CommentController getCommentController()
	{
		return commentController;
	}

	public BlogRepository getBlogRepository()
	{
		return blogRepository;
	}

	public BlogEntryRepository getBlogEntryRepository()
	{
		return blogEntryRepository;
	}

	public CommentRepository getCommentRepository()
	{
		return commentRepository;
	}
	
	public int getExecutorThreadPoolSize()
	{
		return executorThreadPoolSize;
	}
}
