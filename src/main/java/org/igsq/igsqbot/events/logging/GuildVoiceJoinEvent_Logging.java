package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.StringUtils;


public class GuildVoiceJoinEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
	{
		final Guild guild = event.getGuild();
		final MessageChannel logChannel = new GuildConfig(guild, event.getJDA()).getLogChannel();
		final VoiceChannel channel = event.getChannelJoined();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator((MessageChannel) logChannel)
					.title("Member Joined VC")
					.text("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.color(Constants.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
