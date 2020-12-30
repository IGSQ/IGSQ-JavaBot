package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.JsonBotConfig;
import org.igsq.igsqbot.entities.json.JsonMinecraft;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class SyncMinecraft
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncMinecraft.class);
	private static Guild guild;
	private static Role verifiedRole = null;


	private SyncMinecraft()
	{
		//Overriding the default, public, constructor
	}

	public static void startSync(ShardManager shardManager)
	{
		JsonBotConfig jsonBotConfig = Json.get(JsonBotConfig.class, Filename.CONFIG);
		if(jsonBotConfig == null)
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync stopped due JSON error.");
		}
		else if(jsonBotConfig.getServer().equals(new JsonBotConfig().getServer()))
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync stopped due to no guild being defined in CONFIG.json.");
		}
		else if(!Database.isOnline())
		{
			TaskHandler.cancelTask("minecraftSync");
			LOGGER.warn("Minecraft sync task stopped due to no Database connectivity.");
		}
		else
		{
			guild = shardManager.getGuildById(jsonBotConfig.getServer());
			if(guild == null)
			{
				TaskHandler.cancelTask("minecraftSync");
				LOGGER.warn("Minecraft sync stopped due to invalid guild being defined in CONFIG.json.");
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
				JsonMinecraft json = Json.get(JsonMinecraft.class, Filename.MINECRAFT);
				if(json != null)
				{
					String username = selectedMember.getUser().getAsTag();
					String nickname = selectedMember.getEffectiveName();
					String id = selectedMember.getId();
					String rank = getRank(selectedMember);

					int supporter = hasRole(json.getSupporter(), selectedMember) ? 1 : 0;
					int birthday = hasRole(json.getBirthday(), selectedMember) ? 1 : 0;
					int developer = hasRole(json.getDeveloper(), selectedMember) ? 1 : 0;
					int founder = hasRole(json.getFounder(), selectedMember) ? 1 : 0;
					// int retired = hasRole(json.getRetired(), selectedMember)?1:0;
					int nitroboost = hasRole(json.getNitroboost(), selectedMember) ? 1 : 0;

					boolean userExists = Database.scalarCommand("SELECT COUNT(*) FROM discord_accounts WHERE id = '" + id + "';") > 0;

					if(userExists)
					{
						CommonMinecraft.updateUser(id, username, nickname, rank, supporter, birthday, developer, founder, nitroboost);
					}
					else
					{
						CommonMinecraft.addNewUser(id, username, nickname, rank, supporter, birthday, developer, founder, nitroboost);
					}
				}
			}
		});
	}

	public static void clean()
	{
		if(!Database.isOnline())
		{
			TaskHandler.cancelTask("minecraftClean");
			LOGGER.warn("Minecraft clean task stopped due to no Database connectivity.");
		}
		ResultSet discord_accounts = Database.queryCommand("SELECT * FROM discord_accounts");
		if(discord_accounts == null)
		{
			TaskHandler.cancelTask("minecraftClean");
			LOGGER.warn("Minecraft cleaning stopped due to invalid discord_accounts table.");
		}
		else if(guild == null)
		{
			TaskHandler.cancelTask("minecraftClean");
			LOGGER.warn("Minecraft cleaning stopped due to null guild.");
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

	private static boolean hasRole(List<String> roles, Member member)
	{
		for(String selectedRole : roles)
		{
			if(hasRole(selectedRole, member)) return true;
		}
		return false;
	}

	private static String getRank(Member member)
	{
		JsonMinecraft json = Json.get(JsonMinecraft.class, Filename.MINECRAFT);
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
