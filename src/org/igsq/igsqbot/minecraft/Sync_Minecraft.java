package org.igsq.igsqbot.minecraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Sync_Minecraft 
{
	private static final Map<String, String> ranks = new Hashtable<String, String>();
	private static Guild guild = Common.jda.getGuildById(Yaml.getFieldString("BOT.server", "config"));
	private static Role verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", "guild"));
	
	public static void sync()
	{
		for(Member selectedMember : guild.loadMembers().get())
		{
			if(!selectedMember.getUser().isBot() && (verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
			{
				String username = selectedMember.getUser().getAsTag();
				String nickname = selectedMember.getEffectiveName();
				String id = selectedMember.getId();
				String rank = getRank(selectedMember);
				
				int supporter = hasRole(Yaml.getFieldString("ranks.supporter", "minecraft").split(" "), selectedMember) ?1:0;
				int birthday = hasRole(Yaml.getFieldString("ranks.birthday", "minecraft").split(" "), selectedMember)?1:0;
				int developer = hasRole(Yaml.getFieldString("ranks.default", "minecraft").split(" "), selectedMember) ?1:0;
				int founder = hasRole(Yaml.getFieldString("ranks.founder", "minecraft").split(" "), selectedMember)?1:0;
				// int retired = hasRole(Yaml.getFieldString("ranks.retired", "minecraft").split(" "), selectedMember)?1:0;
				int nitroboost = hasRole(Yaml.getFieldString("ranks.nitroboost", "minecraft").split(" "), selectedMember)?1:0;
				
				boolean userExists = Database.ScalarCommand("SELECT COUNT(*) FROM discord_accounts WHERE id = '"+ id +"';") > 0;
				
				if(userExists)
				{
					Database.UpdateCommand("UPDATE discord_accounts SET " + "username = '" + username + "', nickname = '" + nickname + "', role = '" + rank + "', founder = " + founder + ", developer = " + developer + ", birthday = " + birthday + ", supporter = " + supporter + ", nitroboost = " + nitroboost + " WHERE id = '" + id + "';");
				}
				else
				{
					Database.UpdateCommand("INSERT INTO discord_accounts VALUES('" + id + "','" + username + "','" + nickname + "','" + rank + "'," + founder + "," + developer + "," + birthday + "," + supporter + "," + nitroboost + ");");
				}
			}
		}
	}
	
	public static void clean()
	{		
		ResultSet discord_accounts = Database.QueryCommand("SELECT * FROM discord_accounts");
		try 
		{
			while(discord_accounts.next())
			{
				Member selectedMember = guild.getMemberById(discord_accounts.getString(1));
				String id = selectedMember.getId();
				
				if(!guild.isMember(selectedMember.getUser()) || !(verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
				{
					ResultSet linked_accounts = Database.QueryCommand("SELECT uuid from linked_accounts WHERE id = '" + id + "';");
					try 
					{
						if(linked_accounts.next())
						{
							Database.UpdateCommand("DELETE FROM discord_2fa WHERE uuid = '" + linked_accounts.getString(1) + "';");
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
		} 
		catch (SQLException exception) 
		{
		}
	}
	
	private static boolean hasRole(String roleID, Member member)
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
	
	private static boolean hasRole(String[] roles, Member member)
	{
		for(String selectedRole : roles)
		{
			if(hasRole(selectedRole, member)) return true;
		}
		return false;
	}
	
	private static String getRank(Member member)
	{
		if(ranks.isEmpty())
		{
			setRanks();
		}
		
		for(Role selectedRole : member.getRoles())
		{
			for(String selectedRanks : ranks.keySet())
			{
				for(String selectedRank : selectedRanks.split(" "))
				{
					if(selectedRole.getId().equals(selectedRank))
					{
						return ranks.get(selectedRanks);
					}
				}
			}
		}
		return "default";
	}
	
	private static void setRanks()
	{
		ranks.put(Yaml.getFieldString("ranks.default", "minecraft"), "default");
		
		ranks.put(Yaml.getFieldString("ranks.rising", "minecraft"), "rising");
		ranks.put(Yaml.getFieldString("ranks.flying", "minecraft"), "flying");
		ranks.put(Yaml.getFieldString("ranks.soaring", "minecraft"), "soaring");
		
		ranks.put(Yaml.getFieldString("ranks.epic", "minecraft"), "epic");
		ranks.put(Yaml.getFieldString("ranks.epic2", "minecraft"), "epic2");
		ranks.put(Yaml.getFieldString("ranks.epic3", "minecraft"), "epic3");
		
		ranks.put(Yaml.getFieldString("ranks.elite", "minecraft"), "elite");
		ranks.put(Yaml.getFieldString("ranks.elite2", "minecraft"), "elite2");
		ranks.put(Yaml.getFieldString("ranks.elite3", "minecraft"), "elite3");
		
		ranks.put(Yaml.getFieldString("ranks.mod", "minecraft"), "mod");
		ranks.put(Yaml.getFieldString("ranks.mod2", "minecraft"), "mod2");
		ranks.put(Yaml.getFieldString("ranks.mod3", "minecraft"), "mod3");
		
		ranks.put(Yaml.getFieldString("ranks.council", "minecraft"), "council");
	}
}
