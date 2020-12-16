package main.java.org.igsq.igsqbot.logging;

import main.java.org.igsq.igsqbot.Yaml;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.objects.MessageCache;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import main.java.org.igsq.igsqbot.util.StringUtils;
import main.java.org.igsq.igsqbot.util.YamlUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.format.DateTimeFormatter;

public class MessageDeleteEvent_Logging extends ListenerAdapter
{
	@Override
    public void onMessageDelete(MessageDeleteEvent event)
    {
    	if(event.getChannelType().equals(ChannelType.TEXT))
	    {
		    MessageCache cache;
		    if(!MessageCache.isGuildCached(event.getGuild().getId()))
		    {
			    cache = MessageCache.addAndReturnCache(event.getGuild().getId());
		    }
		    else
		    {
			    cache = MessageCache.getCache(event.getGuild().getId());
		    }


		    if(cache.isInCache(event.getMessageId()))
		    {
			    Message message = cache.get(event.getMessageId());
			    GuildChannel logChannel = YamlUtils.getLogChannel(event.getGuild().getId());
			    MessageChannel channel = event.getChannel();
			    String content = message.getContentRaw();

			    if(StringUtils.isCommand(content, event.getGuild().getId()))
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
				    new EmbedGenerator((MessageChannel) logChannel)
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
