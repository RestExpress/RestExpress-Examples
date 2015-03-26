package org.restexpress.example.blogging.serialization;

import org.bson.types.ObjectId;

import com.thoughtworks.xstream.converters.SingleValueConverter;


/**
 * @author toddf
 * @since Feb 16, 2011
 */
public class XstreamObjectIdConverter
implements SingleValueConverter
{
	@SuppressWarnings("rawtypes")
    @Override
	public boolean canConvert(Class aClass)
	{
		return ObjectId.class.isAssignableFrom(aClass);
	}

	@Override
	public Object fromString(String value)
	{
		return new ObjectId(value);
	}

	@Override
	public String toString(Object objectId)
	{
		return ((ObjectId) objectId).toString();
	}
}
