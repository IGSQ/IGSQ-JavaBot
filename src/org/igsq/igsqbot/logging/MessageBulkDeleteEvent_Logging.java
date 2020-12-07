package org.igsq.igsqbot.logging;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.MessageCache;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageBulkDeleteEvent_Logging extends ListenerAdapter //TODO: implement .clear to trigger similar functionality to this
{
	@Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event)
    {
		GuildChannel logChannel = Common.getLogChannel(event.getGuild().getId());
		MessageChannel channel = event.getChannel();
		StringBuilder embedDescription = new StringBuilder();
		MessageCache cache;
		
		if(!MessageCache.isGuildCached(event.getGuild().getId()))
		{
			cache = MessageCache.addAndReturnCache(event.getGuild().getId());
		}
		else
		{
			cache = MessageCache.getCache(event.getGuild().getId());
		}
		
		for(String selectedMessageID : event.getMessageIds())
		{
			if(cache.isInCache(selectedMessageID))
			{
				Message selectedMessage = cache.get(selectedMessageID);
				String content = selectedMessage.getContentDisplay();

				if(selectedMessage.getAuthor().isBot()) continue;
				if(content.length() >= 50) content = content.substring(0, 20) + " **...**";

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

				embedDescription.append(selectedMessage.getAuthor().getAsMention()).append(" --> ").append(content).append("\n");
				cache.remove(selectedMessage);
			}
		}
		if(logChannel != null)
		{
			new EmbedGenerator((MessageChannel) logChannel).title("Messages Deleted").text(
			"**Channel**: " + Common.getChannelAsMention(channel.getId()) +
			"\n\n**Messages**: " + embedDescription)
			.color(Color.PINK).footer("Logged on: " + Common.getTimestamp()).send();
		}
    }
}
