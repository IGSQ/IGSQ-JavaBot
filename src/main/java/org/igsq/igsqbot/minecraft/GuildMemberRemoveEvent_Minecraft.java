package org.igsq.igsqbot.minecraft;

import main.java.org.igsq.igsqbot.Database;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberRemoveEvent_Minecraft extends ListenerAdapter
{	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		Member member = event.getMember();

		if(member != null)
		{
			String id = member.getId();
			String uuid = Common_Minecraft.getUUIDFromID(id);
			if(uuid != null)
			{
				Database.updateCommand("DELETE FROM discord_2fa WHERE id = '" + uuid + "';");
				Database.updateCommand("DELETE FROM linked_accounts WHERE id = '" + id + "';");
			}
			Database.updateCommand("DELETE FROM discord_accounts WHERE id = '" + id + "';");
		}
	}
}

