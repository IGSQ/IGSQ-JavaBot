package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.util.EmbedUtils;

public class MessageEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageEventsMain(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT) && !event.getAuthor().isBot())
		{
			MessageCache.getCache(event.getGuild()).set(new CachedMessage(event.getMessage()));
		}
		else if(event.getChannelType().equals(ChannelType.PRIVATE) && !event.getAuthor().isBot())
		{
			if(event.getMessage().getReferencedMessage() != null)
			{
				String content = event.getMessage().getContentRaw();

				if(content.startsWith("vote"))
				{
					String[] opts = content.split(" ");

					if(opts.length < 2)
					{
						EmbedUtils.sendError(event.getChannel(), "You need to enter an option to vote for.");
						return;
					}
					try
					{
						long messageId = event.getMessage().getReferencedMessage().getIdLong();
						int option = Integer.parseInt(opts[1]);
						int maxOptions = Vote.getMaxVoteById(messageId, igsqBot);

						if(maxOptions == -1)
						{
							EmbedUtils.sendError(event.getChannel(), "That vote is closed.");
							return;
						}
						if(option > maxOptions)
						{
							EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
							return;
						}
						if(Vote.castById(messageId, option, igsqBot))
						{
							EmbedUtils.sendSuccess(event.getChannel(), "Vote cast!");
						}
						else
						{
							EmbedUtils.sendError(event.getChannel(), "Could not cast your vote, maybe you have already cast a vote.");
						}
					}
					catch(Exception exception)
					{
						EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
					}

				}
			}
		}
		igsqBot.getCommandHandler().handle(event);
	}
}

