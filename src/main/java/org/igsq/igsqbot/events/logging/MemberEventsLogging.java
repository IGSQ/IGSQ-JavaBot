package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class MemberEventsLogging extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MemberEventsLogging(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		Guild guild = event.getGuild();
		User user = event.getUser();

		event.getGuild().retrieveMember(user).queue(member ->
		{
			String timeJoined = member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
			if(!user.isBot())
			{
				new EmbedBuilder()
						.setTitle("Member Left")
						.setDescription("**Member**: " + member.getAsMention() +
								"\n**Joined On**: " + timeJoined)
						.setColor(Constants.IGSQ_PURPLE)
						.setTimestamp(Instant.now())
						.build();
			}
		});
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		Guild guild = event.getGuild();
		Member member = event.getMember();
		User user = event.getUser();

		if(!user.isBot())
		{
			new EmbedBuilder()
					.setTitle("Member Joined")
					.setDescription("**Member**: " + member.getAsMention() +
							"\n**Joined On**: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build();
		}
	}
}
