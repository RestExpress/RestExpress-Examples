package org.restexpress.example.blogging.serialization;

import org.restexpress.example.blogging.domain.Blog;
import org.restexpress.example.blogging.domain.BlogEntry;
import org.restexpress.example.blogging.domain.Comment;
import org.restexpress.serialization.xml.XstreamXmlProcessor;

public class XmlSerializationProcessor
extends XstreamXmlProcessor
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
