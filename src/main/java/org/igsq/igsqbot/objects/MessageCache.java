package org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageCache
{
	private static final List<MessageCache> messageCaches = new ArrayList<>();
	private final Map<String, Message> cachedMessages;
	private final String guildId;
	
	public MessageCache(String guildId)
	{
		this.guildId = guildId;

		PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<String, Message> expirationPolicy =
				new PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<>(24, TimeUnit.HOURS);
		this.cachedMessages = new PassiveExpiringMap<>(expirationPolicy, new HashMap<>());
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
		for(String selectedMessage : cachedMessages.keySet())
		{
			if(selectedMessage.equals(messageId))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isInCache(Message message)
	{
		for(Map.Entry<String, Message> entry : cachedMessages.entrySet())
		{
			if(entry.getValue().equals(message))
			{
				return true;
			}
		}
		return false;
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
		cachedMessages.forEach((id, message) ->
		{
			if(message.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(1)))
			{
				cachedMessages.remove(id);
			}
		});
	}
	
	public void flush()
	{
		cachedMessages.clear();
	}
	
	public static void cleanCaches()
	{
		for(MessageCache selectedCache : messageCaches)
		{
			selectedCache.clean();
		}
	}
	
	public static MessageCache getCache(String id)
	{
		for(MessageCache selectedCache : messageCaches)
		{
			if(selectedCache.getID().equals(id))
			{
				return selectedCache;
			}
		}
		return null;
	}
	
	public static boolean isGuildCached(String id)
	{
		for(MessageCache selectedCache : messageCaches)
		{
			if(selectedCache.getID().equals(id))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void addCache(String guildId)
	{
		messageCaches.add(new MessageCache(guildId));
	}
	
	public static MessageCache addAndReturnCache(String id)
	{
		MessageCache cache = new MessageCache(id);
		messageCaches.add(cache);
		return cache;
	}
}
