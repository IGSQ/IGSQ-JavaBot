package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.cache.GuildConfigCache;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;


public class GuildVoiceJoinEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
	{
		final Guild guild = event.getGuild();
		final MessageChannel logChannel = GuildConfigCache.getCache(guild, event.getJDA()).getLogChannel();
		final VoiceChannel channel = event.getChannelJoined();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator((MessageChannel) logChannel)
					.title("Member Joined VC")
					.text("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
