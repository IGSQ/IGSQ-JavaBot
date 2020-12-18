package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GuildConfig;
import org.igsq.igsqbot.objects.cache.MessageCache;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

public class MessageBulkDeleteEvent_Logging extends ListenerAdapter
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
				Message selectedMessage = cache.get(selectedMessageID);
				String content = selectedMessage.getContentRaw();

				if(StringUtils.isCommand(content, event.getGuild().getId(), event.getJDA()))
				{
					return;
				}
				if(selectedMessage.getAuthor().isBot()) continue;
				if(content.length() >= 50) content = content.substring(0, 20) + " **...**";

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
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
