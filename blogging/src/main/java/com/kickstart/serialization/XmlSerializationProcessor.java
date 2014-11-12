package com.kickstart.serialization;

import com.kickstart.domain.Blog;
import com.kickstart.domain.BlogEntry;
import com.kickstart.domain.Comment;
import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;

public class XmlSerializationProcessor
extends DefaultXmlProcessor
{
	public XmlSerializationProcessor()
    {
	    super();
	    alias("blog", Blog.class);
	    alias("blog_entry", BlogEntry.class);
	    alias("comment", Comment.class);
		registerConverter(new XstreamObjectIdConverter());
    }
}
