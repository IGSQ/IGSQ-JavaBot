package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.entities.yaml.GuildConfig;

public class UnavailableGuildLeaveEvent_Main extends ListenerAdapter
{
	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		new GuildConfig(event.getGuildId(), event.getJDA()).wipe();
	}
}
