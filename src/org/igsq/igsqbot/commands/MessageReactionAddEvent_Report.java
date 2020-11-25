package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Report extends ListenerAdapter 
{
	public MessageReactionAddEvent_Report()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		Message message = event.retrieveMessage().complete();
		String messageID = event.getMessageId();
		User user = event.retrieveUser().complete();
		Guild guild = event.getGuild();
		
		if(Yaml.getFieldBool(messageID + ".report.enabled", "internal") && !user.isBot())
		{
			Member reportedUser = guild.retrieveMemberById(Yaml.getFieldString(messageID + ".report.reporteduser", "internal")).complete();
			// Member reportingUser = event.getGuild().getMemberById(Yaml.getFieldInt(messageID + ".report.reportinguser", "internal"));
			MessageEmbed embed = message.getEmbeds().get(0);
			if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705") && event.getMember().canInteract(reportedUser))
			{
				new EmbedGenerator(event.getChannel(), embed).footer("This was dealt with by " + event.getUser().getAsTag()).color(Color.GREEN).replace(message, true);
				Yaml.updateField(messageID + ".report", "internal", null);
			}
			else
			{
				new EmbedGenerator(event.getChannel(), embed).color(Color.YELLOW).footer(event.getUser().getAsTag() + ", you are not higher than " + reportedUser.getRoles().get(0).getName() + ".").replace(message, 5000, embed);
				event.getReaction().removeReaction(user).queue();
			}
		}
    }
}
