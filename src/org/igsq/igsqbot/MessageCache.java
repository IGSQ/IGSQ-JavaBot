package org.igsq.igsqbot;

import java.time.OffsetDateTime;

import net.dv8tion.jda.api.entities.Message;

public class MessageCache
{
	public static MessageCache[] messageCaches = new MessageCache[0];
	private Message[] messageCache;
	private String ID;
	
	public MessageCache(String ID)
	{
		this.ID = ID;
		this.messageCache = new Message[0];
	}
	
	public void set(Message message)
	{
		if(messageCache.length >= 1000) 
		{
			messageCache = Common.depend(messageCache, 0);
			clean();
		}
		messageCache = Common.append(messageCache, message);
	}
	
	public Message get(String ID)
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getId().equals(ID))
			{
				return selectedMessage;
			}
		}
		return null;
	}
	
	public void remove(String ID)
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getId().equals(ID))
			{
				messageCache = Common.depend(messageCache, selectedMessage);
			}
		}
	}
	
	public void remove(Message message) //TODO: make this accept a List / Array
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.equals(message))
			{
				messageCache = Common.depend(messageCache, selectedMessage);
			}
		}
	}
	
	public boolean isInCache(String ID)
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getId().equals(ID))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isInCache(Message message)
	{
		for(Message selectedMessage : messageCache)
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
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.equals(oldMessage))
			{
				messageCache = Common.depend(messageCache, selectedMessage);
				set(newMessage);
			}
		}
	}
	public void update(String oldMessageID, Message newMessage)
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getId().equals(oldMessageID))
			{
				messageCache = Common.depend(messageCache, selectedMessage);
				set(newMessage);
			}
		}
	}
	public String getID()
	{
		return ID;
	}
	
	public void clean()
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(1)))
			{
				messageCache = Common.depend(messageCache, selectedMessage);
			}	
		}
	}
	
	public void flush()
	{
		messageCache = new Message[0];
	}
	
	public static MessageCache[] append(MessageCache[] array, MessageCache value)
	{
		MessageCache[] arrayAppended = new MessageCache[array.length+1];
		for (int i = 0;i < array.length;i++)
		{
			arrayAppended[i] = array[i];
		}
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
	
	public static MessageCache getCache(String ID)
	{
		for(MessageCache selectedCache : messageCaches)
		{
			if(selectedCache.getID().equals(ID))
			{
				return selectedCache;
			}
		}
		return null;
	}
	
	public static boolean isGuildCached(String ID)
	{
		for(MessageCache selectedCache : messageCaches)
		{
			if(selectedCache.getID().equals(ID))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void addCache(String ID)
	{
		messageCaches = append(messageCaches, new MessageCache(ID));
	}
	
	public static MessageCache addAndReturnCache(String ID)
	{
		MessageCache cache = new MessageCache(ID);
		messageCaches = append(messageCaches, cache);
		return cache;
	}
}
