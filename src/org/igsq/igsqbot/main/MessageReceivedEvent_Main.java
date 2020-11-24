package org.igsq.igsqbot.main;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.MessageCache;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedEvent_Main extends ListenerAdapter
{	
	public MessageReceivedEvent_Main()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {	
		if(!MessageCache.isCacheExist(event.getGuild().getId()))
		{
			MessageCache.setAndReturnCache(event.getGuild().getId()).set(event.getMessage());
		}
		else
		{
			MessageCache.getCache(event.getGuild().getId()).set(event.getMessage());
		}
    }
}
