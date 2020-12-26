package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.time.format.DateTimeFormatter;

public class MessageUpdate_Logging extends ListenerAdapter
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
				final CachedMessage oldMessage = cache.get(event.getMessageId());
				final MessageChannel logChannel = new GuildConfig(event.getGuild(), event.getJDA()).getLogChannel();
				final MessageChannel channel = event.getChannel();
				final String oldContent = oldMessage.getContentRaw();
				String newContent = newMessage.getContentRaw();

				if(CommandUtils.isValidCommand(newMessage.getContentRaw(), event.getGuild().getId(), event.getJDA()))
				{
					return;
				}

				if(newMessage.getAuthor().isBot()) return;
				if(newContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
				if(oldContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";

				if(!YamlUtils.isFieldEmpty(event.getGuild().getId() + ".blacklistlog", Filename.GUILD))
				{
					if(ArrayUtils.isValueInArray(Yaml.getFieldString(event.getGuild().getId() + ".blacklistlog", Filename.GUILD).split(","), channel.getId()))
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
							.color(Constants.IGSQ_PURPLE)
							.footer("Logged on: " + StringUtils.getTimestamp())
							.send();
				}
				cache.update(oldMessage, new CachedMessage(newMessage));
			}
		}
	}
}
