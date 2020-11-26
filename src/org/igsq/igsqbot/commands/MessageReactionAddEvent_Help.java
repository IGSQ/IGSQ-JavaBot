package org.igsq.igsqbot.commands;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Help extends ListenerAdapter 
{
	public MessageReactionAddEvent_Help()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		Message message = event.retrieveMessage().complete();
		String messageID = event.getMessageId();
		
		if(Yaml.getFieldBool(messageID + ".help.enabled", "internal") && !event.retrieveUser().complete().isBot())
		{
			event.getReaction().removeReaction(event.retrieveUser().complete()).queue();
			if(Yaml.getFieldString(messageID + ".help.user","internal").equals(event.getUserId()))
			{
				int page = Yaml.getFieldInt(messageID + ".help.page", "internal");
				if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+25c0"))
				{
					page--;
					if(page == 0) page = Common_Command.HELP_PAGE_TEXT.length;
					Yaml.updateField(messageID + ".help.page", "internal", page);
				}
				else if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+25b6")) 
				{
					page++;
					if(page == Common_Command.HELP_PAGE_TEXT.length + 1) page = 1;
					Yaml.updateField(messageID + ".help.page", "internal", page);
				}
				EmbedGenerator embed = Common_Command.HELP_PAGE_TEXT[page-1];
				embed.setChannel(message.getChannel()).replace(message);
			}
		}
    }
}
