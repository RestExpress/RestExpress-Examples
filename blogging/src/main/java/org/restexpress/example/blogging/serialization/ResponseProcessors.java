package org.restexpress.example.blogging.serialization;

import org.restexpress.response.ErrorResponseWrapper;
import org.restexpress.response.ResponseProcessor;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.serialization.SerializationProcessor;

/**
 * A factory to create ResponseProcessors for serialization and wrapping of responses.
 * 
 * @author toddf
 * @since May 15, 2012
 */
public class ResponseProcessors
{
	// SECTION: CONSTANTS

	private static final SerializationProcessor JSON_SERIALIZER = new JsonSerializationProcessor();
	private static final SerializationProcessor XML_SERIALIZER = new XmlSerializationProcessor();
	private static final ResponseWrapper RAW_WRAPPER = new ErrorResponseWrapper();


	// SECTION: FACTORY

	public static ResponseProcessor json()
	{
		return new ResponseProcessor(JSON_SERIALIZER, RAW_WRAPPER);
	}

	public static ResponseProcessor xml()
	{
		return new ResponseProcessor(XML_SERIALIZER, RAW_WRAPPER);
	}
}
