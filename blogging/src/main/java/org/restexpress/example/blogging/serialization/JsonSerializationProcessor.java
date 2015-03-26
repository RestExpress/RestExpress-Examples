package org.restexpress.example.blogging.serialization;

import org.restexpress.serialization.json.JacksonJsonProcessor;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonSerializationProcessor
extends JacksonJsonProcessor
{

	public JsonSerializationProcessor()
	{
		super();
	}

	@Override
	protected void initializeModule(SimpleModule module)
	{
		super.initializeModule(module);
		// module.addDeserializer(ObjectId.class, new
		// JacksonObjectIdDeserializer());
		// module.addSerializer(ObjectId.class, new
		// JacksonObjectIdSerializer());
	}

	@Override
	protected void initializeMapper(ObjectMapper mapper)
	{
		super.initializeMapper(mapper);
		mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}
}
