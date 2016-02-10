package org.restexpress.example.blogging.domain;

import java.util.UUID;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.restexpress.example.blogging.Constants;
import org.restexpress.example.blogging.serialization.UuidFormatter;

import com.strategicgains.hyperexpress.annotation.BindToken;
import com.strategicgains.hyperexpress.annotation.TokenBindings;
import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.annotation.StringValidation;

@Entity("comments")
@Indexes({
	@Index("createdAt")
})
@TokenBindings({
	@BindToken(value=Constants.Url.COMMENT_ID_PARAMETER, field="id", formatter=UuidFormatter.class)
})
public class Comment
extends AbstractEntity
{
	@Indexed
	@Required("Blog Entry ID")
	@BindToken(value=Constants.Url.BLOG_ENTRY_ID_PARAMETER, formatter=UuidFormatter.class)
	private UUID blogEntryId;
	
	@StringValidation(name="Author", required=true)
	private String author;
	
	@StringValidation(name="Comment Content", required=true)
	private String content;

	public UUID getBlogEntryId()
    {
    	return blogEntryId;
    }

	public void setBlogEntryId(UUID blogEntryId)
    {
    	this.blogEntryId = blogEntryId;
    }

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
