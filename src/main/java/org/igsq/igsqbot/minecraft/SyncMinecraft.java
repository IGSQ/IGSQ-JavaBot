package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SyncMinecraft
{
	private static final Map<String, String> ranks = new HashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncMinecraft.class);
	private static Guild guild;
	private static Role verifiedRole = null;


	private SyncMinecraft()
	{
		//Overriding the default, public, constructor
	}

	public static void startSync(JDA JDA)
	{
		guild = JDA.getGuildById(Yaml.getFieldString("bot.server", "config"));
	}

	public static void sync()
	{
		if(guild == null)
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync stopped due to invalid guild.");
		}
		else
		{
			verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", "guild"));
			guild.loadMembers(selectedMember ->
			{
				if(!selectedMember.getUser().isBot() && (verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
				{
					String username = selectedMember.getUser().getAsTag();
					String nickname = selectedMember.getEffectiveName();
					String id = selectedMember.getId();
					String rank = getRank(selectedMember);

					int supporter = hasRole(Yaml.getFieldString("ranks.supporter", "minecraft").split(" "), selectedMember) ? 1 : 0;
					int birthday = hasRole(Yaml.getFieldString("ranks.birthday", "minecraft").split(" "), selectedMember) ? 1 : 0;
					int developer = hasRole(Yaml.getFieldString("ranks.developer", "minecraft").split(" "), selectedMember) ? 1 : 0;
					int founder = hasRole(Yaml.getFieldString("ranks.founder", "minecraft").split(" "), selectedMember) ? 1 : 0;
					// int retired = hasRole(Yaml.getFieldString("ranks.retired", "minecraft").split(" "), selectedMember)?1:0;
					int nitroboost = hasRole(Yaml.getFieldString("ranks.nitroboost", "minecraft").split(" "), selectedMember) ? 1 : 0;

					boolean userExists = Database.scalarCommand("SELECT COUNT(*) FROM discord_accounts WHERE id = '" + id + "';") > 0;

					if(userExists)
					{
						Database.updateCommand("UPDATE discord_accounts SET " + "username = '" + username + "', nickname = '" + nickname + "', role = '" + rank + "', founder = " + founder + ", developer = " + developer + ", birthday = " + birthday + ", supporter = " + supporter + ", nitroboost = " + nitroboost + " WHERE id = '" + id + "';");
					}
					else
					{
						Database.updateCommand("INSERT INTO discord_accounts VALUES('" + id + "','" + username + "','" + nickname + "','" + rank + "'," + founder + "," + developer + "," + birthday + "," + supporter + "," + nitroboost + ");");
					}
				}
			});
		}
	}

	public static void clean()
	{
		ResultSet discord_accounts = Database.queryCommand("SELECT * FROM discord_accounts");
		if(discord_accounts == null)
		{
			TaskHandler.cancelTask("minecraftClean");
			LOGGER.warn("Minecraft cleaning stopped due to invalid discord_accounts table.");
			return;
		}
		try //TODO: REWRITE THIS
		{
			while(discord_accounts.next())
			{
				final String currentId = discord_accounts.getString(1);
				guild.retrieveMemberById(currentId).queue(
						selectedMember ->
						{
							final String memberId = selectedMember.getId();

							if(!(verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
							{
								String uuid = CommonMinecraft.getUUIDFromID(memberId);

								if(uuid != null)
								{
									Database.updateCommand("DELETE FROM discord_2fa WHERE uuid = '" + uuid + "';");
									Database.updateCommand("DELETE FROM linked_accounts WHERE id = '" + memberId + "';");
								}


							}
						}, error -> Database.updateCommand("DELETE FROM discord_accounts WHERE id = '" + currentId + "';")
				);
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
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
			for(Map.Entry<String, String> selectedRanks : ranks.entrySet())
			{
				for(String selectedRank : selectedRanks.getKey().split(" "))
				{
					if(selectedRole.getId().equals(selectedRank))
					{
						return selectedRanks.getValue();
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
