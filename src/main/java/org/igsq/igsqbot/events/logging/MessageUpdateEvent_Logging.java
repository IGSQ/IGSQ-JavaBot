package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GuildConfig;
import org.igsq.igsqbot.objects.cache.MessageCache;
import org.igsq.igsqbot.util.ArrayUtils;
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
			MessageCache cache = MessageCache.getCache(event.getGuild().getId());

			if(cache.isInCache(event.getMessage()))
			{
				final Message newMessage = event.getMessage();
				final Message oldMessage = cache.get(event.getMessageId());
				final MessageChannel logChannel = new GuildConfig(event.getGuild(), event.getJDA()).getLogChannel();
				final MessageChannel channel = event.getChannel();
				final String oldContent = oldMessage.getContentRaw();
				String newContent = newMessage.getContentRaw();

				if(StringUtils.isCommand(newMessage.getContentRaw(), event.getGuild().getId(), event.getJDA()))
				{
					return;
				}

				if(newMessage.getAuthor().isBot()) return;
				if(newContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				if(oldContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";

				if(!YamlUtils.isFieldEmpty(event.getGuild().getId() + ".blacklistlog", "guild"))
				{
					if(ArrayUtils.isValueInArray(Yaml.getFieldString(event.getGuild().getId() + ".blacklistlog", "guild").split(","), channel.getId()))
					{
						return;
					}
				}

				if(logChannel != null)
				{
					new EmbedGenerator(logChannel)
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
