package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.entities.yaml.GuildConfig;

public class GuildLeave_Main extends ListenerAdapter
{
	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		new GuildConfig(event.getGuild(), event.getJDA()).wipe();
	}
}
