package main.java.org.igsq.igsqbot.logging;

import main.java.org.igsq.igsqbot.Yaml;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.objects.MessageCache;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import main.java.org.igsq.igsqbot.util.StringUtils;
import main.java.org.igsq.igsqbot.util.YamlUtils;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageBulkDeleteEvent_Logging extends ListenerAdapter
{
	@Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event)
    {
		GuildChannel logChannel = YamlUtils.getLogChannel(event.getGuild().getId());
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
				String content = selectedMessage.getContentRaw();

				if(StringUtils.isCommand(content, event.getGuild().getId()))
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
			new EmbedGenerator((MessageChannel) logChannel)
					.title("Messages Deleted")
					.text("**Channel**: " + StringUtils.getChannelAsMention(channel.getId())
					+ "\n\n**Messages**: " + embedDescription)
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
    }
}
