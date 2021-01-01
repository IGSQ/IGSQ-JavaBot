package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.minecraft.MinecraftUtils;

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
		new GuildConfig(event.getGuild(), event.getJDA()).wipe();
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		new GuildConfig(event.getGuildId(), event.getJDA()).wipe();
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		String id = event.getUser().getId();
		String uuid = MinecraftUtils.getUUIDFromID(id, igsqBot);
		if(uuid != null)
		{
			igsqBot.getDatabase().updateCommand("DELETE FROM discord_2fa WHERE id = '" + uuid + "';");
			igsqBot.getDatabase().updateCommand("DELETE FROM linked_accounts WHERE id = '" + id + "';");
		}
		igsqBot.getDatabase().updateCommand("DELETE FROM discord_accounts WHERE id = '" + id + "';");
	}
}

