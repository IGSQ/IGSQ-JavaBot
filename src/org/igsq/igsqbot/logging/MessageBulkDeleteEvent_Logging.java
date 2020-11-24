package org.igsq.igsqbot.logging;

import java.awt.Color;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageBulkDeleteEvent_Logging extends ListenerAdapter
{
	public MessageBulkDeleteEvent_Logging()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event)
    {
		GuildChannel logChannel = Common.fetchLogChannel(event.getGuild().getId());
		MessageChannel channel = event.getChannel();
		String embedDescription = "";
		MessageCache cache = Common_Logging.retrieveCache(event.getGuild().getId());
		
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
				
				embedDescription += selectedMessage.getAuthor().getAsMention() + " --> " + content;
				cache.remove(selectedMessage);
			}
		}
		if(logChannel != null)
		{
			new EmbedGenerator((MessageChannel)logChannel).text(
			"**Channel**: " + Common.getChannelAsMention(channel.getId()) 
			+ "**Messages**: " + embedDescription).color(Color.PINK).footer("Deleted at: " + Common.getTimestamp()).send();
		}
    }
}
