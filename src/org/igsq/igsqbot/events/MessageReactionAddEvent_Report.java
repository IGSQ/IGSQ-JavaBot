package org.igsq.igsqbot.events;

import java.awt.Color;

import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.Yaml;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Report extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
	    final String messageID = event.getMessageId();
	    final Guild guild = event.getGuild();
	    final MessageChannel channel = event.getChannel();
		event.retrieveMessage().queue(
				message ->
				{
					event.retrieveUser().queue(
							user ->
							{
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
													new EmbedGenerator(embed).color(Color.YELLOW).footer(user.getAsTag() + ", you are not higher than " + reportedUser.getRoles().get(0).getName() + ".").replace(message, 5000, embed);
													event.getReaction().removeReaction(user).queue();
												}
											}
										);
									}
								}
							);
						}
					);
				}


		);
    }
}
