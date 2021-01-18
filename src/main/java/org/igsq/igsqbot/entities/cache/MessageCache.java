package org.igsq.igsqbot.entities.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageCache
{
	private static final Map<Long, MessageCache> MESSAGE_CACHES = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageCache.class);

	private final Map<Long, CachedMessage> cachedMessages;
	private final long guildId;

	public MessageCache(long guildId)
	{
		this.guildId = guildId;

		this.cachedMessages = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(1, TimeUnit.HOURS)
				.build();
	}

	public static MessageCache getCache(long guildId)
	{
		MessageCache cache = MESSAGE_CACHES.get(guildId);
		if(MESSAGE_CACHES.get(guildId) == null)
		{
			cache = new MessageCache(guildId);
			MESSAGE_CACHES.put(guildId, cache);
		}
		return cache;
	}

	public static MessageCache getCache(Guild guild)
	{
		MessageCache cache = MESSAGE_CACHES.get(guild.getIdLong());
		if(MESSAGE_CACHES.get(guild.getIdLong()) == null)
		{
			cache = new MessageCache(guild.getIdLong());
			MESSAGE_CACHES.put(guild.getIdLong(), cache);
		}
		return cache;
	}

	public void set(CachedMessage message)
	{
		LOGGER.debug("Adding message " + message.getIdLong() + " to cache.");
		cachedMessages.putIfAbsent(message.getIdLong(), message);
	}

	public void set(List<CachedMessage> messages)
	{
		for(CachedMessage selectedMessage : messages)
		{
			LOGGER.debug("Adding message " + selectedMessage.getIdLong() + " to cache.");
			cachedMessages.putIfAbsent(selectedMessage.getIdLong(), selectedMessage);
		}
	}

	public CachedMessage get(long messageId)
	{
		LOGGER.debug("Fetching message " + messageId + " from cache.");
		for(Map.Entry<Long, CachedMessage> entry : cachedMessages.entrySet())
		{
			if(entry.getKey().equals(messageId))
			{
				LOGGER.debug("returned message " + entry.getValue().getIdLong() + " from cache.");
				return entry.getValue();
			}
		}
		return null;
	}

	public void remove(long messageId)
	{
		LOGGER.debug("Removed message " + messageId + " from cache.");
		cachedMessages.remove(messageId);
	}

	public void remove(CachedMessage message)
	{
		LOGGER.debug("Removed message " + message.getIdLong() + " from cache.");
		cachedMessages.remove(message.getIdLong());
	}

	public void remove(Message message)
	{
		LOGGER.debug("Removed message " + message.getIdLong() + " from cache.");
		cachedMessages.remove(message.getIdLong());
	}

	public void remove(List<Message> messages)
	{
		messages.forEach(message ->
		{
			LOGGER.debug("Removed message " + message.getIdLong() + " from cache.");
			cachedMessages.remove(message.getIdLong());
		});
	}

	public boolean isInCache(long messageId)
	{
		return cachedMessages.containsKey(messageId);
	}

	public boolean isInCache(Message message)
	{
		return cachedMessages.containsKey(message.getIdLong());
	}

	public void update(CachedMessage oldMessage, CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage.getIdLong() + " -> " + newMessage.getIdLong() + " in cache.");
		cachedMessages.remove(oldMessage.getIdLong());
		set(newMessage);
	}

	public void update(long oldMessageID, CachedMessage newMessage)
	{
		cachedMessages.remove(oldMessageID);
		set(newMessage);
	}

	public long getID()
	{
		return guildId;
	}

	public Map<Long, CachedMessage> getCacheView()
	{
		return cachedMessages;
	}

	public void flush()
	{
		cachedMessages.clear();
	}
}
