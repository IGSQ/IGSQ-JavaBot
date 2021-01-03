package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.MinecraftConfig;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinecraftUtils
{
	private MinecraftUtils()
	{
		//Overrides the default, public constructor
	}

	public static String getIDFromUUID(String uuid, IGSQBot igsqBot)
	{
		ResultSet linked_accounts = igsqBot.getDatabase().queryCommand("SELECT id FROM linked_accounts WHERE uuid = '" + uuid + "';");
		try
		{
			if(linked_accounts.next())
			{
				return linked_accounts.getString(1);
			}
		}
		catch(SQLException exception)
		{
			return null;
		}
		return null;
	}

	public static String getNameFromUUID(String uuid, IGSQBot igsqBot)
	{
		ResultSet mc_accounts = igsqBot.getDatabase().queryCommand("SELECT username FROM mc_accounts WHERE uuid = '" + uuid + "';");
		try
		{
			if(mc_accounts.next())
			{
				return mc_accounts.getString(1);
			}
		}
		catch(SQLException exception)
		{
			return null;
		}
		return null;
	}

	public static String getUUIDFromName(String name, IGSQBot igsqBot)
	{
		ResultSet mc_accounts = igsqBot.getDatabase().queryCommand("SELECT uuid FROM mc_accounts WHERE username = '" + name + "';");
		try
		{
			if(mc_accounts.next())
			{
				return mc_accounts.getString(1);
			}
		}
		catch(SQLException exception)
		{
			return null;
		}
		return null;
	}

	public static String getUUIDFromID(String id, IGSQBot igsqBot)
	{
		ResultSet linked_accounts = igsqBot.getDatabase().queryCommand("SELECT uuid FROM linked_accounts WHERE id = '" + id + "';");
		try
		{
			if(linked_accounts.next())
			{
				return linked_accounts.getString(1);
			}
		}
		catch(SQLException exception)
		{
			return null;
		}
		return null;
	}

	public static Map<String, String> fetchLinks(String discordId, IGSQBot igsqBot)
	{
		 Map<String, String> result = new HashMap<>();
		 ResultSet linked_accounts = igsqBot.getDatabase().queryCommand("SELECT * FROM linked_accounts WHERE id = '" + discordId + "';");

		try
		{
			while(linked_accounts.next())
			{
				result.putIfAbsent(getNameFromUUID(linked_accounts.getString(2), igsqBot), linked_accounts.getString(4));
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return result;
	}

	public static void removeLinkedAccount(String uuid, IGSQBot igsqBot)
	{
		igsqBot.getDatabase().updateCommand("DELETE FROM linked_accounts WHERE uuid = '" + uuid + "';");
	}

	public static void updateUser(String id, String username, String nickname, String rank
			, int supporter, int birthday, int developer, int founder, int nitroboost, IGSQBot igsqBot)
	{
		igsqBot.getDatabase().updateCommand("UPDATE discord_accounts SET " + "username = '" + username + "', nickname = '" + nickname + "', role = '" + rank + "', founder = " + founder + ", developer = " + developer + ", birthday = " + birthday + ", supporter = " + supporter + ", nitroboost = " + nitroboost + " WHERE id = '" + id + "';");
	}

	public static void addNewUser(String id, String username, String nickname, String rank
			, int supporter, int birthday, int developer, int founder, int nitroboost, IGSQBot igsqBot)
	{
		igsqBot.getDatabase().updateCommand("INSERT INTO discord_accounts VALUES('" + id + "','" + username + "','" + nickname + "','" + rank + "'," + founder + "," + developer + "," + birthday + "," + supporter + "," + nitroboost + ");");
	}

	public static boolean hasRole(String roleID, Member member)
	{
		for(Role selectedRole : member.getRoles())
		{
			if(selectedRole.getId().equals(roleID))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean hasRole(List<String> roles, Member member)
	{
		for(String selectedRole : roles)
		{
			if(hasRole(selectedRole, member)) return true;
		}
		return false;
	}

	public static String getRank(Member member)
	{
		MinecraftConfig json = Json.get(MinecraftConfig.class, Filename.MINECRAFT);
		if(json != null)
		{
			Map<List<String>, String> ranks = json.getRanks();
			for(Role selectedRole : member.getRoles())
			{
				for(Map.Entry<List<String>, String> selectedRanks : ranks.entrySet())
				{
					for(String selectedRank : selectedRanks.getKey())
					{
						if(selectedRole.getId().equals(selectedRank))
						{
							return selectedRanks.getValue();
						}
					}
				}
			}
		}
		else
		{
			return "default";
		}
		return "default";
	}
}
