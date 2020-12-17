package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.MessageCache;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.time.format.DateTimeFormatter;

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
				GuildChannel logChannel = YamlUtils.getLogChannel(event.getGuild().getId());
				MessageChannel channel = event.getChannel();
				String newContent = newMessage.getContentRaw();
				String oldContent = oldMessage.getContentRaw();

				if(StringUtils.isCommand(newMessage.getContentRaw(), event.getGuild().getId()))
				{
					return;
				}
				
				if(newMessage.getAuthor().isBot()) return;
				if(newContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				if(oldContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				
				if(!YamlUtils.isFieldEmpty(event.getGuild().getId() + ".blacklistlog", "guild"))
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
					new EmbedGenerator((MessageChannel) logChannel)
							.title("Message Altered")
							.text(
							"**Author**: " + newMessage.getAuthor().getAsMention() +
							"\n**Sent In**: " + StringUtils.getChannelAsMention(channel.getId()) +
							"\n**Sent On**: " + newMessage.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
							"\n\n**Message Content Before**: " + oldContent +
							"\n**Message Content After**: " + newContent)
							.color(EmbedUtils.IGSQ_PURPLE)
							.footer("Logged on: " + StringUtils.getTimestamp())
							.send();
				}
				cache.update(oldMessage, newMessage);
			}
		}
	}
}
