package org.igsq.igsqbot.main;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Main extends ListenerAdapter 
{
	public MessageReactionAddEvent_Main()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		if(!(event.getUser() == null) && !event.getUser().isBot())
		{
			Message message = event.retrieveMessage().complete();
			if(Yaml.getFieldBool(event.getMessageId() + ".report.enabled", "internal"))
			{
				Member reportedUser = event.getGuild().retrieveMemberById(Yaml.getFieldString(event.getMessageId() + ".report.reporteduser", "internal")).complete();
				// Member reportingUser = event.getGuild().getMemberById(Yaml.getFieldInt(event.getMessageId() + ".report.reportinguser", "internal"));
				MessageEmbed embed = message.getEmbeds().get(0);
				if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705") && event.getMember().canInteract(reportedUser))
				{
					new EmbedGenerator(event.getChannel(), embed).footer("This was dealt with by " + event.getUser().getAsTag()).color(Color.GREEN).replace(message, true);
					Yaml.updateField(event.getMessageId() + ".report", "internal", null);
				}
				else
				{
					new EmbedGenerator(event.getChannel(), embed).color(Color.YELLOW).footer(event.getUser().getAsTag() + ", you are not higher than " + reportedUser.getRoles().get(0).getName() + ".").replace(message, 5000, embed);
					event.getReaction().removeReaction(event.retrieveUser().complete()).queue();
				}
			}
		}
    }
}
