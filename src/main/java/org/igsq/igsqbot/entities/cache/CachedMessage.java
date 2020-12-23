package org.igsq.igsqbot.entities.cache;

import net.dv8tion.jda.api.entities.Message;

import java.time.OffsetDateTime;

public class CachedMessage
{
	private final OffsetDateTime timeCreated;
	private final String contentRaw;
	private final CachedUser author;
	private final String channelId;
	private final String id;

	public CachedMessage(Message message)
	{
		this.timeCreated = message.getTimeCreated();
		this.contentRaw = message.getContentRaw();
		this.author = new CachedUser(message.getAuthor());
		this.channelId = message.getChannel().getId();
		this.id = message.getId();
	}

	public OffsetDateTime getTimeCreated()
	{
		return timeCreated;
	}

	public String getContentRaw()
	{
		return contentRaw;
	}

	public CachedUser getAuthor()
	{
		return author;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public String getId()
	{
		return id;
	}
}
