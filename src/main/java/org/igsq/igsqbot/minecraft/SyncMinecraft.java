package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.BotConfig;
import org.igsq.igsqbot.entities.json.MinecraftConfig;

import java.util.concurrent.TimeUnit;

public class SyncMinecraft
{
	private final IGSQBot igsqBot;
	private Guild guild;

	public SyncMinecraft(IGSQBot igsqBot)
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
			igsqBot.getLogger().warn("Minecraft sync task stopped due to no Database connectivity being found.");
			return;
		}

		guild = igsqBot.getShardManager().getGuildById(botConfig.getServer());
		if(guild == null)
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync", false);
			igsqBot.getLogger().warn("Minecraft sync stopped due to invalid guild being defined in CONFIG.json.");
		}
		else
		{
			igsqBot.getTaskHandler().addRepeatingTask(this::sync, "minecraftSync", 0, TimeUnit.SECONDS, 10);
		}
	}

	private void sync()
	{
		Role verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", Filename.GUILD));
		guild.loadMembers(selectedMember ->
		{
			if(!selectedMember.getUser().isBot() && (verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
			{
				MinecraftConfig json = Json.get(MinecraftConfig.class, Filename.MINECRAFT);
				if(json != null)
				{
					String username = selectedMember.getUser().getAsTag();
					String nickname = selectedMember.getEffectiveName();
					String id = selectedMember.getId();
					String rank = MinecraftUtils.getRank(selectedMember);

					int supporter = MinecraftUtils.hasRole(json.getSupporter(), selectedMember) ? 1 : 0;
					int birthday = MinecraftUtils.hasRole(json.getBirthday(), selectedMember) ? 1 : 0;
					int developer = MinecraftUtils.hasRole(json.getDeveloper(), selectedMember) ? 1 : 0;
					int founder = MinecraftUtils.hasRole(json.getFounder(), selectedMember) ? 1 : 0;
					// int retired = hasRole(json.getRetired(), selectedMember)?1:0;
					int nitroboost = MinecraftUtils.hasRole(json.getNitroboost(), selectedMember) ? 1 : 0;

					boolean userExists = igsqBot.getDatabase().scalarCommand("SELECT COUNT(*) FROM discord_accounts WHERE id = '" + id + "';") > 0;

					if(userExists)
					{
						MinecraftUtils.updateUser(id, username, nickname, rank, supporter, birthday, developer, founder, nitroboost, igsqBot);
					}
					else
					{
						MinecraftUtils.addNewUser(id, username, nickname, rank, supporter, birthday, developer, founder, nitroboost, igsqBot);
					}
				}
			}
		});
	}
}
