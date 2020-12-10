package org.igsq.igsqbot.events;

import java.awt.Color;

import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.ErrorHandler;
import org.igsq.igsqbot.util.Yaml;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Report extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		Message message = null;
		User user = null;
		String messageID = event.getMessageId();
		Guild guild = event.getGuild();
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
