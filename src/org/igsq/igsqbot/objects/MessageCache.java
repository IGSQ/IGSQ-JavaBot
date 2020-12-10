package org.igsq.igsqbot.objects;

import java.time.OffsetDateTime;
import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import org.igsq.igsqbot.Common;

public class MessageCache
{
	private static MessageCache[] messageCaches = new MessageCache[0];
	private Message[] cachedMessages;
	private final String guildId;
	
	public MessageCache(String guildId)
	{
		this.guildId = guildId;
		this.cachedMessages = new Message[0];
	}
	
	public void set(Message message)
	{
		if(cachedMessages.length >= 1000)
		{
			cachedMessages = Common.depend(cachedMessages, 0);
			clean();
		}
		cachedMessages = Common.append(cachedMessages, message);
	}
	
	public void set(Message[] messages)
	{
		for(Message selectedMessage : messages)
		{
			if(cachedMessages.length >= 1000)
			{
				cachedMessages = Common.depend(cachedMessages, 0);
			}
			cachedMessages = Common.append(cachedMessages, selectedMessage);
		}
	}
	
	public void set(List<Message> messages)
	{
		for(Message selectedMessage : messages)
		{
			if(cachedMessages.length >= 1000)
			{
				cachedMessages = Common.depend(cachedMessages, 0);
			}
			cachedMessages = Common.append(cachedMessages, selectedMessage);
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
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.getId().equals(id))
			{
				cachedMessages = Common.depend(cachedMessages, selectedMessage);
			}
		}
	}
	
	public void remove(Message message)
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.equals(message))
			{
				cachedMessages = Common.depend(cachedMessages, selectedMessage);
			}
		}
	}
	public void remove(Message[] messages)
	{
		for(Message selectedCachedMessage : cachedMessages)
		{
			for(Message selectedMessage : messages)
			{
				if(selectedCachedMessage.equals(selectedMessage))
				{
					cachedMessages = Common.depend(cachedMessages, selectedCachedMessage);
				}
			}
		}
	}
	
	public void remove(List<Message> messages)
	{
		for(Message selectedCachedMessage : cachedMessages)
		{
			for(Message selectedMessage : messages)
			{
				if(selectedCachedMessage.equals(selectedMessage))
				{
					cachedMessages = Common.depend(cachedMessages, selectedCachedMessage);
				}
			}
		}
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
				cachedMessages = Common.depend(cachedMessages, selectedMessage);
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
				cachedMessages = Common.depend(cachedMessages, selectedMessage);
				set(newMessage);
			}
		}
	}
	public String getID()
	{
		return guildId;
	}
	
	public Message[] getCachedMessages()
	{
		return cachedMessages;
	}
	
	public void clean()
	{
		for(Message selectedMessage : cachedMessages)
		{
			if(selectedMessage.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(1)))
			{
				cachedMessages = Common.depend(cachedMessages, selectedMessage);
			}	
		}
	}
	
	public void flush()
	{
		cachedMessages = new Message[0];
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
		messageCaches = append(messageCaches, new MessageCache(guildId));
	}
	
	public static MessageCache addAndReturnCache(String id)
	{
		MessageCache cache = new MessageCache(id);
		messageCaches = append(messageCaches, cache);
		return cache;
	}
}
