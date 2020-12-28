package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.jetbrains.annotations.NotNull;

public class GuildEventsMain extends ListenerAdapter
{
	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		new GuildConfig(event.getGuild(), event.getJDA()).wipe();
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		new GuildConfig(event.getGuildId(), event.getJDA()).wipe();
	}
}
