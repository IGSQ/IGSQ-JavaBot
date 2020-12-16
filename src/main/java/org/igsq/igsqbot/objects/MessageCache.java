package main.java.org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.entities.Message;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageCache
{
	private static List<MessageCache> messageCaches = new ArrayList<>();
	private final List<Message> cachedMessages;
	private final String guildId;
	
	public MessageCache(String guildId)
	{
		this.guildId = guildId;
		this.cachedMessages = new ArrayList<>();
	}
	
	public void set(Message message)
	{
		if(cachedMessages.size() >= 1000)
		{
			cachedMessages.remove(0);
			clean();
		}
		cachedMessages.add(message);
	}
	
	public void set(List<Message> messages)
	{
		for(Message selectedMessage : messages)
		{
			if(cachedMessages.size() >= 1000)
			{
				cachedMessages.remove(0);
			}
			cachedMessages.add(selectedMessage);
		}
	}
	
	public Message get(String id)
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.getId().equals(id))
			{
				return selectedMessage;
			}
		}
		return null;
	}
	
	public void remove(String id)
	{
		cachedMessages.removeIf(selectedMessage -> selectedMessage.getId().equals(id));
	}
	
	public void remove(Message message)
	{
		cachedMessages.removeIf(selectedMessage -> selectedMessage.equals(message));
	}

	public void remove(List<Message> messages)
	{
		cachedMessages.removeAll(messages);
	}
	
	public boolean isInCache(String messageId)
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.getId().equals(messageId))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isInCache(Message message)
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.equals(message))
			{
				return true;
			}
		}
		return false;
	}
	
	public void update(Message oldMessage, Message newMessage)
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.equals(oldMessage))
			{
				cachedMessages.remove(selectedMessage);
				set(newMessage);
			}
		}
	}
	public void update(String oldMessageID, Message newMessage)
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.getId().equals(oldMessageID))
			{
				cachedMessages.remove(selectedMessage);
				set(newMessage);
			}
		}
	}
	public String getID()
	{
		return guildId;
	}
	
	public List<Message> getCachedMessages()
	{
		return cachedMessages;
	}
	
	public void clean()
	{
		cachedMessages.removeIf(selectedMessage -> selectedMessage.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(1)));
	}
	
	public void flush()
	{
		cachedMessages.clear();
	}
	
	public static MessageCache[] append(MessageCache[] array, MessageCache value)
	{
		MessageCache[] arrayAppended = new MessageCache[array.length+1];
        System.arraycopy(array, 0, arrayAppended, 0, array.length);
		arrayAppended[array.length] = value;
		return arrayAppended;
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
