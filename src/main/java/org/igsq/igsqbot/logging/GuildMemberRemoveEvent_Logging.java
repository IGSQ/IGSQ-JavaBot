package main.java.org.igsq.igsqbot.logging;

import main.java.org.igsq.igsqbot.handlers.ErrorHandler;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import main.java.org.igsq.igsqbot.util.StringUtils;
import main.java.org.igsq.igsqbot.util.YamlUtils;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.format.DateTimeFormatter;

public class GuildMemberRemoveEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		GuildChannel logChannel = YamlUtils.getLogChannel(event.getGuild().getId());
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
			new EmbedGenerator((MessageChannel) logChannel)
					.title("Member Left")
					.text(
					"**Member**: " + member.getAsMention() +
					"\n**Joined On**: " + timeJoined)
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}