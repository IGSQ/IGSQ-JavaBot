package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
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

	public static void startSync(ShardManager shardManager)
	{
		if(YamlUtils.isFieldEmpty("bot.server", Filename.CONFIG))
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync stopped due to no guild.");
		}
		else if(!Database.isOnline())
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync stopped due to no Database connectivity.");
		}
		else
		{
			guild = shardManager.getGuildById(Yaml.getFieldString("bot.server", Filename.CONFIG));
			if(guild == null)
			{
				TaskHandler.cancelTask("minecraftSync");
				LOGGER.warn("Minecraft sync stopped due to invalid guild.");
			}
			else
			{
				LOGGER.info("Guild link established with guild: " + guild.getName() + ", starting sync.");
			}
		}
	}

	public static void sync()
	{
		verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", Filename.GUILD));
		guild.loadMembers(selectedMember ->
		{
			if(!selectedMember.getUser().isBot() && (verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
			{
				String username = selectedMember.getUser().getAsTag();
				String nickname = selectedMember.getEffectiveName();
				String id = selectedMember.getId();
				String rank = getRank(selectedMember);

				int supporter = hasRole(Yaml.getFieldString("ranks.supporter", Filename.MINECRAFT).split(" "), selectedMember) ? 1 : 0;
				int birthday = hasRole(Yaml.getFieldString("ranks.birthday", Filename.MINECRAFT).split(" "), selectedMember) ? 1 : 0;
				int developer = hasRole(Yaml.getFieldString("ranks.developer", Filename.MINECRAFT).split(" "), selectedMember) ? 1 : 0;
				int founder = hasRole(Yaml.getFieldString("ranks.founder", Filename.MINECRAFT).split(" "), selectedMember) ? 1 : 0;
				// int retired = hasRole(Yaml.getFieldString("ranks.retired", Filename.MINECRAFT).split(" "), selectedMember)?1:0;
				int nitroboost = hasRole(Yaml.getFieldString("ranks.nitroboost", Filename.MINECRAFT).split(" "), selectedMember) ? 1 : 0;

				boolean userExists = Database.scalarCommand("SELECT COUNT(*) FROM discord_accounts WHERE id = '" + id + "';") > 0;

				if(userExists) //TODO: Make a method for this
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

	public static void clean()
	{
		ResultSet discord_accounts = Database.queryCommand("SELECT * FROM discord_accounts");
		if(discord_accounts == null)
		{
			TaskHandler.cancelTask("minecraftClean");
			LOGGER.warn("Minecraft cleaning stopped due to invalid discord_accounts table.");
		}
		else if(!Database.isOnline())
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync stopped due to no Database connectivity.");
		}
		else
		{
			try
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
									Database.updateCommand("DELETE FROM discord_accounts WHERE id = '" + currentId + "';");
								}
							},
							error ->
							{
								Database.updateCommand("DELETE FROM discord_2fa WHERE uuid = '" + CommonMinecraft.getUUIDFromID(currentId) + "';");
								Database.updateCommand("DELETE FROM discord_accounts WHERE id = '" + currentId + "';");
								Database.updateCommand("DELETE FROM linked_accounts WHERE id = '" + currentId + "';");
							}
					);
				}
			}
			catch(Exception exception)
			{
				new ErrorHandler(exception);
			}
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
		ranks.put(Yaml.getFieldString("ranks.default", Filename.MINECRAFT), "default");

		ranks.put(Yaml.getFieldString("ranks.rising", Filename.MINECRAFT), "rising");
		ranks.put(Yaml.getFieldString("ranks.flying", Filename.MINECRAFT), "flying");
		ranks.put(Yaml.getFieldString("ranks.soaring", Filename.MINECRAFT), "soaring");

		ranks.put(Yaml.getFieldString("ranks.epic", Filename.MINECRAFT), "epic");
		ranks.put(Yaml.getFieldString("ranks.epic2", Filename.MINECRAFT), "epic2");
		ranks.put(Yaml.getFieldString("ranks.epic3", Filename.MINECRAFT), "epic3");

		ranks.put(Yaml.getFieldString("ranks.elite", Filename.MINECRAFT), "elite");
		ranks.put(Yaml.getFieldString("ranks.elite2", Filename.MINECRAFT), "elite2");
		ranks.put(Yaml.getFieldString("ranks.elite3", Filename.MINECRAFT), "elite3");

		ranks.put(Yaml.getFieldString("ranks.mod", Filename.MINECRAFT), "mod");
		ranks.put(Yaml.getFieldString("ranks.mod2", Filename.MINECRAFT), "mod2");
		ranks.put(Yaml.getFieldString("ranks.mod3", Filename.MINECRAFT), "mod3");

		ranks.put(Yaml.getFieldString("ranks.council", Filename.MINECRAFT), "council");
	}
}
