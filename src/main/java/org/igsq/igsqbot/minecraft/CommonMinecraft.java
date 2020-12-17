package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		catch (SQLException exception) 
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
		catch (SQLException exception) 
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
		catch (SQLException exception) 
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
		catch (SQLException exception) 
		{
			return null;
		}
		return null;
	}
}
