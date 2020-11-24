package org.igsq.igsqbot.logging;

import org.igsq.igsqbot.Common;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedEvent_Logging extends ListenerAdapter
{	
	public MessageReceivedEvent_Logging()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
		if(Common_Logging.isCache(event.getGuild().getId()))
		{
			Common_Logging.retrieveCache(event.getGuild().getId()).put(event.getMessage());
		}
		else
		{
			Main_Logging.messageCaches = Common_Logging.append(Main_Logging.messageCaches, new MessageCache(event.getGuild().getId()));
			Common_Logging.retrieveCache(event.getGuild().getId()).put(event.getMessage());
		}
    }
}
