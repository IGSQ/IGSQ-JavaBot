package org.igsq.igsqbot.main;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.MessageCache;
import org.igsq.igsqbot.util.EventWaiter;

import net.dv8tion.jda.api.entities.ChannelType;
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
		if(event.getChannelType().equals(ChannelType.TEXT) && !event.getAuthor().isBot() && !event.getMessage().getContentRaw().startsWith(Common.BOT_PREFIX) && !EventWaiter.waitingOnThis(event))
		{
			if(!MessageCache.isGuildCached(event.getGuild().getId()))
			{
				MessageCache.addAndReturnCache(event.getGuild().getId()).set(event.getMessage());
			}
			else
			{
				MessageCache.getCache(event.getGuild().getId()).set(event.getMessage());
			}
		}
    }
}
