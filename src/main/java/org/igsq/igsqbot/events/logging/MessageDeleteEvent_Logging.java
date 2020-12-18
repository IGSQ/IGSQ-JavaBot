package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.cache.GuildConfigCache;
import org.igsq.igsqbot.objects.cache.MessageCache;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.time.format.DateTimeFormatter;

public class MessageDeleteEvent_Logging extends ListenerAdapter
{
	@Override
	public void onMessageDelete(MessageDeleteEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT))
		{
			final MessageCache cache = MessageCache.getCache(event.getGuild());

			if(cache.isInCache(event.getMessageId()))
			{
				final Message message = cache.get(event.getMessageId());
				final MessageChannel logChannel = GuildConfigCache.getCache(event.getGuild(), event.getJDA()).getLogChannel();
				final MessageChannel channel = event.getChannel();
				String content = message.getContentRaw();

				if(StringUtils.isCommand(content, event.getGuild().getId(), event.getJDA()))
				{
					return;
				}
				if(message.getAuthor().isBot()) return;
				if(content.length() >= 2000) content = content.substring(0, 1500) + " **...**";

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
					new EmbedGenerator(logChannel)
							.title("Message Deleted")
							.text(
									"**Author**: " + message.getAuthor().getAsMention() +
											"\n**Sent In**: " + StringUtils.getChannelAsMention(channel.getId()) +
											"\n**Sent On**: " + message.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
											"\n\n**Message Content**: " + content)
							.color(EmbedUtils.IGSQ_PURPLE)
							.footer("Logged on: " + StringUtils.getTimestamp())
							.send();
				}
				cache.remove(message);
			}
		}
	}
}
