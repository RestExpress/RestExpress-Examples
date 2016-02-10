package org.restexpress.example.blogging.domain;

import org.mongodb.morphia.annotations.Entity;
import org.restexpress.example.blogging.Constants;
import org.restexpress.example.blogging.serialization.UuidFormatter;

import com.strategicgains.hyperexpress.annotation.BindToken;
import com.strategicgains.hyperexpress.annotation.TokenBindings;
import com.strategicgains.syntaxe.annotation.StringValidation;

@Entity("blogs")
@TokenBindings({
	@BindToken(value=Constants.Url.BLOG_ID_PARAMETER, field="id", formatter=UuidFormatter.class)
})
public class Blog
extends AbstractEntity
{
	@StringValidation(name = "Blog Title", required = true)
	private String title;
	private String description;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
