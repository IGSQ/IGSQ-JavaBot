package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.StringUtils;

import java.time.format.DateTimeFormatter;

public class MemberEventsLogging extends ListenerAdapter
{
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		final MessageChannel logChannel = new GuildConfig(event.getGuild(), event.getJDA()).getLogChannel();
		final User user = event.getUser();

		event.getGuild().retrieveMember(user).queue(member ->
		{
			final String timeJoined = member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
			if(logChannel != null && !user.isBot())
			{
				new EmbedGenerator(logChannel)
						.title("Member Left")
						.text(
								"**Member**: " + member.getAsMention() +
										"\n**Joined On**: " + timeJoined)
						.color(Constants.IGSQ_PURPLE)
						.footer("Logged on: " + StringUtils.getTimestamp())
						.send();
			}
		});
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		final MessageChannel logChannel = new GuildConfig(event.getGuild(), event.getJDA()).getLogChannel();
		Member member = event.getMember();
		User user = event.getUser();

		if(logChannel != null && !user.isBot())
		{
			new EmbedGenerator(logChannel)
					.title("Member Joined").text(
					"**Member**: " + member.getAsMention() +
							"\n**Joined On**: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
					.color(Constants.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
