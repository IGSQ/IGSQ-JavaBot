package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;

public class MessageEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageEventsMain(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		if(event.isFromType(ChannelType.TEXT))
		{
			event.retrieveUser().queue(
					user ->
					{
						if(!user.isBot())
						{
							//TO BE IMPLEMENTED
						}
					}
			);
		}
	}


	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event)
	{
		if(event.isFromType(ChannelType.TEXT))
		{
			event.retrieveUser().queue(
					user ->
					{
						if(!user.isBot())
						{
							//TO BE IMPLEMENTED
						}
					}
			);
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT) && !event.getAuthor().isBot())
		{
			MessageCache.getCache(event.getGuild()).set(new CachedMessage(event.getMessage()));
		}
		igsqBot.getCommandHandler().handle(event);
	}


	@Override
	public void onMessageDelete(MessageDeleteEvent event)
	{
		//TO BE IMPLEMENTED
	}
}

