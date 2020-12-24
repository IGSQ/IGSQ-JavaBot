package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

public class MessageBulkDelete_Logging extends ListenerAdapter
{
	@Override
	public void onMessageBulkDelete(MessageBulkDeleteEvent event)
	{
		final MessageChannel logChannel = new GuildConfig(event.getGuild(), event.getJDA()).getLogChannel();
		MessageChannel channel = event.getChannel();
		StringBuilder embedDescription = new StringBuilder();
		MessageCache cache = MessageCache.getCache(event.getGuild());

		for(String selectedMessageID : event.getMessageIds())
		{
			if(cache.isInCache(selectedMessageID))
			{
				CachedMessage selectedMessage = cache.get(selectedMessageID);
				String content = selectedMessage.getContentRaw();

				if(StringUtils.isCommand(content, event.getGuild().getId(), event.getJDA()))
				{
					return;
				}
				if(selectedMessage.getAuthor().isBot()) continue;
				if(content.length() >= 50) content = content.substring(0, 20) + " **...**";

				if(!YamlUtils.isFieldEmpty(event.getGuild().getId() + ".blacklistlog", Filename.GUILD))
				{
					for(String selectedChannel : Yaml.getFieldString(event.getGuild().getId() + ".blacklistlog", Filename.GUILD).split(","))
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
			new EmbedGenerator(logChannel)
					.title("Messages Deleted")
					.text("**Channel**: " + StringUtils.getChannelAsMention(channel.getId())
							+ "\n\n**Messages**: " + embedDescription.toString())
					.color(Constants.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
