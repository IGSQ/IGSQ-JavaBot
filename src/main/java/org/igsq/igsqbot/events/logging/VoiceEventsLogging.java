package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.yaml.GuildConfig;

import java.time.Instant;

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
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Moved VC")
					.setDescription("**Member**: " + member.getAsMention() + "\n" +
							"**Old Channel**: " + oldChannel.getName() + "\n" +
							"**New Channel**: " + newChannel.getName())
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build()).queue();
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
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Left VC")
					.setDescription("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build()).queue();
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
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Joined VC")
					.setDescription("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}
}
