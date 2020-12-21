package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonMinecraft
{
	private CommonMinecraft()
	{
		//Overrides the default, public constructor
	}

	public static String getIDFromUUID(String uuid)
	{
		ResultSet linked_accounts = Database.queryCommand("SELECT id FROM linked_accounts WHERE uuid = '" + uuid + "';");
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

	public static String getNameFromUUID(String uuid)
	{
		ResultSet mc_accounts = Database.queryCommand("SELECT username FROM mc_accounts WHERE uuid = '" + uuid + "';");
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

	public static String getUUIDFromName(String name)
	{
		ResultSet mc_accounts = Database.queryCommand("SELECT uuid FROM mc_accounts WHERE username = '" + name + "';");
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

	public static String getUUIDFromID(String id)
	{
		ResultSet linked_accounts = Database.queryCommand("SELECT uuid FROM linked_accounts WHERE id = '" + id + "';");
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

	public static Map<String, String> fetchLinks(String discordId)
	{
		final Map<String, String> result = new ConcurrentHashMap<>();
		final ResultSet linked_accounts = Database.queryCommand("SELECT * FROM linked_accounts WHERE id = '" + discordId + "';");

		try
		{
			while(linked_accounts.next())
			{
				result.putIfAbsent(getNameFromUUID(linked_accounts.getString(2)), linked_accounts.getString(4));
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return result;
	}

	public static void removeLinkedAccount(String uuid)
	{
		Database.updateCommand("DELETE FROM linked_accounts WHERE uuid = '" + uuid + "';");
	}
}
