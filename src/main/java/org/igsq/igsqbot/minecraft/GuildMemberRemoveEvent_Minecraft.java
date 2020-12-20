package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Database;

public class GuildMemberRemoveEvent_Minecraft extends ListenerAdapter
{
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		String id = event.getUser().getId();
		String uuid = CommonMinecraft.getUUIDFromID(id);
		if(uuid != null)
		{
			Database.updateCommand("DELETE FROM discord_2fa WHERE id = '" + uuid + "';");
			Database.updateCommand("DELETE FROM linked_accounts WHERE id = '" + id + "';");
		}
		Database.updateCommand("DELETE FROM discord_accounts WHERE id = '" + id + "';");
	}
}


