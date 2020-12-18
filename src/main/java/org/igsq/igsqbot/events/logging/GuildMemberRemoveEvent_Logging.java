package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.cache.GuildConfigCache;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;

import java.time.format.DateTimeFormatter;

public class GuildMemberRemoveEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		final MessageChannel logChannel = GuildConfigCache.getCache(event.getGuild(), event.getJDA()).getLogChannel();
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
						.color(EmbedUtils.IGSQ_PURPLE)
						.footer("Logged on: " + StringUtils.getTimestamp())
						.send();
			}
		});
	}
}