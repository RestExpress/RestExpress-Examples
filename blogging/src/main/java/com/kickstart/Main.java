package com.kickstart;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.event.BlogCascadeDeleteHandler;
import com.kickstart.event.BlogEntryCascadeDeleteHandler;
import com.kickstart.postprocessor.LastModifiedHeaderPostprocessor;
import com.kickstart.serialization.ResponseProcessors;
import com.strategicgains.eventing.DomainEvents;
import com.strategicgains.eventing.EventBus;
import com.strategicgains.eventing.local.LocalEventBusBuilder;
import com.strategicgains.repoexpress.exception.DuplicateItemException;
import com.strategicgains.repoexpress.exception.InvalidObjectIdException;
import com.strategicgains.repoexpress.exception.ItemNotFoundException;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.Parameters;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.exception.ConflictException;
import com.strategicgains.restexpress.exception.NotFoundException;
import com.strategicgains.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import com.strategicgains.restexpress.plugin.cache.CacheControlPlugin;
import com.strategicgains.restexpress.plugin.route.RoutesMetadataPlugin;
import com.strategicgains.restexpress.util.Environment;
import com.strategicgains.syntaxe.ValidationException;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Configuration config = loadEnvironment(args);
		RestExpress server = new RestExpress()
		    .setName("Sample Blogging")
		    .setBaseUrl(config.getBaseUrl())
		    .setDefaultFormat(config.getDefaultFormat())
		    .setExecutorThreadCount(config.getExecutorThreadPoolSize())
		    .putResponseProcessor(Format.JSON, ResponseProcessors.json())
		    .putResponseProcessor(Format.XML, ResponseProcessors.xml())
		    .putResponseProcessor(Format.WRAPPED_JSON, ResponseProcessors.wrappedJson())
		    .putResponseProcessor(Format.WRAPPED_XML, ResponseProcessors.wrappedXml())
		    .addPostprocessor(new LastModifiedHeaderPostprocessor())
		    .addMessageObserver(new SimpleConsoleLogMessageObserver());

		defineRoutes(config, server);

		new RoutesMetadataPlugin()							// Support basic discoverability.
			.register(server)
			.parameter(Parameters.Cache.MAX_AGE, 86400);	// Cache for 1 day (24 hours).

		new CacheControlPlugin()							// Support caching headers.
			.register(server);

		mapExceptions(server);
		registerDomainEvents(server, config);
		server.bind(config.getPort());
		server.awaitShutdown();
	}

	private static void defineRoutes(Configuration config, RestExpress server)
	{
		server.uri("/blogs.{format}", config.getBlogController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST);

		server.uri("/blogs/{blogId}.{format}", config.getBlogController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.BLOG_READ_ROUTE);

		server.uri("/blogs/{blogId}/entries.{format}", config.getBlogEntryController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name(Constants.BLOG_ENTRIES_READ_ROUTE);

		server.uri("/blogs/{blogId}/entries/{entryId}.{format}", config.getBlogEntryController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.BLOG_ENTRY_READ_ROUTE);

		server.uri("/blogs/{blogId}/entries/{entryId}/comments.{format}", config.getCommentController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name(Constants.COMMENTS_READ_ROUTE);

		server.uri("/blogs/{blogId}/entries/{entryId}/comments/{commentId}.{format}", config.getCommentController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.COMMENT_READ_ROUTE);
	}

	/**
     * @param server
     */
    private static void mapExceptions(RestExpress server)
    {
    	server
	    	.mapException(ItemNotFoundException.class, NotFoundException.class)
	    	.mapException(DuplicateItemException.class, ConflictException.class)
	    	.mapException(ValidationException.class, BadRequestException.class)
	    	.mapException(InvalidObjectIdException.class, NotFoundException.class)
	    	.mapException(com.github.jmkgreen.morphia.query.ValidationException.class, BadRequestException.class);
    }

	private static void registerDomainEvents(RestExpress server, Configuration config)
	{
		EventBus localBus = new LocalEventBusBuilder()
//			.addPublishalbeEventType(BlogDeletedEvent.class)
//			.addPublishalbeEventType(BlogEntryDeletedEvent.class)
//			.addPublishalbeEventType(CommentDeletedEvent.class)
			.subscribe(new BlogCascadeDeleteHandler(config.getBlogEntryRepository(), config.getCommentRepository()))
			.subscribe(new BlogEntryCascadeDeleteHandler(config.getCommentRepository()))
			.build();
		DomainEvents.addBus("local", localBus);

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				DomainEvents.shutdown();
			}
		});
	}

	private static Configuration loadEnvironment(String[] args)
    throws FileNotFoundException, IOException
    {
	    if (args.length > 0)
		{
			return Environment.from(args[0], Configuration.class);
		}

	    return Environment.fromDefault(Configuration.class);
    }
}
