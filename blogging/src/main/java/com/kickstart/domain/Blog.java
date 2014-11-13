package com.kickstart.domain;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.strategicgains.syntaxe.annotation.StringValidation;

@Entity("blogs")
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
