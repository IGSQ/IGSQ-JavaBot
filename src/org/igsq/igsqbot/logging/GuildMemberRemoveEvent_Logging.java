package org.igsq.igsqbot.logging;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.handlers.ErrorHandler;

public class GuildMemberRemoveEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		GuildChannel logChannel = Common.getLogChannel(event.getGuild().getId());
		User user = event.getUser();
		String timeJoined;
		Member member = null;
		try
		{
			member = event.getGuild().retrieveMember(user).submit().get();
		}
		catch (Exception exception)
		{
			new ErrorHandler(exception);
			return;
		}

		if(member.hasTimeJoined())
		{
			timeJoined = member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		else
		{
			timeJoined = event.getGuild().retrieveMemberById(member.getId()).complete().getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		if(logChannel != null && !user.isBot())
		{
			new EmbedGenerator((MessageChannel)logChannel).title("Member Left").text(
			"**Member**: " + member.getAsMention() + 
			"\n**Joined On**: " + timeJoined)
			.color(Color.PINK).footer("Logged on: " + Common.getTimestamp()).send();
		}
	}
}