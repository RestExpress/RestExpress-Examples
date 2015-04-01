package org.restexpress.example.blogging.serialization;

import org.restexpress.ContentType;
import org.restexpress.serialization.json.JacksonJsonProcessor;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.strategicgains.hyperexpress.domain.hal.HalResource;
import com.strategicgains.hyperexpress.serialization.jackson.HalResourceDeserializer;
import com.strategicgains.hyperexpress.serialization.jackson.HalResourceSerializer;

public class JsonSerializationProcessor
extends JacksonJsonProcessor
{

	public JsonSerializationProcessor()
	{
		super();
		addSupportedMediaTypes(ContentType.HAL_JSON);
	}

	@Override
	protected void initializeModule(SimpleModule module)
	{
		super.initializeModule(module);

		// Support HalResource (de)serialization.
		module.addDeserializer(HalResource.class, new HalResourceDeserializer());
		module.addSerializer(HalResource.class, new HalResourceSerializer());
	}
}
