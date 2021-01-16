package org.igsq.igsqbot.entities.cache;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public class MessageCache
{
	private static final Map<String, MessageCache> MESSAGE_CACHES = new ConcurrentHashMap<>();

	private final Map<String, CachedMessage> cachedMessages;
	private final String guildId;

	public MessageCache(String guildId)
	{
		this.guildId = guildId;

		this.cachedMessages = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(3, TimeUnit.HOURS)
				.build();
	}

	public static MessageCache getCache(String guildId)
	{
		if(MESSAGE_CACHES.get(guildId) != null)
		{
			return MESSAGE_CACHES.get(guildId);
		}
		MessageCache newCache = new MessageCache(guildId);
		MESSAGE_CACHES.put(guildId, newCache);
		return newCache;
	}

	public static MessageCache getCache(Guild guild)
	{
		if(MESSAGE_CACHES.get(guild.getId()) != null)
		{
			return MESSAGE_CACHES.get(guild.getId());
		}
		MessageCache newCache = new MessageCache(guild.getId());
		MESSAGE_CACHES.put(guild.getId(), newCache);
		return newCache;
	}

	public void set(CachedMessage message)
	{
		if(cachedMessages.size() >= 1000)
		{
			clean();
		}
		cachedMessages.putIfAbsent(message.getId(), message);
	}

	public void set(List<CachedMessage> messages)
	{
		for(CachedMessage selectedMessage : messages)
		{
			if(cachedMessages.size() >= 1000)
			{
				clean();
			}
			cachedMessages.putIfAbsent(selectedMessage.getId(), selectedMessage);
		}
	}

	public CachedMessage get(String messageId)
	{
		for(Map.Entry<String, CachedMessage> entry : cachedMessages.entrySet())
		{
			if(entry.getKey().equals(messageId))
			{
				return entry.getValue();
			}
		}
		return null;
	}

	public void remove(String messageId)
	{
		cachedMessages.remove(messageId);
	}

	public void remove(CachedMessage message)
	{
		cachedMessages.remove(message.getId());
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

	public void update(CachedMessage oldMessage, CachedMessage newMessage)
	{
		cachedMessages.remove(oldMessage.getId());
		set(newMessage);
	}

	public void update(String oldMessageID, CachedMessage newMessage)
	{
		cachedMessages.remove(oldMessageID);
		set(newMessage);
	}

	public String getID()
	{
		return guildId;
	}

	public Map<String, CachedMessage> getCachedMessages()
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
