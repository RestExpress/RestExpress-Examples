package org.restexpress.example.blogging;

import static io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT;
import static io.netty.handler.codec.http.HttpHeaders.Names.AUTHORIZATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.REFERER;
import static org.restexpress.Flags.Auth.PUBLIC_ROUTE;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.restexpress.Flags;
import org.restexpress.RestExpress;
import org.restexpress.example.blogging.event.BlogCascadeDeleteHandler;
import org.restexpress.example.blogging.event.BlogEntryCascadeDeleteHandler;
import org.restexpress.example.blogging.postprocessor.LastModifiedHeaderPostprocessor;
import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.ConflictException;
import org.restexpress.exception.NotFoundException;
import org.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import org.restexpress.plugin.hyperexpress.HyperExpressPlugin;
import org.restexpress.plugin.hyperexpress.Linkable;
import org.restexpress.util.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.strategicgains.eventing.DomainEvents;
import com.strategicgains.eventing.EventBus;
import com.strategicgains.eventing.local.LocalEventBusBuilder;
import com.strategicgains.repoexpress.exception.DuplicateItemException;
import com.strategicgains.repoexpress.exception.InvalidObjectIdException;
import com.strategicgains.repoexpress.exception.ItemNotFoundException;
import com.strategicgains.restexpress.plugin.cache.CacheControlPlugin;
import com.strategicgains.restexpress.plugin.cors.CorsHeaderPlugin;
import com.strategicgains.restexpress.plugin.metrics.MetricsConfig;
import com.strategicgains.restexpress.plugin.metrics.MetricsPlugin;
import com.strategicgains.restexpress.plugin.swagger.SwaggerPlugin;
import com.strategicgains.syntaxe.ValidationException;

public class Main
{
	private static final String SERVICE_NAME = "Blogging Example";
	private static final Logger LOG = LoggerFactory.getLogger(SERVICE_NAME);

	public static void main(String[] args) throws Exception
	{
		Configuration config = Environment.load(args, Configuration.class);
		RestExpress server = new RestExpress()
		    .setName(SERVICE_NAME)
		    .setBaseUrl(config.getBaseUrl())
		    .setExecutorThreadCount(config.getExecutorThreadPoolSize())
		    .addPostprocessor(new LastModifiedHeaderPostprocessor())
		    .addMessageObserver(new SimpleConsoleLogMessageObserver());

		Routes.define(config, server);
		Relationships.define(server);
		configurePlugins(config, server);
		mapExceptions(server);
		registerDomainEvents(server, config);
		server.bind(config.getPort());
		server.awaitShutdown();
	}

	private static void configurePlugins(Configuration config, RestExpress server)
    {
	    configureMetrics(config, server);

		new SwaggerPlugin()
			.flag(Flags.Auth.PUBLIC_ROUTE)
			.register(server);

		new CacheControlPlugin()							// Support caching headers.
				.register(server);

		new HyperExpressPlugin(Linkable.class)
			.register(server);

		new CorsHeaderPlugin("*")
			.flag(PUBLIC_ROUTE)
		    .allowHeaders(CONTENT_TYPE, ACCEPT, AUTHORIZATION, REFERER, LOCATION)
		    .exposeHeaders(LOCATION)
		    .register(server);
    }

	private static void configureMetrics(Configuration config, RestExpress server)
    {
		MetricsConfig mc = config.getMetricsConfig();

	    if (mc.isEnabled())
		{
	    	MetricRegistry registry = new MetricRegistry();
			new MetricsPlugin(registry)
				.register(server);

			if (mc.isGraphiteEnabled())
			{
				final Graphite graphite = new Graphite(new InetSocketAddress(mc.getGraphiteHost(), mc.getGraphitePort()));
				final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
					.prefixedWith(mc.getPrefix())
					.convertRatesTo(TimeUnit.SECONDS)
					.convertDurationsTo(TimeUnit.MILLISECONDS)
					.filter(MetricFilter.ALL)
					.build(graphite);
				reporter.start(mc.getPublishSeconds(), TimeUnit.SECONDS);
			}
			else
			{
				LOG.warn("*** Graphite Metrics Publishing is Disabled ***");
			}
		}
		else
		{
			LOG.warn("*** Metrics Generation is Disabled ***");
		}
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
	    	.mapException(org.mongodb.morphia.query.ValidationException.class, BadRequestException.class);
    }

	private static void registerDomainEvents(RestExpress server, Configuration config)
	{
		EventBus localBus = new LocalEventBusBuilder()
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
}
