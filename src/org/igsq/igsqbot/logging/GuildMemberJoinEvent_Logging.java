package org.igsq.igsqbot.logging;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoinEvent_Logging extends ListenerAdapter
{
	public GuildMemberJoinEvent_Logging()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		GuildChannel logChannel = Common.fetchLogChannel(event.getGuild().getId());
		Member member = event.getMember();
		User user = event.getUser();
		String timeJoined;
		
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