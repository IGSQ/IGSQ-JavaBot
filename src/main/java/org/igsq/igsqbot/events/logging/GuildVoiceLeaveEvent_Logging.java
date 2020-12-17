package org.igsq.igsqbot.events.logging;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

public class GuildVoiceLeaveEvent_Logging extends ListenerAdapter
{
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
	{
		final Guild guild = event.getGuild();
		final GuildChannel logChannel = YamlUtils.getVoidLogChannel(guild.getId());
		final VoiceChannel channel = event.getChannelLeft();
		final Member member = event.getMember();

		if(logChannel != null)
		{
			new EmbedGenerator((MessageChannel) logChannel)
					.title("Member Left VC")
					.text("**Member**: " + member.getAsMention() +  "\n" +
							"**Channel**: " + channel.getName())
					.color(EmbedUtils.IGSQ_PURPLE)
					.footer("Logged on: " + StringUtils.getTimestamp())
					.send();
		}
	}
}
