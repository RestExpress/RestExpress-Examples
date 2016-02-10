package org.restexpress.example.blogging.domain;

import java.util.UUID;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.restexpress.example.blogging.Constants;
import org.restexpress.example.blogging.serialization.UuidFormatter;

import com.strategicgains.hyperexpress.annotation.BindToken;
import com.strategicgains.hyperexpress.annotation.TokenBindings;
import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.annotation.StringValidation;

@Entity("blog_entries")
@TokenBindings({
	@BindToken(value=Constants.Url.BLOG_ENTRY_ID_PARAMETER, field="id", formatter=UuidFormatter.class)
})
public class BlogEntry
extends AbstractEntity
{
	@Indexed
	@Required("Blog ID")
	@BindToken(value=Constants.Url.BLOG_ID_PARAMETER, formatter=UuidFormatter.class)
	private UUID blogId;

	@StringValidation(name="Title", required=true)
	private String title;
	
	@StringValidation(name="Entry Content", required=true)
	private String content;
	
	@Indexed
	@StringValidation(name="Author", required=true)
	private String author;

	public UUID getBlogId()
    {
    	return blogId;
    }

	public void setBlogId(UUID blogId)
    {
    	this.blogId = blogId;
    }

	public String getTitle()
    {
    	return title;
    }

	public void setTitle(String title)
    {
    	this.title = title;
    }

	public String getContent()
    {
    	return content;
    }

	public void setContent(String content)
    {
    	this.content = content;
    }

	public String getAuthor()
    {
    	return author;
    }

	public void setAuthor(String author)
    {
    	this.author = author;
    }
}
