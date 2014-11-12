package com.kickstart.domain.event;

import com.kickstart.domain.Comment;

public class CommentDeletedEvent
{
	public String commentId;

	public CommentDeletedEvent(Comment deleted)
	{
		this.commentId = deleted.getId();
	}
}
