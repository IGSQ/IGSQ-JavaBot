package org.igsq.igsqbot.events;

import org.igsq.igsqbot.commands.Common_Command;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.ErrorHandler;
import org.igsq.igsqbot.util.Yaml;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Help extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		Message message = null;
		User user = null;
		String messageID = event.getMessageId();
		
		try 
		{
			message = event.retrieveMessage().submit().get();
			user = event.retrieveUser().submit().get();
		} 
		catch (Exception exception) 
		{
			new ErrorHandler(exception);
			return;
		}
		ReactionEmote reaction = event.getReactionEmote();
		String codePoint = reaction.getAsCodepoints();
		
		
		if(Yaml.getFieldBool(messageID + ".help.enabled", "internal") && !user.isBot())
		{
			event.getReaction().removeReaction(user).queue();
			
			if(Yaml.getFieldString(messageID + ".help.user","internal").equals(event.getUserId()))
			{
				int page = Yaml.getFieldInt(messageID + ".help.page", "internal");
				
				if(reaction.isEmoji() && codePoint.equals("U+25c0"))
				{
					page--;
					if(page == 0) page = Common_Command.HELP_PAGE_TEXT.length;
					Yaml.updateField(messageID + ".help.page", "internal", page);
				}
				
				else if(reaction.isEmoji() && codePoint.equals("U+25b6")) 
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
