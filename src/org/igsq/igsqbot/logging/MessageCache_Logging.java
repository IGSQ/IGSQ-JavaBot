package org.igsq.igsqbot.logging;

import java.time.OffsetDateTime;

import org.igsq.igsqbot.Common;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageCache_Logging extends ListenerAdapter
{
	public static Message[] messageCache = new Message[0];
	public MessageCache_Logging()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {	
		if(messageCache.length >= 1000)
		{
			messageCache = Common.depend(messageCache, 0);
		}
		else
		{
			messageCache = Common.append(messageCache, event.getMessage());
		}
    }
	
	public static Message get(String ID)
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
	
	public static void put(Message message)
	{
		if(messageCache.length >= 1000)
		{
			messageCache = Common.depend(messageCache, 0);
		}
		else
		{
			messageCache = Common.append(messageCache, message);
		}
	}
	
	public static void remove(String ID)
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getId().equals(ID))
			{
				messageCache = Common.append(messageCache, selectedMessage);
				return;
			}
		}
	}
	
	public static void remove(Message message)
	{
		messageCache = Common.depend(messageCache, message);
	}
	
	public static boolean isInCache(Message message)
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
	
	public static boolean isInCache(String ID)
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
	
	public static void clean()
	{
		for(Message selectedMessage : messageCache)
		{
			if(selectedMessage.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(1)))
			{
				messageCache = Common.depend(messageCache, selectedMessage);
			}	
		}
	}
}
