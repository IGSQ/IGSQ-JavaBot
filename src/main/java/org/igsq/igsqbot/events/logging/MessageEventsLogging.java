package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.StringUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class MessageEventsLogging extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageEventsLogging(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event)
	{
		if(event.isFromGuild())
		{
			MessageCache cache = MessageCache.getCache(event.getGuild().getId());

			if(cache.isInCache(event.getMessage()))
			{
				Message newMessage = event.getMessage();
				CachedMessage oldMessage = cache.get(event.getMessageId());
				MessageChannel channel = event.getChannel();
				String oldContent = oldMessage.getContentRaw();
				String newContent = newMessage.getContentRaw();

				if(CommandUtils.isValidCommand(newMessage.getContentRaw(), event.getGuild().getId(), event.getJDA()))
				{
					return;
				}

				if(newMessage.getAuthor().isBot()) return;
				if(newContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				if(oldContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";

				new EmbedBuilder()
						.setTitle("Message Altered")
						.setDescription("**Author**: " + newMessage.getAuthor().getAsMention() +
								"\n**Sent In**: " + StringUtils.getChannelAsMention(channel.getId()) +
								"\n**Sent On**: " + newMessage.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
								"\n\n**Message Content Before**: " + oldContent +
								"\n**Message Content After**: " + newContent)
						.setColor(Constants.IGSQ_PURPLE)
						.setTimestamp(Instant.now())
						.build();

				cache.update(oldMessage, new CachedMessage(newMessage));
			}
		}
	}

	@Override
	public void onMessageDelete(MessageDeleteEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT))
		{
			MessageCache cache = MessageCache.getCache(event.getGuild());

			if(cache.isInCache(event.getMessageId()))
			{
				CachedMessage message = cache.get(event.getMessageId());
				Guild guild = event.getGuild();
				MessageChannel channel = event.getChannel();
				String content = message.getContentRaw();

				if(CommandUtils.isValidCommand(content, event.getGuild().getId(), event.getJDA()))
				{
					return;
				}
				if(message.getAuthor().isBot()) return;
				if(content.length() >= 2000) content = content.substring(0, 1500) + " **...**";



					new EmbedBuilder()
							.setTitle("Message Deleted")
							.setDescription("**Author**: " + message.getAuthor().getAsMention() +
									"\n**Sent In**: " + StringUtils.getChannelAsMention(channel.getId()) +
									"\n**Sent On**: " + message.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
									"\n\n**Message Content**: " + content)
							.setColor(Constants.IGSQ_PURPLE)
							.setTimestamp(Instant.now())
							.build();
				cache.remove(message);
			}
		}
	}
}

