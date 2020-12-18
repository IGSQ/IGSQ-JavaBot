package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;

public class GuildVoiceLeaveEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
	{
		final MessageChannel logChannel = new GuildConfig(event.getGuild(), event.getJDA()).getLogChannel();
		final VoiceChannel channel = event.getChannelLeft();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator(logChannel)
					.title("Member Left VC")
					.text("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
