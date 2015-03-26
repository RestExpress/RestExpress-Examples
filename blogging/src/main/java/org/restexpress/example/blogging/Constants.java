package org.restexpress.example.blogging;

public class Constants
{
	public class Routes
	{
		public static final String BLOG_READ_ROUTE = "blog.read.route";
		public static final String BLOG_ENTRY_READ_ROUTE = "blog-entry.read.route";
		public static final String BLOG_ENTRIES_READ_ROUTE = "blog-entry.read.collection.route";
		public static final String COMMENT_READ_ROUTE = "comment.read.route";
		public static final String COMMENTS_READ_ROUTE = "comment.read.collection.route";
	};

	public class Url
	{
		public static final String BLOG_ID_PARAMETER = "blogId";
		public static final String BLOG_ENTRY_ID_PARAMETER = "entryId";
		public static final String COMMENT_ID_PARAMETER = "commentId";
	}
}
