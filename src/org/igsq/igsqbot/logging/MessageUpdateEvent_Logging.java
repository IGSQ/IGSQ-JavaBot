package org.igsq.igsqbot.logging;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.MessageCache;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageUpdateEvent_Logging extends ListenerAdapter
{
	@Override
	public void onMessageUpdate(MessageUpdateEvent event)
	{
		if(event.getChannel().getType().equals(ChannelType.TEXT))
		{
			MessageCache cache;
			if(!MessageCache.isGuildCached(event.getGuild().getId()))
			{
				MessageCache.addCache(event.getGuild().getId());
				return;
			}
			else
			{
				cache = MessageCache.getCache(event.getGuild().getId());
			}
			
			if(cache.isInCache(event.getMessage()))
			{
				Message newMessage = event.getMessage();
				Message oldMessage = cache.get(event.getMessageId());
				GuildChannel logChannel = Common.getLogChannel(event.getGuild().getId());
				MessageChannel channel = event.getChannel();
				String newContent = newMessage.getContentDisplay();
				String oldContent = oldMessage.getContentDisplay();
				
				if(newMessage.getAuthor().isBot()) return;
				if(newContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				if(oldContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				
				if(!Common.isFieldEmpty(event.getGuild().getId() + ".blacklistlog", "guild"))
				{
					for(String selectedChannel : Yaml.getFieldString(event.getGuild().getId() + ".blacklistlog", "guild").split(","))
					{
						if(selectedChannel.equals(channel.getId())) 
						{
							return;
						}
					}
				}
				
				if(logChannel != null)
				{
					new EmbedGenerator((MessageChannel) logChannel).title("Message Altered").text(
					"**Author**: " + newMessage.getAuthor().getAsMention() +
					"\n**Sent In**: " + Common.getChannelAsMention(channel.getId()) +
					"\n**Sent On**: " + newMessage.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
					"\n\n**Message Content Before**: " + oldContent +
					"\n**Message Content After**: " + newContent)
					.color(Color.PINK).footer("Logged on: " + Common.getTimestamp()).send();
				}
				cache.update(oldMessage, newMessage);
			}
		}
	}
}
