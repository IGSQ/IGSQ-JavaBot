package org.igsq.igsqbot.entities.cache;

import java.time.OffsetDateTime;
import net.dv8tion.jda.api.entities.Message;

public class CachedMessage
{
	private final OffsetDateTime timeCreated;
	private final String contentRaw;
	private final CachedUser author;
	private final String channelId;
	private final long id;

	public CachedMessage(Message message)
	{
		this.timeCreated = message.getTimeCreated();
		this.contentRaw = message.getContentRaw();
		this.author = new CachedUser(message.getAuthor());
		this.channelId = message.getChannel().getId();
		this.id = message.getIdLong();
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

	public long getId()
	{
		return id;
	}
}
