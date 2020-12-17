package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.ArrayUtils;

public class MessageReactionAddEvent_Help extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		final String messageID = event.getMessageId();
		final ReactionEmote reaction = event.getReactionEmote();
		final String codePoint = reaction.getAsCodepoints();
		
		event.retrieveMessage().queue(
			message ->
				event.retrieveUser().queue(
					user ->
					{
						if(Yaml.getFieldBool(messageID + ".help.enabled", "internal") && !user.isBot())
						{
							event.getReaction().removeReaction(user).queue();

							if(Yaml.getFieldString(messageID + ".help.user","internal").equals(event.getUserId()))
							{
								int page = Yaml.getFieldInt(messageID + ".help.page", "internal");

								if(reaction.isEmoji() && codePoint.equals("U+25c0"))
								{
									page--;
									if(page == 0) page = ArrayUtils.HELP_PAGE_TEXT.size();
									Yaml.updateField(messageID + ".help.page", "internal", page);
								}

								else if(reaction.isEmoji() && codePoint.equals("U+25b6"))
								{
									page++;
									if(page == ArrayUtils.HELP_PAGE_TEXT.size() + 1) page = 1;
									Yaml.updateField(messageID + ".help.page", "internal", page);
								}
								EmbedGenerator embed = ArrayUtils.HELP_PAGE_TEXT.get(page-1);
								embed.setChannel(message.getChannel()).replace(message);
							}
						}
					})
		);
    }
}
