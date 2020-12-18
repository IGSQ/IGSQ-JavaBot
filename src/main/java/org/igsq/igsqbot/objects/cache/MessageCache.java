package org.igsq.igsqbot.objects.cache;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageCache
{
	private static final List<MessageCache> MESSAGE_CACHES = new ArrayList<>();
	private final Map<String, Message> cachedMessages;
	private final String guildId;

	public MessageCache(String guildId)
	{
		this.guildId = guildId;

		this.cachedMessages = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.CREATED)
				.expiration(6, TimeUnit.HOURS)
				.build();
	}

	public static MessageCache getCache(String id)
	{
		for(MessageCache selectedCache : MESSAGE_CACHES)
		{
			if(selectedCache.getID().equals(id))
			{
				return selectedCache;
			}
		}
		MessageCache newCache = new MessageCache(id);
		MESSAGE_CACHES.add(newCache);
		return newCache;
	}

	public static MessageCache getCache(Guild guild)
	{
		for(MessageCache selectedCache : MESSAGE_CACHES)
		{
			if(selectedCache.getID().equals(guild.getId()))
			{
				return selectedCache;
			}
		}
		MessageCache newCache = new MessageCache(guild.getId());
		MESSAGE_CACHES.add(newCache);
		return newCache;
	}

	public void set(Message message)
	{
		if(cachedMessages.size() >= 1000)
		{
			clean();
		}
		cachedMessages.putIfAbsent(message.getId(), message);
	}

	public void set(List<Message> messages)
	{
		for(Message selectedMessage : messages)
		{
			if(cachedMessages.size() >= 1000)
			{
				clean();
			}
			cachedMessages.putIfAbsent(selectedMessage.getId(), selectedMessage);
		}
	}

	public Message get(String id)
	{
		for(Map.Entry<String, Message> entry : cachedMessages.entrySet())
		{
			if(entry.getKey().equals(id))
			{
				return entry.getValue();
			}
		}
		return null;
	}

	public void remove(String id)
	{
		cachedMessages.remove(id);
	}

	public void remove(Message message)
	{
		cachedMessages.remove(message.getId());
	}

	public void remove(List<Message> messages)
	{
		messages.forEach(message -> cachedMessages.remove(message.getId()));
	}

	public boolean isInCache(String messageId)
	{
		return cachedMessages.containsKey(messageId);
	}

	public boolean isInCache(Message message)
	{
		return cachedMessages.containsKey(message.getId());
	}

	public void update(Message oldMessage, Message newMessage)
	{
		cachedMessages.remove(oldMessage.getId());
		set(newMessage);
	}

	public void update(String oldMessageID, Message newMessage)
	{
		cachedMessages.remove(oldMessageID);
		set(newMessage);
	}

	public String getID()
	{
		return guildId;
	}

	public Map<String, Message> getCachedMessages()
	{
		return cachedMessages;
	}

	public void clean()
	{
		cachedMessages.entrySet().removeIf(entry -> entry.getValue().getTimeCreated().isBefore(OffsetDateTime.now().minusDays(1)));
	}

	public void flush()
	{
		cachedMessages.clear();
	}
}
