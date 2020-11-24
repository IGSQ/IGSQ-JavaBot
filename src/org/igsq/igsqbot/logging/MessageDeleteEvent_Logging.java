package org.igsq.igsqbot.logging;

import java.awt.Color;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageDeleteEvent_Logging extends ListenerAdapter
{
	public MessageDeleteEvent_Logging()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageDelete(MessageDeleteEvent event)
    {
		MessageCache cache = Common_Logging.retrieveCache(event.getGuild().getId());
		if(cache != null)
		{
			if(cache.isInCache(event.getMessageId()))
			{
				Message message = cache.get(event.getMessageId());
				GuildChannel logChannel = Common.fetchLogChannel(event.getGuild().getId());
				MessageChannel channel = event.getChannel();
				String content = message.getContentDisplay();
				
				if(message.getAuthor().isBot()) return;
				if(content.length() >= 2000) content = content.substring(0, 1500) + " **...**";
				
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
					new EmbedGenerator((MessageChannel) logChannel).title("Message Deleted").text(
					"**Author**: " + message.getAuthor().getAsMention() +
					"\n**Sent In**: " + Common.getChannelAsMention(channel.getId()) +
					"\n\n**Message Content**: " + content).color(Color.PINK).footer("Deleted at: " + Common.getTimestamp()).send();
				}
				cache.remove(message);
			}
		}
    }
}
