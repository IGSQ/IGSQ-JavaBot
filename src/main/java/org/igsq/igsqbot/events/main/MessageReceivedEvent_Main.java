package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.objects.EventWaiter;
import org.igsq.igsqbot.objects.cache.MessageCache;
import org.igsq.igsqbot.util.YamlUtils;

public class MessageReceivedEvent_Main extends ListenerAdapter
{
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT) && !event.getAuthor().isBot() && !EventWaiter.waitingOnThis(event))
		{
			if(!event.getMessage().getContentRaw().startsWith(YamlUtils.getGuildPrefix(event.getGuild().getId())))
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
		CommandHandler.handle(event);
	}
}
