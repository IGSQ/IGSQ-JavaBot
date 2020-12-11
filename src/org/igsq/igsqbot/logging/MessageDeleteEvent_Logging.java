package org.igsq.igsqbot.logging;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.entities.ChannelType;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.MessageCache;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.util.String_Utils;
import org.igsq.igsqbot.util.Yaml_Utils;

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

		    Message message = cache.get(event.getMessageId());
		    if(cache.isInCache(message))
		    {
			    GuildChannel logChannel = Yaml_Utils.getLogChannel(event.getGuild().getId());
			    MessageChannel channel = event.getChannel();
			    String content = message.getContentDisplay();

			    if(message.getAuthor().isBot()) return;
			    if(content.length() >= 2000) content = content.substring(0, 1500) + " **...**";

			    if(!Yaml_Utils.isFieldEmpty(event.getGuild().getId() + ".blacklistlog", "guild"))
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
								    "\n**Sent In**: " + String_Utils.getChannelAsMention(channel.getId()) +
								    "\n**Sent On**: " + message.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
								    "\n\n**Message Content**: " + content)
						    .color(Color.PINK).footer("Logged on: " + String_Utils.getTimestamp()).send();
			    }
			    cache.remove(message);
		    }
	    }
    }
}
