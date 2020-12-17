package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

public class GuildVoiceMoveEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event)
	{
		final Guild guild = event.getGuild();
		final GuildChannel logChannel = YamlUtils.getVoidLogChannel(guild.getId());
		final VoiceChannel oldChannel = event.getChannelLeft();
		final VoiceChannel newChannel = event.getChannelJoined();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator((MessageChannel) logChannel)
					.title("Member Moved VC")
					.text("**Member**: " + member.getAsMention() +  "\n" +
							"**Old Channel**: " + oldChannel.getName() + "\n" +
							"**New Channel**: " + newChannel.getName()
					)
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}

