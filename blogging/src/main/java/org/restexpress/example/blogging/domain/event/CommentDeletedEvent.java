package org.restexpress.example.blogging.domain.event;

import java.util.UUID;

import org.restexpress.example.blogging.domain.Comment;

public class CommentDeletedEvent
{
	public UUID commentId;

	public CommentDeletedEvent(Comment deleted)
	{
		this.commentId = deleted.getUuid();
	}
}
