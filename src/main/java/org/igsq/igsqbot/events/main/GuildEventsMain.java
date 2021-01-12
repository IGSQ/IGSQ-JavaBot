package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;

public class GuildEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public GuildEventsMain(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		//TODO: this
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		//TODO: this
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		//TODO: this
	}
}

