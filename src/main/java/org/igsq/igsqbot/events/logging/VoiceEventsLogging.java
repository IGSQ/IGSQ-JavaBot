package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.StringUtils;

public class VoiceEventsLogging extends ListenerAdapter
{
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event)
	{
		final Guild guild = event.getGuild();
		final MessageChannel logChannel = new GuildConfig(guild, event.getJDA()).getLogChannel();
		final VoiceChannel oldChannel = event.getChannelLeft();
		final VoiceChannel newChannel = event.getChannelJoined();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator(logChannel)
					.title("Member Moved VC")
					.text("**Member**: " + member.getAsMention() + "\n" +
							"**Old Channel**: " + oldChannel.getName() + "\n" +
							"**New Channel**: " + newChannel.getName()
					)
					.color(Constants.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}


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
					.color(Constants.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}


	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
	{
		final Guild guild = event.getGuild();
		final MessageChannel logChannel = new GuildConfig(guild, event.getJDA()).getLogChannel();
		final VoiceChannel channel = event.getChannelJoined();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator(logChannel)
					.title("Member Joined VC")
					.text("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.color(Constants.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
