package org.igsq.igsqbot.minecraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.igsq.igsqbot.Database;

public class Common_Minecraft 
{
	public static String getIDFromUUID(String uuid)
	{
		ResultSet linked_accounts = Database.QueryCommand("SELECT id FROM linked_accounts WHERE uuid = '" + uuid + "';");
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
	
	public static String getUUIDFromID(String id)
	{
		ResultSet linked_accounts = Database.QueryCommand("SELECT uuid FROM linked_accounts WHERE id = '" + id + "';");
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
