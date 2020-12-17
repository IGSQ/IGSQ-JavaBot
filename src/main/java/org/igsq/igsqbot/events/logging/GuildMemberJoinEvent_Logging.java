package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.time.format.DateTimeFormatter;

public class GuildMemberJoinEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		GuildChannel logChannel = YamlUtils.getLogChannel(event.getGuild().getId());
		Member member = event.getMember();
		User user = event.getUser();
		
		if(logChannel != null && !user.isBot())
		{
			new EmbedGenerator((MessageChannel)logChannel)
					.title("Member Joined").text(
					"**Member**: " + member.getAsMention() +
					"\n**Joined On**: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}