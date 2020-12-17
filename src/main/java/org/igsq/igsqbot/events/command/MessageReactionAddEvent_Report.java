package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.awt.*;

public class MessageReactionAddEvent_Report extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
	    final String messageID = event.getMessageId();
	    final Guild guild = event.getGuild();
		event.retrieveMessage().queue(
		message ->
			event.retrieveUser().queue(
			user ->
				event.retrieveMember().queue(
					member ->
					{
						if(Yaml.getFieldBool(messageID + ".report.enabled", "internal") && !user.isBot())
						{
							guild.retrieveMemberById(Yaml.getFieldString(messageID + ".report.reporteduser", "internal")).queue(
								reportedUser ->
								{
									// Member reportingUser = event.getGuild().getMemberById(Yaml.getFieldInt(messageID + ".report.reportinguser", "internal"));
									MessageEmbed embed = message.getEmbeds().get(0);

									if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705") && member.canInteract(reportedUser))
									{
										new EmbedGenerator(embed).footer("This was dealt with by " + user.getAsTag()).color(Color.GREEN).replace(message, true);
										Yaml.updateField(messageID + ".report", "internal", null);
									}
									else
									{
										new EmbedGenerator(embed)
												.color(Color.YELLOW)
												.footer(user.getAsTag() + ", you are not higher than " + reportedUser.getRoles().get(0).getName() + ".")
												.replace(message, 5000, embed);
										event.getReaction().removeReaction(user).queue();
									}
								}
							);
						}
					}
				)
			)
		);
    }
}
