package org.igsq.igsqbot.logging;

import java.time.OffsetDateTime;

import org.igsq.igsqbot.Common;

import net.dv8tion.jda.api.entities.Message;

public class MessageCache
{
	private Message[] messageCache;
	private String ID;
	
	public MessageCache(String ID)
	{
		this.ID = ID;
		this.messageCache = new Message[0];
	}
	
	public void put(Message message)
	{
		if(messageCache.length >= 1000) messageCache = Common.depend(messageCache, 0);
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
	
	public void remove(Message message)
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
}
