package org.igsq.igsqbot.minecraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberRemoveEvent_Minecraft extends ListenerAdapter
{
	public GuildMemberRemoveEvent_Minecraft()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		Member member = event.getMember();
		String id = member.getId();
		ResultSet linked_accounts = Database.QueryCommand("SELECT uuid from linked_accounts WHERE id = '" + id + "';");
		try 
		{
			if(linked_accounts.next())
			{
				Database.UpdateCommand("DELETE FROM discord_2fa WHERE id = '" + linked_accounts.getString(1) + "';");
				Database.UpdateCommand("DELETE FROM linked_accounts WHERE id = '" + id + "';");
			}
		} 
		catch (SQLException exception) 
		{
		}
		finally
		{
			Database.UpdateCommand("DELETE FROM discord_accounts WHERE id = '" + id + "';");
		}
	}
}
