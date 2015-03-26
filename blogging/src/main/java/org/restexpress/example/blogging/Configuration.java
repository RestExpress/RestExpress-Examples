package org.restexpress.example.blogging;

import java.util.Properties;

import org.restexpress.RestExpress;
import org.restexpress.example.blogging.controller.BlogController;
import org.restexpress.example.blogging.controller.BlogEntryController;
import org.restexpress.example.blogging.controller.CommentController;
import org.restexpress.example.blogging.persistence.BlogEntryRepository;
import org.restexpress.example.blogging.persistence.BlogRepository;
import org.restexpress.example.blogging.persistence.CommentRepository;
import org.restexpress.util.Environment;

import com.strategicgains.repoexpress.mongodb.MongoConfig;
import com.strategicgains.restexpress.plugin.metrics.MetricsConfig;

public class Configuration
extends Environment
{
	private static final String DEFAULT_EXECUTOR_THREAD_POOL_SIZE = "20";

	private static final String PORT_PROPERTY = "port";
	private static final String BASE_URL_PROPERTY = "base.url";
	private static final String EXECUTOR_THREAD_POOL_SIZE = "executor.threadPool.size";

	private int port;
	private String baseUrl;
	private int executorThreadPoolSize;
	private MetricsConfig metricsSettings;

	private BlogEntryRepository blogEntryRepository;
	private CommentRepository commentRepository;

	private BlogController blogController;
	private BlogEntryController blogEntryController;
	private CommentController commentController;

	@Override
	protected void fillValues(Properties p)
	{
		this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
		this.baseUrl = p.getProperty(BASE_URL_PROPERTY, "http://localhost:" + String.valueOf(port));
		this.executorThreadPoolSize = Integer.parseInt(p.getProperty(EXECUTOR_THREAD_POOL_SIZE, DEFAULT_EXECUTOR_THREAD_POOL_SIZE));
		this.metricsSettings = new MetricsConfig(p);
		MongoConfig mongo = new MongoConfig(p);
		initialize(mongo);
	}

	private void initialize(MongoConfig mongo)
	{
		BlogRepository blogRepository = new BlogRepository(mongo.getClient(), mongo.getDbName());
		blogEntryRepository = new BlogEntryRepository(mongo.getClient(), mongo.getDbName());
		commentRepository = new CommentRepository(mongo.getClient(), mongo.getDbName());

		blogController = new BlogController(blogRepository);
		blogEntryController = new BlogEntryController(blogEntryRepository, blogRepository);
		commentController = new CommentController(commentRepository, blogEntryRepository, blogRepository);
	}
	
	
	// SECTION: ACCESSORS - PUBLIC

	public String getBaseUrl()
	{
		return baseUrl;
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
	
	public int getExecutorThreadPoolSize()
	{
		return executorThreadPoolSize;
	}

	public MetricsConfig getMetricsConfig()
    {
	    return metricsSettings;
    }

	public BlogEntryRepository getBlogEntryRepository()
    {
	    return blogEntryRepository;
    }

	public CommentRepository getCommentRepository()
    {
	    return commentRepository;
    }
}
