package org.igsq.igsqbot.main;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.logging.Common_Logging;

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
		if(!Common_Logging.isCacheExist(event.getGuild().getId()))
		{
			Common_Logging.createAndReturnCache(event.getGuild().getId()).put(event.getMessage());
		}
		else
		{
			Common_Logging.retrieveCache(event.getGuild().getId()).put(event.getMessage());
		}
    }
}
