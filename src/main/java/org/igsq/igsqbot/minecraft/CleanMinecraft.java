package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.BotConfig;
import org.igsq.igsqbot.entities.json.GuildConfig;
import org.igsq.igsqbot.entities.cache.GuildConfigCache;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

public class CleanMinecraft
{
	private final IGSQBot igsqBot;
	private Guild guild;

	public CleanMinecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		start();
	}

	private void start()
	{
		BotConfig botConfig = Json.get(BotConfig.class, Filename.CONFIG);
		if(botConfig == null)
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync", false);
			igsqBot.getLogger().error("An error occurred while reading JSON for Minecraft.");
			return;
		}
		if(!igsqBot.getDatabase().isOnline())
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync", false);
			igsqBot.getLogger().warn("Minecraft clean task stopped due to no Database connectivity being found.");
			return;
		}

		guild = igsqBot.getShardManager().getGuildById(botConfig.getServer());
		if(guild == null)
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync", false);
			igsqBot.getLogger().warn("Minecraft clean stopped due to invalid guild being defined in CONFIG.json.");
		}
		else
		{
			igsqBot.getTaskHandler().addRepeatingTask(this::clean, "minecraftSync", 0, TimeUnit.SECONDS, 10);
		}
	}

	private void clean()
	{

		if(!igsqBot.getDatabase().isOnline())
		{
			igsqBot.getTaskHandler().cancelTask("minecraftClean", false);
			igsqBot.getLogger().warn("Minecraft clean task stopped due to no Database connectivity.");
		}
		GuildConfig guildConfig = GuildConfigCache.getInstance().get(guild.getId());
		Role verifiedRole = guild.getRoleById(guildConfig.getVerifiedRole());
		ResultSet discord_accounts = igsqBot.getDatabase().queryCommand("SELECT * FROM discord_accounts");
		if(discord_accounts == null)
		{
			igsqBot.getTaskHandler().cancelTask("minecraftClean", false);
			igsqBot.getLogger().warn("Minecraft clean task stopped due to invalid discord_accounts table.");
		}
		else
		{
			try
			{
				while(discord_accounts.next())
				{
					String currentId = discord_accounts.getString(1);
					guild.retrieveMemberById(currentId).queue(
							selectedMember ->
							{
								String memberId = selectedMember.getId();

								if(!(verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
								{
									String uuid = MinecraftUtils.getUUIDFromID(memberId, igsqBot);

									if(uuid != null)
									{
										igsqBot.getDatabase().updateCommand("DELETE FROM discord_2fa WHERE uuid = '" + uuid + "';");
										igsqBot.getDatabase().updateCommand("DELETE FROM linked_accounts WHERE id = '" + memberId + "';");
									}
									igsqBot.getDatabase().updateCommand("DELETE FROM discord_accounts WHERE id = '" + currentId + "';");
								}
							},
							error ->
							{
								igsqBot.getDatabase().updateCommand("DELETE FROM discord_2fa WHERE uuid = '" + MinecraftUtils.getUUIDFromID(currentId, igsqBot) + "';");
								igsqBot.getDatabase().updateCommand("DELETE FROM discord_accounts WHERE id = '" + currentId + "';");
								igsqBot.getDatabase().updateCommand("DELETE FROM linked_accounts WHERE id = '" + currentId + "';");
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
}
