package com.echo.serialization;

import org.restexpress.serialization.xml.XstreamXmlProcessor;

import com.echo.controller.DelayResponse;

/**
 * @author toddf
 * @since Feb 16, 2011
 */
public class XmlSerializationProcessor
extends XstreamXmlProcessor
{
	public XmlSerializationProcessor()
    {
	    super();
		alias("delay_response", DelayResponse.class);
    }
}
